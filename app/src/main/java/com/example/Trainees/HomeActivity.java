package com.example.Trainees;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Trainees.Menu.MenuActivity;
import com.example.Trainees.Profile.MypageActivity;
import com.example.Trainees.Toukou.My_ToukouActivity;
import com.example.Trainees.Toukou.Others_ToukouActivity;
import com.example.Trainees.Toukou.ToukouAdapter;
import com.example.Trainees.Toukou.Add_ToukouActivity;
import com.example.Trainees.dataclass.ToukouData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Query.Direction;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    public FirebaseFirestore db;
    public ToukouAdapter mCustomAdapter;
    public ListView mListView;
    public ArrayList<String> toukouid = new ArrayList<String>();
    public String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uid = user.getUid();
        }

        mListView = (ListView)findViewById(R.id.toukou_list);
        db = FirebaseFirestore.getInstance();

        mCustomAdapter = new ToukouAdapter(getApplicationContext(), R.layout.home_toukou_layout, new ArrayList<ToukouData>());
        mListView.setAdapter(mCustomAdapter);
        mListView.setOnItemClickListener(this);

        db.collection("Toukou").orderBy("time", Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ToukouData toukou = document.toObject(ToukouData.class);
                        mCustomAdapter.add(toukou);
                        toukouid.add(document.getId());
                    }
                    mCustomAdapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(HomeActivity.this, "Error Getting task list.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        FloatingActionButton addBt = findViewById(R.id.addbutton);
        addBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), Add_ToukouActivity.class);
                intent.putExtra("Activity","Home");
                startActivity(intent);
            }
        });

        ImageButton setBT = findViewById(R.id.settingbutton);
        setBT.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), SetteiActivity.class);
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

    @Override
    public  void onItemClick(AdapterView<?> parent, View view,int position, long id) {
        Intent intent = new Intent();
        String a = mCustomAdapter.getItem(position).getUid();
        String tid = toukouid.get(position);
        if(uid.equals(a)) {
            intent.setClass(this, My_ToukouActivity.class);
        }else{
            intent.setClass(this, Others_ToukouActivity.class);
        }
        intent.putExtra("ToukouID", tid);
        intent.putExtra("Activity","Home");
        startActivity(intent);
    }
}