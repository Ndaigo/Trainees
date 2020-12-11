package com.example.Trainees.Menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.example.Trainees.R;
import com.example.Trainees.dataclass.TrainingData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Add_TrainingActivity extends AppCompatActivity {
    public String uid,menuid;
    EditText trainingEdit,weightEdit,countEdit,timesEdit,memoEdit;
    CheckBox weightCheck, countCheck, timeCheck;
    private FirebaseFirestore db;
    private static final String TAG = "Add_TrainingActivity";
    InputMethodManager inputMethodManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_training);

        Intent intent = getIntent();
        uid = intent.getStringExtra("UserID");
        menuid = intent.getStringExtra("MenuID");

        ImageButton backBt = findViewById(R.id.backbutton);
        backBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        trainingEdit = findViewById(R.id.training_main);
        weightEdit = findViewById(R.id.weightedit);
        countEdit = findViewById(R.id.countedit);
        timesEdit = findViewById(R.id.timesedit);
        memoEdit = findViewById(R.id.menu_main);
        inputMethodManager =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        weightCheck = findViewById(R.id.weight_check);
        weightEdit.setEnabled(false);
        countCheck = findViewById(R.id.count_check);
        countEdit.setEnabled(false);
        timeCheck = findViewById(R.id.time_check);
        timesEdit.setEnabled(false);

        weightCheck.setChecked(false);
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

        countCheck.setChecked(false);
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

        timeCheck.setChecked(false);
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


                if(training.equals("")||(weight.equals("") && count.equals("") && time.equals(""))){
                    new AlertDialog.Builder(Add_TrainingActivity.this)
                            .setTitle("必須項目を入力してください")
                            .setPositiveButton("OK" , null )
                            .show();
                }else{
                    setTraining(uid,menuid,training,weight,count,time,memo);
                }

            }
        });
    }

    public void setTraining(String uid,String menuid,String training,String weight,String count,String time,String memo){
        TrainingData trainingData = new TrainingData(training,weight,count,time,memo);

        db.collection(uid).document(menuid).collection("Training")
                .add(trainingData)
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
        Intent intent = new Intent(this, TrainingActivity.class);
        intent.putExtra("UserID",uid);
        intent.putExtra("MenuID",menuid);
        startActivity(intent);
        finish();
    }
}