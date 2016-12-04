package com.corp.theapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ContentFrameLayout;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRReader extends AppCompatActivity   implements  ZXingScannerView.ResultHandler {
    // Değişken tanımlamaları
    private ZXingScannerView mScannerView;
    ContentFrameLayout rootLayout;
    ImageView flash,af;
    Boolean aftoggle;
    TextView flash_text,af_text;
    DatabaseReference mDatabase;
    SweetAlertDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

    }

    protected void init(){
        // FullScreen komutu ve Ekran Ayarlamaları
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        ColorDrawable colorDrawable = new ColorDrawable(0x000000);
        getWindow().setBackgroundDrawable(colorDrawable);
        //END

                    mScannerView = new ZXingScannerView(QRReader.this);
                    setContentView(mScannerView);
                    rootLayout = (ContentFrameLayout) findViewById(android.R.id.content);
                    View.inflate(this, R.layout.qr_tabs, rootLayout);
                    flash = (ImageView) findViewById(R.id.qr_flash);
                    af = (ImageView) findViewById(R.id.qr_af);
                    flash_text = (TextView) findViewById(R.id.qr_flash_text);
                    af_text = (TextView) findViewById(R.id.qr_af_text);
                    mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    protected void permissionCheck(){
        // Marshmallow ve üzeri için camera permission check
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    int permissionCheck = ContextCompat.checkSelfPermission(QRReader.this,
                    Manifest.permission.CAMERA);
            if(permissionCheck == PackageManager.PERMISSION_DENIED){
                    ActivityCompat.requestPermissions(QRReader.this,
                    new String[]{Manifest.permission.CAMERA}, 1);}}
        // END
    }

    protected void flash(){
                if(mScannerView.getFlash())
                                    flash.setImageResource(R.drawable.flash_on);
                else
                                    flash.setImageResource(R.drawable.flash_off);

        flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mScannerView.getFlash()){
                                    flash.setImageResource(R.drawable.flash_off);
                                    mScannerView.setFlash(false);}
                else{
                                    flash.setImageResource(R.drawable.flash_on);
                                    mScannerView.setFlash(true);
                }}});

        flash_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flash.performClick();}});

    }

    protected void af(){
                                    aftoggle=true;
                                    mScannerView.setAutoFocus(true);
                                    af.setImageResource(R.drawable.af_on);

        af.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(aftoggle){
                                    mScannerView.setAutoFocus(false);
                                    af.setImageResource(R.drawable.af_off);
                                    aftoggle=false;}
                else{
                                    mScannerView.setAutoFocus(true);
                                    af.setImageResource(R.drawable.af_on);
                                    aftoggle=true;}}});

        af_text.setOnClickListener(new View.OnClickListener() {
             @Override
            public void onClick(View v) {
                af.performClick();}});
    }

    @Override
    public void onBackPressed() {
                                super.onBackPressed();
                                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @Override
    public void onResume() {
                                super.onResume();

                                    init();
                                    permissionCheck();
                                    flash();
                                    af();

                                    mScannerView.setResultHandler(QRReader.this); // Register ourselves as a handler for scan results.

                                    mScannerView.startCamera();        // Start camera on resume

    }
    @Override
    public void onPause() {
                                super.onPause();


                                    mScannerView.stopCamera();           // Stop camera on pause
    }
    @Override
    public void handleResult(Result result) {
                                    loadingDialog(); // qr Taraması Başarılı Olduğunda Kullanıcıya Gösterilecek Bilgi Ekranı.

                                    QRKontrol(result);

    }

    public void QRKontrol(final Result result){
        mDatabase.child(result.getText()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot snapshot) {
                if(snapshot.getValue()!=null){
                       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                            pDialog.dismissWithAnimation();
                            new SweetAlertDialog(QRReader.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Success!")
                                    .setContentText("Welcome to "+ snapshot.child("isim").getValue()+"!")
                                    .setConfirmText("Continue")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismissWithAnimation();
                                            finish();
                                           /* Intent intent = new Intent(QRReader.this,MainMenu.class);
                                            intent.putExtra("firma_ID",result.getText());
                                            intent.putExtra("firma_AD", snapshot.child("isim").getValue().toString());
                                            intent.putExtra("menu",snapshot.child("mainmenu").getValue().toString());
                                            startActivity(intent);*/
                                            overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                                        }
                                    }).show();}
                else{
                   
                            pDialog.dismissWithAnimation();
                            new SweetAlertDialog(QRReader.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Sorry!")
                                    .setContentText("Qr code not recognized. Please try again.")
                                    .setConfirmText("Okay")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            mScannerView.resumeCameraPreview(QRReader.this);
                                            sweetAlertDialog.dismissWithAnimation();
                                        }
                                    }).show();
                }
}
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });
    }

    protected void loadingDialog(){
                                    pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
                                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#F4511E"));
                                    pDialog.setTitleText("Qr Scan Successful");
                                    pDialog.setContentText("\nPlease Wait...");
                                    pDialog.setCancelable(false);
                                    pDialog.show();
    }

}
