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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.car.vendor.api.Constants;
import com.car.vendor.api.WebRequest;
import com.car.vendor.api.WebServices;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText email;
    private EditText password,name,mobile;
ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
////        });

        findViewById(R.id.show).setOnClickListener(this);
        findViewById(R.id.signup_button).setOnClickListener(this);
        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        name=(EditText)findViewById(R.id.name);
        mobile=(EditText)findViewById(R.id.mobile);
        findViewById(R.id.login).setOnClickListener(this);
//        findViewById(R.id.fgtpaswd).setOnClickListener(this);


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
            case R.id.signup_button:
                validate();
                break;

            case R.id.login:
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.show:
                password.setInputType(InputType.TYPE_CLASS_TEXT);
                break;

        }
    }

    public void validate(){
        name.clearFocus();
//        fullname.clearFocus();
        mobile.clearFocus();
        email.clearFocus();
        password.clearFocus();
//        retypepassword.clearFocus();
        if (name.getText().toString().isEmpty()) {
            name.setError("Field Required");
            name.requestFocus();
        }
//        else if (fullname.getText().toString().isEmpty()) {
//            fullname.setError("Field Required");
//            fullname.requestFocus();
//        }

        else if (mobile.getText().toString().isEmpty()) {
            mobile.setError("Field Required");
            mobile.requestFocus();
        }else if (mobile.getText().toString().startsWith("0")) {
            mobile.setError("zero not allowed");
            mobile.requestFocus();
        }else if (mobile.getText().toString().length() != 10) {
            mobile.setError("Mobile Number should be 10 digit without zero");
            mobile.requestFocus();
        }
        else if (email.getText().toString().isEmpty()) {
            email.setError("Field Required");
            email.requestFocus();
        } else if (!Validater.isValidEmail(email.getText().toString())) {
            email.setError("Invalid Email");
            email.requestFocus();
        } else if (password.getText().toString().isEmpty()) {
            password.setError("Field Required");
            password.requestFocus();
        } else  if (!Validater.isValidPassword(password.getText().toString())) {
            password.setError("Invalid Format Password");
            password.requestFocus();
        }
//        else if (!password.getText().toString().equals(retypepassword.getText().toString())) {
//            retypepassword.setError("Password Mismatching");
//            retypepassword.requestFocus();
//        }
        else {
            submit();
        }
    }

    private void submit(){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("name", name.getText().toString());

            jsonObject.accumulate("email", email.getText().toString());
            jsonObject.accumulate("password", password.getText().toString());
//            jsonObject.accumulate(Keys.fullname, fullname.getText().toString());
            jsonObject.accumulate("mobile", mobile.getText().toString());
//            jsonObject.accumulate(Keys.establishmentName, establshname.getText().toString());
//            if (CarServiceApp.isNetworkAvailable()) {
                new SignUp().execute(jsonObject.toString());
//            } else {
//
//            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private class SignUp extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            getActivity().getSupportFragmentManager().beginTransaction()
//                    .add(R.id.container_loading, new ProgressBarFrament(), Constants.PROGRESS_FRAGMENT)
//                    .commit();
            dialog =new  ProgressDialog(SignUpActivity.this);
            dialog.setMessage("Loading....");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONObject jsonObject = null;
            try {
                jsonObject = WebRequest.postData(params[0], WebServices.vendor_signUp);
            } catch (Exception e) {
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
            onDone(jsonObject);

        }
    }

    private void onDone(JSONObject jsonObject) {
        try {
            if (jsonObject.getString("status").equals(Constants.SUCCESS)) {
//                CarServiceApp.showAToast(jsonObject.getString(Keys.notice));
                Toast.makeText(getApplication(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
//                CarServiceApp.saveToPreferences(getActivity(), Keys.id, jsonObject.getString(Keys.id));
//                DialogFragment dialogFrag = SuccessDialogFragment.newInstance();
//                dialogFrag.show(getFragmentManager().beginTransaction(), Constants.SUCCESSDIALOG_FRAGMENT);
            } else if (jsonObject.getString("status").equals(Constants.FAILED)) {
                Toast.makeText(getApplication(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplication(),"something went wrong",Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}



