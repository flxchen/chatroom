package protocol;

import static org.junit.jupiter.api.Assertions.*;

import chatroom.ActiveClientList;
import chatroom.ClientHandler;
import client.ClientDriver;
import client.ClientInterface;
import client.InvalidUserInputException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import problem1.sentencegenerator.GrammarNotDefinedException;

class ProtocolTest {

  Protocol protocol;

  @BeforeEach
  void setUp() {
    protocol = new Protocol();
  }

  @Test
  void connectClientIO() {
  }

  @Test
  void determineState() {
  }

  @Test
  void listenClientRequest() {
  }

  @Test
  void processClientRequest() {
  }

  @Test
  void listenInsultRequest() {
  }

  @Test
  void sendInsult() {
  }

  @Test
  void forward() {
  }

  @Test
  void readQueryUser() throws IOException {

  }

  @Test
  void sendFailedMessage() {
  }

  @Test
  void readBroadCastMessage() {
  }

  @Test
  void forwardBroadCastMessage() {
  }

  @Test
  void isValidClient() {
  }

  @Test
  void addToActiveClientList() {
  }

  @Test
  void removeFromActiveClientList() {
  }

  @Test
  void sendQueryUserResponse() {
  }

  @Test
  void listenDirectMessage() {
  }

  @Test
  void forwardDirectMessage() {
  }

  @Test
  void connectMessage() {
  }

  @Test
  void connectResponse() {
  }

  @Test
  void disconnectMessage() throws IOException, InvalidUserInputException {

  }

  @Test
  void queryUsers() {
  }

  @Test
  void queryResponse() {
  }

  @Test
  void broadcastMessage() {
  }

  @Test
  void directMessage() throws IOException {

  }

  @Test
  void failedMessage() {
  }

  @Test
  void insult() throws GrammarNotDefinedException, IOException {


  }

  @Test
  void readConnectMessage() {
  }

  @Test
  void readDisconnectMessage() {
  }

  @Test
  void sendConnectResponse() throws IOException, InterruptedException {

  }

  @Test
  void connectToServerIO() {
  }

  @Test
  void queryConnectedUsers() {
  }

  @Test
  void listenQueryResponse() {
  }

  @Test
  void sendClientRequest() {
  }

  @Test
  void listenServerResponse() {
  }

  @Test
  void listenConnectResponse() {
  }

  @Test
  void sendConnectMessage() {
  }

  @Test
  void sendDisconnectMessage() {
  }

  @Test
  void sendDirectMessage() {
  }

  @Test
  void listenDirectMessageResponse() {
  }

  @Test
  void sendBroadcastMessage() {
  }

  @Test
  void listenBroadcastMessage() {
  }

  @Test
  void listenFailedMessage() {
  }

  @Test
  void sendInsultRequest() {
  }

  @Test
  void isRead() {
    protocol.setRead(Boolean.TRUE);
    assertTrue(protocol.isRead());
  }

  @Test
  void setRead() {
  }

  @Test
  void isServer() {
    protocol.setServer(Boolean.TRUE);
    assertTrue(protocol.isServer());
  }

  @Test
  void setServer() {
  }

  @Test
  void getSocket() {
    protocol.getSocket();
  }

  @Test
  void setSocket() {
  }

  @Test
  void getActiveClientList() {
    protocol.setActiveClientList(null);
    assertNull(protocol.getActiveClientList());
  }

  @Test
  void setActiveClientList() {
  }

  @Test
  void getClientSocket() {
  }

  @Test
  void setClientSocket() {
  }

  @Test
  void getSTATE() {
  }

  @Test
  void setSTATE() {
  }

  @Test
  void getDataFrame() {
  }

  @Test
  void setDataFrame() {
  }

  @Test
  void getClientInput() {
    protocol.setClientInput(null);
    assertNull(protocol.getClientInput());
  }

  @Test
  void setClientInput() {
  }

  @Test
  void getServerInput() {
    protocol.setServerInput(null);
    assertNull(protocol.getServerInput());
  }

  @Test
  void setServerInput() {
  }

  @Test
  void getServerOutput() {
    protocol.setServerOutput(null);
    assertNull(protocol.getServerOutput());
  }

  @Test
  void setServerOutput() {
  }

  @Test
  void getClientOutput() {
    protocol.getClientOutput();
  }

  @Test
  void setClientOutput() {
  }

  @Test
  void getSeparator() {
  }

  @Test
  void setSeparator() {
    protocol.setSeparator(null);
    assertNull(protocol.getSeparator());
  }

  @Test
  void getCONNECT_MESSAGE() {
  }

  @Test
  void getCONNECT_RESPONSE() {
    protocol.getCONNECT_RESPONSE();
  }

  @Test
  void getDISCONNECT_MESSAGE() {
  }

  @Test
  void getQUERY_CONNECTED_USERS() {
    protocol.getQUERY_CONNECTED_USERS();
  }

  @Test
  void getQUERY_USER_RESPONSE() {
    protocol.getQUERY_USER_RESPONSE();
  }

  @Test
  void getBROADCAST_MESSAGE() {
  }

  @Test
  void getDIRECT_MESSAGE() {

  }

  @Test
  void getFAILED_MESSAGE() {
    protocol.getFAILED_MESSAGE();
  }

  @Test
  void getSEND_INSULT() {
  }

  @Test
  void testEqualsSameObject() {
    assertTrue(protocol.equals(protocol));
  }
  @Test
  void testEqualsNullObject() {
    assertFalse((protocol.equals(null) ) );
  }
  @Test
  void testEquals_similarObjects() {
    String test = new String("hello");
    assertFalse(protocol.equals(test));
  }

  void testEqualsDifferent() {
    ClientHandler clientHandler =  new ClientHandler();
    assertTrue(clientHandler.equals(protocol));
  }
  @Test
  void testEqualsDifferentObject(){
    Protocol protocol1 = new Protocol();
    Protocol protocol2 = new Protocol();
    assertTrue(protocol1.equals(protocol2));
  }


  @Test
  void testHashCode() {
    Protocol protocol1 = new Protocol();
    Protocol protocol2 = new Protocol();
    assertEquals(protocol1.hashCode(),protocol2.hashCode());
  }

  @Test
  void testToString() {
    Protocol protocol1 =  new Protocol();
    String s = protocol1.toString();
    assertEquals(s,protocol1.toString());
  }
}