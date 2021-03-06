package com.example.hanifkf.lostandfound.Models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

/**
 * Created by M Taufiq R on 29/03/2018.
 */

@IgnoreExtraProperties
public class Home extends GetId {

    private String nama_barang;
    private String deskripsi;
    private String lokasi;
    private String category;
    private String info;
    private String image_url;
    private String user_id;
    private Date timestap;

    public Home() {

    }

    public Home(String nama_barang, String deskripsi, String lokasi, String category, String info, String image_url, String user_id, Date timestap) {
        this.nama_barang = nama_barang;
        this.deskripsi = deskripsi;
        this.lokasi = lokasi;
        this.category = category;
        this.info = info;
        this.image_url = image_url;
        this.user_id = user_id;
        this.timestap = timestap;
    }


    public String getNama_barang() {
        return nama_barang;
    }

    public void setNama_barang(String nama_barang) {
        this.nama_barang = nama_barang;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Date getTimestap() {
        return timestap;
    }

    public void setTimestap(Date timestap) {
        this.timestap = timestap;
    }
}
