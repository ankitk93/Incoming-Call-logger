package example.maps.andoid.com.incomingcalllogs;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URLEncoder;


public class MainActivity extends Activity {
    TextView tvNumber;
    EditText etName;
    EditText etEmail;
    EditText etReview;
    DBAdapter db;
    private boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

   public void saveData(View view) {
       boolean connectionAvailable= isNetworkAvailable();
       if(connectionAvailable== true)
       {
           Thread t = new Thread(new Runnable() {
               @Override
               public void run() {
                   postData();
               }
           });
           t.start();
           Toast.makeText(getBaseContext(), "contact saved in Spreadsheet", Toast.LENGTH_LONG).show();
       }
       if(connectionAvailable== false) {
           db.open();

               String number = tvNumber.getText().toString();
               String name = etName.getText().toString();
               String email = etEmail.getText().toString();
               String review= etReview.getText().toString();
               db.insertContact(number, name, email,review);
               db.close();
               Toast.makeText(getBaseContext(), "contact saved in Database", Toast.LENGTH_LONG).show();
       }
    }

// Posting Data to Google Spreadsheet

    public void postData() {
        String fullUrl = getString(R.string.new_spreasheet);
        HttpRequest mReq = new HttpRequest();
        String number = tvNumber.getText().toString();
        String name = etName.getText().toString();
        String email = etEmail.getText().toString();
        String review= etReview.getText().toString();
        String data = getString(R.string.number) + URLEncoder.encode(number) + "&" +
                getString(R.string.name) + URLEncoder.encode(name) + "&" + getString(R.string.email) + URLEncoder.encode(email) + "&" + getString(R.string.review) + URLEncoder.encode(review);
        String response = mReq.sendPost(fullUrl, data);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = new DBAdapter(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle extra=getIntent().getExtras();
        String phoneNumber=extra.getString("number");
        tvNumber=(TextView)findViewById(R.id.tvNmber);
        tvNumber.setText(""+phoneNumber);
        etName=(EditText)findViewById(R.id.etName);
        etEmail=(EditText)findViewById(R.id.etEmail);
        etReview=(EditText)findViewById(R.id.etReview);
    }



}
