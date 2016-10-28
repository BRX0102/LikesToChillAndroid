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
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException error) {

        }
    };

    public void setFacebookData(final LoginResult loginResult){
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback(){
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response){
                        //Application code
                        try{
                            Log.i("Response",response.toString());

                            Profile profile = Profile.getCurrentProfile();

                            MainActivity.thisUser = new User(
                                    Integer.parseInt(profile.getId()),
                                    response.getJSONObject().getString("first_name"),
                                    response.getJSONObject().getString("last_name"),
                                    "00000",
                                    response.getJSONObject().getString("email"),
                                    response.getJSONObject().getString("gender"),
                                    response.getJSONObject().getString("birthday"),
                                    "About");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
        );
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());

        mCallbackManager = CallbackManager.Factory.create();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    private void displayWelcomeMessage(Profile profile){
        if(profile != null){

            Uri uri = profile.getProfilePictureUri(300,300);

            mTextDetails.setText("Welcome " + profile.getName());
            String sample = "https://graph.facebook.com/" + profile.getId() + "/picture?type=large&redirect=true&width=500&height=500";
            ImageView image = (ImageView)getView().findViewById(R.id.profile_pic);
            DownloadImageWithURLTask downloadTask = new DownloadImageWithURLTask(image);
            downloadTask.execute(sample);

            GraphRequest request = GraphRequest.newMeRequest(
                    AccessToken.getCurrentAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {
                            // Application code
                            Log.d("response", String.valueOf(object));
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,link,email,gender, location");
            request.setParameters(parameters);
            request.executeAsync();

            Log.d("TAG", bundle2string(parameters));
            /*JSONObject mainObject = new JSONObject(Your_Sring_data);
            JSONObject uniObject = mainObject.getJSONObject("university");
            String  uniName = uniObject.getJsonString("name");
            String uniURL = uniObject.getJsonString("url");

            JSONObject oneObject = mainObject.getJSONObject("1");
            String id = oneObject.getJsonString("id");*/

        }
    }

    public static String bundle2string(Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        String string = "Bundle{";
        for (String key : bundle.keySet()) {
            string += " " + key + " => " + bundle.get(key) + ";";
        }
        string += " }Bundle";
        return string;
    }

    private class DownloadImageWithURLTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        public DownloadImageWithURLTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String pathToFile = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream in = new java.net.URL(pathToFile).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bitmap;
        }
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
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
        loginButton.setReadPermissions("public profile","user_friends","email");
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
