package com.services.filemgt.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by Bandula Gamage on 19/07/2015.
 */
@Provider
public class ServiceExceptionHandler implements ExceptionMapper<ServiceException> {

    public Response toResponse(ServiceException exception) {
        return Response.status(Response.Status.BAD_REQUEST).entity(exception.getMessage()).build();
    }
}
