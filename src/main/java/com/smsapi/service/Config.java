package com.smsapi.service;

import static io.restassured.RestAssured.given;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;

import com.cloudhopper.smpp.SmppBindType;
import com.cloudhopper.smpp.SmppSessionConfiguration;
import com.google.gson.Gson;
import com.smsapi.model.SMSCModel;
import com.smsapi.model.Stats;
import com.smsapi.model.TPS;

import io.restassured.http.ContentType;
import io.restassured.response.Response;


@Configuration
@ComponentScan("com.smsapi.service")
public class Config {

	
	
	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	@DependsOn("SMSCModel")
	public SmppSessionConfiguration setSmppSessionConfiguration(@Autowired SMSCModel smscmodel) {
		
		SmppSessionConfiguration config=new SmppSessionConfiguration(SmppBindType.TRANSCEIVER, smscmodel.getUsername(), smscmodel.getPassword());

		config.setHost(smscmodel.getIp());
		config.setPort(smscmodel.getPort());
		
		return config;
	}
	
	@Bean("Stats")
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	public Stats getStats() {
		
		return Stats.builder().build();
		
	}
	
	
	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	@DependsOn("Stats")
	public TPS getTPS(@Autowired Stats stats) {
	
		return new TPS(stats);
	}
	
	
   	@Bean("SMSCModel")
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	public SMSCModel getSMSCModel()  {
		 
	    	Gson gson =new Gson();
	    	
	        Response response = given().baseUri("http://masterapilb:8080").contentType(ContentType.JSON).body(gson.toJson(SMSCModel.builder().smscid(System.getenv("smscid")).build(), SMSCModel.class))
	                            .when()
	                            .post("/sms-api/smsc/get");

	        
	                            
	        if(response.getStatusCode()==200) {
	           
	        	return gson.fromJson(response.then().extract().asString(), SMSCModel.class);
	        		
	        }else {

	        	System.exit(0);
	        }
	        
	        return null;
	                 
	 
	 
	    }
}
