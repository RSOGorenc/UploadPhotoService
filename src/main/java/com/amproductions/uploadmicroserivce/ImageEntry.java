package com.amproductions.uploadmicroserivce;
import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbDateFormat;
import javax.json.bind.annotation.JsonbProperty;
import java.time.LocalDate;
import java.util.List;

public class ImageEntry {
    private String imageLink;
    private String userId;
    @JsonbDateFormat("dd.MM.yyyy")
    private LocalDate date;
    private List<String> comments;

    @JsonbCreator
    public ImageEntry(@JsonbProperty("image")String imageLink,
                      @JsonbProperty("userId")String userId,
                      @JsonbProperty("date")LocalDate date,
                      @JsonbProperty("comment")List<String> comments){
        this.imageLink = imageLink;
        this.userId = userId;
        this.date = date;
        this.comments = comments;
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
    public List<String> getComments(){
        return comments;
    }

}
