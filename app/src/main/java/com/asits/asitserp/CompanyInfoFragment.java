package com.asits.asitserp;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;

import com.developer.mtextfield.ExtendedEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CompanyInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompanyInfoFragment extends Fragment {

    private PropertyInfo pi1;
    private PropertyInfo pi2;
    private PropertyInfo pi3;

    String Query_getCompany = "SELECT ASI04_Capital, ASI04_CompanyStatus ,ASI04_CompanyActivity_ID,ASI04_CompanyMaster_ID,ASI04_NumberOfStock,ASI04_StockPrice,ASI04_Fees  from ASI04_Application Where  ASI04_Application_ID=1000002";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ExtendedEditText TXT_CO_Name, TXT_CO_Activity, TXT_CO_Status, TXT_CO_Capital, TXT_CO_NumOfStock, TXT_CO_StockPrice, TXT_CO_Fees;
    private String CO_Name = "", CO_Activity = "", CO_Status = "", CO_Capital = "", CO_NumOfStock = "", CO_StockPrice = "", CO_Fees = "";
    private FrameLayout ParentframeLayout;

    public CompanyInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CompanyInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CompanyInfoFragment newInstance(String param1, String param2) {
        CompanyInfoFragment fragment = new CompanyInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_company_info, container, false);

        ParentframeLayout = requireActivity().findViewById(R.id.frameLayout);

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                setFragment(new MainFragment());
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        TXT_CO_Name = view.findViewById(R.id.TXT_CO_Name);
        TXT_CO_Activity = view.findViewById(R.id.TXT_CO_Activity);
        TXT_CO_Status = view.findViewById(R.id.TXT_CO_Status);
        TXT_CO_Capital = view.findViewById(R.id.TXT_CO_Capital);
        TXT_CO_NumOfStock = view.findViewById(R.id.TXT_CO_NumOfStock);
        TXT_CO_StockPrice = view.findViewById(R.id.TXT_CO_StockPrice);
        TXT_CO_Fees = view.findViewById(R.id.TXT_CO_Fees);
        SetEnable();

        Async_GetCompanyInfo async_getCompanyInfo = new Async_GetCompanyInfo();
        async_getCompanyInfo.execute(Query_getCompany);
        return view;
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slideout_from_left);

        fragmentTransaction.replace(ParentframeLayout.getId(), fragment);
        fragmentTransaction.commit();
    }

    private void SetEnable() {
        TXT_CO_Name.setEnabled(false);
        TXT_CO_Activity.setEnabled(false);
        TXT_CO_Status.setEnabled(false);
        TXT_CO_Capital.setEnabled(false);
        TXT_CO_NumOfStock.setEnabled(false);
        TXT_CO_StockPrice.setEnabled(false);
        TXT_CO_Fees.setEnabled(false);

    }

    private void SetText() {

        TXT_CO_Name.setText(CO_Name);
        TXT_CO_Activity.setText(CO_Activity);
        TXT_CO_Status.setText(CO_Status);
        TXT_CO_Capital.setText(CO_Capital);
        TXT_CO_NumOfStock.setText(CO_NumOfStock);
        TXT_CO_StockPrice.setText(CO_StockPrice);
        TXT_CO_Fees.setText(CO_Fees);

    }


    public class Async_GetCompanyInfo extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//        loading = ProgressDialog.show(getContext(),
//                "Loading ..", "Please wait...", true, true);

        }

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... strings) {
            SoapObject request = new SoapObject(SaveSettings.NAMESPACE, SaveSettings.GETRECORD_METHOD);

            pi1 = new PropertyInfo();
            pi1.setName("selectQuery");
            pi1.setValue(strings[0]);//get the string that is to be sent to the web service
            pi1.setType(String.class);
            request.addProperty(pi1);

            pi2 = new PropertyInfo();
            pi2.setName("_session_ID");
            pi2.setValue( "1001147");//get the string that is to be sent to the web service
            pi2.setType(String.class);
            request.addProperty(pi2);

            pi3 = new PropertyInfo();
            pi3.setName("accessKey");
            pi3.setValue(SaveSettings.ACCESS_KEY);//get the string that is to be sent to the web service
            pi3.setType(String.class);
            request.addProperty(pi3);

            SoapSerializationEnvelope envp = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envp.dotNet = true;
            envp.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(SaveSettings.URL);
            try {
                JSONObject jsonObject = null;
                JSONObject jsonObject1 = null;
                JSONObject jsonObject2 = null;
                androidHttpTransport.call(SaveSettings.SOAP_ACTION + SaveSettings.GETRECORD_METHOD, envp);
                SoapPrimitive resultRequestSOAP = null;
                resultRequestSOAP = (SoapPrimitive) envp.getResponse();
                jsonObject = XML.toJSONObject(String.valueOf(resultRequestSOAP)); //convert xml to json
                System.out.println("********Response : " + resultRequestSOAP.toString());

                jsonObject1 = jsonObject.getJSONObject("NewDataSet");
                //   jsonObject2 = (JSONObject) jsonObject1.get("NewDataSet");

                //    Object json = new JSONTokener(jsonObject1.toString()).nextValue();
                JSONObject json = new JSONObject(jsonObject1.toString());

                JSONObject dataObject = json.optJSONObject("Table");
                if (dataObject instanceof JSONObject) {
                    // System.out.println("********JSONArray : " + resultRequestSOAP.toString());
                    jsonObject2 = jsonObject1.getJSONObject("Table");

                    CO_Name = jsonObject2.getString("ASI04_COMPANYMASTER_ID");
                    CO_Activity = jsonObject2.getString("ASI04_COMPANYACTIVITY_ID");
//                    CO_Status = jsonObject2.getString("ASI04_COMPANYSTATUS");
                    CO_Capital = jsonObject2.getString("ASI04_CAPITAL");
                    CO_NumOfStock = jsonObject2.getString("ASI04_NUMBEROFSTOCK");
                    CO_StockPrice = jsonObject2.getString("ASI04_STOCKPRICE");
                    CO_Fees = jsonObject2.getString("ASI04_FEES");



                }
                //you have an object
                else {

                    JSONArray arr = jsonObject1.getJSONArray("Table");
                    for (int i = 0; i < arr.length(); i++) {

                        //  JSONObject mJsonObjectProperty = mJsonArrayProperty.getJSONObject(i);
                        JSONObject obj = arr.getJSONObject(i);
                        CO_Name = obj.getString("C_CITY_ID");
                        CO_Activity = obj.getString("C_CITY_ID");
                        CO_Status = obj.getString("C_CITY_ID");
                        CO_Capital = obj.getString("C_CITY_ID");
                        CO_NumOfStock = obj.getString("C_CITY_ID");
                        CO_StockPrice = obj.getString("C_CITY_ID");
                        CO_Fees = obj.getString("C_CITY_ID");

                    }
                }


            } catch (JSONException e) {
                Log.e("WS Error->", e.toString());
            } catch (IOException | XmlPullParserException soapFault) {
                soapFault.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            SetText();

        }
    }
}