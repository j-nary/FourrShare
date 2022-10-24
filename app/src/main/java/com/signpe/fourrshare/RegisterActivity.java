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

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_register);
        Button register= findViewById(R.id.register_button);
        EditText id = findViewById(R.id.input_id);
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
            Toast.makeText(getApplicationContext(),"비어 있는 항목이 있습니다.",Toast.LENGTH_SHORT).show();

        }
        else if(!isEmailValid(id.getText().toString())){
            Toast.makeText(this, "이메일의 형식으로 입력하셔야 합니다.", Toast.LENGTH_SHORT).show();

        }

        else if (!(pw.getText().toString().equals(check_pw.getText().toString()))){
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
        }
        else if((pw.getText().toString().length() < 6)){
            Toast.makeText(this, "비밀번호는 6글자 이상이여야 합니다.", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(getApplicationContext(),"회원가입 성공",Toast.LENGTH_SHORT).show();
                            Intent myintent = new Intent(getApplicationContext(),LoginActivity.class);
                            finish();
                            startActivity(myintent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "이미 존재하는 이메일 입니다.",
                                    Toast.LENGTH_SHORT).show();
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