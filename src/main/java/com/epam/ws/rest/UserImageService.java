package com.epam.ws.rest;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.*;

@Path("userImage")
public class UserImageService {

    private static final UserData USER_DATA = UserData.getInstance();

    private static final UserImageData USER_DATA_IMAGE = UserImageData.getInstance();

    private static final String UPLOAD_FOLDER = "c:/temp/";

    @POST
    @Path("/upload/{id}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(
            @PathParam("id") String id,
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail) {

        if (uploadedInputStream == null || fileDetail == null)
            return Response.status(400).entity("Invalid form USER_DATA").build();

        try {
            createFolderIfNotExists(UPLOAD_FOLDER);
        } catch (SecurityException se) {
            return Response.status(500)
                    .entity("Can not create destination folder on server")
                    .build();
        }

        if (id == null || id.isEmpty())
            return Response.status(Response.Status.BAD_REQUEST).build();

        User user = USER_DATA.getUserById(Long.valueOf(id));
        if (user == null)
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("User does not exist [" + id + "]")
                    .build();

        String uploadedFileLocation = UPLOAD_FOLDER + "User_" + id + "." + getFileExtension(fileDetail.getFileName());

        try {
            saveToFile(uploadedInputStream, uploadedFileLocation);
            USER_DATA_IMAGE.addUserImage(user, uploadedFileLocation);
        } catch (IOException e) {
            return Response.status(500).entity("Can not save file").build();
        }
        return Response.status(200)
                .entity("File saved to " + uploadedFileLocation).build();
    }

    @GET
    @Path("/download/{id}")
    @Produces({"image/gif", "image/jpeg", "image/png"})
    public Response downloadFile(@PathParam("id") String id) {

        if (id == null || id.isEmpty())
            return Response.status(Response.Status.BAD_REQUEST).build();

        User user = USER_DATA.getUserById(Long.valueOf(id));
        if (user == null)
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("User does not exist [" + id + "]")
                    .build();

        String userImagePath = USER_DATA_IMAGE.getImageByUser(user);

        if (userImagePath == null || userImagePath.isEmpty())
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Image file for user [" + id + "] not found")
                    .build();

        File file = new File(userImagePath);

        Response.ResponseBuilder response = Response.ok((Object) file);
        response.header("Content-Disposition",
                "attachment; filename=userImage." + getFileExtension(userImagePath));
        return response.build();
    }

    private void saveToFile(InputStream inStream, String fileLocation)
            throws IOException {
        OutputStream out = null;
        int read = 0;
        byte[] bytes = new byte[1024];
        out = new FileOutputStream(new File(fileLocation));
        while ((read = inStream.read(bytes)) != -1) {
            out.write(bytes, 0, read);
        }
        out.flush();
        out.close();
    }

    private void createFolderIfNotExists(String dirName)
            throws SecurityException {
        File targetDir = new File(dirName);
        if (!targetDir.exists()) {
            targetDir.mkdir();
        }
    }

    private String getFileExtension(String filename) {

        String extension = "";

        int dotPosition = filename.lastIndexOf('.');
        if (dotPosition >= 0) {
            extension = filename.substring(dotPosition+1);
        }

        return extension;
    }
}
