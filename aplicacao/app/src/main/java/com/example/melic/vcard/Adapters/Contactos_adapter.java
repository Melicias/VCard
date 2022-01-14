package com.example.melic.vcard.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.melic.vcard.FazertransferenciaActivity;
import com.example.melic.vcard.Models.Contact;
import com.example.melic.vcard.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Contactos_adapter extends
        RecyclerView.Adapter<Contactos_adapter.ViewHolder>{

    private Context context;
    private ArrayList<Contact> contactos;
    private ArrayList<Contact> contactos_all;

    public Contactos_adapter(ArrayList<Contact> contactos) {
        this.contactos = contactos;
        this.contactos_all = contactos;
    }

    @NonNull
    @Override
    public Contactos_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View treinoView = inflater.inflate(R.layout.contact_card, viewGroup, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(treinoView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Contactos_adapter.ViewHolder viewHolder, int i) {
        final Contact contacto = this.contactos.get(i);

        TextView tvContacto = viewHolder.tvContacto;
        TextView tvNrTelemovel = viewHolder.tvNrTelemovel;
        ImageView ivLogotipo = viewHolder.ivLogotipo;
        CardView cardViewContacto = viewHolder.cardViewContacto;

        tvContacto.setText(contacto.getName());
        tvNrTelemovel.setText(contacto.getPhoneNumber());
        if(contacto.isVcard()){
            ivLogotipo.setVisibility(View.VISIBLE);
        }else{
            ivLogotipo.setVisibility(View.GONE);
        }
        cardViewContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //abrir uma nova cena
                System.out.println(contacto.getPhoneNumber());
                Intent i = new Intent(context, FazertransferenciaActivity.class);
                i.putExtra("contacto",contacto);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvContacto,tvNrTelemovel;
        public ImageView imageViewContact;
        public ImageView ivLogotipo;
        public CardView cardViewContacto;


        public ViewHolder(View itemView) {

            super(itemView);
            tvContacto = (TextView) itemView.findViewById(R.id.tvContacto);
            tvNrTelemovel = (TextView) itemView.findViewById(R.id.tvNrTelemovel);
            imageViewContact = (ImageView) itemView.findViewById(R.id.imageViewContact);
            ivLogotipo = (ImageView) itemView.findViewById(R.id.ivLogotipo);
            cardViewContacto = (CardView) itemView.findViewById(R.id.cardViewContacto);
        }
    }

    public void filter(String text){
        this.contactos = this.contactos_all;
        if(text == null){
            notifyDataSetChanged();
            return;
        }
        ArrayList<Contact> nova = new ArrayList<>();
        for(int i = 0;i<this.contactos.size();i++){
            if(this.contactos.get(i).getName().toLowerCase().contains(text)){
                nova.add(this.contactos.get(i));
            }
        }
        for(int i = 0;i<this.contactos.size();i++){
            if(this.contactos.get(i).getPhoneNumber() != null)
                if(this.contactos.get(i).getPhoneNumber().contains(text)){
                    nova.add(this.contactos.get(i));
                }
        }
        this.contactos = nova;
        notifyDataSetChanged();
    }


}
