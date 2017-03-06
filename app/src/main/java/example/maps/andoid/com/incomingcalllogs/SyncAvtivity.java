package example.maps.andoid.com.incomingcalllogs;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URLEncoder;


public class SyncAvtivity extends Activity {
    DBAdapter db;
    TextView tvResult;
    Button btnSync;
    public void sync(final View view)
    {
        boolean connection= isOnline(getBaseContext());
        if(connection== true)
        {
            Thread t =new Thread(new Runnable() {
                @Override
                public void run() {
                    db.open();
                    final Cursor cursor = db.getAllContacts();
                    if (cursor.moveToFirst()) {
                        do {
                            postData(cursor.getString(0), cursor.getString(1), cursor.getString(2),cursor.getString(3));
                        } while (cursor.moveToNext());
                        db.deleteContacts();
                        db.close();
                    }
                }
            });
            t.start();
            Toast.makeText(getBaseContext(),"Contacts Updated to Spreadsheet",Toast.LENGTH_LONG).show();
            tvResult.setText("Contact Updated");

        }
        else
        {
            Toast.makeText(getBaseContext(),"CHECK YOUR INTERNET CONNECTION AND TRY AGAIN",Toast.LENGTH_LONG).show();
        }
    }

    public boolean isOnline(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in air plan mode it will be null
        return (netInfo != null && netInfo.isConnected());

    }
    public void postData(String number,String name,String email,String review) {
        String fullUrl = getString(R.string.spreadsheet);
        HttpRequest mReq = new HttpRequest();
        String data = getString(R.string.number) + URLEncoder.encode(number) + "&" +
                getString(R.string.name) + URLEncoder.encode(name) + "&" + getString(R.string.email) + URLEncoder.encode(email) + "&" + getString(R.string.review) + URLEncoder.encode(review);
        String response = mReq.sendPost(fullUrl, data);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_avtivity);
        tvResult=(TextView)findViewById(R.id.tvResult);
        db = new DBAdapter(this);
        btnSync=(Button)findViewById(R.id.btnSync);
    }

}
