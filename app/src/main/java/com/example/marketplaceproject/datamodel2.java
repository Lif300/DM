package com.example.marketplaceproject;

public class datamodel2 {
    String header, descr, prec, imageurl, usuario, pushid, userid;

    public datamodel2(){

    }

    public datamodel2(String header, String descr, String prec, String imageurl, String usuario, String pushid) {
        this.header = header;
        this.descr = descr;
        this.prec = prec;
        this.imageurl = imageurl;
        this.usuario = usuario;
        this.pushid = pushid;
        this.userid = userid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPushid() {
        return pushid;
    }

    public void setPushid(String pushid) {
        this.pushid = pushid;
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

