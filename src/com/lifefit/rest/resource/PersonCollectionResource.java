package com.lifefit.rest.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;

import com.lifefit.rest.client.bls.LifeFitBLSClient;
import com.lifefit.rest.client.ss.LifeFitSSClient;
import com.lifefit.rest.model.Goal;
import com.lifefit.rest.model.HealthMeasureHistory;
import com.lifefit.rest.model.LifeStatus;
import com.lifefit.rest.model.Person;

@Path("/person")
public class PersonCollectionResource {

	// Allows to insert contextual objects into the class,
    // e.g. ServletContext, Request, Response, UriInfo
    @Context
    UriInfo uriInfo;
    @Context
    Request request;
    
    @Path("{personId}")
    public PersonResource getPerson(@javax.ws.rs.PathParam("personId") int id) {
        return new PersonResource(uriInfo, request, id);
    }
    
    @GET
    @Path("{personId}/goal")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Goal getPersonGoal(@PathParam("personId") int personId) {
    	
    	LifeFitSSClient client = new LifeFitSSClient();
    	
    	Goal personGoal = client.getPersonGoal(personId);
        return personGoal;
    }
    
    @PUT
    @Path("{personId}/goal")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updatePersonGoal(Goal goal, @PathParam("personId") int personId){
    	Response res;
    	LifeFitBLSClient client = new LifeFitBLSClient();
    	
    	if(client.updatePersonGoal(personId, goal)){
    		res = Response.created(uriInfo.getAbsolutePath()).build();   
    	}
    	else
    		throw new NotFoundException();
    	
    	return res;    	
    }    
    
    @GET
    @Path("{personId}/measurehistory/{measureType}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public HealthMeasureHistory[] getPersonHealthMeasureHistory(@PathParam("personId") int personId, 
    		@PathParam("measureType") String measureType) {
    	
    	LifeFitBLSClient client = new LifeFitBLSClient();
    	
    	HealthMeasureHistory[] healthMeasureHistory = client.getPersonHealthMeasureHistory(personId, measureType);
        return healthMeasureHistory;
    }
    
    @POST
    @Path("{personId}/{measureType}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response savePersonHealthMeasure(LifeStatus lifeStatus, @PathParam("personId") int personId,
    		@PathParam("measureType") String measureType){
    	Response res;
    	LifeFitBLSClient client = new LifeFitBLSClient();
    	
    	if(client.savePersonHealthMeasure(lifeStatus, personId, measureType)){
    		res = Response.created(uriInfo.getAbsolutePath()).build();   
    	}
    	else
    		throw new NotFoundException();
    	
    	return res;
    }
    
    @GET
    @Path("{email}/{pass}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Person authenticateUser(@PathParam("email") String email, @PathParam("pass") String pass){
    	LifeFitBLSClient client = new LifeFitBLSClient();
    	Person person = client.authenticateUser(email, pass);
    	return person;
    }
}
