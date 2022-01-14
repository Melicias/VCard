package com.example.melic.vcard.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.TextView;

import com.example.melic.vcard.Models.Transacao;
import com.example.melic.vcard.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Transacoes_adapter extends
        RecyclerView.Adapter<Transacoes_adapter.ViewHolder>{

    private Context context;
    private ArrayList<Transacao> transacoes;
    private ArrayList<Transacao> transacoes_all;

    public Transacoes_adapter(ArrayList<Transacao> transacoes) {
        Collections.reverse(transacoes);
        this.transacoes = transacoes;
        this.transacoes_all = transacoes;
    }

    @NonNull
    @Override
    public Transacoes_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View treinoView = inflater.inflate(R.layout.transaction_card, viewGroup, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(treinoView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Transacoes_adapter.ViewHolder viewHolder, int i) {
        final Transacao transacao = this.transacoes.get(i);

        TextView tvDeParaQuem = viewHolder.tvDeParaQuem;
        TextView tvValorTransferencia = viewHolder.tvValorTransferencia;
        TextView tvData = viewHolder.tvData;

        if(transacao.getType().toLowerCase().compareTo("d") == 0){
            tvDeParaQuem.setText("To: " + transacao.getVcard());
            tvValorTransferencia.setText("Value: -" + transacao.getValor() + "€");
        }else{
            tvDeParaQuem.setText("From: " + transacao.getVcard());
            tvValorTransferencia.setText("Valu: +" + transacao.getValor() + "€");
        }
        SimpleDateFormat postFormat = new SimpleDateFormat("dd-MM-yyyy' as 'HH:mm");
        tvData.setText("Date: " + postFormat.format(transacao.getDate()));
    }

    @Override
    public int getItemCount() {
        return transacoes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvDeParaQuem,tvValorTransferencia,tvData;


        public ViewHolder(View itemView) {

            super(itemView);
            tvDeParaQuem = (TextView) itemView.findViewById(R.id.tvContacto);
            tvValorTransferencia = (TextView) itemView.findViewById(R.id.tvNrTelemovel);
            tvData = (TextView) itemView.findViewById(R.id.tvData);
        }
    }

    public void orderByData(int option){
        if(option == 0){
            Collections.sort(transacoes, new Comparator<Transacao>() {
                public int compare(Transacao o1, Transacao o2) {
                    if (o1.getDate() == null || o2.getDate() == null)
                        return 0;
                    return o2.getDate().compareTo(o1.getDate());
                }
            });
        }else{
            Collections.sort(transacoes, new Comparator<Transacao>() {
                public int compare(Transacao o1, Transacao o2) {
                    if (o1.getDate() == null || o2.getDate() == null)
                        return 0;
                    return o1.getDate().compareTo(o2.getDate());
                }
            });
        }
        Collections.reverse(transacoes);
        notifyDataSetChanged();
    }

    public void orderByValue(int option){
        if(option == 0){
            Collections.sort(transacoes, new Comparator<Transacao>() {
                public int compare(Transacao o1, Transacao o2) {
                    if (o1.getDate() == null || o2.getDate() == null)
                        return 0;
                    if(Double.parseDouble(o1.getValor())<Double.parseDouble(o2.getValor()))
                        return -1;
                    else if(Double.parseDouble(o2.getValor())<Double.parseDouble(o1.getValor()))
                        return 1;
                    return 0;
                }
            });
        }else {
            Collections.sort(transacoes, new Comparator<Transacao>() {
                public int compare(Transacao o1, Transacao o2) {
                    if (o1.getDate() == null || o2.getDate() == null)
                        return 0;
                    if(Double.parseDouble(o1.getValor())>Double.parseDouble(o2.getValor()))
                        return -1;
                    else if(Double.parseDouble(o2.getValor())>Double.parseDouble(o1.getValor()))
                        return 1;
                    return 0;
                }
            });
        }
        Collections.reverse(transacoes);
        notifyDataSetChanged();
    }

    public void filterByType(String type){
        transacoes = new ArrayList<>();
        for (int i = 0; i < transacoes_all.size(); i++) {
            if(transacoes_all.get(i).getType().toLowerCase().compareTo(type) == 0)
                transacoes.add(transacoes_all.get(i));
        }
        notifyDataSetChanged();
    }

    public void reset(){
        transacoes = transacoes_all;
        notifyDataSetChanged();
    }


}
