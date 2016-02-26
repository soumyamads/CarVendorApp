package com.car.vendor;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.service.textservice.SpellCheckerService;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.car.vendor.api.Constants;
import com.car.vendor.api.WebRequest;
import com.car.vendor.api.WebServices;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
//import com.facebook.login.LoginClient;
//import com.facebook.login.LoginResult;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.PlusShare;
import com.google.android.gms.plus.model.people.Person;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.app.PendingIntent.getActivity;


public class MainActivity extends AppCompatActivity implements  GoogleApiClient.OnConnectionFailedListener,View.OnClickListener, GoogleApiClient.ConnectionCallbacks {

    private EditText email;
    private EditText password;
    ProgressDialog dialog;
    private String fbId = null,emailid,uid,access_token;
    private SignInButton signinButton;
    private boolean mIntentInProgress;

    private boolean mSignInClicked;

    private ConnectionResult mConnectionResult;

    private static final int PICK_MEDIA_REQUEST_CODE = 8;
    private static final int SHARE_MEDIA_REQUEST_CODE = 9;
    private static final int SIGN_IN_REQUEST_CODE = 10;
    private static final int ERROR_DIALOG_REQUEST_CODE = 11;
    private CallbackManager callbackManager;
    private LoginButton fbLoginButton;
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private GoogleApiClient mGoogleApiClient;
    private TextView mStatusTextView;
    private ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

initializeGooglePlus();

        findViewById(R.id.show).setOnClickListener(this);
        findViewById(R.id.login_button).setOnClickListener(this);
        findViewById(R.id.signup).setOnClickListener(this);

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        findViewById(R.id.fgtpaswd).setOnClickListener(this);


       fbLoginButton = (LoginButton) findViewById(R.id.fb_login_button);


