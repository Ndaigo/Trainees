package com.example.Trainees.Login;

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
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.Trainees.HomeActivity;
import com.example.Trainees.R;
import com.example.Trainees.dataclass.UserData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;


public class Set_ProfileActivity extends AppCompatActivity {
    EditText nick,selintro;
    private Uri m_uri;
    private static final int REQUEST_CHOOSER = 1000;
    private ImageView profileview;
    private static final String TAG = "set_Profile";
    private DatabaseReference mDatabase;
    Bitmap probmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_profile);

        profileview = findViewById(R.id.profileimage);
        findViewById(R.id.profileimage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGallery();
            }
        });

        nick = findViewById(R.id.nickname);
        selintro = findViewById(R.id.s_intro);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference().child("User");

        findViewById(R.id.decided).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(profileview.getDrawable() instanceof BitmapDrawable){
                    probmp = ((BitmapDrawable)profileview.getDrawable()).getBitmap();
                }else{
                    probmp = BitmapFactory.decodeResource(getResources(), R.drawable.kinniku);
                }

                String proimage = encodeTobase64(probmp);

                String nickname = nick.getText().toString();
                String selfintro = selintro.getText().toString();
                Intent intent = getIntent();
                String email = intent.getStringExtra( "email" );

                setUserData(email,nickname,selfintro,proimage);

            }
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

    public void setUserData(String email, String nickname, String selfintro,String profileimage){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            UserData userData = new UserData(email, nickname, selfintro,profileimage);
            mDatabase.child(uid).setValue(userData);
            changeActivity();
        }
    }

    public static String encodeTobase64(Bitmap image)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.NO_WRAP);
    }

    private void changeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }


}
