package com.kumuluz.ee.samples.jaxrs;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("upload")
public class ImageResource {

    private static String dummyLink = "https://i.imgur.com/HXLuZf5.jpg";

    @POST
    public Response uploadImage(ImageRequest image) {
        // Add code for sending to a validation and processing micro-service
        // Add code for dropping off to S3 bucket
        // Use the s3 link in place of the dummy link
        String imageUrl = AwsStorage.UploadImage(image.getImage());
        ImageEntry response = new ImageEntry(imageUrl, image.getUserId(), LocalDate.now());
        if(Database.AddImage(response)){
            return Response.status(Response.Status.CREATED).build();
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
