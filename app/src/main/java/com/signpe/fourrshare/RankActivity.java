package com.signpe.fourrshare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class RankActivity extends AppCompatActivity {
    public RecyclerView rankRecyclerView;
    private RecyclerView.Adapter rankAdapter;
    private RecyclerView.LayoutManager rankLayoutManager;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

        rankRecyclerView = findViewById(R.id.rank_recycler_view);
        rankRecyclerView.setHasFixedSize(true);

        ArrayList<RankItem> items = new ArrayList<>();

        items.add(new RankItem(R.drawable.common_google_signin_btn_text_dark, "Micky Mouse1"));
        items.add(new RankItem(R.drawable.common_google_signin_btn_icon_dark, "M2"));
        items.add(new RankItem(R.drawable.common_google_signin_btn_icon_dark_normal_background, "M3"));
        items.add(new RankItem(R.drawable.common_google_signin_btn_icon_dark_normal, "M4"));
        items.add(new RankItem(R.drawable.common_google_signin_btn_text_dark, "Micky Mouse1"));
        items.add(new RankItem(R.drawable.common_google_signin_btn_text_dark_normal_background, "M2"));
        items.add(new RankItem(R.drawable.common_google_signin_btn_text_light_normal_background, "M3"));
        items.add(new RankItem(R.drawable.common_google_signin_btn_icon_light, "M4"));
        items.add(new RankItem(R.drawable.common_google_signin_btn_text_dark, "Micky Mouse1"));
        items.add(new RankItem(R.drawable.common_google_signin_btn_icon_dark, "M2"));
        items.add(new RankItem(R.drawable.common_google_signin_btn_icon_dark_normal_background, "M3"));
        items.add(new RankItem(R.drawable.common_google_signin_btn_icon_dark_normal, "M4"));
        items.add(new RankItem(R.drawable.common_google_signin_btn_text_dark, "Micky Mouse1"));
        items.add(new RankItem(R.drawable.common_google_signin_btn_text_dark_normal_background, "M2"));
        items.add(new RankItem(R.drawable.common_google_signin_btn_text_light_normal_background, "M3"));
        items.add(new RankItem(R.drawable.common_google_signin_btn_icon_light, "M4"));
        items.add(new RankItem(R.drawable.common_google_signin_btn_text_dark, "Micky Mouse1"));
        items.add(new RankItem(R.drawable.common_google_signin_btn_icon_dark, "M2"));
        items.add(new RankItem(R.drawable.common_google_signin_btn_icon_dark_normal_background, "M3"));
        items.add(new RankItem(R.drawable.common_google_signin_btn_icon_dark_normal, "M4"));
        items.add(new RankItem(R.drawable.common_google_signin_btn_text_dark, "Micky Mouse1"));
        items.add(new RankItem(R.drawable.common_google_signin_btn_text_dark_normal_background, "M2"));
        items.add(new RankItem(R.drawable.common_google_signin_btn_text_light_normal_background, "M3"));
        items.add(new RankItem(R.drawable.common_google_signin_btn_icon_light, "M4"));
        items.add(new RankItem(R.drawable.common_google_signin_btn_text_dark, "Micky Mouse1"));
        items.add(new RankItem(R.drawable.common_google_signin_btn_icon_dark, "M2"));
        items.add(new RankItem(R.drawable.common_google_signin_btn_icon_dark_normal_background, "M3"));
        items.add(new RankItem(R.drawable.common_google_signin_btn_icon_dark_normal, "M4"));
        items.add(new RankItem(R.drawable.common_google_signin_btn_text_dark, "Micky Mouse1"));
        items.add(new RankItem(R.drawable.common_google_signin_btn_text_dark_normal_background, "M2"));
        items.add(new RankItem(R.drawable.common_google_signin_btn_text_light_normal_background, "M3"));
        items.add(new RankItem(R.drawable.common_google_signin_btn_icon_light, "M4"));
        items.add(new RankItem(R.drawable.common_google_signin_btn_text_dark, "Micky Mouse1"));
        items.add(new RankItem(R.drawable.common_google_signin_btn_icon_dark, "M2"));
        items.add(new RankItem(R.drawable.common_google_signin_btn_icon_dark_normal_background, "M3"));
        items.add(new RankItem(R.drawable.common_google_signin_btn_icon_dark_normal, "M4"));
        items.add(new RankItem(R.drawable.common_google_signin_btn_text_dark, "Micky Mouse1"));
        items.add(new RankItem(R.drawable.common_google_signin_btn_text_dark_normal_background, "M2"));
        items.add(new RankItem(R.drawable.common_google_signin_btn_text_light_normal_background, "M3"));
        items.add(new RankItem(R.drawable.common_google_signin_btn_icon_light, "M4"));
        items.add(new RankItem(R.drawable.common_google_signin_btn_text_dark, "Micky Mouse1"));
        items.add(new RankItem(R.drawable.common_google_signin_btn_icon_dark, "M2"));
        items.add(new RankItem(R.drawable.common_google_signin_btn_icon_dark_normal_background, "M3"));
        items.add(new RankItem(R.drawable.common_google_signin_btn_icon_dark_normal, "M4"));
        items.add(new RankItem(R.drawable.common_google_signin_btn_text_dark, "Micky Mouse1"));
        items.add(new RankItem(R.drawable.common_google_signin_btn_text_dark_normal_background, "M2"));
        items.add(new RankItem(R.drawable.common_google_signin_btn_text_light_normal_background, "M3"));
        items.add(new RankItem(R.drawable.common_google_signin_btn_icon_light, "M4"));


        rankLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        rankRecyclerView.setLayoutManager(rankLayoutManager);

        rankAdapter = new RankAdapter(items,
                getApplicationContext());
        rankRecyclerView.setAdapter(rankAdapter);
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