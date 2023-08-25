package com.example.google_map_to_track_user;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Get_LatLong extends AppCompatActivity {

    private NetworkChangeReceiver networkChangeReceiver;

    Button btntrack;
    FusedLocationProviderClient mFusedLocationClient;
    int min1, hr1, endmin1, endhr1;
    ImageView logout;
    String result;

    Button push;
    LinearLayout datepicker, starttimepicker, endtimepicker;
    AppCompatEditText txtdate, txtstarttime, txtendtime;
    // Initializing other items
    // from layout file
    TextView latitudeTextView, longitTextView;
    DBhelper DB;
    int PERMISSION_ID = 44;
    int count = 0;
    Button start, stop;
    long time;
    static boolean check = false;
    //===============
    TextView txtsqlite, txtposteddata;
    Button btntrackdata;

    private final static String SOAP_HEADER = "<?xml version=\"1.0\" encoding=\"utf-8\"?><soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Body><PostEmpLocation xmlns=\"http://tempuri.org/\"><empLocations>";
    private final static String SOAP_FOOTER = "</empLocations></PostEmpLocation></soap:Body></soap:Envelope>";

    private ArrayList<LatLagDO> arrlistpost = new ArrayList<>();

    //Handler handler=new Handler();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("onCreatejdfhhf", "onCreate: ");
        setContentView(R.layout.activity_get_lat_long);
        latitudeTextView = findViewById(R.id.text1);
        longitTextView = findViewById(R.id.text2);
        start = findViewById(R.id.start1);
        stop = findViewById(R.id.stop1);
        btntrack = findViewById(R.id.btntrack);
        logout = findViewById(R.id.logout);
        push = findViewById(R.id.push);


        txtsqlite = findViewById(R.id.txtsqlitedata);
        txtposteddata = findViewById(R.id.txtserverdata);
        Button btntrackdata = findViewById(R.id.btntrackdata);
        //============


        DB = new DBhelper(this);

        // DB.Deleteposted();
        //brodcast receiver classes
        new Thread(new Runnable() {
            @Override
            public void run() {
               /* if (!isFinishing()) {

                }*/

                IntentFilter intentFilter = new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION);
                registerReceiver(new NetworkChangeReceiver(), intentFilter);
                registerReceiver(new NetworkChangeReceiver.LocationProvidersChangedReceiver(), intentFilter);
            }
        }).start();


        StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(gfgPolicy);


        btntrackdata.setOnClickListener((View view) -> {

            int sqlitedata = DB.TrackSqliteData();
            int serverdata = DB.TrackserverData();

            txtsqlite.setText(String.valueOf(sqlitedata));
            txtposteddata.setText(String.valueOf(serverdata));
            //push.setText("pushed");
        });
       /* Cursor cursor = DB.PostDatalist();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "post data emty", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                arrlistpost.add(new LatLagDO(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getDouble(4),
                        cursor.getDouble(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10),
                        cursor.getString(11), cursor.getInt(12)));
            }
            Log.d("arrlistpost", "onCreate: " + arrlistpost.size());
            Thread gfgThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try  {
                        BuildXMLRequest buildXMLRequest = new BuildXMLRequest();
                        String requestcode=buildXMLRequest.HttpSendData(arrlistpost);
                        // String requestcode = HttpSendData();
                        Log.i("push push", "run: button push" + requestcode);

                        if (requestcode=="200"){
                            Log.i("push push", "run: button push 20000000" + requestcode);
                            DB.PostedDatatoserver(arrlistpost);
                        }
                        // Your network activity
                        // code comes here
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            gfgThread.start();

        }*/
        //linearlayout
