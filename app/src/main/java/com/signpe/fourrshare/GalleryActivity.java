package com.signpe.fourrshare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;
import com.signpe.fourrshare.model.ImageDTO;

import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class GalleryActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    LinearProgressIndicator linearProgressIndicator;
    private static final String TAG = "MultiImageActivity";
    private RecyclerView.LayoutManager LayoutManager;
    ArrayList<Uri> uriList = new ArrayList<>();     // ???????????? uri??? ?????? ArrayList ??????
    ArrayList<ImageDTO> imageDTOS = new ArrayList<>();
    RecyclerView recyclerView;  // ???????????? ????????? ??????????????????
    MultiImageAdapter adapter;  // ????????????????????? ???????????? ?????????


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
         linearProgressIndicator = findViewById(R.id.linear_prog);
        Intent intent = getIntent();

//        Toast.makeText(this, intent.getStringExtra("urls"), Toast.LENGTH_SHORT).show();
        if (!TextUtils.isEmpty(intent.getStringExtra("urls"))){
            linearProgressIndicator.setVisibility(View.VISIBLE);
            Glide.with(getApplicationContext()).asBitmap().load(intent.getStringExtra("urls")).into(new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    resource.compress(Bitmap.CompressFormat.JPEG,100,baos);
                    byte[] data= baos.toByteArray();
                    contentUpLoad(data);
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {

                }
            });
        }

        // ???????????? ???????????? ??????
        Button btn_getImage = findViewById(R.id.getImage);
        btn_getImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2222);
            }
        });
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        adapter = new MultiImageAdapter(GalleryActivity.this,imageDTOS);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(LayoutManager);

    }

    // ???????????? ??????????????? ????????? ??? ???????????? ?????????
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        linearProgressIndicator.setProgressCompat(0,true);
        linearProgressIndicator.setVisibility(View.VISIBLE);
        uriList.clear();
        if(data == null){   // ?????? ???????????? ???????????? ?????? ??????
            DynamicToast.makeWarning(GalleryActivity.this,"???????????? ???????????? ???????????????.").show();
        }
        else{   // ???????????? ???????????? ????????? ??????
                ClipData clipData = data.getClipData();
                Log.e("clipData", String.valueOf(clipData.getItemCount()));

                if(clipData.getItemCount() > 10){   // ????????? ???????????? 11??? ????????? ??????
                    DynamicToast.makeError(GalleryActivity.this,"????????? 10????????? ?????? ???????????????.").show();
                }
                else{   // ????????? ???????????? 1??? ?????? 10??? ????????? ??????
                    Log.e(TAG, "multiple choice");

                    for (int i = 0; i < clipData.getItemCount(); i++){
                        Uri imageUri = clipData.getItemAt(i).getUri();  // ????????? ??????????????? uri??? ????????????.
                        try {
                            uriList.add(imageUri);  //uri??? list??? ?????????.

                        } catch (Exception e) {
                            Log.e(TAG, "File select error", e);
                        }
                    }
                    contentUpLoad();
                      // ?????????????????? ?????? ????????? ??????
                }
            }

    }


    private void contentUpLoad(){ // ?????? ???????????? ????????? ?????? ??????????????? ????????? ???????????????
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        int count=0;
        int inner_cnt=0;
        int temp_dto_size = adapter.imageDTOs.size();
        for(Uri uri : uriList) {
            String fileName = "user_" + sdf.format(timestamp) + count + "_.png";
            count++;
            inner_cnt++;
            StorageReference ImageRef = storageRef.child("images").child(currentUser.getUid()).child(fileName);
            UploadTask uploadTask = ImageRef.putFile(uri);
            int finalInner_cnt = inner_cnt;
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String img_uri = uri.toString();
                            ImageDTO dto = new ImageDTO();
                            dto.setImageUri(img_uri);
                            dto.setUid(currentUser.getUid());
                            dto.setUserId(currentUser.getEmail());
                            dto.setUserNickname(currentUser.getDisplayName());
                            dto.setTimeStamp(sdf.format(timestamp));
                            dto.setUploadTimeStamp(sdf.format(timestamp));
                            dto.setLikeCount(0);
                            dto.setIsUpload(false);
                            adapter.imageDTOs.add(dto);
                            if (uriList.size() == finalInner_cnt) {
                                adapter.notifyItemRangeInserted(temp_dto_size, uriList.size());
                            }

                             db.collection("images").add(dto).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                 @Override
                                 public void onSuccess(DocumentReference documentReference) {
                                     adapter.imageUidList.add(documentReference.getId());
                                     linearProgressIndicator.setProgressCompat(100*finalInner_cnt/uriList.size(),true);
                                     if (uriList.size() == finalInner_cnt) {
                                         Handler handler =new Handler();
                                         handler.postDelayed(new Runnable() {
                                             @Override
                                             public void run() {
                                                 AlphaAnimation anim = new AlphaAnimation(1, 0);
                                                 anim.setDuration(1000);
                                                 linearProgressIndicator.setAnimation(anim);
                                                 linearProgressIndicator.startAnimation(anim);
                                                 anim.setAnimationListener(new Animation.AnimationListener() {
                                                     @Override
                                                     public void onAnimationStart(Animation animation) {

                                                     }

                                                     @Override
                                                     public void onAnimationEnd(Animation animation) {
                                                         linearProgressIndicator.setVisibility(View.INVISIBLE);
                                                     }

                                                     @Override
                                                     public void onAnimationRepeat(Animation animation) {

                                                     }
                                                 });
                                             }
                                         },1000);
                                     }
                                 }
                             }).addOnFailureListener(new OnFailureListener() {
                                 @Override
                                 public void onFailure(@NonNull Exception e) {
                                     DynamicToast.makeError(GalleryActivity.this,"????????? ??????").show();
                                 }
                             });
                        }
                    });
                }
            });
        }

    }

        private void contentUpLoad(byte[] data){ // for qr scan
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String fileName = "user_" + sdf.format(timestamp) + 1 + "_.png";
                StorageReference ImageRef = storageRef.child("images").child(currentUser.getUid()).child(fileName);

                UploadTask uploadTask = ImageRef.putBytes(data);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String img_uri = uri.toString();
                                ImageDTO dto = new ImageDTO();
                                dto.setImageUri(img_uri);
                                dto.setUid(currentUser.getUid());
                                dto.setUserId(currentUser.getEmail());
                                dto.setUserNickname(currentUser.getDisplayName());
                                dto.setTimeStamp(sdf.format(timestamp));
                                dto.setLikeCount(0);
                                dto.setIsUpload(false);
                                adapter.imageDTOs.add(dto);
                                adapter.notifyItemInserted(adapter.imageDTOs.size()-1);
                                adapter.notifyItemRangeChanged(adapter.imageDTOs.size()-1,adapter.imageDTOs.size());
                                db.collection("images").add(dto).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        adapter.imageUidList.add(documentReference.getId());
                                        linearProgressIndicator.setProgressCompat(100,true);
                                        Handler handler =new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                AlphaAnimation anim = new AlphaAnimation(1, 0);
                                                anim.setDuration(1000);
                                                linearProgressIndicator.setAnimation(anim);
                                                linearProgressIndicator.startAnimation(anim);
                                                anim.setAnimationListener(new Animation.AnimationListener() {
                                                    @Override
                                                    public void onAnimationStart(Animation animation) {

                                                    }

                                                    @Override
                                                    public void onAnimationEnd(Animation animation) {
                                                        linearProgressIndicator.setVisibility(View.INVISIBLE);
                                                    }

                                                    @Override
                                                    public void onAnimationRepeat(Animation animation) {

                                                    }
                                                });
                                            }
                                        },1000);


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        DynamicToast.makeError(GalleryActivity.this,"????????? ??????").show();

                                    }
                                });
                            }
                        });
    //                Toast.makeText(MainActivity.this, "succes!!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    // ??????????????? ???
    public void onClickNavigationBar(View v){
        if(v.getId() == R.id.galleryClickButton){

            startActivity(new Intent(getApplicationContext(),GalleryActivity.class));
            finish();
            overridePendingTransition(0,0);
        }
        else if(v.getId() == R.id.scanClickButton){
            startActivity(new Intent(getApplicationContext(),ScanActivity.class));
            finish();
            overridePendingTransition(0,0);
        }
        else if(v.getId() == R.id.rankClickButton){

            startActivity(new Intent(getApplicationContext(),RankActivity.class));
            finish();
            overridePendingTransition(0,0);
        }
        else if(v.getId() == R.id.scrapClickButton){
            startActivity(new Intent(getApplicationContext(),ScrapActivity.class));
            finish();
            overridePendingTransition(0,0);
        }
        else if(v.getId() == R.id.myinfoClickButton){
            startActivity(new Intent(getApplicationContext(),MyInfoActivity.class));
            finish();
            overridePendingTransition(0,0);
        }
    }

}