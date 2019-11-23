package com.amproductions.uploadmicroserivce;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AwsStorage {
    public static AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.EU_WEST_1).withCredentials(new EnvironmentVariableCredentialsProvider()).build();
    public static final String bucketName = "rso-images";

    private static List<Bucket> GetBuckets(){
        try {
            return s3.listBuckets();
        }
        catch (AmazonS3Exception e){
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    private static Bucket GetBucket(String name){
        List<Bucket> filtered = GetBuckets().stream().filter(bucket -> bucket.getName().equals(name)).collect(Collectors.toList());
        if(filtered.size() > 0){
            return filtered.get(0);
        }
        return null;
    }

    private static boolean CreateBucket(String name){
        if(GetBucket(name) == null){
            try {
                s3.createBucket(name);
            }
            catch (AmazonS3Exception e){
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    private static String UploadFile(String bucketName, String fileId, InputStream input, ObjectMetadata metadata){
        try {
            s3.putObject(bucketName, fileId, input, metadata);
            return s3.getUrl(bucketName, fileId).toExternalForm();
        }
        catch (AmazonS3Exception e){
            e.printStackTrace();
            return "upload failed";
        }
        catch (SdkClientException e){
            e.printStackTrace();
            return "upload failed";
        }
    }

    public static String UploadImage(String base64String) throws Exception{
        byte[] decoded = Base64.getDecoder().decode(base64String);
        byte[] processed = ImageTool.processImage(decoded);
        if (processed == null) throw new IllegalArgumentException("file stream is not image");
        InputStream imageStream = new ByteArrayInputStream(processed);
        String mime = URLConnection.guessContentTypeFromStream(imageStream);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(mime);
        metadata.setContentLength(processed.length);
        CreateBucket(bucketName.toLowerCase());
        String objectName = Long.toString(System.currentTimeMillis());
        PutObjectRequest request = new PutObjectRequest(bucketName, objectName, imageStream, metadata);
        request.setCannedAcl(CannedAccessControlList.PublicRead);
        s3.putObject(request);
        return s3.getUrl(bucketName, objectName).toExternalForm();
    }

}
