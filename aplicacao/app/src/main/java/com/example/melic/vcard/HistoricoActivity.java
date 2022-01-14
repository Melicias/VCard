package com.example.melic.vcard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.melic.vcard.Adapters.Transacoes_adapter;
import com.example.melic.vcard.Models.NetStatus;
import com.example.melic.vcard.Models.Transacao;
import com.example.melic.vcard.Models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HistoricoActivity extends AppCompatActivity {

    Button btVoltar;
    private RecyclerView rvTransacoes;
    ArrayList<Transacao> transacoes;
    ImageButton btOrdenarData,btOrdenarValor;
    CheckBox cbCredito, cbDebito;

    Transacoes_adapter AdaptadorTreinos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);

        this.btVoltar = (Button) findViewById(R.id.btVoltar);
        this.rvTransacoes =(RecyclerView)findViewById(R.id.rvTransacoes);
        this.btOrdenarData = (ImageButton) findViewById(R.id.btOrdenarData);
        this.btOrdenarData.setTag("up");
        this.btOrdenarValor = (ImageButton) findViewById(R.id.btOrdenarValor);
        this.btOrdenarValor.setTag("up");
        this.cbCredito = (CheckBox) findViewById(R.id.cbCredito);
        this.cbDebito = (CheckBox) findViewById(R.id.cbDebito);

        transacoes = new ArrayList<>();

        if(NetStatus.getInstance(getApplicationContext()).isOnline()) {
            getUserTransactions();
        }else{
            //tvError.setText("There is no internet connection!");
        }

        this.btVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        this.cbDebito.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
              @Override
              public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                  if(isChecked && cbCredito.isChecked()){
                      AdaptadorTreinos.reset();
                      return;
                  }
                  if(!isChecked && !cbCredito.isChecked()){
                      AdaptadorTreinos.filterByType("");
                  }else{
                      if(!isChecked){
                          AdaptadorTreinos.filterByType("c");
                      }else{
                          if(isChecked){
                              AdaptadorTreinos.filterByType("d");
                          }else{
                              AdaptadorTreinos.reset();
                          }
                      }
                  }
              }
        });

        this.cbCredito.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(isChecked && cbCredito.isChecked()){
                    AdaptadorTreinos.reset();
                    return;
                }
                if(!isChecked && !cbCredito.isChecked()){
                    AdaptadorTreinos.filterByType("");
                }else{
                    if(!isChecked){
                        AdaptadorTreinos.filterByType("d");
                    }else{
                        if(isChecked){
                            AdaptadorTreinos.filterByType("c");
                        }else{
                            AdaptadorTreinos.reset();
                        }
                    }
                }
            }
        });

        this.btOrdenarData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btOrdenarData.getTag() == "up"){
                    btOrdenarData.setImageResource(R.drawable.down);
                    btOrdenarData.setTag("down");
                    AdaptadorTreinos.orderByData(0);
                    AdaptadorTreinos.notifyDataSetChanged();
                }else{
                    btOrdenarData.setImageResource(R.drawable.up);
                    btOrdenarData.setTag("up");
                    AdaptadorTreinos.orderByData(1);
                    AdaptadorTreinos.notifyDataSetChanged();
                }
            }
        });

        this.btOrdenarValor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btOrdenarValor.getTag() == "up"){
                    btOrdenarValor.setImageResource(R.drawable.down);
                    btOrdenarValor.setTag("down");
                    AdaptadorTreinos.orderByValue(0);
                    AdaptadorTreinos.notifyDataSetChanged();
                }else{
                    btOrdenarValor.setImageResource(R.drawable.up);
                    btOrdenarValor.setTag("up");
                    AdaptadorTreinos.orderByValue(1);
                    AdaptadorTreinos.notifyDataSetChanged();
                }
            }
        });

    }

    public void getUserTransactions(){
        User user = new User();
        user = user.getUserFromFile(this);
        if(user.getAuth_key() != null){
            final String auth_key = user.getAuth_key();
            RequestQueue requestQueue = Volley.newRequestQueue(getApplication());
            String url = getResources().getString(R.string.url) + "vcards/transactions";
            JsonObjectRequest jsonObject = new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    new Response.Listener<JSONObject>() {
                        public void onResponse(JSONObject respon) {
                            JSONArray response = null;
                            try {
                                response = respon.getJSONArray("data");
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");
                                for(int i=0; i<response.length(); i++){
                                    try {
                                        JSONObject json_data = response.getJSONObject(i);
                                        //String type, String vcard,String valor, Date date
                                        try {
                                            Date date = format.parse(json_data.getString("datetime"));
                                            Transacao transacao = new Transacao(json_data.getString("type"),json_data.getString("payment_reference"),
                                                                                json_data.getString("value"),date);
                                            addTransacao(transacao);
                                        } catch (ParseException e) {
                                            //erorr skip
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            setRecycler();
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

    private void addTransacao(Transacao transacao){
        this.transacoes.add(transacao);
    }

    private void setRecycler(){
        AdaptadorTreinos = new Transacoes_adapter(transacoes);
        rvTransacoes.setAdapter(AdaptadorTreinos);
        rvTransacoes.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }
}
