package com.strongmuhoti.myhikes;

public class Objects {

    // Model class
    String name;
    String image;
    String about;
    String latitude;
    String longitude;

    //Constructor
    public Objects() {

    }

    public Objects(String name, String image, String about, String latitude, String longitude) {
        this.name = name;
        this.image = image;
        this.about = about;
        this.latitude = latitude;
        this.longitude = longitude;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
