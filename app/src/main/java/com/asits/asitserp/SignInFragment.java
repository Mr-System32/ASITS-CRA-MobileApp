package com.asits.asitserp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.kaopiz.kprogresshud.KProgressHUD;

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
import java.text.SimpleDateFormat;
import java.util.Locale;

import com.developer.mtextfield.ExtendedEditText;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment {
    private ImageView IMG_QR;

    //  static String Name = "", Email = "", Pic = "", MembersinceDate = "", ValidFrom = "", ValidTO = "", CurrentLevel = "", Salesoffice_ID, Salesoffice_Name, MemberID;

    String myFormat = "dd-MMM-yyyy"; //In which you need put here
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

    String C_BPartner_ID = "";
    private TextView dontHaveAnAccount;
    private ExtendedEditText TXT_Email;
    private ExtendedEditText TXT_Password;
    private Button SinginBTN;
    private FrameLayout ParentframeLayout;
    private final String emailPattren = "[a-zA-z0-9._-]+@[a-z]+.[a-z]+";
    ProgressDialog loading;
    SignInFragment context;
    private PropertyInfo pi1;
    private PropertyInfo pi2;
    private PropertyInfo pi3;
    private PropertyInfo pi4;
    private PropertyInfo pi5;
    private PropertyInfo pi6;
    //PIC
    KProgressHUD kProgressHUD;

    //String C_BPartner_query = "Select C_BPARTNER_ID,EMAIL,NAME from AD_User where AD_User_ID= ";
    String PIC_query = "Select C_BPARTNER_ID,PIC,NAME from C_BPARTNER where C_BPARTNER_ID= ";
    String Branches_query = "SELECT TRL.NAME AS Name,SO.NAME as NameEn,SO.VALUE as Value, SO.C_SALESOFFICE_ID as C_SALESOFFICE_ID FROM C_SALESOFFICE SO INNER JOIN C_SALESOFFICE_TRL TRL ON TRL.C_SALESOFFICE_ID=SO.C_SALESOFFICE_ID WHERE SO.ISACTIVE='Y' AND SO.IEU01_UseInWebsite='Y'";
    String C_BPartner_query = "Select BP.IEU01_MemberID , BP.C_SalesOffice_ID, BP.IEU01_CurrLvl,BP.IEU01_MemVldTo,BP.IEU01_MemVldFrm,BP.IEU01_MemberSince , AD.C_BPartner_ID , BP.PIC , BP.NAME , BP.EMAIL, BP.Mobile FROM (Select C_BPartner_ID from AD_USER where AD_USER_ID=" + SaveSettings.AD_USER_ID + ")  AD INNER join C_BPartner BP  on(AD.C_BPartner_ID = BP.C_BPartner_ID)";

    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!SaveSettings.UserEmail.equals("empty") && !SaveSettings.UserEmail.equals("")) {
            TXT_Email.setText(SaveSettings.UserEmail);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        context = this;
        dontHaveAnAccount = view.findViewById(R.id.goto_sing_up);
        TXT_Email = view.findViewById(R.id.TXT_CO_StockPrice);
        TXT_Password = view.findViewById(R.id.sign_in_password);
        SinginBTN = view.findViewById(R.id.sing_in_btn);
        ParentframeLayout = getActivity().findViewById(R.id.frameLayout);
        IMG_QR = view.findViewById(R.id.IMG_QR);
        IMG_QR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Scanner.class);
                startActivity(intent);
            }
        });


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        kProgressHUD = new KProgressHUD(getContext())
                .create(getContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("يرجى الأنتظار")
                .setDetailsLabel("")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        TXT_Email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInput();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        TXT_Password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInput();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        dontHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), CreateNewApp.class);
