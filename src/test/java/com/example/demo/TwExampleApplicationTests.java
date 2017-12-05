package com.example.demo;

import com.example.demo.game.InputEntity;
import com.example.demo.game.OutPutEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TwExampleApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Test
	public void playGameTest(){
		System.out.println(playGame());
		Assert.assertTrue(true);
	}

	/////////////////

	private static final String GET_URL = "http://tw-http-hunt-api-1062625224.us-east-2.elb.amazonaws.com/challenge/input";
	private static final String POST_URL = "http://tw-http-hunt-api-1062625224.us-east-2.elb.amazonaws.com/challenge/output";

	protected HttpHeaders getHeadersForGetCall(){
		HttpHeaders headers = new HttpHeaders();
		headers.add("userId", "Hy-ekYipW");
		return headers;
	}

	protected HttpHeaders getHeadersForPostCall(){
		HttpHeaders headers = new HttpHeaders();
		headers.add("userId", "Hy-ekYipW");
		headers.add("Content-Type", "application/json");
		return headers;
	}

	public String playGame(){
		String inputEntityList = fetchInputFromRestClient();
		//OutPutEntity outPutEntity = new OutPutEntity(inputEntityList);
		return postToRestServer(inputEntityList);
	}

	public String fetchInputFromRestClient(){
		HttpEntity<InputEntity> getEntity = null;
		ResponseEntity<String> responseEntity = null;
		InputEntity[] inputEntities = null;
		String responseBody = null;
		RestTemplate restTemplate = null;
		try{
			getEntity = new HttpEntity<>(getHeadersForGetCall());
			restTemplate = new RestTemplate();
			responseEntity = restTemplate.exchange(GET_URL, HttpMethod.GET, getEntity, String.class);
			responseBody = responseEntity.getBody();
			System.out.println(responseBody);
			return responseBody;
		}catch (HttpClientErrorException ex){
			ex.printStackTrace();
		}
		return "";
	}

	public String postToRestServer(String bodyString){
		ObjectMapper mapper = new ObjectMapper();
		HttpEntity postEntity = null;
		ResponseEntity<String> responseEntity = null;
		String result = "";
		RestTemplate restTemplate = null;

		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE + ";" + "charset=UTF-8");
		headers.add("userId", "Hy-ekYipW");
		HttpEntity<String> entity = new HttpEntity<>(bodyString, headers);

		try{
			//String bodyString = mapper.writeValueAsString(body);
			//JsonNode jsonNode = mapper.readTree(bodyString);
			postEntity = new HttpEntity<>(bodyString, getHeadersForPostCall());
			restTemplate = new RestTemplate();
			responseEntity = restTemplate.exchange(POST_URL, HttpMethod.POST, entity, String.class);
			//restTemplate.postForObject(POST_URL, postEntity, String.class,null);
			result = responseEntity.getBody();
			System.out.println("result "+result);
			return result;
		}catch (HttpClientErrorException ex){
			ex.printStackTrace();
		}
		return "Post Not Success";
	}




}
