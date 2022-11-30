package com.signpe.fourrshare;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

public class ScanActivity extends AppCompatActivity {
    private final int REQ_CAMERA_PERMISSION = 1001;
    ImageView myimg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        String url = "https://upload.wikimedia.org/wikipedia/ko/1/1c/T1%EB%A1%9C%EA%B3%A0.jpg";


        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},REQ_CAMERA_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case 1001:

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new IntentIntegrator(this).setOrientationLocked(false).setPrompt("QR스캔").initiateScan ();

                } else {
                    //거부했을 경우
                    DynamicToast.makeWarning(this,"기능 사용을 위한 권한 동의가 필요합니다.").show();
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult (requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents () == null) {
                finish();
                startActivity(new Intent(this,GalleryActivity.class));
            } else {
//                Toast.makeText (this, "Scanned: " + result.getContents (), Toast.LENGTH_LONG).show ();
//                Glide.with(getApplicationContext()).load(result.getContents()).into(myimg);
//                Glide.with(getApplicationContext()).load(result.getContents()).into()
                Intent intent = new Intent(this,GalleryActivity.class);
                intent.putExtra("urls",result.getContents());
                finish();
                startActivity(intent);

            }
        } else {
            super.onActivityResult (requestCode, resultCode, data);
        }
//        finish();
    }



}