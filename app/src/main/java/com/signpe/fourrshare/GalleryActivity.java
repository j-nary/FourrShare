package com.signpe.fourrshare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        //private void getImageFromAlbum() {

        //}

        Button plus_btn = findViewById(R.id.plus_btn);
        plus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getImageFromAlbum();
            }
        });


        mRecyclerView = findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        ArrayList<GalleryItem> items = new ArrayList<>();

        items.add(new GalleryItem(R.drawable.common_google_signin_btn_text_dark, "Micky Mouse1"));
        items.add(new GalleryItem(R.drawable.common_google_signin_btn_icon_dark, "M2"));
        items.add(new GalleryItem(R.drawable.common_google_signin_btn_icon_dark_normal_background, "M3"));
        items.add(new GalleryItem(R.drawable.common_google_signin_btn_icon_dark_normal, "M4"));
        items.add(new GalleryItem(R.drawable.common_google_signin_btn_text_dark, "Micky Mouse1"));
        items.add(new GalleryItem(R.drawable.common_google_signin_btn_text_dark_normal_background, "M2"));
        items.add(new GalleryItem(R.drawable.common_google_signin_btn_text_light_normal_background, "M3"));
        items.add(new GalleryItem(R.drawable.common_google_signin_btn_icon_light, "M4"));
        items.add(new GalleryItem(R.drawable.common_google_signin_btn_text_dark, "Micky Mouse1"));
        items.add(new GalleryItem(R.drawable.common_google_signin_btn_icon_dark, "M2"));
        items.add(new GalleryItem(R.drawable.common_google_signin_btn_icon_dark_normal_background, "M3"));
        items.add(new GalleryItem(R.drawable.common_google_signin_btn_icon_dark_normal, "M4"));
        items.add(new GalleryItem(R.drawable.common_google_signin_btn_text_dark, "Micky Mouse1"));
        items.add(new GalleryItem(R.drawable.common_google_signin_btn_text_dark_normal_background, "M2"));
        items.add(new GalleryItem(R.drawable.common_google_signin_btn_text_light_normal_background, "M3"));
        items.add(new GalleryItem(R.drawable.common_google_signin_btn_icon_light, "M4"));
        items.add(new GalleryItem(R.drawable.common_google_signin_btn_text_dark, "Micky Mouse1"));
        items.add(new GalleryItem(R.drawable.common_google_signin_btn_icon_dark, "M2"));
        items.add(new GalleryItem(R.drawable.common_google_signin_btn_icon_dark_normal_background, "M3"));
        items.add(new GalleryItem(R.drawable.common_google_signin_btn_icon_dark_normal, "M4"));
        items.add(new GalleryItem(R.drawable.common_google_signin_btn_text_dark, "Micky Mouse1"));
        items.add(new GalleryItem(R.drawable.common_google_signin_btn_text_dark_normal_background, "M2"));
        items.add(new GalleryItem(R.drawable.common_google_signin_btn_text_light_normal_background, "M3"));
        items.add(new GalleryItem(R.drawable.common_google_signin_btn_icon_light, "M4"));
        items.add(new GalleryItem(R.drawable.common_google_signin_btn_text_dark, "Micky Mouse1"));
        items.add(new GalleryItem(R.drawable.common_google_signin_btn_icon_dark, "M2"));
        items.add(new GalleryItem(R.drawable.common_google_signin_btn_icon_dark_normal_background, "M3"));
        items.add(new GalleryItem(R.drawable.common_google_signin_btn_icon_dark_normal, "M4"));
        items.add(new GalleryItem(R.drawable.common_google_signin_btn_text_dark, "Micky Mouse1"));
        items.add(new GalleryItem(R.drawable.common_google_signin_btn_text_dark_normal_background, "M2"));
        items.add(new GalleryItem(R.drawable.common_google_signin_btn_text_light_normal_background, "M3"));
        items.add(new GalleryItem(R.drawable.common_google_signin_btn_icon_light, "M4"));
        items.add(new GalleryItem(R.drawable.common_google_signin_btn_text_dark, "Micky Mouse1"));
        items.add(new GalleryItem(R.drawable.common_google_signin_btn_icon_dark, "M2"));
        items.add(new GalleryItem(R.drawable.common_google_signin_btn_icon_dark_normal_background, "M3"));
        items.add(new GalleryItem(R.drawable.common_google_signin_btn_icon_dark_normal, "M4"));
        items.add(new GalleryItem(R.drawable.common_google_signin_btn_text_dark, "Micky Mouse1"));
        items.add(new GalleryItem(R.drawable.common_google_signin_btn_text_dark_normal_background, "M2"));
        items.add(new GalleryItem(R.drawable.common_google_signin_btn_text_light_normal_background, "M3"));
        items.add(new GalleryItem(R.drawable.common_google_signin_btn_icon_light, "M4"));
        items.add(new GalleryItem(R.drawable.common_google_signin_btn_text_dark, "Micky Mouse1"));
        items.add(new GalleryItem(R.drawable.common_google_signin_btn_icon_dark, "M2"));
        items.add(new GalleryItem(R.drawable.common_google_signin_btn_icon_dark_normal_background, "M3"));
        items.add(new GalleryItem(R.drawable.common_google_signin_btn_icon_dark_normal, "M4"));
        items.add(new GalleryItem(R.drawable.common_google_signin_btn_text_dark, "Micky Mouse1"));
        items.add(new GalleryItem(R.drawable.common_google_signin_btn_text_dark_normal_background, "M2"));
        items.add(new GalleryItem(R.drawable.common_google_signin_btn_text_light_normal_background, "M3"));
        items.add(new GalleryItem(R.drawable.common_google_signin_btn_icon_light, "M4"));


        mLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new GalleryAdapter(items,
                getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);
    }

//    public void onPlusButtonClick(View v) {
//
//    }

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