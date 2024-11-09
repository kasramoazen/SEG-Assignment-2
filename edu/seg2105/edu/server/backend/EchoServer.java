package edu.seg2105.edu.server.backend;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 


import ocsf.server.*;
import java.io.IOException;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
	
	String loginKey = "loginID";
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  
  private ServerConsole console;

  public EchoServer(int port, ServerConsole console) {
      super(port);
      this.console = console;
     
      //console = new ServerConsole(this);
      
      try {
    	 
          listen(); // Automatically start listening on the specified port
      } catch (IOException e) {
          System.out.println("Error: Could not start server on port " + getPort());
      }
  }
  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
	  
    String msgStr = (String)msg;
    
    
    try {
        if (msgStr.startsWith("#login")) {
            // Check if the client has already sent a login command
            if (client.getInfo(loginKey) != null) {
                client.sendToClient("Error: Login command can only be sent once.");
                client.close();
                return;
            }
            
            
            else {
    			System.out.println(msgStr);
    			String[] parts = msgStr.split(" ");
    			client.setInfo(loginKey, parts[1]);
    			this.sendToAllClients(parts[1] + " has logged on.");
    			
            }

            // Extract login ID and save it using setInfo
//            String loginId = msgStr.substring(7).trim(); // Extracts text after "#login "
//            if (loginId.isEmpty()) {
//                client.sendToClient("Error: Login ID cannot be empty.");
//                client.close();
//                return;
//            }
//
//            client.setInfo(loginKey, loginId);
//            sendToAllClients(loginId + " has joined."); // Notify other clients
//            System.out.println("Client " + loginId + " has connected.");
        } 
        else {
    

            // Prefix message with login ID and send to all clients
            System.out.println("Message received: " + msg + " from " + client.getInfo(loginKey));
            this.sendToAllClients(client.getInfo(loginKey) +":"+ msg);
        }
        

    } 
    catch (IOException e) {
        System.out.println("Error handling message from client: " + e.getMessage());
    }
}
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for clients on port " + getPort());
    
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  
  
  
  @Override
  protected void clientConnected(ConnectionToClient client) {
	  System.out.println("A new client has connected: ");
	  
  }
  
  
  @Override
  synchronized protected void clientDisconnected(ConnectionToClient client) {
	  System.out.println(client.getInfo("loginID") + " has disconnected.");
	  
  }
  
  
  @Override
  protected synchronized void clientException(ConnectionToClient client, Throwable exception) {
    System.out.println(client.getInfo("loginID") + " has disconnected.");
  }
  
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(port,null);
    
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
    ServerConsole serverConsole = new ServerConsole(sv); 
    serverConsole.accept();
  }
}
//End of EchoServer class
