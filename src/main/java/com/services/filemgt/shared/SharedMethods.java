package com.services.filemgt.shared;

import com.services.filemgt.enums.FileServiceStatus;

/**
 * Created by Bandula Gamage on 19/07/2015.
 */
public class SharedMethods {

    public static String generateJSONStatusMessage(FileServiceStatus service, int status, String description) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        stringBuilder.append("\"service\":");
        stringBuilder.append("\"");
        stringBuilder.append(service);
        stringBuilder.append("\"");
        stringBuilder.append(",");
        stringBuilder.append("\"status\":");
        stringBuilder.append(status);
        stringBuilder.append(",");
        stringBuilder.append("\"description\":");
        stringBuilder.append("\"");
        stringBuilder.append(description);
        stringBuilder.append("\"");
        stringBuilder.append("}");

        return stringBuilder.toString();
    }

}
