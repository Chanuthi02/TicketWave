package org.example;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class UserInterface extends Application {
    private Thread vendorThread;
    private Thread customerThread;

    @Override
    public void start(Stage primaryStage) {
        TicketPool ticketPool = new TicketPool(Configuration.MAX_TICKET_CAPACITY);

        // UI Components
        Button startButton = new Button("Start System");
        Button stopButton = new Button("Stop System");
        stopButton.setDisable(true);

        Label ticketCountLabel = new Label("Tickets Available: 0");
        TextArea logArea = new TextArea();
        logArea.setEditable(false);

        // Layout
        VBox layout = new VBox(10);
        layout.getChildren().addAll(startButton, stopButton, ticketCountLabel, logArea);

        // Start Button Action
        startButton.setOnAction(e -> {
            logArea.appendText("System Starting...\n");

            vendorThread = new Thread(createVendorTask(ticketPool, logArea));
            customerThread = new Thread(createCustomerTask(ticketPool, logArea));
            vendorThread.start();
            customerThread.start();

            startButton.setDisable(true);
            stopButton.setDisable(false);

            // Update ticket count periodically
            new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        Thread.sleep(1000); // Update every second
                        int count = ticketPool.getTicketCount();
                        ticketCountLabel.setText("Tickets Available: " + count);
                    } catch (InterruptedException ignored) {
                        Thread.currentThread().interrupt();
                    }
                }
            }).start();
        });

        // Stop Button Action
        stopButton.setOnAction(e -> {
            if (vendorThread != null) vendorThread.interrupt();
            if (customerThread != null) customerThread.interrupt();
            logArea.appendText("System Stopped.\n");

            startButton.setDisable(false);
            stopButton.setDisable(true);
        });

        // Scene Setup
        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setTitle("Event Ticketing System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Task<Void> createVendorTask(TicketPool ticketPool, TextArea logArea) {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                Vendor vendor = new Vendor(ticketPool, "Vendor1", Configuration.TICKET_RELEASE_RATE);
                vendor.run();
                return null;
            }
        };
    }

    private Task<Void> createCustomerTask(TicketPool ticketPool, TextArea logArea) {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                Customer customer = new Customer(ticketPool, "Customer1");
                customer.run();
                return null;
            }
        };
    }

    public static void main(String[] args) {
        launch(args);
    }
}
