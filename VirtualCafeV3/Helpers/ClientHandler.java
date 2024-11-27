package Helpers;// Helpers.ClientHandler.java
import Helpers.VirtualCafe;

import java.io.PrintWriter;   import java.net.Socket;   import java.util.Scanner;

public class ClientHandler implements Runnable
{   private final Socket socket;
    private VirtualCafe virtualCafe;

    public ClientHandler( Socket socket, VirtualCafe virtualCafe ) {
        this.socket = socket;
        this.virtualCafe = virtualCafe;
    }

    @Override
    public void run()
    {   String customerName = "";
        try (Scanner scanner    = new Scanner( socket.getInputStream() );
             PrintWriter writer = new PrintWriter( socket.getOutputStream(), true ) )
        {   try
            {   customerName = scanner.nextLine();
                if (virtualCafe.customerAlreadyExists(customerName)){
                    writer.println("ERROR A client with that name already exists, please log in with another name");
                }else{
                    System.out.println( "New connection! customer name " + customerName);
                    writer.println( "SUCCESS" ); //Para que el cliente sepa que pudo conectarse, sino se corta
                    virtualCafe.addOrder(customerName, 0, 0);
                }

                // Thread for notifications
                NotificationHandler notificationHandler = new NotificationHandler(virtualCafe, customerName, writer);
                Thread notificationThread = new Thread(notificationHandler);
                notificationThread.start();


                while (true)
                {
                    try {
                        String line = scanner.nextLine();

                        String[] substrings = line.split(" ");
                        switch (substrings[0].toLowerCase()) {
                            case "order":
                                virtualCafe.addOrder(customerName, Integer.parseInt(substrings[1]), Integer.parseInt(substrings[2]));
                                notificationHandler.resetLastNotification();
                                writer.println("Order received for " + customerName + " (" + substrings[1] + " teas and " + substrings[2] + " coffess)");
                                break;

                            case "order_status":
                                writer.println(virtualCafe.getOrderStatus(customerName));
                                break;

                            case "collect":
                                writer.println(virtualCafe.collect(customerName));
                                break;

                            case "exit":
                                notificationHandler.stop();
                                writer.println(virtualCafe.exit(customerName));
                                notificationHandler.stop();
                                socket.close();
                                break;

                            default:
                                throw new Exception("Unknown command: " + substrings[0]);
                        }
                    }catch (Exception e){
                        notificationHandler.stop();
                        virtualCafe.exit(customerName); //The client disconnected; I want to delete all their information as if they had executed an exit
                        break;
                    }
                }
            }
            catch (Exception e) { writer.println("ERROR " + e.getMessage()); socket.close(); }
        }
        catch (Exception e) { }
        finally { System.out.println("Customer " + customerName + " disconnected."); }
    } // end of 'run' method
}