package com.asits.asitserp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.mtextfield.ExtendedEditText;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private FrameLayout ParentframeLayout;
    private PropertyInfo pi1;
    private PropertyInfo pi2;
    private PropertyInfo pi3;
    public static String AppNum = "";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Button BTN_UploadDocument, BTN_Pricing, BTN_AppInfo;
    private ImageView IMG_QR;
    private ImageView IMG__;
    private TextView AppNumber;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String App_ID = "";

    Button BTN_Search;
    static KProgressHUD kProgressHUD;
    public static String N_Name = "", N_Capital = "", N_CoType = "", N_Owner = "", CO_Name = "", CO_ISPRICED = "", CO_Application_ID = "", CO_Activity = "", CO_Status = "", CO_Capital = "", CO_NumOfStock = "", CO_StockPrice = "", CO_Fees = "";
    private ExtendedEditText TXT_N_Name, TXT_N_Capital, TXT_N_CoType, TXT_N_Owner, TXT_APP_NUM, TXT_CO_Name, TXT_CO_Activity, TXT_CO_Status, TXT_CO_Capital, TXT_CO_NumOfStock, TXT_CO_StockPrice, TXT_CO_Fees;
    private LinearLayout Liner_New, Liner_Application;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
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
    public void onResume() {
        super.onResume();
        SetDisplay();
        SetDisplayNew();
        AppNumber.setText(AppNum);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        // This callback will only be called when MyFragment is at least Started.


        kProgressHUD = new KProgressHUD(getContext())
                .create(getContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setDetailsLabel("يرجى الأنتظار ...")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        BTN_Search = view.findViewById(R.id.BTN_Search);
        AppNumber = view.findViewById(R.id.TXT_APP_NUM);
        // AppNumber.setText(AppNum);
        ParentframeLayout = requireActivity().findViewById(R.id.frameLayout);
        IMG_QR = view.findViewById(R.id.IMG_QR_Sca);
        IMG__ = view.findViewById(R.id.IMG__);

        BTN_AppInfo = view.findViewById(R.id.BTN_App_Info);
        BTN_Pricing = view.findViewById(R.id.BTN_Pricing);
        BTN_UploadDocument = view.findViewById(R.id.BTN_Upload_Document);

        //TXT_APP_NUM= view.findViewById(R.id.TXT_APP_NUM);
        TXT_CO_Name = view.findViewById(R.id.TXT_CO_Name);
        TXT_CO_Activity = view.findViewById(R.id.TXT_CO_Activity);
        TXT_CO_Status = view.findViewById(R.id.TXT_CO_Status);
        TXT_CO_Capital = view.findViewById(R.id.TXT_CO_Capital);
        TXT_CO_NumOfStock = view.findViewById(R.id.TXT_CO_NumOfStock);
        TXT_CO_StockPrice = view.findViewById(R.id.TXT_CO_StockPrice);
        TXT_CO_Fees = view.findViewById(R.id.TXT_CO_Fees);

        TXT_N_Name = view.findViewById(R.id.TXT_N_Name);
        TXT_N_Capital = view.findViewById(R.id.TXT_N_Capital);
        TXT_N_CoType = view.findViewById(R.id.TXT_N_CoType);
        TXT_N_Owner = view.findViewById(R.id.TXT_N_Owner);

        Liner_New = view.findViewById(R.id.Liner_New);
        Liner_Application = view.findViewById(R.id.Liner_Application);
        SetEnable();
        SetText();
        SetDisplay();
        SetDisplayNew();
        BTN_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                App_ID = AppNumber.getText().toString();
                ClearText();
                //   String Query_GetApp = "SELECT ASI04_IsPriced,ASI04_Application_ID ,ASI04_Capital,ASI04_CompanyStatus,ASI04_CompanyActivity_ID,ASI04_CompanyMaster_ID,ASI04_NumberOfStock,ASI04_StockPrice,ASI04_Fees  from ASI04_Application Where  Value= "+App_ID;
                String Query_GetApp = "SELECT \n" +
                        "app.ASI04_IsPriced,\n" +
                        "app.ASI04_Application_ID,\n" +
                        "app.VALUE,\n" +
                        "cmp.ASI04_Capital,\n" +
                        "ASI04_CompanyStatus.NAME,\n" +
                        "cma.name,\n" +
                        "cmp.name,\n" +
                        "cmp.ASI04_NumberOfStock,\n" +
                        "cmp.ASI04_StockPrice,\n" +
                        "app.ASI04_Fees\n" +
                        "from ASI04_Application  app\n" +
                        "INNER JOIN ASI04_CompanyMaster cmp\n" +
                        "ON cmp.ASI04_CompanyMaster_ID=app.ASI04_CompanyMaster_ID\n" +
                        "INNER JOIN ASI04_CompanyActivity cma\n" +
                        "ON cma.ASI04_CompanyActivity_ID=cmp.ASI04_CompanyActivity_ID\n" +
                        "INNER JOIN (\n" +
                        "        SELECT Ad_Ref_List.Value,\n" +
                        "    Ad_Ref_List_trl.name\n" +
                        "  FROM Ad_Reference\n" +
                        "  INNER JOIN Ad_Ref_List\n" +
                        "  ON Ad_Reference.Ad_Reference_Id=Ad_Ref_List.Ad_Reference_Id\n" +
                        "  INNER JOIN Ad_Ref_List_Trl\n" +
                        "  ON (Ad_Ref_List.Ad_Ref_List_Id                            =Ad_Ref_List_trl.Ad_Ref_List_Id\n" +
                        "  AND Ad_Ref_List_trl.ad_language                           ='ar_IQ' )\n" +
                        "  WHERE Ad_Reference.Name                                   ='ASI04_CompanyStatus'\n" +
                        "  AND AD_REFERENCE.ISACTIVE                                 ='Y'\n" +
                        "  AND Ad_Ref_List.isactive                                  ='Y'\n" +
                        "  ) ASI04_CompanyStatus\n" +
                        "  ON ASI04_CompanyStatus.VALUE=CMP.ASI04_CompanyStatus\n" +
                        "Where app.VALUE=" + App_ID;
                Async_GetApplicationInfo async_getApplicationInfo = new Async_GetApplicationInfo();
                async_getApplicationInfo.execute(Query_GetApp);


            }
        });
        IMG_QR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Scanner.class);
                startActivity(intent);

            }
        });
        BTN_UploadDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!CO_Application_ID.equals("")) {
                    setFragment(new DocumentAppFragment());
                } else {
                    Toast.makeText(getContext(), "يرجى اختيار معاملة", Toast.LENGTH_LONG).show();
                }


            }
        });
        BTN_Pricing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CO_ISPRICED.equals("N")) {
                    Async_PricingProcess async_pricingProcess = new Async_PricingProcess();
                    async_pricingProcess.execute();
                } else {
                    Toast.makeText(getContext(), "تم ترسيم المعاملة سابقآ", Toast.LENGTH_LONG).show();
                }
            }

        });

        BTN_AppInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new CompanyInfoFragment());
            }
        });


        return view;
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slideout_from_left);

        fragmentTransaction.replace(ParentframeLayout.getId(), fragment);
        fragmentTransaction.commit();
    }

    public class Async_PricingProcess extends AsyncTask<String, String, String> {
        String Pricung = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            kProgressHUD.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... strings) {

            HttpPost httpPost = new HttpPost(SaveSettings.PostURL);
            httpPost.addHeader("Content-Type", "text/xml");

            // prepare POST body
            String body = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                    "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                    "  <soap:Body>\n" +
                    "    <RunProcess xmlns=\"http://tempuri.org/\">\n" +
                    "      <AD_Process_ID>1000429</AD_Process_ID>\n" +
                    "      <Record_ID>" + CO_Application_ID + "</Record_ID>\n" +
                    "      <_session_ID>" + 1001102 + "</_session_ID>\n" +
                    "      <accessKey>" + SaveSettings.ACCESS_KEY + "</accessKey>\n" +
                    "      <Param>\n" +
                    "        <ProcessPara>\n" +
                    "          <Name>ASI04_Application_ID</Name>\n" +
                    "          <Type></Type>\n" +
                    "          <Value>" +   CO_Application_ID   + "</Value>\n" +
                    "        </ProcessPara>\n" +
                    "      </Param>\n" +
                    "    </RunProcess>\n" +
                    "  </soap:Body>\n" +
                    "</soap:Envelope>";

            // set POST body
            HttpEntity entity = null;
            try {
                entity = new StringEntity(body);

                httpPost.setEntity(entity);

                HttpClient httpClient = new DefaultHttpClient();
                // Create a response handler
                HttpResponse httpResponse = httpClient.execute(httpPost);

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(httpResponse.getEntity().getContent());


//                jsonObject = XML.toJSONObject(String.valueOf(br.readLine())); //convert xml to json
                NodeList nodeList = doc.getElementsByTagName("RunProcessResponse");
                for (int itr = 0; itr < nodeList.getLength(); itr++) {
                    Node node = nodeList.item(itr);
                    System.out.println("\nNode Name :" + node.getNodeName());
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) node;
                        System.out.println("New App RunProcessResult: " + eElement.getElementsByTagName("RunProcessResult").item(0).getTextContent());
                        Pricung = eElement.getElementsByTagName("RunProcessResult").item(0).getTextContent();


                    }
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }

            return Pricung;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            kProgressHUD.dismiss();
            System.out.println("SSSSSSSSSs= " + s);
            Toast.makeText(getContext(), "مبلغ الترسيم " + s, Toast.LENGTH_LONG).show();

