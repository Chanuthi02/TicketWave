package org.example;

public class Vendor implements Runnable {
    private final TicketPool ticketPool;
    private final String vendorId;
    private final int ticketsPerRelease;

    public Vendor(TicketPool ticketPool, String vendorId, int ticketsPerRelease) {
        this.ticketPool = ticketPool;
        this.vendorId = vendorId;
        this.ticketsPerRelease = ticketsPerRelease;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i <= ticketsPerRelease; i++) {
                String ticket = vendorId + "-Ticket-" + i;
                ticketPool.addTickets(ticket);
                System.out.println("Vendor " + vendorId + " added: " + ticket);
                Thread.sleep(500); // Simulate ticket release every 0.5 seconds
            }
        } catch (InterruptedException e) {
            System.out.println("Vendor " + vendorId + " interrupted.");
        }
    }
}
