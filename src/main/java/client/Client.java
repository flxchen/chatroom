package client;


import java.io.IOException;
import java.net.Socket;
import problem1.sentencegenerator.GrammarNotDefinedException;

/**
 * main class for running the client side, each client can be added to server by providing the port number and computer name of the server
 */

public class Client {

  /**
   * main function to run each clients
   * @param args command line argument passed are "localhost" and "port number"
   */
  public static void main(String[] args) {
    try {
      IPHostChecker ipHostChecker = new IPHostChecker(args);
      Socket socket = new Socket(ipHostChecker.getHostName(),ipHostChecker.getPort());
      ClientDriver clientDriver = new ClientDriver(socket);
      clientDriver.getUserName();
      clientDriver.listenServerResponse();
      clientDriver.listenUserInput();
    }catch (InvalidUserInputException | GrammarNotDefinedException | IOException |
            InterruptedException e){
      System.out.println(e.getMessage());
    }
  }

  @Override
  public String toString() {
    return "Client{}";
  }
}