import java.io.*;
import java.net.*;

public class ChatServer {

    public static void main(String[] args) {

        int port = 5000;

        try {
            // Create server socket
            ServerSocket serverSocket = new ServerSocket(port);

            System.out.println("Server started.....");
            System.out.println("Waiting for Client connection....");

            // Accept client connection
            Socket socket = serverSocket.accept();

            System.out.println("Client connected!");

            // Input stream: Read messages from client
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );

            // Output stream: Send messages to client
            PrintWriter output = new PrintWriter(
                    socket.getOutputStream(), true
            );

            // Thread for reading messages from client
            Thread readThread = new Thread(() -> {
                try {
                    String message;

                    while ((message = input.readLine()) != null) {

                        System.out.println("\nClient: " + message);

                        if (message.equalsIgnoreCase("exit")) {
                            System.out.println("Client disconnected.");
                            break;
                        }
                    }

                } catch (IOException e) {
                    System.out.println("Connection closed.");
                }
            });

            // Thread for writing messages to client
            Thread writeThread = new Thread(() -> {
                try {
                    BufferedReader keyboard = new BufferedReader(
                            new InputStreamReader(System.in)
                    );

                    String message;

                    while (true) {

                        System.out.print("Server: ");
                        message = keyboard.readLine();

                        output.println(message);

                        if (message.equalsIgnoreCase("exit")) {
                            break;
                        }
                    }

                } catch (IOException e) {
                    System.out.println("Error sending message.");
                }
            });

            // Start both threads
            readThread.start();
            writeThread.start();

            // Wait for writing thread to finish
            writeThread.join();

            // Close connections
            socket.close();
            serverSocket.close();

            System.out.println("Server stopped.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}