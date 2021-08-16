package com.Rest;

import java.io.File;

import org.hamcrest.Matchers;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class SendDraftMailUsingGmail {

	RequestSpecBuilder reqspecbuild=null;
	RequestSpecification reqspec=null;
	ResponseSpecBuilder respspecbuild=null;
	ResponseSpecification respspec=null;
	
	String Authorization="Bearer ya29.a0ARrdaM9CplDpY2DQ_e-5lMSx0o0xiEGDClaMvtnWfsz3mlGho26IuLDRM3g_OTByC7AU1rFdeXn7TF_VrLMV0kjV27fZAX2lk-8PFXrlDpcPZTdzUBEssf0UafwuviQEvfTylKq1JWwLILhW2sCsecUl8EFaQQ";
	String userMailID="bhagavatulaprasant0@gmail.com";
	
	String id=null;
	@BeforeTest
	public void preCond()
	{
		reqspecbuild=new RequestSpecBuilder().
						 setBaseUri("https://gmail.googleapis.com").
						 setBasePath("/gmail/v1/users").
						 addHeader("Authorization",Authorization).
						 log(LogDetail.URI).
						 and().
						 log(LogDetail.BODY);
		reqspec=reqspecbuild.build();
		
		respspecbuild=new ResponseSpecBuilder().
						  expectStatusCode(200).
						  expectContentType(ContentType.JSON).
						  expectResponseTime(Matchers.is(Matchers.lessThan((long)(6000)))).
						  log(LogDetail.BODY);		  
		respspec=respspecbuild.build();
	}
	
	@Test(priority = 1)
	public void getDraftMailID()
	{
		id=(String)RestAssured.given(reqspec).
					   queryParam("maxResults", 1).	
					   when().
					   get("/{userID}/drafts",userMailID).
					   then().
					   spec(respspec).
					   extract().
					   response().
					   path("drafts[0].id").
					   toString();
	System.out.println(id);				   
	}
	
	@Test(priority = 2)
	public void sendDraftMail()
	{
		File f=new File("src/main/resources/DataFiles/SendDraftMail.json");
		
		RestAssured.given(reqspec).
					body(f).
					when().
					post("/{userId}/drafts/send",userMailID).
					then().
					spec(respspec);
	}
}