//        datepicker = findViewById(R.id.selectdate);
        // starttimepicker = findViewById(R.id.starttimepicker);
        // endtimepicker = findViewById(R.id.endtimepicker);
        //text view date time
        txtdate = findViewById(R.id.txtselecteddate);
        txtstarttime = findViewById(R.id.txtstarttime);
        txtendtime = findViewById(R.id.txtendtime);

        txtstarttime.setText("00:00");
        time = DB.getSettings();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentdate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        String date = currentdate.format(calendar.getTime());
        String time = currentTime.format(calendar.getTime());
        txtdate.setText(date);
        //txtendtime.setText(time);
        Log.d("txtendtime", "onCreate:" + time + "time");
        /*txtstarttime.setText("00:00");
        txtendtime.setText("23:59");*/
        //requestPermissions();
        //getting date

        /*Thread uithtread = new Thread();
        uithtread = new Thread(new Runnable() {
            @Override
            public void run() {
                getLocation();
            }
        });*/
        //Bundle bundle=new Bundle();
        //bundle.;


        //requestPermissions();

        MyServices.context = this;
        MyServices.get_latLong = this;
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                Intent intent = new Intent(Get_LatLong.this, MyServices.class);
                ContextCompat.startForegroundService(this, intent);
                //
                startService(intent);
            } else {
                Toast.makeText(Get_LatLong.this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }

        } else {
            requestPermissions();
        }

        //stop service
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(new Intent(Get_LatLong.this, MyServices.class));
                //MyServices.isstarted = true;

            }
        });
        //logout
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getSharedPreferences("login", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("flag", false);
                editor.apply();
                Intent intent = new Intent(Get_LatLong.this, MainActivity.class);
                startActivity(intent);
                Log.d("hgdghhjdsgjgdsjgh", "onClick: ");
                MyServices.isstarted = false;
                stopService(new Intent(Get_LatLong.this, MyServices.class));
                finish();
            }
        });
        //getLocation();
        //date picker
        txtdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c = Calendar.getInstance();

                // on below line we are getting
                // our day, month and year.
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);


                CalendarConstraints.Builder caladarconstraintbuilder = new CalendarConstraints.Builder();
                caladarconstraintbuilder.setValidator(DateValidatorPointBackward.now());
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        Get_LatLong.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our text view.
                                SimpleDateFormat currentdate = new SimpleDateFormat("yyyy-MM-dd");
                                String s = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                try {
                                    Date date = currentdate.parse(s);
                                    txtdate.setText(currentdate.format(date));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }


                            }
                        },
                        // on below line we are passing year,
                        // month and day for selected date in our date picker.
                        year, month, day);
                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.show();
            }
        });
        /*txtdate.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_WAKEUP) {
                    InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.);
                    imm.;

                    return true;
                }
                return true;
            }
        });*/
        // start time picker
        txtstarttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePicker = new TimePickerDialog(Get_LatLong.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                min1 = i1;
                                hr1 = i;
                                String time1 = hr1 + ":" + min1;
                                SimpleDateFormat f24Hours = new SimpleDateFormat("HH:mm");
                                //String s = f24Hours.format(time1);
                                try {
                                    Date date = f24Hours.parse(time1);
                                    txtstarttime.setText(f24Hours.format(date));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, 24, 0, true

                );
                timePicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePicker.updateTime(hr1, min1);
                timePicker.show();

            }
        });
        // end time
        txtendtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePicker = new TimePickerDialog(Get_LatLong.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                endmin1 = i1;
                                endhr1 = i;
                                String time1 = endhr1 + ":" + endmin1;
                                SimpleDateFormat f24Hours = new SimpleDateFormat("HH:mm");
                                try {
                                    Date date = f24Hours.parse(time1);
                                    txtendtime.setText(f24Hours.format(date));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }


                            }
                        }, 24, 0, true

                );
                timePicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //
                // timePicker.getWindow().set
                timePicker.updateTime(endhr1, endmin1);
                timePicker.show();

            }
        });
