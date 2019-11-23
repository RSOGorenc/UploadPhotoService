package com.amproductions.uploadmicroserivce;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("upload")
public class ImageResource {

    @POST
    public Response uploadImage(ImageRequest image) {
        try {
            String imageUrl = AwsStorage.UploadImage(image.getImageBase64());
            ImageEntry response = new ImageEntry(imageUrl, image.getUserId(), LocalDate.now());
            if(Database.AddImage(response)){
                return Response.status(Response.Status.CREATED).build();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return  Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @DELETE
    @Path("{imageId}")
    public Response deleteImage(@PathParam("imageId") String imageId) {
        // Database.deleteImage(imageId);
        return Response.noContent().build();
    }
}
