package com.example.Trainees.Toukou;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Trainees.HomeActivity;
import com.example.Trainees.Menu.MenuActivity;
import com.example.Trainees.Profile.MypageActivity;
import com.example.Trainees.Profile.Others_pageActivity;
import com.example.Trainees.R;
import com.example.Trainees.SearchActivity;
import com.example.Trainees.Toukou.Comment.Add_CommentActivity;
import com.example.Trainees.Toukou.Comment.CommentAdapter;
import com.example.Trainees.dataclass.CommentData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class My_ToukouActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    public FirebaseFirestore db;
    public String nickname,title,goal,menu,memo,time,uid;
    public Long comnum;
    public String proimage;
    TextView Nickname,Title,Goal,Menu,Memo,Time,ComNumber;
    ImageView Proimage;
    public String ToukouID,Activity;
    public CommentAdapter mCustomAdapter;
    public ListView mListView;
    private static final String TAG = "My_ToukouActivity";
    public String myuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_toukou);

        Intent intent = getIntent();
        ToukouID = intent.getStringExtra("ToukouID");
        Activity = intent.getStringExtra("Activity");

        db = FirebaseFirestore.getInstance();

        Nickname = (TextView)findViewById(R.id.nickname);
        Title = (TextView)findViewById(R.id.title_main);
        Goal = (TextView)findViewById(R.id.goal_main);
        Menu = (TextView)findViewById(R.id.menu_main);
        Memo = (TextView)findViewById(R.id.memo_main);
        Time = (TextView)findViewById(R.id.time);
        Proimage = findViewById(R.id.icon);
        ComNumber = findViewById(R.id.com_num);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            myuid = user.getUid();
        }


        final DocumentReference docRef = db.collection("Toukou").document(ToukouID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        title = (String)document.get("title");
                        goal = (String)document.get("goal");
                        menu = (String)document.get("menu");
                        memo = (String) document.get("memo");
                        time = (String) document.get("time");
                        comnum = (Long) document.get("comnum");
                        uid = (String) document.get("uid");

                        Title.setText(title);
                        Goal.setText(goal);
                        Menu.setText(menu);
                        Memo.setText(memo);
                        Time.setText(time);
                        ComNumber.setText(String.valueOf(comnum));

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference mDatabase = database.getReference().child("User").child(uid);
                        mDatabase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                //インスタンスの取得
                                for (DataSnapshot data : dataSnapshot.getChildren()) {
                                    nickname = dataSnapshot.child("nickname").getValue().toString();
                                    proimage = dataSnapshot.child("profileimage").getValue().toString();

                                    Bitmap probim = decodeBase64(proimage);

                                    Nickname.setText(nickname);
                                    Proimage.setImageBitmap(probim);

                                    Log.d("",data.getValue().toString());
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                // データの取得に失敗
                            }
                        });

                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        mListView = (ListView)findViewById(R.id.comment_list);
        db = FirebaseFirestore.getInstance();

        mCustomAdapter = new CommentAdapter(getApplicationContext(), R.layout.comment_layout, new ArrayList<CommentData>());
        mListView.setAdapter(mCustomAdapter);
        mListView.setOnItemClickListener(this);

        docRef.collection("Comment").orderBy("time")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        CommentData comment = document.toObject(CommentData.class);
                        mCustomAdapter.add(comment);
                    }
                    mCustomAdapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(My_ToukouActivity.this, "Error Getting task list.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ConstraintLayout namelayout = findViewById(R.id.name_frame);
        namelayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), MypageActivity.class);
                startActivity(intent);
            }
        });

        ImageButton backBt = findViewById(R.id.backbutton);
        backBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(Activity.equals("Home")) {
                    Intent intent = new Intent(getApplication(), HomeActivity.class);
                    startActivity(intent);
                }
                /*else if(Activity.equals("Mypage")){
                    Intent intent = new Intent(getApplication(), MypageActivity.class);
                    startActivity(intent);
                }*/
                finish();
            }
        });

        ImageButton comBt = findViewById(R.id.commentButton);
        comBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), Add_CommentActivity.class);
                intent.putExtra("ToukouID",ToukouID);
                startActivity(intent);
            }
        });

        ImageButton homeBt = findViewById(R.id.homeBT);
        homeBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ImageButton searchBt = findViewById(R.id.searchBT);
        searchBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), SearchActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ImageButton menuBt = findViewById(R.id.menuBT);
        menuBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), MenuActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ImageButton mypageBt = findViewById(R.id.mypageBT);
        mypageBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), MypageActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ImageButton deleteBT = findViewById(R.id.reloadbutton);
        deleteBT.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(My_ToukouActivity.this);
                String[] menulist = {"更新","削除"};
                builder.setItems(menulist,  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        if(which==0){
                            Intent intent = new Intent(getApplication(), My_ToukouActivity.class);
                            intent.putExtra("ToukouID",ToukouID);
                            intent.putExtra("Activity",Activity);
                            startActivity(intent);
                            finish();
                        }
                        else if(which==1){ Delete(docRef); }
                    }
                }).setPositiveButton("閉じる", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create().show();
            }
        });
    }

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    @Override
    public  void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        if(myuid.equals(mCustomAdapter.getItem(position).getUid())) {
            intent.setClass(this, MypageActivity.class);

        }else{
            intent.setClass(this, Others_pageActivity.class);
            intent.putExtra("UserID", mCustomAdapter.getItem(position).getUid());
        }
        startActivity(intent);
    }

    public void Delete(final DocumentReference docRef){
        new AlertDialog.Builder(My_ToukouActivity.this)
                .setTitle("この投稿を削除しますか？")
                .setPositiveButton("はい", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        docRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                Toast.makeText(My_ToukouActivity.this, "削除しました", Toast.LENGTH_SHORT).show();
                                if(Activity.equals("Home")) {
                                    Intent intent = new Intent(getApplication(), HomeActivity.class);
                                    startActivity(intent);
                                }else if(Activity.equals("Mypage")){
                                    Intent intent = new Intent(getApplication(), MypageActivity.class);
                                    startActivity(intent);
                                }
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
                        Toast.makeText(My_ToukouActivity.this, "キャンセルしました", Toast.LENGTH_SHORT).show();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        Toast.makeText(My_ToukouActivity.this, "キャンセルしました", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }
}