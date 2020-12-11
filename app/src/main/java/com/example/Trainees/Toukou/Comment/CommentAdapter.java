package com.example.Trainees.Toukou.Comment;

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
import com.example.Trainees.dataclass.CommentData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class CommentAdapter extends ArrayAdapter<CommentData> {
    private final List<CommentData> mCards;

    public CommentAdapter(Context context, int layoutResourceId, List<CommentData> commentData) {
        super(context, layoutResourceId, commentData);

        this.mCards = commentData;
    }

    @Override
    public int getCount() {
        return mCards.size();
    }

    @Nullable
    @Override
    public CommentData getItem(int position) {
        return mCards.get(position);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        final CommentAdapter.ViewHolder viewHolder;

        if (convertView != null) {
            viewHolder = (CommentAdapter.ViewHolder) convertView.getTag();

        } else {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.comment_layout, null);
            viewHolder = new CommentAdapter.ViewHolder();


            viewHolder.Nickname = (TextView) convertView.findViewById(R.id.nickname);
            viewHolder.Comment = (TextView) convertView.findViewById(R.id.comment);
            viewHolder.Proimage = (ImageView) convertView.findViewById(R.id.icon);
            viewHolder.Time = (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(viewHolder);

        }

        CommentData commentData = mCards.get(position);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabase = database.getReference().child("User").child(commentData.getUid());
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //インスタンスの取得
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String nickname = dataSnapshot.child("nickname").getValue().toString();
                    String proimage = dataSnapshot.child("profileimage").getValue().toString();

                    Bitmap probim = decodeBase64(proimage);

                    viewHolder.Nickname.setText(nickname);
                    viewHolder.Proimage.setImageBitmap(probim);

                    Log.d("",data.getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // データの取得に失敗
            }
        });

        viewHolder.Comment.setText(commentData.getComment());
        viewHolder.Time.setText(commentData.getTime());

        return convertView;
    }

    static class ViewHolder {
        TextView Nickname;
        TextView Comment;
        TextView Time;
        ImageView Proimage;
    }

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}

