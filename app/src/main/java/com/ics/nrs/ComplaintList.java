package com.ics.nrs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.nrs.R;
import com.ics.nrs.SharedPreference.SessionManager;
import com.ics.nrs.Utility.Connectivity;
import com.ics.nrs.Utility.RecyclerTouchListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class ComplaintList extends AppCompatActivity {

    private List<Complaint> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ComplaintAdapter mAdapter;
    private ImageView staff_profile;
    private ImageView staff_image, logout;
    String server_url;
    String Complaint_no, profile;
    LinearLayout pending, closed;
    AppPreference logoutSession;
    private SessionManager manager;
    private String complain_no;
    // String status;
    TextView forgetPassword;
    private Button resenr_H_Code;
    private String strMob_otp;
    private EditText editText1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_list);
        manager = new SessionManager(this);
        staff_image = (ImageView) findViewById(R.id.person);
        resenr_H_Code = findViewById(R.id.resendHCode);
        Complaint_no = AppPreference.getComplaintId(ComplaintList.this);

        profile = AppPreference.getProfile(ComplaintList.this);


        logout = (ImageView) findViewById(R.id.logout);
        pending = (LinearLayout) findViewById(R.id.pending);
        closed = (LinearLayout) findViewById(R.id.closed);
        forgetPassword = (TextView) findViewById(R.id.forgetPassword);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        staff_profile = (ImageView) findViewById(R.id.person_profile);
        staff_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popup = new PopupMenu(ComplaintList.this, staff_profile);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.poupup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent intent = new Intent(ComplaintList.this, StaffProfile.class);
                        startActivity(intent);
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }

        });


        if (Connectivity.isNetworkAvailable(this)) {
            new GetComplaintList().execute();
        } else {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
        }

        logoutSession = new AppPreference(ComplaintList.this);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutSession.logoutSession();
                finish();
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(ComplaintList.this, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Complaint complaint = movieList.get(position);
                String CompCode = complaint.getComplaint_code();
                manager.stroreCompCode(CompCode);
                Intent intent = new Intent(view.getContext(), NewFeedbackActivity.class);
//              intent.putExtra("CompCode",CompCode);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        resenr_H_Code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otpDialog();
            }
        });


    }


    class GetComplaintList extends AsyncTask<String, String, String> {
        String output = "";
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(ComplaintList.this);
            dialog.setMessage("Processing");
            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();
            //movieList = new ArrayList<>();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                server_url = "https://infocentroid.us/NRS_LIVE/Api/complian_show?complain_id=" + Complaint_no;

            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e("sever_url>>>>>>>>>", server_url);
            output = HttpHandler.makeServiceCall(server_url);
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
                    JSONArray message_array = json.getJSONArray("data");
                    for (int i = 0; i < message_array.length(); i++) {
                        JSONObject m = message_array.getJSONObject(i);
                        String id = m.getString("id");
                        String customer_name = m.getString("customer_name");
                        String mobile_no = m.getString("mobile_no");
                        String address = m.getString("address");
                        String product_name = m.getString("product_name");
                        String model_no = m.getString("model_no");
                        String serial_no = m.getString("serial_no");
                        String complain_id = m.getString("complain_id");
                        complain_no = m.getString("complain_no");
                        String status = m.getString("status");
                        Complaint complaintCons = new Complaint();
                        complaintCons.setComplaint_code(complain_no);
                        complaintCons.setName(customer_name);
                        complaintCons.setMobile_no(mobile_no);
                        complaintCons.setLocation(address);
                        complaintCons.setProductName(product_name);

                        movieList.add(complaintCons);


                    }

                    mAdapter = new ComplaintAdapter(movieList);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(mAdapter);


                    Log.e("json>>>>>>>>>", json.toString());


                    if (response.equalsIgnoreCase("1")) {
                        dialog.dismiss();

                        manager.stroreCompCode(complain_no);

                        //  AppPreference.setStatus(ComplaintList.this, status);


                    } else {
                        Toast.makeText(ComplaintList.this, response, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                }
                super.onPostExecute(output);
            }

        }

    }

    public void onBackPressed() {
        Intent intent = new Intent(ComplaintList.this, MainActivity.class);
        startActivity(intent);
        //ComplaintList.this.finish();
    }


  /*  @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {

            finish();

           *//* username.setText("");
            password.setText("");*//*

        }
        return super.onKeyDown(keyCode, event);
    }*/

    private void otpDialog() {

        final Dialog dialog = new Dialog(ComplaintList.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.otp_reset_layout);
        // dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.show();

        // final Spinner spinner = (Spinner) dialog.findViewById(R.id.spinner1);
        final TextView text1 = (TextView) dialog.findViewById(R.id.text1);
        editText1 = (EditText) dialog.findViewById(R.id.editText1);

        Button button = (Button) dialog.findViewById(R.id.button1);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                strMob_otp = editText1.getText().toString();
                if (strMob_otp.isEmpty()) {
                    editText1.setError("Mobile Number Is Required");
                    editText1.requestFocus();
                    return;
                }
                if (strMob_otp.length() != 10) {
                    editText1.setError("Mobile Number Should Be 10 Digits");
                    editText1.requestFocus();
                    return;
                }

                if (Connectivity.isNetworkAvailable(ComplaintList.this)) {
                    dialog.dismiss();
                    new SendJsonDataToServer(strMob_otp).execute();
                    Log.e("OTTP", strMob_otp);
                } else {
                    Toast.makeText(ComplaintList.this, "no Internet", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    class SendJsonDataToServer extends AsyncTask<String, String, String> {

        ProgressDialog dialog;
        String strMob_otp;

        public SendJsonDataToServer(String strMob_otp) {
            this.strMob_otp = strMob_otp;
        }

        protected void onPreExecute() {
            dialog = new ProgressDialog(ComplaintList.this);
            dialog.show();

        }

        protected String doInBackground(String... arg0) {

            try {

//                URL url = new URL("https://infocentroid.us/NRS_LIVE/Api/resendotp");
                URL url = new URL("https://infocentroid.us/NRS_LIVE/Api/resendhappycode");


                JSONObject postDataParams = new JSONObject();
                postDataParams.put("mobile_no", strMob_otp);

                Log.e("postDataParams", postDataParams.toString());

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
                try {
                    JSONObject obj = new JSONObject(result);
                    String status = obj.getString("status");
                    String message = obj.getString("message");
                    if (status.equals("true")) {
                        Toast.makeText(ComplaintList.this, "Happy Code Send To " + strMob_otp + " Number", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ComplaintList.this, message, Toast.LENGTH_LONG).show();

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


}
