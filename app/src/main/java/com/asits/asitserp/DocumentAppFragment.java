package com.asits.asitserp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.kaopiz.kprogresshud.KProgressHUD;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
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

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Random;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * <p>
 * create an instance of this fragment.
 */
public class DocumentAppFragment extends Fragment {
    private FrameLayout ParentframeLayout;
    private static String Document = "";
    private static String DocumentName = "";
    private int DocumentLength = 0;
    static final int random = new Random().nextInt(61) + 20; // [0, 60] + 20 => [20, 80]
    private final int LOAD_Doc_RESULTS = 2;
    private final int LOAD_IMAGE_RESULTS = 1;
    private PropertyInfo pi1;
    private PropertyInfo pi2;
    private PropertyInfo pi3;
    private PropertyInfo pi4;
    private PropertyInfo pi5;
    private PropertyInfo pi6;
    private CardView CardDocument;
    public static TextView PDF_Name;
    ImageView Personal_Image;
    private String Image = "";

    private Bitmap bitmap1;
    static KProgressHUD kProgressHUD;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    String Image_query = "UPDATE C_BPartner  SET Pic = @fileName WHERE C_BPartner_ID=@pk";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Button Next;

    public DocumentAppFragment() {
        // Required empty public constructor
        //_Applications.ChangeThem(2);


    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment
     */
    // TODO: Rename and change types and number of parameters
    public static DocumentAppFragment newInstance(String param1, String param2) {
        DocumentAppFragment fragment = new DocumentAppFragment();
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
        View view = inflater.inflate(R.layout.fragment_document_app, container, false);

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                setFragment(new MainFragment());
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        ParentframeLayout = requireActivity().findViewById(R.id.frameLayout);
        CardDocument = view.findViewById(R.id.CardDocument);
        PDF_Name = view.findViewById(R.id.PDF_App_Name);
        CardDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //    chooseFile(LOAD_Doc_RESULTS);
                //   getFileChooserIntent();

                browseDocuments();
            }
        });
        Personal_Image = view.findViewById(R.id.Personal_Image);
        Personal_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile(LOAD_IMAGE_RESULTS);

                // chooseFile();
            }
        });
        Next = view.findViewById(R.id.Upload);

        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (!Image.equals("")) {
//
//                    Async_UploadDocument async_uploadImage = new Async_UploadDocument();
//                    async_uploadImage.execute(Image, ".png");
//
//
//                }
                if (!Document.equals("")) {
                    Async_UploadDocument async_uploadDocument = new Async_UploadDocument();
                    async_uploadDocument.execute(Document, DocumentName.trim());

                } else {
                    Toast.makeText(getContext(), "يرجى تحديد ملف ", Toast.LENGTH_LONG).show();

                }
            }
        });
        kProgressHUD = new KProgressHUD(getContext())
                .create(getContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("يرجى الأنتظار")

                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);


        Async_GETSECURACCESSKEY async_getsecuraccesskey = new Async_GETSECURACCESSKEY();
        async_getsecuraccesskey.execute();

