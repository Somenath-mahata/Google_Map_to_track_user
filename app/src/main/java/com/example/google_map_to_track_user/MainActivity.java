package com.example.google_map_to_track_user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button btnlogin;
    EditText edtuserid,edtpswd;

    BuildXMLRequest buildXMLRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnlogin = findViewById(R.id.btnlogin);
        edtuserid = findViewById(R.id.edtusername);
        edtpswd = findViewById(R.id.edtpassword);



        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edtuserid.getText().toString().isEmpty() && !edtpswd.getText().toString().isEmpty()) {
                    if (edtuserid.getText().toString().equals("1234") && edtpswd.getText().toString().equals("1234")) {
                        SharedPreferences pref = getSharedPreferences("login",MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();

                        editor.putBoolean("flag",true);
                        editor.apply();
                        Intent ihome = new Intent(MainActivity.this,Get_LatLong.class);
                        startActivity(ihome);
                        finish();
                        /*Intent intent = new Intent(MainActivity.this, Get_LatLong.class);
                        startActivity(intent);*/
                    } else {
                        Toast.makeText(MainActivity.this, "Invalid User Details", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    if(edtuserid.getText().toString().isEmpty()) {
                        edtuserid.setHintTextColor(Color.parseColor("#EE4B2B"));
                    }
                    if(edtpswd.getText().toString().isEmpty()) {
                        edtpswd.setHintTextColor(Color.parseColor("#EE4B2B"));
                    }

                    Toast.makeText(MainActivity.this, "Enter all details", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}