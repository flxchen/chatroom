package client;

import static client.ClientInterface.COMMAND_LIST;
import static client.ClientInterface.COMMAND_MESSAGE;
import static org.junit.jupiter.api.Assertions.*;

import chatroom.ActiveClientList;
import chatroom.ClientHandler;
import java.net.Socket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import protocol.DataFrame;
import protocol.Protocol;
import protocol.State;

class ClientInterfaceTest {

  ClientInterface clientInterface;

  Protocol protocol = new Protocol();

  ClientInterface clientInterface4 = new ClientInterface("hai",protocol);

  @BeforeEach
  void setUp() {
    clientInterface = new ClientInterface();
  }

  @Test
  void parseDirectMessage() {
    assertFalse(clientInterface.parseDirectMessage("test1"));
  }

  @Test
  void parseDirectMessage1() {
    String s = "@aa hai bye good";
    clientInterface.parseDirectMessage(s);
    assertFalse(clientInterface.parseDirectMessage("user"));
  }
  @Test
  void parseDirectMessage2() {
    assertFalse(clientInterface.parseDirectMessage("user hi"));
  }

  @Test
  void parseBroadCast() {
    String s = "@all hello";
    clientInterface.parseBroadCast(s);
  }

  @Test
  void parseBroadCast2() {
    assertFalse(clientInterface.parseInsult("user"));
  }

  @Test
  void parseBroadCast3() {
    assertFalse(clientInterface.parseInsult("@user hi "));
  }

  @Test
  void parseInsult() {
    clientInterface.parseInsult("!user");
    assertFalse(clientInterface.parseInsult("user"));
  }

  @Test
  void parseUserName() {
    clientInterface.setProtocol(new Protocol(new DataFrame()));
    clientInterface.parseUserName("?");
  }

  @Test
  void parseUserName2() {
    clientInterface.setProtocol(new Protocol(new DataFrame()));
    assertFalse(clientInterface.parseUserName("logoff"));
  }

  @Test
  void parseUserName3() {
    clientInterface.setProtocol(new Protocol(new DataFrame()));
    assertFalse(clientInterface.parseUserName("who"));
  }
  @Test
  void parseCommand() throws InvalidUserInputException {
    clientInterface.setProtocol(new Protocol(new DataFrame()));
    clientInterface.parseCommand("?");
  }

  @Test
  void parseCommand1() throws InvalidUserInputException {
    assertThrows(InvalidUserInputException.class,()->{
      clientInterface.setProtocol(new Protocol(new DataFrame()));
      clientInterface.parseCommand(null);
    });
  }

  @Test
  void parseCommand3() throws InvalidUserInputException {
    clientInterface.setProtocol(new Protocol(new DataFrame()));
    clientInterface.parseCommand("logoff");
  }

  @Test
  void parseCommand2() throws InvalidUserInputException {
    clientInterface.setProtocol(new Protocol(new DataFrame()));
    clientInterface.parseCommand("who");
  }

  @Test
  void parseCommand4() throws InvalidUserInputException {
    clientInterface.setProtocol(new Protocol(new DataFrame()));
    clientInterface.parseCommand("@user hello");
  }

  @Test
  void parseCommand5() throws InvalidUserInputException {
    clientInterface.setProtocol(new Protocol(new DataFrame()));
    clientInterface.parseCommand("@all hello");
  }

  @Test
  void parseCommand6() throws InvalidUserInputException {
    clientInterface.setProtocol(new Protocol(new DataFrame()));
    clientInterface.parseCommand("!user");
  }

  @Test
  void showMenu() {
  }

  @Test
  void getRecipientName() {
    clientInterface.setRecipientName("test1");
    assertEquals("test1",clientInterface.getRecipientName());
  }

  @Test
  void setRecipientName() {
  }

  @Test
  void getCommandList() {

    assertEquals(COMMAND_LIST,clientInterface.getCommandList());
  }

  @Test
  void isShowMenu() {
    clientInterface.showMenu();
  }

  @Test
  void setShowMenu() {
  }

  @Test
  void getCommandMessage() {
    assertEquals(COMMAND_MESSAGE, clientInterface.getCommandMessage());
  }

  @Test
  void getMessage() {
    clientInterface.setMessage("test1");
    assertEquals("test1",clientInterface.getMessage());

  }

  @Test
  void setMessage() {
  }

  @Test
  void getCommand() {
    clientInterface.setCommand("test1");
    assertEquals("test1",clientInterface.getCommand());
  }

  @Test
  void setCommand() {
  }

  @Test
  void getProtocol() {
    clientInterface.setProtocol(protocol);
    assertEquals(protocol,clientInterface.getProtocol());
  }

  @Test
  void setProtocol() {

  }

  @Test
  void testEqualsSameObject() {
    assertTrue(clientInterface.equals(clientInterface));
  }
  @Test
  void testEqualsNullObject() {
    assertFalse((clientInterface.equals(null) ) );
  }
  @Test
  void testEquals_similarObjects() {
    String test = new String("hello");
    assertFalse(clientInterface.equals(test));
  }

  void testEqualsDifferent() {
    ClientHandler clientHandler =  new ClientHandler();
    assertTrue(clientHandler.equals(clientInterface));
  }
  @Test
  void testEqualsDifferentObject(){
    ClientInterface clientInterface1 = new ClientInterface();
    ClientInterface clientInterface2 = new ClientInterface();
    assertTrue(clientInterface1.equals(clientInterface2));
  }


  @Test
  void testHashCode() {
    ClientInterface clientInterface1 = new ClientInterface();
    ClientInterface clientInterface2 = new ClientInterface();
    assertEquals(clientInterface1.hashCode(),clientInterface2.hashCode());
  }

  @Test
  void testToString() {
    ClientInterface clientInterface1 = new ClientInterface();
    String s = clientInterface1.toString();
    assertEquals(s,clientInterface1.toString());
  }
}