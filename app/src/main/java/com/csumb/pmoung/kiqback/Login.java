package com.csumb.pmoung.kiqback;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import com.facebook.HttpMethod;

import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;


import static com.csumb.pmoung.kiqback.R.id.button2;
import static com.csumb.pmoung.kiqback.R.id.imageView;
import static com.csumb.pmoung.kiqback.R.id.login_button;
import static com.facebook.FacebookSdk.getApplicationContext;
import static java.lang.Thread.sleep;

import java.net.URL;
import java.util.Arrays;


public class Login extends AppCompatActivity {



        String name;
        TextView mTextDetails;
        Context context;
        LoginButton loginButton;
        AccessToken accessToken;
        private CallbackManager mCallbackManager;
        Button button3;

        /*@Override
        public boolean onTouchEvent(MotionEvent event) {
            // MotionEvent object holds X-Y values
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                String text = "You click at x = " + event.getX() + " and y = " + event.getY();
                Toast.makeText(this, text, Toast.LENGTH_LONG).show();
            }

            return super.onTouchEvent(event);
        }*/




        @Override
        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            FacebookSdk.sdkInitialize(getApplicationContext());
            setContentView(R.layout.login);

            mCallbackManager = CallbackManager.Factory.create();
            loginButton = (LoginButton) findViewById(R.id.login_button);

            loginButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    loginButton.setReadPermissions("public_profile","user_friends","email","user_hometown","user_birthday","user_about_me","user_location");
                    loginButton.registerCallback(mCallbackManager, mCallback);
                }
            });



        }


        private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                accessToken = loginResult.getAccessToken();
                Profile profile = Profile.getCurrentProfile();

                //Log.d("hiya", profile.getName());

                displayWelcomeMessage(profile);

                GraphRequest request = GraphRequest.newMeRequest(
                        AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                // Application code
                                Log.d("response", String.valueOf(object));

                                try {

                                    JSONObject getLocation = object.getJSONObject("location");
                                    Object locationName = getLocation.get("name");
                                    //Log.d("check1n", level.toString());

                                /*///testing JSON call
                                String fName = object.getString("name");
                                Log.d("sweg", fName);*/
                                    MainActivity.thisUser = new User(
                                            object.getInt("id"),
                                            object.getString("first_name"),
                                            object.getString("last_name"),
                                            locationName.toString(),
                                            object.getString("email"),
                                            object.getString("gender"),
                                            object.getString("birthday"),
                                            object.getString("about"));



                                /*String fName = object.getString("first_name");
                                Log.d("sweg", fName);*/

                                } catch (JSONException e) {

                                    // Do something to recover ... or kill the app.
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email, gender, hometown, birthday, about, location");
                request.setParameters(parameters);
                request.executeAsync();

                //Log.d("TAG", bundle2string(parameters));

                ////////COMMENTED CHANGE INTENT TO TEST LOGIN///////////////////////
                Intent toUpdate = new Intent(Login.this, TempLogout.class);
                startActivity(toUpdate);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        };

        private void displayWelcomeMessage(Profile profile){
            if(profile != null){
                mTextDetails = (TextView)findViewById(R.id.text_details);
                mTextDetails.setText("Welcome " + profile.getName());
            }
        }

        @Override
        public void onResume(){
            super.onResume();
            Profile profile = Profile.getCurrentProfile();
            displayWelcomeMessage(profile);


            if(profile != null){
                Intent toUpdate = new Intent(Login.this, TempLogout.class);
                startActivity(toUpdate);
            }


            /*Intent toUpdate = new Intent(Login.this, TempLogout.class);
            startActivity(toUpdate);*/
//        Intent to main if resuming

        }

        public void onActivityResult(int requestCode, int resultCode, Intent data){
            super.onActivityResult(requestCode, resultCode, data);
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }


}
