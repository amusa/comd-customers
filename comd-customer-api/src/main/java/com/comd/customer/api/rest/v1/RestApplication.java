package com.comd.customer.api.rest.v1;


import com.comd.customer.api.rest.v1.mappers.EmptyPayloadMapper;
import com.comd.customer.api.rest.v1.mappers.GeneralMapper;
import com.comd.customer.api.rest.v1.mappers.IdMismatchMapper;
import com.comd.customer.api.rest.v1.mappers.JCoExceptionMapper;
import com.comd.customer.api.rest.v1.mappers.ResourceNotFoundMapper;
import com.comd.customer.api.rest.v1.mappers.UnauthorizedMapper;
import com.comd.customer.api.rest.v1.resources.CustomerResource;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/v1")
public class RestApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {

        Set<Class<?>> classes = new HashSet<>();

        classes.add(JacksonJsonProvider.class);

        //classes.add(JacksonProvider.class);

        classes.add(CustomerResource.class);

        classes.add(EmptyPayloadMapper.class);

        classes.add(ResourceNotFoundMapper.class);

        classes.add(JCoExceptionMapper.class);

        classes.add(IdMismatchMapper.class);

        classes.add(GeneralMapper.class);

        classes.add(UnauthorizedMapper.class);

        return classes;
    }

}
