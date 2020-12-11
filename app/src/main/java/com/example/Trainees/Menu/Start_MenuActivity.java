package com.example.Trainees.Menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.Trainees.R;
import com.example.Trainees.dataclass.TrainingData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Start_MenuActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    public FirebaseFirestore db;
    public TrainingAdapter mCustomAdapter;
    public ListView mListView;
    public String uid,menuid;
    public ArrayList<Boolean> clicked = new ArrayList<Boolean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_menu);

        Intent intent = getIntent();
        uid = intent.getStringExtra("UserID");
        menuid = intent.getStringExtra("MenuID");

        mListView = (ListView)findViewById(R.id.training_list);
        db = FirebaseFirestore.getInstance();

        mCustomAdapter = new TrainingAdapter(getApplicationContext(), R.layout.training_layout, new ArrayList<TrainingData>());
        mListView.setAdapter(mCustomAdapter);
        mListView.setOnItemClickListener(this);

        db.collection(uid).document(menuid).collection("Training")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        TrainingData training = document.toObject(TrainingData.class);
                        mCustomAdapter.add(training);
                        clicked.add(false);
                    }
                    mCustomAdapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(Start_MenuActivity.this, "Error Getting task list.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button endBT = findViewById(R.id.endbutton);
        endBT.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public  void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(!clicked.get(position)){
            clicked.set(position, true);
            view.setBackgroundColor(getColor(R.color.training));
        }else if(clicked.get(position)){
            clicked.set(position, false);
            view.setBackgroundColor(Color.WHITE);
        }
    }
}