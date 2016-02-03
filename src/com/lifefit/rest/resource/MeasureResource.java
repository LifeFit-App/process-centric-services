package com.lifefit.rest.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.lifefit.rest.client.ss.LifeFitSSClient;
import com.lifefit.rest.model.Measure;

@Path("/measureTypes")
public class MeasureResource {

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Measure[] getMeasureTypes() {
    	LifeFitSSClient client = new LifeFitSSClient();  
        System.out.println("Getting list of measureTypes...");
        Measure[] measureType = client.getMeasureTypes();
        return measureType;
    }
}
