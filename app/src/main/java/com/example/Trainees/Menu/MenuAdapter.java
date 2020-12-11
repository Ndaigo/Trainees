package com.example.Trainees.Menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.Trainees.R;
import com.example.Trainees.dataclass.MenuData;

import java.util.List;

public class MenuAdapter extends ArrayAdapter<MenuData> {

    private List<MenuData> mCards;

    public MenuAdapter(Context context, int layoutResourceId, List<MenuData> menuData) {
        super(context, layoutResourceId, menuData);

        this.mCards = menuData;
    }

    @Override
    public int getCount() {
        return mCards.size();
    }

    @Nullable
    @Override
    public MenuData getItem(int position) {
        return mCards.get(position);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        final MenuAdapter.ViewHolder viewHolder;

        if (convertView != null) {
            viewHolder = (MenuAdapter.ViewHolder) convertView.getTag();

        } else {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.menu_layout, null);
            viewHolder = new MenuAdapter.ViewHolder();


            viewHolder.Title = (TextView) convertView.findViewById(R.id.Title);
            viewHolder.Next = (ImageView) convertView.findViewById(R.id.NextView);
            convertView.setTag(viewHolder);

        }

        MenuData menuData = mCards.get(position);

        viewHolder.Title.setText(menuData.getTitle());
        viewHolder.Next.setImageResource(R.drawable.yazirushi);

        return convertView;
    }

    static class ViewHolder {
        TextView Title;
        ImageView Next;
    }
}


