package com.example.kif.kursy_12;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import static android.app.Activity.RESULT_OK;

public class MyService extends Service {
    private MyBinder myBinder;

    public MyService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String action = intent.getAction();
        PendingIntent pendingIntent = intent.getParcelableExtra("PendingIntent");

        if (action.equals("Save student")){
            String text = intent.getStringExtra("Student");
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();

            Intent result = new Intent();
            result.putExtra("Saved", true);

            try {
                pendingIntent.send(this, RESULT_OK, result);
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }

        }


        return super.onStartCommand(intent, flags, startId);
    }

    public void test(){
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "I am cool Service", Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        if(myBinder== null){
            myBinder = new MyBinder();

        }
        return myBinder;

    }
     public class MyBinder extends Binder{
         MyService getService(){
             return MyService.this;
         }
     }
}
