package com.amproductions.uploadmicroserivce;
import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbProperty;


public class ImageRequest {
    private String imageBase64;
    private String userId;

    @JsonbCreator
    public ImageRequest(@JsonbProperty("imageBase64")String imageBase64,
                         @JsonbProperty("userId")String userId){
        this.imageBase64 = imageBase64;
        this.userId = userId;
    }

    public String getImageBase64(){
        return imageBase64;
    }
    public String getUserId(){
        return userId;
    }

}
