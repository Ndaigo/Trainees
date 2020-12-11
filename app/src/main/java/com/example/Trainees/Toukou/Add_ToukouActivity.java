package com.example.Trainees.Toukou;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.Trainees.HomeActivity;
import com.example.Trainees.Profile.MypageActivity;
import com.example.Trainees.R;
import com.example.Trainees.dataclass.ToukouData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

public class Add_ToukouActivity extends AppCompatActivity {
    EditText titleEdit,goalEdit,menuEdit,memoEdit;
    private FirebaseFirestore db;
    private static final String TAG = "add_ToukouActivity";
    public String Activity;
    public String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_toukou);

        Intent intent = getIntent();
        Activity = intent.getStringExtra("Activity");

        titleEdit = findViewById(R.id.title_main);
        goalEdit = findViewById(R.id.goal_main);
        menuEdit = findViewById(R.id.menu_main);
        memoEdit = findViewById(R.id.memo_main);
        db = FirebaseFirestore.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uid = user.getUid();
        }

        final Button addBT = findViewById(R.id.addbutton);

        final ArrayList<String> menulist = new ArrayList<>();
        final ArrayList<String> menuID = new ArrayList<>();
        db.collection(uid).orderBy("title")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        menulist.add((String) document.get("title"));
                        menuID.add(document.getId());
                    }

                    final String[] menuarray = new String[ menulist.size() ];
                    menulist.toArray(menuarray);
                    addBT.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Add_ToukouActivity.this);
                            if(menuarray.length==0){
                                builder.setTitle("登録されているメニューがありません")
                                        .setPositiveButton("OK" , null ).show();
                            }else{
                                builder.setTitle("追加するメニューを選んでください");
                                builder.setItems(menuarray,  new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which){
                                        titleEdit.setText(menuarray[which]);
                                        menuEdit.setText(text(uid,menuID.get(which)));
                                    }
                                }).setPositiveButton("閉じる", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).create().show();
                            }
                        }
                    });

                }else{
                    Toast.makeText(Add_ToukouActivity.this, "Error Getting task list.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ImageButton backBt = findViewById(R.id.backbutton);
        backBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        Button postBt = findViewById(R.id.decidedbutton);
        postBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String title = titleEdit.getText().toString();
                String goal = goalEdit.getText().toString();
                String menu = menuEdit.getText().toString();
                String memo = memoEdit.getText().toString();
                if(title.equals("") || menu.equals("")){
                    new AlertDialog.Builder(Add_ToukouActivity.this)
                            .setTitle("必須項目を入力してください")
                            .setPositiveButton("OK" , null )
                            .show();
                }else{
                    setToukou(title, goal, menu, memo,uid);
                }
            }
        });
    }

    public void setToukou(final String title, final String goal, final String menu, final String memo,String uid){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
        final String time = sdf.format(timestamp);

        ToukouData toukouData = new ToukouData(uid,title, goal, menu, memo, time,0);
        db.collection("Toukou")
                .add(toukouData)
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
        Intent intent = new Intent();
        if(Activity.equals("Mypage")){
            intent.setClass(this, MypageActivity.class);
        }
        else{
            intent.setClass(this, HomeActivity.class);
        }
        startActivity(intent);
        finish();
    }

    public StringBuilder text(String uid, String menuid){
        DocumentReference docRef = db.collection(uid).document(menuid);
        final StringBuilder text = new StringBuilder();
        docRef.collection("Training").orderBy("training")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        text.append(document.get("training")).append(" ");
                        if(!(document.get("weight")).equals("")){
                            text.append(" ").append(document.get("weight"));
                        }
                        if(!(document.get("count")).equals("")){
                            text.append(" ").append(document.get("count"));
                        }
                        if(!(document.get("times")).equals("")){
                            text.append(" ").append(document.get("times"));
                        }
                        text.append("\n");
                    }
                    menuEdit.setText(text);
                }else{
                    Toast.makeText(Add_ToukouActivity.this, "Error Getting task list.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return text;
    }
}