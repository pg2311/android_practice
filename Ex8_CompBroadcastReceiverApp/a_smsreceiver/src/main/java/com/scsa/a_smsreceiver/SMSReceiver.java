package com.scsa.a_smsreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSReceiver extends BroadcastReceiver {

    private static final String TAG = "SMSReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive!");
        String action = intent.getAction();
        if ("android.provider.Telephony.SMS_RECEIVED".equals(action)) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                if (pdus != null) {
                    for (Object pdu: pdus) {
                        String format = bundle.getString("format");
                        SmsMessage smsMessage = SmsMessage.createFromPdu((byte[])pdu, format);
                        String sender = smsMessage.getDisplayOriginatingAddress();//발신자 번호
                        String messageBody = smsMessage.getMessageBody();//메시지 내용
                        long timestamp = smsMessage.getTimestampMillis(); //발신 시간

                        String sms = "sender=" + sender
                                + ", messageBody=" + messageBody
                                + ", timestamp=" + timestamp;

                        MainActivity.getSMSListener().onSmsReceived(sms);
                    }
                }
            }
        }
    }
}
