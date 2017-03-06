package example.maps.andoid.com.incomingcalllogs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.widget.Toast;

/**
 * Created by techie93 on 4/23/2015.
 */
public class MyBroadcastReciever extends BroadcastReceiver {
    boolean isRinging = false;
    String number = "";

    @Override
    public void onReceive(Context context, Intent intent) {

        String phoneState = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        if (phoneState.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            isRinging = true;
            number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            Toast.makeText(context, "Number is " + number, Toast.LENGTH_LONG).show();
        }
        if (phoneState.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
            number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            Toast.makeText(context, "Number is " + number, Toast.LENGTH_LONG).show();
            boolean check=contactExists(context.getApplicationContext(),number);
            if(check== false)
            {
                Intent intentForActivity = new Intent(context.getApplicationContext(), MainActivity.class);
                intentForActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intentForActivity.putExtra("number", number);
                context.startActivity(intentForActivity);
            }
        }
    }


    public boolean contactExists(Context context, String number) {
/// number is the phone number
        Uri lookupUri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(number));
        String[] mPhoneNumberProjection = { ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.NUMBER, ContactsContract.PhoneLookup.DISPLAY_NAME };
        Cursor cur = context.getContentResolver().query(lookupUri,mPhoneNumberProjection, null, null, null);
        try {
            if (cur.moveToFirst()) {
                return true;
            }
        } finally {
            if (cur != null)
                cur.close();
        }
        return false;
    }


}
