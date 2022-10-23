package com.signpe.fourrshare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        loginChk();
        TextView tester=  findViewById(R.id.main_text);
      tester.setText(currentUser.getEmail());

    }


    public void loginChk(){
        currentUser = mAuth.getCurrentUser();
        if (currentUser == null){
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            finish();
        }
    }
}