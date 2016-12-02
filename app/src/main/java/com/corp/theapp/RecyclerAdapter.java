package com.corp.theapp;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private static DatabaseReference mDatabase;
    private String[] dataSource;
    ArrayList<String> count;
    static RecyclerView recyclerView2,recyclerView;
    private RecyclerView.Adapter adapter;
    static Activity mainActivity;
    static RecyclerView.LayoutManager layoutManager;
    static int firstClick;
    static int secondClick;

    public RecyclerAdapter(String[] dataArgs, Menu mainActivity, ArrayList<String> counts){

        dataSource = dataArgs;

        this.mainActivity = mainActivity;

        count = counts;

    }



    @Override

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // create a new view

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list, parent, false);

        layoutManager = new LinearLayoutManager(mainActivity, LinearLayoutManager.HORIZONTAL, false);

        ViewHolder viewHolder = new ViewHolder(view);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        return viewHolder;

    }



    @Override

    public void onBindViewHolder(ViewHolder holder, final int position) {
        try{
                holder.textView.setText(dataSource[position]);
                adapter = new RecyclerAdapter2(position+1,count.get(position),mainActivity);
                recyclerView2.setAdapter(adapter);}
        catch (IndexOutOfBoundsException ignored){}
    }



    @Override

    public int getItemCount() {

        return dataSource.length;

    }



    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textView;
        LinearLayout container;


        public ViewHolder(View itemView) {

            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView1);
            container = (LinearLayout) itemView.findViewById(R.id.item_list_container);
            recyclerView2 = (RecyclerView) itemView.findViewById(R.id.recycler_view2);
            recyclerView = (RecyclerView) mainActivity.findViewById(R.id.recycler_view);
            recyclerView2.setHasFixedSize(true);
            recyclerView2.setLayoutManager(layoutManager);
            recyclerView.addOnItemTouchListener(
                    new RecyclerItemClickListener(mainActivity, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override public void onItemClick(View view, int position) {
                            firstClick=position;

                        }
                    })
            );
            recyclerView2.addOnItemTouchListener(
                    new RecyclerItemClickListener(mainActivity, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override public void onItemClick(View view, int position) {
                           secondClick=position;

                            Log.e("_"+firstClick,"_"+secondClick);
                        }
                    })
            );

        }

    }

}