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

public class UpdateLabelInfoGmail {

	RequestSpecBuilder reqspecbuild=null;
	RequestSpecification reqspec=null;
	ResponseSpecBuilder respspecbuild=null;
	ResponseSpecification respspec=null;
	
	String Authorization="Bearer ya29.a0ARrdaM_eZce9bY7ssNizqETg046VoEajpdYs0bF6asctx98UjBuXK5d2wNivbU-ybg8WPWlhe8KOX9ZgDFt1S-jaT-eTzCuRgaPNHtuZvXCSNDPdoKQVTpsQ5wOgx_IQF4pgsiAIyXUT3sOKSWwPqZl7uAOgEQ";
	String userMailID="bhagavatulaprasant0@gmail.com";
	
	@BeforeTest
	public void preCond()
	{	
		reqspecbuild =new RequestSpecBuilder().
				      setBaseUri("https://gmail.googleapis.com").
				      setBasePath("/gmail/v1/users").
				      addHeader("Authorization", Authorization).
				      log(LogDetail.BODY).and().log(LogDetail.URI);
		reqspec=reqspecbuild.build();
		
		respspecbuild=new ResponseSpecBuilder().
				      expectContentType(ContentType.JSON).
				      expectStatusCode(200).
				      expectResponseTime(Matchers.is(Matchers.lessThan((long)8000))).
				      log(LogDetail.BODY);
		respspec=respspecbuild.build();
	}
	
	@Test(priority = 1)
	public void updateLabel()
	{
		File f=new File("src/main/resources/DataFiles/UpdateLabel.json");
		
		String id="Label_1";
		RestAssured.given(reqspec).
					body(f).
					when().
					put("/{userId}/labels/{id}",userMailID,id).
					then().
					spec(respspec);
	}
	
	@Test(priority = 2)
	public void getLabelsList()
	{
		RestAssured.given(reqspec).
				    when().
				    get("/{userID}/labels",userMailID).
				    then().
				    spec(respspec);
	}
}