package com.signpe.fourrshare;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.signpe.fourrshare.model.ImageDTO;

import java.util.ArrayList;

public class MyInfoActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    private ArrayList<ImageDTO> imageDTOs = new ArrayList<>();
    public ArrayList<String> imageUidList = new ArrayList<>();
    private FirebaseFirestore firestore;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
     CircularProgressIndicator circularProgressIndicator;
    ImageView profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
        mAuth = FirebaseAuth.getInstance();
        Button logout = findViewById(R.id.logout_button);
        firestore = FirebaseFirestore.getInstance();
        EditText nameEdit = findViewById(R.id.username);
        if (nameEdit.getText().toString() != null){
            nameEdit.setText(mAuth.getCurrentUser().getDisplayName());
        }

        circularProgressIndicator = findViewById(R.id.progress_circular);


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

         profile = findViewById(R.id.profile_image);
        Glide.with(getApplicationContext()).load(mAuth.getCurrentUser().getPhotoUrl()).into(profile);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                intent.setAction(Intent.ACTION_PICK);
                activityResultLauncher.launch(intent);
            }
        });
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK) {
                        circularProgressIndicator.setVisibility(View.VISIBLE);
                        Intent intent = result.getData();
                        Uri uri = intent.getData();
                        setProfileImage(uri);
                    }
                }
            }
    );

    private void setProfileImage(Uri uri){
        String fileName= mAuth.getCurrentUser().getUid()+".png";
        StorageReference ImageRef = storageRef.child("profile").child(mAuth.getCurrentUser().getUid()).child(fileName);
        UploadTask uploadTask = ImageRef.putFile(uri);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ImageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setPhotoUri(uri).build();
                        mAuth.getCurrentUser().updateProfile(profileChangeRequest).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Glide.with(getApplicationContext()).load(mAuth.getCurrentUser().getPhotoUrl()).placeholder(profile.getDrawable()).addListener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                                        circularProgressIndicator.setVisibility(View.INVISIBLE);
                                        circularProgressIndicator.setProgressCompat(100,true);
                                        Handler handler =new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                AlphaAnimation anim = new AlphaAnimation(1, 0);
                                                anim.setDuration(1000);
                                                circularProgressIndicator.setAnimation(anim);
                                                circularProgressIndicator.startAnimation(anim);
                                                anim.setAnimationListener(new Animation.AnimationListener() {
                                                    @Override
                                                    public void onAnimationStart(Animation animation) {

                                                    }

                                                    @Override
                                                    public void onAnimationEnd(Animation animation) {
                                                        circularProgressIndicator.setVisibility(View.INVISIBLE);
                                                    }

                                                    @Override
                                                    public void onAnimationRepeat(Animation animation) {

                                                    }
                                                });
                                            }
                                        },2500);

                                        return false;
                                    }
                                }).into(profile);
                            }
                        });
                    }
                });
            }
        });


    }

    // 이름 변경 후 저장 버튼 누르기 ( 일단 코드는 완성 실행은 아직 X )
    public void onClickChangeName(View v){
        EditText nameEdit = (EditText)findViewById(R.id.username);
        InputMethodManager imm;
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(nameEdit.getWindowToken(), 0);
        String name = nameEdit.getText().toString();
        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
        mAuth.getCurrentUser().updateProfile(profileChangeRequest).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(MyInfoActivity.this, "이름 변경 완료!", Toast.LENGTH_SHORT).show();
            }
        });
        nameEdit.setText(nameEdit.getText());


        firestore = FirebaseFirestore.getInstance();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firestore.collection("images").whereEqualTo("uid",uid).orderBy("timeStamp").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots==null)
                            return;
                        try{
                            for(DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                                ImageDTO item = snapshot.toObject(ImageDTO.class);
                                imageDTOs.add(item);
                                imageUidList.add(snapshot.getId());
                            }
                            for(String imageUid : imageUidList){
                                final DocumentReference sfDocRef = firestore.collection("images").document(imageUid);
                                firestore.runTransaction(new Transaction.Function<Void>() {
                                    @Nullable
                                    @Override
                                    public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                                        String newNickname = nameEdit.getText().toString();
                                        transaction.update(sfDocRef,"userNickname",newNickname);
                                        return null;
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(MyInfoActivity.this, "failed", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        }
                        catch (Exception e){

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MyInfoActivity.this, "fail to load", Toast.LENGTH_SHORT).show();
                    }
                });



    }

    // 네비게이션 바
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