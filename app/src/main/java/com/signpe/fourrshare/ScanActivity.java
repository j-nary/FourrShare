package com.signpe.fourrshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ScanActivity extends AppCompatActivity {
    private final int REQ_CAMERA_PERMISSION = 1001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        String url = "https://upload.wikimedia.org/wikipedia/ko/1/1c/T1%EB%A1%9C%EA%B3%A0.jpg";
        ImageView myimg = findViewById(R.id.imgview);
        Bitmap mybtm=null;
        ImageDownload imageDownload = new ImageDownload(url,myimg);
        imageDownload.execute();

        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if(permission == PackageManager.PERMISSION_DENIED){
            // 권한 없어서 요청
        }else{
            // 권한 있음
            new IntentIntegrator(this).setOrientationLocked(false).setPrompt("QR스캔").initiateScan ();
        }

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},REQ_CAMERA_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case 1001:

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    finish();//인텐트 종료
                    overridePendingTransition(0, 0);//인텐트 효과 없애기//인텐트
                    startActivity(getIntent()); //액티비티 열기
                    overridePendingTransition(0, 0);//인텐트 효과 없애기
                } else {
                    //거부했을 경우
                    Toast toast = Toast.makeText(this, "기능 사용을 위한 권한 동의가 필요합니다.", Toast.LENGTH_SHORT);
                    toast.show();
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult (requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents () == null) {
                Toast.makeText (this, "전달할 값이 없습니다.", Toast.LENGTH_LONG).show ();
            } else {
                Toast.makeText (this, "Scanned: " + result.getContents (), Toast.LENGTH_LONG).show ();
                checkTypeOfPicture(result.getContents());
            }
        } else {
            super.onActivityResult (requestCode, resultCode, data);
        }
//        finish();
    }
    private int checkTypeOfPicture(String result){
        //

        return 0;
    }




}