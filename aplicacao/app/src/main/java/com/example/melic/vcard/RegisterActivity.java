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
import android.widget.Toast;

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
import org.w3c.dom.Text;

public class RegisterActivity extends AppCompatActivity {


    Button btRegistar;
    TextView tvEntrar, tvError;
    EditText etNrTelemovel, etPassword,etCode;

    private static String URL = "vcards";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String[] permission = {
                Manifest.permission.READ_PHONE_NUMBERS
        };
        requestPermissions(permission, 102);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.tvEntrar = (TextView) findViewById(R.id.tvEntrar);
        this.btRegistar = (Button) findViewById(R.id.btLogin);
        this.etNrTelemovel = (EditText) findViewById(R.id.etNrTelemovelLogin);
        this.etCode=(EditText) findViewById(R.id.etCode);
        this.etPassword = (EditText) findViewById(R.id.etPasswordLogin);
        this.tvError = (TextView) findViewById(R.id.tvErrosRegisto);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

        }else{
            TelephonyManager tMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            this.etNrTelemovel.setText(tMgr.getLine1Number());
        }

        this.tvEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //CRIAR A ATIVIDADE E INICIAR
                Intent i = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(i);
            }
        });
        this.btRegistar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvError.setText("");
                if (checkValues()) {
                    if(NetStatus.getInstance(getApplicationContext()).isOnline()) {
                        efetuarRegisto(etNrTelemovel.getText().toString(),etPassword.getText().toString(),etCode.getText().toString());
                    }else{
                        tvError.setText("There is no internet connection!");
                    }
                }
            }
        });
    }

    private void efetuarRegisto(String nrTelemovel, String password, String code) {
        final ConstraintLayout cl = (ConstraintLayout)findViewById(R.id.clRegister);
        try {
            cl.setEnabled(false);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("phone_number", nrTelemovel);
            jsonBody.put("name", nrTelemovel);
            jsonBody.put("email",nrTelemovel+"@mail.com");
            jsonBody.put("password", password);
            jsonBody.put("password_confirmation", password);
            jsonBody.put("confirmation_code", code);
            RequestQueue requestQueue = Volley.newRequestQueue(getApplication());
            JsonObjectRequest jsonObject = new JsonObjectRequest(
                    Request.Method.POST,
                    getResources().getString(R.string.url) + URL,
                    jsonBody,
                    new Response.Listener<JSONObject>() {
                        public void onResponse(JSONObject response) {
                            //save num file do user
                            //verificar a resposta!!!!
                            try {
                                if (!response.isNull("access_token")) {
                                    login(response);
                                } else {
                                    tvError.setText("Something when wrong with the token");
                                }
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
                    try {
                        JSONObject response = new JSONObject(new String(error.networkResponse.data));
                        if(!response.isNull("phone_number")) {
                            tvError.setText(response.getJSONArray("phone_number").getString(0));
                        }else{
                            if(!response.isNull("email")) {
                                tvError.setText(response.getJSONArray("email").getString(0));
                            }else{
                                tvError.setText("Something went wrong with the request!");
                            }
                        }
                    } catch (JSONException e) {
                        tvError.setText("Something when wrong, with the API");
                    }
                }
            });
            requestQueue.add(jsonObject);
        } catch (JSONException e) {
            cl.setEnabled(true);
            Toast.makeText(RegisterActivity.this, "Ocurreu algum erro, tente mais tarde de novo", Toast.LENGTH_SHORT).show();
            tvError.setText("Something when wrong, Try again later!");
        }
    }
    public boolean checkValues() {
        if (this.etNrTelemovel.getText().toString() != null && this.etNrTelemovel.getText().length() == 9) {
            if (this.etCode.getText().toString() == null || this.etCode.getText().length() != 4) {
                tvError.setText("The code has to have 4 digits");
                return false;
            }
            if (this.etPassword.getText().toString() == null || this.etPassword.getText().toString().isEmpty() || this.etPassword.getText().length() < 8) {
                tvError.setText("The password needs to be 8 digits long!");
                return false;
            }
            return true;
        } else {
            tvError.setText("The phone number must be 9 digits long!");
        }
        return false;
    }

    public void login(JSONObject response){
        try {
            User user = new User(response.getString("access_token"),response.getJSONObject("vcard").getLong("phone_number"));
            user.saveUserInFile(getApplicationContext());
            //CHAMAR A ATIVIDADE DE INDEX
            Intent Index = new Intent(RegisterActivity.this,MenuActivity.class);
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