//        // GetContent creates an ActivityResultLauncher<String> to allow you to pass
//// in the mime type you'd like to allow the user to select
//         ActivityResultLauncher<String> mGetContent = registerForActivityResult(new GetContent(),
//                new ActivityResultCallback<Uri>() {
//                    @Override
//                    public void onActivityResult(Uri uri) {
//                        // Handle the returned Uri
//                    }
//                });

        return view;
    }



    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slideout_from_left);

        fragmentTransaction.replace(ParentframeLayout.getId(), fragment);
        fragmentTransaction.commit();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        System.out.println("requestCode " + requestCode);

        switch (requestCode) {
            case 1: // Do your stuff here...
                Uri filePath = data.getData();
                try {
                    bitmap1 = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), filePath);
                    Personal_Image.setImageBitmap(bitmap1);
                    Image = getStringImage(bitmap1);
                    System.out.println("Image : " + Image);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 2: // Do your other stuff here...

                 try {
                    Uri uri = data.getData();
                    ConvertToString(uri);
                    System.out.println("DocumentName : " + DocumentName);
                    PDF_Name.setText(DocumentName);
                } catch (Exception e) {
                    System.out.println("DocumentError : " + e.getMessage().toString());
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);

    }




    public class Async_GETSECURACCESSKEY extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            kProgressHUD.show();

        }

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... strings) {
            SoapObject request = new SoapObject(SaveSettings.NAMESPACE, SaveSettings.GETSECURACCESSKEY_METHOD);

            pi1 = new PropertyInfo();
            pi1.setName("accessKey");
            pi1.setValue("112");
            pi1.setType(String.class);
            request.addProperty(pi1);

            pi2 = new PropertyInfo();
            pi2.setName("keyType");
            pi2.setValue("U");
            pi2.setType(String.class);
            request.addProperty(pi2);

            SoapSerializationEnvelope envp = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envp.dotNet = true;
            envp.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(SaveSettings.URL_DMS);
            try {
                JSONObject jsonObject = null;

                androidHttpTransport.call(SaveSettings.SOAP_ACTION + SaveSettings.GETSECURACCESSKEY_METHOD, envp);
                SoapPrimitive resultRequestSOAP = null;
                resultRequestSOAP = (SoapPrimitive) envp.getResponse();
                jsonObject = XML.toJSONObject(String.valueOf(resultRequestSOAP)); //convert xml to json
                System.out.println("********SECUR ACCESSKEY : " + resultRequestSOAP.toString());

                SaveSettings.DMS_ACCESS_KEY = resultRequestSOAP.toString();


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

            Async_GetDMSToken async_getDMSToken = new Async_GetDMSToken();
            async_getDMSToken.execute();

        }
    }

    public class Async_GetDMSToken extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... strings) {
            SoapObject request = new SoapObject(SaveSettings.NAMESPACE, SaveSettings.GETDMSTOKEN_METHOD);

            pi1 = new PropertyInfo();
            pi1.setName("userName");
            pi1.setValue("MOT Admin");
            pi1.setType(String.class);
            request.addProperty(pi1);

            pi2 = new PropertyInfo();
            pi2.setName("password");
            pi2.setValue("MOT Admin@123");
            pi2.setType(String.class);
            request.addProperty(pi2);

            pi3 = new PropertyInfo();
            pi3.setName("roleID");
            pi3.setValue("1000118");
            pi3.setType(String.class);
            request.addProperty(pi3);


            pi6 = new PropertyInfo();
            pi6.setName("accessKey");
            pi6.setValue(SaveSettings.DMS_ACCESS_KEY);//get the string that is to be sent to the web service
            pi6.setType(String.class);
            request.addProperty(pi6);

            SoapSerializationEnvelope envp = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envp.dotNet = true;
            envp.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(SaveSettings.URL_DMS);
            try {
                JSONObject jsonObject = null;
                JSONObject jsonObject1 = null;
                JSONObject jsonObject2 = null;
                androidHttpTransport.call(SaveSettings.SOAP_ACTION + SaveSettings.GETDMSTOKEN_METHOD, envp);
                SoapPrimitive resultRequestSOAP = null;
                resultRequestSOAP = (SoapPrimitive) envp.getResponse();
                jsonObject = XML.toJSONObject(String.valueOf(resultRequestSOAP)); //convert xml to json
                System.out.println("********DMS Token : " + resultRequestSOAP.toString());

                SaveSettings.DMS_TOKEN = resultRequestSOAP.toString();


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
            kProgressHUD.dismiss();


        }
    }


    public static class Async_UploadDocument extends AsyncTask<String, String, String[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            kProgressHUD.show();

        }

        @SuppressLint("WrongThread")
        @Override
        protected String[] doInBackground(String... strings) {


            HttpPost httpPost = new HttpPost(SaveSettings.URL_DMS_POST);

            httpPost.addHeader("Content-Type", "text/xml");

            // prepare POST body
            // prepare POST body
            String body = "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                    "  <soap:Body>\n" +
                    "    <UploadDocumentWithMetaData xmlns=\"http://tempuri.org/\">\n" +
                    "      <dmsToken>" + SaveSettings.DMS_TOKEN + "</dmsToken>\n" +
                    "      <folderID>1000006</folderID>\n" +
                    "      <byteArray>" + strings[0] + "</byteArray>\n" +
                    "      <fileName>" + 1000006 + strings[1] + "</fileName>\n" +
                    "      <folderKey>" + random + "</folderKey>\n" +
                    "      <objFileInfo>\n" +
                    "        <AttachedFileInfo>\n" +
                    "          <FileName>" + 1000006 + strings[1] + "</FileName>\n" +
                    "          <FileUID>null</FileUID>\n" +
                    "          <Size>0</Size>\n" +
                    "          <StoredFileName>null</StoredFileName>\n" +
                    "          <FileExtension>null</FileExtension>\n" +
                    "          <FileNameWithoutExtension>null</FileNameWithoutExtension>\n" +
                    "          <FileBytes>" + strings[0] + "</FileBytes>\n" +
                    "          <DocName>null</DocName>\n" +
                    "          <DocComment>null</DocComment>\n" +
                    "          <Keyword>null</Keyword>\n" +
                    "          <MetaComment>null</MetaComment>\n" +
                    "          <FileCreatedDate>" + "2021-01-08" + "</FileCreatedDate>\n" +
                    "          <Description>null</Description>\n" +
                    "          <EmailUID>null</EmailUID>\n" +
                    "          <StartIndex>0</StartIndex>\n" +
                    "          <EndIndex>33498</EndIndex>\n" +
                    "          <Length>33499</Length>\n" +
                    "          <latestModifiedDate>" + "2021-01-08" + "</latestModifiedDate>\n" +
                    "          <folderKey>null</folderKey>\n" +
                    "          <TotalNoOfFileSize>0.0</TotalNoOfFileSize>\n" +
                    "        </AttachedFileInfo>\n" +
                    "      </objFileInfo>\n" +
                    "      <isLastChunk>" + false + "</isLastChunk>\n" +
                    "      <keepVersion>2</keepVersion>\n" +
                    "      <docCategory_ID>0</docCategory_ID>\n" +
                    "      <ad_Window_ID>1000170</ad_Window_ID>\n" +
                    "      <tableID>1000806</tableID>\n" +
                    "      <recordID>" + Integer.parseInt(MainFragment.CO_Application_ID) + "</recordID>\n" +
                    "    </UploadDocumentWithMetaData>\n" +
                    "  </soap:Body>\n" +
                    "</soap:Envelope>";

            // set POST body
            HttpEntity entity = null;
            try {
                entity = new StringEntity(body);

                httpPost.setEntity(entity);

                System.out.println(entity.toString());

                HttpClient httpClient = new DefaultHttpClient();
                // Create a response handler
                HttpResponse httpResponse = httpClient.execute(httpPost);

                InputStreamReader isr = new InputStreamReader(httpResponse.getEntity().getContent());
                BufferedReader br = new BufferedReader(isr);
                System.out.println("vvvvvvvvvvvvvvvvvv " + br.readLine());

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return strings;
        }

        @Override
        protected void onPostExecute(String[] s) {
            super.onPostExecute(s);

            Async_UploadDocument2 async_uploadDocument2 = new Async_UploadDocument2();
            async_uploadDocument2.execute(s[0], s[1]);

        }
    }

    public static class Async_UploadDocument2 extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            Async_GetDMSToken async_getDMSToken = new Async_GetDMSToken();
