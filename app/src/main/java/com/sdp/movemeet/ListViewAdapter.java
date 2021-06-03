package com.sdp.movemeet;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdp.movemeet.view.TextWorkoutActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListViewAdapter extends BaseAdapter {

    //variables
    Context mContext;
    LayoutInflater inflater;
    List<Model> modellist;
    ArrayList<Model> arrayList;

    //constructor
    public ListViewAdapter(Context context, List<Model> modellist) {
        mContext = context;
        this.modellist = modellist;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<Model>();
        this.arrayList.addAll(modellist);
    }

    public class ViewHolder{
        TextView mTitleTv, mDescTv;
        ImageView mIconIv;
    }

    @Override
    public int getCount() {
        return modellist.size();
    }

    @Override
    public Object getItem(int i) {
        return modellist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view==null){
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.row, null);

            //locate the views in row.xml
            holder.mTitleTv = view.findViewById(R.id.mainTitle);
            holder.mDescTv = view.findViewById(R.id.mainDescription);
            holder.mIconIv = view.findViewById(R.id.mainIcon);

            view.setTag(holder);

        }
        else {
            holder = (ViewHolder)view.getTag();
        }
        //set the results into textviews
        holder.mTitleTv.setText(modellist.get(position).getTitle());
        holder.mDescTv.setText(modellist.get(position).getDesc());
        //set the result in imageview
        holder.mIconIv.setImageResource(modellist.get(position).getIcon());

        //listview item clicks
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //code later
                if (modellist.get(position).getTitle().equals("Abs Workout")){
                    //start NewActivity with title for actionbar and text for textview
                    Intent intent = new Intent(mContext, TextWorkoutActivity.class);
                    //intent.putExtra("actionBarTitle", "Battery");
                    intent.putExtra("contentTv", "15 Leg crunches\n6 Leg Raises\n");
                    mContext.startActivity(intent);
                }
                if (modellist.get(position).getTitle().equals("Leg Workout")){
                    //start NewActivity with title for actionbar and text for textview
                    Intent intent = new Intent(mContext, TextWorkoutActivity.class);
                    //intent.putExtra("actionBarTitle", "Cpu");
                    intent.putExtra("contentTv", "15 Leg crunches\n6 Leg Raises\n");
                    mContext.startActivity(intent);
                }
                if (modellist.get(position).getTitle().equals("Upper Body Workout")) {
                    //start NewActivity with title for actionbar and text for textview
                    Intent intent = new Intent(mContext, TextWorkoutActivity.class);
                    //intent.putExtra("actionBarTitle", "Display");
                    intent.putExtra("contentTv", "15 Leg crunches\n6 Leg Raises\n");
                    mContext.startActivity(intent);
                }
            }
        });


        return view;
    }

    //filter
    public void filter(String charText){
        charText = charText.toLowerCase(Locale.getDefault());
        modellist.clear();
        if (charText.length()==0){
            modellist.addAll(arrayList);
        }
        else {
            for (Model model : arrayList){
                if (model.getTitle().toLowerCase(Locale.getDefault())
                        .contains(charText)){
                    modellist.add(model);
                }
            }
        }
        notifyDataSetChanged();
    }

}