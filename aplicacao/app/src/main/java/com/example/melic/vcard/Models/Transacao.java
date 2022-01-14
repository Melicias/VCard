package com.example.melic.vcard.Models;

import java.util.Date;

public class Transacao {
    private String type;
    private String vcard;
    private Date date;
    private String valor;

    public Transacao(String type, String vcard,String valor, Date date) {
        this.type = type;
        this.vcard = vcard;
        this.date = date;
        this.valor = valor;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVcard() {
        return vcard;
    }

    public void setVcard(String vcard) {
        this.vcard = vcard;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
