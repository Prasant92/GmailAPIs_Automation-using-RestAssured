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

public class MoveMailToTrashThenUntrash {

	RequestSpecBuilder reqspecbuild=null;
	RequestSpecification reqspec=null;
	ResponseSpecBuilder respspecbuild=null;
	ResponseSpecification respspec=null;
	
	String Authorization="Bearer ya29.a0ARrdaM9DAV9lcl8zugfRptLg1_a-hnMvcArSvmD6D3oveaowrULCo7iBnhT_C411DLrjPgDSNqObKu2hImmtAknxFLgvxYVhiLm3FxMaaeCyV0yGHXOTNfEH8tahi8jVlI8h4HQRXOvbrhjN_LazQkVMgJeoFg";
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
	
	@Test(priority = 1)
	public void moveMailToTrash()
	{
		String id="17afbf0a589a8eda";
		
		RestAssured.given(reqspec).
					when().
					post("/{userId}/messages/{id}/trash",userMailID,id).
					then().
					spec(respspec).
					assertThat().
					body("labelIds",Matchers.hasItem("TRASH"));
	}
	
	@Test(priority = 2)
	public void moveMailBackToUntrash()
	{
		String id="17afbf0a589a8eda";
		
		RestAssured.given(reqspec).
					when().
					post("/{userId}/messages/{id}/untrash",userMailID,id).
					then().
					spec(respspec).
					assertThat().
					body("labelIds",Matchers.hasItem("SENT"));
	}
	
	@Test(priority = 3)
	public void getLabelIDsOfTheMail()
	{
		String id="17afbf0a589a8eda";
		
		RestAssured.given(reqspec).
					queryParam("format","full").
					when().
					get("/{userId}/messages/{id}",userMailID,id).
					then().
					spec(respspec).
					assertThat().
					body("id",Matchers.is(Matchers.equalTo(id)));
	}
}