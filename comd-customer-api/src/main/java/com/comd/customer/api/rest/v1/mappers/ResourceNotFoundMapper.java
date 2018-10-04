package com.comd.customer.api.rest.v1.mappers;

import com.comd.customer.api.services.exceptions.ResourceNotFoundException;
import com.comd.customer.lib.v1.common.ApiError;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.UUID;

@Provider
public class ResourceNotFoundMapper implements ExceptionMapper<ResourceNotFoundException> {

    @Override
    public Response toResponse(ResourceNotFoundException exception) {

        ApiError error = new ApiError();
        error.setRef(UUID.randomUUID());
        error.setStatus(404);
        error.setCode("resource.not.found");

        return Response.status(Response.Status.NOT_FOUND).entity(error).build();
    }
}
