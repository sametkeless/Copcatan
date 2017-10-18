package com.tolerans.samet.navigationbardenemeler.Model;

/**
 * Created by Samet on 18.02.2017.
 */

public class Model {
    private String userId,resim,ad,yas,memleket,cinsiyet,snapchat,instagram,twitter,aciklama;

    public Model() {
    }

    public Model(String userId, String resim, String ad, String yas, String memleket, String cinsiyet, String snapchat, String instagram, String twitter, String aciklama) {
        this.userId = userId;
        this.resim = resim;
        this.ad = ad;
        this.yas = yas;
        this.memleket = memleket;
        this.cinsiyet = cinsiyet;
        this.snapchat = snapchat;
        this.instagram = instagram;
        this.twitter = twitter;
        this.aciklama = aciklama;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getResim() {
        return resim;
    }

    public void setResim(String resim) {
        this.resim = resim;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getYas() {
        return yas;
    }

    public void setYas(String yas) {
        this.yas = yas;
    }

    public String getMemleket() {
        return memleket;
    }

    public void setMemleket(String memleket) {
        this.memleket = memleket;
    }

    public String getCinsiyet() {
        return cinsiyet;
    }

    public void setCinsiyet(String cinsiyet) {
        this.cinsiyet = cinsiyet;
    }

    public String getSnapchat() {
        return snapchat;
    }

    public void setSnapchat(String snapchat) {
        this.snapchat = snapchat;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

}
