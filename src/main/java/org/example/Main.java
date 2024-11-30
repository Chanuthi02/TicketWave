package org.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        TicketPool ticketPool = new TicketPool(Configuration.MAX_TICKET_CAPACITY);

        // Start Vendors
        ExecutorService executor = Executors.newFixedThreadPool(4); // Pool for threads
        executor.execute(new Vendor(ticketPool, "Vendor1", Configuration.TICKET_RELEASE_RATE));
        executor.execute(new Vendor(ticketPool, "Vendor2", Configuration.TICKET_RELEASE_RATE));

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
