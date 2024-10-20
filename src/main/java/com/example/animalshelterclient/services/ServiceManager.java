package com.example.animalshelterclient.services;

import com.example.animalshelterclient.models.Shelter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.*;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.IOException;

public class ServiceManager {

    static CloseableHttpClient httpClient = HttpClients.createDefault();
    private static final ObjectMapper mapper = new ObjectMapper(); // Jackson ObjectMapper för JSON

    // Konvertera Shelter-objekt till JSON
    public static String convertShelterToJson(Shelter shelter) throws JsonProcessingException {
        return mapper.writeValueAsString(shelter);
    }

    // Skicka en GET-förfrågan
    public static void sendGetRequest(String uri) throws IOException, ParseException {
        HttpGet request = new HttpGet(uri);
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            if (response.getCode() != 200) {
                System.out.println("Fel! Statuskod: " + response.getCode() + " vid GET-förfrågan till: " + uri);
                return;
            }
            String jsonResp = EntityUtils.toString(response.getEntity());
            System.out.println("GET-svar: " + jsonResp);
        }
    }

    // Skicka en POST-förfrågan
    public static void sendPostShelterRequest(String uri, Shelter shelter) throws IOException, ParseException {
        HttpPost postRequest = new HttpPost(uri);
        StringEntity jsonPayload = new StringEntity(convertShelterToJson(shelter), ContentType.APPLICATION_JSON);
        postRequest.setEntity(jsonPayload);

        try (CloseableHttpResponse response = httpClient.execute(postRequest)) {
            if (response.getCode() != 200 && response.getCode() != 201) {
                String errorResponse = EntityUtils.toString(response.getEntity());
                System.out.println("Fel! Statuskod: " + response.getCode() + " vid POST-förfrågan till: " + uri);
                System.out.println("Felmeddelande från servern: " + errorResponse);
            } else {
                String jsonResp = EntityUtils.toString(response.getEntity());
                System.out.println("POST-svar: " + jsonResp);
            }
        }
    }

    // Skicka en PUT-förfrågan
    public static void sendPutShelterRequest(String uri, Shelter shelter) throws IOException, ParseException {
        HttpPut putRequest = new HttpPut(uri);
        StringEntity jsonPayload = new StringEntity(convertShelterToJson(shelter), ContentType.APPLICATION_JSON);
        putRequest.setEntity(jsonPayload);

        try (CloseableHttpResponse response = httpClient.execute(putRequest)) {
            if (response.getCode() != 200) {
                String errorResponse = EntityUtils.toString(response.getEntity());
                System.out.println("Fel! Statuskod: " + response.getCode() + " vid PUT-förfrågan till: " + uri);
                System.out.println("Felmeddelande från servern: " + errorResponse);
            } else {
                String jsonResp = EntityUtils.toString(response.getEntity());
                System.out.println("PUT-svar: " + jsonResp);
            }
        }
    }

    // Skicka en DELETE-förfrågan
    public static void sendDeleteShelterRequest(String uri) throws IOException, ParseException {
        HttpDelete deleteRequest = new HttpDelete(uri);
        try (CloseableHttpResponse response = httpClient.execute(deleteRequest)) {
            if (response.getCode() != 200 && response.getCode() != 204) {
                String errorResponse = EntityUtils.toString(response.getEntity());
                System.out.println("Fel! Statuskod: " + response.getCode() + " vid DELETE-förfrågan till: " + uri);
                System.out.println("Felmeddelande från servern: " + errorResponse);
            } else {
                System.out.println("DELETE-förfrågan lyckades för ID: " + uri.substring(uri.lastIndexOf("/") + 1));
            }
        }
    }

    // Välj miljö (localhost eller AWS Elastic Beanstalk)
    public static String chooseBaseUrl(String choice) {
        String baseUrl;
        if ("1".equals(choice)) {
            baseUrl = "http://localhost:5000/shelter"; // Localhost
        } else if ("2".equals(choice)) {
            baseUrl = "http://animalshelterdatabase.c96eumq2m82i.eu-north-1.rds.amazonaws.com:3306/molnintro1/shelter"; // Din faktiska AWS Elastic Beanstalk URL
        } else {
            System.out.println("Ogiltigt val, avslutar programmet.");
            return null;
        }
        return baseUrl;
    }
}