//            async_getDMSToken.execute();
        }

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... strings) {


            HttpPost httpPost = new HttpPost(SaveSettings.URL_DMS_POST);

            httpPost.addHeader("Content-Type", "text/xml");

            // prepare POST body
            String body = "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                    "  <soap:Body>\n" +
                    "    <UploadDocumentWithMetaData xmlns=\"http://tempuri.org/\">\n" +
                    "      <dmsToken>" + SaveSettings.DMS_TOKEN + "</dmsToken>\n" +
                    "      <folderID>1000006</folderID>\n" +
                    "      <byteArray>" + strings[0] + "</byteArray>\n" +
                    "      <fileName>" + 1000006 + strings[1] + "</fileName>\n" +
                    "      <folderKey>" + random + "</folderKey>\n" +
                    "      <objFileInfo>\n" +
                    "        <AttachedFileInfo>\n" +
                    "          <FileName>" + 1000006 + strings[1] + "</FileName>\n" +
                    "          <FileUID>null</FileUID>\n" +
                    "          <Size>0</Size>\n" +
                    "          <StoredFileName>null</StoredFileName>\n" +
                    "          <FileExtension>null</FileExtension>\n" +
                    "          <FileNameWithoutExtension>null</FileNameWithoutExtension>\n" +
                    "          <FileBytes>" + strings[0] + "</FileBytes>\n" +
                    "          <DocName>null</DocName>\n" +
                    "          <DocComment>null</DocComment>\n" +
                    "          <Keyword>null</Keyword>\n" +
                    "          <MetaComment>null</MetaComment>\n" +
                    "          <FileCreatedDate>" + "2021-01-08" + "</FileCreatedDate>\n" +
                    "          <Description>null</Description>\n" +
                    "          <EmailUID>null</EmailUID>\n" +
                    "          <StartIndex>0</StartIndex>\n" +
                    "          <EndIndex>33498</EndIndex>\n" +
                    "          <Length>33499</Length>\n" +
                    "          <latestModifiedDate>" + "2021-01-08" + "</latestModifiedDate>\n" +
                    "          <folderKey>null</folderKey>\n" +
                    "          <TotalNoOfFileSize>0.0</TotalNoOfFileSize>\n" +
                    "        </AttachedFileInfo>\n" +
                    "      </objFileInfo>\n" +
                    "      <isLastChunk>" + true + "</isLastChunk>\n" +
                    "      <keepVersion>2</keepVersion>\n" +
                    "      <docCategory_ID>0</docCategory_ID>\n" +
                    "      <ad_Window_ID>1000170</ad_Window_ID>\n" +
                    "      <tableID>1000806</tableID>\n" +
                    "      <recordID>" + Integer.parseInt(MainFragment.CO_Application_ID) + "</recordID>\n" +
                    "    </UploadDocumentWithMetaData>\n" +
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

                InputStreamReader isr = new InputStreamReader(httpResponse.getEntity().getContent());
                BufferedReader br = new BufferedReader(isr);
                System.out.println("vvvvvvvvvvvvvvvvvv " + br.readLine());

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            kProgressHUD.dismiss();
            Document = "";
            PDF_Name.setText("Document");

        }
    }

    public class Async_UploadImage extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... strings) {
            SoapObject request = new SoapObject(SaveSettings.NAMESPACE, SaveSettings.UPDATEIMAGE_METHOD);

            pi1 = new PropertyInfo();
            pi1.setName("query");
            pi1.setValue("UPDATE C_BPartner SET Pic = @fileName WHERE C_BPartner_ID=@pk");
            pi1.setType(String.class);
            request.addProperty(pi1);

            pi2 = new PropertyInfo();
            pi2.setName("id");
            pi2.setValue(Integer.parseInt(SaveSettings.C_BPartner_ID));
            pi2.setType(String.class);
            request.addProperty(pi2);

            pi3 = new PropertyInfo();
            pi3.setName("file");
            pi3.setValue(Image);
            pi3.setType(String.class);
            request.addProperty(pi3);
            System.out.println("Image : " + Image + " C_BPartner_ID : " + SaveSettings.C_BPartner_ID);

            pi4 = new PropertyInfo();
            pi4.setName("fileType");
            pi4.setValue(".png");
            pi4.setType(String.class);
            request.addProperty(pi4);

            pi5 = new PropertyInfo();
            pi5.setName("_session_ID");
            int a2 = Integer.parseInt(SaveSettings.Static_AD_SESSION_ID);
            pi5.setValue(a2);//get the string that is to be sent to the web service
            pi5.setType(Integer.class);
            request.addProperty(pi5);

            pi6 = new PropertyInfo();
            pi6.setName("accessKey");
            pi6.setValue(SaveSettings.ACCESS_KEY);//get the string that is to be sent to the web service
            pi6.setType(String.class);
            request.addProperty(pi6);

            SoapSerializationEnvelope envp = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envp.dotNet = true;
            envp.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(SaveSettings.URL);
            try {
                JSONObject jsonObject = null;
                JSONObject jsonObject1 = null;
                JSONObject jsonObject2 = null;
                androidHttpTransport.call(SaveSettings.SOAP_ACTION + SaveSettings.UPDATEIMAGE_METHOD, envp);
                SoapPrimitive resultRequestSOAP = null;
                resultRequestSOAP = (SoapPrimitive) envp.getResponse();
                jsonObject = XML.toJSONObject(String.valueOf(resultRequestSOAP)); //convert xml to json
                System.out.println("********Check : " + resultRequestSOAP.toString());

//                "result":"success"
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


        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //   _Applications.ChangeThem(2);
    }

    private Intent getFileChooserIntent() {
        String[] mimeTypes = {"image/*", "application/pdf"};

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            if (mimeTypes.length > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        } else {
            String mimeTypesStr = "";

            for (String mimeType : mimeTypes) {
                mimeTypesStr += mimeType + "|";
            }

            intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
        }

        return intent;
    }

    private void chooseFile(int req) {
        try {

            if (req == 2) {
                Intent intent = new Intent();
                intent.setType("image/*|application/pdf|audio/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select PDF"), LOAD_Doc_RESULTS);


            } else if (req == 1) {
                Intent intent = new Intent();
                intent.setType("image/jpeg");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "image/jpeg"), LOAD_IMAGE_RESULTS);


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void browseDocuments() {

        String[] mimeTypes =
                {"image/jpeg",
                        "application/pdf",
                };

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(Intent.createChooser(intent, "ChooseFile"), 2);

    }
    private String getFileName(Uri uri) throws IllegalArgumentException {
        // Obtain a cursor with information regarding this uri
        Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);

        if (cursor.getCount() <= 0) {
            cursor.close();
            throw new IllegalArgumentException("Can't obtain file name, cursor is empty");
        }

        cursor.moveToFirst();

        String fileName = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));

        cursor.close();

        return fileName;
    }
    //    private void chooseFile() {