//-------------------------------------------------------
        //mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        //.time = 5000;
        // method to get the location


        //getLastLocation();
        /*start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check = true;
                new Thread(() -> {
                    while (check) {
                        try {

                            getLocation();
                            //requestNewLocationData();
                            Thread.sleep(time);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //getLastLocation();

                    }

                }).start();



            }
        });
*/


        btntrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = txtdate.getText().toString();
                String starttime = date + " " + txtstarttime.getText().toString() + ":00";
                String endtime = date + " " + txtendtime.getText().toString() + ":00";
                Log.d("times", "onClick: " + starttime + "   " + endtime);
                //   String hrst= String.valueOf(starttime.indexOf(0,2));
                ArrayList<LatLng> latLngslist = DB.getLatLong(starttime, endtime);
                boolean check = false;
                if (hr1 < endhr1) {
                    check = true;
                } else if (hr1 == endhr1 && min1 < endmin1) {
                    check = true;
                } else {
                    if (!txtendtime.getText().toString().isEmpty() && !txtstarttime.getText().toString().isEmpty()) {
                        Toast.makeText(Get_LatLong.this, "Select Correct Time", Toast.LENGTH_SHORT).show();
                        //  Log.d("Trackdatetime", "onClick: "+txtendtime.getText().toString()+"  "+txtstarttime.getText().toString()+"hr1 "+hr1+"endhr1 "+endhr1+"min1 "+min1+" endmin1 "+endmin1+"  "+hrst);
                    }
                }

                if ((!txtdate.getText().toString().isEmpty() && !txtendtime.getText().toString().isEmpty() && !txtstarttime.getText().toString().isEmpty())) {
                    //Log.d("nullllllllllllll", "onClick: " + txtendtime.getText());
                    if (check) {

                        if (latLngslist.size() > 0) {

                           /* Intent intent = new Intent(Get_LatLong.this, MapsActivity.class);
                            intent.putParcelableArrayListExtra("list", latLngslist);
                            startActivity(intent);*/
                        } else {

                            Toast.makeText(Get_LatLong.this, "No data found", Toast.LENGTH_SHORT).show();

                        }
                    }
                } else {

                    Toast.makeText(Get_LatLong.this, "Select all Details", Toast.LENGTH_SHORT).show();

                }


            }
        });


    }

//getlocation method
   /* public  void getLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last
                // location from
                // FusedLocationClient
                // object
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager
                        .PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return ;
                }
             //   requestNewLocationData();
                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
                mFusedLocationClient.getLastLocation().addOnCompleteListener(task -> {
                    Location location = task.getResult();
                    if (location == null) {
                        //requestNewLocationData();
                    } else {
//                        latitudeTextView.setText(location.getLatitude() + "");
//                        longitTextView.setText(location.getLongitude() + "");
//                        count++;
//                        DB.insertData(Double.parseDouble(location.getLatitude()+""),Double.parseDouble(location.getLongitude()+""));
//                        Toast.makeText(this, location.getLatitude() + " " + location.getLongitude() + " count=" + count, Toast.LENGTH_SHORT).show();
                    }
                //});
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
        return ;
    }


    public void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(12);
        mLocationRequest.setNumUpdates(1);


        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.getMainLooper());

    }

    public LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latitudeTextView.setText("Latitude: " + mLastLocation.getLatitude() );
            longitTextView.setText("Longitude: " + mLastLocation.getLongitude() );
            count++;
            DB.insertData(Double.parseDouble(mLastLocation.getLatitude()+""),Double.parseDouble(mLastLocation.getLongitude()+""));
            Toast.makeText(Get_LatLong.this, mLastLocation.getLatitude() + " " + mLastLocation.getLongitude() + " count=" + count, Toast.LENGTH_SHORT).show();

        }
    };*/

    // method to check for permissions
    public boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        //ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    // method to request for permissions
    public void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                ACCESS_COARSE_LOCATION,
                ACCESS_FINE_LOCATION}, PERMISSION_ID);
        Intent intent = new Intent(Get_LatLong.this, MyServices.class);
        ContextCompat.startForegroundService(this, intent);
        startService(intent);
       /* AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("please allow locations permissions");
        builder.setPositiveButton("Allow", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
        builder.show();*/

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(this, new String[]{

                    Manifest.permission.ACCESS_BACKGROUND_LOCATION}, PERMISSION_ID);
        }*/


    }

    // method to check
    // if location is enabled
    public boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    protected void onResume() {
        super.onResume();
        networkChangeReceiver = new NetworkChangeReceiver();
        final IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");

        registerReceiver(networkChangeReceiver, intentFilter);
       /* LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Please enable GPS", Toast.LENGTH_SHORT).show();
        }*/

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkChangeReceiver);
        //unregisterReceiver(new NetworkChangeReceiver.LocationProvidersChangedReceiver());


        /*Intent intent = new Intent(Get_LatLong.this,MyServices.class);
        ContextCompat.startForegroundService(Get_LatLong.this,intent);
        startService(intent);*/
        Log.d("gdgfghdfghfgdsdhsghd", "onPause: ");
    }


}