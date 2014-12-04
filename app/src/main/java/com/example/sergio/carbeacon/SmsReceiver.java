package com.example.sergio.carbeacon;

/**
 * Created by Stefano and Sergio on 04/12/2014.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {

    private SharedPreferences preferences;
    private final String TAG = this.getClass().getSimpleName();
    private SmsReceiverListener listener;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        String strMessage = "";
        if (extras != null) {
            Object[] smsextras = (Object[]) extras.get("pdus");
            for (int i = 0; i < smsextras.length; i++) {
                SmsMessage smsmsg = SmsMessage.createFromPdu((byte[]) smsextras[i]);
                String strMsgBody = smsmsg.getMessageBody().toString();
                String strMsgSrc = smsmsg.getOriginatingAddress();
                strMessage += "SMS from " + strMsgSrc + " : " + strMsgBody;
                listener.smsReceived(strMsgSrc, strMsgBody);
                Log.i(TAG, strMessage);
            }
        }
    }

    public void setOnSmsReceivedListener(SmsReceiverListener l) {
        this.listener = l;
    }

    public interface SmsReceiverListener {
        public void smsReceived(final String sender, final String text);
    }
}