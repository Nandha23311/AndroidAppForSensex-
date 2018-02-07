package com.example.nanda.sensex;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SaveActivity extends AppCompatActivity {
    int i=0;
TextView tv1,tv2;
JSONArray memberNameArray;
JSONObject jsonformember,singlejsonformember,json;
EditText familyName,memberName,age,village;
Spinner gender,occupation;
String familyNameString,memberNameString,ageString,genderString,occupationString,villageString;
Button bsave,addmember,finishMember;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);
        tv1=(TextView)findViewById(R.id.textView1);
        tv2=(TextView)findViewById(R.id.textView2);
        familyName=(EditText)findViewById(R.id.familyNametext);
        memberName=(EditText)findViewById(R.id.memberText);
        village=(EditText)findViewById(R.id.villageNametext);
        age=(EditText)findViewById(R.id.ageText);
        gender=(Spinner)findViewById(R.id.genderspin);
        occupation=(Spinner)findViewById(R.id.occupationspinn);
        bsave=(Button)findViewById(R.id.bsave);


        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        genderString = "Male";
                        break;
                    case 2:
                        genderString = "Female";
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplicationContext(), "Please Select Gender ", Toast.LENGTH_SHORT).show();
            }
        });

        occupation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        occupationString = "Student";
                        break;
                    case 2:
                        occupationString = "Govt Job";
                        break;
                    case 3:
                        occupationString = "Private Job";
                        break;
                    case 4:
                        occupationString = "Farmer";
                        break;
                    case 5:
                        occupationString = "Other";
                        break;

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplicationContext(), "Please Select Gender ", Toast.LENGTH_SHORT).show();
            }
        });
    bsave.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            familyNameString = familyName.getText().toString();
            memberNameString = memberName.getText().toString();
            ageString = age.getText().toString();
            villageString = village.getText().toString().toLowerCase();
            json = new JSONObject();
            try {
                json.put("familyName", familyNameString);
                json.put("villageName", villageString);
                JSONArray memberName = new JSONArray();
                JSONObject singlejson = new JSONObject();
                singlejson.put("name", memberNameString);
                singlejson.put("gender",genderString);
                singlejson.put("age", ageString);
                singlejson.put("occupation", occupationString);
                memberName.put(singlejson);
                json.put("memberName", (Object) memberName);

                new savetoServer().execute();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    });
        /*addmember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bsave.setVisibility(View.INVISIBLE);
                finishMember.setVisibility(View.VISIBLE);
                memberNameString = memberName.getText().toString();
                ageString = age.getText().toString();


                try {
                    memberNameArray = new JSONArray();
                    singlejsonformember = new JSONObject();
                    singlejsonformember.put("name", memberNameString);
                    singlejsonformember.put("age", ageString);
                    singlejsonformember.put("gender",genderString);
                    singlejsonformember.put("occupation", occupationString);
                    memberNameArray.put(singlejsonformember);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
        finishMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                familyNameString = familyName.getText().toString();
                villageString = village.getText().toString();
                jsonformember = new JSONObject();
                try {
                    jsonformember.put("familyName", familyNameString);
                    jsonformember.put("villageName", villageString);
                    jsonformember.put("memberName", (Object) memberNameArray);
                    new addmember().execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
*/

    }

    public class savetoServer extends AsyncTask<String,String,String> {
        int code;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SaveActivity.this);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setIndeterminate(false);
            progressDialog.setMessage("Please Wait");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String postUrl = "https://sensexapp.herokuapp.com/sensex/family/save";
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");

            OkHttpClient client = new OkHttpClient();

            JSONObject json1 = new JSONObject();
            try {
                json1.put("familyObj", json);
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
            SaveActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject json = new JSONObject(myRes);
                        code = json.getInt("code");
                        if (code == 200) {
                          progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Record Saved",Toast.LENGTH_LONG).show();
                            familyName.setText("");
                            memberName.setText("");
                            age.setText("");
                            village.setText("");

                        } else {
                          Toast.makeText(getApplicationContext(),"Record Not Uploaded",Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

}
