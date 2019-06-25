package com.example.asynctask;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button button;
    TextView tv_result;
    EditText in_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_result = (TextView)findViewById(R.id.tvResult);
        in_time = (EditText) findViewById(R.id.inTime);
        button = (Button) findViewById(R.id.btnRun);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTaskRunner runner = new AsyncTaskRunner();
                String sleepTime = in_time.getText().toString();
                runner.execute(sleepTime);
            }
        });
    }

    public class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String response;
        ProgressDialog progressDialog;

        // This method contains the code which is executed before the background processing starts
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(MainActivity.this,
                    "ProgressDialog",
                    "Wait for "+in_time.getText().toString()+ " seconds");

            Log.e("Print", "This is the first step in the process");

        }

        //  This method contains the code which needs to be executed in background. In this method we can send results multiple
        //  times to the UI thread by publishProgress() method. To notify that the background processing has been completed we
        //  just need to use the return statements
        @Override
        protected String doInBackground(String... strings) {
            // Calls onProgressUpdate()
            publishProgress("Sleeping...");

            try {
                int time = Integer.parseInt(strings[0])*1000;

                Thread.sleep(time);
                response = "Slept for " + strings[0] + " seconds.";

            } catch (InterruptedException e) {
                e.printStackTrace();
                response = e.getMessage();
            } catch (Exception e) {
                e.printStackTrace();
                response = e.getMessage();
            }
            return response;
        }

        // This method is called after doInBackground method completes processing. Result from doInBackground is passed to this method
        @Override
        protected void onPostExecute(String s) {
            // execution of result of Long time consuming operation
            progressDialog.dismiss();;
            tv_result.setText(response);
            super.onPostExecute(s);

            Log.e("Print", "Response in onPostExecute section " + response.toString());
            Log.e("Print", "This is after the execution is complete in doInbackground and result needs to be displayed");
        }

        // This method receives progress updates from doInBackground method, which is published via publishProgress method, and this
        // method can use this progress update to update the UI thread
        @Override
        protected void onProgressUpdate(String... values) {
            tv_result.setText(values[0]);
            Log.e("Print", "OnProgress Update section " + values[0]);
            Log.e("Print", "This comes after onPreExecute");
        }


    }
}
