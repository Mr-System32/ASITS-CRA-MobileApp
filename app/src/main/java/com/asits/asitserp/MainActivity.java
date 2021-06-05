package com.asits.asitserp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.inappmessaging.FirebaseInAppMessaging;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.onesignal.OneSignal;
import com.squareup.picasso.Picasso;
import com.thecode.aestheticdialogs.AestheticDialog;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import de.hdodenhof.circleimageview.CircleImageView;


@RequiresApi(api = Build.VERSION_CODES.M)
public class MainActivity extends AppCompatActivity {
    //    public static MeowBottomNavigation bottomNavigation;
    private FrameLayout ParentframeLayout;
    int MENU_ITEM_CLICK = 0;
    // SNavigationDrawer sNavigationDrawer;
    Class fragmentClass;
    static KProgressHUD kProgressHUD;
    public static String App_Type = "";

    public static Fragment fragment;
    private PropertyInfo pi1;
    String session = "";
    private AppBarConfiguration mAppBarConfiguration;
    View contentView;
    ImageView Menu;
    private static final float END_SCALE = 0.7f;
    MeowBottomNavigation bottomNavigation;
    //Menu Item
    LinearLayout Main, Profile, Application, Myapp, Info, Notification, Logout;

    TextView TXT_Main, TXT_Profile, TXT_App, TXT_Myapp, TXT_Info, TXT_Noti, TXT_Logout;
    ImageView IMG_Main, IMG_Profile, IMG_App, IMG_Myapp, IMG_Info, IMG_Noti, IMG_Logout;
    TextView TXT_Name, TXT_Email;
    CircleImageView IMG_Personal;
    private static final String ONESIGNAL_APP_ID = "a45e7aba-2ca3-4aee-9893-6f1843b42a32";

    @Override
    protected void onResume() {
        super.onResume();

//        FirebaseAnalytics.getInstance(this).logEvent("main_screen_opened", null);
//        FirebaseInAppMessaging.getInstance().triggerEvent("main_screen_opened");

//        FirebaseInAppMessaging.getInstance().setAutomaticDataCollectionEnabled(true);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        SaveSettings saveSettings = new SaveSettings(getApplicationContext());
        saveSettings.LoadData();

        kProgressHUD = new KProgressHUD(this)
                .create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("جاري تسجيل الخروج")
                .setDetailsLabel("يرجى الأنتظار ...")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
//
//        FirebaseAnalytics.getInstance(this).logEvent("ashref", null);
//        FirebaseInAppMessaging.getInstance().triggerEvent("ashref");


        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);

//        // MyApp.Notification
//        Intent intentBackgroundService = new Intent(this, Notification.class);
//        startService(intentBackgroundService);

//        NotificationChannel notificationChannel = new NotificationChannel("MyNotification", "MyNotification", NotificationManager.IMPORTANCE_DEFAULT);
//        NotificationManager notificationManager = getSystemService(NotificationManager.class);
//        notificationManager.createNotificationChannel(notificationChannel);

        ParentframeLayout = this.findViewById(R.id.frameLayout);
        //  Toolbar toolbar = findViewById(R.id.toolbar);
        contentView = findViewById(R.id.cordenate);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        // setSupportActionBar(toolbar);
        //  setFragment(new NewsFragment());

        ///Menu Item
        //LinerLayout
        Menu = findViewById(R.id.menu);

        Main = findViewById(R.id.Mainpage);
        Profile = findViewById(R.id.Profile);
        Application = findViewById(R.id.Application);
        Myapp = findViewById(R.id.MyApplication);
        Info = findViewById(R.id.Info);
        Notification = findViewById(R.id.Notofication);
        Logout = findViewById(R.id.Logout);
        //Text
        TXT_Main = findViewById(R.id.TXT_Main);
        TXT_Profile = findViewById(R.id.TXT_Profile);
        TXT_App = findViewById(R.id.TXT_App);
        TXT_Myapp = findViewById(R.id.TXT_Myapp);
        TXT_Info = findViewById(R.id.TXT_Info);
        TXT_Noti = findViewById(R.id.TXT_Noti);
        TXT_Logout = findViewById(R.id.TXT_Logout);
        // IMAGE
        IMG_Main = findViewById(R.id.IMG_Main);
        IMG_Profile = findViewById(R.id.IMG_Profile);
        IMG_App = findViewById(R.id.IMG_App);
        IMG_Myapp = findViewById(R.id.IMG_Myapp);
        IMG_Info = findViewById(R.id.IMG_Info);
        IMG_Noti = findViewById(R.id.IMG_Noti);
        IMG_Logout = findViewById(R.id.IMG_Logout);

