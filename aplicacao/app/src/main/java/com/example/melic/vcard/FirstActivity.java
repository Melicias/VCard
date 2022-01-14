package com.example.melic.vcard;

import android.content.Intent;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.melic.vcard.Models.User;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FirstActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                User user = new User();
                user = user.getUserFromFile(getBaseContext());
                if(user.getAuth_key() != null){
                    //ir para o login
                    Intent intent=new Intent(getBaseContext(),LoginActivity.class);
                    startActivity(intent);
                }else{
                    //ir para o registo
                    Intent intent=new Intent(getBaseContext(),RegisterActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        },SPLASH_TIME_OUT);
    }
}
