package com.example.nanda.sensex;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Result extends AppCompatActivity {
TextView tv1,tv2,tv3,tv4;
EditText findtext;
String findString;
Button find;
    int total,male,female;
    JSONObject json;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        tv1=(TextView)findViewById(R.id.villagename);
        tv2=(TextView)findViewById(R.id.population);
        tv3=(TextView)findViewById(R.id.nomale);
        tv4=(TextView)findViewById(R.id.nofemale);
        findtext=(EditText)findViewById(R.id.findtext);
        find=(Button)findViewById(R.id.bfind);
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(findtext.getText().toString().trim().length()==0){
                    Toast.makeText(getApplicationContext(),"Please Enter Village Name",Toast.LENGTH_LONG).show();
                }
                findString=findtext.getText().toString().toLowerCase();

                new findData().execute();

            }
        });
    }
    public class findData extends AsyncTask<String,String,String> {
        int code;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Result.this);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setIndeterminate(false);
            progressDialog.setMessage("Please Wait");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String postUrl = "https://sensexapp.herokuapp.com/sensex/family/find";
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");

            OkHttpClient client = new OkHttpClient();
            JSONObject json1 = new JSONObject();
            try {
                json1.put("villageName", findString);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            RequestBody body = RequestBody.create(JSON, json1.toString());

            final Request request = new Request.Builder()
                    .url(postUrl)
                    .post(body)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(final String s) {
            super.onPostExecute(s);
            final String myRes = s;
            Log.d("Response", s);
            Result.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject json = new JSONObject(myRes);
                        code = json.getInt("code");
                        if (code == 200) {
                            progressDialog.dismiss();
                            total=json.getInt("total");
                            male=json.getInt("male");
                            female=json.getInt("female");
                            tv1.setText("Village Name       : "+findString );
                            tv2.setText("Total population   : "+total );
                            tv3.setText("Total male         : "+male );
                            tv4.setText("Total Female       : "+female );

                        } else {
                            Toast.makeText(getApplicationContext(), "Record Not Uploaded", Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