        TXT_Name = findViewById(R.id.TXT_Name);

        TXT_Email = findViewById(R.id.TXT_Email);
        //St menu Info
        if (!SaveSettings.C_BPartner_ID.equals("")) {
            IMG_Personal = findViewById(R.id.IMG_persunal);
            Picasso.get()
                    .load(SaveSettings.UserPhoto)
                    .placeholder(R.drawable.ieu_logo)
                    .error(R.drawable.ieu_logo)
                    .into(IMG_Personal);
            TXT_Name.setText(SaveSettings.UserName);
            TXT_Email.setText(SaveSettings.UserEmail);
        }
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();

        drawer.setScrimColor(Color.TRANSPARENT);
        drawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                // Scale the View based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }

        });
        //Menu open and close
        Menu.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                //   DrawerLayout navDrawer = findViewById(R.id.drawer_layout);
                // If the navigation drawer is not open then open it, if its already open then close it.
                if (!drawer.isDrawerOpen(Gravity.START)) drawer.openDrawer(Gravity.START);
                else drawer.closeDrawer(Gravity.END);
            }
        });

        //Menu Item
        Main.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                MenuChangThemes(1);
                 setFragment(new MainFragment());
                drawer.closeDrawers();

            }
        });
        Profile.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                MenuChangThemes(7);
                drawer.closeDrawers();
                if (!SaveSettings.AD_SESSION_ID.equals("") && !SaveSettings.AD_SESSION_ID.equals("empty")) {
//                    Intent intent = new Intent(getApplicationContext(), UserProfile.class);
//                    startActivity(intent);
                    //        setFragment(new SignInFragment());

                } else {
                    Toast.makeText(getApplicationContext(), "يجب تسجيل الدخول اولآ", Toast.LENGTH_LONG).show();
                    setFragment(new SignInFragment());
                }


            }
        });
        Application.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                MenuChangThemes(2);
                drawer.closeDrawers();

                if (!SaveSettings.AD_SESSION_ID.equals("") && !SaveSettings.AD_SESSION_ID.equals("empty")) {
                    //      setFragment(new ApplicationsFragment());
                    setFragment(new SignInFragment());
                } else {
                    Toast.makeText(getApplicationContext(), "يجب تسجيل الدخول اولآ", Toast.LENGTH_LONG).show();
                    setFragment(new SignInFragment());
                }

            }
        });
        Info.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                MenuChangThemes(6);
//                Intent intent = new Intent(getApplicationContext(), Info.class);
//                startActivity(intent);
                drawer.closeDrawers();

            }
        });
        Myapp.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                MenuChangThemes(3);
                drawer.closeDrawers();
                if (!SaveSettings.AD_SESSION_ID.equals("") && !SaveSettings.AD_SESSION_ID.equals("empty")) {
                    //     setFragment(new MyAppFragment());

                } else {
                    Toast.makeText(getApplicationContext(), "يجب تسجيل الدخول اولآ", Toast.LENGTH_LONG).show();
                    setFragment(new SignInFragment());
                }
            }
        });
        Notification.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                MenuChangThemes(4);
                drawer.closeDrawers();

                if (!SaveSettings.AD_SESSION_ID.equals("") && !SaveSettings.AD_SESSION_ID.equals("empty")) {
                    //   setFragment(new NotificationAppFragment());

                } else {
                    Toast.makeText(getApplicationContext(), "يجب تسجيل الدخول اولآ", Toast.LENGTH_LONG).show();
                    setFragment(new SignInFragment());
                }

            }
        });
        Logout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
//                setFragment(new NewsFragment());
                MenuChangThemes(5);
                LOGOUT();

                drawer.closeDrawers();
            }
        });


        //Get Firebase FCM token
//        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {
//            @Override
//            public void onSuccess(InstanceIdResult instanceIdResult) {
//                String token = instanceIdResult.getToken();
//
//                //  Toast.makeText(getApplicationContext(), token, Toast.LENGTH_LONG).show();
//                System.out.println("token : " + token);
//
//            }
//        });


