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

public class CreateDraftMailUsingGmail {

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
				      log(LogDetail.BODY).and().log(LogDetail.URI);
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
		File f=new File("src/main/resources/DataFiles/DraftMail.json");
				
		RestAssured.given(reqspec).
					body(f).
					when().
					post("/{userID}/drafts",userMailID).
					then().
					spec(respspec).
					assertThat().
					body("message.labelIds",Matchers.hasItem("DRAFT"));
	}
}