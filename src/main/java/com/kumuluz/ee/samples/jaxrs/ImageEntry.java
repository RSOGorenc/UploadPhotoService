package com.kumuluz.ee.samples.jaxrs;

import java.awt.image.BufferedImage;
import java.util.Date;

public class ImageEntry {
    private String image;
    private String imageId;
    private String userId;
    private Date date;

    public ImageEntry(String image, String imageId, String userId, Date date){
        this.image = image;
        this.imageId = imageId;
        this.userId = userId;
        this.date = date;
    }

    public String getImage(){
        return image;
    }

    public String getImageId(){
        return imageId;
    }

    public String getUserId(){
        return userId;
    }
    
    public Date getDate(){
        return date;
    }
}
