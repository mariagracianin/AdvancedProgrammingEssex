// ClientHandler.java
import Helpers.VirtualCafe;

import java.io.PrintWriter;   import java.net.Socket;   import java.util.Scanner;

public class ClientHandler implements Runnable
{   private final Socket socket;
    private VirtualCafe virtualCafe;

    public ClientHandler( Socket socket, VirtualCafe virtualCafe ) { this.socket = socket; this.virtualCafe = virtualCafe; }

    @Override
    public void run()
    {   String customerName = "";
        boolean lastNotificationSend = false;
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

                while (true)
                {
                    if (socket.getInputStream().available() > 0){
                        String line = scanner.nextLine();
                        String[] substrings = line.split(" ");
                        switch (substrings[0].toLowerCase())
                        {   case "order":
                                virtualCafe.addOrder(customerName, Integer.parseInt(substrings[1]), Integer.parseInt(substrings[2]));
                                lastNotificationSend = false;
                                writer.println("order received for " + customerName + " (" + substrings[1] + " teas and " + substrings[2] + " coffess)" );
                                //TODO mejorar respuesta del server para 0 cafes o 1 en vez de muchos etc
                                break;

                            case "order_status":
                                writer.println(virtualCafe.getOrderStatus(customerName));
                                break;

                            case "collect":
                                writer.println(virtualCafe.collect(customerName));
                                break;

                            case "exit":
                                writer.println(virtualCafe.exit(customerName));
                                socket.close();
                                break;

                            default:   throw new Exception( "Unknown command: " + substrings[ 0 ] );
                        }
                    }else {
                        if (!lastNotificationSend) {
                            String notification = virtualCafe.notifyOrderReady(customerName);
                            if (notification != null) {
                                writer.println(notification);
                                lastNotificationSend = true;
                            }
                        }
                    }
                }
            }
            catch (Exception e) { writer.println("ERROR " + e.getMessage()); socket.close(); }
        }
        catch (Exception e) { }
        finally { System.out.println("Customer " + customerName + " disconnected."); }
    } // end of 'run' method
}
