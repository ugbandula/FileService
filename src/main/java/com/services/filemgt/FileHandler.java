package com.services.filemgt;

import com.services.filemgt.enums.FileServiceStatus;
import com.services.filemgt.shared.Constants;
import com.services.filemgt.shared.SharedMethods;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Bandula Gamage on 19/07/2015.
 */
public class FileHandler {
    private static FileHandler selfRef;

    public FileHandler() {
        this.selfRef = this;
    }

    public String writeFile(byte[] content, String fileName) throws IOException {
        String path = Initializer.getSelfRef().getUploadHome() + Constants.DIRECTORY_DATA_FILES;
        File file = new File(path + fileName);

        if (!file.exists()) {
            file.createNewFile();
        }

        FileOutputStream fop = new FileOutputStream(file);

        fop.write(content);
        fop.flush();
        fop.close();

        return SharedMethods.generateJSONStatusMessage(FileServiceStatus.UPLOAD_STATUS_SUCCESSFUL,
                Constants.STATUS_SUCCESS, "Data file successfully uploaded!");
    }

    public String writeProfileImage(byte[] content, String fileName) throws IOException {
        String path = Initializer.getSelfRef().getUploadHome() + Constants.DIRECTORY_USER_PROFILE_IMAGES;
        File file = new File(path + fileName);

        //System.out.println(">> Upload Path: " + path + ", " + fileName);
        if (!file.exists()) {
            file.createNewFile();
        }

        FileOutputStream fop = new FileOutputStream(file);

        fop.write(content);
        fop.flush();
        fop.close();

        // Generate thumbnail image
        return generateThumbnail(fileName, path);
    }

    private String generateThumbnail(String fileName, String path) {
        String jsonResponse = null;

        try {
            File f = new File(path + fileName);
            BufferedImage img = ImageIO.read(f); // load image

            //Quality indicate that the scaling implementation should do everything
            // create as nice of a result as possible , other options like speed
            // will return result as fast as possible
            //Automatic mode will calculate the resultant dimensions according
            //to image orientation .so resultant image may be size of 50*36.if you want
            //fixed size like 50*50 then use FIT_EXACT
            //other modes like FIT_TO_WIDTH..etc also available.

            BufferedImage thumbImg = Scalr.resize(img, Scalr.Method.QUALITY, Scalr.Mode.AUTOMATIC,
                    100,
                    100, Scalr.OP_ANTIALIAS);
            //convert bufferedImage to outpurstream
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(thumbImg, "jpg", os);

            File f2 = new File(Initializer.getSelfRef().getUploadHome() + Constants.DIRECTORY_USER_PROFILE_THUMBNAILS + fileName);

            ImageIO.write(thumbImg, "jpg", f2);

            jsonResponse = SharedMethods.generateJSONStatusMessage(FileServiceStatus.UPLOAD_STATUS_SUCCESSFUL,
                    Constants.STATUS_SUCCESS, "Thumbnail generated!");
        } catch (IOException e) {
            e.printStackTrace();
            jsonResponse = SharedMethods.generateJSONStatusMessage(FileServiceStatus.UPLOAD_STATUS_THUMBNAIL_GENERATION_ERROR,
                    Constants.STATUS_FAILED, "Thumbnail generation failed!");
        }

        return jsonResponse;
    }

    public File getProfileThumbnailImage(String userName) {
//        System.out.println("getProfileThumbnailImage> " + userName);
        File file = new File(Initializer.getSelfRef().getUploadHome() + Constants.DIRECTORY_USER_PROFILE_THUMBNAILS + userName + ".jpg");

        if (!file.exists())
            return null;
        else
            return file;
    }

    public File getProfileImage(String userName) {
//        System.out.println("getProfileImage> " + userName);
        File file = new File(Initializer.getSelfRef().getUploadHome() + Constants.DIRECTORY_USER_PROFILE_IMAGES + userName + ".jpg");

        if (!file.exists())
            return null;
        else
            return file;
    }

    public static synchronized FileHandler getReference() {
        if (selfRef == null)
            selfRef = new FileHandler();

        return selfRef;
    }
}
