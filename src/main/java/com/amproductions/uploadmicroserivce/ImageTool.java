package com.amproductions.uploadmicroserivce;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import java.awt.image.BufferedImage;
import java.io.*;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

// Will be moved to a separate micro-service

public class ImageTool {
    private static int maxWidth = 720;
    private static int maxHeight = 720;
    private static float jpegCompression = 0.85f;

    public static byte[] processImage(byte[] inputImage) {
        try {
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(inputImage));
            if(image == null){
                return null;
            }
            BufferedImage resized = performResize(image);
            return writeImageToByte(resized);
        }
        catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    private static BufferedImage performResize(BufferedImage inputImage){
        final int width = inputImage.getWidth();
        final int height = inputImage.getHeight();
        if(width <= maxWidth && height <= maxHeight){
            return inputImage;
        }
        int targetWidth = width;
        int targetHeight = height;
        if(targetWidth > maxWidth){
            targetWidth = maxWidth;
            targetHeight = Math.round(height * maxWidth / (float)width);
        }
        if(targetHeight > maxHeight){
            targetHeight = maxHeight;
            targetWidth = Math.round(width * maxHeight / (float)height);
        }
        return resizeImage(inputImage, targetWidth, targetHeight);
    }

    private static BufferedImage resizeImage(final BufferedImage inputImage, int width, int height) {
        final BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        final Graphics2D graphics2D = outputImage.createGraphics();
        graphics2D.setComposite(AlphaComposite.Src);
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.drawImage(inputImage, 0, 0, width, height, null);
        graphics2D.dispose();
        return outputImage;
    }

    private static byte[] writeImageToByte(BufferedImage image){
        ByteArrayOutputStream jpegStream = new ByteArrayOutputStream();
        ByteArrayOutputStream pngStream = new ByteArrayOutputStream();
        try {
            if(!writeImageToJpeg(image, jpegCompression, jpegStream) &
                    !ImageIO.write(image, "png", pngStream)) return null;
        }
        catch (IOException e){
            e.printStackTrace();
            return null;
        }
        ByteArrayOutputStream smallerStream = jpegStream;
        if(pngStream.size() < jpegStream.size()){
            smallerStream = pngStream;
        }
        return smallerStream.toByteArray();
    }

    private static boolean writeImageToJpeg(BufferedImage image, float compression, OutputStream stream){
        boolean written = false;
        ImageWriter jpgWriter = ImageIO.getImageWritersByFormatName("jpg").next();
        ImageWriteParam jpgWriteParam = jpgWriter.getDefaultWriteParam();
        jpgWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        jpgWriteParam.setCompressionQuality(compression);
        try {
            jpgWriter.setOutput(ImageIO.createImageOutputStream(stream));
            IIOImage outputImage = new IIOImage(image, null, null);
            jpgWriter.write(null, outputImage, jpgWriteParam);
            written = true;
        }
        catch (IOException e){
            e.printStackTrace();
        }
        finally {
            jpgWriter.dispose();
        }
        return written;
    }
}
