package com.kumuluz.ee.samples.jaxrs;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import java.util.List;

public class AwsStorage {
    public static AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.EU_WEST_1).withCredentials(new EnvironmentVariableCredentialsProvider()).build();

    public static void ListBuckets(){
        List<Bucket> buckets = s3.listBuckets();
        for (Bucket bucket: buckets) {
            System.out.println(bucket.getName());
        }
    }
}
