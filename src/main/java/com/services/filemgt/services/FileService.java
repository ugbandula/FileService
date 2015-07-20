package com.services.filemgt.services;

import com.services.filemgt.FileHandler;
import com.services.filemgt.Initializer;
import com.services.filemgt.enums.FileServiceStatus;
import com.services.filemgt.exception.ServiceException;
import com.services.filemgt.shared.Constants;
import com.services.filemgt.shared.SharedMethods;
import org.apache.commons.io.IOUtils;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.*;
import java.util.List;
import java.util.Map;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

/**
 * Created by Bandula Gamage on 19/07/2015.
 */
@Path("/file")
public class FileService {

    @PermitAll
    @POST
    @Path("/upload/datafiles/")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(MultipartFormDataInput input) throws ServiceException {
        String fileName     = "";
        String jsonResponse = "";
        Response.ResponseBuilder rb = null;
        System.out.println("<uploadFile.HTTP.POST> File upload detected");

        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        List<InputPart> inputParts = uploadForm.get("dataFile");    // Name of the file selection input field

        for (InputPart inputPart : inputParts) {
            try {
                MultivaluedMap<String, String> header = inputPart.getHeaders();
                fileName = getFileName(header);

                //convert the uploaded file to inputstream
                InputStream inputStream = inputPart.getBody(InputStream.class,null);

                byte [] bytes = IOUtils.toByteArray(inputStream);

                jsonResponse = FileHandler.getReference().writeFile(bytes, fileName);
                System.out.println("<uploadFile> Data file successfully saved");

                rb = Response.ok(jsonResponse);
            } catch (IOException e) {
                e.printStackTrace();
                throw new ServiceException(SharedMethods.generateJSONStatusMessage(FileServiceStatus.UPLOAD_STATUS_UNSUCCESSFUL,
                        Constants.STATUS_FAILED, "Data file saving failed!"));
            }
        }

        return rb.build();
    }

    @PermitAll
    @POST
    @Path("/upload/profile")
//    @Consumes("multipart/form-data")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadProfilePic(MultipartFormDataInput input) throws ServiceException {
        String fileName     = "";
        String jsonResponse = "";
        Response.ResponseBuilder rb = null;
        System.out.println("<uploadProfilePic.HTTP.POST> Profile pic upload detected");

        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        List<InputPart> inputParts = uploadForm.get("dataFile");    // Name of the file selection input field

        for (InputPart inputPart : inputParts) {
            try {
                MultivaluedMap<String, String> header = inputPart.getHeaders();
                fileName = getFileName(header);

                //convert the uploaded file to inputstream
                InputStream inputStream = inputPart.getBody(InputStream.class,null);

                byte [] bytes = IOUtils.toByteArray(inputStream);

                jsonResponse = FileHandler.getReference().writeProfileImage(bytes, fileName);
                System.out.println("<uploadProfilePic> Profile pic successfully saved");

                rb = Response.ok(jsonResponse);
            } catch (IOException e) {
                e.printStackTrace();
                throw new ServiceException(SharedMethods.generateJSONStatusMessage(FileServiceStatus.UPLOAD_STATUS_UNSUCCESSFUL,
                        Constants.STATUS_FAILED, "Profile image saving failed!"));
            }
        }

        return rb.build();
    }

    @GET
    @Path("/profile/thumbnail/{name}")
    @Produces("image/jpg")
    public Response getProfileImageThumbnail(@PathParam("name") String name) {
        Response.ResponseBuilder response = null;
        File file = FileHandler.getReference().getProfileThumbnailImage(name);

        if (file == null) {
            response = Response.ok(SharedMethods.generateJSONStatusMessage(FileServiceStatus.READ_STATUS_FILE_NOT_FOUND,
                    Constants.STATUS_FAILED, "Profile image not found!"));
        } else {
            response = Response.ok((Object) file);
            response.header("Content-Disposition", "attachment; filename=image_from_server.png");
        }
        return response.build();
    }

    @GET
    @Path("/profile/{name}")
    @Produces("image/jpg")
    public Response getProfileImage(@PathParam("name") String name) {
        Response.ResponseBuilder response = null;
        File file = FileHandler.getReference().getProfileThumbnailImage(name);

        if (file == null) {
            response = Response.ok(SharedMethods.generateJSONStatusMessage(FileServiceStatus.READ_STATUS_FILE_NOT_FOUND,
                    Constants.STATUS_FAILED, "Profile image not found!"));
        } else {
            response = Response.ok((Object) file);
            response.header("Content-Disposition", "attachment; filename=" + name + ".jpg");
        }
        return response.build();
    }

    @GET
    @Path("/data/{name}/pdf")
    @Produces("application/pdf")
    public Response getPDFFile(@PathParam("name") String name) {
        Response.ResponseBuilder response = null;
        File file = FileHandler.getReference().getPDFFile(name);

        if (file == null) {
            response = Response.ok(SharedMethods.generateJSONStatusMessage(FileServiceStatus.READ_STATUS_FILE_NOT_FOUND,
                    Constants.STATUS_FAILED, "Profile image not found!"));
        } else {
            response = Response.ok((Object) file);
            response.header("Content-Disposition", "attachment; filename=" + name + ".pdf");
        }
        return response.build();
    }

    @GET
    @Path("/data/{name}/{extension}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getFile(@PathParam("name") String name, @PathParam("extension") String extension) {
        Response.ResponseBuilder response = null;
        File file = FileHandler.getReference().getFile(name, extension);

        if (file == null) {
            response = Response.ok(SharedMethods.generateJSONStatusMessage(FileServiceStatus.READ_STATUS_FILE_NOT_FOUND,
                    Constants.STATUS_FAILED, "File not found!"));
        } else {
            try {
                final InputStream is = new FileInputStream(file);
                StreamingOutput stream = new StreamingOutput() {
                    public void write(OutputStream output) throws IOException, WebApplicationException {
                        try {
                            output.write(IOUtils.toByteArray(is));
                        } catch (Exception e) {
                            throw new WebApplicationException(e);
                        }
                    }
                };

                return Response.ok(stream, MediaType.APPLICATION_OCTET_STREAM).header("content-disposition", "attachment; filename=" + name + "." + extension).build();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                response = Response.ok(SharedMethods.generateJSONStatusMessage(FileServiceStatus.READ_STATUS_FILE_NOT_FOUND,
                        Constants.STATUS_FAILED, "File not found!"));
            }
        }
        return response.build();
    }

    /**
     * header sample
     * {
     * 	Content-Type=[image/png],
     * 	Content-Disposition=[form-data; name="file"; filename="filename.extension"]
     * }
     **/
    private String getFileName(MultivaluedMap<String, String> header) {
        String[] contentDisposition = header.getFirst("Content-Disposition").split(";");

        for (String filename : contentDisposition) {
            System.out.println("getFileName >> " + filename);
            if ((filename.trim().startsWith("filename"))) {

                String[] name = filename.split("=");

                String finalFileName = name[1].trim().replaceAll("\"", "");
                return finalFileName;
            }
        }
        return "unknown";
    }

}
