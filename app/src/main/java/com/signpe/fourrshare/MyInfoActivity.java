package com.signpe.fourrshare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MyInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
        Button logout = findViewById(R.id.logout_button);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }

    // 네비게이션 바
    public void onClickNavigationBar(View v){
        if(v.getId() == R.id.galleryClickButton){
            finish();
            overridePendingTransition(0,0);
            startActivity(new Intent(getApplicationContext(),GalleryActivity.class));
            overridePendingTransition(0,0);
        }
        else if(v.getId() == R.id.scanClickButton){
            finish();
            overridePendingTransition(0,0);
            startActivity(new Intent(getApplicationContext(),ScanActivity.class));
            overridePendingTransition(0,0);
        }
        else if(v.getId() == R.id.rankClickButton){
            finish();
            overridePendingTransition(0,0);
            startActivity(new Intent(getApplicationContext(),RankActivity.class));
            overridePendingTransition(0,0);
        }
        else if(v.getId() == R.id.scrapClickButton){
            finish();
            overridePendingTransition(0,0);
            startActivity(new Intent(getApplicationContext(),ScrapActivity.class));
            overridePendingTransition(0,0);
        }
        else if(v.getId() == R.id.myinfoClickButton){
            finish();
            overridePendingTransition(0,0);
            startActivity(new Intent(getApplicationContext(),MyInfoActivity.class));
            overridePendingTransition(0,0);
        }
    }

}