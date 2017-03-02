package com.example.kif.kursy_12;

import android.app.Activity;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

public class MyIntentService extends IntentService {
    private static final String ACTION_SAVE_STUDENT = "com.example.kif.kursy_12.action.SAVE_STUDENT";
    private static final String ACTION_GET_STUDENT = "com.example.kif.kursy_12.action.GET_STUDENT";

    // TODO: Rename parameters
    public static final String EXTRA_STUDENT = "com.example.kif.kursy_12.extra.STUDENT";
    public static final String EXTRA_ID = "com.example.kif.kursy_12.extra.ID";
    public static final String EXTRA_PENDING_INTENT="com.example.kif.kursy_12.extra.PENDING_INTENT";

    public static final int REQUEST_CODE_SAVE_STUDENT = 1;
    public static final int REQUEST_CODE_GET_STUDENT = 2;


    public MyIntentService() {
        super("MyIntentService");
    }//обязательно конструктор с его айдишкой, без него работать не будет



    public static void saveStudent(Context context, Student student) {
        Intent intent = new Intent(context, MyIntentService.class);

        PendingIntent pendingIntent = ((AppCompatActivity)context).createPendingResult(REQUEST_CODE_SAVE_STUDENT,intent,0);
        intent.putExtra(EXTRA_PENDING_INTENT, pendingIntent);


        intent.setAction(ACTION_SAVE_STUDENT);

        intent.putExtra(EXTRA_STUDENT,student);

        context.startService(intent);
    }

    public static void getStudent(Context context, long id) {
        Intent intent = new Intent(context, MyIntentService.class);

        PendingIntent pendingIntent = ((AppCompatActivity)context).createPendingResult(REQUEST_CODE_GET_STUDENT,intent,0);
        intent.putExtra(EXTRA_PENDING_INTENT,pendingIntent);
        // вначале пендинг интент перед другими  путЭкстра, или его данные будут не те


        intent.setAction(ACTION_GET_STUDENT);

        intent.putExtra(EXTRA_ID,id);

        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

/*
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
*/
        if (intent != null) {
            String action = intent.getAction();//вытягиваем экшн и пендинг

            PendingIntent pendingIntent = intent.getParcelableExtra(EXTRA_PENDING_INTENT);
            Intent resultIntent = new Intent();

            DataBaseHelper helper = new DataBaseHelper(this);

            if (ACTION_GET_STUDENT.equals(action)) {
                long id =intent.getLongExtra(EXTRA_ID,0);

                Student student = helper.getStudent(id);
                resultIntent.putExtra(EXTRA_STUDENT, student);


            } else if (ACTION_SAVE_STUDENT.equals(action)) {
                Student student = intent.getParcelableExtra(EXTRA_STUDENT);

                long id = helper.insertStudent(student);

                resultIntent.putExtra(EXTRA_ID, id);
            }

            try {
                pendingIntent.send(this, Activity.RESULT_OK, resultIntent);
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
        }
    }

}
