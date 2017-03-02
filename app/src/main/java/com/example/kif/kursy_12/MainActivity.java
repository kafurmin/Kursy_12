package com.example.kif.kursy_12;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private boolean mConnected;
    private MyService myService;
    private  SaveTask mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void OnClick(View view) {

        switch (view.getId()) {
            case R.id.button:

                Intent intent = new Intent(this, MyService.class);
                intent.setAction("Save student");
                intent.putExtra("Student", "i am Supper SSStudent");

                PendingIntent pendingIntent = createPendingResult(1, intent,0);
                intent.putExtra("PendingIntent", pendingIntent);

                startService(intent);

                break;
            case R.id.button2:
                ServiceConnection connection = new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName componentName, IBinder service) {
                        mConnected =true;// индикатор подключен сервис или нет
                        myService = ((MyService.MyBinder)service).getService();
                        Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_SHORT ).show();
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName componentName) {
                        mConnected=false;
                    }

                };

                Intent intent2 = new Intent(this, MyService.class);
                bindService(intent2, connection, BIND_AUTO_CREATE);

                break;
            case R.id.button3:
                if(mConnected){
                    myService.test();
                }
                break;
            case R.id.button4:
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Wait...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                MyIntentService.saveStudent(this, new Student("Ivan", "Ivanov", 22));
                break;
            case R.id.button5:
//                progressDialog = new ProgressDialog(this);
  //              progressDialog.setMessage("Wait...");
      //          progressDialog.setCancelable(false);
    //            progressDialog.show();

                MyIntentService.getStudent(this, 10);//id=8
                break;
            case R.id.button6:
                mTask = new SaveTask();
                mTask.execute(new Student("Ivan","Ivanov",44));
                //new SaveTask().execute(new Student("Petya","Petrov",33));
                break;
            case R.id.button7:
                break;
            case R.id.button8:
                break;
            case R.id.button9:
                break;
            case R.id.button10:
                break;
            case R.id.button11:
                break;
            case R.id.button12:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    if (mTask != null){
        mTask.cancel(true);
    }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    /*    if(resultCode==RESULT_OK){
            if(requestCode==1){
                boolean res=data.getBooleanExtra("Saved", false);
                Toast.makeText(this, String.valueOf(res), Toast.LENGTH_SHORT).show();
            }
        }
*/
        if(resultCode == RESULT_OK){
            if(requestCode == MyIntentService.REQUEST_CODE_SAVE_STUDENT){
                long id = data.getLongExtra(MyIntentService.EXTRA_ID, 0);
                Toast.makeText(this, String.valueOf(id), Toast.LENGTH_SHORT).show();
            } else
            if(requestCode == MyIntentService.REQUEST_CODE_GET_STUDENT){
                Student student = data.getParcelableExtra(MyIntentService.EXTRA_STUDENT);
                Toast.makeText(this, student.FirstName, Toast.LENGTH_LONG).show();
            }

            if(progressDialog != null&& progressDialog.isShowing()){
            progressDialog.dismiss();
            }
        }
    }


    class SaveTask extends AsyncTask<Student, Void, Long>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected Long doInBackground(Student... params) {

            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);



            return dataBaseHelper.insertStudent(params[0]);
        }

        @Override
        protected void onPostExecute(Long aLong) {//приходит ид нашего студента
            super.onPostExecute(aLong);

            Toast.makeText(MainActivity.this, String.valueOf(aLong),Toast.LENGTH_SHORT).show();
            if(progressDialog != null&& progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        }
    }
}
