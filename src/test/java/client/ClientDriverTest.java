package client;

import static org.junit.jupiter.api.Assertions.*;

import chatroom.ActiveClientList;
import chatroom.ClientHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import problem1.sentencegenerator.GrammarNotDefinedException;

class ClientDriverTest {

  ClientDriver clientDriver;
  ClientDriver  clientDriver1;

  @BeforeEach
  void setUp() throws InvalidUserInputException {

    clientDriver1 = new ClientDriver(new Socket());
    clientDriver = new ClientDriver();
  }

  @Test
  void parseUserCommand() throws InvalidUserInputException {
    clientDriver1.parseUserCommand("?");
  }

  @Test
  void getUserName() {
  }

  @Test
  void listenServerResponse() throws IOException, InterruptedException {
    clientDriver1.listenServerResponse();
  }

  @Test
  void listenServerResponse1() throws IOException, InterruptedException {
    clientDriver1.listenServerResponse();
  }

  @Test
  void listenUserInput() throws GrammarNotDefinedException, IOException {
    clientDriver1.listenUserInput();
  }

  @Test
  void getHost() {
  }

  @Test
  void getPort() {
  }

  @Test
  void closeIO() {
  }

  @Test
  void isValidateUserName() {
    clientDriver.setValidateUserName(Boolean.TRUE);
    assertTrue(clientDriver.isValidateUserName());
  }

  @Test
  void setValidateUserName() {
  }

  @Test
  void getUserInput() {
    BufferedReader test1 = new BufferedReader(new InputStreamReader(System.in));
    clientDriver.setUserInput(test1);
    assertEquals(test1, clientDriver.getUserInput());
  }

  @Test
  void setUserInput() {
  }

  @Test
  void testEqualsSameObject() {
    assertTrue(clientDriver.equals(clientDriver));
  }
  @Test
  void testEqualsNullObject() {
    assertFalse((clientDriver.equals(null) ) );
  }
  @Test
  void testEquals_similarObjects() {
    String test = new String("hello");
    assertFalse(clientDriver.equals(test));
  }

  void testEqualsDifferent() {
    ClientHandler clientHandler =  new ClientHandler();
    assertTrue(clientHandler.equals(clientDriver));
  }
  @Test
  void testEqualsDifferentObject(){
    ClientDriver clientDriver1 = new ClientDriver();
    ClientDriver clientDriver2 = new ClientDriver();
    assertTrue(clientDriver1.equals(clientDriver2));
  }


  @Test
  void testHashCode() {
    ClientDriver clientDriver1 = new ClientDriver();
    ClientDriver clientDriver2 = new ClientDriver();
    assertEquals(clientDriver1.hashCode(),clientDriver2.hashCode());
  }

  @Test
  void testToString() {
    ClientDriver clientDriver1 = new ClientDriver();
    String s = clientDriver1.toString();
    assertEquals(s,clientDriver1.toString());
  }
}