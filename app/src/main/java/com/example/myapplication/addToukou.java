package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.myapplication.dataclass.ToukouData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class addToukou extends AppCompatActivity {
    EditText titleEdit,goolEdit,menyuEdit,memoEdit;
    private DatabaseReference mDatabase;
    private FirebaseFirestore db;
    private static final String TAG = "addToukou";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_toukou);

        ImageButton backBt = findViewById(R.id.backbutton);
        backBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), HomeActivity.class);
                startActivity(intent);
            }
        });

        titleEdit = findViewById(R.id.title_main);
        goolEdit = findViewById(R.id.gool_main);
        menyuEdit = findViewById(R.id.menyu_main);
        memoEdit = findViewById(R.id.memo_main);
        db = FirebaseFirestore.getInstance();

        Button postBt = findViewById(R.id.postButton);
        postBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String title = titleEdit.getText().toString();
                String gool = goolEdit.getText().toString();
                String menu = menyuEdit.getText().toString();
                String memo = memoEdit.getText().toString();

                setToukou(title, gool, menu, memo);

            }
        });
    }

    public void setToukou(final String title, final String gool, final String menu, final String memo){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            final String uid = user.getUid();

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            final String time = sdf.format(timestamp);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            mDatabase = database.getReference().child("User").child(uid);
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String nickname = dataSnapshot.child("nickname").getValue().toString();

                    ToukouData toukouData = new ToukouData(uid,nickname, title, gool, menu,  memo, time);

                    db.collection("Toukou")
                            .add(toukouData)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
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
                @Override
                public void onCancelled(DatabaseError error) {

                }
            });

        }
    }

    private void changeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}