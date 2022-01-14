package com.example.melic.vcard;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
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
import com.example.melic.vcard.Models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MenuActivity extends AppCompatActivity {

    Button btHistorico, btEnviarDinheiro, btPiggyBank, btNotificacoes, btApagarVcard;
    TextView tvSucesso, tvApagarVcard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        this.btHistorico = (Button) findViewById(R.id.btHistorico);
        this.btApagarVcard = (Button) findViewById(R.id.btApagarVcard);
        this.btEnviarDinheiro = (Button) findViewById(R.id.btEnviarDinheiro);
        this.btPiggyBank = (Button) findViewById(R.id.btPiggyBank);
        this.tvSucesso = (TextView) findViewById(R.id.tvSucesso);
        this.tvApagarVcard = (TextView) findViewById(R.id.tvApagarVcard);

        if (getIntent().hasExtra("sucesso")) {
            this.tvSucesso.setText(getIntent().getStringExtra("sucesso"));
        }

        try {
            if (getIntent().hasExtra("response_string")) {
                JSONObject response = new JSONObject(getIntent().getStringExtra("response_string"));
                loadInfo(response);
            }else{
                getUserInfo();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.btHistorico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent historico = new Intent(MenuActivity.this,HistoricoActivity.class);
                startActivity(historico);
            }
        });

        this.btEnviarDinheiro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent enviarDinheiro = new Intent(MenuActivity.this, MostrarcontactosActivity.class);
                startActivity(enviarDinheiro);
            }
        });

        this.btApagarVcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvApagarVcard.setText("");
                showDialogApagarVcard();
            }
        });

        this.btPiggyBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent piggybank = new Intent(MenuActivity.this, PiggybankActivity.class);
                startActivity(piggybank);
            }
        });


    }

    @Override
    public void onResume(){
        super.onResume();
        getUserInfo();
    }

    private void apagarVcard(String password){
        if(!password.isEmpty() || password.length() >= 8){
            try {
                User user = new User();
                user = user.getUserFromFile(this);
                if(user.getAuth_key() != null) {
                    final String auth_key = user.getAuth_key();
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("password", password);
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplication());
                    String url = getResources().getString(R.string.url) + "vcards/deletemobile";
                    JsonObjectRequest jsonObject = new JsonObjectRequest(
                            Request.Method.POST,
                            url,
                            jsonBody,
                            new Response.Listener<JSONObject>() {
                                public void onResponse(JSONObject response) {
                                    Intent i = new Intent(getBaseContext(), LoginActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    i.putExtra("sucesso", "Vcard deleted with success");
                                    startActivity(i);
                                    finish();
                                }
                            }, new Response.ErrorListener() {
                        public void onErrorResponse(VolleyError error) {
                            //algum erro, por exemplo cena
                            try {
                                JSONObject response = new JSONObject(new String(error.networkResponse.data));
                                if(!response.isNull("error")) {
                                    tvApagarVcard.setText(response.getString("error"));
                                }else{
                                    tvApagarVcard.setText("Some error happen");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }) {
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
                }
            }catch(Exception e){

            }
        }else{
            this.tvApagarVcard.setText("The passowrd is not correct");
        }
    }

    public void showDialogApagarVcard()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Delete Vcard");
        alert.setMessage("You need to confirm the password");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        alert.setView(input);

        alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                apagarVcard(input.getText().toString());
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
        alert.show();
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
                                //tvError.setText("Token invalid, login again please");
                            }
                        }
                    }, new Response.ErrorListener() {
                public void onErrorResponse(VolleyError error) {
                    //algum erro, por exemplo cena
                    //tvError.setText("Login timeout");
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
        TextView tvBalance = (TextView) findViewById(R.id.tvBalance);
        TextView tvPiggyBank = (TextView) findViewById(R.id.tvPiggyBank);
        try {
            tvBalance.setText(response.getJSONObject("vcard").getString("balance") + "€");
            tvPiggyBank.setText(response.getJSONObject("vcard").getString("piggy_bank") + "€");

            View viewLastTrans = findViewById(R.id.viewLastTrans);
            if(!response.isNull("last_transaction")){
                viewLastTrans.setVisibility(View.VISIBLE);
                TextView tvDeParaQuem = (TextView) viewLastTrans.findViewById(R.id.tvContacto);
                TextView tvValorTransferencia = (TextView) viewLastTrans.findViewById(R.id.tvNrTelemovel);
                TextView tvData = (TextView) viewLastTrans.findViewById(R.id.tvData);
                JSONObject transferencia = response.getJSONObject("last_transaction");
                if(transferencia.getString("type").toLowerCase().compareTo("d") == 0){
                    tvDeParaQuem.setText("To: " + (transferencia.isNull("pair_vcard") ? transferencia.getString("payment_reference") : transferencia.getString("pair_vcard")));
                    tvValorTransferencia.setText("Value: -" + transferencia.getString("value") + "€");
                }else{
                    tvDeParaQuem.setText("From: " + (transferencia.isNull("pair_vcard") ? transferencia.getString("payment_reference") : transferencia.getString("pair_vcard")));
                    tvValorTransferencia.setText("Value: +" + transferencia.getString("value") + "€");
                }
                //String teste = getContactDisplayNameByNumber(transferencia.getString("pair_vcard"));
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");
                try {
                    Date date = format.parse(transferencia.getString("datetime"));
                    SimpleDateFormat postFormat = new SimpleDateFormat("dd-MM-yyyy' as 'HH:mm");
                    tvData.setText("Date: " + postFormat.format(date));
                } catch (ParseException e) {
                    tvData.setText("Date: " + transferencia.getString("datetime"));
                }
            }else{
                viewLastTrans.setVisibility(View.INVISIBLE);
            }
        }catch (JSONException e){
            e.getMessage();
        }
    }

    public String getContactDisplayNameByNumber(String number) {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        String name = "?";

        ContentResolver contentResolver = getContentResolver();
        Cursor contactLookup = contentResolver.query(uri, new String[] {BaseColumns._ID,
                ContactsContract.PhoneLookup.DISPLAY_NAME }, null, null, null);

        try {
            if (contactLookup != null && contactLookup.getCount() > 0) {
                contactLookup.moveToNext();
                name = contactLookup.getString(contactLookup.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
                //String contactId = contactLookup.getString(contactLookup.getColumnIndex(BaseColumns._ID));
            }
        } finally {
            if (contactLookup != null) {
                contactLookup.close();
            }
        }

        return name;
    }
}
