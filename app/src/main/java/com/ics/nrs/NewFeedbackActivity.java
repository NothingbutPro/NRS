package com.ics.nrs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.nrs.R;
import com.ics.nrs.SharedPreference.SessionManager;

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
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class NewFeedbackActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener, View.OnClickListener {

    private int _clicks = 0;
    boolean isPlaying = false;

    RadioGroup feedback_type;
    RadioButton repair, service, installation;
    String Condition;
    String Id;
    Spinner spin, spin2;
    LinearLayout installation_layout, service_layout, service_in_warranty, out_warranty_service, repair_layout, in_warranty_repair, out_warranty_repair;

    EditText complaintCode, prodct_name, serial_no;
    EditText in_complaint_code_repair, et_machine_serial_in, oldPartName, newPartName, oldPartSerial, newPartSerial, oldPartCode, newPartCode, et_feedback;
    String InComplaintCodeRepair, MachineSerialIn, OldPartName, NewPartName, OldPartSerial, NewPartSerial, OldPartCode, NewPartCode, RepairFeedback;

    EditText out_complaint_code_repair, payment_out_repair, out_repair_newPartName, out_repair_newPartSerial, out_repair_newPartCode, out_repair_feedback;
    String OutComplaintCodeRepair, PaymentOutRepair, OutRepairNewPartName, OutRepairNewPartSerial, OutRepairNewPartCode, OutRepairFeedback;

    EditText in_complaint_code, in_product_name, in_machine, out_complaint_code, out_payment_service;
    String InComplaintCode, inProductName, InMachine, OutComplaintCode, OutPaymentService;

    String ComplaintCode, ProductName, SerialNo;
    Button submit, installation_btn, service_btn;
    private SessionManager manager;

    String repair_warranty_type, service_warranty_type;
    String strCompCode;

    String[] warranty = {"In-warranty", "Out-of-warranty"};
    String[] warranty2 = {"In-warranty", "Out-of-warranty"};
    private boolean isSolved = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_feedback);
        manager = new SessionManager(this);

        strCompCode = manager.getCompCode();
        Log.e("Complain_Code", strCompCode);

        in_complaint_code = (EditText) findViewById(R.id.in_complaint_code);
        in_product_name = (EditText) findViewById(R.id.in_product_name);
        in_machine = (EditText) findViewById(R.id.in_machine);
        out_complaint_code = (EditText) findViewById(R.id.out_complaint_code);
        out_payment_service = (EditText) findViewById(R.id.out_payment_service);

        in_complaint_code_repair = (EditText) findViewById(R.id.in_complaint_code_repair);
        et_machine_serial_in = (EditText) findViewById(R.id.et_machine_serial_in);
        oldPartName = (EditText) findViewById(R.id.oldPartName);
        newPartName = (EditText) findViewById(R.id.newPartName);
        oldPartSerial = (EditText) findViewById(R.id.oldPartSerial);
        newPartSerial = (EditText) findViewById(R.id.newPartSerial);
        oldPartCode = (EditText) findViewById(R.id.oldPartCode);
        newPartCode = (EditText) findViewById(R.id.newPartCode);
        et_feedback = (EditText) findViewById(R.id.et_feedback);
        out_complaint_code_repair = (EditText) findViewById(R.id.out_complaint_code_repair);
        payment_out_repair = (EditText) findViewById(R.id.payment_out_repair);
        out_repair_newPartName = (EditText) findViewById(R.id.out_repair_newPartName);
        out_repair_newPartSerial = (EditText) findViewById(R.id.out_repair_newPartSerial);
        out_repair_newPartCode = (EditText) findViewById(R.id.out_repair_newPartCode);
        out_repair_feedback = (EditText) findViewById(R.id.out_repair_feedback);
        feedback_type = (RadioGroup) findViewById(R.id.feedback_type);
        repair = (RadioButton) findViewById(R.id.repair);
        service = (RadioButton) findViewById(R.id.service);
        installation = (RadioButton) findViewById(R.id.installation);

        installation_layout = (LinearLayout) findViewById(R.id.installation_layout);
        service_layout = (LinearLayout) findViewById(R.id.service_layout);
        repair_layout = (LinearLayout) findViewById(R.id.repair_layout);

        service_in_warranty = (LinearLayout) findViewById(R.id.service_in_warranty);
        out_warranty_service = (LinearLayout) findViewById(R.id.out_warranty_service);
        in_warranty_repair = (LinearLayout) findViewById(R.id.in_warranty_repair);
        out_warranty_repair = (LinearLayout) findViewById(R.id.out_warranty_repair);
        // in_complaint_code_repair.setText(strCompCode);

        feedback_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {


                if (repair.isChecked()) {
                    Condition = "repair";
                    Id = "1";
                    service.setChecked(false);
                    installation.setChecked(false);

                    repair_layout.setVisibility(View.VISIBLE);
                    service_layout.setVisibility(View.GONE);
                    installation_layout.setVisibility(View.GONE);

                    // Toast.makeText(NewFeedbackActivity.this,"recommend is Selected" , Toast.LENGTH_LONG).show();
                }

                if (service.isChecked()) {
                    Condition = "service";
                    Id = "2";
                    repair.setChecked(false);
                    installation.setChecked(false);


                    repair_layout.setVisibility(View.GONE);
                    service_layout.setVisibility(View.VISIBLE);
                    installation_layout.setVisibility(View.GONE);

                }

                if (installation.isChecked()) {
                    Condition = "installation";
                    Id = "3";
                    repair.setChecked(false);
                    service.setChecked(false);

                    repair_layout.setVisibility(View.GONE);
                    service_layout.setVisibility(View.GONE);
                    installation_layout.setVisibility(View.VISIBLE);
                }

            }
        });


        spin = (Spinner) findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, warranty);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);


        spin2 = (Spinner) findViewById(R.id.spinner2);
        spin2.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, warranty2);
        aa2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin2.setAdapter(aa2);


        submit = (Button) findViewById(R.id.bt_submit);
        installation_btn = (Button) findViewById(R.id.installation_submit);
        service_btn = (Button) findViewById(R.id.service_submit);

        complaintCode = (EditText) findViewById(R.id.et_complaint);
        prodct_name = (EditText) findViewById(R.id.et_product_name);
        serial_no = (EditText) findViewById(R.id.et_machine_serial);

        submit.setOnClickListener(this);
        service_btn.setOnClickListener(this);
        installation_btn.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {

        attemptFeedback();

        switch (view.getId()) {

            case R.id.bt_submit:

                if (repair_warranty_type.equals("0")) {
                    if (!InComplaintCodeRepair.equals("")) {
                        if (in_complaint_code_repair.getText().toString().equalsIgnoreCase(strCompCode)) {
                            if (!MachineSerialIn.equals("")) {
                                if (!OldPartName.equals("")) {
                                    if (!NewPartName.equals("")) {
                                        if (!OldPartSerial.equals("")) {
                                            if (!NewPartSerial.equals("")) {
                                                if (!OldPartCode.equals("")) {
                                                    if (!NewPartCode.equals("")) {
                                                        if (!RepairFeedback.equals("")) {

                                                            new SendFeedbackRepair().execute();

                                                        } else {
                                                            et_feedback.setError("Feedback is required");
                                                            et_feedback.requestFocus();
                                                            return;
                                                        }
                                                    } else {
                                                        newPartCode.setError("New part code is required");
                                                        newPartCode.requestFocus();
                                                        return;
                                                    }
                                                } else {
                                                    oldPartCode.setError("Old part code is required");
                                                    oldPartCode.requestFocus();
                                                    return;
                                                }
                                            } else {
                                                newPartSerial.setError("New part name is required");
                                                newPartSerial.requestFocus();
                                                return;
                                            }
                                        } else {
                                            oldPartSerial.setError("Old part serial number is required");
                                            oldPartSerial.requestFocus();
                                            return;
                                        }
                                    } else {
                                        newPartName.setError("New part name is required");
                                        newPartName.requestFocus();
                                        return;
                                    }
                                } else {
                                    oldPartName.setError("Old part name is required");
                                    oldPartName.requestFocus();
                                    return;
                                }
                            } else {
                                et_machine_serial_in.setError("Serial number is required");
                                et_machine_serial_in.requestFocus();
                                return;
                            }
                        } else {
                            in_complaint_code_repair.setError("Complaint code is Wrong");
                            in_complaint_code_repair.requestFocus();
                            return;
                        }
                    } else {
                        in_complaint_code_repair.setError("Complaint code is required");
                        in_complaint_code_repair.requestFocus();
                        return;
                    }
                } else if (repair_warranty_type.equals("1")) {

                    if (!OutComplaintCodeRepair.equals("")) {

                        if (out_complaint_code_repair.getText().toString().equalsIgnoreCase(strCompCode)) {

                            if (!PaymentOutRepair.equals("")) {

                                if (!OutRepairNewPartName.equals("")) {

                                    if (!OutRepairNewPartSerial.equals("")) {

                                        if (!OutRepairNewPartCode.equals("")) {

                                            if (!OutRepairFeedback.equals("")) {

                                                new SendFeedbackRepair().execute();


                                            } else {
                                                out_repair_feedback.setError("Feedback is required");
                                                out_repair_feedback.requestFocus();
                                                return;
                                            }

                                        } else {
                                            out_repair_newPartCode.setError("New part name is required");
                                            out_repair_newPartCode.requestFocus();
                                            return;
                                        }

                                    } else {
                                        out_repair_newPartSerial.setError("New part name is required ");
                                        out_repair_newPartSerial.requestFocus();
                                        return;
                                    }

                                } else {
                                    out_repair_newPartName.setError("New part name is required");
                                    out_repair_newPartName.requestFocus();
                                    return;
                                }


                            } else {
                                out_complaint_code_repair.setError("Complaint code is Wrong");
                                out_complaint_code_repair.requestFocus();
                                return;
                            }
                        } else {
                            payment_out_repair.setError("Payment is required ");
                            payment_out_repair.requestFocus();
                            return;
                        }

                    } else {
                        out_complaint_code_repair.setError("Complaint code is required ");
                        out_complaint_code_repair.requestFocus();
                        return;
                    }

                }

                break;

            case R.id.service_submit:

                if (service_warranty_type.equals("0")) {
                    if (!InComplaintCode.equals("")) {

                        if (in_complaint_code.getText().toString().equalsIgnoreCase(strCompCode)) {

                            if (!inProductName.equals("")) {
                                if (!InMachine.equals("")) {
                                    new SendFeedbackService().execute();
                                } else {
                                    in_machine.setError("Machine is required");
                                    in_machine.requestFocus();
                                    return;
                                }
                            } else {
                                in_product_name.setError("Product name is required");
                                in_product_name.requestFocus();
                                return;
                            }
                        } else {
                            in_complaint_code.setError("Complaint Code is wrong");
                            in_complaint_code.requestFocus();
                            return;
                        }

                    } else {
                        in_complaint_code.setError("Complaint Code is required");
                        in_complaint_code.requestFocus();
                        return;
                    }
                } else if (service_warranty_type.equals("1")) {
                    if (!OutComplaintCode.equals("")) {

                        if (out_complaint_code.getText().toString().equalsIgnoreCase(strCompCode)) {


                            if (!OutPaymentService.equals("")) {
                                new SendFeedbackService().execute();
                            } else {
                                out_payment_service.setError("Payment is required");
                                out_payment_service.requestFocus();
                                return;
                            }
                        } else {
                            out_complaint_code.setError("Complaint code is wrong");
                            out_complaint_code.requestFocus();
                            return;
                        }

                    } else {
                        out_complaint_code.setError("Complaint code is required");
                        out_complaint_code.requestFocus();
                        return;
                    }
                }


                break;


            case R.id.installation_submit:


                if (!ComplaintCode.equals("")) {

                    if (complaintCode.getText().toString().equalsIgnoreCase(strCompCode)) {

                        if (!ProductName.equals("")) {

                            if (!SerialNo.equals("")) {

                                new SendFeedbackInstallation().execute();


                            } else {
                                serial_no.setError("Serial number is required");
                                serial_no.requestFocus();
                                return;
                            }
                        } else {
                            prodct_name.setError("Product Name is required");
                            prodct_name.requestFocus();
                            return;
                        }
                    } else {
                        complaintCode.setError("Complaint Code is wrong");
                        complaintCode.requestFocus();
                        return;
                    }
                } else {
                    complaintCode.setError("Complaint Code should not empty");
                    complaintCode.requestFocus();
                    return;
                }

               /* if (!ComplaintCode.equals("") && ComplaintCode.equals(strCompCode)){
                    if (!ProductName.equals("")){

                        if (!SerialNo.equals("")){

                            new SendFeedbackInstallation().execute();

                        } else if (SerialNo.isEmpty()) {
                            serial_no.setError("Serial number is required");
                            serial_no.requestFocus();
                            return;
                        }

                    } else if (ProductName.isEmpty()) {
                        prodct_name.setError("Product Name is required");
                        prodct_name.requestFocus();
                        return;
                    }

                }else if (ComplaintCode.isEmpty()) {
                    complaintCode.setError("Complaint Code should not empty");
                    complaintCode.requestFocus();
                    return;
                }
*/
                break;

        }




               /* if (isPlaying) {
                    new SendFeedbackRepair().execute();
                }else{
                    new SendFeedbackInstallation().execute();

                }
                isPlaying = !isPlaying;*/


               /* int count = ++_clicks;

                if(count == 1){

                    new SendFeedbackRepair().execute();

                }
                    if(count == 2){

                    }

                        if(count == 3){

                            new SendFeedbackInstallation().execute();


                        }*/


               /* if (view.getId()==R.id.repair_layout){
                    new SendFeedbackRepair().execute();
                }
                else if (view.getId()==R.id.installation_layout){
                    new SendFeedbackInstallation().execute();
                }*/


    }


    private void attemptFeedback() {

        ComplaintCode = complaintCode.getText().toString().trim();
        ProductName = prodct_name.getText().toString().trim();
        SerialNo = serial_no.getText().toString().trim();

        InComplaintCodeRepair = in_complaint_code_repair.getText().toString();
        MachineSerialIn = et_machine_serial_in.getText().toString();
        OldPartName = oldPartName.getText().toString();
        NewPartName = newPartName.getText().toString();
        OldPartSerial = oldPartSerial.getText().toString();
        NewPartSerial = newPartSerial.getText().toString();
        OldPartCode = oldPartCode.getText().toString();
        NewPartCode = newPartCode.getText().toString();
        RepairFeedback = et_feedback.getText().toString();
        OutComplaintCodeRepair = out_complaint_code_repair.getText().toString();
        PaymentOutRepair = payment_out_repair.getText().toString();
        OutRepairNewPartName = out_repair_newPartName.getText().toString();
        OutRepairNewPartSerial = out_repair_newPartSerial.getText().toString();
        OutRepairNewPartCode = out_repair_newPartCode.getText().toString();
        OutRepairFeedback = out_repair_feedback.getText().toString();

        InComplaintCode = in_complaint_code.getText().toString();
        inProductName = in_product_name.getText().toString();
        InMachine = in_machine.getText().toString();
        OutComplaintCode = out_complaint_code.getText().toString();
        OutPaymentService = out_payment_service.getText().toString();


    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


        Spinner spinner = (Spinner) adapterView;
        Spinner spinner2 = (Spinner) adapterView;

        if (spinner.getId() == R.id.spinner2) {
            if (i != 0) {

                in_warranty_repair.setVisibility(View.GONE);
                out_warranty_repair.setVisibility(View.VISIBLE);
                //  service_in_warranty.setVisibility(View.GONE);
                //  out_warranty_service.setVisibility(View.GONE);

                repair_warranty_type = "" + 1;


            } else {

                in_warranty_repair.setVisibility(View.VISIBLE);
                out_warranty_repair.setVisibility(View.GONE);
                // service_in_warranty.setVisibility(View.GONE);
                // out_warranty_service.setVisibility(View.GONE);

                repair_warranty_type = "" + 0;

            }

        } else if (spinner2.getId() == R.id.spinner) {
            if (i != 0) {

                //   in_warranty_repair.setVisibility(View.GONE);
                //   out_warranty_repair.setVisibility(View.GONE);
                service_in_warranty.setVisibility(View.GONE);
                out_warranty_service.setVisibility(View.VISIBLE);

                service_warranty_type = "" + 1;

            } else {

                //   in_warranty_repair.setVisibility(View.GONE);
                //  out_warranty_repair.setVisibility(View.GONE);
                service_in_warranty.setVisibility(View.VISIBLE);
                out_warranty_service.setVisibility(View.GONE);

                service_warranty_type = "" + 0;


            }

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    //*****************************Repair***************************************************

    class SendFeedbackRepair extends AsyncTask<String, String, String> {

        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(NewFeedbackActivity.this);
            dialog.show();

        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("https://infocentroid.us/NRS_LIVE/Api/repair");


                JSONObject postDataParams = new JSONObject();
                postDataParams.put("flag", repair_warranty_type);
                postDataParams.put("mac_serial_no", MachineSerialIn);
                postDataParams.put("old_part_name", OldPartName);
                postDataParams.put("old_part_serial_no", OldPartSerial);
                postDataParams.put("old_part_code", OldPartCode);
                postDataParams.put("w_new_part_name", NewPartName);
                postDataParams.put("w_part_code", NewPartCode);
                postDataParams.put("w_part_serial_no", NewPartSerial);
                postDataParams.put("w_feedback", RepairFeedback);
                postDataParams.put("o_payment", PaymentOutRepair);
                postDataParams.put("o_new_part_name", OutRepairNewPartName);
                postDataParams.put("o_new_part_serial_no", OutRepairNewPartSerial);
                postDataParams.put("o_new_part_code", OutRepairNewPartCode);
                postDataParams.put("o_feedback", OutRepairFeedback);
                postDataParams.put("w_complain_code", InComplaintCodeRepair);
                postDataParams.put("o_complain_code", OutComplaintCodeRepair);


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

                  /*  BufferedReader in = new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {

                        StringBuffer Ss = sb.append(line);
                        Log.e("Ss", Ss.toString());
                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();*/

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
                Log.e("SendFeedbackRepair>>>", result.toString());
                try {


                    JSONObject jsonObject = new JSONObject(result);

                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");

                    JSONObject data = jsonObject.getJSONObject("data");

                    String flag = data.getString("flag");
                    String w_complain_code = data.getString("w_complain_code");
                    String mac_serial_no = data.getString("mac_serial_no");
                    String old_part_name = data.getString("old_part_name");
                    String old_part_serial_no = data.getString("old_part_serial_no");
                    String old_part_code = data.getString("old_part_code");
                    String w_new_part_name = data.getString("w_new_part_name");
                    String w_part_code = data.getString("w_part_code");
                    String w_part_serial_no = data.getString("w_part_serial_no");
                    String w_feedback = data.getString("w_feedback");
                    String o_complain_code = data.getString("o_complain_code");
                    String o_payment = data.getString("o_payment");
                    String o_new_part_name = data.getString("o_new_part_name");
                    String o_new_part_serial_no = data.getString("o_new_part_serial_no");
                    String o_new_part_code = data.getString("o_new_part_code");
                    String o_feedback = data.getString("o_feedback");
                    String date = data.getString("date");


                    Log.e("Repair>>>>", jsonObject.toString() + " " + status + " " + message);

                    if (status.equalsIgnoreCase("true")) {

                        Toast.makeText(NewFeedbackActivity.this, message, Toast.LENGTH_LONG).show();


                        Intent intent = new Intent(NewFeedbackActivity.this, ComplaintList.class);
                        startActivity(intent);


                    }

                } catch (JSONException e) {
                    Toast.makeText(NewFeedbackActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
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


    //********************************Service****************************************************


    class SendFeedbackService extends AsyncTask<String, String, String> {

        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(NewFeedbackActivity.this);
            dialog.show();

        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("https://infocentroid.us/NRS_LIVE/Api/services");


                JSONObject postDataParams = new JSONObject();
                postDataParams.put("flag", service_warranty_type);
                postDataParams.put("w_compln_code", InComplaintCode);
                postDataParams.put("w_product_name", inProductName);
                postDataParams.put("w_mac_serial_no", InMachine);
                postDataParams.put("o_compln_code", OutComplaintCode);
                postDataParams.put("o_payment", OutPaymentService);


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

                // JSONObject jsonObject = null;
                Log.e("SendFeedbackService>>>", result.toString());
                try {


                    JSONObject jsonObject = new JSONObject(result);

                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");

                    JSONObject data = jsonObject.getJSONObject("data");

                    String flag = data.getString("flag");
                    String w_compln_code = data.getString("w_compln_code");
                    String w_product_name = data.getString("w_product_name");
                    String w_mac_serial_no = data.getString("w_mac_serial_no");
                    String o_compln_code = data.getString("o_compln_code");
                    String o_payment = data.getString("o_payment");
                    String date = data.getString("date");


                    Log.e(">>>>", jsonObject.toString() + " " + status + " " + message);

                    if (status.equalsIgnoreCase("true")) {

                        Toast.makeText(NewFeedbackActivity.this, message, Toast.LENGTH_LONG).show();
                        String strMob=manager.getCompMobNo();
                        manager.isComplainSolve(strMob,isSolved);
                        Intent intent = new Intent(NewFeedbackActivity.this, ComplaintList.class);
                        startActivity(intent);
                        finish();
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


    //*******************************Installation*********************************************************


    class SendFeedbackInstallation extends AsyncTask<String, String, String> {

        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(NewFeedbackActivity.this);
            dialog.show();

        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("https://infocentroid.us/NRS_LIVE/Api/instalation");


                JSONObject postDataParams = new JSONObject();
                postDataParams.put("complain_code", ComplaintCode);
                postDataParams.put("product_name", ProductName);
                postDataParams.put("m_serial_no", SerialNo);

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

                  /*  BufferedReader in = new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {

                        StringBuffer Ss = sb.append(line);
                        Log.e("Ss", Ss.toString());
                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();*/

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
                Log.e("SendFeedback>>>", result.toString());
                try {


                    JSONObject jsonObject = new JSONObject(result);

                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");

                    JSONObject data = jsonObject.getJSONObject("data");

                    String product_name = data.getString("product_name");
                    String complain_code = data.getString("complain_code");
                    String m_serial_no = data.getString("m_serial_no");
                    String date = data.getString("date");


                    Log.e(">>>>", jsonObject.toString() + " " + status + " " + message);

                    if (status.equalsIgnoreCase("true")) {

                        Toast.makeText(NewFeedbackActivity.this, message, Toast.LENGTH_LONG).show();


                        Intent intent = new Intent(NewFeedbackActivity.this, ComplaintList.class);
                        startActivity(intent);
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
