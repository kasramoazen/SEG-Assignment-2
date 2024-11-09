package edu.seg2105.edu.server.backend;
import edu.seg2105.client.common.ChatIF;



import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.io.IOException;

public class ServerConsole implements ChatIF {
	ChatIF serverUI;
	Scanner fromServer;
    private EchoServer server;

    public ServerConsole(EchoServer server) {
        this.server = server;
        
        //accept();
    }

    @Override
    public void display(String message) {
    	
        System.out.println("SERVER MSG> " + message);
    }

    public void accept() {
    	
    	fromServer = new Scanner(System.in); 
    	String message;
    	
        try {
  
        
            while (true) {
            	message = fromServer.nextLine();
                if (message.startsWith("#")) {
                    handleCommand(message);
                } 
                else {
                    // Prefix and broadcast to all clients and display on server console
                    server.sendToAllClients("SERVER MSG> " + message);
                    display(message);
                }
            }
        } catch (Exception ex) {
            System.out.println("Unexpected error while reading from console!");
        }
    }

    private void handleCommand(String command) {
    	
        try {
            if (command.equals("#quit")) {
                server.close();
                System.exit(0);
            } 
            else if (command.equals("#stop")) {
                server.stopListening();
                
            } 
            else if (command.equals("#close")) {
                server.close();
                
            } 
            else if (command.startsWith("#setport")) {
                if (!server.isListening()) {
                    int port = Integer.parseInt(command.substring(9).trim());
                    server.setPort(port);
                    display("Port set to " + port);
                } 
                else {
                    display("Error: Server must be closed to set port.");
                }
            } 
            else if (command.equals("#start")) {
            	
            	if (!server.isListening()) {
                    try {
                    	server.listen();
                    } catch (IOException e) {}
                  } 
            	else {
                    serverUI.display("Server already listening.");
                  }

            } 
            else if (command.equals("#getport")) {
                display("Current port: " + server.getPort());
            } 
            else {
                display("Unknown command.");
            }
        } 
        catch (IOException e) {
            display("Command error: " + e.getMessage());
        }
    }
}
