package org.example;

import com.google.gson.Gson;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.io.File;
import java.util.Scanner;

public class Configuration implements Serializable {
    public static final int MAX_TICKET_CAPACITY = 10;
    public static final int TICKET_RELEASE_RATE = 5;

    public static int totalTickets;
    public static int ticketReleaseRate;
    public static int customerRetrievalRate;
    public static int maxTicketCapacity;

    private static final String CONFIG_FILE = "config.json";
    private static final Scanner scanner = new Scanner(System.in);

    // Save the configuration to a JSON file
    public static void saveConfiguration() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            Gson gson = new Gson();
            gson.toJson(new Configuration(), writer);
        } catch (IOException e) {
            System.out.println("Error saving configuration: " + e.getMessage());
        }
    }

    // Load configuration from the JSON file or ask for input if the file doesn't exist
    public static void loadConfiguration() {
        File file = new File(CONFIG_FILE);
        if (file.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                Gson gson = new Gson();
                Configuration config = gson.fromJson(reader, Configuration.class);
                totalTickets = config.totalTickets;
                ticketReleaseRate = config.ticketReleaseRate;
                customerRetrievalRate = config.customerRetrievalRate;
                maxTicketCapacity = config.maxTicketCapacity;
            } catch (IOException e) {
                System.out.println("Error loading configuration: " + e.getMessage());
            }
        } else {
            // If config file doesn't exist, ask the user to input the configuration
            getUserConfiguration();
        }
    }

    // Ask the user for configuration input if the file doesn't exist
    public static void getUserConfiguration() {
        System.out.println("Please configure the system:");

        totalTickets = getValidInteger("Enter Total Number of Tickets:");
        ticketReleaseRate = getValidInteger("Enter Ticket Release Rate:");
        customerRetrievalRate = getValidInteger("Enter Customer Retrieval Rate:");
        maxTicketCapacity = getValidInteger("Enter Maximum Ticket Capacity:");

        // Save the configuration after getting the input
        saveConfiguration();
    }

    // Validate and get valid integer input from the user
    private static int getValidInteger(String prompt) {
        int value;
        while (true) {
            System.out.print(prompt + " ");
            String input = scanner.nextLine();
            try {
                value = Integer.parseInt(input);
                if (value > 0) {
                    return value;
                } else {
                    System.out.println("Value must be greater than zero. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }
}
