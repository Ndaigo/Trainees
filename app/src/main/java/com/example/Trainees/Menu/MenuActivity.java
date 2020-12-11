package com.example.Trainees.Menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.Trainees.HomeActivity;
import com.example.Trainees.Profile.MypageActivity;
import com.example.Trainees.R;
import com.example.Trainees.SearchActivity;
import com.example.Trainees.dataclass.MenuData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    public FirebaseFirestore db;
    public MenuAdapter mCustomAdapter;
    public ListView mListView;
    public ArrayList<String> menuid = new ArrayList<String>();
    public String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uid = user.getUid();
        }

        mListView = (ListView)findViewById(R.id.menu_list);
        db = FirebaseFirestore.getInstance();

        mCustomAdapter = new MenuAdapter(getApplicationContext(), R.layout.menu_layout, new ArrayList<MenuData>());
        mListView.setAdapter(mCustomAdapter);
        mListView.setOnItemClickListener(this);

        db.collection(uid).orderBy("title")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        MenuData menu = document.toObject(MenuData.class);
                        mCustomAdapter.add(menu);
                        menuid.add(document.getId());
                    }
                    mCustomAdapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(MenuActivity.this, "Error Getting task list.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        Button addBT = findViewById(R.id.decidedbutton);
        addBT.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), Add_MenuActivity.class);
                intent.putExtra("UserID",uid);
                startActivity(intent);
            }
        });

        ImageButton homeBT = findViewById(R.id.homeBT);
        homeBT.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), HomeActivity.class);
                startActivity(intent);
            }
        });

        ImageButton searchBT = findViewById(R.id.searchBT);
        searchBT.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), SearchActivity.class);
                startActivity(intent);
            }
        });

        ImageButton menuBT = findViewById(R.id.menuBT);
        menuBT.setImageResource(R.drawable.select_menuicon);
        menuBT.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), MenuActivity.class);
                startActivity(intent);
            }
        });

        ImageButton mypageBT = findViewById(R.id.mypageBT);
        mypageBT.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), MypageActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public  void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String mid = menuid.get(position);

        Intent intent = new Intent(this, TrainingActivity.class);
        intent.putExtra("MenuID", mid);
        intent.putExtra("UserID",uid);
        startActivity(intent);
    }
}