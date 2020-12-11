package com.example.Trainees.Menu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Trainees.R;
import com.example.Trainees.dataclass.TrainingData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Change_TrainingActivity extends AppCompatActivity {
    public String uid,menuid,tid;
    public String training,weight,count,times,memo;
    EditText trainingEdit,weightEdit,countEdit,timesEdit,memoEdit;
    CheckBox weightCheck, countCheck, timeCheck;
    private FirebaseFirestore db;
    private static final String TAG = "Add_TrainingActivity";
    InputMethodManager inputMethodManager;
    public String text = "必須項目を入力してください";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_training);

        Intent intent = getIntent();
        uid = intent.getStringExtra("UserID");
        menuid = intent.getStringExtra("MenuID");
        tid = intent.getStringExtra("TrainingID");

        trainingEdit = findViewById(R.id.training_main);
        weightEdit = findViewById(R.id.weightedit);
        countEdit = findViewById(R.id.countedit);
        timesEdit = findViewById(R.id.timesedit);
        memoEdit = findViewById(R.id.menu_main);
        inputMethodManager =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        weightCheck = findViewById(R.id.weight_check);
        countCheck = findViewById(R.id.count_check);
        timeCheck = findViewById(R.id.time_check);

        db = FirebaseFirestore.getInstance();
        final DocumentReference docRef = db.collection(uid).document(menuid).collection("Training").document(tid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        training = (String)document.get("training");
                        weight = (String)document.get("weight");
                        count = (String)document.get("count");
                        times = (String)document.get("times");
                        memo = (String) document.get("memo");

                        trainingEdit.setText(training);
                        memoEdit.setText(memo);

                        if(!weight.equals("")){
                            weightCheck.setChecked(true);
                            weightEdit.setText(weight);

                        }else{
                            weightCheck.setChecked(false);
                            weightEdit.setEnabled(false);
                        }

                        if(!count.equals("")){
                            countEdit.setText(count);
                            countCheck.setChecked(true);
                        }else{
                            countCheck.setChecked(false);
                            countEdit.setEnabled(false);
                        }

                        if(!times.equals("")){
                            timesEdit.setText(times);
                            timeCheck.setChecked(true);
                        }else{
                            timeCheck.setChecked(false);
                            timesEdit.setEnabled(false);
                        }


                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


        weightCheck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean check = weightCheck.isChecked();
                if(check){
                    weightEdit.setEnabled(true);
                }
                else{
                    weightEdit.setText("");
                    weightEdit.setEnabled(false);
                }
            }
        });

        countCheck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean check = countCheck.isChecked();
                if(check){
                    countEdit.setEnabled(true);
                }
                else{
                    countEdit.setText("");
                    countEdit.setEnabled(false);
                }
            }
        });

        timeCheck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean check = timeCheck.isChecked();
                if(check){
                    timesEdit.setEnabled(true);
                }
                else{
                    timesEdit.setText("");
                    timesEdit.setEnabled(false);
                }
            }
        });

        trainingEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                    inputMethodManager.hideSoftInputFromWindow(trainingEdit.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    return true;
                }
                return false;
            }
        });

        weightEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                    inputMethodManager.hideSoftInputFromWindow(weightEdit.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    return true;
                }
                return false;
            }
        });

        countEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                    inputMethodManager.hideSoftInputFromWindow(countEdit.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    return true;
                }
                return false;
            }
        });

        timesEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                    inputMethodManager.hideSoftInputFromWindow(timesEdit.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    return true;
                }
                return false;
            }
        });

        db = FirebaseFirestore.getInstance();
        Button postBt = findViewById(R.id.decidedbutton);
        postBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String training = trainingEdit.getText().toString();
                String weight = weightEdit.getText().toString();
                String count = countEdit.getText().toString();
                String time = timesEdit.getText().toString();
                String memo = memoEdit.getText().toString();

                final boolean b = weight.equals("") && count.equals("") && time.equals("");
                if(b && !training.equals("")){
                    text = "重さ、回数、時間どれか1つ以上にチェックを入れ、入力してください";
                }else if(!b && training.equals("")){
                    text = "タイトルが入力されてません";
                }

                if(training.equals("")||b){
                    new AlertDialog.Builder(Change_TrainingActivity.this)
                            .setTitle(text)
                            .setPositiveButton("OK" , null )
                            .show();
                }else{
                    changeTraining(uid,menuid,tid,training,weight,count,time,memo);
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

    public void changeTraining(String uid,String mid,String tid,String training,String weight,String count,String time,String memo){
        TrainingData trainingData = new TrainingData(training,weight,count,time,memo);
        db.collection(uid).document(mid).collection("Training").document(tid).set(trainingData);
        changeActivity();
    }

    private void changeActivity() {
        Intent intent = new Intent(this, Details_TrainingActivity.class);
        intent.putExtra("UserID",uid);
        intent.putExtra("MenuID",menuid);
        intent.putExtra("TrainingID",tid);
        startActivity(intent);
        finish();
    }
}