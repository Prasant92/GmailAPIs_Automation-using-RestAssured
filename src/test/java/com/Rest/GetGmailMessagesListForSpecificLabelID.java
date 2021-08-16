package com.Rest;

import java.util.ArrayList;

import org.hamcrest.Matchers;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class GetGmailMessagesListForSpecificLabelID {

	RequestSpecBuilder reqspecbuild=null;
	RequestSpecification reqspec=null;
	ResponseSpecBuilder respspecbuild=null;
	ResponseSpecification respspec=null;
	
	String Authorization="Bearer ya29.a0ARrdaM_tAeb0ifM8wWAhMkNFOGM_BfEOE2YG0mK9FG-jPc5I-7go_fL3eswnhdqhwqoJm5jNJNZKKsBdTjH28YyZK-W9FSk88JemN9KUYyu1v15yxb5VsjIDR1pRS0bkxu3oJdeqe3c9JrOf7bjoFaX9DIoMeA";
	String userMailID="bhagavatulaprasant0@gmail.com";
	
	ArrayList<String> al=new ArrayList<String>();
	
	@BeforeTest
	public void preCond()
	{	
		reqspecbuild =new RequestSpecBuilder().
				      setBaseUri("https://gmail.googleapis.com").
				      setBasePath("/gmail/v1/users").
				      addHeader("Authorization", Authorization).
				      log(LogDetail.BODY).log(LogDetail.URI);
		reqspec=reqspecbuild.build();
		
		respspecbuild=new ResponseSpecBuilder().
				      expectContentType(ContentType.JSON).
				      expectStatusCode(200).
				      expectResponseTime(Matchers.is(Matchers.lessThan((long)8000))).
				      log(LogDetail.BODY);
		respspec=respspecbuild.build();
	}
	
	@Test(priority = 1)
	public void test()
	{
		//Getting the response in Byte[] and converting the same to the String for validation
		
		byte[] resp=RestAssured.given(reqspec).
					when().
					get("/{userID}/labels",userMailID).
					then().
					spec(respspec).extract().response().asByteArray();
		
		String str=new String(resp);
		
		JsonPath jp=new JsonPath(str);
		
		for(int i=0;i<14;i++)
		{
			String ID=jp.getString("labels["+i+"].id");
			al.add(ID);
			System.out.println(ID);
		}
	}
	
	@Test(priority = 2)
	public void getMessagesListUsingLabelID() 
	{
		for(int i=0;i<al.size();i++)
		{
			String id=null;
			id=al.get(i);
			if(id.equals("STARRED"))
			{
				RestAssured.given(reqspec).
				queryParam("labelIds", id).
				when().
				get("/{userID}/messages",userMailID).
				then().
				spec(respspec).log().body(true);
			}	
		}
	}
}