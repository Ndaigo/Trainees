package com.example.myapplication.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.set_Profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class newLoginActivity extends AppCompatActivity {

    EditText emailFormEditText, passwordFormEditText;
    public Intent data;
    public FirebaseAuth mAuth;
    private static final String TAG = "newLoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newlogin);

        emailFormEditText = (EditText) findViewById(R.id.email);
        passwordFormEditText = (EditText) findViewById(R.id.password);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.newlogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = ((TextView)findViewById(R.id.email)).getText().toString();
                String password = ((TextView)findViewById(R.id.password)).getText().toString();
                createAccount(email, password);
            }
        });
    }

    public boolean checkEmpty() {
        if (TextUtils.isEmpty(emailFormEditText.getText())) {
            Log.d("newLoginActivity", "何も記入されていません");
            Toast.makeText(newLoginActivity.this, "ログインに成功しました！", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(passwordFormEditText.getText())) {
            Log.d("newLoginActivity", "何も記入されていません");
            Toast.makeText(newLoginActivity.this, "passwordが何も記入されていません", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void createAccount(final String email, String password) {
        Log.d(TAG, "createAccount:" + email);

        if (!checkEmpty()) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // ログインに成功したら、ログインしたユーザーの情報でUIを更新します。
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(newLoginActivity.this, "新規作成に成功しました！", Toast.LENGTH_SHORT).show();
                            changeActivity(email);
                        } else {
                            // サインインに失敗した場合は、ユーザーにメッセージを表示します。
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(newLoginActivity.this, "新規登録に失敗しました",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }


    private void changeActivity(String email) {
        Intent intent = new Intent(this, set_Profile.class);
        intent.putExtra("email",email);
        startActivity(intent);
        finish();
    }

}
