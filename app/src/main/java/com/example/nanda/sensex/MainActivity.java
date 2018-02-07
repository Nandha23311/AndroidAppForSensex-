package com.example.nanda.sensex;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
EditText user,pass;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user=(EditText)findViewById(R.id.username);
        pass=(EditText)findViewById(R.id.password);
        submit=(Button)findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usertext=user.getText().toString().toLowerCase();
                Integer passtext=Integer.parseInt(pass.getText().toString());
                if((usertext.equals("student"))&&(passtext==123)){
                        Toast.makeText(getApplicationContext(), "Student", Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(MainActivity.this,SaveActivity.class);
                        startActivity(i);
                    }
                if((usertext.equals("officer"))&&(passtext==123)){
                    Toast.makeText(getApplicationContext(), "Officer", Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(MainActivity.this,Result.class);
                    startActivity(i);
                }
            }
        });
    }
}
