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

public class ModifyLabelIDsOnMessage {

	RequestSpecBuilder reqspecbuild=null;
	RequestSpecification reqspec=null;
	ResponseSpecBuilder respspecbuild=null;
	ResponseSpecification respspec=null;
	
	String Authorization="Bearer ya29.a0ARrdaM8mrQTlSxLzA3a0PSB90Yp914uUDTMHs2s5poyhCJ-R3Lb-60PV3OzsIVlK8cxCVL2f5kmWHsA9Hijz4AV-KSt7sus5E8BKICd7kiUpMBJVG8I0heJ0miNSn4vS1JrCrmnmuZdvR2oO6Y598rtoITsUBg";
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
	public void addLabelID()
	{
		File f=new File("src/main/resources/DataFiles/ModifyLabelWithAddLabelOperation.json");
		
		String id="17afbf0a589a8eda";
		
		RestAssured.given(reqspec).
					body(f).
					when().
					post("/{userId}/messages/{id}/modify",userMailID,id).
					then().
					spec(respspec);
	}
	
	@Test(priority = 3)
	public void removeLabelID()
	{
		File f=new File("src/main/resources/DataFiles/ModifyLabelWithRemoveLabelOperation.json");
		
		String id="17afbf0a589a8eda";
		
		RestAssured.given(reqspec).
		body(f).
		when().
		post("/{userId}/messages/{id}/modify",userMailID,id).
		then().
		spec(respspec);
	}
	
	@Test(priority = 2)
	public void getLabelIDsOfTheMail1()
	{
		String id="17afbf0a589a8eda";
		
		RestAssured.given(reqspec).
					queryParam("format","minimal").
					when().
					get("/{userId}/messages/{id}",userMailID,id).
					then().
					spec(respspec).
					assertThat().
					body("id",Matchers.is(Matchers.equalTo(id)));
	}
	
	@Test(priority = 4)
	public void getLabelIDsOfTheMail2()
	{
		String id="17afbf0a589a8eda";
		
		RestAssured.given(reqspec).
					queryParam("format","minimal").
					when().
					get("/{userId}/messages/{id}",userMailID,id).
					then().
					spec(respspec).
					assertThat().
					body("id",Matchers.is(Matchers.equalTo(id)));
	}
}