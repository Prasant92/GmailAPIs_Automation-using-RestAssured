package com.Rest;

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

public class GetGmailUserProfile {

	RequestSpecBuilder reqspecbuild=null;
	RequestSpecification reqspec=null;
	ResponseSpecBuilder respspecbuild=null;
	ResponseSpecification respspec=null;
	
	String Authorization="Bearer ya29.a0ARrdaM8_dP4h-ERK5GdMd4XgsXomqveqhApIi0t4tVK2UeJzDLWc8PHiPxAfR5NTLRfNbvaqw6b8GQmWKcHdKfJhD0vTVjVDFDXC8ZzDbkAhN7iYFTcpmGkcvCZtKdidkYUu3ewQuJBIvUs_pTax-4WoY6cygA";
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
				      expectBody("messagesTotal",Matchers.isA(Integer.class)).
				      expectResponseTime(Matchers.is(Matchers.lessThan((long)6000))).
				      log(LogDetail.BODY);
		respspec=respspecbuild.build();
	}
	
	@Test
	public void test()
	{
		RestAssured.given(reqspec).
					when().
					get("/{userID}/profile",userMailID).
					then().
					spec(respspec);
	}
}