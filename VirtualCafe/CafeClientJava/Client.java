import java.io.*;   import java.net.Socket;   import java.util.Scanner;

public class Client implements AutoCloseable  // i.e. can use in a try-with-resources block
{   final int port = 8080; private final Scanner reader; private final PrintWriter writer;

    public Client( String customerName ) throws Exception {
        Socket socket = new Socket( "localhost", port );            // Connecting to the server,
        reader = new Scanner( socket.getInputStream() );            //   and create objects for communications;
        writer = new PrintWriter( socket.getOutputStream(), true ); // Provides nice 'println' functions (which flush automatically!)
        writer.println(customerName);        // Send customer name
        String line = reader.nextLine();   // Parse the response
        if (line.trim().compareToIgnoreCase( "success" ) != 0) throw new Exception( line );
    }

//    public int[] getAccountNumbers()
//    {   writer.println("ACCOUNTS");                      // Send "command"
//        String line = reader.nextLine();                 // Read response (expecting: number of accounts)
//        int numberOfAccounts = Integer.parseInt( line ); // Parse response
//        int [] accounts = new int[ numberOfAccounts ];   // Create array to hold accounts
//        for (int i = 0; i < numberOfAccounts; i++) { line = reader.nextLine();  accounts[ i ] = Integer.parseInt( line ); } // Populate accounts
//        return accounts;   // Return accounts array (or, to be exact, a reference to the array)
//    }
//
//    public int getBalance( int accountNumber )
//    {   writer.println("BALANCE " + accountNumber);   // Send "command"
//        String line = reader.nextLine();              // Read response (expecting: balance)
//        return Integer.parseInt(line);                // Parse Response
//    }
//
//    public void transfer( int fromAccount, int toAccount, int amount ) throws Exception
//    {   writer.println( "TRANSFER " +fromAccount+ " " +toAccount+ " " +amount);   // Send command
//        String line = reader.nextLine();                                          // Read response (expecting: 'success')
//        if (line.trim().compareToIgnoreCase( "success" ) != 0) throw new Exception( line );
//    }

    @Override
    public void close() throws Exception {  // Implement AutoCloseable interface!
        reader.close();
        writer.close();
    }

    // ABAJO VA LO MIO

    public String setOrder(int numTeas, int numCoffees){
        writer.println("ORDER " + numTeas + " " + numCoffees);                 // Send "command"
        String line = reader.nextLine();                                    // Read response (expecting: balance)
        return line;
    }

    public String getOrderStatus() {
        writer.println("ORDER_STATUS"); // Send "command"
        StringBuilder fullResponse = new StringBuilder();
        // Leer todas las líneas que lleguen
        while (reader.hasNextLine()) {
            String line = reader.nextLine();

            // Condición de salida
            if (line.equalsIgnoreCase("END")) {
                break;
            }

            // Agregar la línea al resultado
            fullResponse.append(line).append("\n");
        }

        return fullResponse.toString().trim(); // Elimina saltos de línea al final
    }

    public String collectOrder(){
        writer.println("COLLECT");                      // Send "command"
        String line = reader.nextLine();                // Read response (expecting: respuesta a  collect order)
        return line;
    }

    public String exitCafe(){
        writer.println("EXIT");                      // Send "command"
        String line = reader.nextLine();                // Read response (expecting: balance)
        return line;
    }


}
