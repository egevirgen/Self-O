package com.corp.theapp;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Menu extends AppCompatActivity {
    private static DatabaseReference mDatabase;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    ArrayList<String> counts = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String headers = dataSnapshot.child("turkiye_antalya_kultur_yemenkahvesi_1").child("menu").child("headers").getValue().toString();
                String [] headers_array = headers.split("--");
                for(int x=1;x<=headers_array.length;x++){
                    try{
                    String temp = dataSnapshot.child("turkiye_antalya_kultur_yemenkahvesi_1").child("menu").child("name_"+x).getValue().toString();
                    String [] temp_array = temp.split("--");
                  counts.add(x-1, String.valueOf(temp_array.length));}
                    catch (NullPointerException ignored){}
                }

                Log.e("counts = ",counts.toString());
                recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                RecyclerView.LayoutManager layoutManager
                        = new LinearLayoutManager(Menu.this, LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(layoutManager);

                adapter = new RecyclerAdapter(headers_array,Menu.this,counts);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });



    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

    }

}