//            if (isNumeric(s))
//            {
//
//                Async_GetPersnlInfo_ID async_getPersnlInfo_id=new Async_GetPersnlInfo_ID();
//                async_getPersnlInfo_id.execute(QUERY_PersnlInfo_ID , s);
//
//
////                DocumentAppFragment.Async_Insert_Document_Info_Ration async_insert_document_info_ration=new DocumentNewFragment.Async_Insert_Document_Info_Ration();
////                async_insert_document_info_ration.execute();
//
//            } else {
//
//                AestheticDialog.showEmotion((Activity) getContext(), "لم يتم انشاء المعاملة", "يوجد خطأ في عملية انشاء المعاملة يرجى مراجعة النقابة  ", AestheticDialog.ERROR);
//
//            }
//            kProgressHUD.dismiss();


        }
    }


    public class Async_GetApplicationInfo extends AsyncTask<String, String, Boolean> {
        boolean a = true;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//        loading = ProgressDialog.show(getContext(),
//                "Loading ..", "Please wait...", true, true);
            kProgressHUD.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Boolean doInBackground(String... strings) {
            SoapObject request = new SoapObject(SaveSettings.NAMESPACE, SaveSettings.GETRECORD_METHOD);

            pi1 = new PropertyInfo();
            pi1.setName("selectQuery");
            pi1.setValue(strings[0]);//get the string that is to be sent to the web service
            pi1.setType(String.class);
            request.addProperty(pi1);

            pi2 = new PropertyInfo();
            pi2.setName("_session_ID");
            pi2.setValue("1001147");//get the string that is to be sent to the web service
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

                    CO_ISPRICED = jsonObject2.getString("ASI04_ISPRICED");
                    CO_Name = jsonObject2.getString("NAME2");
                    CO_Activity = jsonObject2.getString("NAME1");
                    CO_Status = jsonObject2.getString("NAME");
                    CO_Capital = ConvertTOCurrency(jsonObject2.getString("ASI04_CAPITAL")) + " دينار ";
                    CO_NumOfStock = ConvertTOCurrency(jsonObject2.getString("ASI04_NUMBEROFSTOCK")) + " سهم ";
                    CO_StockPrice = ConvertTOCurrency(jsonObject2.getString("ASI04_STOCKPRICE")) + " دينار ";
                    CO_Fees = ConvertTOCurrency(jsonObject2.getString("ASI04_FEES")) + " دينار ";
                    CO_Application_ID = jsonObject2.getString("ASI04_APPLICATION_ID");
//                    App_ID = CO_Application_ID;

//<ASI04_ISPRICED>Y</ASI04_ISPRICED>
//        <ASI04_APPLICATION_ID>1000018</ASI04_APPLICATION_ID>
//        <VALUE>1000012</VALUE>
//        <ASI04_CAPITAL>1500000</ASI04_CAPITAL>
//        <NAME>نشطة</NAME>
//        <NAME1>اتصالات</NAME1>
//        <NAME2>شركة بلات فورم للانظمة الالكترونية</NAME2>
//        <ASI04_NUMBEROFSTOCK>20000</ASI04_NUMBEROFSTOCK>
//        <ASI04_STOCKPRICE>1</ASI04_STOCKPRICE>
//        <ASI04_FEES>0</ASI04_FEES>
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
                a = false;
            } catch (IOException | XmlPullParserException soapFault) {
                soapFault.printStackTrace();
                a = false;

            }
            return a;
        }

        @Override
        protected void onPostExecute(Boolean s) {
            super.onPostExecute(s);
            kProgressHUD.dismiss();
            if (s) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    MainActivity.App_Type = "App";
                }
                SetText();
                SetDisplay();
            } else {
                String Query_WebApp = "SELECT\n" +
                        "app.VALUE,\n" +
                        "rgs.NAME,\n" +
                        "app.ASI04_Application_ID,\n" +
                        "rgs.ASI04_Capital ,\n" +
                        "app.ASI04_ISPRICED,\n" +
                        "ASI04_MixedOrPrivate.MixedOrPrivate,\n" +
                        "ASI04_TypeOfOwners.TypeOfOwners\n" +
                        "from ASI04_WebRegister rgs\n" +
                        "INNER JOIN ASI04_Application app\n" +
                        "on app.ASI04_WebRegister_ID=rgs.ASI04_WebRegister_ID\n" +
                        "INNER JOIN (\n" +
                        "SELECT Ad_Ref_List.Value,\n" +
                        "Ad_Ref_List_trl.name AS MixedOrPrivate\n" +
                        "FROM Ad_Reference\n" +
                        "INNER JOIN Ad_Ref_List\n" +
                        "ON Ad_Reference.Ad_Reference_Id=Ad_Ref_List.Ad_Reference_Id\n" +
                        "INNER JOIN Ad_Ref_List_Trl\n" +
                        "ON (Ad_Ref_List.Ad_Ref_List_Id =Ad_Ref_List_trl.Ad_Ref_List_Id\n" +
                        "AND Ad_Ref_List_trl.ad_language ='ar_IQ' )\n" +
                        "WHERE Ad_Reference.Name ='ASI04_MixedOrPrivate'\n" +
                        "AND AD_REFERENCE.ISACTIVE ='Y'\n" +
                        "AND Ad_Ref_List.isactive ='Y'\n" +
                        ") ASI04_MixedOrPrivate\n" +
                        "ON ASI04_MixedOrPrivate.VALUE=rgs.ASI04_MixedOrPrivate\n" +
                        "INNER JOIN (\n" +
                        "SELECT Ad_Ref_List.Value,\n" +
                        "Ad_Ref_List_trl.name AS TypeOfOwners\n" +
                        "FROM Ad_Reference\n" +
                        "INNER JOIN Ad_Ref_List\n" +
                        "ON Ad_Reference.Ad_Reference_Id=Ad_Ref_List.Ad_Reference_Id\n" +
                        "INNER JOIN Ad_Ref_List_Trl\n" +
                        "ON (Ad_Ref_List.Ad_Ref_List_Id =Ad_Ref_List_trl.Ad_Ref_List_Id\n" +
                        "AND Ad_Ref_List_trl.ad_language ='ar_IQ' )\n" +
                        "WHERE Ad_Reference.Name ='ASI04_TypeOfOwners'\n" +
                        "AND AD_REFERENCE.ISACTIVE ='Y'\n" +
                        "AND Ad_Ref_List.isactive ='Y'\n" +
                        ") ASI04_TypeOfOwners\n" +
                        "ON ASI04_TypeOfOwners.VALUE=rgs.ASI04_TypeOfOwners"
                        +
                        " Where app.VALUE=" + App_ID;
                Async_GetNewApplicationInfo async_getNewApplicationInfo = new Async_GetNewApplicationInfo();
                async_getNewApplicationInfo.execute(Query_WebApp);
            }


        }
    }


    public class Async_GetNewApplicationInfo extends AsyncTask<String, String, Boolean> {
        boolean a = true;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//        loading = ProgressDialog.show(getContext(),
//                "Loading ..", "Please wait...", true, true);
            kProgressHUD.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected Boolean doInBackground(String... strings) {
            SoapObject request = new SoapObject(SaveSettings.NAMESPACE, SaveSettings.GETRECORD_METHOD);

            pi1 = new PropertyInfo();
            pi1.setName("selectQuery");
            pi1.setValue(strings[0]);//get the string that is to be sent to the web service
            pi1.setType(String.class);
            request.addProperty(pi1);

            pi2 = new PropertyInfo();
            pi2.setName("_session_ID");
            pi2.setValue("1001147");//get the string that is to be sent to the web service
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

                    CO_ISPRICED = jsonObject2.getString("ASI04_ISPRICED");
                    N_Name = jsonObject2.getString("NAME");
                    N_Owner = jsonObject2.getString("TYPEOFOWNERS");
                    N_CoType = jsonObject2.getString("MIXEDORPRIVATE");
                    N_Capital = ConvertTOCurrency(jsonObject2.getString("ASI04_CAPITAL")) + " دينار ";

                    CO_Application_ID = jsonObject2.getString("ASI04_APPLICATION_ID");


//        <NAME>شركة البحر الاسود للمقاولات </NAME>
//        <ASI04_APPLICATION_ID>1000040</ASI04_APPLICATION_ID>
//        <ASI04_CAPITAL>10000000</ASI04_CAPITAL>
//        <ASI04_ISPRICED>Y</ASI04_ISPRICED>
//        <MIXEDORPRIVATE>شركة خاصة</MIXEDORPRIVATE>
//        <TYPEOFOWNERS>عراقيين فقط</TYPEOFOWNERS>
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
                a = false;
            } catch (IOException | XmlPullParserException soapFault) {
                soapFault.printStackTrace();
                a = false;

            }
            return a;
        }

        @Override
        protected void onPostExecute(Boolean s) {
            super.onPostExecute(s);
            kProgressHUD.dismiss();
            if (s) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    MainActivity.App_Type = "NewApp";
                }
                SetText();
                SetDisplayNew();
            } else {
                Toast.makeText(getContext(), "رقم المعاملة خاطئ", Toast.LENGTH_LONG).show();
            }


        }
    }

    private void SetEnable() {
        TXT_CO_Name.setEnabled(false);
        TXT_CO_Activity.setEnabled(false);
        TXT_CO_Status.setEnabled(false);
        TXT_CO_Capital.setEnabled(false);
        TXT_CO_NumOfStock.setEnabled(false);
        TXT_CO_StockPrice.setEnabled(false);
        TXT_CO_Fees.setEnabled(false);

        TXT_N_Name.setEnabled(false);
        TXT_N_Capital.setEnabled(false);
        TXT_N_CoType.setEnabled(false);
        TXT_N_Owner.setEnabled(false);

    }

    private void SetText() {
        AppNumber.setText(App_ID);
        TXT_CO_Name.setText(CO_Name);
        TXT_CO_Activity.setText(CO_Activity);
        TXT_CO_Status.setText(CO_Status);
        TXT_CO_Capital.setText(CO_Capital);
        TXT_CO_NumOfStock.setText(CO_NumOfStock);
        TXT_CO_StockPrice.setText(CO_StockPrice);
        TXT_CO_Fees.setText(CO_Fees);

        TXT_N_Name.setText(N_Name);
        TXT_N_Capital.setText(N_Capital);
        TXT_N_CoType.setText(N_CoType);
        TXT_N_Owner.setText(N_Owner);
    }

    private void SetDisplay() {
//        AppNumber.setVisibility(View.VISIBLE);
//        TXT_CO_Name.setVisibility(View.VISIBLE);
//        TXT_CO_Activity.setVisibility(View.VISIBLE);
//        TXT_CO_Status.setVisibility(View.VISIBLE);
//        TXT_CO_Capital.setVisibility(View.VISIBLE);
//        TXT_CO_NumOfStock.setVisibility(View.VISIBLE);
//        TXT_CO_StockPrice.setVisibility(View.VISIBLE);
//        TXT_CO_Fees.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (MainActivity.App_Type.equals("App")) {
                Liner_New.setVisibility(View.GONE);
                Liner_Application.setVisibility(View.VISIBLE);
                IMG__.setVisibility(View.GONE);
                BTN_UploadDocument.setVisibility(View.VISIBLE);
                BTN_Pricing.setVisibility(View.VISIBLE);
            }
        }

    }

    private void SetDisplayNew() {


//        TXT_N_Name.setVisibility(View.VISIBLE);
//        TXT_N_Capital.setVisibility(View.VISIBLE);
//        TXT_N_CoType.setVisibility(View.VISIBLE);
//        TXT_N_Owner.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (MainActivity.App_Type.equals("NewApp")) {
                Liner_New.setVisibility(View.VISIBLE);
                Liner_Application.setVisibility(View.GONE);
                IMG__.setVisibility(View.GONE);
                BTN_UploadDocument.setVisibility(View.VISIBLE);
                BTN_Pricing.setVisibility(View.VISIBLE);
            }
        }
    }

    private void ClearText() {

        TXT_CO_Name.setText("");
        TXT_CO_Activity.setText("");
        TXT_CO_Status.setText("");
        TXT_CO_Capital.setText("");
        TXT_CO_NumOfStock.setText("");
        TXT_CO_StockPrice.setText("");
        TXT_CO_Fees.setText("");

        TXT_N_Owner.setText("");
        TXT_N_Capital.setText("");
        TXT_N_CoType.setText("");
        TXT_N_Name.setText("");

    }

    String ConvertTOCurrency(String s) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        double myNumber = Double.parseDouble(s);
        s = formatter.format(myNumber);

        return s;
    }

}