package com.Rest;

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

public class GetGmailLabelsList {

	RequestSpecBuilder reqspecbuild=null;
	RequestSpecification reqspec=null;
	ResponseSpecBuilder respspecbuild=null;
	ResponseSpecification respspec=null;
	
	String Authorization="Bearer ya29.a0ARrdaM-tirZXfKl1XNHX2hHDBt5Ml6U6MiIudOEo8JCv4BUYt1ARFltbzgYqTRqiN2O_2vDM8lvKqWx17vmWc49vdg1WWX8103OCFFdDiqxwiQV-8zjbRdjMemgW18z79F_XJH-MlkAUAVJG_UBa9C5vVsIOHQ";
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
	
	@Test
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
			System.out.println(ID);
		}
	}
}