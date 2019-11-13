package com.ics.nrs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.nrs.R;
import com.ics.nrs.SharedPreference.SessionManager;
import com.ics.nrs.Utility.Connectivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

public class Login extends AppCompatActivity {
    TextView forgetPassword;
    Button login;
    // TextView username,password;
    EditText username, password;
    String Token = "4568", Username = "", Password = "";
    String id, mobilenumber = "", address = "", email;
    String image;
    LinearLayout emp_layout, cust_layout;

    Button employee_login, customer_login;
    private SessionManager manager;
    boolean mUserVisibleHint = true;

    EditText et_mobileno, et_username;
    String CustMobileno, CustUsername;
    Button user_login;

    // String URL="https://infocentroid.us/NRS_LIVE/Api/admin_login?username=jeny&password=123456&admin_device_token=4568";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        manager = new SessionManager(this);
        if (manager.isLoggedIn()) {
            Toast.makeText(this, "You are already logged in", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        username = (EditText) findViewById(R.id.et_emp_username);
        password = (EditText) findViewById(R.id.et_password);
        employee_login = (Button) findViewById(R.id.employee_login);
        customer_login = (Button) findViewById(R.id.customer_login);
        emp_layout = (LinearLayout) findViewById(R.id.emp_layout);
        cust_layout = (LinearLayout) findViewById(R.id.cust_layout);
        forgetPassword = (TextView)findViewById(R.id.forgetPassword);

        login = (Button) findViewById(R.id.login);
        /*if (getIntent().getStringExtra("WithOut").equals("0")) {
            emp_layout.setVisibility(View.GONE);
            cust_layout.setVisibility(View.VISIBLE);
        } else if (getIntent().getStringExtra("Main").equals("1")) {
            emp_layout.setVisibility(View.VISIBLE);
            cust_layout.setVisibility(View.GONE);
        }*/
        et_username = (EditText) findViewById(R.id.et_username);
        et_mobileno = (EditText) findViewById(R.id.et_mobileno);
        user_login = (Button) findViewById(R.id.user_login);

        employee_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // customer_login.setBackgroundDrawable(getResources().getDrawable(R.drawable.selected));
                emp_layout.setVisibility(View.VISIBLE);
                cust_layout.setVisibility(View.GONE);

            }
        });


        customer_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // employee_login.setBackgroundDrawable(getResources().getDrawable(R.drawable.selected));
                emp_layout.setVisibility(View.GONE);
                cust_layout.setVisibility(View.VISIBLE);

            }
        });


        user_login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                CustUsername = et_username.getText().toString();
                CustMobileno = et_mobileno.getText().toString();

                if (CustUsername.isEmpty()) {
                    et_username.setError("Username Field Is Empty");
                    et_username.requestFocus();
                    return;
                }

                if (CustMobileno.isEmpty()) {
                    et_mobileno.setError("Mobile Number Field Is Empty");
                    et_mobileno.requestFocus();
                    return;
                } else if (CustMobileno.length() != 10) {
                    et_mobileno.setError("Mobile Number Should Be 10 Digits");
                    et_mobileno.requestFocus();
                    return;
                }

                if (Connectivity.isNetworkAvailable(Login.this)) {
                    new SendUserLogin().execute();
                } else {
                    Toast.makeText(Login.this, "no Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Username = username.getText().toString().trim();
                Password = password.getText().toString().trim();

                    if (Username.isEmpty()) {
                        username.setError("Username required");
                        username.requestFocus();
                        return;
                    }
                if (Password.isEmpty()) {
                    password.setError("Password required");
                }

                if (Connectivity.isNetworkAvailable(Login.this)) {

                    new GetLoginAsynctask().execute();

                } else {
                    Toast.makeText(Login.this, "no Internet", Toast.LENGTH_SHORT).show();
                }

                // login();

               /* Intent intent=new Intent(Login.this,ComplaintList.class);
                startActivity(intent);*/
            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,VerificationActivity.class);
                startActivity(intent);
            }
        });

    }


    //***********CustomerLogin***************


    class SendUserLogin extends AsyncTask<String, String, String> {

        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(Login.this);
            dialog.show();

        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("https://infocentroid.us/NRS_LIVE/Api/customer_login");


                JSONObject postDataParams = new JSONObject();
                postDataParams.put("name", CustUsername);
                postDataParams.put("mobile", CustMobileno);


                Log.e("postDataParams", postDataParams.toString());

                URLConnection l_connection = null;

                SSLContext sslcontext = SSLContext.getInstance("TLSv1");
                sslcontext.init(null, null, null);
                SSLSocketFactory NoSSLv3Factory = new NoSSLv3SocketFactory(sslcontext.getSocketFactory());

                HttpsURLConnection.setDefaultSSLSocketFactory(NoSSLv3Factory);
                l_connection = (HttpsURLConnection) url.openConnection();
                l_connection.connect();

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds*/);
                conn.setConnectTimeout(15000  /*milliseconds*/);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader r = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        result.append(line);
                    }
                    r.close();
                    return result.toString();

                } else {
                    return new String("false : " + responseCode);
                }
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                dialog.dismiss();

                // JSONObject jsonObject = null;
                Log.e("SendLogin>>>", result.toString());
                try {

                    JSONObject jsonObject = new JSONObject(result);

                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");

                    JSONObject data = jsonObject.getJSONObject("data");

                    String id = data.getString("id");
                    String name = data.getString("name");
                    String mobile = data.getString("mobile");
                    String otp = data.getString("otp");
                    String created = data.getString("created");


                    Log.e(">>>>", jsonObject.toString() + " " + status + " " + message);

                    if (status.equalsIgnoreCase("true")) {

                        /*AppPreference.setCustName(Login.this,name);
                        AppPreference.setCustMobileno(Login.this,mobile);
                        AppPreference.setCustOtp(Login.this,otp);*/

                        manager.serverLoginForNRS(CustUsername, CustMobileno);

                        Toast.makeText(Login.this, message, Toast.LENGTH_LONG).show();

                        et_username.setText("");
                        et_mobileno.setText("");


                        Intent intent = new Intent(Login.this, OtpActivity.class);
                        intent.putExtra("OTP", otp);
                        manager.setOtp(otp);
                        startActivity(intent);

                        finish();
                    } else {
                        Toast.makeText(Login.this, "Unsuccessful Login", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

        public String getPostDataString(JSONObject params) throws Exception {

            StringBuilder result = new StringBuilder();
            boolean first = true;

            Iterator<String> itr = params.keys();

            while (itr.hasNext()) {

                String key = itr.next();
                Object value = params.get(key);

                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(key, "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(value.toString(), "UTF-8"));

            }
            return result.toString();
        }


    }


    //****************EmployeeLogin**************


    class GetLoginAsynctask extends AsyncTask<String, String, String> {
        String output = "";
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(Login.this);
            dialog.setMessage("Processing");
            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();
            /*arComentChat = new ArrayList<>();*/
        }

        @Override
        protected String doInBackground(String... params) {

     /*   String urlParameters = "";
        try {
            urlParameters = "f_id=" + URLEncoder.encode(s_id, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/
            String sever_url = "https://infocentroid.us/NRS_LIVE/Api/admin_login?username=" + Username + "&password=" + Password + "&admin_device_token=" + Token;
            Log.e("sever_url>>>>>>>>>", sever_url);
            output = HttpHandler.makeServiceCall(sever_url);
            System.out.println("getcomment_url" + output);
            return output;
        }

        @Override
        protected void onPostExecute(String output) {
            if (output == null) {
                dialog.dismiss();
            } else {
                try {
                    dialog.dismiss();
                    JSONObject json = new JSONObject(output);
                    String response = json.getString("response");
                    String message = json.getString("message");
                    if (response.equalsIgnoreCase("1")) {
                        JSONObject data = json.getJSONObject("data");
                        String user = data.getString("user");
                        String pwd = data.getString("password");
                        id = data.getString("id");
                        String name = data.getString("name");
                        mobilenumber = data.getString("mobilenumber");
                        address = data.getString("address");
                        email = data.getString("email");
                        image = data.getString("image");
                        String admin_device_token = data.getString("admin_device_token");

                        Log.e("user", user);
                        Log.e("password", pwd);
                        Log.e("json>>>>>>>>>", json.toString());


                        dialog.dismiss();
                        // AppPreference.setLogout(Login.this , Token);

                        Toast.makeText(Login.this, message, Toast.LENGTH_LONG).show();


                        AppPreference sessionManagement = new AppPreference(Login.this);
                        sessionManagement.createLoginSession(id, email, name, mobilenumber, Password);
                        Intent intent = new Intent(Login.this, ComplaintList.class);
                        startActivity(intent);

                        AppPreference.setComplaintId(Login.this, id);
                        AppPreference.setProfile(Login.this, image);
                        AppPreference.setMobileNo(Login.this, mobilenumber);
                        AppPreference.setAddress(Login.this, address);
                        AppPreference.setEmail(Login.this, email);

                       /* username.setText("");
                        password.setText("");*/


                    } else if (response.equals("0")) {
                        dialog.dismiss();
                        Toast.makeText(Login.this, message, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                }
                super.onPostExecute(output);
            }
        }

    }


    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            // checkConnection();
            return true;
        } else {
            return false;
        }
    }

    public void checkConnection() {
        if (isOnline()) {
            Toast.makeText(Login.this, "You are connected to Internet", Toast.LENGTH_SHORT).show();
            new GetLoginAsynctask().execute();
        } else {
            Toast.makeText(Login.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
        }
    }

/*
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {

            finish();

            username.setText("");
            password.setText("");

        }
        return super.onKeyDown(keyCode, event);
    }*/

  /*  public void onBackPressed() {
        Login.this.finish();
    }*/
}
