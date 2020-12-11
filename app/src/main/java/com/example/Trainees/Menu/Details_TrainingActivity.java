package com.example.Trainees.Menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Trainees.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Details_TrainingActivity extends AppCompatActivity {
    public String uid, menuid, tid;
    public String training, weight, count, times, memo;
    TextView Training, Memo;
    LinearLayout Details;
    public FirebaseFirestore db;
    private static final String TAG = "Details_TrainingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_training);

        final Intent intent = getIntent();
        uid = intent.getStringExtra("UserID");
        menuid = intent.getStringExtra("MenuID");
        tid = intent.getStringExtra("TrainingID");

        db = FirebaseFirestore.getInstance();
        Training = findViewById(R.id.training_main);
        Memo = findViewById(R.id.menu_main);
        Details = findViewById(R.id.details_layout);

        Details.setOrientation(LinearLayout.VERTICAL);
        final TextView weightText = new TextView(this);
        final TextView countText = new TextView(this);
        final TextView timesText = new TextView(this);

        final DocumentReference docRef = db.collection(uid).document(menuid).collection("Training").document(tid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        training = (String) document.get("training");
                        weight = (String) document.get("weight");
                        count = (String) document.get("count");
                        times = (String) document.get("times");
                        memo = (String) document.get("memo");

                        Training.setText(training);
                        Memo.setText(memo);

                        if (!weight.equals("")) {
                            String str = ("重さ " + weight);
                            weightText.setText(str);
                            //weightText.setTextColor(Color.BLACK);
                            //weightText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                            Details.addView(weightText,
                                    new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT));
                        }
                        if (!count.equals("")) {
                            String str = ("回数 " + count);
                            countText.setText(str);
                            //countText.setTextColor(Color.BLACK);
                            //countText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                            Details.addView(countText,
                                    new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT));
                        }
                        if (!times.equals("")) {
                            String str = ("時間 " + times);
                            timesText.setText(str);
                            //timesText.setTextColor(Color.BLACK);
                            //timesText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                            Details.addView(timesText,
                                    new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT));
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

        ImageButton backBt = findViewById(R.id.backbutton);
        backBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        ImageButton changeBT = findViewById(R.id.changebutton);
        changeBT.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Details_TrainingActivity.this);
                builder.setTitle(training);
                String[] menulist = {"編集", "削除"};
                builder.setItems(menulist, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Intent intent = new Intent(getApplication(), Change_TrainingActivity.class);
                            intent.putExtra("MenuID", menuid);
                            intent.putExtra("UserID", uid);
                            intent.putExtra("TrainingID", tid);
                            startActivity(intent);
                            finish();
                        } else if (which == 1) { Delete(docRef); }
                    }
                }).setPositiveButton("閉じる", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create().show();
            }
        });
    }

    public void Delete(final DocumentReference docRef) {
        new AlertDialog.Builder(Details_TrainingActivity.this)
                .setTitle("このトレーニングを削除しますか？")
                .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        docRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                Toast.makeText(Details_TrainingActivity.this, "削除しました", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplication(), TrainingActivity.class);
                                intent.putExtra("MenuID", menuid);
                                intent.putExtra("UserID", uid);
                                startActivity(intent);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error deleting document", e);
                            }
                        });
                    }
                })
                .setNegativeButton("いいえ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(Details_TrainingActivity.this, "キャンセルしました", Toast.LENGTH_SHORT).show();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        Toast.makeText(Details_TrainingActivity.this, "キャンセルしました", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }
}