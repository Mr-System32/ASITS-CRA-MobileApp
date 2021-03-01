package com.asits.asitserp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

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
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Scanner extends AppCompatActivity {
    CodeScanner codeScanner;
    CodeScannerView scannView;
    TextView resultData;
    String encryptionDecryptionKey = "0123456789abcdef";
    String ivs = "0123456789abcdef";
    private PropertyInfo pi1;
    private PropertyInfo pi2;
    private PropertyInfo pi3;
    private PropertyInfo pi4;
    private PropertyInfo pi5;
    private PropertyInfo pi6;
    KProgressHUD kProgressHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_r_);
        kProgressHUD = new KProgressHUD(getApplicationContext())
                .create(getApplicationContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("يرجى الأنتظار")
                .setDetailsLabel("")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        scannView = findViewById(R.id.scanner_view);
        codeScanner = new CodeScanner(this, scannView);
        resultData = findViewById(R.id.resultsOfQr);

        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String decryptedData="";
                         decryptedData = decrypt(result.getText().getBytes(), encryptionDecryptionKey.getBytes(), ivs.getBytes());
                        System.out.println(decryptedData);
                        //   resultData.setText(decryptedData);
                        List<String> elephantList = Arrays.asList(decryptedData.split(","));
                        MainFragment.AppNum = elephantList.get(0);
                        finish();

//                        String C_BPartner_query = "Select  AD.EMAIL FROM AD_USER AD    INNER join C_BPartner BP on(AD.C_BPartner_ID = BP.C_BPartner_ID) where BP.IEU01_MemberID="+ elephantList.get(1);
//                        System.out.println("C_BPartner_query : " + C_BPartner_query);

//                        String a="SELECT EMAIL ,IEU01_MemberID " +
//                                "FROM C_BPartner\n" +
//                                "INNER JOIN AD_USER\n" +
//                                "ON C_BPartner.C_BPartner_ID = AD_USER.C_BPartner_ID " +
//                                " Where IEU01_MemberID="+ elephantList.get(1);

//                        Async_GetC_BPartner async_getC_bPartner = new Async_GetC_BPartner();
//                        async_getC_bPartner.execute(C_BPartner_query);

                    }
                });

            }
        });


        scannView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeScanner.startPreview();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestForCamera();

    }

    public void requestForCamera() {
        Dexter.withActivity(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                codeScanner.startPreview();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                Toast.makeText(getApplicationContext(), "Camera Permission is Required.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();

            }
        }).check();
    }


    public static String decrypt(byte[] data, byte[] key, byte[] ivs) {
        try {
//            Cipher cipher;
//            cipher = Cipher.getInstance("AES/CBC/NoPadding");
//            cipher.init(Cipher.DECRYPT_MODE, seckey, ivspec);
//            decrypted = cipher.doFinal(Base64.decode(ciphertext, Base64.DEFAULT)); //byte[]
//            String result = new String(decrypted);
//            Log.i("Decrypted Data",result);


            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
            byte[] finalIvs = new byte[16];
            int len = ivs.length > 16 ? 16 : ivs.length;
            System.arraycopy(ivs, 0, finalIvs, 0, len);
            IvParameterSpec ivps = new IvParameterSpec(finalIvs);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivps);

            byte[] clearText = cipher.doFinal(Base64.decode(data, Base64.DEFAULT));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                return new String(clearText, StandardCharsets.UTF_8);
            }

            // return new String(cipher.doFinal(data) );
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public static byte[] encrypt(byte[] data, byte[] key, byte[] ivs) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
            byte[] finalIvs = new byte[16];
            int len = ivs.length > 16 ? 16 : ivs.length;
            System.arraycopy(ivs, 0, finalIvs, 0, len);
            IvParameterSpec ivps = new IvParameterSpec(finalIvs);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivps);
            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
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
            pi2.setValue(SaveSettings.Static_AD_SESSION_ID);//get the string that is to be sent to the web service
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

                JSONObject json = new JSONObject(jsonObject1.toString());

                JSONObject dataObject = json.optJSONObject("Table");
                if (dataObject instanceof JSONObject) {
                    jsonObject2 = jsonObject1.getJSONObject("Table");
//                    C_BPartner_ID = jsonObject2.getString("C_BPARTNER_ID");
//                    SaveSettings.UserName = jsonObject2.getString("NAME");
                    SaveSettings.UserEmail = jsonObject2.getString("EMAIL");
//                    String Picture = jsonObject2.getString("PIC");
//                    SaveSettings.UserPhoto = SaveSettings.ServerImageURL + Picture + ".jpg";
//                    SaveSettings.MembersinceDate = jsonObject2.getString("IEU01_MEMBERSINCE").replace("T00:00:00+03:00", "");
//                    SaveSettings.ValidFrom = jsonObject2.getString("IEU01_MEMVLDFRM").replace("T00:00:00+03:00", "");
//                    SaveSettings.ValidTO = jsonObject2.getString("IEU01_MEMVLDTO").replace("T00:00:00+03:00", "");
//                    SaveSettings.CurrentLevel = jsonObject2.getString("IEU01_CURRLVL");
//                    SaveSettings.Salesoffice_ID = jsonObject2.getString("C_SALESOFFICE_ID");
//                    SaveSettings.MemberID = jsonObject2.getString("IEU01_MEMBERID");
//
//                    System.out.println("C_BPartner_ID : " + C_BPartner_ID);
//                    SaveSettings.C_BPartner_ID = C_BPartner_ID;
                    SaveSettings saveSettings = new SaveSettings(getApplicationContext());
                    saveSettings.SaveData();
                }
                //you have an object
                else {

                    JSONArray arr = jsonObject1.getJSONArray("Table");
                    for (int i = 0; i < arr.length(); i++) {

                        //  JSONObject mJsonObjectProperty = mJsonArrayProperty.getJSONObject(i);
                        JSONObject obj = arr.getJSONObject(i);
//                        String C_BPartner_ID = obj.getString("C_BPARTNER_ID");
//                        SaveSettings.UserName = obj.getString("NAME");
                        SaveSettings.UserEmail = obj.getString("EMAIL");

//                        SaveSettings.MembersinceDate = obj.getString("IEU01_MEMBERSINCE").replace("T00:00:00+03:00", "");
//
//
//                        SaveSettings.ValidFrom = obj.getString("IEU01_MEMVLDFRM").replace("T00:00:00+03:00", "");
//                        SaveSettings.ValidTO = obj.getString("IEU01_MEMVLDTO").replace("T00:00:00+03:00", "");
//                        SaveSettings.CurrentLevel = obj.getString("IEU01_CURRLVL");
//                        SaveSettings.Salesoffice_ID = obj.getString("C_SALESOFFICE_ID");
//                        SaveSettings.MemberID = obj.getString("IEU01_MEMBERID");
//
//                        String Picture = obj.getString("PIC");
//                        SaveSettings.UserPhoto = "http://185.104.157.19//Images/" + Picture + ".jpg";
//
//                        System.out.println("C_BPartner_ID : " + C_BPartner_ID);
//                        SaveSettings.C_BPartner_ID = C_BPartner_ID;
                        SaveSettings saveSettings = new SaveSettings(getApplicationContext());
                        saveSettings.SaveData();
                    }
                }


            } catch (JSONException e) {
                Log.e("WS Error->", e.toString());
            } catch (IOException | XmlPullParserException soapFault) {
                soapFault.printStackTrace();
                kProgressHUD.dismiss();
            }
            return SaveSettings.C_BPartner_ID;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //        loading.dismiss();
            //        SinginBTN.setEnabled(true);


            kProgressHUD.dismiss();
            finish();

        }
    }


}
