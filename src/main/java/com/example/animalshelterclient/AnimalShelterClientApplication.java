package com.example.animalshelterclient;

import com.example.animalshelterclient.models.Shelter;
import com.example.animalshelterclient.services.ServiceManager;
import org.apache.hc.core5.http.ParseException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.Scanner;

@SpringBootApplication
public class AnimalShelterClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnimalShelterClientApplication.class, args);
        Scanner scanner = new Scanner(System.in);

        // Välj mellan localhost eller AWS
        System.out.println("Välj miljö: 1 för Localhost, 2 för AWS (Elastic Beanstalk): ");
        String choice = scanner.nextLine();
        String baseUrl;

        if ("1".equals(choice)) {
            baseUrl = "http://localhost:5000/shelter"; // Localhost
        } else if ("2".equals(choice)) {
            baseUrl = "http://your-aws-url/shelter"; // AWS Elastic Beanstalk URL
        } else {
            System.out.println("Ogiltigt val, avslutar programmet.");
            return;
        }

        try {
            // Demonstrera några operationer
            // 1. GET - Hämta alla shelters
            System.out.println("Skickar GET-förfrågan för att hämta alla shelters...");
            ServiceManager.sendGetRequest(baseUrl);

            // 2. POST - Skapa ett nytt shelter
            Shelter newShelter = new Shelter("New Shelter", "123 Main Street", 50);
            System.out.println("Skickar POST-förfrågan för att skapa ett nytt shelter...");
            ServiceManager.sendPostShelterRequest(baseUrl, newShelter);

            // 3. PUT - Uppdatera befintligt shelter med ID 1
            Shelter updatedShelter = new Shelter("Updated Shelter", "456 Main Street", 60);
            updatedShelter.setId(1); // Exempel-ID, ska normalt hämtas från en GET-förfrågan
            System.out.println("Skickar PUT-förfrågan för att uppdatera shelter med ID 1...");
            ServiceManager.sendPutShelterRequest(baseUrl + "/1", updatedShelter);

            // 4. DELETE - Ta bort ett shelter med ID 1
            System.out.println("Skickar DELETE-förfrågan för att ta bort shelter med ID 1...");
            ServiceManager.sendDeleteShelterRequest(baseUrl + "/1");

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
