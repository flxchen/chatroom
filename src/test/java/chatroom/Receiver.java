package chatroom;

import java.io.IOException;
import java.net.Socket;
import protocol.Protocol;

/**
 * @author jialiang Chen
 */
public class Receiver implements Runnable{
  private Socket receiverSocket;
  private Protocol protocol;
  public Receiver() {
  }

  public Receiver(Protocol protocol) {
    this.protocol = protocol;
  }

  public void run(){
    try {
      this.protocol.connectToServerIO(this.receiverSocket);
      while (this.receiverSocket.isConnected()) {
        this.protocol.listenServerResponse();
      }
    }catch (InterruptedException | IOException e){
      System.out.println(e.getMessage());
    }
  }

  public Socket getReceiverSocket() {
    return receiverSocket;
  }

  public void setReceiverSocket(Socket receiverSocket) {
    this.receiverSocket = receiverSocket;
  }

  public Protocol getProtocol() {
    return protocol;
  }

  public void setProtocol(Protocol protocol) {
    this.protocol = protocol;
  }
}
