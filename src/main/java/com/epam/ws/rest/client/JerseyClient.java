package com.epam.ws.rest.client;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;

import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

public class JerseyClient {

    public static void main(String[] args) {

        String newUser = "<user>\n" +
                "<email>MisterT@example.com</email>\n" +
                "<firstName>Mister</firstName>\n" +
                "<id>1</id>\n" +
                "<lastName>T</lastName>\n" +
                "<login>MisterT</login>\n" +
                "</user>";

        String outputUser;

        long userId;

        getAllUsers();
        getUserById(4L);
        outputUser = createUser(newUser);

        userId = new Scanner(outputUser).useDelimiter("\\D+").nextInt();

        updateUser(userId, outputUser.replace("T", "Question"));
        deleteUser(userId);

        getAllUsers();

        uploadUserImage(1L, "C:\\temp\\upload\\trump.jpg");

        downloadUserImage(1L);

    }

    public static void getAllUsers() {
        try {

            Client client = Client.create();

            WebResource webResource = client
                    .resource("http://localhost:8080/services/user");

            ClientResponse response = webResource.accept("application/json")
                    .get(ClientResponse.class);

            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatus());
            }

            String output = response.getEntity(String.class);

            System.out.println("GET All Users .... \n");
            System.out.println(output+"\n");

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    public static void getUserById(long userId) {

        try {

            Client client = Client.create();

            WebResource webResource = client
                    .resource("http://localhost:8080/services/user/" + String.valueOf(userId));

            ClientResponse response = webResource.accept("application/xml")
                    .get(ClientResponse.class);

            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatus());
            }

            String output = response.getEntity(String.class);

            System.out.println("GET User " + String.valueOf(userId) + " .... \n");
            System.out.println(output+"\n");

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    public static String createUser(String newUser) {

        try {

            Client client = Client.create();

            WebResource webResource = client
                    .resource("http://localhost:8080/services/user");

            ClientResponse response = webResource.type("application/xml")
                    .post(ClientResponse.class, newUser);

            if (response.getStatus() != 201) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatus());
            }

            String output = response.getEntity(String.class);

            System.out.println("POST new User .... \n");
            System.out.println(output+"\n");

            return output;

        } catch (Exception e) {

            e.printStackTrace();

            return null;

        }

    }

    public static void updateUser(long userId, String updatedUser) {

        try {

            Client client = Client.create();

            WebResource webResource = client
                    .resource("http://localhost:8080/services/user/" + String.valueOf(userId));

            ClientResponse response = webResource.type("application/json")
                    .put(ClientResponse.class, updatedUser);

            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatus());
            }

            String output = response.getEntity(String.class);

            System.out.println("PUT User " + String.valueOf(userId) + " .... \n");
            System.out.println(output+"\n");

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    public static void deleteUser(long userId) {

        try {

            Client client = Client.create();

            WebResource webResource = client
                    .resource("http://localhost:8080/services/user/" + String.valueOf(userId));

            ClientResponse response = webResource.accept("application/xml")
                    .delete(ClientResponse.class);

            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatus());
            }

            System.out.println("DELETE User " + String.valueOf(userId) + " .... \n");

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    public static void uploadUserImage(long userId, String filePath) {

        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);

        WebResource resource = client.resource("http://localhost:8080/services/userImage/upload/" + String.valueOf(userId));

        File fileToUpload = new File(filePath);

        FormDataMultiPart multiPart = new FormDataMultiPart();

        if (fileToUpload != null)
        {
            multiPart.bodyPart(new FileDataBodyPart("file", fileToUpload, MediaType.APPLICATION_OCTET_STREAM_TYPE));
        }

        ClientResponse response = resource.type(
                MediaType.MULTIPART_FORM_DATA_TYPE).post(ClientResponse.class,
                multiPart);

        String output = response.getEntity(String.class);

        System.out.println(output+"\n");

        client.destroy();

    }

    public static void downloadUserImage(long userId) {

        try {

            Client client = Client.create();

            WebResource webResource = client
                    .resource("http://localhost:8080/services/userImage/download/" + String.valueOf(userId));

            ClientResponse response = webResource.accept("image/gif", "image/jpeg", "image/png")
                    .get(ClientResponse.class);

            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatus());
            }

            InputStream inputStream = response.getEntityInputStream();

            if (inputStream != null && response.getType().toString().startsWith("image/")) {
                File downloadFile = new File("C:\\temp\\download\\downloadedServiceFile." + response.getType().toString().replace("image/", ""));

                byte[] buffer = new byte[inputStream.available()];
                inputStream.read(buffer);

                OutputStream outStream = new FileOutputStream(downloadFile);
                outStream.write(buffer);

                System.out.println("GET image file for user " + String.valueOf(userId) + " .... \n");

                System.out.println("File downloaded to: " + downloadFile.getAbsolutePath()+"\n");
            } else {
                System.out.println("Failed! Response status: " + response.getStatus() + " fileType: " + response.getType().toString() + "\n");
            }

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

}
