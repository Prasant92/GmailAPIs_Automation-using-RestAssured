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

public class CreateLabelInGmail {

	
	RequestSpecBuilder reqspecbuild=null;
	RequestSpecification reqspec=null;
	ResponseSpecBuilder respspecbuild=null;
	ResponseSpecification respspec=null;
	
	String Authorization="Bearer ya29.a0ARrdaM-aFRe66p13GJ1DrU4HOhsA1IWShD9TfacHlIsR9XweyVEuWDG5akDIrvC1T1SlmaYzTLbwOHQyYV0WRYKSvUukuUOX-nCocLDuUNT4mBRdmIj2gKjmfVktNTLihKTFYgDB8oCMKIQzf-EAvgJq3e9Epw";
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
	public void createLabel()
	{
		File f=new File("src/main/resources/DataFiles/CreateLabel.json");
		
		RestAssured.given(reqspec).
					body(f).
					when().
					post("/{userId}/labels",userMailID).
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