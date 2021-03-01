package com.asits.asitserp;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;

public class SaveSettings {

    private Context context;
    private static SharedPreferences sharedpreferences;
    private static final String MyPREFERENCES = "MyPrefs3";
    public static String AD_USER_ID = "1005342";
    public static String AD_ROLE_ID = "1000118";
    public static String AD_Client_ID = "1000005";
    public static String AD_ORG_ID = "1000008";
    public static String AD_SESSION_ID = "1001102";
    public static String Static_AD_Client_ID = "1000005";

    public static String Static_AD_SESSION_ID = "1537476";
    public static String C_BPartner_ID = "";
    public static String UserName = "";
    public static String Phone = "";

    public static String UserPhoto = "";
    public static String UserEmail = "";
    public static String MembersinceDate = "", ValidFrom = "", ValidTO = "", CurrentLevel = "", Salesoffice_ID, Salesoffice_Name, MemberID;

    public static final String ACCESS_KEY = "ed1931db1254c71306fd0efb12f586ba";
    public static String DMS_ACCESS_KEY = "";
    public static String DMS_TOKEN = "";
    public static String ServerImageURL = "http://185.104.157.19//Images/";

    public static final String NAMESPACE = "http://tempuri.org/";
    public static final String URL = "http://185.104.157.19:6062/service.asmx?WSDL";
    public static final String PostURL = "http://185.104.157.19:6062/service.asmx";

    public static final String URL_DMS = "http://185.104.157.19:6063/VADMSSvc/DmsWebService.asmx?WSDL";
    public static final String URL_DMS_POST = "http://185.104.157.19:6063/VADMSSvc/DmsWebService.asmx";

    public static final String SOAP_ACTION = "http://tempuri.org/";
    public static final String LOGIN_METHOD = "Login";
    public static final String GETCLIENTS_METHOD = "GetClients";
    public static final String GETORG_METHOD = "GetOrgs";
    public static final String INTSESSION_METHOD = "InitSession";
    public static final String GETRECORD_METHOD = "GetRecord";
    public static final String RUNPROCESS_METHOD = "RunProcess";
    public static final String UPDATEIMAGE_METHOD = "UpdateImage";
    public static final String INSERTRECORDWITHPID_METHOD = "InsertRecordWithPid";
    public static final String INSERTRECORD_METHOD = "InsertRecord";

    public static final String LOGOUT_METHOD = "LogOut";
    //DMS Method
    public static final String GETSECURACCESSKEY_METHOD = "GetSecureAccessKey";
    public static final String GETDMSTOKEN_METHOD = "GetDMSToken";
    public static final String UploadDocumentWithMetaData_METHOD = "UploadDocumentWithMetaData";


    public static String getIP(SignInFragment context) {

//        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//        WifiManager wm = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
//
//        WifiInfo connectionInfo = wm.getConnectionInfo();
//        int ipAddress = connectionInfo.getIpAddress();
//        String ipString = Formatter.formatIpAddress(ipAddress);
        return "ipString";
    }

    public SaveSettings(Context context) {
        this.context = context;
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

    }

    public void SaveData() {

        try {

            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("AD_USER_ID", String.valueOf(AD_USER_ID));
            editor.putString("AD_ROLE_ID", String.valueOf(AD_ROLE_ID));
            editor.putString("AD_SESSION_ID", String.valueOf(AD_SESSION_ID));
            editor.putString("AD_Client_ID", String.valueOf(AD_Client_ID));
            editor.putString("UserName", String.valueOf(UserName));
            editor.putString("UserPhoto", String.valueOf(UserPhoto));
            editor.putString("Phone", String.valueOf(Phone));

            editor.putString("UserEmail", String.valueOf(UserEmail));
            editor.putString("C_BPartner_ID", String.valueOf(C_BPartner_ID));
            editor.putString("MembersinceDate", String.valueOf(MembersinceDate));
            editor.putString("ValidFrom", String.valueOf(ValidFrom));
            editor.putString("ValidTO", String.valueOf(ValidTO));
            editor.putString("CurrentLevel", String.valueOf(CurrentLevel));
            editor.putString("Salesoffice_ID", String.valueOf(Salesoffice_ID));
            editor.putString("Salesoffice_Name", String.valueOf(Salesoffice_Name));
            editor.putString("MemberID", String.valueOf(MemberID));

            editor.apply();
            LoadData();
        } catch (Exception ignored) {
        }
    }

