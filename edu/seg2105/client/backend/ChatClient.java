// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package edu.seg2105.client.backend;

import ocsf.client.*;

import java.io.*;

import edu.seg2105.client.common.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 
  private String loginId; // Instance variable to store the login ID

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String loginId,String host, int port, ChatIF clientUI) throws IOException {
	  super(host, port); // Call the superclass constructor
      this.clientUI = clientUI;

      // Check if loginId is provided
      if (loginId == null || loginId.isEmpty()) {
          clientUI.display("Error: Login ID is required. Terminating client.");
          quit();
      } 
      
      else {
          this.loginId = loginId;
          }

      openConnection();
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
    
    
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    try
    {
    	if (message.startsWith("#")) {
    		handleCommand(message);
    	}
    	else {
    		sendToServer(message);
    	}
    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }
 
  
  
  
  private void handleCommand(String command) {
	  try {
		  if (command.equals("#quit")) {
			  quit();  
		  }
		  else if (command.equals("#logoff")) {
			  if (isConnected()) {
	              closeConnection();
	          } else {
	              clientUI.display("Not connected to any server");
	          }
		  }
	
		  else if (command.equals("#login")) {
			  if (!isConnected()) {
	              openConnection();
	          } else {
	              clientUI.display("Already connected to the server");
	          }
		  }
		  else if (command.startsWith("#sethost")) {
			  if (!isConnected()) {
				  int start = command.indexOf('<') + 1; // Find the start of host
				  int end = command.indexOf('>');       // Find the end of host
				  if (start > 0 && end > start) {
				        String newHost = command.substring(start, end);
				  
				  setHost(newHost);
				  clientUI.display("Host set to " + newHost);
				  }
	            } 
			  else {
				  clientUI.display("Cannot change host while connected");
	            }  
		  }
		  else if (command.startsWith("#gethost")) {
			  clientUI.display("Current host: " + getHost());  
		  }
		  else if (command.startsWith("#setport")) {
			  if (!isConnected()) {
				  try {
					  int start = command.indexOf('<') + 1; // Find the start of host
					  int end = command.indexOf('>');       // Find the end of host
					  if (start > 0 && end > start) {
					        String newPort = command.substring(start, end);
					        
					  setPort(Integer.parseInt(newPort));
					  clientUI.display("Port set to " + newPort);
					  }
	                }
				  catch (NumberFormatException e) {
					  clientUI.display("Invalid port number");
	                }
	            } 
			  else {
	                clientUI.display("Cannot change port while connected");
	            }  
		  }
		  else if (command.equals("#getport")) {
			  clientUI.display("Current port: " + getPort());  
		  }
		  
		  }
	  catch (IOException e) {
	        clientUI.display("An error occurred: " + e.getMessage());
	    }
	 
}
  
  
  @Override
  protected void connectionEstablished() {
      try {
          sendToServer("#login " + "<" +loginId + ">"); // Send #login <loginId> to the server
      } catch (IOException e) {
          clientUI.display("Error: Could not send login message to server.");
          quit();
      }
  }

	  
	  
	  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
  
  //////////////////////////////////////////
  @Override
  protected void connectionException(Exception exception) {
	  clientUI.display("The server is shut down");
	  quit();
	  //System.exit(0);
	}
  @Override
  protected void connectionClosed() {
	  clientUI.display("Connection closed");
	}
  
  
}
//End of ChatClient class


