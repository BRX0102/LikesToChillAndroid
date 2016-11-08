package com.csumb.pmoung.kiqback;


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
import com.facebook.AccessToken;

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


import static com.csumb.pmoung.kiqback.R.id.imageView;
import static com.facebook.FacebookSdk.getApplicationContext;
import java.net.URL;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private TextView mTextDetails;
    Context context;
    LoginButton loginButton;
    AccessToken accessToken;
    private CallbackManager mCallbackManager;
    private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            accessToken = loginResult.getAccessToken();
             Profile profile = Profile.getCurrentProfile();
            displayWelcomeMessage(profile);

            String test = "testing123";
            Log.d("TAG", test);

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
            //Intent toUpdate = new Intent(LoginFragment.this.getContext(), UpdateProfile.class);
            //startActivity(toUpdate);
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException error) {

        }
    };

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());

        mCallbackManager = CallbackManager.Factory.create();

    }



    private void displayWelcomeMessage(Profile profile){
        if(profile != null){


            /*JSONObject mainObject = new JSONObject(Your_Sring_data);
            JSONObject uniObject = mainObject.getJSONObject("university");
            String  uniName = uniObject.getJsonString("name");
            String uniURL = uniObject.getJsonString("url");

            JSONObject oneObject = mainObject.getJSONObject("1");
            String id = oneObject.getJsonString("id");*/

        }
    }


    private void setupTextDetails(View view){
        mTextDetails = (TextView) view.findViewById(R.id.text_details);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupTextDetails(view);
        loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile","user_friends","email","user_hometown","user_birthday","user_about_me","user_location");
        loginButton.setFragment(this);
        loginButton.registerCallback(mCallbackManager, mCallback);


    }

    @Override
    public void onResume(){
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        displayWelcomeMessage(profile);

//        Intent to main if resuming
//        Intent i = new Intent(LoginFragment.this.getContext(), MainActivity.class);
//        startActivity(i);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
