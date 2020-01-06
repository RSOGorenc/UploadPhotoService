package com.amproductions.uploadmicroserivce;
import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbDateFormat;
import javax.json.bind.annotation.JsonbProperty;
import java.time.LocalDate;
import java.util.List;

public class ImageEntry {
    private String imageId;
    private String userId;
    @JsonbDateFormat("dd.MM.yyyy")
    private LocalDate date;
    private List<String> comments;
    private List<String> shareUsers;

    @JsonbCreator
    public ImageEntry(@JsonbProperty("image")String imageId,
                      @JsonbProperty("userId")String userId,
                      @JsonbProperty("date")LocalDate date,
                      @JsonbProperty("comment")List<String> comments,
                      @JsonbProperty("shareUsers")List<String> shareUsers){
        this.imageId = imageId;
        this.userId = userId;
        this.date = date;
        this.comments = comments;
        this.shareUsers = shareUsers;
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
    public List<String> getComments(){
        return comments;
    }
    public List<String> getShareUsers(){
        return shareUsers;
    }

}
