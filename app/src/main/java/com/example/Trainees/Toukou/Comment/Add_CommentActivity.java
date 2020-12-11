package com.example.Trainees.Toukou.Comment;

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

import com.example.Trainees.Menu.MenuActivity;
import com.example.Trainees.Menu.TrainingActivity;
import com.example.Trainees.R;
import com.example.Trainees.Toukou.My_ToukouActivity;
import com.example.Trainees.Toukou.Others_ToukouActivity;
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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class Add_CommentActivity extends AppCompatActivity {
    private String ToukouID;
    private String comment;
    EditText CommentEdit;
    private FirebaseFirestore db;
    private static final String TAG = "add_Comment";
    private String comnumst;
    private int comnum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);

        Intent intent = getIntent();
        ToukouID = intent.getStringExtra("ToukouID");


        ImageButton backBt = findViewById(R.id.backbutton);
        backBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        CommentEdit = findViewById(R.id.title_main);
        db = FirebaseFirestore.getInstance();

        Button postBt = findViewById(R.id.ComPosButton);
        postBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                comment = CommentEdit.getText().toString();
                if(comment.equals("")){
                    new AlertDialog.Builder(Add_CommentActivity.this)
                            .setTitle("コメントが入力されてません")
                            .setPositiveButton("OK" , null )
                            .show();
                }else{
                    setComment(comment,ToukouID);
                }
            }
        });

    }

    public void setComment(final String comment, final String ToukouID){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            final String uid = user.getUid();

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
            final String time = sdf.format(timestamp);

            CommentData commentData = new CommentData(uid, comment, time);

            final DocumentReference docRef = db.collection("Toukou").document(ToukouID);

            //コメント追加
            docRef.collection("Comment")
                    .add(commentData)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());

                            //コメント数を増やす
                            docRef.update("comnum", FieldValue.increment(1));
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
    }

    private void changeActivity() {
        finish();
    }
}