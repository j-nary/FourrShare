package com.signpe.fourrshare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class ScrapActivity extends AppCompatActivity {
    private RecyclerView scrapRecyclerView;
    private RecyclerView.Adapter scrapAdapter;
    private RecyclerView.LayoutManager scrapLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrap);

    scrapRecyclerView = findViewById(R.id.scrap_recycler_view);
    scrapRecyclerView.setHasFixedSize(true);

    ArrayList<ScrapItem> items = new ArrayList<>();

        items.add(new ScrapItem(R.drawable.common_google_signin_btn_text_dark, "Micky Mouse1"));
        items.add(new ScrapItem(R.drawable.common_google_signin_btn_icon_dark, "M2"));
        items.add(new ScrapItem(R.drawable.common_google_signin_btn_icon_dark_normal_background, "M3"));
        items.add(new ScrapItem(R.drawable.common_google_signin_btn_icon_dark_normal, "M4"));
        items.add(new ScrapItem(R.drawable.common_google_signin_btn_text_dark, "Micky Mouse1"));
        items.add(new ScrapItem(R.drawable.common_google_signin_btn_text_dark_normal_background, "M2"));
        items.add(new ScrapItem(R.drawable.common_google_signin_btn_text_light_normal_background, "M3"));
        items.add(new ScrapItem(R.drawable.common_google_signin_btn_icon_light, "M4"));
        items.add(new ScrapItem(R.drawable.common_google_signin_btn_text_dark, "Micky Mouse1"));
        items.add(new ScrapItem(R.drawable.common_google_signin_btn_icon_dark, "M2"));
        items.add(new ScrapItem(R.drawable.common_google_signin_btn_icon_dark_normal_background, "M3"));
        items.add(new ScrapItem(R.drawable.common_google_signin_btn_icon_dark_normal, "M4"));
        items.add(new ScrapItem(R.drawable.common_google_signin_btn_text_dark, "Micky Mouse1"));
        items.add(new ScrapItem(R.drawable.common_google_signin_btn_text_dark_normal_background, "M2"));
        items.add(new ScrapItem(R.drawable.common_google_signin_btn_text_light_normal_background, "M3"));
        items.add(new ScrapItem(R.drawable.common_google_signin_btn_icon_light, "M4"));
        items.add(new ScrapItem(R.drawable.common_google_signin_btn_text_dark, "Micky Mouse1"));
        items.add(new ScrapItem(R.drawable.common_google_signin_btn_icon_dark, "M2"));
        items.add(new ScrapItem(R.drawable.common_google_signin_btn_icon_dark_normal_background, "M3"));
        items.add(new ScrapItem(R.drawable.common_google_signin_btn_icon_dark_normal, "M4"));
        items.add(new ScrapItem(R.drawable.common_google_signin_btn_text_dark, "Micky Mouse1"));
        items.add(new ScrapItem(R.drawable.common_google_signin_btn_text_dark_normal_background, "M2"));
        items.add(new ScrapItem(R.drawable.common_google_signin_btn_text_light_normal_background, "M3"));
        items.add(new ScrapItem(R.drawable.common_google_signin_btn_icon_light, "M4"));
        items.add(new ScrapItem(R.drawable.common_google_signin_btn_text_dark, "Micky Mouse1"));
        items.add(new ScrapItem(R.drawable.common_google_signin_btn_icon_dark, "M2"));
        items.add(new ScrapItem(R.drawable.common_google_signin_btn_icon_dark_normal_background, "M3"));
        items.add(new ScrapItem(R.drawable.common_google_signin_btn_icon_dark_normal, "M4"));
        items.add(new ScrapItem(R.drawable.common_google_signin_btn_text_dark, "Micky Mouse1"));
        items.add(new ScrapItem(R.drawable.common_google_signin_btn_text_dark_normal_background, "M2"));
        items.add(new ScrapItem(R.drawable.common_google_signin_btn_text_light_normal_background, "M3"));
        items.add(new ScrapItem(R.drawable.common_google_signin_btn_icon_light, "M4"));
        items.add(new ScrapItem(R.drawable.common_google_signin_btn_text_dark, "Micky Mouse1"));
        items.add(new ScrapItem(R.drawable.common_google_signin_btn_icon_dark, "M2"));
        items.add(new ScrapItem(R.drawable.common_google_signin_btn_icon_dark_normal_background, "M3"));
        items.add(new ScrapItem(R.drawable.common_google_signin_btn_icon_dark_normal, "M4"));
        items.add(new ScrapItem(R.drawable.common_google_signin_btn_text_dark, "Micky Mouse1"));
        items.add(new ScrapItem(R.drawable.common_google_signin_btn_text_dark_normal_background, "M2"));
        items.add(new ScrapItem(R.drawable.common_google_signin_btn_text_light_normal_background, "M3"));
        items.add(new ScrapItem(R.drawable.common_google_signin_btn_icon_light, "M4"));
        items.add(new ScrapItem(R.drawable.common_google_signin_btn_text_dark, "Micky Mouse1"));
        items.add(new ScrapItem(R.drawable.common_google_signin_btn_icon_dark, "M2"));
        items.add(new ScrapItem(R.drawable.common_google_signin_btn_icon_dark_normal_background, "M3"));
        items.add(new ScrapItem(R.drawable.common_google_signin_btn_icon_dark_normal, "M4"));
        items.add(new ScrapItem(R.drawable.common_google_signin_btn_text_dark, "Micky Mouse1"));
        items.add(new ScrapItem(R.drawable.common_google_signin_btn_text_dark_normal_background, "M2"));
        items.add(new ScrapItem(R.drawable.common_google_signin_btn_text_light_normal_background, "M3"));
        items.add(new ScrapItem(R.drawable.common_google_signin_btn_icon_light, "M4"));


    scrapLayoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        scrapRecyclerView.setLayoutManager(scrapLayoutManager);

    scrapAdapter = new ScrapAdapter(items,
                                  getApplicationContext());
        scrapRecyclerView.setAdapter(scrapAdapter);
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