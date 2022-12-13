package chatroom;

import static org.junit.jupiter.api.Assertions.*;

import client.Client;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.Socket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import protocol.Protocol;

/**
 * @author jialiang Chen
 */
class ChatRoomTest {
  private Socket testSocket1;
  private String host = "localhost";
  private int port = 8000;
  private Receiver receiver;
  private Protocol protocol;
  private Messenger messenger;
  private ChatRoom chatRoom;
  private Client client;

  @BeforeEach
  void setUp() throws IOException {
    protocol = new Protocol();
    messenger = new Messenger();
    receiver = new Receiver();
    chatRoom = new ChatRoom();
  }



  @Test
  void clientConnection() {
    new Thread(messenger).start();
    String input = "cjl";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
    System.setIn(inputStream);
    String[] IPHost = {host,String.valueOf(port)};
    Client.main(IPHost);
  }
  @Test
  void clientConnection_invalidIPHost(){
    new Thread(messenger).start();
    String input = "cjl";
    ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
    System.setIn(inputStream);
    String extra =  "foo";
    String[] IPHost = {host,String.valueOf(port),extra};
    Client.main(IPHost);
  }

  @Test
  void clientToString() {
    String expect = "Client{}";
    client = new Client();
    assertEquals(expect,client.toString());
  }

  @Test
  void test_toString() {
    String expect = "ChatRoom{}";
    assertEquals(expect,chatRoom.toString());
  }
}