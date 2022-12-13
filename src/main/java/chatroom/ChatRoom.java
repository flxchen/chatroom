package chatroom;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * main class, which contain the server side, it is used to connect all the client to the server
 */

public class ChatRoom {

  /**
   * main function to run the server side
   * @param args command line argument passed are "localhost" and "port number"
   */

  public static void main(String[] args) {
    int capacity = 10, port = 8000;
    try (ServerSocket serversocket = new ServerSocket(port,capacity)) {
      System.out.println("start server at:"+ InetAddress.getLocalHost()+" port:"+port);
      ActiveClientList clientList = new ActiveClientList(capacity);
      while (true) {
        Socket socket = serversocket.accept();
        System.out.println("connect:"+socket.getLocalSocketAddress());
        ClientHandler clientHandler = new ClientHandler(socket,clientList);
        Thread listenClientRequest = new Thread(clientHandler);
        listenClientRequest.start();
      }
    } catch (IOException e) {
      System.out.println("Could not listen on port " + port);
    }
  }

  @Override
  public String toString() {
    return "ChatRoom{}";
  }
}
