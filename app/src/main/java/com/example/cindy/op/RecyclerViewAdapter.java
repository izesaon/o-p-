package com.example.cindy.op;


import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.net.Proxy;
import java.util.List;

/**
 * Created by YiLong on 24/1/18.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolders> {
    private List<People> peopleList;
    protected Context context;


    public RecyclerViewAdapter(Context context, List<People> peopleList){
        this.peopleList = peopleList;
        this.context = context;


    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerViewHolders viewHolder = null;
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.peoplelistitem, parent, false);
        viewHolder = new RecyclerViewHolders(layoutView, peopleList);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, int position) {

        holder.personName.setText(peopleList.get(position).name);

        Log.i("name",peopleList.get(position).name);
        Log.i("contact", String.valueOf(peopleList.get(position).contact));
       holder.contact.setText(peopleList.get(position).contact+"");
        holder.owed.setText(Double.toString(peopleList.get(position).amount));

    }

    @Override
    public int getItemCount() {
        return this.peopleList.size();
    }
}
