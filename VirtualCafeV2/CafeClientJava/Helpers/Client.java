package Helpers;

import java.io.*;   import java.net.Socket;   import java.util.Scanner;

public class Client implements AutoCloseable  // i.e. can use in a try-with-resources block
{   final int port = 8080;
    private final Scanner reader;
    private final PrintWriter writer;

    public Client( String customerName ) throws Exception {
        Socket socket = new Socket( "localhost", port );            // Connecting to the server,
        reader = new Scanner( socket.getInputStream() );            //   and create objects for communications;
        writer = new PrintWriter( socket.getOutputStream(), true ); // Provides nice 'println' functions (which flush automatically!)
        writer.println(customerName);        // Send customer name
        String line = reader.nextLine();   // Parse the response

        new Thread(() -> {
            while (true) {
                try{
                    System.out.println(reader.nextLine());
                }catch (Exception e){

                }
            }
        }).start();

        if (line.trim().compareToIgnoreCase( "success" ) != 0) throw new Exception( line );
    }

    @Override
    public void close() throws Exception {  // Implement AutoCloseable interface!
        reader.close();
        writer.close();
    }

    public void setOrder(int numTeas, int numCoffees){
        writer.println("ORDER " + numTeas + " " + numCoffees);                 // Send "command"
    }

    public void getOrderStatus() {
        writer.println("ORDER_STATUS"); // Send "command"
    }

    public void collectOrder(){
        writer.println("COLLECT");                      // Send "command"
    }

    public void exitCafe(){
        writer.println("EXIT");                      // Send "command"
    }
}
