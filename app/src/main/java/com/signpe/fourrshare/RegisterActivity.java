package com.signpe.fourrshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText nickname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_register);
        Button register= findViewById(R.id.register_button);
        EditText id = findViewById(R.id.input_id);
        nickname = findViewById(R.id.input_nickname);
        EditText pw = findViewById(R.id.input_pw);
        EditText check_pw = findViewById(R.id.input_check_pw);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (registerValidationCheck(id,pw,check_pw))
                {
                    createUserWithEmailAndPassword(id.getText().toString(), pw.getText().toString());

                }




            }
        });
    }
    private boolean registerValidationCheck(EditText id, EditText pw, EditText check_pw){
        if (id.getText().toString().length() ==0 || pw.getText().toString().length() ==0 || check_pw.getText().toString().length() ==0){
            DynamicToast.makeWarning(getApplicationContext(),"비어 있는 항목이 있습니다.").show();

        }
        else if(!isEmailValid(id.getText().toString())){
            DynamicToast.makeWarning(getApplicationContext(),"이메일의 형식으로 입력하셔야 합니다.").show();
        }

        else if (!(pw.getText().toString().equals(check_pw.getText().toString()))){
            DynamicToast.makeWarning(getApplicationContext(),"비밀번호가 일치하지 않습니다.").show();
        }
        else if((pw.getText().toString().length() < 6)){
            DynamicToast.makeWarning(getApplicationContext(),"비밀번호는 6글자 이상이여야 합니다.").show();
        }
        else{ return true;}
        return false;
    }


    private void createUserWithEmailAndPassword(String email,String password){
        Log.d("superdroid","called!");
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d("superdroid", "createUserWithEmail:success");
                            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setPhotoUri(Uri.parse("android.resource://"+getPackageName()+"/"+R.drawable.profile)).setDisplayName(nickname.getText().toString()).build();
                            mAuth.getCurrentUser().updateProfile(profileChangeRequest).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    DynamicToast.makeSuccess(getApplicationContext(),"회원가입에 성공하였습니다").show();
                                    Intent myintent = new Intent(getApplicationContext(),LoginActivity.class);
                                    finish();
                                    startActivity(myintent);
                                }
                            });


                        } else {
                            // If sign in fails, display a message to the user.
                            DynamicToast.makeError(getApplicationContext(),"이미 존재하는 이메일입니다.").show();
                            Log.d("sperdorid", "failfail:success");
                        }
                    }
                });
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void reload(){

    }
}