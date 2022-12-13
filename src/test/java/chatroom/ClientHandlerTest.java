package chatroom;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClientHandlerTest {

  ClientHandler clientHandler;

  @BeforeEach
  void setUp() {
    clientHandler = new ClientHandler();
  }

  @Test
  void run() {
  }

  @Test
  void closeSocket() {
  }

  @Test
  void getClientSocket() {
  }

  @Test
  void testEqualsSameObject() {
    assertTrue(clientHandler.equals(clientHandler));
  }
  @Test
  void testEqualsNullObject() {
    assertFalse((clientHandler.equals(null) ) );
  }
  @Test
  void testEquals_similarObjects() {
    String test = new String("hello");
    assertFalse(clientHandler.equals(test));
  }

  void testEqualsDifferent() {
    ActiveClientList activeClientList = new ActiveClientList();
    assertTrue(clientHandler.equals(activeClientList));
  }
  @Test
  void testEqualsDifferentObject(){
    ClientHandler clientHandler1 = new ClientHandler();
    ClientHandler clientHandler2 = new ClientHandler();
    assertTrue(clientHandler1.equals(clientHandler2));
  }


  @Test
  void testHashCode() {
    ClientHandler clientHandler1 = new ClientHandler();
    ClientHandler clientHandler2 = new ClientHandler();
    assertEquals(clientHandler1.hashCode(),clientHandler2.hashCode());
  }

  @Test
  void testToString() {
    String s = clientHandler.toString();
    assertEquals(s,clientHandler.toString());
  }
}