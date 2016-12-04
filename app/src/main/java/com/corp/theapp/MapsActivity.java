package com.corp.theapp;
import android.Manifest;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, LocationListener,SearchView.OnQueryTextListener{
    LatLng latLng;
    protected static final int RESULT_SPEECH = 1;
    private GoogleMap mMap;
    private BottomSheetBehavior mBottomSheetBehavior;

    double latt[]={36.864727,36.901504,36.892676,36.884583,36.898750};
    double longt[]={30.634416,30.672853,30.667396,30.659156,30.653261};
    // Declare Variables
    LatLng latlong1=null;
    ListView list;
    ListViewAdapter adapter;
    Location latlong=null;
    SearchView editsearch;
    String[] placeList;
    ImageView ivIcon;
    int searchImgId;
    RelativeLayout relativeLayout;
    ImageButton locationbutton;
    ArrayList<PlaceNames> arraylist = new ArrayList<>();

    public void Initialize(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permissionCheck = ContextCompat.checkSelfPermission(MapsActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION);
            if(permissionCheck == PackageManager.PERMISSION_DENIED){
                ActivityCompat.requestPermissions(MapsActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);}}

        locationbutton=(ImageButton)findViewById(R.id.location);
        relativeLayout = (RelativeLayout)findViewById(R.id.deneme);
        editsearch = (SearchView) findViewById(R.id.search);
        final EditText searchEditText = (EditText) editsearch.findViewById(editsearch.getContext().getResources().getIdentifier("android:id/search_src_text", null, null));

        searchEditText.setTextColor(Color.parseColor("#212121"));
        searchEditText.setHintTextColor(Color.parseColor("#616161"));
        searchEditText.setTextSize(14f);
        searchEditText.setBackgroundColor(Color.WHITE);
        editsearch.setBackgroundColor(Color.WHITE);
        setSearchViewEditTextBackgroundColor(this,editsearch);
        View bottomSheet = findViewById(R.id.bottom_sheet);

        searchEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            }
        });

        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setPeekHeight(0);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottomSheet.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                Log.e("KEY PRESS = ","View ="+view+"| i ="+i+"| keyevent="+keyEvent);
                return false;
            }
        });
        // internetten array çekicez TODO
        placeList = new String[]{"Yemen Kahvesi--Antalya--Altınkum--36.864727f--30.634416f", "Osmanlı Kahvecisi--Antalya--Bayındır--36.901504--30.672853", "Burger King--Antalya--Meltem--36.892676--30.667396", "Starbucks--Antalya--Konyaaltı--36.884583--30.659156","Rıhtım Döner--Antalya--Akdeniz Üniversitesi--36.898750--30.653261"};

        list = (ListView) findViewById(R.id.listview);
        for (String placeListString : placeList) {
            PlaceNames placeNames = new PlaceNames(placeListString);
            arraylist.add(placeNames);
        }

        editsearch.setOnQueryTextListener(this);
        editsearch.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                Log.e("focused=",""+b);
                if(b)
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            }
        });

        searchImgId = getResources().getIdentifier("android:id/search_mag_icon", null, null);
        ivIcon = (ImageView) editsearch.findViewById(searchImgId);


        //searchview button listener
        editsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mBottomSheetBehavior.getState()== BottomSheetBehavior.STATE_EXPANDED)
                {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                }
                else if (mBottomSheetBehavior.getState()== BottomSheetBehavior.STATE_COLLAPSED) {

                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                }


            }
        });
        final InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet_1, int newState) {
                // React to state change

                if (newState == BottomSheetBehavior.STATE_EXPANDED) {

                    ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), Color.parseColor("#00FFFFFF"), Color.parseColor("#E0E0E0"));
                    colorAnimation.setDuration(250); // milliseconds
                    colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                        @Override
                        public void onAnimationUpdate(ValueAnimator animator) {
                            relativeLayout.setBackgroundColor((int) animator.getAnimatedValue());
                        }

                    });
                    colorAnimation.start();

                    if(ivIcon!=null) {
                        ivIcon.setImageResource(R.drawable.search_button);
                    }

                    editsearch.requestFocus();

                }

                else if(newState==BottomSheetBehavior.STATE_COLLAPSED){

                    ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), Color.parseColor("#E0E0E0"), Color.parseColor("#00FFFFFF"));
                    colorAnimation.setDuration(250); // milliseconds
                    colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                        @Override
                        public void onAnimationUpdate(ValueAnimator animator) {
                            relativeLayout.setBackgroundColor((int) animator.getAnimatedValue());
                        }

                    });
                    colorAnimation.start();
                    imm.hideSoftInputFromWindow(bottomSheet_1.getWindowToken(),0);
                    editsearch.clearFocus();
                    ivIcon.setImageResource(R.drawable.searchview_search);
                }

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });


        locationbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Location();
            }
        });

        ImageButton btnSpeak = (ImageButton) findViewById(R.id.mic);
        btnSpeak.setBackgroundColor(Color.WHITE);
        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, Locale.getDefault());

                try {
                    startActivityForResult(intent, RESULT_SPEECH);
                    searchEditText.setText("");
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getApplicationContext(),
                            "Your device doesn't support Speech to Text",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });






    }

    @Override
    public void onBackPressed() {
        if(mBottomSheetBehavior.getState()==BottomSheetBehavior.STATE_EXPANDED)
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        else
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final SearchView searchView = (SearchView) findViewById(R.id.search);
        final EditText searchEditText = (EditText) searchView.findViewById(searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null));

        switch (requestCode) {
            case RESULT_SPEECH: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> text = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    searchEditText.setText(text.get(0));
                }
                break;
            }

        }
    }


    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.filter(newText);
        return false;
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Initialize();
        mMap = googleMap;
        MapStyleOptions mapStyleOptions = MapStyleOptions.loadRawResourceStyle(this,R.raw.style_json);
        mMap.setMapStyle(mapStyleOptions);
        adapter = new ListViewAdapter(this, arraylist, mMap, mBottomSheetBehavior);
        list.setAdapter(adapter);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location();
            mMap.setMyLocationEnabled(true);
        }


        for (int i=0;i<5;i++) {

            latLng = new LatLng(latt[i],longt[i]);
            mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).title(placeList[i].split("--")[0]).snippet(placeList[i].split("--")[1]+"/"+ placeList[i].split("--")[2]));
        }
    }


    public void Location() {
        Handler mainHandler = new Handler(getApplicationContext().getMainLooper());

        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {try {
                latlong = mMap.getMyLocation();
                latlong1 = new LatLng(latlong.getLatitude(), latlong.getLongitude());
         // Animate fonksiyonu map açıldığında dönecek şekilde ayarlanacak. TODO //
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlong1,12));
            } catch (NullPointerException | IllegalStateException e) {

            }}
        };
        mainHandler.postDelayed(myRunnable,500);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    public static void setSearchViewEditTextBackgroundColor(Context context, SearchView searchView){
        int searchPlateId = context.getResources().getIdentifier("android:id/search_plate", null, null);
        ViewGroup viewGroup = (ViewGroup) searchView.findViewById(searchPlateId);
        viewGroup.setBackgroundColor(Color.WHITE);
    }


}

