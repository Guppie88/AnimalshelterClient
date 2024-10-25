package com.example.animalshelterclient;

import com.example.animalshelterclient.models.Shelter;
import com.example.animalshelterclient.services.ServiceManager;
import org.apache.hc.core5.http.ParseException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.Scanner;

@SpringBootApplication
public class AnimalShelterClientApplication implements CommandLineRunner {

    private static final String AWS_BASE_URL = "http://animalshelterapi-env.eba-mbz3mefy.eu-north-1.elasticbeanstalk.com/shelter";
    private static final String SWAGGER_URL = "http://animalshelterapi-env.eba-mbz3mefy.eu-north-1.elasticbeanstalk.com/swagger-ui/index.html";

    public static void main(String[] args) {
        SpringApplication.run(AnimalShelterClientApplication.class, args);
    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);

        // Välj mellan AWS eller Swagger-dokumentation
        System.out.println("Välj: 1 för att anropa AWS (Elastic Beanstalk), 2 för Swagger-dokumentation: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                runCrudOperations(AWS_BASE_URL);
                break;
            case "2":
                System.out.println("Öppnar Swagger-dokumentationen...");
                try {
                    ServiceManager.sendGetRequest(SWAGGER_URL);
                } catch (IOException | ParseException e) {
                    System.out.println("Kunde inte hämta Swagger-dokumentationen: " + e.getMessage());
                }
                break;
            default:
                System.out.println("Ogiltigt val, avslutar programmet.");
        }
    }

    private void runCrudOperations(String baseUrl) {
        try {
            System.out.println("Skickar GET-förfrågan för att hämta alla shelters...");
            ServiceManager.sendGetRequest(baseUrl);

            Shelter newShelter = new Shelter("New Shelter", "123 Main Street", 50);
            System.out.println("Skickar POST-förfrågan för att skapa ett nytt shelter...");
            ServiceManager.sendPostShelterRequest(baseUrl, newShelter);

            Shelter updatedShelter = new Shelter("Updated Shelter", "456 Main Street", 60);
            updatedShelter.setId(1); // Exempel-ID
            System.out.println("Skickar PUT-förfrågan för att uppdatera shelter med ID 1...");
            ServiceManager.sendPutShelterRequest(baseUrl + "/1", updatedShelter);

            System.out.println("Skickar DELETE-förfrågan för att ta bort shelter med ID 1...");
            ServiceManager.sendDeleteShelterRequest(baseUrl + "/1");

        } catch (IOException | ParseException e) {
            System.out.println("Ett fel inträffade vid anropet: " + e.getMessage());
        }
    }
}
