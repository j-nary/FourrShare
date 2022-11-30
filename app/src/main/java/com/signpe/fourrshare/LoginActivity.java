package com.signpe.fourrshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        loginChk();

        Button register_btn = findViewById(R.id.goto_register);
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myintent = new Intent(getApplicationContext(),RegisterActivity.class);
                myintent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(myintent);
            }
        });

        Button login_btn = findViewById(R.id.login_btn);
        EditText input_id = findViewById(R.id.login_input_id);
        EditText input_pw = findViewById(R.id.login_input_pw);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (registerValidationCheck(input_id,input_pw))
                {
                    signInWithEmailAndPassword(input_id.getText().toString(),input_pw.getText().toString());

                }
            }
        });
    }

    private boolean registerValidationCheck(EditText id, EditText pw){
        if (id.getText().toString().length() ==0 || pw.getText().toString().length() ==0 ){
            DynamicToast.makeWarning(getApplicationContext(),"비어 있는 항목이 있습니다.").show();

        }
        else if(!isEmailValid(id.getText().toString())){
            DynamicToast.makeWarning(getApplicationContext(),"이메일의 형식으로 입력하셔야 합니다.").show();

        }
        else if((pw.getText().toString().length() < 6)){
            DynamicToast.makeWarning(getApplicationContext(),"비밀번호는 6글자 이상이여야 합니다.").show();
        }
        else{ return true;}
        return false;
    }

    private void signInWithEmailAndPassword(String email,String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("superdroid", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser(); // to debug
                           // Toast.makeText(LoginActivity.this, mAuth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(),GalleryActivity.class));

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("superdroid", "signInWithEmail:failure", task.getException());
                            DynamicToast.makeError(LoginActivity.this,"로그인에 실패하였습니다.").show();
                        }
                    }
                });
    }

    //Todo : Logout 만든 뒤 연결해줘야함 onStart로 바꿔주자
    public void loginChk() {
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            finish();
            startActivity(new Intent(getApplicationContext(),GalleryActivity.class));
        }
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}