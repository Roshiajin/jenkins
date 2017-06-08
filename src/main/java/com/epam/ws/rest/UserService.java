package com.epam.ws.rest;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Path("user")
public class UserService {

    private final static UserData USER_DATA = UserData.getInstance();

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<User> getAllUsers() {
        return USER_DATA.getAllUsers();
    }

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getUserById(@PathParam("id") String id) {

        if (id == null || id.isEmpty())
            return Response.status(Response.Status.BAD_REQUEST).build();

        User user = USER_DATA.getUserById(Long.valueOf(id));
        if (user == null)
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("User does not exist [" + id + "]")
                    .build();

        return Response.ok(user).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response deleteUser(@PathParam("id") String id) {

        if (id == null || id.isEmpty())
            return Response.status(Response.Status.BAD_REQUEST).build();

        User existUser = USER_DATA.getUserById(Long.valueOf(id));
        if (existUser == null)
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("User does not exist [" + id + "]")
                    .build();

        USER_DATA.deleteUser(Long.valueOf(id));
        return Response.ok().build();
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createUser(User user) throws URISyntaxException {

        if (user == null) return Response.status(Response.Status.BAD_REQUEST).build();

        User result = USER_DATA.addUser(user);
        return Response.created(new URI("/user/" + String.valueOf(user.getId()))).entity(result).build();
    }

    @PUT
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateUser(@PathParam("id") String id, User user) {

        if (id == null || id.isEmpty())
            return Response.status(Response.Status.BAD_REQUEST).build();
        if (user == null)
            return Response.status(Response.Status.BAD_REQUEST).build();

        User existUser = USER_DATA.getUserById(Long.valueOf(id));
        if (existUser == null)
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("User does not exist [" + id + "]")
                    .build();

        User result = USER_DATA.updateUser(Long.valueOf(id), user);
        return Response.ok().entity(result).build();
    }


}
