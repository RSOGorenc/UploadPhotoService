package com.amproductions.uploadmicroserivce;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("upload")
public class ImageResource {

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    public Response uploadImage(String image, @HeaderParam("userId") String userId) {
        System.out.println(System.getenv("AWS_ACCESS_KEY_ID"));
        if(userId == null){
            userId = "public";
        }
        try {
            HttpPost post = new HttpPost("http://processing-service:22341/v1/processing");
            post.setEntity(new StringEntity(image, ContentType.create("text/plain")));

            String processedBase64;
            try (CloseableHttpClient httpClient = HttpClients.createDefault();
                 CloseableHttpResponse response = httpClient.execute(post)) {
                processedBase64 = EntityUtils.toString(response.getEntity());
            }
            catch (Exception e){
                e.printStackTrace();
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }

            byte[] processed = Base64.getDecoder().decode(processedBase64);
            String imageKey = AwsStorage.UploadImage(processed);
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
