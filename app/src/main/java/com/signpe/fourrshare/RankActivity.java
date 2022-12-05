package com.signpe.fourrshare;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.signpe.fourrshare.model.ImageDTO;

import java.util.ArrayList;

public class RankActivity extends AppCompatActivity{
    public RecyclerView rankRecyclerView;
    private RecyclerView.Adapter rankAdapter;
    private RecyclerView.LayoutManager rankLayoutManager;

    Intent mintent;



    //FireBase 관련 선언. 건들면 어플 죽어요 !
    ArrayList<ImageDTO> imageDTOS = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        sharedpreferences = getSharedPreferences("checkstate", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        CheckBox checkLikeOrder = findViewById(R.id.like_order);
        if (sharedpreferences.getBoolean("state", false) == true) {
            checkLikeOrder.setChecked(true);
        } else {
            checkLikeOrder.setChecked(false);
        }


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        rankRecyclerView = findViewById(R.id.rank_recycler_view);
        rankRecyclerView.setHasFixedSize(true);

        rankLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        rankRecyclerView.setLayoutManager(rankLayoutManager);

        rankAdapter = new RankAdapter(getApplicationContext(), imageDTOS, sharedpreferences.getBoolean("state",false));
        rankRecyclerView.setAdapter(rankAdapter);
        ((RankAdapter)rankRecyclerView.getAdapter()).setRankInterface(new RankAdapter.RankInterface() {
            @Override
            public void getIntent(Intent intent) {
                mintent=intent;
                startActivity(mintent);
            }
        });


    }

    // 좋아요 기능
//    public void onClickLike(View v){
//
//    }

    // 좋아요 순 정렬
    public void onClickLikeOrder(View v){
        CheckBox checkLikeOrder = findViewById(R.id.like_order);
        if (checkLikeOrder.isChecked()){ // 체크하면 좋아요 순으로
            editor.putBoolean("state",true);
            editor.apply();
            startActivity(new Intent(getApplicationContext(),RankActivity.class));
            finish();
            overridePendingTransition(0,0);

        }
        else{
            editor.putBoolean("state",false);
            editor.apply();
            startActivity(new Intent(getApplicationContext(),RankActivity.class));
            finish();
            overridePendingTransition(0,0);


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