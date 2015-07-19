package com.services.filemgt.enums;

/**
 * Created by Bandula Gamage on 19/07/2015.
 */
public enum FileServiceStatus {

    UPLOAD_STATUS_SUCCESSFUL(1),
    UPLOAD_STATUS_UNSUCCESSFUL(2),
    UPLOAD_STATUS_THUMBNAIL_GENERATION_ERROR(3),
    READ_STATUS_FILE_NOT_FOUND(4);

    private int value;

    private FileServiceStatus(int value) {
        this.value = value;
    }

}
