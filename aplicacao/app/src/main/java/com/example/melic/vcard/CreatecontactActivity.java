package com.example.melic.vcard;

import android.Manifest;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.melic.vcard.Adapters.Contactos_adapter;
import com.example.melic.vcard.Models.Contact;

public class CreatecontactActivity extends AppCompatActivity {

    Button btCriarContacto,btVoltarCriarContacto;
    TextView tvErros;
    EditText etNomeCriarContacto, etNrTelemovelCriar;

    private static final int PERMISSIONS_REQUEST_WRITE_CONTACTS = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createcontact);

        this.btCriarContacto = (Button) findViewById(R.id.btCriarContacto);
        this.btVoltarCriarContacto = (Button) findViewById(R.id.btVoltarCriarContacto);

        this.etNomeCriarContacto = (EditText) findViewById(R.id.etNomeCriarContacto);
        this.etNrTelemovelCriar = (EditText) findViewById(R.id.etNrTelemovelCriar);
        this.tvErros = (TextView) findViewById(R.id.tvErros);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS}, PERMISSIONS_REQUEST_WRITE_CONTACTS);//PERMISSIONS_REQUEST_READ_CONTACTS
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            //temos permissao
        }

        this.btCriarContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvErros.setText("");
                if(etNrTelemovelCriar.getText().length() == 9){
                    if(etNomeCriarContacto.getText().length() > 2){
                        Contact contact = addContact(etNomeCriarContacto.getText().toString(), etNrTelemovelCriar.getText().toString());
                        if(contact != null){
                            Intent i = new Intent(getBaseContext(), FazertransferenciaActivity.class);
                            i.putExtra("contacto",contact);
                            finish();
                            startActivity(i);
                        }
                    }else{
                        tvErros.setText("The name must have 3 letters or bigger!");
                    }
                }else{
                    tvErros.setText("The phone number must have 9 digits!");
                }
            }
        });


        this.btVoltarCriarContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private Contact addContact(String name, String phone) {
        try{
            // Inser an empty contact.
            ContentValues contentValues = new ContentValues();
            Uri rawContactUri = getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, contentValues);
            // Get the newly created contact raw id.
            long ret = ContentUris.parseId(rawContactUri);

            rawContactUri = ContactsContract.Data.CONTENT_URI;

            ContentValues contentValuesName = new ContentValues();
            contentValuesName.put(ContactsContract.Data.RAW_CONTACT_ID, ret);
            // Each contact must has an mime type to avoid java.lang.IllegalArgumentException: mimetype is required error.
            contentValuesName.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
            // Put contact display name value.
            contentValuesName.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, name);
            getContentResolver().insert(rawContactUri, contentValuesName);


            // Create a ContentValues object.
            ContentValues contentValuesNumber = new ContentValues();
            // Each contact must has an id to avoid java.lang.IllegalArgumentException: raw_contact_id is required error.
            contentValuesNumber.put(ContactsContract.Data.RAW_CONTACT_ID, ret);
            // Each contact must have a mime type to avoid java.lang.IllegalArgumentException: mimetype is required error.
            contentValuesNumber.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
            // Put phone number value.
            contentValuesNumber.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phone);
            // Insert new contact data into phone contact list.
            getContentResolver().insert(rawContactUri, contentValuesNumber);
            return new Contact(name,phone,null);
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_WRITE_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
            } else {
                finish();
            }
        }
    }


}
