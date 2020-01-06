package com.amproductions.uploadmicroserivce;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("upload")
public class ImageResource {

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    public Response uploadImage(String image, @HeaderParam("userId") String userId) {
        if(userId == null){
            userId = "public";
        }
        try {
            String imageKey = AwsStorage.UploadImage(image);
            List<String> emptyListComments = Collections.<String>emptyList();
            List<String> emptyListShares = Collections.<String>emptyList();

            ImageEntry response = new ImageEntry(imageKey, userId, LocalDate.now(), emptyListComments,emptyListShares);
            if(Database.AddImage(response)){
                return Response.status(Response.Status.CREATED).build();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return  Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @GET
    @Path("{imageId}")
    public Response getImage(@PathParam("imageId") String imageId, @HeaderParam("userId") String userId){
        if(userId == null){
            userId = "public";
        }
        if(!Database.Exists(imageId)){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if(!Database.CheckOwnership(imageId, userId)){
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        try{
            URI uri = AwsStorage.GetUrl(imageId);
            return Response.seeOther(uri).build();
        } catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("{imageId}")
    public Response deleteImage(@PathParam("imageId") String imageId, @HeaderParam("userId") String userId) {
        if(userId == null){
            userId = "public";
        }
        if(!Database.Exists(imageId)){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        if(!Database.CheckOwnership(imageId, userId)){
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        try {
            Database.RemoveImage(imageId, userId);
            AwsStorage.DeleteImage(imageId);
        }catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
}
