package chatroom;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import java.util.Objects;
import problem1.sentencegenerator.GrammarNotDefinedException;
import protocol.DataFrame;
import protocol.Protocol;

/**
 * class client handler handles a client socket connected to server, it listens client requests and responds to them
 */

public class ClientHandler implements Runnable {
  private Socket clientSocket;
  private Protocol protocol;

  /**
   * constructor of the class
   * @param clientSocket socket details of the client
   * @param activeClientList set of active client list
   */
  public ClientHandler(Socket clientSocket, ActiveClientList activeClientList) {
    this.clientSocket = clientSocket;
    this.protocol = new Protocol(activeClientList,new DataFrame());
  }


  /**
   * empty constructor of the class
   */
  public ClientHandler() {
  }
  /**
   * thread created for running parallel clients
   */
  @Override
  public void run() {
    while (this.clientSocket.isConnected()){
      try {
        this.protocol.connectClientIO(clientSocket);
        System.out.println(this.protocol.getSTATE());
        this.protocol.listenClientRequest();
        System.out.println(this.protocol.getSTATE());
        this.protocol.processClientRequest();
        if(this.protocol.getDataFrame().isDisconnect() && this.protocol.getDataFrame().isSuccess()|| this.clientSocket.isClosed()){
          closeSocket();
          break;
        }
      }catch (IOException e) {
        closeSocket();
        removeClient();
        System.out.println("IOException: Could not connect to " + this.clientSocket.getLocalSocketAddress());
        break;
      }catch (GrammarNotDefinedException | InterruptedException e){
        closeSocket();
        removeClient();
        System.out.println(e.getMessage());
      }
    }
  }

  /**
   * function to close the socket of the client
   */
  public void closeSocket(){
    try {
      this.clientSocket.close();
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * function to remove the client
   */
  public void removeClient(){
    //Map.Entry<String,Socket> pair = this.protocol.getActiveClientList().findClientSocketPair(this.protocol.getDataFrame().getSenderUserName());
    this.protocol.getActiveClientList().removeFromClientList(this.protocol.getActiveClientList().findClientSocketPair(this.protocol.getDataFrame().getSenderUserName()));
  }

  /**
   * function to get the client socket details
   * @return clientSocket
   */
  public Socket getClientSocket() {
    return clientSocket;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ClientHandler that = (ClientHandler) o;
    return Objects.equals(clientSocket, that.clientSocket) && Objects.equals(
        protocol, that.protocol);
  }

  @Override
  public int hashCode() {
    return Objects.hash(clientSocket, protocol);
  }

  @Override
  public String toString() {
    return "ClientHandler{" +
        "clientSocket=" + clientSocket +
        ", protocol=" + protocol +
        '}';
  }
}