package com.example.melic.vcard;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.melic.vcard.Adapters.Contactos_adapter;
import com.example.melic.vcard.Models.Contact;
import com.example.melic.vcard.Models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MostrarcontactosActivity extends AppCompatActivity {

    Button btVoltarMostrarContactos;
    ImageButton btAddContact;
    TextView tvError;
    EditText etPesquisar;
    private RecyclerView rvContactos;
    ArrayList<Contact> contactos;
    Contactos_adapter AdaptadorContactos;


    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrarcontactos);

        this.btVoltarMostrarContactos = (Button) findViewById(R.id.btVoltarMostrarContactos);
        this.rvContactos =(RecyclerView)findViewById(R.id.rvContactos);
        this.etPesquisar = (EditText) findViewById(R.id.etPesquisar);
        this.tvError = (TextView) findViewById(R.id.tvErrosMostrarContactos);
        this.btAddContact = (ImageButton) findViewById(R.id.btAddContact);
        contactos = new ArrayList<>();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //carregar contactos
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            contactos = getContacts(getApplicationContext());
            getListFromServer(contactos);
        }

        this.btVoltarMostrarContactos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        this.btAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), CreatecontactActivity.class);
                startActivity(i);
            }
        });

        this.etPesquisar.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                //filtro aqui
                AdaptadorContactos.filter(s.toString().toLowerCase());
                AdaptadorContactos.notifyDataSetChanged();
            }
        });

    }

    public ArrayList<Contact> getContacts(Context context) {
        ArrayList<Contact> list = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor cursorInfo = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(),
                            ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(id)));

                    Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(id));
                    Uri pURI = Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);

                    Bitmap photo = null;
                    if (inputStream != null) {
                        photo = BitmapFactory.decodeStream(inputStream);
                    }
                    while (cursorInfo.moveToNext()) {
                        Contact info = new Contact(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)),
                                                    cursorInfo.getString(cursorInfo.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)),
                                                    photo);
                        list.add(info);

                    }
                    cursorInfo.close();
                }
            }
            cursor.close();
        }
        return list;
    }

    private void getListFromServer(ArrayList<Contact> contactos){
        User user = new User();
        user = user.getUserFromFile(this);
        if(user.getAuth_key() != null) {
            final String auth_key = user.getAuth_key();
            RequestQueue requestQueue = Volley.newRequestQueue(getApplication());
            String categoriesUrl = getResources().getString(R.string.url) + "vcards/contactslist";
            JSONArray jr = new JSONArray();
            for (int x = 0; x < contactos.size(); x++)
            {
                JSONObject obj = new JSONObject();
                try {
                    obj.put("phoneNumber", contactos.get(x).getPhoneNumber());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jr.put(obj);

            }
            JsonArrayRequest jsonCategoriesUrlObject = new JsonArrayRequest(
                    Request.Method.POST,
                    categoriesUrl,
                    jr,
                    new Response.Listener<JSONArray>() {
                        public void onResponse(JSONArray response) {
                            try {
                                readResponse(response);
                            } catch (Exception e){
                                setAdapter();
                            }
                        }
                    }, new Response.ErrorListener() {
                public void onErrorResponse(VolleyError error) {
                    //algum erro, por exemplo cena
                    setAdapter();
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

    private void setAdapter(){
        AdaptadorContactos = new Contactos_adapter(contactos);
        rvContactos.setAdapter(AdaptadorContactos);
        rvContactos.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    private void readResponse(JSONArray response){
        for (int i=0; i < response.length(); i++) {
            try{
                contactos.get(i).setPhoneNumber(response.getJSONObject(i).getString("phoneNumber"));
                contactos.get(i).setVcard(response.getJSONObject(i).getInt("vcard") == 1 ? true : false);
            }catch (Exception e){

            }

        }
        setAdapter();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                contactos = getContacts(getApplicationContext());
                AdaptadorContactos = new Contactos_adapter(contactos);
                rvContactos.setAdapter(AdaptadorContactos);
                rvContactos.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            } else {
                this.tvError.setText("Until you grant the permission, we canot display the names");
            }
        }
    }




}
