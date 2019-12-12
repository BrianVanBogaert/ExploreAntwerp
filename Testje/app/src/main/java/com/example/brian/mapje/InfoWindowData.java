package com.example.brian.mapje;


import java.io.Serializable;

//========= Hier worden alle gegevens opgeslagen van elke WindowData =================
public class InfoWindowData implements Serializable
{
    private String image;
    private String hotel;
    private String beschrijving;
    private String naam;
    private double longitude;
    private double latitude;


   // public String getImage() {
   //     return image;
   // }
   // public void setImage(String image) {
   //     this.image = image;
    // }

   // public String getHotel() {
   //     return hotel;
    // }
    // public void setHotel(String hotel) {
    //    this.hotel = hotel;
    // }

    public String getBeschrijving() {
        return beschrijving;
    }
    public void setBeschrijving(String beschrijving) {
        this.beschrijving = beschrijving;
    }

    public String getNaam() {
        return naam;
    }
    public void setNaam(String naam) {
        this.naam = naam;
    }

    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }





}
