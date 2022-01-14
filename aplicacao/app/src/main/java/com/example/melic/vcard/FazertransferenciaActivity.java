package com.example.melic.vcard;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.melic.vcard.Models.Contact;
import com.example.melic.vcard.Models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FazertransferenciaActivity extends AppCompatActivity {

    Contact contacto;
    Button btVoltar, btEnviar;
    TextView tvErros, tvNrContacto, tvNomeContacto, tvSaldo;
    EditText etMontante, etPinCode;
    JSONObject vcard;
    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fazertransferencia);

        this.btVoltar = (Button) findViewById(R.id.btVoltarFazerTrans);
        this.btEnviar = (Button) findViewById(R.id.btSend);

        this.tvErros = (TextView) findViewById(R.id.tvErrosFazerTrans);
        this.tvNrContacto = (TextView) findViewById(R.id.tvNrContacto);
        this.tvNomeContacto = (TextView) findViewById(R.id.tvNomeContacto);
        this.tvSaldo = (TextView) findViewById(R.id.tvSaldo);

        this.etMontante = (EditText) findViewById(R.id.etMontante);
        this.etPinCode = (EditText) findViewById(R.id.etPinCode);

        getUserInfo();

        getCategories();

        if (getIntent().hasExtra("contacto")) {
            this.contacto = (Contact) (getIntent().getSerializableExtra("contacto")); //this.contacto.getPhoneNumber();
            tvNrContacto.setText(
                    (this.contacto.getPhoneNumber().charAt(0) == '+')
                            ? this.contacto.getPhoneNumber().substring(4)
                            : this.contacto.getPhoneNumber());
            tvNomeContacto.setText(this.contacto.getName());
            //loadInfo(response);
        }else{
           tvErros.setText("Some problem with the contact.");
        }


        this.btVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        this.btEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //cenas a fazer quando clicar no enviar
                createTransaction(etMontante.getText().toString(), tvNrContacto.getText().toString(), etPinCode.getText().toString());
            }
        });
    }

    private void createTransaction(String montante, String numeroRecebedor, String code) {
        User user = new User();
        user = user.getUserFromFile(this);
        final String auth_key = user.getAuth_key();
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("payment_type_code", "VCARD");
            jsonBody.put("phone_number", vcard.getString("phone_number"));
            jsonBody.put("value", montante);
            jsonBody.put("payment_reference", numeroRecebedor);
            jsonBody.put("confirmation_code", code);
            RequestQueue requestQueue = Volley.newRequestQueue(getApplication());
            JsonObjectRequest jsonObject = new JsonObjectRequest(
                    Request.Method.POST,
                    getResources().getString(R.string.url) + "transactions/mobile",
                    jsonBody,
                    new Response.Listener<JSONObject>() {
                        public void onResponse(JSONObject response) {
                            try {
                                double montate = response.getDouble("value");
                                double dado = montate-Math.round(montate);
                                if(response.getDouble("new_balance") > dado){
                                    Toast.makeText(FazertransferenciaActivity.this,"Added to PG:"+dado+"€ Money sent!", Toast.LENGTH_SHORT).show();
                                    goToMenu("Added to PG:"+dado+"€ Money sent!");
                                }else {
                                    Toast.makeText(FazertransferenciaActivity.this, "Money sent!", Toast.LENGTH_SHORT).show();
                                    goToMenu("Money sent!");
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                tvErros.setText("Was not possible to send the money.");
                            }
                        }
                    }, new Response.ErrorListener() {
                public void onErrorResponse(VolleyError error) {
                    try {
                        JSONObject response = new JSONObject(new String(error.networkResponse.data));
                        if(!response.isNull("errors")) {
                            JSONObject errors = response.getJSONObject("errors");
                            if(!errors.isNull("payment_reference")) {
                                tvErros.setText(errors.getJSONArray("payment_reference").getString(0));
                            }else{
                                if(!errors.isNull("value"))
                                    tvErros.setText(errors.getJSONArray("value").getString(0));
                                else
                                    tvErros.setText("Some error happened, dont know what ¯\\_(ツ)_/¯");
                            }
                        }else{
                            if(!response.isNull("message_confirmation_code")) {
                                tvErros.setText(response.getString("message_confirmation_code"));
                            }else{
                                tvErros.setText("Was not possible to send the money.");
                            }
                        }
                    } catch (JSONException e) {
                        tvErros.setText("Something went wrong with in the Server");
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
            Toast.makeText(FazertransferenciaActivity.this, "Something went wrong with in the Server", Toast.LENGTH_SHORT).show();
            tvErros.setText("Something went wrong, Try again later!");
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
                                tvErros.setText("Something Whent Wrong with the request");
                            }
                        }
                    }, new Response.ErrorListener() {
                public void onErrorResponse(VolleyError error) {
                    //algum erro, por exemplo cena
                    tvErros.setText("Something Whent Wrong with the request");
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

    public void getCategories(){
        User user = new User();
        user = user.getUserFromFile(this);
        if(user.getAuth_key() != null) {
            final String auth_key = user.getAuth_key();
            RequestQueue requestQueue = Volley.newRequestQueue(getApplication());
            String categoriesUrl = getResources().getString(R.string.url) + "vcards/categories";
            JsonArrayRequest jsonCategoriesUrlObject = new JsonArrayRequest(
                    Request.Method.GET,
                    categoriesUrl,
                    null,
                    new Response.Listener<JSONArray>() {
                        public void onResponse(JSONArray response) {
                            try {
                                JSONObject json_data = response.getJSONObject(0);
                                if (!json_data.isNull("id")) {
                                    loadCategoryInfo(json_data);
                                } else {
                                    tvErros.setText("Something Went Wrong with the request");
                                }
                            } catch (Exception e){
                                tvErros.setText("Something Went Wrong with the request");
                            }

                        }
                    }, new Response.ErrorListener() {
                public void onErrorResponse(VolleyError error) {
                    //algum erro, por exemplo cena
                    tvErros.setText("Something Whent Wrong with the request");
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + auth_key);
                    return headers;
                }
            };
            requestQueue.add(jsonCategoriesUrlObject);
        }
    }

    public void loadInfo(JSONObject response){
        try {
            vcard = response.getJSONObject("vcard");
            tvSaldo.setText(vcard.getString("balance") + "€");
        }catch (JSONException e){
            e.getMessage();
            tvErros.setText("Something Whent Wrong with the request");
        }
    }

    public void loadCategoryInfo(JSONObject response){
        try {
            category = response.getString("id");
        }catch (JSONException e){
            e.getMessage();
            tvErros.setText("Something Whent Wrong with the request");
        }
    }

    private void goToMenu(String msg){
        Intent i = new Intent(getBaseContext(), MenuActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra("sucesso", msg);
        startActivity(i);
        finish();
    }

}
