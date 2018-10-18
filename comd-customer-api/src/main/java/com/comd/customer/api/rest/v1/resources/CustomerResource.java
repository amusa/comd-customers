package com.comd.customer.api.rest.v1.resources;

import com.comd.customer.api.services.CustomerService;
import com.sap.conn.jco.JCoException;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/customer")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {

    private static final Logger logger = Logger.getLogger(CustomerResource.class.getName());

    @Inject
    private CustomerService customerService;

    @Path("/{customerId}")
    @GET
    public Response getCustomer(@PathParam("customerId") String customerId) throws JCoException {

        return Response
                .ok(customerService.customer(customerId))
                .header("X-Total-Count", 0)
                .build();

    }

    @Path("/")
    @GET
    public Response getCustomerList() throws JCoException {

        return Response
                .ok(customerService.customers())
                .header("X-Total-Count", 0)
                .build();

    }

}
