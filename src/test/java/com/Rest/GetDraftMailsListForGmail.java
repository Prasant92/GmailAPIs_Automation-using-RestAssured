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

public class GetDraftMailsListForGmail {

	RequestSpecBuilder reqspecbuild=null;
	RequestSpecification reqspec=null;
	ResponseSpecBuilder respspecbuild=null;
	ResponseSpecification respspec=null;
	
	String Authorization="Bearer ya29.a0ARrdaM-SMpeBj36R7p7RBvBDJFBIOXxThWEe4PG-L90PUXP1NunRfyLzd1P_p1eHn3hrsUmY1BjgQLd02hPRvRoFfDlml_ngFm30zXHUhHZO6Pcfe4RqtARtQN3TwEbw7AsGMBNiponXZiyNV1U3wLvSEg1doA";
	String userMailID="bhagavatulaprasant0@gmail.com";
	
	String id=null;
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
				      expectResponseTime(Matchers.is(Matchers.lessThan((long)6000))).
				      log(LogDetail.BODY);
		respspec=respspecbuild.build();
	}
	
	@Test(priority = 1)
	public void getListOfMailsInDraftFolder()
	{			
		String resp= (String)RestAssured.given(reqspec).
					   queryParam("maxResults", 1).
					   when().
					   get("/{userID}/drafts",userMailID).
					   then().
					   spec(respspec).
					   extract().
					   response().
					   jsonPath().get("drafts[0].id");
		id=(String)resp.toString();
		System.out.println(id);
	}
	
	@Test(priority = 2)
	public void getDraftMail()
	{
		RestAssured.given(reqspec).
					queryParam("format","raw").
					when().
					get("/{userId}/drafts/{id}",userMailID,id).
					then().
					spec(respspec);
	}
}