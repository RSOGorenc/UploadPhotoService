package com.kumuluz.ee.samples.jaxrs;
import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbProperty;


public class ImageRequest {
    private String image;
    private String userId;

    @JsonbCreator
    public ImageRequest(@JsonbProperty("image")String image,
                         @JsonbProperty("userId")String userId){
        this.image = image;
        this.userId = userId;
    }

    public String getImage(){
        return image;
    }
    public String getUserId(){
        return userId;
    }

}
