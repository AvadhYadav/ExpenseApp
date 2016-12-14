package com.example.vasu.expense_manager;

/**
 * Created by Freeware Sys on 11/27/2016.
 */
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

/**
 * Created by Freeware Sys on 11/18/2016.
 */
public class LoginActivity extends AppCompatActivity implements
        View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener{

    public SignInButton btnSignIn;
    public AppCompatButton btn_signup;
    public TextView user_name, user_email,sign_with_diff,sign_in_text;
    public ImageView prof_image;
    public Integer pic_flag = 1;

    public GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;
    private static final int RC_SIGN_IN = 007;

    String prof_name = " ";
    String prof_email = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(getResources().getColor(R.color.status_bar_color));

        btn_signup = (AppCompatButton) findViewById(R.id.btn_signup);
        btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);
        user_name = (TextView) findViewById(R.id.user_name);
        user_email = (TextView) findViewById(R.id.user_email);
        sign_with_diff = (TextView) findViewById(R.id.sign_with_diff);
        sign_in_text = (TextView) findViewById(R.id.sign_in_text);
        prof_image = (ImageView) findViewById(R.id.imgProfilePic);

//        sign_with_diff.setOnClickListener(this);
//        btnSignIn.setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
//        mGoogleApiClient.connect();

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean connected = false;
                ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    //we are connected to a network
                    connected = true;
                    signIn();
                }
                else {
                    connected = false;
                    Toast.makeText(getApplicationContext(), "You're not connected to the internet", Toast.LENGTH_SHORT).show();
                }

            }
        });

        sign_with_diff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startnewActivity();
            }
        });




        btnSignIn.setScopes(gso.getScopeArray());
        btnSignIn.setSize(SignInButton.SIZE_STANDARD);



    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("Google SignUp", "onConnectionFailed:" + connectionResult);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result == null){
                Log.v("Account Details", "Randaap");
            }
            else{
                Log.v("Account Detail", "Badiya");
                Log.v("Account Details", result.toString());
//                Log.v("Info", result.getSignInAccount().getEmail());
            }
            if(result.getSignInAccount().getDisplayName() != null)
                prof_name = result.getSignInAccount().getDisplayName();
            if(result.getSignInAccount().getEmail() != null)
                prof_email = result.getSignInAccount().getEmail();
            Log.v("person name", prof_name);
            Log.v("person email", prof_email);
            changeUI(result);
        }
    }


    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            Log.v("opr.isDone()", "true");
            GoogleSignInResult result = opr.get();
            changeUI(result);
        } else {
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    changeUI(googleSignInResult);
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
//        int id = view.getId();
//
//        switch (id) {
//            case R.id.sign_with_diff:
//                Log.v("Entered", "true");
//                signOut();
//                break;
//            case R.id.btn_sign_in:
//                Log.v("Entered", "true");
//                signIn();
//                break;
//        }
    }

    public void startnewActivity(){
        Log.v("Prof name Main Activity", prof_name);
        Log.v("Prof email ", prof_email);
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.putExtra("prof_name", prof_name);
        i.putExtra("prof_email" , prof_email);
        startActivity(i);
    }

    public void changeUI(GoogleSignInResult result){
        if(result.isSuccess()) {
            prof_name = result.getSignInAccount().getDisplayName();
            prof_email = result.getSignInAccount().getEmail();
            if(result.getSignInAccount().getPhotoUrl() != null) {
                Log.v("Profile image", "Found");
                String personPhotoUrl = result.getSignInAccount().getPhotoUrl().toString();
                pic_flag = 1;
                Glide.with(getApplicationContext()).load(personPhotoUrl)
                        .thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(prof_image);
            }
            else{
                final Resources res = getResources();
                final int tileSize = res.getDimensionPixelSize(R.dimen.letter_tile_size);

                final LetterTileProvider tileProvider = new LetterTileProvider(this, 1);
                final Bitmap letterTile = tileProvider.getLetterTile(prof_name, "key", tileSize, tileSize);
                prof_image.setImageBitmap(letterTile);
                Log.v("Profile image", "Not Found");
                pic_flag = 0;
            }
            Log.v("person name", prof_name);
            Log.v("person email", prof_email);
            user_name.setText(result.getSignInAccount().getDisplayName());
            user_email.setText(result.getSignInAccount().getEmail());
            user_name.setVisibility(View.VISIBLE);
            user_email.setVisibility(View.VISIBLE);
            sign_with_diff.setVisibility(View.VISIBLE);
            btnSignIn.setVisibility(View.GONE);
            sign_in_text.setVisibility(View.GONE);
            btn_signup.setVisibility(View.VISIBLE);
            prof_image.setVisibility(View.VISIBLE);
//            if(pic_flag == 1){
//                prof_image.setVisibility(View.VISIBLE);
//            }
//            else{
//                prof_image.setVisibility(View.GONE);
//            }

        }
        else{
            user_name.setVisibility(View.GONE);
            user_email.setVisibility(View.GONE);
            sign_with_diff.setVisibility(View.GONE);
            btnSignIn.setVisibility(View.VISIBLE);
            sign_in_text.setVisibility(View.VISIBLE);
            btn_signup.setVisibility(View.GONE);
            prof_image.setVisibility(View.GONE);
        }

    }

    public void signOut(){
//        mGoogleApiClient.reconnect();
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .build();
//
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(this, this)
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build();
//        mGoogleApiClient.connect();

        if(mGoogleApiClient.isConnected()){
            Log.v("Google auth is ", "Connected");
        }
        else{
            Log.v("Google auth is ", "Disconnected");
        }
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        signOutUpdate();
                    }
                });
    }

    public void signOutUpdate(){
        user_name.setVisibility(View.GONE);
        user_email.setVisibility(View.GONE);
        sign_with_diff.setVisibility(View.GONE);
        btnSignIn.setVisibility(View.VISIBLE);
        sign_in_text.setVisibility(View.VISIBLE);
        btn_signup.setVisibility(View.GONE);
        prof_image.setVisibility(View.GONE);
    }

//    public void signInUpdate(GoogleSignInResult result){
//        user_name.setVisibility(View.VISIBLE);
//        user_email.setVisibility(View.VISIBLE);
//        sign_with_diff.setVisibility(View.VISIBLE);
//        btnSignIn.setVisibility(View.GONE);
//        btn_signup.setVisibility(View.VISIBLE);
//    }
}

