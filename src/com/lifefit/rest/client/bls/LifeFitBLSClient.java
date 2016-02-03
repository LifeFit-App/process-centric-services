package com.lifefit.rest.client.bls;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;

import com.lifefit.rest.model.Goal;
import com.lifefit.rest.model.HealthMeasureHistory;
import com.lifefit.rest.model.LifeStatus;
import com.lifefit.rest.model.Person;
import com.lifefit.rest.util.Transformer;
import com.owlike.genson.ext.jaxrs.GensonJsonConverter;

public class LifeFitBLSClient {
	static InputStream stream = null;
	static Response response;
	static String results = null;
	//RESTFul Web Service URL for LifeFit business logic services
	final String SERVER_URL = "http://localhost:5800/lifefit-bls";
	WebTarget service;
	
	public LifeFitBLSClient(){
		init();
	}
	
	private void init(){
		final ClientConfig clientConfig = new ClientConfig().register(GensonJsonConverter.class);			
		Client client = ClientBuilder.newClient(clientConfig);
		service = client.target(getBaseURI(SERVER_URL));		
	}
	
	private static URI getBaseURI(String SERVER_URL){
		return UriBuilder.fromUri(SERVER_URL).build();
	}
	
	public boolean updatePersonGoal(int personId, Goal goal){		
		int httpStatus = 0;
		
		response = service.path("person/"+personId+"/goal").request(MediaType.APPLICATION_JSON)
					.put(Entity.entity(goal, MediaType.APPLICATION_JSON), Response.class);
		httpStatus = response.getStatus();
		
		if(httpStatus == 200 || httpStatus == 201)
			return true;
		else
			return false;
	}
	
	public boolean savePersonHealthMeasure(LifeStatus lifeStatus, int personId, String measureName){
		int httpStatus = 0;
		
		response = service.path("person/"+personId+"/hp/"+measureName).request(MediaType.APPLICATION_JSON)
					.put(Entity.entity(lifeStatus, MediaType.APPLICATION_JSON), Response.class);
		httpStatus = response.getStatus();
		
		if(httpStatus == 200 || httpStatus == 201)
			return true;
		else
			return false;
	}
	
	public HealthMeasureHistory[] getPersonHealthMeasureHistory(int personId, String measureType){
		HealthMeasureHistory[] healthMeasure = null;
		
		try{
			response = service.path("person/"+personId+"/measurehistory/"+measureType).request().accept(MediaType.APPLICATION_JSON).get();
			results = response.readEntity(String.class);
			//Convert string into inputStream
			stream = new ByteArrayInputStream(results.getBytes(StandardCharsets.UTF_8));
			
			Transformer transform = new Transformer();
			healthMeasure = transform.unmarshallJSONHealthMeasureHistory(stream);
		}
		catch(Exception e){e.printStackTrace();}
		return healthMeasure;
	}
	
	public Person authenticateUser(String email, String pass){
		Person person = null;
		
		try{
			response = service.path("person/"+email+"/"+pass).request().accept(MediaType.APPLICATION_JSON).get();
			results = response.readEntity(String.class);
			if(results != null){
				//Convert string into inputStream
				stream = new ByteArrayInputStream(results.getBytes(StandardCharsets.UTF_8));
				
				Transformer transform = new Transformer();
				person = transform.unmarshallJSONPerson(stream);
			}			
		}
		catch(Exception e){e.printStackTrace();}
		return person;
	}
}
