package com.lifefit.rest.client.ss;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.*;

import org.glassfish.jersey.client.ClientConfig;

import com.lifefit.rest.model.Goal;
import com.lifefit.rest.model.Measure;
import com.lifefit.rest.model.Person;
import com.lifefit.rest.util.Transformer;
import com.owlike.genson.ext.jaxrs.GensonJsonConverter;

public class LifeFitSSClient {
	static InputStream stream = null;
	static Response response;
	static String results = null;
	//RESTFul Web Service URL for LifeFit storage services
	final String SERVER_URL = "http://localhost:5700/lifefit-ss";
	WebTarget service;
	
	public LifeFitSSClient(){
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
	
	public Person readPerson(int personId){
		Person person = new Person();
		
		try{
			response = service.path("person/"+personId).request().accept(MediaType.APPLICATION_JSON).get();
			results = response.readEntity(String.class);			
			//Convert string into inputStream
			stream = new ByteArrayInputStream(results.getBytes(StandardCharsets.UTF_8));
			
			Transformer transform = new Transformer();
			person = transform.unmarshallJSONPerson(stream);
								
		}
		catch(Exception e){e.printStackTrace();}
		return person;
	}
	
	public Measure[] getMeasureTypes(){
		Measure[] measureTypes = null;
		
		try{
			response = service.path("measureTypes").request().accept(MediaType.APPLICATION_JSON).get();
			results = response.readEntity(String.class);			
			//Convert string into inputStream
			stream = new ByteArrayInputStream(results.getBytes(StandardCharsets.UTF_8));
			
			Transformer transform = new Transformer();
			measureTypes = transform.unmarshallJSONMeasureTypes(stream);			
		}
		catch(Exception e){e.printStackTrace();}
		return measureTypes;
	}
	
	public Goal getPersonGoal(int personId){
		Goal personGoal = null;
		
		try{
			response = service.path("person/"+personId+"/goal").request().accept(MediaType.APPLICATION_JSON).get();
			results = response.readEntity(String.class);
			//Convert string into inputStream
			stream = new ByteArrayInputStream(results.getBytes(StandardCharsets.UTF_8));
			
			Transformer transform = new Transformer();
			personGoal = transform.unmarshallJSONGoal(stream);
		}
		catch(Exception e){e.printStackTrace();}
		return personGoal;
	}
}
