package chatroom;

import java.net.Socket;

/**
 * @author jialiang Chen
 */
public class Messenger implements Runnable{
  private Socket socket;

  public Messenger() {
  }

  public void run(){
    String[] args = {};
     ChatRoom.main(args);
  }

  public Socket getSocket() {
    return socket;
  }

  public void setSocket(Socket socket) {
    this.socket = socket;
  }
}
