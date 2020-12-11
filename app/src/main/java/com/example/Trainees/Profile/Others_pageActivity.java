package com.example.Trainees.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Trainees.HomeActivity;
import com.example.Trainees.Menu.MenuActivity;
import com.example.Trainees.R;
import com.example.Trainees.SearchActivity;
import com.example.Trainees.Toukou.Others_ToukouActivity;
import com.example.Trainees.Toukou.ToukouAdapter;
import com.example.Trainees.dataclass.ToukouData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Others_pageActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    public FirebaseFirestore db;
    public String nickname,s_intro,proimage;
    public String uid;
    TextView Nickname,Selfintro;
    ImageView Proimage;
    public ToukouAdapter mCustomAdapter;
    public ListView mListView;
    private static final String TAG = "Others_pageActivity";
    public ArrayList<String> toukouid = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_page);

        Nickname = (TextView)findViewById(R.id.nickname);
        Selfintro = (TextView)findViewById(R.id.selfintro);
        Proimage = findViewById(R.id.proicon);

        Intent intent = getIntent();
        uid = intent.getStringExtra("UserID");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabase = database.getReference().child("User").child(uid);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //インスタンスの取得
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    nickname = dataSnapshot.child("nickname").getValue().toString();
                    s_intro = dataSnapshot.child("selfintro").getValue().toString();
                    proimage = dataSnapshot.child("profileimage").getValue().toString();

                    Nickname.setText(nickname);
                    Selfintro.setText(s_intro);

                    Bitmap probim = decodeBase64(proimage);
                    Proimage.setImageBitmap(probim);

                    Log.d(TAG,data.getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // データの取得に失敗
            }
        });

        mListView = (ListView)findViewById(R.id.toukou_list);
        db = FirebaseFirestore.getInstance();

        mCustomAdapter = new ToukouAdapter(getApplicationContext(), R.layout.home_toukou_layout, new ArrayList<ToukouData>());
        mListView.setAdapter(mCustomAdapter);
        mListView.setOnItemClickListener(this);

        final LinearLayout Layout = (LinearLayout)findViewById(R.id.nottoukou_frame);
        Layout.setOrientation(LinearLayout.VERTICAL);
        Layout.setGravity(Gravity.CENTER);
        final TextView notcomtext = new TextView(this);

        db.collection("Toukou")
                .whereEqualTo("uid", uid).orderBy("time", Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ToukouData toukou = document.toObject(ToukouData.class);
                        mCustomAdapter.add(toukou);
                        toukouid.add(document.getId());
                    }

                    if(task.getResult().size() == 0) {
                        String str = "投稿はありません";
                        notcomtext.setText(str);
                        notcomtext.setTextColor(Color.BLACK);
                        notcomtext.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                        Layout.addView(notcomtext,
                                new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT));
                    }

                    mCustomAdapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(Others_pageActivity.this, "Error Getting task list.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ImageButton reloadBT = findViewById(R.id.reloadbutton);
        reloadBT.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), Others_pageActivity.class);
                intent.putExtra("UserID",uid);
                startActivity(intent);
                finish();
            }
        });

        ImageView backBt = findViewById(R.id.backbutton);
        backBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        ImageButton homeBt = findViewById(R.id.homeBT);
        homeBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), HomeActivity.class);
                startActivity(intent);
            }
        });

        ImageButton searchBt = findViewById(R.id.searchBT);
        searchBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), SearchActivity.class);
                startActivity(intent);
            }
        });

        ImageButton menuBt = findViewById(R.id.menuBT);
        menuBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), MenuActivity.class);
                startActivity(intent);
            }
        });

        ImageButton mypageBt = findViewById(R.id.mypageBT);
        mypageBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), MypageActivity.class);
                startActivity(intent);
            }
        });
    }

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    @Override
    public  void onItemClick(AdapterView<?> parent, View view,int position, long id) {
        Intent intent = new Intent(this, Others_ToukouActivity.class);
        String tid = toukouid.get(position);
        intent.putExtra("ToukouID", tid);
        intent.putExtra("Activity", "Otherspage");
        startActivity(intent);
    }
}