    public void LoadData() {

        String TempAD_USER_ID = sharedpreferences.getString("AD_USER_ID", "");
        String Temp_AD_ROLE_ID = sharedpreferences.getString("AD_ROLE_ID", "");
        String Temp_AD_Client_ID = sharedpreferences.getString("AD_Client_ID", "");
        String TempUserName = sharedpreferences.getString("UserName", "");
        String TempUserPhoto = sharedpreferences.getString("UserPhoto", "");
        String TempPhone = sharedpreferences.getString("Phone", "");

        String TempUserEmail = sharedpreferences.getString("UserEmail", "");
        String TempC_BPartner_ID = sharedpreferences.getString("C_BPartner_ID", "");
        String TempAD_SESSION_ID = sharedpreferences.getString("AD_SESSION_ID", "");

        String TempMembersinceDate = sharedpreferences.getString("MembersinceDate", "");
        String TempValidFrom = sharedpreferences.getString("ValidFrom", "");
        String TempValidTO = sharedpreferences.getString("ValidTO", "");
        String TempCurrentLevel = sharedpreferences.getString("CurrentLevel", "");
        String TempSalesoffice_ID = sharedpreferences.getString("Salesoffice_ID", "");
        String TempSalesoffice_Name = sharedpreferences.getString("Salesoffice_Name", "");
        String TempMemberID = sharedpreferences.getString("MemberID", "");


        //    if (!Objects.equals(TempUserName, "empty") && !Objects.equals(TempUserPhoto, "empty") && !Objects.equals(TempUserEmail, "empty")) {
        MembersinceDate = TempMembersinceDate;
        ValidFrom = TempValidFrom;
        ValidTO = TempValidTO;
        CurrentLevel = TempCurrentLevel;
        Salesoffice_ID = TempSalesoffice_ID;
        Salesoffice_Name = TempSalesoffice_Name;
        MemberID = TempMemberID;
        AD_SESSION_ID = TempAD_SESSION_ID;
        UserEmail = TempUserEmail;
        UserPhoto = TempUserPhoto;
        Phone = TempPhone;
        UserName = TempUserName;
        AD_Client_ID = Temp_AD_Client_ID;
        //   }

        //    if (!Objects.equals(Temp_AD_ROLE_ID, "empty")) {
        AD_ROLE_ID = Temp_AD_ROLE_ID;
        //     } else {
//            Intent intent=new Intent(context,AD_ROLE_ID.class);
//            context.startActivity(intent);
        //      }

        //       if (!Objects.equals(TempAD_USER_ID, "empty")) {
        AD_USER_ID = TempAD_USER_ID;
        C_BPartner_ID = TempC_BPartner_ID;
//        } else {
//            Intent intent = new Intent(context, MainActivity.class);
//            context.startActivity(intent);
//        }

    }

    public void Clear(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        MembersinceDate = "";
        ValidFrom = "";
        ValidTO = "";
        CurrentLevel = "";
        Salesoffice_ID = "";
        Salesoffice_Name = "";
        MemberID = "";
        AD_SESSION_ID = "";
        UserEmail = "";
        UserPhoto = "";
        UserName = "";
        Phone = "";
        AD_Client_ID = "";
        AD_ROLE_ID = "";
        AD_USER_ID = "";
        C_BPartner_ID = "";
        deleteCache(context);
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }
}
