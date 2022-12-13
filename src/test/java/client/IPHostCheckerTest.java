package client;

import static org.junit.jupiter.api.Assertions.*;

import chatroom.ActiveClientList;
import chatroom.ClientHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IPHostCheckerTest {

  IPHostChecker ipHostChecker;

  @BeforeEach
  void setUp() {
    ipHostChecker = new IPHostChecker();
  }

  @Test
  void verify() {
  }

  @Test
  void getPort() {
  }

  @Test
  void setPort() {
  }

  @Test
  void getHostName() {
  }

  @Test
  void setHostName() {
  }

  @Test
  void testEqualsSameObject() {
    assertTrue(ipHostChecker.equals(ipHostChecker));
  }
  @Test
  void testEqualsNullObject() {
    assertFalse((ipHostChecker.equals(null) ) );
  }
  @Test
  void testEquals_similarObjects() {
    String test = new String("hello");
    assertFalse(ipHostChecker.equals(test));
  }

  void testEqualsDifferent() {
    ClientHandler clientHandler =  new ClientHandler();
    assertTrue(clientHandler.equals(ipHostChecker));
  }
  @Test
  void testEqualsDifferentObject(){
    IPHostChecker ipHostChecker1 = new IPHostChecker();
    IPHostChecker ipHostChecker2 = new IPHostChecker();
    assertTrue(ipHostChecker1.equals(ipHostChecker2));
  }


  @Test
  void testHashCode() {
    IPHostChecker ipHostChecker1 = new IPHostChecker();
    IPHostChecker ipHostChecker2 = new IPHostChecker();
    assertEquals(ipHostChecker1.hashCode(),ipHostChecker2.hashCode());
  }

  @Test
  void testToString() {
    IPHostChecker ipHostChecker1 = new IPHostChecker();
    String s = ipHostChecker1.toString();
    assertEquals(s,ipHostChecker1.toString());
  }
}