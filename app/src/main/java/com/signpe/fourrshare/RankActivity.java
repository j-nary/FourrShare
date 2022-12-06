package com.signpe.fourrshare;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import java.util.Collections;
import java.util.Comparator;

public class RankActivity extends AppCompatActivity{
    public RecyclerView rankRecyclerView;
    private RankAdapter rankAdapter;
    private RecyclerView.LayoutManager rankLayoutManager;

    Intent mintent;



    //FireBase 관련 선언. 건들면 어플 죽어요 !
    ArrayList<ImageDTO> imageDTOS = new ArrayList<>();
    ArrayList<ImageInfo> imageInfos = new ArrayList<>();
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
        getIntent().setAction("Already created");


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

        rankAdapter = new RankAdapter(getApplicationContext(), imageInfos, sharedpreferences.getBoolean("state",false));
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
        Comparator<ImageInfo> cmpAsc = new Comparator<ImageInfo>() {

            @Override
            public int compare(ImageInfo o1, ImageInfo o2) {
                return Integer.compare(o2.getImageDTO().getLikeCount(), o1.getImageDTO().getLikeCount());
            }
        } ;

        Comparator<ImageInfo> cmpAscTime = new Comparator<ImageInfo>() {

            @Override
            public int compare(ImageInfo o1, ImageInfo o2) {
                return (o2.getImageDTO().getTimeStamp()).compareTo(o1.getImageDTO().getTimeStamp());
            }
        } ;
        CheckBox checkLikeOrder = findViewById(R.id.like_order);
        ArrayList<ImageInfo> temp = new ArrayList<>(rankAdapter.imageInfos);
        Collections.copy(temp,rankAdapter.imageInfos);

        if (checkLikeOrder.isChecked()){ // 체크하면 좋아요 순으로
            Collections.sort(rankAdapter.imageInfos,cmpAsc);
//            for (int i=0; i< rankAdapter.imageInfos.size() ; i++){
//                int toPos = rankAdapter.imageInfos.indexOf(temp.get(i));
//
//                rankAdapter.notifyItemMoved(i,toPos);
//
//            }
            rankAdapter.notifyItemRangeChanged(0,rankAdapter.imageInfos.size());


            editor.putBoolean("state",true);
            editor.apply();
//            startActivity(new Intent(getApplicationContext(),RankActivity.class));
//            finish();
//            overridePendingTransition(0,0);

        }
        else{
            Collections.sort(rankAdapter.imageInfos,cmpAscTime);
            rankAdapter.notifyItemRangeChanged(0,rankAdapter.imageInfos.size());

//            for (int i=0; i< rankAdapter.imageInfos.size() ; i++){
//                int toPos = rankAdapter.imageInfos.indexOf(temp.get(i));
//                rankAdapter.notifyItemMoved(i,toPos);
//
//
//            }


            editor.putBoolean("state",false);
            editor.apply();
//            startActivity(new Intent(getApplicationContext(),RankActivity.class));
//            finish();
//            overridePendingTransition(0,0);


        }
    }
    @Override
    protected void onResume() {
        Log.v("Example", "onResume");

        String action = getIntent().getAction();
        // Prevent endless loop by adding a unique action, don't restart if action is present
        if(action == null || !action.equals("Already created")) {
            Log.v("Example", "Force restart");
            Intent intent = new Intent(this, RankActivity.class);
            startActivity(intent);

            finish();
            overridePendingTransition(0,0);
        }
        // Remove the unique action so the next time onResume is called it will restart
        else
            getIntent().setAction(null);

        super.onResume();
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