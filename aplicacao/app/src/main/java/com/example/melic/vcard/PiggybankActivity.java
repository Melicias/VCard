package com.example.melic.vcard;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.melic.vcard.Models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PiggybankActivity extends AppCompatActivity {

    Button btAdd, btRemove, btVoltar;
    TextView tvBalance, tvPiggyBalance, tvError;
    EditText etNum;
    double balance,piggy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piggybank);

        this.btAdd = (Button) findViewById(R.id.btAdicionarFundos);
        this.btRemove = (Button) findViewById(R.id.btRemoverFundos);
        this.btVoltar = (Button) findViewById(R.id.btVoltarPiggy);
        this.tvBalance = (TextView) findViewById(R.id.tvBalancePiggy);
        this.tvPiggyBalance = (TextView) findViewById(R.id.tvPiggyBalance);
        this.tvError = (TextView) findViewById(R.id.tvErrosPiggy);
        this.etNum = (EditText) findViewById(R.id.etNumPiggyBank);
        this.balance = 0;
        this.piggy = 0;

        this.btVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        this.btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvError.setText("");
                if(etNum.getText().toString() != null){
                    try {
                        double num = Double.parseDouble(etNum.getText().toString());
                        if(num != 0){
                            if (balance < num) {
                                tvError.setText("Your balance should be above the amount written");
                                tvError.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRed));
                            } else {
                                addFunds(num);
                            }
                        }else{
                            tvError.setText("The ammount should be higher than 0");
                            tvError.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRed));
                        }
                    }catch (Exception e){
                        tvError.setText("Write the amount you want to add/remove");
                        tvError.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRed));
                    }
                }else{
                    tvError.setText("Write the amount you want to add/remove");
                    tvError.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRed));
                }

            }
        });

        this.btRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvError.setText("");
                if(etNum.getText().toString() != null){
                    try {
                        double num = Double.parseDouble(etNum.getText().toString());
                        if(num != 0){
                            if (piggy < num) {
                                tvError.setText("Your piggy bank balance should be above the amount");
                                tvError.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRed));
                            } else {
                                removeFunds(num);
                            }
                        }else{
                            tvError.setText("The ammount should be higher than 0");
                            tvError.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRed));
                        }
                    }catch (Exception e){
                        tvError.setText("Write the amount you want to add/remove");
                        tvError.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRed));
                    }
                }else{
                    tvError.setText("Write the amount you want to add/remove");
                    tvError.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRed));
                }
            }
        });

        getUserInfo();
    }

    public void addFunds(double amount){
        User user = new User();
        user = user.getUserFromFile(this);
        final String auth_key = user.getAuth_key();
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("amount", amount);
            RequestQueue requestQueue = Volley.newRequestQueue(getApplication());
            JsonObjectRequest jsonObject = new JsonObjectRequest(
                    Request.Method.POST,
                    getResources().getString(R.string.url) + "piggybank/add",
                    jsonBody,
                    new Response.Listener<JSONObject>() {
                        public void onResponse(JSONObject response) {
                            try {
                                DecimalFormat df = new DecimalFormat("#.##");
                                balance = response.getDouble("balance");
                                tvBalance.setText(df.format(balance) + "€");
                                piggy = response.getDouble("piggy_bank");
                                tvPiggyBalance.setText(df.format(piggy) + "€");

                                tvError.setText("Money added to piggy bank!");
                                tvError.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorGreen));
                            } catch (Exception e) {
                                e.printStackTrace();
                                tvError.setText("ERRO");
                            }
                        }
                    }, new Response.ErrorListener() {
                public void onErrorResponse(VolleyError error) {
                    try {
                        JSONObject response = new JSONObject(new String(error.networkResponse.data));
                        if(!response.isNull("error")) {
                            tvError.setText(response.getString("error"));
                        }else{
                            tvError.setText("Some error happened, dont know what ¯\\_(ツ)_/¯");
                        }
                    } catch (JSONException e) {
                        tvError.setText("Something went wrong with in the Server");
                    }
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", "Bearer " + auth_key);
                    headers.put("Accept", "application/json");
                    return headers;
                }
            };
            requestQueue.add(jsonObject);
        } catch (JSONException e) {

        }
    }

    public void removeFunds(double amount){
        User user = new User();
        user = user.getUserFromFile(this);
        final String auth_key = user.getAuth_key();
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("amount", amount);
            RequestQueue requestQueue = Volley.newRequestQueue(getApplication());
            JsonObjectRequest jsonObject = new JsonObjectRequest(
                    Request.Method.POST,
                    getResources().getString(R.string.url) + "piggybank/remove",
                    jsonBody,
                    new Response.Listener<JSONObject>() {
                        public void onResponse(JSONObject response) {
                            try {
                                DecimalFormat df = new DecimalFormat("#.##");
                                balance = response.getDouble("balance");
                                tvBalance.setText(df.format(balance) + "€");
                                piggy = response.getDouble("piggy_bank");
                                tvPiggyBalance.setText(df.format(piggy) + "€");

                                tvError.setText("Money added to Balance!");
                                tvError.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorGreen));
                            } catch (Exception e) {
                                e.printStackTrace();
                                tvError.setText("ERRO");
                            }
                        }
                    }, new Response.ErrorListener() {
                public void onErrorResponse(VolleyError error) {
                    try {
                        JSONObject response = new JSONObject(new String(error.networkResponse.data));
                        if(!response.isNull("error")) {
                            tvError.setText(response.getString("error"));
                        }else{
                            tvError.setText("Some error happened, dont know what ¯\\_(ツ)_/¯");
                        }
                    } catch (JSONException e) {
                        tvError.setText("Something went wrong with in the Server");
                    }
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", "Bearer " + auth_key);
                    headers.put("Accept", "application/json");
                    return headers;
                }
            };
            requestQueue.add(jsonObject);
        } catch (JSONException e) {

        }
    }

    public void getUserInfo(){
        User user = new User();
        user = user.getUserFromFile(this);
        if(user.getAuth_key() != null){
            final String auth_key = user.getAuth_key();
            RequestQueue requestQueue = Volley.newRequestQueue(getApplication());
            String url = getResources().getString(R.string.url) + "vcards/validatetoken";
            JsonObjectRequest jsonObject = new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    new Response.Listener<JSONObject>() {
                        public void onResponse(JSONObject response) {
                            if (!response.isNull("vcard")) {
                                loadInfo(response);
                            } else {
                                tvError.setText("Token invalid, login again please");
                                tvError.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRed));
                            }
                        }
                    }, new Response.ErrorListener() {
                public void onErrorResponse(VolleyError error) {
                    //algum erro, por exemplo cena
                    tvError.setText("Login timeout");
                    tvError.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRed));
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + auth_key);
                    return headers;
                }
            };
            requestQueue.add(jsonObject);
        }
    }

    public void loadInfo(JSONObject response){
        try {
            tvBalance.setText(response.getJSONObject("vcard").getString("balance") + "€");
            tvPiggyBalance.setText(response.getJSONObject("vcard").getString("piggy_bank") + "€");

            this.balance = response.getJSONObject("vcard").getDouble("balance");
            this.piggy = response.getJSONObject("vcard").getDouble("piggy_bank");
        }catch (JSONException e){
            e.getMessage();
        }
    }
}
