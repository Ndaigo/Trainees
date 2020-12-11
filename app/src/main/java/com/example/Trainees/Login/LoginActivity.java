package com.example.Trainees.Login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Trainees.HomeActivity;
import com.example.Trainees.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;



public class LoginActivity extends AppCompatActivity {

    EditText emailFormEditText, passwordFormEditText;
    public FirebaseAuth mAuth;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailFormEditText = findViewById(R.id.email);
        passwordFormEditText = findViewById(R.id.password);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailFormEditText.getText().toString();
                String password = passwordFormEditText.getText().toString();
                signIn(email, password);
            }
        });

        ImageButton backBt = findViewById(R.id.backbutton);
        backBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }

    public boolean checkEmpty() {
        if (TextUtils.isEmpty(emailFormEditText.getText())) {
            Log.d("LoginActivity", "何も記入されていません");
            Toast.makeText(LoginActivity.this, "emailが何も記入されていません", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(passwordFormEditText.getText())) {
            Log.d("LoginActivity", "何も記入されていません");
            Toast.makeText(LoginActivity.this, "passwordが何も記入されていません", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);

        if (!checkEmpty()) {
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // ログインに成功したら、ログインしたユーザーの情報でUIを更新します。
                            Log.d(TAG, "signInWithEmail:success");
                            Toast.makeText(LoginActivity.this, "ログインに成功しました！", Toast.LENGTH_SHORT).show();
                            changeActivity();
                        } else {
                            // サインインに失敗した場合は、ユーザーにメッセージを表示します。
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "ログインに失敗しました", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }


    private void changeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

}