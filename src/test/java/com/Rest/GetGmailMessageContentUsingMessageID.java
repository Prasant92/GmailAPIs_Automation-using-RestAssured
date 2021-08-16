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

public class GetGmailMessageContentUsingMessageID {

	RequestSpecBuilder reqspecbuild=null;
	RequestSpecification reqspec=null;
	ResponseSpecBuilder respspecbuild=null;
	ResponseSpecification respspec=null;
	
	String Authorization="Bearer ya29.a0ARrdaM9KKgPTQiI0XVd_EWI7uXgMSk29qKuPxnEHuZy8LuN_i5B7GbyL01xFPUT1G7k1WwImZVPpt4bV3F4cF0b5722u7uUOiLYnnTh3LSaSerB0dWA6mdCl9qBh_NWUNhzFV2p5okjPipRZ4FKedjis87bJBw";
	String userMailID="bhagavatulaprasant0@gmail.com";
	
	ArrayList<String> al=new ArrayList<String>();
	ArrayList<String> messageID_al=new ArrayList<String>();
	
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
	public void getMessageListContentUsingMessageID() 
	{
		for(int i=0;i<al.size();i++)
		{
			String id=null;
			id=al.get(i);
			if(id.equals("STARRED"))
			{
				byte[] message_id=RestAssured.given(reqspec).
				queryParam("labelIds", id).
				when().
				get("/{userID}/messages",userMailID).
				then().
				spec(respspec).extract().asByteArray();
				
				String str=new String(message_id);
				
				JsonPath jp=new JsonPath(str);
				int size=jp.getInt("resultSizeEstimate");
				System.out.println(size);
				for(int j=0;j<size;j++)
				{
					String mess_id=jp.getString("messages["+j+"].id");
					System.out.println(mess_id);
					messageID_al.add(mess_id);
				}
			}	
		}
	}
	
	@Test(priority = 3)
	public void getMessageDetailsUsingID() 
	{
		for(int i=0;i<messageID_al.size();i++)
		{
			String message_id=null;
			message_id=messageID_al.get(i);
			System.out.println(message_id);
			
			RestAssured.given(reqspec).
			queryParam("format","minimal").
			when().
			get("/{userID}/messages/{id}",userMailID,message_id).
			then().
			spec(respspec).log().body(true);
		}
	}
}