        signinButton = (SignInButton)findViewById(R.id.sign_in_button);
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processSignIn();
//                processSignOut();
            }
        });


        fbLoginButton.setReadPermissions(Arrays.asList("public_profile", "email"));

        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {


                AccessToken accessToken = loginResult.getAccessToken();
                GraphRequest request = GraphRequest.newMeRequest(accessToken,
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {

                                Log.e("Facebook", object.toString());

                                try {
                                    fbId = object.getString("id");

                                    URL profile_pic = new URL("https://graph.facebook.com/" + fbId + "/picture?width=200&height=150");
                                    Log.i("profile_pic", profile_pic + "");


                                    if (object.has("email")) {
                                        emailid = object.getString("email");
//                                        DealWithItApp.saveToPreferences(getActivity(), Keys.email, emailid);
                                    }


                                    doCheck();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,first_name,last_name,birthday,gender,email,picture.type(large)");
                request.setParameters(parameters);
                request.executeAsync();

            }


            @Override
            public void onCancel() {
                Log.d("print", "cancel");
            }

            @Override
            public void onError(FacebookException error) {
                error.printStackTrace();
            }
        });

    }

    private void initializeGooglePlus() {
        mGoogleApiClient = buildGoogleAPIClient();
    }
    /**
     * API to return GoogleApiClient Make sure to create new after revoking
     * access or for first time sign in
     *
     * @return
     */
    private GoogleApiClient buildGoogleAPIClient() {
        return new GoogleApiClient.Builder(getApplication()).addConnectionCallbacks(MainActivity.this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();
    }

    @Override
    public void onStart() {
        super.onStart();
        // make sure to initiate connection
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        // disconnect api if it is connected
        if (mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
    }
    private void processRevokeRequest() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
                    .setResultCallback(new ResultCallback() {
                        @Override
                        public void onResult(Result result) {
                            Toast.makeText(getApplicationContext(),
                                    "User permissions revoked",
                                    Toast.LENGTH_LONG).show();
                            mGoogleApiClient = buildGoogleAPIClient();
                            mGoogleApiClient.connect();

                        }

                    });

        }

    }

    /**
     * API to process media post request start activity with MIME type as video
     * and image
     */
    private void processShareMedia() {
        Intent photoPicker = new Intent(Intent.ACTION_PICK);
        photoPicker.setType("video/*, image/*");
        startActivityForResult(photoPicker, PICK_MEDIA_REQUEST_CODE);

    }

    /**
     * API to process post share request Use PlusShare.Builder to create share
     * post.
     */
    private void processSharePost() {
        // Launch the Google+ share dialog with attribution to your app.
        Intent shareIntent = new PlusShare.Builder(getApplication()).setType("text/plain")
                .setText("Google+ Demo http://androidsrc.net")
                .setContentUrl(Uri.parse("http://androidsrc.net")).getIntent();

        startActivityForResult(shareIntent, 0);

    }

    /**
     * API to handle sign out of user
     */
    private void processSignOut() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
        }

    }

    /**
     * API to handler sign in of user If error occurs while connecting process
     * it in processSignInError() api
     */
    private void processSignIn() {

        if (!mGoogleApiClient.isConnecting()) {
            Intent shareIntent = new PlusShare.Builder(this)
                    .setType("text/plain")
                    .setText("Welcome to the Google+ platform.")
                    .setContentUrl(Uri.parse("https://developers.google.com/+/"))
                    .getIntent();

            startActivityForResult(shareIntent, 0);
            processSignInError();
            mSignInClicked = true;
        }

    }

    /**
     * API to process sign in error Handle error based on ConnectionResult
     */
    private void processSignInError() {
        if (mConnectionResult != null && mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this,
                        SIGN_IN_REQUEST_CODE);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    /**
     * Callback for GoogleApiClient connection failure
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
                    ERROR_DIALOG_REQUEST_CODE).show();
            return;
        }
        if (!mIntentInProgress) {
            mConnectionResult = result;

            if (mSignInClicked) {
                processSignInError();
            }
        }

    }

    /**
     * Callback for GoogleApiClient connection success
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        mSignInClicked = false;
        Toast.makeText(getApplicationContext(), "Signed In Successfully",
                Toast.LENGTH_LONG).show();

        processUserInfoAndUpdateUI();



    }

    /**
     * Callback for suspension of current connection
     */
    @Override
    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();

    }

    /**
     * API to update signed in user information
     */
    private void processUserInfoAndUpdateUI() {
        Person signedInUser = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
        if (signedInUser != null) {
            if (signedInUser.hasId()) {
                String userId = signedInUser.getId();

                Log.d("UserId",userId);
            }

            if (signedInUser.hasDisplayName()) {
                String userName = signedInUser.getDisplayName();

                Log.d("UserName", userName);
            }

            if (signedInUser.hasTagline()) {
                String tagLine = signedInUser.getTagline();
                Log.d("tagLine",tagLine);
            }

            if (signedInUser.hasAboutMe()) {
                String aboutMe = signedInUser.getAboutMe();
                Log.d("aboutMe",aboutMe);
            }

            if (signedInUser.hasBirthday()) {
                String birthday = signedInUser.getBirthday();
                Log.d("birthday",birthday);
            }

            if (signedInUser.hasCurrentLocation()) {
                String userLocation = signedInUser.getCurrentLocation();
                Log.d("userLocation",userLocation);
            }

            String userEmail = Plus.AccountApi.getAccountName(mGoogleApiClient);
            Log.d("userEmail",userEmail);

            if (signedInUser.hasImage()) {
                String userProfilePicUrl = signedInUser.getImage().getUrl();
                // default size is 50x50 in pixels.changes it to desired size
                int profilePicRequestSize = 250;

                userProfilePicUrl = userProfilePicUrl.substring(0,
                        userProfilePicUrl.length() - 2) + profilePicRequestSize;
                Log.d("userProfilePicUrl",userProfilePicUrl);
            }

        }
    }

    private void doCheck() {

        Profile profile = Profile.getCurrentProfile();
        AccessToken  token = AccessToken.getCurrentAccessToken();


        if (profile != null) {
            uid = profile.getId();

        }

        if (token != null) {
            uid = token.getUserId();
            access_token = token.getToken();
        }


        Log.d("FacebookId", " " + fbId + " email" +emailid+ "uid " + uid + "access_token " + access_token);



    }


    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent i) {
        super.onActivityResult(reqCode, resCode, i);
        callbackManager.onActivityResult(reqCode, resCode, i);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.show:
                password.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
            case R.id.login_button:
                validate();
                break;
            case R.id.fgtpaswd:
                fgtvalidate();
                break;
            case R.id.signup:
                Intent intent=new Intent(getApplicationContext(),SignUpActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
    private void fgtvalidate(){
        email.clearFocus();
        password.clearFocus();
        if(email.getText().toString().isEmpty()){
            email.setError("Field Required");
            email.requestFocus();
        }else if(!Validater.isValidEmail(email.getText().toString())){
            email.setError("Invalid Email");
            email.requestFocus();
        }else{
            forgotsubmit();
        }
    };

    private void forgotsubmit(){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("email", email.getText().toString());
//            if(CarServiceApp.isNetworkAvailable()){
                new Forgot().execute(jsonObject.toString());
            }
//        else{
//
//            }
//        }
    catch (Exception e){
            e.printStackTrace();
        }
    }



    private class Forgot extends AsyncTask<String,Void,JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            getActivity().getSupportFragmentManager().beginTransaction()
//                    .add(R.id.container_loading,new ProgressBarFrament(), Constants.PROGRESS_FRAGMENT)
//                    .commit();
            dialog =new  ProgressDialog(MainActivity.this);
            dialog.setMessage("Loading....");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONObject jsonObject = null;
            try {
                jsonObject = WebRequest.postData(params[0], WebServices.forgotPass);
            }catch (Exception e){
                e.printStackTrace();
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
//            getActivity().getSupportFragmentManager().beginTransaction()
//                    .remove(getActivity().getSupportFragmentManager().findFragmentByTag(Constants.PROGRESS_FRAGMENT))
//                    .commit();
            dialog.dismiss();
            onForgotDone(jsonObject);
        }
    }

    private void onForgotDone(JSONObject jsonObject){
        try {
            if(jsonObject.getString("status").equals(Constants.SUCCESS)){
                email.setText(Constants.DEFAULT_STRING);
                Toast.makeText(getApplication(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
            }else if(jsonObject.getString("status").equals(Constants.FAILED)){
                Toast.makeText(getApplication(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplication(),"something went wrong",Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    private void validate(){
        email.clearFocus();
        password.clearFocus();
        if(email.getText().toString().isEmpty()){
            email.setError("Field Required");
            email.requestFocus();
        }else if(!Validater.isValidEmail(email.getText().toString())){
            email.setError("Invalid Email");
            email.requestFocus();
        }else if(password.getText().toString().isEmpty()){
            password.setError("Field Required");
            password.requestFocus();
        }/* else if (!Validater.isValidPassword(password.getText().toString())){
                password.setError("Invalid Format Password");
                password.requestFocus();
            }*/else{
            submit();
        }

    }

    private void submit(){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("email", email.getText().toString());
            jsonObject.accumulate("password", password.getText().toString());
            Log.d("json", jsonObject.toString());
//            if(CarServiceApp.isNetworkAvailable()){
                new Login().execute(jsonObject.toString());
//            }else{
//
//            }
        }
    catch (JSONException e){
            e.printStackTrace();
        }
    }

    private class Login extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            getActivity().getSupportFragmentManager().beginTransaction()
//                    .add(R.id.container_loading,new ProgressBarFrament(),Constants.PROGRESS_FRAGMENT)
//                    .commit();
            dialog =new  ProgressDialog(MainActivity.this);
            dialog.setMessage("Loading....");
            dialog.setCancelable(false);
                      dialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONObject jsonObject = null;
            try {
                jsonObject = WebRequest.postData(params[0], WebServices.vendor_signIn);
            }catch (Exception e){
                e.printStackTrace();
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
//            getActivity().getSupportFragmentManager().beginTransaction()
//                    .remove(getActivity().getSupportFragmentManager().findFragmentByTag(Constants.PROGRESS_FRAGMENT))
//                    .commit();
           ;
            dialog.dismiss();
            onDone(jsonObject);
//            dialog.dismiss();

        }
    }

    private void onDone(JSONObject jsonObject){
        try {
            if(jsonObject.getString("status").equals(Constants.SUCCESS)){
                email.setText(Constants.DEFAULT_STRING);
                Toast.makeText(getApplication(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
            }else if(jsonObject.getString("status").equals(Constants.FAILED)){
                Toast.makeText(getApplication(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplication(),"something went wrong",Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            e.printStackTrace();

    }


}

}
