package com.signpe.fourrshare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.signpe.fourrshare.model.ImageDTO;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    private static final String TAG = "MultiImageActivity";
    private RecyclerView.Adapter Adapter;
    private RecyclerView.LayoutManager LayoutManager;
    ArrayList<Uri> uriList = new ArrayList<>();     // 이미지의 uri를 담을 ArrayList 객체
    ArrayList<ImageDTO> imageDTOS = new ArrayList<>();
    RecyclerView recyclerView;  // 이미지를 보여줄 리사이클러뷰
    MultiImageAdapter adapter;  // 리사이클러뷰에 적용시킬 어댑터


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        // 앨범으로 이동하는 버튼
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
        adapter = new MultiImageAdapter(getApplicationContext(),imageDTOS);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(LayoutManager);

    }

    // 앨범에서 액티비티로 돌아온 후 실행되는 메서드
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data == null){   // 어떤 이미지도 선택하지 않은 경우
            Toast.makeText(getApplicationContext(), "이미지를 선택하지 않았습니다.", Toast.LENGTH_LONG).show();
        }
        else{   // 이미지를 하나라도 선택한 경우
                ClipData clipData = data.getClipData();
                Log.e("clipData", String.valueOf(clipData.getItemCount()));

                if(clipData.getItemCount() > 10){   // 선택한 이미지가 11장 이상인 경우
                    Toast.makeText(getApplicationContext(), "사진은 10장까지 선택 가능합니다.", Toast.LENGTH_LONG).show();
                }
                else{   // 선택한 이미지가 1장 이상 10장 이하인 경우
                    Log.e(TAG, "multiple choice");

                    for (int i = 0; i < clipData.getItemCount(); i++){
                        Uri imageUri = clipData.getItemAt(i).getUri();  // 선택한 이미지들의 uri를 가져온다.
                        try {
                            uriList.add(imageUri);  //uri를 list에 담는다.

                        } catch (Exception e) {
                            Log.e(TAG, "File select error", e);
                        }
                    }
                    contentUpLoad();
                      // 리사이클러뷰 수평 스크롤 적용
                }
            }

    }


    private void contentUpLoad(){ // 진짜 스파게티 코드로 만든 부분이니까 건들지 말아주세요
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        int count=0;
        int inner_cnt=0;
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
                            dto.setLikeCount(0);
                            dto.setIsUpload(false);

                            db.collection("images").document().set(dto).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    if (uriList.size() == finalInner_cnt){
                                        startActivity(new Intent(getApplicationContext(),GalleryActivity.class));
                                        finish();
                                        overridePendingTransition(0,0);
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "업로드 실패", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
//                Toast.makeText(MainActivity.this, "succes!!", Toast.LENGTH_SHORT).show();
                }
            });
        }
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