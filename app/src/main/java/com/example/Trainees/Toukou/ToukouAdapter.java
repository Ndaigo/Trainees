package com.example.Trainees.Toukou;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.Trainees.R;
import com.example.Trainees.dataclass.ToukouData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ToukouAdapter extends ArrayAdapter<ToukouData> {
    public FirebaseFirestore db;
    private List<ToukouData> mCards;

    public ToukouAdapter(Context context, int layoutResourceId, List<ToukouData> toukouData) {
        super(context, layoutResourceId, toukouData);

        this.mCards = toukouData;
    }

    @Override
    public int getCount() {
        return mCards.size();
    }

    @Nullable
    @Override
    public ToukouData getItem(int position) {
        return mCards.get(position);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();

        } else {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.home_toukou_layout, null);
            viewHolder = new ViewHolder();


            viewHolder.Nickname = (TextView) convertView.findViewById(R.id.nickname);
            viewHolder.TitleText = (TextView) convertView.findViewById(R.id.title_main);
            viewHolder.MenuText = (TextView) convertView.findViewById(R.id.menu_main);
            viewHolder.Time = (TextView) convertView.findViewById(R.id.time);
            viewHolder.Comnum = (TextView) convertView.findViewById(R.id.comnum);
            viewHolder.proimage = (ImageView) convertView.findViewById(R.id.icon);
            viewHolder.Comicon = (ImageView) convertView.findViewById(R.id.comicon);
            convertView.setTag(viewHolder);

        }

        ToukouData toukouData = mCards.get(position);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabase = database.getReference().child("User").child(toukouData.getUid());
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //インスタンスの取得
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String nickname = dataSnapshot.child("nickname").getValue().toString();
                    String proimage = dataSnapshot.child("profileimage").getValue().toString();

                    Bitmap probim = decodeBase64(proimage);

                    viewHolder.Nickname.setText(nickname);
                    viewHolder.proimage.setImageBitmap(probim);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // データの取得に失敗
            }
        });

        viewHolder.TitleText.setText(toukouData.getTitle());
        viewHolder.MenuText.setText(toukouData.getMenu());
        viewHolder.Time.setText(toukouData.getTime());
        viewHolder.Comnum.setText(String.valueOf(toukouData.getComnum()));
        viewHolder.Comicon.setImageResource(R.drawable.chaticon);

        return convertView;
    }

    static class ViewHolder {
        TextView Nickname;
        TextView TitleText;
        TextView MenuText;
        TextView Time;
        TextView Comnum;
        ImageView proimage;
        ImageView Comicon;
    }

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
