package example.maps.andoid.com.incomingcalllogs;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.widget.Toast;

import java.net.URLEncoder;

/**
 * Created by techie93 on 4/28/2015.
 */
public class UpdateToSpreadsheet extends Service {
    DBAdapter db;
    public UpdateToSpreadsheet()
    {
        super();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();


                Thread t =new Thread(new Runnable() {
                    @Override
                    public void run() {
                        db.open();
                        Cursor c = db.getAllContacts();

                        if (c.moveToFirst()) {
                            do {
                                //String fullUrl = "https://docs.google.com/forms/d/1LOAxFsaz6DgvupdQdrr8YvECILHx_Zmp10mGO23Zc3k/formResponse";
                                String fullUrl = "https://docs.google.com/forms/d/e/1FAIpQLSfYCD06STIigtWAU1vv9R26CPB1ubA7X9F3O9q3Xe2OK2tN7Q/formResponse";
                                HttpRequest mReq = new HttpRequest();
                                String number = c.getString(0);
                                String name = c.getString(1);
                                String email = c.getString(2);
                                String data = "entry.1060727697=" + URLEncoder.encode(number) + "&" +
                                        "entry.1226813856=" + URLEncoder.encode(name) + "&" + "entry.1615459493=" + URLEncoder.encode(email);
                                String response = mReq.sendPost(fullUrl, data);
                                db.deleteContact(c.getString(0));
                            } while (c.moveToNext());
                        }db.close();
                    }
                });t.start();


            Toast.makeText(getBaseContext(),"All contacts updated to spreadsheet",Toast.LENGTH_LONG).show();
        }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);



    }
}