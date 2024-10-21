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

    // Skicka en GET-förfrågan för att hämta alla shelters
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

    // Skicka en POST-förfrågan för att skapa ett nytt shelter
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

    // Skicka en PUT-förfrågan för att uppdatera ett shelter
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

    // Skicka en DELETE-förfrågan för att ta bort ett shelter
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

    // Testa alla CRUD-funktioner
    public static void main(String[] args) throws IOException, ParseException {

        String baseUri = "http://localhost:5000/shelter";

        // Testa GET
        System.out.println("Skickar GET-förfrågan för att hämta alla shelters...");
        sendGetRequest(baseUri);

        // Testa POST (Skapa nytt shelter)
        Shelter newShelter = new Shelter("New Shelter", "Gothenburg", 15);
        System.out.println("Skickar POST-förfrågan för att skapa ett nytt shelter...");
        sendPostShelterRequest(baseUri, newShelter);

        // Testa PUT (Uppdatera ett shelter)
        Shelter updatedShelter = new Shelter("Updated Shelter", "Stockholm", 20);
        System.out.println("Skickar PUT-förfrågan för att uppdatera shelter med ID 1...");
        sendPutShelterRequest(baseUri + "/1", updatedShelter);

        // Testa DELETE (Ta bort ett shelter)
        System.out.println("Skickar DELETE-förfrågan för att ta bort shelter med ID 1...");
        sendDeleteShelterRequest(baseUri + "/1");
    }
}