//        bottomNavigation = findViewById(R.id.bottomNavigation);
//        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.home__1_));
//        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.applications_ico));
//        bottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.myappco));
//        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.notification));
//
//
//        bottomNavigation.isShowing(1);
//        bottomNavigation.setCount(3, "5");
//
//        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
//            @Override
//            public void onClickItem(MeowBottomNavigation.Model item) {
////                Toast.makeText(MainActivity.this, "clicked item : " + item.getId(), Toast.LENGTH_SHORT).show();
//
//                switch (item.getId()) {
//
//                    case 1: {
//                        // if (!SaveSettings.C_BPartner_ID.equals("")){
//                        setFragment(new NewsFragment());
////                        }else {
////                            Toast.makeText(getApplicationContext(),"يجب تسجيل الدخول اولآ",Toast.LENGTH_LONG).show();
////                            setFragment(new SignInFragment());
////                        }
//                        MenuChangThemes(1);
//
//                        break;
//                    }
//                    case 2: {
//                        if (!SaveSettings.C_BPartner_ID.equals("") && !SaveSettings.C_BPartner_ID.equals("empty")) {
//                            setFragment(new ApplicationsFragment());
//                        } else {
//                            Toast.makeText(getApplicationContext(), "يجب تسجيل الدخول اولآ", Toast.LENGTH_LONG).show();
//                            setFragment(new  SignInFragment());
//                        }
//                        MenuChangThemes(2);
//
//                        break;
//
//                    }
//                    case 3: {
//                        if (!SaveSettings.C_BPartner_ID.equals("") && !SaveSettings.C_BPartner_ID.equals("empty")) {
//                            setFragment(new BlankFragment());
//                        } else {
//                            Toast.makeText(getApplicationContext(), "يجب تسجيل الدخول اولآ", Toast.LENGTH_LONG).show();
//                            setFragment(new  SignInFragment());
//                        }
//                        MenuChangThemes(4);
//
//                        break;
//                    }
//                    case 4: {
//                        if (!SaveSettings.C_BPartner_ID.equals("") && !SaveSettings.C_BPartner_ID.equals("empty")) {
//                            setFragment(new MyAppFragment());
//                        } else {
//                            Toast.makeText(getApplicationContext(), "يجب تسجيل الدخول اولآ", Toast.LENGTH_LONG).show();
//                            setFragment(new  SignInFragment());
//                        }
//                        MenuChangThemes(3);
//
//                        break;
//                    }
//
//
//                }
//            }
//        });
//        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
//            @Override
//            public void onReselectItem(MeowBottomNavigation.Model item) {
//                switch (item.getId()) {
//
//                    case 1: {
////                        if (!SaveSettings.C_BPartner_ID.equals("")) {
//                        setFragment(new NewsFragment());
////                        } else {
////                            Toast.makeText(getApplicationContext(), "يجب تسجيل الدخول اولآ", Toast.LENGTH_LONG).show();
////                            setFragment(new SignInFragment());
////                        }
//                        MenuChangThemes(1);
//
//                        break;
//                    }
//                    case 2: {
//                        if (!SaveSettings.C_BPartner_ID.equals("") && !SaveSettings.C_BPartner_ID.equals("empty")) {
//                            setFragment(new ApplicationsFragment());
//                        } else {
//                            Toast.makeText(getApplicationContext(), "يجب تسجيل الدخول اولآ", Toast.LENGTH_LONG).show();
//                            setFragment(new  SignInFragment());
//                        }
//                        MenuChangThemes(2);
//
//                        break;
//
//                    }
//                    case 3: {
//                        if (!SaveSettings.C_BPartner_ID.equals("") && !SaveSettings.C_BPartner_ID.equals("empty")) {
//                            setFragment(new BlankFragment());
//                        } else {
//                            Toast.makeText(getApplicationContext(), "يجب تسجيل الدخول اولآ", Toast.LENGTH_LONG).show();
//                            setFragment(new  SignInFragment());
//                        }
//                        MenuChangThemes(4);
//
//                        break;
//                    }
//                    case 4: {
//                        if (!SaveSettings.C_BPartner_ID.equals("") && !SaveSettings.C_BPartner_ID.equals("empty")) {
//                            setFragment(new MyAppFragment());
//                        } else {
//                            Toast.makeText(getApplicationContext(), "يجب تسجيل الدخول اولآ", Toast.LENGTH_LONG).show();
//                            setFragment(new  SignInFragment());
//                        }
//                        MenuChangThemes(3);
//
//                        break;
//                    }
//
//
//                }
//            }
//        });
//        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
//            @Override
//            public void onShowItem(MeowBottomNavigation.Model item) {
//
//            }
//        });
        setFragment(new SignInFragment());
    }

    public void LOGOUT() {
        Async_LOGOUT async_logout = new Async_LOGOUT();
        async_logout.execute();

//        Intent intent= new Intent(this,CreateApplication.class);
//        startActivity(intent);

//        Intent intent = new Intent(getApplicationContext(), _Applications.class);
//        startActivity(intent);

    }

    public void UploadDMS(View view) {

        setFragment(new DocumentAppFragment());
    }

