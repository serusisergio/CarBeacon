package com.example.sergio.carbeacon;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
/**
 * Created by Stefano and Sergio on 04/12/2014.
 */

public class MyActivity extends Activity {

    private String SENT = "SMS_SENT";
    private String latitudine = "";
    String longitudine = "";
    String message = "";
    private boolean enabled = false;
    private EditText testo;
    private EditText phoneNumbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        testo = (EditText) findViewById(R.id.text);
        phoneNumbers = (EditText) findViewById(R.id.editText);

        final SmsReceiver rx = new SmsReceiver();

        final IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");

        final Button button = (Button)findViewById(R.id.button);
        registerReceiver(rx, myIntentFilter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("ButtonClicked", "bottone premuto");

                LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                Criteria criteri = new Criteria();
                String provider = lm.getBestProvider(criteri, true);
                lm.requestSingleUpdate(provider, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        String testos = testo.getText().toString();
                        String number = phoneNumbers.getText().toString();
                        latitudine = "" + String.valueOf(location.getLatitude());
                        longitudine = "" + String.valueOf(location.getLongitude());
                        Log.d("clickBottone", number + " " + testos);
                        message = ""+ testos +" . Mi trovi qui: "+"https://www.google.it/maps/place/" + latitudine + "," + longitudine;
                        Log.d("clickBottone", "testo: " + message);
                        sendSMS(number, message);
                    }

                    @Override
                    public void onStatusChanged(String s, int i, Bundle bundle) {

                    }

                    @Override
                    public void onProviderEnabled(String s) {

                    }

                    @Override
                    public void onProviderDisabled(String s) {

                    }
                }, null);
            }
        });



        rx.setOnSmsReceivedListener(new SmsReceiver.SmsReceiverListener() {
            @Override
            public void smsReceived(final String sender, final String text) {
                if (text.toLowerCase().equals("posizione")) {
                    LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    Criteria criteri = new Criteria();
                    String provider = lm.getBestProvider(criteri, true);
                    lm.requestSingleUpdate(provider, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            latitudine = "" + String.valueOf(location.getLatitude());
                            longitudine = "" + String.valueOf(location.getLongitude());
                            String phoneNumber = sender;
                            message = "https://www.google.it/maps/place/" + latitudine + "," + longitudine;
                            sendSMS(phoneNumber, message);
                        }

                        @Override
                        public void onStatusChanged(String s, int i, Bundle bundle) {

                        }

                        @Override
                        public void onProviderEnabled(String s) {

                        }

                        @Override
                        public void onProviderDisabled(String s) {

                        }
                    }, null);

                }
            }
        });

    }


    public void sendSMS(String phoneNumber, String message) {
        SmsManager smsManager = SmsManager.getDefault();
        String SENT = "SMS_SENT";

        SmsManager sms = SmsManager.getDefault();
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);

        smsManager.sendTextMessage(phoneNumber, null, message, sentPI, null);
    }

}
