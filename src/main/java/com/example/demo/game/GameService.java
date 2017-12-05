package com.example.demo.game;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by gargg on 25/10/17.
 */
@Service
public class GameService {

    @Autowired
    RestTemplate restTemplate;

    ObjectMapper mapper;

    private static final String GET_URL = "http://tw-http-hunt-api-1062625224.us-east-2.elb.amazonaws.com/challenge/input";
    private static final String POST_URL = "http://tw-http-hunt-api-1062625224.us-east-2.elb.amazonaws.com/challenge/output";

    @PostConstruct
    public void init(){
        mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    }

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
        List<InputEntity> inputEntityList = fetchInputFromRestClient();
        Map<String,Integer> categoryMap = null;
        Map<String,Long> totalValueMap = null;
        try {
            System.out.println("InputResponse: " + mapper.writeValueAsString(inputEntityList));

            inputEntityList = prepareActiveDateResponse(inputEntityList);
            System.out.println("PrepareActiveDateResponse: "+inputEntityList);

            categoryMap = filterByCategory(inputEntityList);
            System.out.println("CategoryMap: "+mapper.writeValueAsString(categoryMap));

            totalValueMap = getTotalValueOfActiveProduct(inputEntityList);
            System.out.println("TotalValueMap: "+mapper.writeValueAsString(totalValueMap));

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        //OutPutEntity outPutEntity = new OutPutEntity(inputEntityList.size());

        return postToRestServer(totalValueMap);
    }

    public List<InputEntity> fetchInputFromRestClient(){
        HttpEntity<InputEntity[]> getEntity = null;
        ResponseEntity<InputEntity[]> responseEntity = null;
        InputEntity[] responseBody = null;

        try{
            getEntity = new HttpEntity<>(getHeadersForGetCall());
            responseEntity = restTemplate.exchange(GET_URL, HttpMethod.GET, getEntity, InputEntity[].class);
            responseBody = responseEntity.getBody();
            System.out.println(responseBody);
            return CollectionUtils.arrayToList(responseBody);
        }catch (HttpClientErrorException ex){
            ex.printStackTrace();
        }
        return null;
    }

    public String postToRestServer(Map<String,Long> body){

        HttpEntity<String> postEntity = null;
        ResponseEntity<String> responseEntity = null;
        String result = "";
        String bodyVal = "";
        try{
            bodyVal = mapper.writeValueAsString(body);
            System.out.println(bodyVal);
            postEntity = new HttpEntity<>(bodyVal, getHeadersForPostCall());
            responseEntity = restTemplate.exchange(POST_URL, HttpMethod.POST, postEntity, String.class);
            result = responseEntity.getBody();
            System.out.println("result "+result);
            return result;
        }catch (HttpClientErrorException ex){
            ex.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "POST call failed";
    }

    public List<InputEntity> prepareActiveDateResponse(List<InputEntity> inputEntities){
        List<InputEntity> result = new ArrayList<>();
        if (!CollectionUtils.isEmpty(inputEntities)){
            for (InputEntity entity : inputEntities){
                if (checkActiveDate(entity.getStartDate(), entity.getEndDate())){
                    result.add(entity);
                }
            }
            return result;
        }
        return result;
    }

    public Map<String,Integer> filterByCategory(List<InputEntity> inputEntities){
        Map<String,Integer> categoryMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(inputEntities)){
            for (InputEntity entity : inputEntities){
                String category = entity.getCategory();
                if (categoryMap.containsKey(category)){
                    int val = categoryMap.get(category);
                    categoryMap.put(category,val +1);
                }
                else {
                    categoryMap.put(category,1);
                }
            }
            return categoryMap;
        }
        return categoryMap;
    }

    public boolean checkActiveDate(String startDate, String endDate){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDay = null;
        Date endDay = null;
        Date today = null;
        try {
            today = sdf.parse("2017-10-27");
            if (startDate == null){
                return false;
            }
            if (endDate == null){
                if (today.equals(sdf.parse(startDate)) || today.after(sdf.parse(startDate))){
                    return true;
                }
            }if(endDate != null) {
                if ((today.equals(sdf.parse(startDate)) || today.after(sdf.parse(startDate)))
                        && (today.equals(sdf.parse(endDate)) || today.before(sdf.parse(endDate)))){
                    return true;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public Map<String, Long> getTotalValueOfActiveProduct(List<InputEntity> inputEntities){
        Map<String,Long> map = new HashMap<>();
        String totalValue = "totalValue";
        long value = 0;
        for (InputEntity entity : inputEntities){
            value = value + entity.getPrice();
        }
        map.put(totalValue,value);
        return map;
    }

    public static void main(String[] args){
        ObjectMapper mapper = new ObjectMapper();

        Map<String,Integer> test = new HashMap<>();
        test.put("a",1);
        test.put("b",2);
        try {
            System.out.println(mapper.writeValueAsString(test));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date endDate = sdf.parse("null");
            System.out.println(endDate);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

}
