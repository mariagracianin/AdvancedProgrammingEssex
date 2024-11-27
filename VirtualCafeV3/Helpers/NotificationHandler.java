package Helpers;

import Helpers.VirtualCafe;

import java.io.PrintWriter;

public class NotificationHandler implements Runnable {
    private final VirtualCafe virtualCafe;
    private final String customerName;
    private final PrintWriter writer;
    private boolean lastNotificationSend = false;
    private volatile boolean running = true;

    public NotificationHandler(VirtualCafe virtualCafe, String customerName, PrintWriter writer) {
        this.virtualCafe = virtualCafe;
        this.customerName = customerName;
        this.writer = writer;
    }

    @Override
    public void run() {
        while (running) {
            if (!lastNotificationSend) {
                try{
                    String notification = virtualCafe.notifyOrderReady(customerName);
                    if (notification != null) {
                        writer.println(notification);
                        lastNotificationSend = true;
                    }
                }catch (Exception e){

                }
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void resetLastNotification() {
        lastNotificationSend = false;
    }

    public void stop() {
        running = false; // Detiene el hilo
    }
}
