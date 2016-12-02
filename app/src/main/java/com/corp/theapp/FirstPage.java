package com.corp.theapp;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.multidex.MultiDex;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class FirstPage extends AppCompatActivity {

    static CallbackManager callbackManager;
    private static FirebaseAuth mAuth;
    private static DatabaseReference mDatabase;
    private static FirebaseAuth.AuthStateListener mAuthListener;
    static TabLayout tabLayout;
    Typeface header_font,billabong;
    static int height;
    static ImageView profile_edit;
    static TextView textView_welcome;
    static RelativeLayout signup_container,signed_in_container,start_container;
    static CircleImageView profile_image;
    private static final int SELECT_PICTURE = 1;
    private static StorageReference mStorageRef;
    static String path;
    static CircleImageView editPhoto;
    static Bitmap scale;
    static TextView profile_name;
    static TextView profile_mail;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        SdkInit();
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_first_page);
        Initialize();
        SignedInDecider();
 }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        ProfilePhotoEdit(requestCode,resultCode,data);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";
            public PlaceholderFragment() {
        }
                public static PlaceholderFragment newInstance(int sectionNumber) {
                PlaceholderFragment fragment = new PlaceholderFragment();
                Bundle args = new Bundle();
                args.putInt(ARG_SECTION_NUMBER, sectionNumber);
                fragment.setArguments(args);
                return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView;
            AppBarLayout appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbar);
            if(getArguments().getInt(ARG_SECTION_NUMBER)==1){
                rootView = inflater.inflate(R.layout.fragment_sign_in, container, false);
                final TextView forgot = (TextView) rootView.findViewById(R.id.textview_3);
                forgot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LayoutInflater inflater = LayoutInflater.from(getActivity());
                        View alertView = inflater.inflate(R.layout.forgot_password_layout, null);
                        final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                        alert.setView(alertView);
                        final AlertDialog show = alert.show();
                        show.show();
                        TextView canc,cont,header;
                        final ProgressBar progressBar = (ProgressBar) alertView.findViewById(R.id.progress_bar_2);
                        final MaterialEditText input;
                        canc = (TextView) alertView.findViewById(R.id.textView_7);
                        cont = (TextView) alertView.findViewById(R.id.textView_8);
                        header = (TextView) alertView.findViewById(R.id.textView_5);
                        Typeface face = Typeface.createFromAsset(getActivity().getAssets(),
                                "fonts/Font.ttf");
                        header.setTypeface(face);;
                        input = (MaterialEditText) alertView.findViewById(R.id.edittext_7);
                        cont.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(input.length()!=0)
                                { progressBar.setVisibility(View.VISIBLE);
                                    mAuth.sendPasswordResetEmail(input.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) getActivity().findViewById(R.id.main_content);
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Snackbar snackbar;
                                                snackbar = Snackbar.make(coordinatorLayout, "Password reset email has been sent.", Snackbar.LENGTH_SHORT);
                                                snackbar.getView().setBackgroundColor(Color.parseColor("#006064"));
                                                snackbar.show();
                                            }
                                            else{
                                                task.addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.e("exception = ",""+e);
                                                        if(e.toString().contains("INVALID_EMAIL")){
                                                            Snackbar snackbar;
                                                            snackbar = Snackbar.make(coordinatorLayout, "Email is not valid.", Snackbar.LENGTH_SHORT);
                                                            snackbar.getView().setBackgroundColor(Color.parseColor("#006064"));
                                                            snackbar.show();
                                                        }
                                                        if(e.toString().contains("FirebaseAuthInvalidUserException")){
                                                            Snackbar snackbar;
                                                            snackbar = Snackbar.make(coordinatorLayout, "There is no user record corresponding to this identifier.", Snackbar.LENGTH_SHORT);
                                                            snackbar.getView().setBackgroundColor(Color.parseColor("#006064"));
                                                            snackbar.show();
                                                        }
                                                        if(e.toString().contains("Network Error")){
                                                            Snackbar snackbar;
                                                            snackbar = Snackbar.make(coordinatorLayout, "Connection problem.", Snackbar.LENGTH_SHORT);
                                                            snackbar.getView().setBackgroundColor(Color.parseColor("#006064"));
                                                            snackbar.show();
                                                        }
                                                    }
                                                });

                                            } progressBar.setVisibility(View.GONE);show.dismiss();
                                        }
                                    });

                                }
                            }
                        });
                        canc.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {show.dismiss();}});
                        show.show();
                    }
                });

                signInEmail(rootView, getActivity(), appBarLayout);
                signInFacebook(rootView,getActivity());
              }
            else {
                rootView = inflater.inflate(R.layout.fragment_sign_up, container, false);
                signup_container = (RelativeLayout) rootView.findViewById(R.id.sign_up_container);
                signUp(rootView,getActivity());
            }
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Sign In";
                case 1:
                    return "Sign Up";
            }
            return null;
        }
    }


    public static void notLoggedIn(int height, Activity activity){


        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////// NOT LOGGED IN

        final AppBarLayout appBarLayout = (AppBarLayout) activity.findViewById(R.id.appbar);
        ValueAnimator slideAnimator = ValueAnimator
                .ofInt(height, (height/5))
                .setDuration(700);
        slideAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // get the value the interpolator is at
                // I'm going to set the layout's height 1:1 to the tick
                appBarLayout.getLayoutParams().height = (Integer) animation.getAnimatedValue();
                // force all layouts to see which ones are affected by
                // this layouts height change
                appBarLayout.requestLayout();

            }
        });

        final AnimatorSet set = new AnimatorSet();
        set.play(slideAnimator);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                        start_container.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (mAuthListener != null) {
                    mAuth.removeAuthStateListener(mAuthListener);
                }
                textView_welcome.setVisibility(View.VISIBLE);
                textView_welcome.animate().alpha(1).withLayer();
                signup_container.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        set.setInterpolator(new FastOutSlowInInterpolator());
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                set.start();
            }
        };
        handler.postDelayed(runnable,500);

