package com.ics.nrs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.nrs.R;
import com.ics.nrs.SharedPreference.SessionManager;
import com.ics.nrs.Utility.Connectivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.OkHttpClient;


public class ComplaintFragment extends Fragment implements
        AdapterView.OnItemSelectedListener {


    private static final String TAG = "";
    // String[] country = { "Cooler", "Fan", "Washing Machine", "Television", "AC" };
    Spinner product_name;
    ArrayAdapter ad_product;
    ArrayList<String> product_list;
    String Selected_product;
    String result;
    String server_url;
    String mobile_no = "";

    AppPreference appPreference;
    private OkHttpClient mClient = new OkHttpClient();
    private Context mContext;
    EditText name, mob_no, address_, model_no_, serial_no_, pincode_;
    //Spinner product_name;
    String Name, Mobile_no, Model_no, Serial_no, Address, Pincode;
    String CustMobileno, CustName, StrModel, StrSerial, StrProduct, StrPincode, StrAddress;
    private OnFragmentInteractionListener mListener;
    private SessionManager manager;
    private String strCompCode = "";
    private boolean comp_Done = true;
    private Button submit, resetOtp;

    public ComplaintFragment() {
        // Required empty public constructor
    }

/*
    public static ComplaintFragment newInstance(String param1, String param2) {
        ComplaintFragment fragment = new ComplaintFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_complaint, container, false);
        manager = new SessionManager(getActivity());

        submit = (Button) view.findViewById(R.id.submit);
        name = (EditText) view.findViewById(R.id.customer_name);
        mob_no = (EditText) view.findViewById(R.id.mobile_no);
        address_ = (EditText) view.findViewById(R.id.address);
        product_name = (Spinner) view.findViewById(R.id.spinner1);
        model_no_ = (EditText) view.findViewById(R.id.model_no);
        serial_no_ = (EditText) view.findViewById(R.id.serial_no);
        pincode_ = (EditText) view.findViewById(R.id.pincode);


        // name.setText(CustName);
        mob_no.setText(CustMobileno);
        name.setText(CustName);
        model_no_.setText(StrModel);
        serial_no_.setText(StrSerial);
        pincode_.setText(StrPincode);
        address_.setText(StrAddress);
        product_list = new ArrayList<>();
        product_list.add("RO");
        product_list.add("Gizer");
        product_list.add("Washing Machine");
        product_list.add("Refigerator");
        product_list.add("D-Freeze");
        product_list.add("AC");


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Name = name.getText().toString().trim();
                Mobile_no = mob_no.getText().toString();
                Model_no = model_no_.getText().toString();
                Serial_no = serial_no_.getText().toString();
                Address = address_.getText().toString();
                Pincode = pincode_.getText().toString();


                if (Name.isEmpty()) {
                    name.setError("Name is required");
                    name.requestFocus();
                    return;
                }
                if (Mobile_no.isEmpty()) {
                    mob_no.setError("Mobile number is required");
                    mob_no.requestFocus();
                    return;
                }
                if (Model_no.isEmpty()) {
                    model_no_.setError("Model No. is required");
                    model_no_.requestFocus();
                    return;
                }
                if (Serial_no.isEmpty()) {
                    serial_no_.setError("Serial no is required");
                    serial_no_.requestFocus();
                    return;
                }
                if (Address.isEmpty()) {
                    address_.setError("Address is required");
                    address_.requestFocus();
                    return;
                }
                if (Pincode.isEmpty()) {
                    pincode_.setError("Pincode is required");
                    pincode_.requestFocus();
                    return;
                } else if (Pincode.length() != 6) {
                    pincode_.setError("Pincode Should Be 6 Digits");
                    pincode_.requestFocus();
                    return;
                }

                if (Connectivity.isNetworkAvailable(getActivity())) {
                    manager.storeComplainData(Name, Mobile_no, Address, Pincode, Selected_product, Model_no, Serial_no);
                    Intent intent = new Intent(getActivity(), OtpActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                } else {
                    Toast.makeText(getActivity(), "no Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });


        product_name = (Spinner) view.findViewById(R.id.spinner1);
        ad_product = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, product_list);
        ad_product.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        product_name.setAdapter(ad_product);
        product_name.setOnItemSelectedListener(this);

        return view;
    }


    private boolean validate() {
        if (name.getText().toString().trim().equals(""))
            return false;
        else if (address_.getText().toString().trim().equals(""))
            return false;
        else if (mob_no.getText().toString().trim().equals(""))
            return false;
        else if (model_no_.getText().toString().trim().equals(""))
            return false;
        else if (serial_no_.getText().toString().trim().equals(""))
            return false;
        else if (serial_no_.getText().toString().trim().equals(""))
            return false;
        else if (product_name.getSelectedItem().toString().trim().equals(""))
            return false;
        else
            return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Selected_product = product_name.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    //---------------------------------------------------------------------


    //-------------------------------------------------------------------------------------

    class GetComplaintList extends AsyncTask<String, String, String> {
        String output = "";
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Processing");
            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();
            //movieList = new ArrayList<>();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                server_url = "http://spellclasses.co.in/NRS/api/complianDetailsbyid?complain_id=" + strCompCode;
                Log.e("COMPCODE", strCompCode);
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
                    JSONObject data_obj = json.getJSONObject("data");
                    String id = json.getString("id");
                    String customer_name = json.getString("customer_name");
                    String mobile_no = json.getString("mobile_no");
                    String address = json.getString("address");
                    String product_name = json.getString("product_name");
                    String model_no = json.getString("model_no");
                    String serial_no = json.getString("serial_no");
                    String complain_id = json.getString("complain_id");
                    String complain_no = json.getString("complain_no");
                    String status = json.getString("status");
                    String pin_code = json.getString("pin_code");

                    name.setText(customer_name);
                    mob_no.setText(mobile_no);
                    address_.setText(address);
                    model_no_.setText(model_no);
                    serial_no_.setText(serial_no);
                    pincode_.setText(pin_code);

                    Log.e("json>>>>>>>>>", json.toString());


                    if (response.equalsIgnoreCase("1")) {
                        dialog.dismiss();

                        //  manager.stroreCompCode(complain_no);
                        //  AppPreference.setStatus(ComplaintList.this, status);


                    } else {
                        Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                }
                super.onPostExecute(output);
            }

        }

    }

//********************************************************************************************


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       /* if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/

        try {
            mListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }

/*
    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {
            if (Connectivity.isNetworkAvailable(getActivity())) {
                new GetComplaintList().execute();
            } else {
                Toast.makeText(getActivity(), "no Internet", Toast.LENGTH_SHORT).show();
            }
        }
    }*/


}
