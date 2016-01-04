package com.car.vendor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.car.vendor.api.Constants;
import com.car.vendor.api.WebRequest;
import com.car.vendor.api.WebServices;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText email;
    private EditText password;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        findViewById(R.id.show).setOnClickListener(this);
        findViewById(R.id.login_button).setOnClickListener(this);
        findViewById(R.id.signup).setOnClickListener(this);

        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        findViewById(R.id.fgtpaswd).setOnClickListener(this);


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
