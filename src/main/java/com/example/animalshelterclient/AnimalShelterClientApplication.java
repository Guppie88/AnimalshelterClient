package com.example.animalshelterclient;

import com.example.animalshelterclient.models.Shelter;
import com.example.animalshelterclient.services.ServiceManager;
import org.apache.hc.core5.http.ParseException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

@SpringBootApplication
public class AnimalShelterClientApplication implements CommandLineRunner {

    private static final String LOCAL_BASE_URL = "http://localhost:5000/shelter";
    private static final String AWS_BASE_URL = "http://animalshelterapi-env.eba-mbz3mefy.eu-north-1.elasticbeanstalk.com/shelter";
    private static final String SWAGGER_URL = "http://localhost:5000/swagger-ui/index.html"; // URL för Swagger-lokalt

    public static void main(String[] args) {
        SpringApplication.run(AnimalShelterClientApplication.class, args);
    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Välj miljö: 1 för Localhost, 2 för AWS (Elastic Beanstalk), 3 för Swagger-dokumentation: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                ServiceManager.setBaseUrl(LOCAL_BASE_URL);
                runCrudOperations();
                break;
            case "2":
                ServiceManager.setBaseUrl(AWS_BASE_URL);
                runCrudOperations();
                break;
            case "3":
                openSwaggerDocumentation();
                break;
            default:
                System.out.println("Ogiltigt val, avslutar programmet.");
        }
    }

    private void runCrudOperations() {
        try {
            System.out.println("Hämta alla shelters:");
            ServiceManager.getAllShelters();

            System.out.println("Skapa ett nytt shelter:");
            Shelter newShelter = new Shelter("Nytt Shelter", "Adress", 10);
            ServiceManager.createNewShelter(newShelter);

            System.out.println("Uppdatera ett shelters tillgängliga sängar:");
            ServiceManager.updateAvailableBeds(1, "5");

            System.out.println("Hämta ett shelter via namn:");
            ServiceManager.getShelterByName("Nytt Shelter");

            System.out.println("Ta bort ett shelter med ID 1:");
            ServiceManager.deleteOneShelter(1);

        } catch (IOException | ParseException e) {
            System.out.println("Ett fel inträffade vid anropet: " + e.getMessage());
        }
    }

    private void openSwaggerDocumentation() {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI(SWAGGER_URL));
                System.out.println("Swagger-dokumentation öppnad i webbläsaren.");
            } else {
                System.out.println("Desktop-läge stöds inte, öppna Swagger manuellt på: " + SWAGGER_URL);
            }
        } catch (IOException | URISyntaxException e) {
            System.out.println("Kunde inte öppna Swagger-dokumentationen: " + e.getMessage());
        }
    }
}
