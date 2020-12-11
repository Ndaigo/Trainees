package com.example.Trainees.Menu;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Trainees.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Change_MenuActivity extends AppCompatActivity {
    EditText titleEdit;
    private String uid,menuid;
    public String title;
    private FirebaseFirestore db;
    private static final String TAG = "Change_MenuActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_menu);

        Intent intent = getIntent();
        uid = intent.getStringExtra("UserID");
        menuid = intent.getStringExtra("MenuID");

        titleEdit = findViewById(R.id.title_main);
        db = FirebaseFirestore.getInstance();

        final DocumentReference docRef = db.collection(uid).document(menuid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        title = (String)document.get("title");
                        titleEdit.setText(title);

                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        Button addBT = findViewById(R.id.decidedbutton);
        addBT.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String title = titleEdit.getText().toString();
                if(title.equals("")){
                    new AlertDialog.Builder(Change_MenuActivity.this)
                            .setTitle("タイトルが入力されてません")
                            .setPositiveButton("OK" , null )
                            .show();
                }else{
                    changeMenu(title, uid, menuid);
                }
            }
        });

        ImageButton backBt = findViewById(R.id.backbutton);
        backBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void changeMenu(String title,String uid,String menuid){
        db.collection(uid).document(menuid).update("title",title);
        changeActivity();
    }

    private void changeActivity() {
        Intent intent = new Intent(this, TrainingActivity.class);
        intent.putExtra("MenuID", menuid);
        intent.putExtra("UserID",uid);
        startActivity(intent);
        finish();
    }

}