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
import java.util.Optional;

public class ServiceManager {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final CloseableHttpClient httpClient = HttpClients.createDefault();
    public static String baseUrl;

    public static void setBaseUrl(String url) {
        baseUrl = url;
    }

    public static String convertShelterToJson(Shelter shelter) throws JsonProcessingException {
        return mapper.writeValueAsString(shelter);
    }

    public static void getAllShelters() throws IOException, ParseException {
        HttpGet request = new HttpGet(baseUrl);
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            if (response.getCode() != 200) {
                System.out.println("Fel! Statuskod: " + response.getCode() + " vid GET-förfrågan.");
                return;
            }
            String jsonResp = EntityUtils.toString(response.getEntity());
            System.out.println("Alla shelters: " + jsonResp);
        }
    }

    public static void getShelterByName(String name) throws IOException, ParseException {
        // Se till att använda korrekt URL-format
        HttpGet request = new HttpGet(baseUrl + "/name/" + name.replace(" ", "%20"));
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            if (response.getCode() != 200) {
                System.out.println("Fel! Statuskod: " + response.getCode() + " vid GET-förfrågan för namn.");
                return;
            }
            String jsonResp = EntityUtils.toString(response.getEntity());
            System.out.println("Shelter med namn " + name + ": " + jsonResp);
        }
    }

    public static void getOneShelter(long id) throws IOException, ParseException {
        HttpGet request = new HttpGet(baseUrl + "/" + id);
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            if (response.getCode() != 200) {
                System.out.println("Fel! Statuskod: " + response.getCode() + " vid GET-förfrågan för ID.");
                return;
            }
            String jsonResp = EntityUtils.toString(response.getEntity());
            System.out.println("Shelter med ID " + id + ": " + jsonResp);
        }
    }

    public static void updateAvailableBeds(long id, String availableBeds) throws IOException, ParseException {
        HttpPut request = new HttpPut(baseUrl + "/" + id + "/available-beds");
        StringEntity jsonPayload = new StringEntity(availableBeds, ContentType.APPLICATION_JSON);
        request.setEntity(jsonPayload);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            if (response.getCode() != 204) {
                System.out.println("Fel! Statuskod: " + response.getCode() + " vid uppdatering av sängar.");
            } else {
                System.out.println("Antal tillgängliga sängar uppdaterat för shelter med ID " + id);
            }
        }
    }

    public static void updateOneShelter(long id, Shelter shelter) throws IOException, ParseException {
        HttpPut request = new HttpPut(baseUrl + "/" + id);
        StringEntity jsonPayload = new StringEntity(convertShelterToJson(shelter), ContentType.APPLICATION_JSON);
        request.setEntity(jsonPayload);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            if (response.getCode() != 200) {
                System.out.println("Fel! Statuskod: " + response.getCode() + " vid uppdatering av shelter.");
            } else {
                String jsonResp = EntityUtils.toString(response.getEntity());
                System.out.println("Shelter uppdaterat: " + jsonResp);
            }
        }
    }

    public static void createNewShelter(Shelter shelter) throws IOException, ParseException {
        HttpPost request = new HttpPost(baseUrl);
        StringEntity jsonPayload = new StringEntity(convertShelterToJson(shelter), ContentType.APPLICATION_JSON);
        request.setEntity(jsonPayload);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            if (response.getCode() != 201) {
                System.out.println("Fel! Statuskod: " + response.getCode() + " vid skapande av shelter.");
            } else {
                String jsonResp = EntityUtils.toString(response.getEntity());
                System.out.println("Nytt shelter skapat: " + jsonResp);
            }
        }
    }

    public static void deleteOneShelter(long id) throws IOException, ParseException {
        HttpDelete request = new HttpDelete(baseUrl + "/" + id);
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            if (response.getCode() != 204) {
                System.out.println("Fel! Statuskod: " + response.getCode() + " vid borttagning av shelter.");
            } else {
                System.out.println("Shelter med ID " + id + " borttaget.");
            }
        }
    }
}
