package com.example.Trainees;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Trainees.Menu.MenuActivity;
import com.example.Trainees.Profile.MypageActivity;
import com.example.Trainees.Toukou.My_ToukouActivity;
import com.example.Trainees.Toukou.Others_ToukouActivity;
import com.example.Trainees.Toukou.ToukouAdapter;
import com.example.Trainees.dataclass.ToukouData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    public FirebaseFirestore db;
    public ToukouAdapter mCustomAdapter;
    public ListView mListView;
    public ArrayList<String> toukouid = new ArrayList<String>();
    public String uid,text;
    public String TAG = "SearchActivity";
    public String str1 = "検索欄に文字を入力してください";
    public String str2 = "該当する投稿はありません";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uid = user.getUid();
        }

        mListView = (ListView)findViewById(R.id.toukou_list);
        db = FirebaseFirestore.getInstance();

        mCustomAdapter = new ToukouAdapter(getApplicationContext(), R.layout.home_toukou_layout, new ArrayList<ToukouData>());
        mListView.setAdapter(mCustomAdapter);
        mListView.setOnItemClickListener(this);

        final LinearLayout Layout = findViewById(R.id.nottoukouframe);
        Layout.setOrientation(LinearLayout.VERTICAL);
        Layout.setGravity(Gravity.CENTER);
        final TextView nottoukoutext = new TextView(this);
        nottoukoutext.setTextColor(Color.BLACK);
        nottoukoutext.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        Layout.addView(nottoukoutext,
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
        nottoukoutext.setText(str1);

        final EditText searchEdit = findViewById(R.id.searchEdit);
        final TabLayout tablayout = findViewById(R.id.tabLayout);
        Button search = findViewById(R.id.SearchButton);
        search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final String strsearch = searchEdit.getText().toString();
                mCustomAdapter.clear();
                toukouid.clear();

                nottoukoutext.setVisibility(View.GONE);
                if(!strsearch.equals("")){
                    switch (tablayout.getSelectedTabPosition()){
                        case 0:
                            db.collection("Toukou").orderBy("time", Query.Direction.DESCENDING)
                                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if(task.isSuccessful()){
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    ToukouData toukou = document.toObject(ToukouData.class);

                                                    text = (String)document.get("title");
                                                    if(text.contains(strsearch)){
                                                        mCustomAdapter.add(toukou);
                                                        toukouid.add(document.getId());
                                                    }
                                                }
                                                if(toukouid.size() == 0) {
                                                    nottoukoutext.setVisibility(View.VISIBLE);
                                                    nottoukoutext.setText(str2);
                                                }
                                                mCustomAdapter.notifyDataSetChanged();
                                            }else{
                                                Toast.makeText(SearchActivity.this, "Error Getting task list.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                            break;

                        case 1:
                            db.collection("Toukou").orderBy("time", Query.Direction.DESCENDING)
                                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful()){
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                ToukouData toukou = document.toObject(ToukouData.class);
                                                text = (String)document.get("menu");
                                                if(text.contains(strsearch)){
                                                    mCustomAdapter.add(toukou);
                                                    toukouid.add(document.getId());
                                                }
                                            }
                                            if(toukouid.size() == 0) {
                                                nottoukoutext.setVisibility(View.VISIBLE);
                                                nottoukoutext.setText(str2);
                                            }

                                        mCustomAdapter.notifyDataSetChanged();
                                    }else{
                                        Toast.makeText(SearchActivity.this, "Error Getting task list.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            break;
                    }

                }else {
                    nottoukoutext.setVisibility(View.VISIBLE);
                    nottoukoutext.setText(str1);
                }

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
        searchBt.setImageResource(R.drawable.select_searchicon);
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
