package com.example.Trainees.Menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.Trainees.R;
import com.example.Trainees.Toukou.Comment.Add_CommentActivity;
import com.example.Trainees.dataclass.MenuData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Add_MenuActivity extends AppCompatActivity {
    EditText titleEdit;
    private String uid;
    private FirebaseFirestore db;
    private static final String TAG = "Add_MenuActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu);

        Intent intent = getIntent();
        uid = intent.getStringExtra("UserID");

        ImageButton backBt = findViewById(R.id.backbutton);
        backBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        titleEdit = findViewById(R.id.title_main);
        db = FirebaseFirestore.getInstance();
        Button addBT = findViewById(R.id.decidedbutton);
        addBT.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String title = titleEdit.getText().toString();
                if(title.equals("")){
                    new AlertDialog.Builder(Add_MenuActivity.this)
                            .setTitle("タイトルが入力されてません")
                            .setPositiveButton("OK" , null )
                            .show();
                }else{
                    setMenu(title, uid);
                }
            }
        });
    }

    public void setMenu(String title,String uid){
        MenuData menuData = new MenuData(title);

        db.collection(uid)
                .add(menuData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        changeActivity();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    private void changeActivity() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        finish();
    }

}