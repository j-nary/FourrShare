package com.signpe.fourrshare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ScrapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrap);
    }

    // 네비게이션 바
    public void onClickNavigationBar(View v){
        if(v.getId() == R.id.galleryClickButton){
            finish();
            overridePendingTransition(0, 0);
            startActivity(new Intent(getApplicationContext(),GalleryActivity.class));
            overridePendingTransition(0,0);
        }
        else if(v.getId() == R.id.scanClickButton){
            finish();
            overridePendingTransition(0, 0);
            startActivity(new Intent(getApplicationContext(),ScanActivity.class));
            overridePendingTransition(0,0);
        }
        else if(v.getId() == R.id.rankClickButton){
            finish();
            overridePendingTransition(0, 0);
            startActivity(new Intent(getApplicationContext(),RankActivity.class));
            overridePendingTransition(0,0);
        }
        else if(v.getId() == R.id.scrapClickButton){
            finish();
            overridePendingTransition(0, 0);
            startActivity(new Intent(getApplicationContext(),ScrapActivity.class));
            overridePendingTransition(0,0);
        }
        else if(v.getId() == R.id.myinfoClickButton){
            finish();
            overridePendingTransition(0, 0);
            startActivity(new Intent(getApplicationContext(),MyInfoActivity.class));
            overridePendingTransition(0,0);
        }
    }

}