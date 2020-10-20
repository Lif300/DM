package com.example.marketplaceproject;

public class datamodel {

    String header, descr, prec, imageurl;

    public datamodel(){

    }

    public datamodel(String header, String descr, String prec, String imageurl) {
        this.header = header;
        this.descr = descr;
        this.prec = prec;
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

    public String getPrec() {
        return prec;
    }

    public void setPrec(String prec) {
        this.prec = prec;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
}
