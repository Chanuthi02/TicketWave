package org.example;

public class Customer implements Runnable {
    private final TicketPool ticketPool;
    private final String customerId;

    public Customer(TicketPool ticketPool, String customerId) {
        this.ticketPool = ticketPool;
        this.customerId = customerId;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String ticket = ticketPool.removeTicket();
                System.out.println("Customer " + customerId + " purchased: " + ticket);
                Thread.sleep(700); // Simulate ticket purchase every 0.7 seconds
            }
        } catch (InterruptedException e) {
            System.out.println("Customer " + customerId + " interrupted.");
        }
    }
}
