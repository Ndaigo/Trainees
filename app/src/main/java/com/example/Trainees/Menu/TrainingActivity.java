package com.example.Trainees.Menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Trainees.HomeActivity;
import com.example.Trainees.Profile.MypageActivity;
import com.example.Trainees.R;
import com.example.Trainees.Toukou.My_ToukouActivity;
import com.example.Trainees.dataclass.TrainingData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class TrainingActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    public FirebaseFirestore db;
    public TrainingAdapter mCustomAdapter;
    public ListView mListView;
    public ArrayList<String> trainingid = new ArrayList<>();
    String uid,menuid,title;
    public TextView Title;
    private static final String TAG = "TrainingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        Intent intent = getIntent();
        uid = intent.getStringExtra("UserID");
        menuid = intent.getStringExtra("MenuID");

        mListView = (ListView)findViewById(R.id.training_list);
        db = FirebaseFirestore.getInstance();

        Title = (TextView)findViewById(R.id.title_main);
        final DocumentReference docRef = db.collection(uid).document(menuid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        title = (String)document.get("title");
                        Title.setText(title);

                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });



        mCustomAdapter = new TrainingAdapter(getApplicationContext(), R.layout.training_layout, new ArrayList<TrainingData>());
        mListView.setAdapter(mCustomAdapter);
        mListView.setOnItemClickListener(this);

        docRef.collection("Training").orderBy("training")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        TrainingData training = document.toObject(TrainingData.class);
                        mCustomAdapter.add(training);
                        trainingid.add(document.getId());
                    }
                    mCustomAdapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(TrainingActivity.this, "Error Getting task list.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ImageButton backBt = findViewById(R.id.backbutton);
        backBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), MenuActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ImageButton startBT = findViewById(R.id.Startbutton);
        startBT.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), Start_MenuActivity.class);
                intent.putExtra("UserID",uid);
                intent.putExtra("MenuID",menuid);
                startActivity(intent);
            }
        });

        ImageButton changeBT = findViewById(R.id.changebutton);
        changeBT.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TrainingActivity.this);
                builder.setTitle(title);
                String[] menulist = {"トレーニング追加","タイトル変更","メニュー削除"};
                builder.setItems(menulist,  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        if(which==0){
                            Intent intent = new Intent(getApplication(), Add_TrainingActivity.class);
                            intent.putExtra("UserID",uid);
                            intent.putExtra("MenuID",menuid);
                            startActivity(intent);
                        }
                        if(which==1){
                            Intent intent = new Intent(getApplication(), Change_MenuActivity.class);
                            intent.putExtra("UserID",uid);
                            intent.putExtra("MenuID",menuid);
                            startActivity(intent);
                            finish();
                        }
                        else if(which==2){ Delete(docRef); }
                    }
                }).setPositiveButton("閉じる", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create().show();
            }
        });
    }

    @Override
    public  void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String tid = trainingid.get(position);

        Intent intent = new Intent(this, Details_TrainingActivity.class);
        intent.putExtra("MenuID", menuid);
        intent.putExtra("UserID",uid);
        intent.putExtra("TrainingID",tid);
        startActivity(intent);
    }

    public void Delete(final DocumentReference docRef){
        new AlertDialog.Builder(TrainingActivity.this)
                .setTitle("このメニューを削除しますか？")
                .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        docRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                Toast.makeText(TrainingActivity.this, "削除しました", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplication(), MenuActivity.class);
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
                        Toast.makeText(TrainingActivity.this, "キャンセルしました", Toast.LENGTH_SHORT).show();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        Toast.makeText(TrainingActivity.this, "キャンセルしました", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }
}