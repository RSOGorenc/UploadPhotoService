package com.amproductions.uploadmicroserivce;
import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbDateFormat;
import javax.json.bind.annotation.JsonbProperty;
import java.time.LocalDate;

public class ImageEntry {
    private String imageLink;
    private String userId;
    @JsonbDateFormat("dd.MM.yyyy")
    private LocalDate date;

    @JsonbCreator
    public ImageEntry(@JsonbProperty("image")String imageLink,
                      @JsonbProperty("userId")String userId,
                      @JsonbProperty("date")LocalDate date){
        this.imageLink = imageLink;
        this.userId = userId;
        this.date = date;
    }

    public String getImageLink(){
        return imageLink;
    }
    public String getUserId(){
        return userId;
    }
    public LocalDate getDate(){
        return date;
    }
}