///////////////////////////////////////////////////////////////////////////////////////////////////////////////// NOT LOGGED IN
    }

    public static void signInEmail(final View rootView, final Activity activity, final AppBarLayout appBarLayout){

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////// SIGN IN WITH EMAIL
        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) activity.findViewById(R.id.main_content);
        final MaterialEditText email,password;
        Button signin;

        email = (MaterialEditText) rootView.findViewById(R.id.edittext_1);
        password = (MaterialEditText) rootView.findViewById(R.id.edittext_2);
        password.setTransformationMethod(new PasswordTransformationMethod());
        signin = (Button) rootView.findViewById(R.id.button_1);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email.clearFocus();password.clearFocus();
                InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                if(!email.getText().toString().equals("") & !password.getText().toString().equals("") ){

                    final ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar_1);
                    progressBar.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                            .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        task.addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressBar.setVisibility(View.GONE);
                                                Snackbar snackbar;
                                                Log.e("exception = ",""+e);
                                                if(e.toString().contains("The email address is badly formatted.")){
                                                    // Please check your Email
                                                    snackbar = Snackbar.make(coordinatorLayout, "Please check your Email", Snackbar.LENGTH_SHORT);
                                                    snackbar.getView().setBackgroundColor(Color.parseColor("#006064"));
                                                    snackbar.show();
                                                }
                                                if(e.toString().contains("There is no user record corresponding to this identifier. The user may have been deleted.")){
                                                    // User not found
                                                    snackbar = Snackbar.make(coordinatorLayout, "User not found", Snackbar.LENGTH_SHORT);
                                                    snackbar.getView().setBackgroundColor(Color.parseColor("#006064"));
                                                    snackbar.show();

                                                }
                                                if(e.toString().contains("The password is invalid or the user does not have a password.")){
                                                    //  password is invaid
                                                    password.setText("");
                                                    snackbar = Snackbar.make(coordinatorLayout, "The password is invalid", Snackbar.LENGTH_SHORT);
                                                    snackbar.getView().setBackgroundColor(Color.parseColor("#006064"));
                                                    snackbar.show();

                                                }
                                                if(e.toString().contains("com.google.firebase.FirebaseNetworkException")){
                                                   // A network error occured
                                                    snackbar = Snackbar.make(coordinatorLayout, "A network error occured", Snackbar.LENGTH_SHORT);
                                                    snackbar.getView().setBackgroundColor(Color.parseColor("#006064"));
                                                    snackbar.show();


                                                }
                                            }
                                        });
                                    }
                                    else{
                                        Log.e("login =","successful");
                                        if(mAuth.getCurrentUser().isEmailVerified()){
                                        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                               final String name = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Name").getValue().toString();
                                                final String mail = mAuth.getCurrentUser().getEmail();

                                                profile_name = (TextView) activity.findViewById(R.id.profile_name);
                                                profile_mail = (TextView) activity.findViewById(R.id.profile_mail);
                                                profile_image = (CircleImageView) activity.findViewById(R.id.profile_image);
                                                try {
                                                    final File localFile = File.createTempFile("images", "jpg");
                                                    final Bitmap[] bitmap = new Bitmap[1];
                                                    mStorageRef.child(mAuth.getCurrentUser().getUid()).child("profile_image.jpg").getFile(localFile).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                                                            if(task.isSuccessful()){
                                                                bitmap[0] = BitmapFactory.decodeFile(localFile.getPath());
                                                                profile_image.setImageBitmap(bitmap[0]);
                                                                Bitmap blurredBitmap = BlurBuilder.blur( activity, bitmap[0] );
                                                                Drawable blurredDrawable = new BitmapDrawable(activity.getResources(), blurredBitmap );
                                                                ImageView background = (ImageView) activity.findViewById(R.id.profile_background);
                                                                background.setImageDrawable(blurredDrawable);
                                                                profile_name.setText(name);
                                                                profile_mail.setText(mail);
                                                                textView_welcome.animate().alpha(0).withLayer().withEndAction(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        textView_welcome.setVisibility(View.GONE);
                                                                        signed_in_container.setVisibility(View.VISIBLE);
                                                                        signed_in_container.animate().alpha(1).withLayer();
                                                                        signInAnimation(appBarLayout);
                                                                        signInBehaviour(activity);
                                                                        profile_edit.setVisibility(View.VISIBLE);
                                                                        progressBar.setVisibility(View.GONE);
                                                                    }
                                                                });

                                                            }
                                                            else{
                                                                final Bitmap[] bitmap = {null};

                                                                bitmap[0] = BitmapFactory.decodeResource(activity.getResources(),
                                                                        R.drawable.person);
                                                                profile_image.setImageBitmap(bitmap[0]);
                                                                profile_name.setText(name);
                                                                profile_mail.setText(mail);
                                                                Bitmap blurredBitmap = BlurBuilder.blur( activity, bitmap[0] );
                                                                Drawable blurredDrawable = new BitmapDrawable(activity.getResources(), blurredBitmap );
                                                                ImageView background = (ImageView) activity.findViewById(R.id.profile_background);
                                                                background.setImageDrawable(blurredDrawable);
                                                                textView_welcome.animate().alpha(0).withLayer().withEndAction(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        progressBar.setVisibility(View.GONE);
                                                                        textView_welcome.setVisibility(View.GONE);
                                                                        signed_in_container.setVisibility(View.VISIBLE);
                                                                        signed_in_container.animate().alpha(1).withLayer();
                                                                        signInAnimation(appBarLayout);
                                                                        signInBehaviour(activity);
                                                                        profile_edit.setVisibility(View.VISIBLE);
                                                                    }
                                                                });
                                                            }
                                                        }
                                                    });

                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }




                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });}
                                        else{
                                            mAuth.signOut();
                                            progressBar.setVisibility(View.GONE);
                                            new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
                                                    .setTitleText("Email is not verified")
                                                    .setContentText("Please verify your email before continue.")
                                                    .setConfirmText("Okay!")
                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                            sweetAlertDialog.dismiss();
                                                        }
                                                    }).show();
                                        }

                                    }
                                }
                            });}
                else{
                   // Email or Password cannot be empty
                    Snackbar snackbar;
                    snackbar = Snackbar.make(coordinatorLayout, "Email or Password cannot be empty", Snackbar.LENGTH_SHORT);
                    snackbar.getView().setBackgroundColor(Color.parseColor("#006064"));
                    snackbar.show();

                }
            }
        });

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////// SIGN IN WITH EMAIL

    }

    public static void signInFacebook(final View rootView, final Activity activity){

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////// SIGN IN WITH FACEBOOK
        final LoginButton fblogin;
        Button fblogin_replace;
        fblogin_replace = (Button) rootView.findViewById(R.id.button_2);
        fblogin = new LoginButton(activity);
        fblogin.setReadPermissions("public_profile");
        fblogin.setReadPermissions("email");
        fblogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // Eğer Facebook oturum açması başarılı olduysa Firebase Database'inde bu bilgileri kullanarak oturum açmaya yarayan komut.
                Log.e("success","1");
               ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar_1);
                progressBar.setVisibility(View.VISIBLE);
                handleFacebookAccessToken(loginResult.getAccessToken().getToken(),activity,progressBar);
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

        fblogin_replace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fblogin.performClick();
            }
        });

    }

    private static void handleFacebookAccessToken(String token, final Activity activity, final ProgressBar progressBar) {
        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) activity.findViewById(R.id.main_content);
        final AppBarLayout appBarLayout = (AppBarLayout) activity.findViewById(R.id.appbar);
        AuthCredential credential = FacebookAuthProvider.getCredential(token);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            task.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("exception = ", "" + e);
                                    if (e.toString().contains("The user account has been disabled by an administrator.")) {
                                        LoginManager.getInstance().logOut();
                                        Snackbar snackbar;
                                        snackbar = Snackbar.make(coordinatorLayout, "Your account has been disabled", Snackbar.LENGTH_SHORT);
                                        snackbar.getView().setBackgroundColor(Color.parseColor("#006064"));
                                        snackbar.show();


                                    }
                                }
                            });
                        }
                        else{
                            Log.e("login =","successful");
                            mDatabase.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Name").setValue(Profile.getCurrentProfile().getName());
                            mDatabase.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Facebook ID").setValue(Profile.getCurrentProfile().getId());
                            mDatabase.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Provider").setValue("Facebook");
                            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    final String name = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Name").getValue().toString();
                                    final String mail = mAuth.getCurrentUser().getEmail().toString();

                                    profile_name = (TextView) activity.findViewById(R.id.profile_name);
                                    profile_mail = (TextView) activity.findViewById(R.id.profile_mail);
                                    profile_image = (CircleImageView) activity.findViewById(R.id.profile_image);

                                    final URL[] url = {null};
                                    final Bitmap[] bitmap = {null};
                                    Thread thread = new Thread(new Runnable() {
                                        @Override
                                        public void run() {

                                            try {
                                                url[0] = new URL(Profile.getCurrentProfile().getProfilePictureUri(300, 300).toString());
                                                bitmap[0] = BitmapFactory.decodeStream(url[0].openConnection().getInputStream());
                                                activity.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        profile_image.setImageBitmap(bitmap[0]);
                                                        signInAnimation(appBarLayout);
                                                        profile_name.setText(name);
                                                        profile_mail.setText(mail);
                                                        Bitmap blurredBitmap = BlurBuilder.blur( activity, bitmap[0] );
                                                        Drawable blurredDrawable = new BitmapDrawable( activity.getResources(), blurredBitmap );
                                                        ImageView background = (ImageView) activity.findViewById(R.id.profile_background);
                                                        background.setImageDrawable(blurredDrawable);
                                                        textView_welcome.animate().alpha(0).withLayer().withEndAction(new Runnable() {
                                                            @Override
                                                            public void run() {

                                                                textView_welcome.setVisibility(View.GONE);
                                                                signed_in_container.setVisibility(View.VISIBLE);
                                                                signed_in_container.animate().alpha(1).withLayer();
                                                            }
                                                        });
                                                        progressBar.setVisibility(View.GONE);
                                                        signInBehaviour(activity);
                                                    }
                                                });
                                            } catch (MalformedURLException e) {
                                                e.printStackTrace();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                    thread.start();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });



                        }
                    }
                });
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////// SIGN IN WITH FACEBOOK
    }

    public static void signUp(View rootView, final Activity activity){

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////// SIGN UP

        final MaterialEditText full_name,email,password,password_repeat;
        Button sign_up;

        full_name = (MaterialEditText) rootView.findViewById(R.id.edittext_3);
        email = (MaterialEditText) rootView.findViewById(R.id.edittext_4);
        password = (MaterialEditText) rootView.findViewById(R.id.edittext_5);
        password_repeat = (MaterialEditText) rootView.findViewById(R.id.edittext_6);
        sign_up = (Button) rootView.findViewById(R.id.button_3);

        password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        password_repeat.setTransformationMethod(PasswordTransformationMethod.getInstance());

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controlLogic(full_name,email,password,password_repeat,activity);
            }
        });

    }

    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }


    public static void controlLogic(final MaterialEditText full_name, final MaterialEditText email, final MaterialEditText password, final MaterialEditText password_repeat, final Activity activity){
        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) activity.findViewById(R.id.main_content);
        Boolean full_name_control,email_control,password_match,password_character;
        full_name_control=false;email_control=false;password_match=false;password_character=false;

        if(full_name.getText().toString().equals("")){
            full_name.setError("Name cannot be empty");
        }
        else{
            full_name_control=true;
        }
        //
        if(email.getText().toString().equals("") | !isValidEmail(email.getText().toString())){
            email.setError("E-Mail is not valid");
        }
        else{
            email_control=true;
        }
        //
        if(!password.getText().toString().equals(password_repeat.getText().toString())){
            password.setError("Passwords do not match");
        }
        else{
            password_match=true;
        }
        if(password.getText().toString().length()<6){
            password.setError("Min 6 characters");
        }
        else{
            password_character=true;
        }

        if(full_name_control & email_control & password_match & password_character){

            final SweetAlertDialog progress =
            new SweetAlertDialog(activity, SweetAlertDialog.PROGRESS_TYPE)
                    .setTitleText("Please wait")
                    .setContentText("Creating your account");
            progress.show();
            mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        // error logic
                        task.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("Exception = ",""+e);
                                Snackbar snackbar;
                                progress.dismissWithAnimation();
                                if(e.toString().contains("The email address is already in use by another account")){
                                    snackbar = Snackbar.make(coordinatorLayout, "Email is already in use", Snackbar.LENGTH_SHORT);
                                    snackbar.getView().setBackgroundColor(Color.parseColor("#006064"));
                                    snackbar.show();
                                }
                                else if(e.toString().contains("FirebaseNetworkException")){
                                    snackbar = Snackbar.make(coordinatorLayout, "A network error has occured", Snackbar.LENGTH_SHORT);
                                    snackbar.getView().setBackgroundColor(Color.parseColor("#006064"));
                                    snackbar.show();
                                }
                                else if(e.toString().contains("badly formatted")){
                                    snackbar = Snackbar.make(coordinatorLayout, "Email adress is badly formatted", Snackbar.LENGTH_SHORT);
                                    snackbar.getView().setBackgroundColor(Color.parseColor("#006064"));
                                    snackbar.show();
                                }
                            }
                        });
                    }
                    else {
                        // success logic
                        Log.e("successfully","created");
                        FirebaseAuth.getInstance().signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                mDatabase.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Name").setValue(full_name.getText().toString());
                                mDatabase.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Email").setValue(email.getText().toString());
                                mDatabase.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Provider").setValue("Email");
                                mAuth.getCurrentUser().sendEmailVerification();
                                mAuth.signOut();
                                progress.dismissWithAnimation();
                                new SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("One more step!")
                                        .setContentText("A verification email has been sent to "+email.getText()+". Follow the link to finish creating your account.")
                                        .setConfirmText("Okay!")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                email.setText("");
                                                password.setText("");
                                                full_name.setText("");
                                                password_repeat.setText("");
                                                TabLayout tabhost = (TabLayout) activity.findViewById(R.id.tablayout);
                                                tabhost.getTabAt(0).select();
                                                sweetAlertDialog.dismiss();

                                            }
                                        }).show();

                            }
                        });


                    }
                }
            });
        }
}
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////// SIGN UP

    public static void signInAnimation(final AppBarLayout appBarLayout){
        ValueAnimator slideAnimator = ValueAnimator
                .ofInt((height/5),height)
                .setDuration(500);

        slideAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                appBarLayout.getLayoutParams().height = (Integer) animation.getAnimatedValue();
                appBarLayout.requestLayout();
            }
        });
        final AnimatorSet set = new AnimatorSet();
        set.play(slideAnimator);
        set.setInterpolator(new AnticipateOvershootInterpolator());
        set.start();


    }

    public static void signInBehaviour(final Activity activity){
        start_container.setVisibility(View.GONE);
        signed_in_container.setVisibility(View.VISIBLE);
        signed_in_container.animate().alpha(1).withLayer();

        final ImageView exit,edit;
       CardView qr;
        CardView participating;
        exit = (ImageView) activity.findViewById(R.id.profile_exit);
        edit = (ImageView) activity.findViewById(R.id.profile_edit);
        qr = (CardView) activity.findViewById(R.id.profile_scan_qr);
        participating = (CardView) activity.findViewById(R.id.profile_participating_workplaces);

        participating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity,MapsActivity.class);
                activity.startActivity(intent);
                activity.overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notLoggedIn(height,activity);
                mAuth.signOut();
                signed_in_container.animate().alpha(0).withLayer().withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        profile_image = (CircleImageView) activity.findViewById(R.id.profile_image);
                        profile_image.setImageDrawable(activity.getResources().getDrawable(R.drawable.person));
                        if(Profile.getCurrentProfile()!=null)
                        LoginManager.getInstance().logOut();
                        signed_in_container.setVisibility(View.GONE);
                        textView_welcome.setVisibility(View.VISIBLE);
                        textView_welcome.animate().alpha(1).withLayer();
                        profile_edit.setVisibility(View.GONE);
                    }
                });
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                path = null;
                LayoutInflater inflater = LayoutInflater.from(activity);
                View alertView = inflater.inflate(R.layout.edit_profile_layout, null);

                final AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(activity, R.style.myDialog));
                alert.setView(alertView);
                final AlertDialog show = alert.show();
                editPhoto = (CircleImageView) alertView.findViewById(R.id.edit_profile_image);
                FloatingActionButton editPhotoEdit = (FloatingActionButton) alertView.findViewById(R.id.edit_profile_image_fab);
                editPhotoEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (activity.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    != PackageManager.PERMISSION_GRANTED) {
                                // Should we show an explanation?
                                if (activity.shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                    // Explain to the user why we need to read the contacts
                                }
                                activity.requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},5);
                                ImagePicker(activity);
                                return;
                            }
                            else{
                                ImagePicker(activity);
                            }
                        }else
                            ImagePicker(activity);

                    }
                });
                final MaterialEditText name,city,birth;
                name = (MaterialEditText) alertView.findViewById(R.id.edittext_8);
                city = (MaterialEditText) alertView.findViewById(R.id.edittext_9);
                birth = (MaterialEditText) alertView.findViewById(R.id.edittext_10);
                final TextWatcher tw = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (!s.toString().equals(current)) {
                            String clean = s.toString().replaceAll("[^\\d.]", "");
                            String cleanC = current.replaceAll("[^\\d.]", "");

                            int cl = clean.length();
                            int sel = cl;
                            for (int i = 2; i <= cl && i < 6; i += 2) {
                                sel++;
                            }
                            //Fix for pressing delete next to a forward slash
                            if (clean.equals(cleanC)) sel--;

                            if (clean.length() < 8){
                                clean = clean + ddmmyyyy.substring(clean.length());
                            }else{
                                //This part makes sure that when we finish entering numbers
                                //the date is correct, fixing it otherwise
                                int day  = Integer.parseInt(clean.substring(0,2));
                                int mon  = Integer.parseInt(clean.substring(2,4));
                                int year = Integer.parseInt(clean.substring(4,8));

                                if(mon > 12) mon = 12;
                                if(mon == 0) mon = 1;
                                if(day ==0) day = 1;
                                cal.set(Calendar.MONTH, mon-1);
                                year = (year<1900)?1900:(year>2000)?2000:year;
                                cal.set(Calendar.YEAR, year);
                                // ^ first set year for the line below to work correctly
                                //with leap years - otherwise, date e.g. 29/02/2012
                                //would be automatically corrected to 28/02/2012

                                day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                                clean = String.format("%02d%02d%02d",day, mon, year);
                            }

                            clean = String.format("%s/%s/%s", clean.substring(0, 2),
                                    clean.substring(2, 4),
                                    clean.substring(4, 8));

                            sel = sel < 0 ? 0 : sel;
                            current = clean;
                            birth.setText(current);
                            birth.setSelection(sel < current.length() ? sel : current.length());
                            Log.e("text = ",birth.getText().toString());
                        }
                    }
                    @Override
                    public void afterTextChanged(Editable editable) {

                    }

                    private String current = "";
                    private String ddmmyyyy = "DDMMYYYY";
                    private Calendar cal = Calendar.getInstance();};
                birth.addTextChangedListener(tw);
                final AppCompatRadioButton female,male,none;
                female = (AppCompatRadioButton) alertView.findViewById(R.id.radio_female);
                male = (AppCompatRadioButton) alertView.findViewById(R.id.radioButton_male);
                none = (AppCompatRadioButton) alertView.findViewById(R.id.radioButton_notspecified);
                female.setOnClickListener(new View.OnClickListener(){public void onClick(View view) {male.setChecked(false);none.setChecked(false);}});
                male.setOnClickListener(new View.OnClickListener(){public void onClick(View view) {female.setChecked(false);none.setChecked(false);}});
                none.setOnClickListener(new View.OnClickListener(){public void onClick(View view) {male.setChecked(false);female.setChecked(false);}});

                Button editFinish;
                editFinish = (Button) alertView.findViewById(R.id.edit_finish);
                editFinish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDatabase.getDatabase().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("Name").setValue(name.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {profile_name.setText(name.getText().toString());}});
                        mDatabase.getDatabase().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("City").setValue(city.getText().toString());
                        mDatabase.getDatabase().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("Birth").setValue(birth.getText().toString());
                        if(female.isChecked())mDatabase.getDatabase().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("Gender").setValue("Female");
                        if(male.isChecked())mDatabase.getDatabase().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("Gender").setValue("Male");
                        if(none.isChecked())mDatabase.getDatabase().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("Gender").setValue("Not Specified");
                        try{ImageUpload(Uri.parse(path),activity);show.dismiss();}catch (NullPointerException e){show.dismiss();}
                    }
                });
                mDatabase.getDatabase().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try{name.setText(dataSnapshot.child("Name").getValue().toString());}catch (NullPointerException ignored){}
                        try{city.setText(dataSnapshot.child("City").getValue().toString());}catch (NullPointerException ignored){}
                        try{birth.setText(dataSnapshot.child("Birth").getValue().toString());}catch (NullPointerException ignored){}
                        try{if(dataSnapshot.child("Gender").getValue().toString().equals("Female")){female.setChecked(true);}if(dataSnapshot.child("Gender").getValue().toString().equals("Male")){male.setChecked(true);}if(dataSnapshot.child("Gender").getValue().toString().equals("Not Specified")){none.setChecked(true);}}catch (NullPointerException ignored){none.setChecked(true);}

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                ////////////
                editPhoto.setImageDrawable(profile_image.getDrawable());
                show.show();
            }
        });


        qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity,QRReader.class);
                activity.startActivity(intent);
                activity.overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
            }
        });



    }

    public static void signInColorAnimate(final Activity activity){
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), Color.parseColor("#FFFFFF"), Color.parseColor("#00FFFFFF"));
        colorAnimation.setDuration(1000); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                tabLayout.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });
        colorAnimation.start();

        ValueAnimator colorAnimation1 = ValueAnimator.ofObject(new ArgbEvaluator(), Color.parseColor("#FFFFFF"), Color.parseColor("#20FFFFFF"));
        colorAnimation1.setDuration(1000); // milliseconds
        colorAnimation1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                AppBarLayout appBarLayout = (AppBarLayout) activity.findViewById(R.id.appbar);
                appBarLayout.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });
        colorAnimation1.start();
    }

    public static String getRealPathFromURI_API19(Context context, Uri uri){
        String filePath = "";
        String wholeID = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            wholeID = DocumentsContract.getDocumentId(uri);
        }
        String id = wholeID.split(":")[1];
        String[] column = { MediaStore.Images.Media.DATA };
        String sel = MediaStore.Images.Media._ID + "=?";
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{ id }, null);
        int columnIndex = cursor.getColumnIndex(column[0]);
        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }
    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static void ImageUpload(Uri uri, final Activity activity){
        StorageReference riversRef = mStorageRef.child(mAuth.getCurrentUser().getUid()).child("profile_image.jpg");
        riversRef.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        profile_image.setImageDrawable(editPhoto.getDrawable());
                        Bitmap bitmap = ((BitmapDrawable)editPhoto.getDrawable()).getBitmap();
                        Bitmap blurredBitmap = BlurBuilder.blur( activity, bitmap);
                        Drawable blurredDrawable = new BitmapDrawable(activity.getResources(), blurredBitmap );
                        ImageView background = (ImageView) activity.findViewById(R.id.profile_background);
                        background.setImageDrawable(blurredDrawable);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });
    }

    public static void ImagePicker(Activity activity){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);
    }
    public void MailSignedIn(){
        profile_edit.setVisibility(View.VISIBLE);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String name = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Name").getValue().toString();
                final String mail = mAuth.getCurrentUser().getEmail();

                profile_name = (TextView) findViewById(R.id.profile_name);
                profile_mail = (TextView) findViewById(R.id.profile_mail);
                profile_image = (CircleImageView) findViewById(R.id.profile_image);
                try {
                    final File localFile = File.createTempFile("images", "jpg");
                    final Bitmap[] bitmap = new Bitmap[1];
                    mStorageRef.child(mAuth.getCurrentUser().getUid()).child("profile_image.jpg").getFile(localFile).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()){
                                bitmap[0] = BitmapFactory.decodeFile(localFile.getPath());
                                profile_image.setImageBitmap(bitmap[0]);
                                Bitmap blurredBitmap = BlurBuilder.blur( FirstPage.this, bitmap[0] );
                                Drawable blurredDrawable = new BitmapDrawable(getResources(), blurredBitmap );
                                ImageView background = (ImageView) findViewById(R.id.profile_background);
                                background.setImageDrawable(blurredDrawable);
                                profile_name.setText(name);
                                profile_mail.setText(mail);
                                textView_welcome.animate().alpha(0).withLayer().withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        textView_welcome.setVisibility(View.GONE);
                                        signed_in_container.setVisibility(View.VISIBLE);
                                        signed_in_container.animate().alpha(1).withLayer();
                                        signInColorAnimate(FirstPage.this);
                                        signInBehaviour(FirstPage.this);
                                    }
                                });

                            }
                            else{
                                final Bitmap[] bitmap = {null};
                                bitmap[0] = BitmapFactory.decodeResource(getResources(),
                                        R.drawable.person);
                                profile_image.setImageBitmap(bitmap[0]);
                                profile_name.setText(name);
                                profile_mail.setText(mail);
                                Bitmap blurredBitmap = BlurBuilder.blur( FirstPage.this, bitmap[0] );
                                Drawable blurredDrawable = new BitmapDrawable(getResources(), blurredBitmap );
                                ImageView background = (ImageView) findViewById(R.id.profile_background);
                                background.setImageDrawable(blurredDrawable);
                                textView_welcome.animate().alpha(0).withLayer().withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        textView_welcome.setVisibility(View.GONE);
                                        signed_in_container.setVisibility(View.VISIBLE);
                                        signed_in_container.animate().alpha(1).withLayer();
                                        signInColorAnimate(FirstPage.this);
                                        signInBehaviour(FirstPage.this);
                                    }
                                });

                            }
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void FacebookSignedIn(){
        mDatabase.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Name").setValue(Profile.getCurrentProfile().getName());
        mDatabase.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Facebook ID").setValue(Profile.getCurrentProfile().getId());
        mDatabase.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Provider").setValue("Facebook");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String name = dataSnapshot.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Name").getValue().toString();
                final String mail = mAuth.getCurrentUser().getEmail();

                profile_name = (TextView) findViewById(R.id.profile_name);
                profile_mail = (TextView) findViewById(R.id.profile_mail);
                profile_image = (CircleImageView) findViewById(R.id.profile_image);

                final URL[] url = {null};
                final Bitmap[] bitmap = {null};
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            url[0] = new URL(Profile.getCurrentProfile().getProfilePictureUri(300, 300).toString());
                            bitmap[0] = BitmapFactory.decodeStream(url[0].openConnection().getInputStream());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    profile_image.setImageBitmap(bitmap[0]);
                                    profile_name.setText(name);
                                    profile_mail.setText(mail);
                                    Bitmap blurredBitmap = BlurBuilder.blur( FirstPage.this, bitmap[0] );
                                    Drawable blurredDrawable = new BitmapDrawable(getResources(), blurredBitmap );
                                    ImageView background = (ImageView) findViewById(R.id.profile_background);
                                    background.setImageDrawable(blurredDrawable);
                                    textView_welcome.animate().alpha(0).withLayer().withEndAction(new Runnable() {
                                        @Override
                                        public void run() {

                                            textView_welcome.setVisibility(View.GONE);
                                            signed_in_container.setVisibility(View.VISIBLE);
                                            signed_in_container.animate().alpha(1).withLayer();
                                            signInColorAnimate(FirstPage.this);
                                            signInBehaviour(FirstPage.this);
                                        }
                                    });
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void Initialize(){
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        header_font = Typeface.createFromAsset(getAssets(), "fonts/Font.ttf");
        billabong = Typeface.createFromAsset(getAssets(), "fonts/Billabong.ttf");

        signed_in_container = (RelativeLayout) findViewById(R.id.signed_in_container);

        profile_edit = (ImageView) findViewById(R.id.profile_edit);
        start_container = (RelativeLayout) findViewById(R.id.start_container);
        textView_welcome = (TextView) findViewById(R.id.textview_1);
        textView_welcome.setTypeface(billabong);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        height = size.y;
    }
    public void SignedInDecider(){
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) { // User is signed in
                    if(Profile.getCurrentProfile()!=null){
                        FacebookSignedIn();
                    }
                    else{
                        MailSignedIn();
                    }

                } else {// User is signed out;
                    signInColorAnimate(FirstPage.this);
                    notLoggedIn(height,FirstPage.this);
                }
            }
        };

        mAuth.addAuthStateListener(mAuthListener);
    }
    public void SdkInit(){
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }
    public void ProfilePhotoEdit(int requestCode,int resultCode,Intent data){
        Bitmap bitmap = null;
        if (requestCode == SELECT_PICTURE & resultCode == RESULT_OK) {
            try{
                Uri selectedImageUri = data.getData();
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
            }catch (Exception e){
                try{
                    Uri selectedImageUri = data.getData();
                    File media = new File(getRealPathFromURI_API19(getApplicationContext(),selectedImageUri));
                    Uri uri = Uri.fromFile(media);
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                }catch (Exception e1){

                    try{
                        Uri selectedImageUri = data.getData();
                        String x = "file://"+getRealPathFromURI(getApplicationContext(),selectedImageUri);
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(x));
                    }catch (Exception e2){
                        Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                    }}
            }
            if(bitmap!=null){

                if(bitmap.getHeight()>800 ){
                    scale = Bitmap.createScaledBitmap(bitmap,bitmap.getWidth()*800/bitmap.getHeight(),bitmap.getHeight()*800/bitmap.getHeight(),false);
                    if(scale.getWidth()>800)
                        scale = Bitmap.createScaledBitmap(bitmap,bitmap.getWidth()*800/bitmap.getWidth(),bitmap.getHeight()*800/bitmap.getWidth(),false);
                } else {
                    scale = bitmap;
                }

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                scale.compress(Bitmap.CompressFormat.JPEG, 99, bytes);
                path = MediaStore.Images.Media.insertImage(getContentResolver(), scale, "Title", null);
                Log.e("PATH = ","1"+path);
                editPhoto.setImageBitmap(scale);
            }


        }
    }
}

