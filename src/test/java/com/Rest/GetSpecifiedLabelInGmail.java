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

public class GetSpecifiedLabelInGmail {

	RequestSpecBuilder reqspecbuild=null;
	RequestSpecification reqspec=null;
	ResponseSpecBuilder respspecbuild=null;
	ResponseSpecification respspec=null;
	
	String Authorization="Bearer ya29.a0ARrdaM8D_SLqS2KC4q7K2enWXyxVLoCNo8rFGXlzcGIqs_X_Ygxu95iSw2LYmVXSXsSIGzqE5pd_Z3kxShEkzi0DF9c-UFAxgYBU2kVeyoNq4qtZ8ku2m9QzHqMczayuAOqI-6ZZ2waSWeZ4rXRLfE04nNC5ww";
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
				      expectResponseTime(Matchers.is(Matchers.lessThan((long)8000))).
				      log(LogDetail.BODY);
		respspec=respspecbuild.build();
	}
	
	@Test(priority = 1)
	public void getSpecifiedLabelsList()
	{
		byte[] resp=RestAssured.given(reqspec).
				when().
				get("/{userID}/labels",userMailID).
				then().
				spec(respspec).extract().response().asByteArray();
	
	String str=new String(resp);
	
	JsonPath jp=new JsonPath(str);
	
	for(int i=0;i<15;i++)
	{
		id=jp.getString("labels["+i+"].id");
		if(id.equals("Label_1"))
		{
			System.out.println(id);
			RestAssured.given(reqspec).
		    when().
		    get("/{userID}/labels/{id}",userMailID,id).
		    then().
		    spec(respspec);
		}
	}
 }
}