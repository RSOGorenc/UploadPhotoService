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

@Path("status")
public class StatusResource {

    @GET
    public Response status(){
        return Response.status(Response.Status.OK).build();
    }
}
