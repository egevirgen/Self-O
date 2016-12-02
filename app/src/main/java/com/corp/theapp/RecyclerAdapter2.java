package com.corp.theapp;
import android.app.Activity;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RecyclerAdapter2 extends RecyclerView.Adapter<RecyclerAdapter2.ViewHolder> {

    private static DatabaseReference mDatabase;
    private int count;
    String counts;
    String [] name_array,info_array,price_array,calorie_array;
    Activity mainActivity;

    public RecyclerAdapter2(int i, String s, Activity mainActivity){
        count = i;

        counts = s;

        this.mainActivity = mainActivity;

    }



    @Override

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // create a new view
        View view = LayoutInflater.from(parent.getContext())

                .inflate(R.layout.item_list2, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name1 = dataSnapshot.child("turkiye_antalya_kultur_yemenkahvesi_1").child("menu").child("name_"+count).getValue().toString();
                String price1 = dataSnapshot.child("turkiye_antalya_kultur_yemenkahvesi_1").child("menu").child("price_"+count).getValue().toString();
                String info1 = dataSnapshot.child("turkiye_antalya_kultur_yemenkahvesi_1").child("menu").child("info_"+count).getValue().toString();
                String calorie1 = dataSnapshot.child("turkiye_antalya_kultur_yemenkahvesi_1").child("menu").child("calorie_"+count).getValue().toString();

                calorie_array = calorie1.split("--");
                name_array = name1.split("--");
                price_array = price1.split("--");
                info_array = info1.split("--");



            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return viewHolder;
    }

public void setText(final ViewHolder holder, final int position){

        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try{
                holder.textView.setText(name_array[position]);
                holder.textView2.setText(info_array[position]);
                holder.textView3.setText(" "+(position+1)+" ");
                holder.textView4.setText(price_array[position]+"₺");
                holder.textView5.setText(calorie_array[position]+" Cal");


                }
                catch (NullPointerException ignored){
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {setText(holder,position);}});
                    thread.start();

                    try {thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}}}});
}

    @Override

    public void onBindViewHolder(final ViewHolder holder, final int position) {
                setText(holder,position);
    }



    @Override
    public int getItemCount() {
            return Integer.parseInt(counts);
    }



    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textView,textView2,textView3,textView4,textView5;
        public CardView imageView;
        public RelativeLayout container;

        public ViewHolder(View itemView) {

            super(itemView);

            container = (RelativeLayout) itemView.findViewById(R.id.container_itemlist2);
            textView = (TextView) itemView.findViewById(R.id.atextView1);
            textView2 = (TextView) itemView.findViewById(R.id.atextView2);
            textView3 = (TextView) itemView.findViewById(R.id.atextView3);
            textView4 = (TextView) itemView.findViewById(R.id.atextView4);
            textView5 = (TextView) itemView.findViewById(R.id.atextView5);
            imageView = (CardView) itemView.findViewById(R.id.aimageView1Holder);
        }

    }

}