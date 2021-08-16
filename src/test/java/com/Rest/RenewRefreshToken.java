package com.Rest;

import java.util.HashMap;

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

public class RenewRefreshToken {

	RequestSpecBuilder reqspecbuild=null;
	RequestSpecification reqspec=null;
	ResponseSpecBuilder respspecbuild=null;
	ResponseSpecification respspec=null;
	
	String Client_ID="605529844215-vkq4s3qn5ggs3bo4cu6ce5pq7i29e5q9.apps.googleusercontent.com";
	String Client_Secret="WjZBVlqYUUeZtCLwFtez1sgV";
	String Ref_Token="1//0gSQrRglu1nFUCgYIARAAGBASNwF-L9IrzkTnzgY4O_3BZ7WG2bBEsnxeMSMyYIWr1lv3C994Gb2W66fGNjzdIUFl_DY76VHDYvU";
	
	public static String access_token=null;
	
	@BeforeTest
	public void preCond()
	{
		HashMap<String, String> hm=new HashMap<String, String>();
		hm.put("client_id", Client_ID);
		hm.put("client_secret", Client_Secret);
		hm.put("grant_type", "refresh_token");
		
		reqspecbuild =new RequestSpecBuilder().
				      setBaseUri("https://oauth2.googleapis.com").
				      setContentType(ContentType.URLENC).
				      setUrlEncodingEnabled(true).
				      addFormParams(hm).
				      log(LogDetail.BODY);
		reqspec=reqspecbuild.build();
		
		respspecbuild=new ResponseSpecBuilder().
				      expectContentType(ContentType.JSON).
				      expectStatusCode(200).
				      expectBody("token_type",Matchers.is(Matchers.equalTo("Bearer"))).
				      expectResponseTime(Matchers.is(Matchers.lessThan((long)4000))).
				      log(LogDetail.BODY);
		respspec=respspecbuild.build();
	}
	
	@Test
	public void retrieveAccessToken()
	{
			   access_token=RestAssured.given(reqspec).
							formParams("refresh_token",Ref_Token).
							when().post("/token").
							then().
							spec(respspec).
							extract().path("access_token").toString();
		
			   System.out.println(access_token);
	}
}