//    public void createApp() {
//
//        Intent intent = new Intent(this, CreateNewApp.class);
//        startActivity(intent);
//    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.Mainpage: {
//                setFragment(new NewsFragment());
//                break;
//            }
//            case R.id.Application: {
//                setFragment(new ApplicationsFragment());
//
//                break;
//            }
//            case R.id.MyApplication: {
//                setFragment(new MyAppFragment());
//                break;
//            }
//            case R.id.Notofication: {
//                break;
//            }
//            case R.id.Info: {
//                Intent intent=new Intent(getApplicationContext(),Info.class);
//                startActivity(intent);
//                break;
//            }
//            case R.id.Logout: {
//                LOGOUT();
//                break;
//            }
//
//        }
//    }

    public class Async_LOGOUT extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            kProgressHUD.show();

//            loading = ProgressDialog.show(getContext(),
//                    "Loading ..", "Please wait...", true, true);
        }

        @Override
        protected String doInBackground(String... strings) {
            SoapObject request = new SoapObject(SaveSettings.NAMESPACE, SaveSettings.LOGOUT_METHOD);


            pi1 = new PropertyInfo();
            pi1.setName("AD_Session_ID");
            pi1.setValue(SaveSettings.AD_SESSION_ID);//get the string that is to be sent to the web service
            pi1.setType(Integer.class);
            request.addProperty(pi1);


            SoapSerializationEnvelope envp = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envp.dotNet = true;
            envp.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(SaveSettings.URL);
            try {

                androidHttpTransport.call(SaveSettings.SOAP_ACTION + SaveSettings.LOGOUT_METHOD, envp);

                SoapPrimitive resultRequestSOAP = null;
                resultRequestSOAP = (SoapPrimitive) envp.getResponse();
                // jsonObject = XML.toJSONObject(String.valueOf(resultRequestSOAP)); //convert xml to json
                System.out.println("********Response5 : " + resultRequestSOAP.toString());

                //      SaveSettings.AD_SESSION_ID =  resultRequestSOAP.toString();
                session = resultRequestSOAP.toString();

            } catch (IOException | XmlPullParserException soapFault) {
                soapFault.printStackTrace();
            }
            return session;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            SaveSettings saveSettings = new SaveSettings(getApplicationContext());
            saveSettings.Clear(getApplicationContext());
            if (session.equals("true")) {

                kProgressHUD.dismiss();
                //   setFragment(new NewsFragment());
            }

        }
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slideout_from_left);

        fragmentTransaction.replace(ParentframeLayout.getId(), fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity2, menu);
        return true;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void MenuChangThemes(int a) {
        switch (a) {
            case 1: {
                TXT_Main.setTextColor(getResources().getColor(R.color.purple_200));
                IMG_Main.setBackground(getResources().getDrawable(R.drawable.home__1_));
                Main.setBackground(getResources().getDrawable(R.drawable.menu_item));
                MenuReChangThemes(MENU_ITEM_CLICK);
                MENU_ITEM_CLICK = 1;
//                bottomNavigation.show(1, true);

                break;
            }
            case 7: {
                TXT_Profile.setTextColor(getResources().getColor(R.color.purple_200));
                IMG_Profile.setBackground(getResources().getDrawable(R.drawable.profile));
                Profile.setBackground(getResources().getDrawable(R.drawable.menu_item));
                MenuReChangThemes(MENU_ITEM_CLICK);
                MENU_ITEM_CLICK = 7;


                break;
            }
            case 2: {
                TXT_App.setTextColor(getResources().getColor(R.color.purple_200));
                IMG_App.setBackground(getResources().getDrawable(R.drawable.test));
                Application.setBackground(getResources().getDrawable(R.drawable.menu_item));
                MenuReChangThemes(MENU_ITEM_CLICK);
                MENU_ITEM_CLICK = 2;
                //    bottomNavigation.show(2, true);

                break;
            }
            case 3: {

                TXT_Myapp.setTextColor(getResources().getColor(R.color.purple_200));
                IMG_Myapp.setBackground(getResources().getDrawable(R.drawable.myappco));
                Myapp.setBackground(getResources().getDrawable(R.drawable.menu_item));
                MenuReChangThemes(MENU_ITEM_CLICK);
                MENU_ITEM_CLICK = 3;
                //     bottomNavigation.show(4, true);

                break;
            }
            case 4: {
                TXT_Noti.setTextColor(getResources().getColor(R.color.purple_200));
                IMG_Noti.setBackground(getResources().getDrawable(R.drawable.notification));
                Notification.setBackground(getResources().getDrawable(R.drawable.menu_item));
                MenuReChangThemes(MENU_ITEM_CLICK);
                MENU_ITEM_CLICK = 4;
                //   bottomNavigation.show(3, true);

                break;
            }
            case 5: {
                TXT_Logout.setTextColor(getResources().getColor(R.color.purple_200));
                IMG_Logout.setBackground(getResources().getDrawable(R.drawable.logout));
                Logout.setBackground(getResources().getDrawable(R.drawable.menu_item));
                MenuReChangThemes(MENU_ITEM_CLICK);
                MENU_ITEM_CLICK = 5;

                break;
            }

            case 6: {
                TXT_Info.setTextColor(getResources().getColor(R.color.purple_200));
                IMG_Info.setBackground(getResources().getDrawable(R.drawable.information));
                Info.setBackground(getResources().getDrawable(R.drawable.menu_item));
                MenuReChangThemes(MENU_ITEM_CLICK);
                MENU_ITEM_CLICK = 6;

                break;
            }

        }
    }

    private void MenuReChangThemes(int a) {
        switch (a) {
            case 1: {
                TXT_Main.setTextColor(getResources().getColor(R.color.White));
                IMG_Main.setBackground(getResources().getDrawable(R.drawable.ico_home));
                Main.setBackground(null);
                break;
            }

            case 7: {
                TXT_Profile.setTextColor(getResources().getColor(R.color.White));
                IMG_Profile.setBackground(getResources().getDrawable(R.drawable.user));
                Profile.setBackground(null);

                break;
            }
            case 2: {
                TXT_App.setTextColor(getResources().getColor(R.color.White));
                IMG_App.setBackground(getResources().getDrawable(R.drawable.ico_applications));
                Application.setBackground(null);

                break;
            }
            case 3: {

                TXT_Myapp.setTextColor(getResources().getColor(R.color.white));
                IMG_Myapp.setBackground(getResources().getDrawable(R.drawable.ico_myapp));
                Myapp.setBackground(null);

                break;
            }
            case 4: {
                TXT_Noti.setTextColor(getResources().getColor(R.color.white));
                IMG_Noti.setBackground(getResources().getDrawable(R.drawable.ico_notifi));
                Notification.setBackground(null);

                break;
            }
            case 5: {
                TXT_Logout.setTextColor(getResources().getColor(R.color.white));
                IMG_Logout.setBackground(getResources().getDrawable(R.drawable.ico_logout));
                Logout.setBackground(null);

                break;
            }
            case 6: {
                TXT_Info.setTextColor(getResources().getColor(R.color.white));
                IMG_Info.setBackground(getResources().getDrawable(R.drawable.ico_info));
                Info.setBackground(null);

                break;
            }

        }

    }

    @Override
    protected void onResumeFragments() {
        //setFragment(new NewsFragment());
        MenuChangThemes(1);
//        bottomNavigation.show(1, true);
        super.onResumeFragments();
    }



}