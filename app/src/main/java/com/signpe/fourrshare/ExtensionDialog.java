package com.signpe.fourrshare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

public class ExtensionDialog extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extension_dialog);
        Switch switchButton = findViewById(R.id.sw);
        return switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    // switchButton이 체크된 경우
                } else {
                    // switchButton이 체크되지 않은 경우
                }
            }
        }
    }

}