//        try {
//            Intent intent = new Intent();
//            intent.setType("application/pdf");
//            intent.setAction(Intent.ACTION_GET_CONTENT);
//            startActivityForResult(Intent.createChooser(intent, "Select PDF"), LOAD_Doc_RESULTS);
//            try {
//                startActivityForResult(intent, LOAD_IMAGE_RESULTS);
//
//            } catch (ActivityNotFoundException e) {
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//    // Picture Gallery
//    private void ChooesPhoto(int req) {
//        // Create intent to Open Image applications like Gallery, Google Photos
//        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
//                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        // Start the Intent
//        startActivityForResult(galleryIntent, req);
//    }
    public static String getFileName2(Uri uri) {


        String fileName = null;
        String path = uri.getPath();
        String someFilepath = "image.fromyesterday.test.jpg";
     //   String extension = path.substring(path.lastIndexOf(".")); // Extension with dot .jpg, .png
        System.out.println("path " + path);
        int cut = path.lastIndexOf('/');
        if (cut != -1) {
            fileName = path.substring(cut + 1);
        }
        return fileName;
    }

    public static String getCurrentDate() {

        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date today = Calendar.getInstance().getTime();
        return dateFormat.format(today);
    }

    public void ConvertToString(Uri uri) {

        DocumentName = getFileName(uri);
        String uriString = uri.toString();
        Log.d("data", "onActivityResult: uri" + uriString);
        //            myFile = new File(uriString);
        //            ret = myFile.getAbsolutePath();
        //Fpath.setText(ret);
        try {
            InputStream in = requireActivity().getContentResolver().openInputStream(uri);
            byte[] bytes = getBytes(in);
            DocumentLength = bytes.length;
            Log.d("data", "onActivityResult: bytes size=" + bytes.length);
            Log.d("data", "onActivityResult: Base64string=" + Base64.encodeToString(bytes, Base64.DEFAULT));
            String ansValue = Base64.encodeToString(bytes, Base64.DEFAULT);
            Document = Base64.encodeToString(bytes, Base64.DEFAULT);

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Log.d("error", "onActivityResult: " + e.toString());
        }

    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 10, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    public class MimeType {
        public static final String DOC = "application/msword";
        public static final String DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        public static final String XLS = "application/vnd.ms-excel application/x-excel";
        public static final String XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        public static final String PPT = "application/vnd.ms-powerpoint";
        public static final String PPTX = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
        public static final String PDF = "application/pdf";

    }

}