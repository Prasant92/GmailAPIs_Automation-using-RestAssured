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

public class SendEmailUsingGmailID {

	RequestSpecBuilder reqspecbuild=null;
	RequestSpecification reqspec=null;
	ResponseSpecBuilder respspecbuild=null;
	ResponseSpecification respspec=null;
	
	String Authorization="Bearer ya29.a0ARrdaM_Phyp2mSshEBc8haiBdRK5csNKluVy7FdaVys3KrQ1K-j5trYU7cXKFEeRjgm3H46nrgS7c1KoRHndUQ3dCUQI1n0iK3pLAnadeh0HVYdT3hOCrKa3gKby0-Q2wyNt8kUJPLr4kLhvoqYOJbiXMr4hqA";
	String userMailID="bhagavatulaprasant0@gmail.com";
	
	@BeforeTest
	public void preCond()
	{	
		reqspecbuild =new RequestSpecBuilder().
				      setBaseUri("https://gmail.googleapis.com").
				      setBasePath("/gmail/v1/users").
				      addHeader("Authorization", Authorization).
				      log(LogDetail.BODY);
		reqspec=reqspecbuild.build();
		
		respspecbuild=new ResponseSpecBuilder().
				      expectContentType(ContentType.JSON).
				      expectStatusCode(200).
				      expectResponseTime(Matchers.is(Matchers.lessThan((long)6000))).
				      log(LogDetail.BODY);
		respspec=respspecbuild.build();
	}
	
	@Test
	public void test()
	{
		File f=new File("src/main/resources/DataFiles/EncodedMessage.json");
				
		RestAssured.given(reqspec).
					body(f).
					when().
					post("/{userID}/messages/send",userMailID).
					then().
					spec(respspec);
	}
}