package org.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        // Load configuration if available
        Configuration.loadConfiguration();

        // Optionally, use CLI if no config file exists or user opts for CLI
        // If you want to prompt the user, you can uncomment the line below
        // Configuration.getUserConfiguration();

        // Now you can pass these parameters to your system logic
        TicketPool ticketPool = new TicketPool(Configuration.maxTicketCapacity);

        // Start Vendors
        ExecutorService executor = Executors.newFixedThreadPool(4); // Pool for threads
        executor.execute(new Vendor(ticketPool, "Vendor1", Configuration.ticketReleaseRate));
        executor.execute(new Vendor(ticketPool, "Vendor2", Configuration.ticketReleaseRate));

        // Start Customers
        executor.execute(new Customer(ticketPool, "Customer1"));
        executor.execute(new Customer(ticketPool, "Customer2"));

        // Shutdown executor after 10 seconds
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executor.shutdownNow();
    }
}
