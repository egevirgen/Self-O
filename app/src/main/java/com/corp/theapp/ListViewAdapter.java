package com.corp.theapp;


import android.content.Context;
import android.support.design.widget.BottomSheetBehavior;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class ListViewAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    GoogleMap mMap;
    private List<PlaceNames> placeNamesList = null;
    BottomSheetBehavior bottomSheetBehavior;
    private ArrayList<PlaceNames> arraylist;

    ListViewAdapter(Context context, List<PlaceNames> placeNamesList, GoogleMap mMap, BottomSheetBehavior mBottomSheetBehavior) {
        this.placeNamesList = placeNamesList;
        inflater = LayoutInflater.from(context);
        this.arraylist = new ArrayList<>();
        this.arraylist.addAll(placeNamesList);
        this.mMap=mMap;
        bottomSheetBehavior = mBottomSheetBehavior;
    }

    private class ViewHolder {
        TextView name;
        TextView sehir;
        TextView ilce;
        Button tag;
        ImageView location;
    }

    @Override
    public int getCount() {
        return placeNamesList.size();
    }

    @Override
    public PlaceNames getItem(int position) {
        return placeNamesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.listview_item, null);
            // Locate the TextViews in listview_item.xml
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.sehir = (TextView) view.findViewById(R.id.sehir);
            holder.ilce = (TextView) view.findViewById(R.id.ilce);
            holder.tag = (Button) view.findViewById(R.id.button_tag);
            holder.location = (ImageView) view.findViewById(R.id.location);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        try {
            holder.name.setText(placeNamesList.get(position).getPlaceName().split("--")[0]);
            holder.sehir.setText(placeNamesList.get(position).getPlaceName().split("--")[1]+", ");
            holder.ilce.setText(placeNamesList.get(position).getPlaceName().split("--")[2]);
            holder.tag.setText(placeNamesList.get(position).getPlaceName().split("--")[0].substring(0,2));
            holder.location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    LatLng latLng = new LatLng(Float.parseFloat(placeNamesList.get(position).getPlaceName().split("--")[3]),Float.parseFloat(placeNamesList.get(position).getPlaceName().split("--")[4]));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));}
            });
        }catch (IndexOutOfBoundsException | NullPointerException ignored) {
        }

        return view;
    }

    // Filter Class
    void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        placeNamesList.clear();
        if (charText.length() == 0) {
            placeNamesList.addAll(arraylist);
        } else {
            for (PlaceNames wp : arraylist ) {
                if (wp.getPlaceName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    placeNamesList.add(wp);
                }



            }

        }
        notifyDataSetChanged();
    }

}