//                startActivity(intent);
            }
        });

        SinginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chackEmailandPassword();

            }
        });


    }

    private void checkInput() {
//        if (!TextUtils.isEmpty(Email.getText())) {
//            if (!TextUtils.isEmpty(Password.getText()) && (Password.length() >= 8)) {
//
//                SinginBTN.setEnabled(true);
//                SinginBTN.setTextColor(getResources().getColor(R.color.white));
//
//            } else {
//                SinginBTN.setEnabled(false);
//                SinginBTN.setTextColor(getResources().getColor(R.color.customeblue));
//            }
//
//        } else {
//            SinginBTN.setEnabled(false);
//            SinginBTN.setTextColor(getResources().getColor(R.color.customeblue));
//        }


    }

    private void chackEmailandPassword() {

//        if (Email.getText().toString().matches(emailPattren)) {
//            if (Password.length() >= 1) {
//                SinginBTN.setEnabled(false);
//                SinginBTN.setTextColor(getResources().getColor(R.color.customeblue));


        Async_Login jsonTask = new Async_Login();
        jsonTask.execute(TXT_Email.getText().toString(), TXT_Password.getText().toString());

//        Async_GetClients jsonTask=new Async_GetClients();
//        jsonTask.execute();

//            } else {
//                SinginBTN.setEnabled(true);
//                SinginBTN.setTextColor(getResources().getColor(R.color.white));
//                Password.setError("Incorrect Password !");
//            }
//        } else {
//            SinginBTN.setEnabled(true);
//            SinginBTN.setTextColor(getResources().getColor(R.color.white));
//            Email.setError("Incorrect Email !");
//        }
    }


    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slideout_from_left);

        fragmentTransaction.replace(ParentframeLayout.getId(), fragment);
        fragmentTransaction.commit();
    }


    @SuppressLint("StaticFieldLeak")
    public class Async_Login extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            loading = ProgressDialog.show(getContext(),
//                    "Loading ..", "Please wait...", true, true);
            kProgressHUD.show();
            SinginBTN.setEnabled(false);
        }

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... strings) {
            SoapObject request = new SoapObject(SaveSettings.NAMESPACE, SaveSettings.LOGIN_METHOD);

            pi1 = new PropertyInfo();
            pi1.setName("userName");
            pi1.setValue(strings[0]);//get the string that is to be sent to the web service
            pi1.setType(String.class);
            request.addProperty(pi1);

            pi2 = new PropertyInfo();
            pi2.setName("password");
            pi2.setValue(strings[1]);//get the string that is to be sent to the web service
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
                androidHttpTransport.call(SaveSettings.SOAP_ACTION + SaveSettings.LOGIN_METHOD, envp);
                SoapPrimitive resultRequestSOAP = null;
                resultRequestSOAP = (SoapPrimitive) envp.getResponse();
                jsonObject = XML.toJSONObject(String.valueOf(resultRequestSOAP)); //convert xml to json
                System.out.println("********Response : " + resultRequestSOAP.toString());

                if (!jsonObject.get("NewDataSet").toString().isEmpty()) {
//                    jsonObject1 = (JSONObject) jsonObject.get("NewDataSet");
//                    jsonObject2 = (JSONObject) jsonObject1.get("Table");
//                    SaveSettings.AD_USER_ID = jsonObject2.getString("AD_USER_ID");
//                    SaveSettings.AD_ROLE_ID = jsonObject2.getString("AD_ROLE_ID");

                    SaveSettings.AD_USER_ID = "1005342";
                    SaveSettings.AD_ROLE_ID ="1000118";
                    System.out.println("********AD_USER_ID : " + SaveSettings.AD_USER_ID);
                    System.out.println("********AD_ROLE_ID : " + SaveSettings.AD_ROLE_ID);


                    Async_GetClients jsonTask = new Async_GetClients();
                    jsonTask.execute(SaveSettings.AD_ROLE_ID);

                } else {
                    System.out.println("********Response : error ");
                    kProgressHUD.dismiss();
                }

//                jsonObject1 = jsonObject.getJSONObject("NewDataSet");
//                //   jsonObject2 = (JSONObject) jsonObject1.get("NewDataSet");
//
//                //    Object json = new JSONTokener(jsonObject1.toString()).nextValue();
//                JSONObject json = new JSONObject(jsonObject1.toString());
//
//                JSONObject dataObject = json.optJSONObject("Table");
//                if (dataObject instanceof JSONObject) {
//                    // System.out.println("********JSONArray : " + resultRequestSOAP.toString());
//                    jsonObject2 = jsonObject1.getJSONObject("Table");
//                    SaveSettings.AD_USER_ID = jsonObject2.getString("AD_USER_ID");
//                    SaveSettings.AD_ROLE_ID = jsonObject2.getString("AD_ROLE_ID");
//                    System.out.println("********AD_USER_ID : " + SaveSettings.AD_USER_ID);
//                    System.out.println("********AD_ROLE_ID : " + SaveSettings.AD_ROLE_ID);
//                }
//                //you have an object
//                else {
//
//                    JSONArray arr = jsonObject1.getJSONArray("Table");
//                    for (int i = 0; i < arr.length(); i++) {
//
//                        //  JSONObject mJsonObjectProperty = mJsonArrayProperty.getJSONObject(i);
//                        JSONObject obj = arr.getJSONObject(i);
//                        SaveSettings.AD_USER_ID = obj.getString("AD_USER_ID");
//                        SaveSettings.AD_ROLE_ID = obj.getString("AD_ROLE_ID");
//                        System.out.println("********AD_USER_ID : " + SaveSettings.AD_USER_ID);
//                        System.out.println("********AD_ROLE_ID : " + SaveSettings.AD_ROLE_ID);
//
//                    }
//                }
//                    Async_GetClients jsonTask = new Async_GetClients();
//                    jsonTask.execute(SaveSettings.AD_ROLE_ID);


            } catch (JSONException e) {
//                SinginBTN.setEnabled(true);
//                kProgressHUD.dismiss();
                Log.e("WS Error->", e.toString());
            } catch (IOException | XmlPullParserException soapFault) {
                soapFault.printStackTrace();
//                SinginBTN.setEnabled(true);
                kProgressHUD.dismiss();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            SinginBTN.setEnabled(true);
            SaveSettings saveSettings = new SaveSettings(getContext());
            saveSettings.SaveData();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class Async_GetClients extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            loading = ProgressDialog.show(getContext(),
//                    "Loading ..", "Please wait...", true, true);
        }

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... strings) {
            SoapObject request = new SoapObject(SaveSettings.NAMESPACE, SaveSettings.GETCLIENTS_METHOD);

            pi1 = new PropertyInfo();
            pi1.setName("AD_Role_ID");
            pi1.setValue(strings[0]);//get the string that is to be sent to the web service
            pi1.setType(Integer.class);
            request.addProperty(pi1);
            pi2 = new PropertyInfo();
            pi2.setName("accessKey");
            pi2.setValue(SaveSettings.ACCESS_KEY);//get the string that is to be sent to the web service
            pi2.setType(String.class);
            request.addProperty(pi2);


            SoapSerializationEnvelope envp = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envp.dotNet = true;
            envp.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(SaveSettings.URL);
            try {

                androidHttpTransport.call(SaveSettings.SOAP_ACTION + SaveSettings.GETCLIENTS_METHOD, envp);

                SoapObject obj = (SoapObject) envp.getResponse();
                if (!obj.toString().isEmpty()) {
                    SoapObject obj1 = (SoapObject) envp.getResponse();
                    SoapObject obj2 = (SoapObject) obj1.getProperty(0);
                    SoapObject obj3 = (SoapObject) obj2.getProperty(0);
                    SaveSettings.AD_Client_ID = obj3.getProperty("Key").toString();
                    System.out.println("********AD_Client_ID  : " + SaveSettings.AD_Client_ID);
                    Async_GetOrgs jsonTask = new Async_GetOrgs();
                    jsonTask.execute(SaveSettings.AD_ROLE_ID, SaveSettings.AD_USER_ID, SaveSettings.AD_Client_ID);

                } else {
                    System.out.println("AD_Client_ID error ");
                }
            } catch (IOException | XmlPullParserException soapFault) {
                soapFault.printStackTrace();
                SinginBTN.setEnabled(true);
                kProgressHUD.dismiss();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            loading.dismiss();
//            SinginBTN.setEnabled(true);

        }
    }


    @SuppressLint("StaticFieldLeak")
    public class Async_GetOrgs extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            loading = ProgressDialog.show(getContext(),
//                    "Loading ..", "Please wait...", true, true);
        }

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... strings) {
            SoapObject request = new SoapObject(SaveSettings.NAMESPACE, SaveSettings.GETORG_METHOD);

            pi1 = new PropertyInfo();
            pi1.setName("AD_Role_ID");
            pi1.setValue(strings[0]);//get the string that is to be sent to the web service
            pi1.setType(Integer.class);
            request.addProperty(pi1);

            pi2 = new PropertyInfo();
            pi2.setName("AD_User_ID");
            pi2.setValue(strings[1]);//get the string that is to be sent to the web service
            pi2.setType(Integer.class);
            request.addProperty(pi2);

            pi3 = new PropertyInfo();
            pi3.setName("AD_Client_ID");
            pi3.setValue(strings[2]);//get the string that is to be sent to the web service
            pi3.setType(Integer.class);
            request.addProperty(pi3);

            pi4 = new PropertyInfo();
            pi4.setName("accessKey");
            pi4.setValue(SaveSettings.ACCESS_KEY);//get the string that is to be sent to the web service
            pi4.setType(String.class);
            request.addProperty(pi4);


            SoapSerializationEnvelope envp = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envp.dotNet = true;
            envp.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(SaveSettings.URL);
            try {

                androidHttpTransport.call(SaveSettings.SOAP_ACTION + SaveSettings.GETORG_METHOD, envp);

//                SoapObject obj1 = (SoapObject) envp.getResponse();
//                System.out.println("********Response3 : " + obj1.toString());
//
//                SoapObject obj2 = (SoapObject) obj1.getProperty(0);
//
//                SoapObject obj3 = (SoapObject) obj2.getProperty(0);
//                SaveSettings.AD_ORG_ID = obj3.getProperty("Key").toString();
//                // SaveSettings.AD_ORG_ID = "0";
//                System.out.println("********AD_ORG_ID : " + SaveSettings.AD_ORG_ID);

                Async_InitSession jsonTask = new Async_InitSession();
                jsonTask.execute(SaveSettings.AD_Client_ID, SaveSettings.AD_ORG_ID, SaveSettings.AD_USER_ID, SaveSettings.AD_ROLE_ID);


            } catch (IOException | XmlPullParserException soapFault) {
                soapFault.printStackTrace();
                SinginBTN.setEnabled(true);
                kProgressHUD.dismiss();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            loading.dismiss();
//            SinginBTN.setEnabled(true);

        }
    }

    @SuppressLint("StaticFieldLeak")
    public class Async_InitSession extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            loading = ProgressDialog.show(getContext(),
//                    "Loading ..", "Please wait...", true, true);
        }

        @Override
        protected String doInBackground(String... strings) {
            SoapObject request = new SoapObject(SaveSettings.NAMESPACE, SaveSettings.INTSESSION_METHOD);

            pi4 = new PropertyInfo();
            pi4.setName("AD_Role_ID");
            pi4.setValue(strings[3]);//get the string that is to be sent to the web service
            pi4.setType(Integer.class);
            request.addProperty(pi4);

            pi3 = new PropertyInfo();
            pi3.setName("AD_User_ID");
            pi3.setValue(strings[2]);//get the string that is to be sent to the web service
            pi3.setType(Integer.class);
            request.addProperty(pi3);

            pi2 = new PropertyInfo();
            pi2.setName("AD_Org_ID");
            pi2.setValue(strings[1]);//get the string that is to be sent to the web service
            pi2.setType(Integer.class);
            request.addProperty(pi2);

            pi1 = new PropertyInfo();
            pi1.setName("AD_Client_ID");
            pi1.setValue(strings[0]);//get the string that is to be sent to the web service
            pi1.setType(Integer.class);
            request.addProperty(pi1);

            pi5 = new PropertyInfo();
            pi5.setName("accessKey");
            pi5.setValue(SaveSettings.ACCESS_KEY);//get the string that is to be sent to the web service
            pi5.setType(String.class);
            request.addProperty(pi5);

            SaveSettings.getIP(context);
            pi6 = new PropertyInfo();
            pi6.setName("requestAddr");
            pi6.setValue("SaveSettings.getIP(context)");//get the string that is to be sent to the web service
            pi6.setType(String.class);
            request.addProperty(pi6);

            SoapSerializationEnvelope envp = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envp.dotNet = true;
            envp.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(SaveSettings.URL);
            try {

                androidHttpTransport.call(SaveSettings.SOAP_ACTION + SaveSettings.INTSESSION_METHOD, envp);

                SoapPrimitive resultRequestSOAP = null;
                resultRequestSOAP = (SoapPrimitive) envp.getResponse();
                // jsonObject = XML.toJSONObject(String.valueOf(resultRequestSOAP)); //convert xml to json

                SaveSettings.AD_SESSION_ID = resultRequestSOAP.toString();
                System.out.println("********AD_SESSION_ID : " + resultRequestSOAP.toString());


            } catch (IOException | XmlPullParserException soapFault) {
                soapFault.printStackTrace();
                SinginBTN.setEnabled(true);
                kProgressHUD.dismiss();
            }
            return SaveSettings.AD_SESSION_ID;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (!s.equals("")){
                setFragment(new MainFragment());
                kProgressHUD.dismiss();
            }

//            Async_GetC_BPartner c_bPartner = new Async_GetC_BPartner();
//            c_bPartner.execute(C_BPartner_query);

        }
    }


    public class Async_GetC_BPartner extends AsyncTask<String, String, String> {

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
            pi2.setValue(SaveSettings.AD_SESSION_ID);//get the string that is to be sent to the web service
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
                    C_BPartner_ID = jsonObject2.getString("C_BPARTNER_ID");
                    SaveSettings.UserName = jsonObject2.getString("NAME");
                    SaveSettings.UserEmail = jsonObject2.getString("EMAIL");
                    String Picture = jsonObject2.getString("PIC");
                    SaveSettings.UserPhoto = SaveSettings.ServerImageURL + Picture + ".jpg";
                    SaveSettings.MembersinceDate = jsonObject2.getString("IEU01_MEMBERSINCE").replace("T00:00:00+03:00", "");
                    SaveSettings.ValidFrom = jsonObject2.getString("IEU01_MEMVLDFRM").replace("T00:00:00+03:00", "");
                    SaveSettings.ValidTO = jsonObject2.getString("IEU01_MEMVLDTO").replace("T00:00:00+03:00", "");
                    SaveSettings.CurrentLevel = jsonObject2.getString("IEU01_CURRLVL");
                    SaveSettings.Salesoffice_ID = jsonObject2.getString("C_SALESOFFICE_ID");
                    SaveSettings.MemberID = jsonObject2.getString("IEU01_MEMBERID");
                    SaveSettings.Phone = jsonObject2.getString("MOBILE");
                    System.out.println("C_BPartner_ID : " + C_BPartner_ID);
                    SaveSettings.C_BPartner_ID = C_BPartner_ID;
                    SaveSettings saveSettings = new SaveSettings(getContext());
                    saveSettings.SaveData();
                }
                //you have an object
                else {

                    JSONArray arr = jsonObject1.getJSONArray("Table");
                    for (int i = 0; i < arr.length(); i++) {

                        //  JSONObject mJsonObjectProperty = mJsonArrayProperty.getJSONObject(i);
                        JSONObject obj = arr.getJSONObject(i);
                        String C_BPartner_ID = obj.getString("C_BPARTNER_ID");
                        SaveSettings.UserName = obj.getString("NAME");
                        SaveSettings.UserEmail = obj.getString("EMAIL");

                        SaveSettings.MembersinceDate = obj.getString("IEU01_MEMBERSINCE").replace("T00:00:00+03:00", "");


                        SaveSettings.ValidFrom = obj.getString("IEU01_MEMVLDFRM").replace("T00:00:00+03:00", "");
                        SaveSettings.ValidTO = obj.getString("IEU01_MEMVLDTO").replace("T00:00:00+03:00", "");
                        SaveSettings.CurrentLevel = obj.getString("IEU01_CURRLVL");
                        SaveSettings.Salesoffice_ID = obj.getString("C_SALESOFFICE_ID");
                        SaveSettings.MemberID = obj.getString("IEU01_MEMBERID");
                        SaveSettings.Phone = obj.getString("MOBILE");
                        String Picture = obj.getString("PIC");
                        SaveSettings.UserPhoto = "http://185.104.157.19//Images/" + Picture + ".jpg";

                        System.out.println("C_BPartner_ID : " + C_BPartner_ID);
                        SaveSettings.C_BPartner_ID = C_BPartner_ID;
                        SaveSettings saveSettings = new SaveSettings(getContext());
                        saveSettings.SaveData();
                    }
                }


            } catch (JSONException e) {
                Log.e("WS Error->", e.toString());
            } catch (IOException | XmlPullParserException soapFault) {
                soapFault.printStackTrace();
                SinginBTN.setEnabled(true);
                kProgressHUD.dismiss();
            }
            return SaveSettings.C_BPartner_ID;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //        loading.dismiss();
            //        SinginBTN.setEnabled(true);

            Async_GetBranches async_getBranches = new Async_GetBranches();
            async_getBranches.execute(Branches_query);
            SinginBTN.setEnabled(true);
            kProgressHUD.dismiss();
            if (!s.isEmpty()) {

                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                requireActivity().finish();
            }

        }
    }

    public class Async_GetBranches extends AsyncTask<String, String, String> {

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
            pi2.setValue(SaveSettings.AD_SESSION_ID);//get the string that is to be sent to the web service
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
                    String SALESOFFICE_ID = jsonObject2.getString("C_SALESOFFICE_ID");
                    String SALESOFFICE_NAME = jsonObject2.getString("NAME");
                    if (SALESOFFICE_ID.equals(SaveSettings.Salesoffice_ID)) {
                        SaveSettings.Salesoffice_Name = SALESOFFICE_NAME;
                    }

//                    Salesoffice.add(new Module(SALESOFFICE_ID, SALESOFFICE_NAME));
                    System.out.println("********JSONArray : " + SALESOFFICE_ID + "  " + SALESOFFICE_NAME);
                }
                //you have an objectC_SALESOFFICE_ID
                else {

                    JSONArray arr = jsonObject1.getJSONArray("Table");
                    for (int i = 0; i < arr.length(); i++) {

                        //  JSONObject mJsonObjectProperty = mJsonArrayProperty.getJSONObject(i);
                        JSONObject obj = arr.getJSONObject(i);
                        String SALESOFFICE_ID = obj.getString("C_SALESOFFICE_ID");
                        String SALESOFFICE_NAME = obj.getString("NAME");

                        if (SALESOFFICE_ID.equals(SaveSettings.Salesoffice_ID)) {
                            SaveSettings.Salesoffice_Name = SALESOFFICE_NAME;
                        }

                        //    Salesoffice.add(new Module(SALESOFFICE_ID, SALESOFFICE_NAME));
                        System.out.println("********JSONObject : " + SALESOFFICE_ID + "  " + SALESOFFICE_NAME);

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
            SaveSettings saveSettings = new SaveSettings((Activity) getContext());
            saveSettings.SaveData();
        }
    }
}
