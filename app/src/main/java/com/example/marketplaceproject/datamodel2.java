package com.example.marketplaceproject;

public class datamodel2 {
    String header, descr, prec, imageurl, usuario;

    public datamodel2(){

    }

    public datamodel2(String header, String descr, String prec, String imageurl, String usuario) {
        this.header = header;
        this.descr = descr;
        this.prec = prec;
        this.imageurl = imageurl;
        this.usuario = usuario;
    }

    public datamodel2(String header, String descr, String prec, String imageurl) {
        this.header = header;
        this.descr = descr;
        this.prec = prec;
        this.imageurl = imageurl;
    }

    public String getPrec() {
        return prec;
    }

    public void setPrec(String prec) {
        this.prec = prec;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public datamodel2(String header, String descr, String imageurl){
        this.header = header;
        this.descr = descr;
        this.imageurl = imageurl;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
}

