package com.kumuluz.ee.samples.jaxrs;
import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbDateFormat;
import javax.json.bind.annotation.JsonbProperty;
import java.time.LocalDate;
import java.util.Date;

public class ImageEntry {
    private String image;
    private String imageId;
    private String userId;
    @JsonbDateFormat("dd.MM.yyyy")
    private LocalDate date;

    @JsonbCreator
    public ImageEntry(@JsonbProperty("image")String image,
                      @JsonbProperty("imageId")String imageId,
                      @JsonbProperty("userId")String userId,
                      @JsonbProperty("date")LocalDate date){
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

    public LocalDate getDate(){
        return date;
    }
}
