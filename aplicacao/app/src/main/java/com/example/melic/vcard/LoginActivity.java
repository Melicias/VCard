package com.example.melic.vcard;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.melic.vcard.Models.NetStatus;
import com.example.melic.vcard.Models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    Button btLogin;
    TextView tvRegistar,tvError,tvSucessoDelete;
    EditText etNrTelemovel, etPassword;

    private static String URL = "login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.tvRegistar = (TextView) findViewById(R.id.tvRegistar);
        this.tvError = (TextView) findViewById(R.id.tvErros);
        this.tvSucessoDelete = (TextView) findViewById(R.id.tvSucessoDelete);
        this.btLogin = (Button) findViewById(R.id.btLogin);
        this.etNrTelemovel = (EditText) findViewById(R.id.etNrTelemovelLogin);
        this.etPassword = (EditText) findViewById(R.id.etPasswordLogin);

        String[] permission = {
                Manifest.permission.READ_PHONE_NUMBERS
        };
        requestPermissions(permission, 102);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

        }else{
            TelephonyManager tMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            this.etNrTelemovel.setText(tMgr.getLine1Number());
        }

        this.tvRegistar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //CRIAR A ATIVIDADE E INICIAR
                tvSucessoDelete.setText("");
                Intent i = new Intent(getBaseContext(), RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });

        this.btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvSucessoDelete.setText("");
                tvError.setText("");
                if (checkValues()) {
                    if(NetStatus.getInstance(getApplicationContext()).isOnline()) {
                        efetuarLogin(etNrTelemovel.getText().toString(),etPassword.getText().toString());
                    }else{
                        tvError.setText("There is no internet connection!");
                    }
                }
            }
        });
        if (getIntent().hasExtra("sucesso")) {
            this.tvSucessoDelete.setText(getIntent().getStringExtra("sucesso"));
        }else{
            checkForLogin();
        }
    }

    public boolean checkValues() {
        //check if email is nto null and if it is an email
        if (this.etNrTelemovel.getText().toString() != null && this.etNrTelemovel.getText().length() == 9) {
            if (this.etPassword.getText().toString() != null || !this.etPassword.getText().toString().isEmpty() || this.etNrTelemovel.getText().length() < 8) {
                //dados inseridos
                return true;
            } else {
                tvError.setText("The password needs to be 8 digits long!");
            }
        } else {
            tvError.setText("The phone number must be 9 digits long!");
        }
        return false;
    }

    public void efetuarLogin(String nrTelemovel, String password) {
        final ConstraintLayout cl = (ConstraintLayout)findViewById(R.id.clLogin);
        try {
            cl.setEnabled(false);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("phone_number", nrTelemovel);
            jsonBody.put("password", password);
            RequestQueue requestQueue = Volley.newRequestQueue(getApplication());
            JsonObjectRequest jsonObject = new JsonObjectRequest(
                    Request.Method.POST,
                    getResources().getString(R.string.url) + URL,
                    jsonBody,
                    new Response.Listener<JSONObject>() {
                        public void onResponse(JSONObject response) {
                            try {
                                if(response.isNull("message")) {
                                    if (!response.isNull("access_token")) {
                                        login(response,true);
                                    } else {
                                        tvError.setText(response.getString("Login failed"));
                                    }
                                }else{
                                    tvError.setText(response.getString("message"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                tvError.setText("Something when wrong");
                            } catch (Exception e) {
                                e.printStackTrace();
                                tvError.setText("Something when wrong");
                            }
                            cl.setEnabled(true);
                        }
                    }, new Response.ErrorListener() {
                public void onErrorResponse(VolleyError error) {
                    //algum erro, por exemplo cena
                    cl.setEnabled(true);
                    if(error.networkResponse.statusCode == 401){
                        tvError.setText("Phone/Password not correct");
                    }else{
                        tvError.setText("Something when wrong, with the API");
                    }
                }
            });
            requestQueue.add(jsonObject);
        } catch (JSONException e) {
            cl.setEnabled(true);
            tvError.setText("Something when wrong, try again later!");
        }
    }

    public void checkForLogin(){
        User user = new User();
        user = user.getUserFromFile(this);
        if(user.getAuth_key() != null){
            final String auth_key = user.getAuth_key();
            this.etNrTelemovel.setText(user.getNrTelemovel().toString());
            //pedido a bd e login
            final ConstraintLayout cl = (ConstraintLayout)findViewById(R.id.clLogin);
            cl.setEnabled(false);
            RequestQueue requestQueue = Volley.newRequestQueue(getApplication());
            String url = getResources().getString(R.string.url) + "vcards/validatetoken";
            JsonObjectRequest jsonObject = new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    new Response.Listener<JSONObject>() {
                        public void onResponse(JSONObject response) {
                            if (!response.isNull("vcard")) {
                                login(response,false);
                            } else {
                                tvError.setText("Token invalid, login again please");
                            }
                            cl.setEnabled(true);
                        }
                    }, new Response.ErrorListener() {
                public void onErrorResponse(VolleyError error) {
                    //algum erro, por exemplo cena
                    cl.setEnabled(true);
                    tvError.setText("Login timeout");
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

    public void login(JSONObject response, boolean accessToken){
        try {
            if(accessToken){
                User user = new User(response.getString("access_token"),response.getJSONObject("vcard").getLong("phone_number"));
                user.saveUserInFile(getApplicationContext());
            }
            //CHAMAR A ATIVIDADE DE INDEX
            Intent Index = new Intent(LoginActivity.this,MenuActivity.class);
            Index.putExtra("response_string",response.toString());
            startActivity(Index);
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
            tvError.setText("Some problem with the API");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
