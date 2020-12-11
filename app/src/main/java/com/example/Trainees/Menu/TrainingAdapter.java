package com.example.Trainees.Menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.Trainees.R;
import com.example.Trainees.dataclass.TrainingData;

import java.util.List;

public class TrainingAdapter extends ArrayAdapter<TrainingData> {

    private List<TrainingData> mCards;

    public TrainingAdapter(Context context, int layoutResourceId, List<TrainingData> trainingData) {
        super(context, layoutResourceId, trainingData);

        this.mCards = trainingData;
    }

    @Override
    public int getCount() {
        return mCards.size();
    }

    @Nullable
    @Override
    public TrainingData getItem(int position) {
        return mCards.get(position);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        final TrainingAdapter.ViewHolder viewHolder;

        if (convertView != null) {
            viewHolder = (TrainingAdapter.ViewHolder) convertView.getTag();

        } else {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.training_layout, null);
            viewHolder = new TrainingAdapter.ViewHolder();


            viewHolder.Title = (TextView) convertView.findViewById(R.id.training_main);
            viewHolder.Details = (TextView) convertView.findViewById(R.id.details_text);

            convertView.setTag(viewHolder);

        }

        TrainingData trainingData = mCards.get(position);

        viewHolder.Title.setText(trainingData.getTraining());

        String str = "";
        if(!trainingData.getWeight().equals("")){
            str += ("重さ  " + trainingData.getWeight()+"\n");
        }
        if(!trainingData.getCount().equals("")){
            str += ("回数  " + trainingData.getCount()+"\n");
        }
        if(!trainingData.getTimes().equals("")){
            str += ("時間  " + trainingData.getTimes()+"\n");
        }
        viewHolder.Details.setText(str);

        return convertView;
    }

    static class ViewHolder {
        TextView Title;
        TextView Details;
    }
}
