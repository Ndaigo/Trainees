package com.example.Trainees.Profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.Trainees.HomeActivity;
import com.example.Trainees.R;
import com.example.Trainees.dataclass.UserData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;

public class Change_ProfileActivity extends AppCompatActivity {
    EditText Nickname,Selfintro;
    ImageView Profileview;
    private Uri m_uri;
    private static final int REQUEST_CHOOSER = 1000;
    private static final String TAG = "Change_ProfileActivity";
    public String uid;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile);

        Intent intent = getIntent();
        uid = intent.getStringExtra("UserID");

        Nickname = findViewById(R.id.nickname);
        Selfintro = findViewById(R.id.s_intro);
        Profileview = findViewById(R.id.profileimage);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference().child("User").child(uid);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //インスタンスの取得
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String nickname = dataSnapshot.child("nickname").getValue().toString();
                    String s_intro = dataSnapshot.child("selfintro").getValue().toString();
                    String proimage = dataSnapshot.child("profileimage").getValue().toString();

                    Nickname.setText(nickname);
                    Selfintro.setText(s_intro);

                    Bitmap probim = decodeBase64(proimage);
                    Profileview.setImageBitmap(probim);

                    Log.d("",data.getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // データの取得に失敗
            }
        });


        Profileview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGallery();
            }
        });

        Button decidedBT = findViewById(R.id.decided);
        decidedBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap probmp = ((BitmapDrawable)Profileview.getDrawable()).getBitmap();
                String proimage = encodeTobase64(probmp);

                String nickname = Nickname.getText().toString();
                String selfintro = Selfintro.getText().toString();

                ChangeUserData(nickname,selfintro,proimage);

            }
        });

        ImageButton backBt = findViewById(R.id.backbutton);
        backBt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { finish(); }
        });
    }

    private void showGallery() {
        String photoName = System.currentTimeMillis() + ".jpg";
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, photoName);
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        m_uri = getContentResolver()
                .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, m_uri);

        // ギャラリー用のIntent作成
        Intent intentGallery;
        if (Build.VERSION.SDK_INT < 19) {
            intentGallery = new Intent(Intent.ACTION_GET_CONTENT);
            intentGallery.setType("image/*");
        } else {
            intentGallery = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intentGallery.addCategory(Intent.CATEGORY_OPENABLE);
            intentGallery.setType("image/jpeg");
        }
        Intent intent = Intent.createChooser(intentCamera, "画像の選択");
        intent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {intentGallery});
        startActivityForResult(intent, REQUEST_CHOOSER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CHOOSER) {

            if(resultCode != RESULT_OK) {
                return; }

            Uri resultUri = (data != null ? data.getData() : m_uri);
            if(resultUri == null) {
                return; }

            MediaScannerConnection.scanFile(
                    this,
                    new String[]{resultUri.getPath()},
                    new String[]{"image/jpeg"},
                    null
            );
            ImageView imageView = findViewById(R.id.profileimage);
            imageView.setImageURI(resultUri);
        }
    }

    public void ChangeUserData(String nickname, String selfintro,String profileimage){
        mDatabase.child("nickname").setValue(nickname);
        mDatabase.child("profileimage").setValue(profileimage);
        mDatabase.child("selfintro").setValue(selfintro);
        changeActivity();
    }

    public static String encodeTobase64(Bitmap image)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.NO_WRAP);
    }

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    private void changeActivity() {
        Intent intent = new Intent(this, MypageActivity.class);
        startActivity(intent);
        finish();
    }
}