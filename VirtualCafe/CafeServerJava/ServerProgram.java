// ServerProgram.java
import Helpers.VirtualCafe;

import java.io.IOException;   import java.net.ServerSocket;   import java.net.Socket;

public class ServerProgram
{   private final static int port = 8080;
    private static final VirtualCafe virtualCafe = new VirtualCafe();

    public static void main(String[] args) {
        new Thread(virtualCafe).start();
        RunServer();
    }

    private static void RunServer()
    {   ServerSocket serverSocket = null;   // passive socket, used for 'listening'
        try
        {   serverSocket = new ServerSocket( port );  // bind port and start listening
            System.out.println( "Waiting for incoming connections..." );
            while (true)
            {   Socket socket = serverSocket.accept();  // accept incoming connections (blocks until it does!)
                new Thread(new ClientHandler(socket, virtualCafe)).start();
            }
        }
        catch (IOException e) { e.printStackTrace(); }
    }
}
