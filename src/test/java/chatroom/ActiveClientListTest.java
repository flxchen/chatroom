package chatroom;

import static org.junit.jupiter.api.Assertions.*;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ActiveClientListTest {

  private ActiveClientList activeClientList, activeClientList2;

  @BeforeEach
  void setUp() {
    activeClientList = new ActiveClientList();
  }






  @Test
  void findClientSocketPair() throws InterruptedException {
    String name = "foo";
    Map.Entry<String, Socket> testClient = Map.entry(name,new Socket());
    int capacity = 10;
    activeClientList2 = new ActiveClientList(capacity);
    activeClientList2.addToClientList(testClient);
    assertEquals(testClient,activeClientList2.findClientSocketPair(name));
  }

  @Test
  void getNumberOfUsers() throws InterruptedException {
    String name = "foo";
    Map.Entry<String, Socket> testClient = Map.entry(name,new Socket());
    int capacity = 10;
    activeClientList2 = new ActiveClientList(capacity);
    activeClientList2.addToClientList(testClient);
    int expect =1;
    assertEquals(expect,activeClientList2.getNumberOfUsers());
  }

  @Test
  void retrieveActiveUserList() throws InterruptedException {
    Map.Entry<String, Socket> testClient = Map.entry("foo",new Socket());
    int capacity = 10;
    activeClientList2 = new ActiveClientList(capacity);
    activeClientList2.addToClientList(testClient);
    activeClientList2.retrieveActiveUserList();
  }

  @Test
  void retrieveActiveClientSocketList() throws InterruptedException {
    Map.Entry<String, Socket> testClient = Map.entry("foo",new Socket());
    int capacity = 10;
    activeClientList2 = new ActiveClientList(capacity);
    activeClientList2.addToClientList(testClient);
    activeClientList2.retrieveActiveClientSocketList();
  }

  @Test
  void findForwardSocket() throws InterruptedException {
    Socket test = new Socket();
    String name = "foo";
    Map.Entry<String, Socket> testClient = Map.entry(name,test);
    int capacity = 10;
    activeClientList2 = new ActiveClientList(capacity);
    activeClientList2.addToClientList(testClient);
    assertEquals(test,activeClientList2.findForwardSocket(name));
  }

  @Test
  void findForwardNameByIndex() throws InterruptedException {
    String expect = "foo";
    Map.Entry<String, Socket> testClient = Map.entry(expect,new Socket());
    int capacity = 10;
    activeClientList2 = new ActiveClientList(capacity);
    activeClientList2.addToClientList(testClient);
    String test = activeClientList2.findForwardNameByIndex(0);
    assertEquals(expect,test);
  }

  @Test
  void isFull() throws InterruptedException {
    String expect = "foo";
    Map.Entry<String, Socket> testClient = Map.entry(expect,new Socket());
    int capacity = 1;
    activeClientList2 = new ActiveClientList(capacity);
    activeClientList2.addToClientList(testClient);
    assertTrue(activeClientList2.isFull());
  }



  @Test
  void getActiveUserList() {
    assertEquals(null,activeClientList.getActiveUserList());
  }

  @Test
  void setActiveUserList() {

  }

  @Test
  void getActiveClientSocketList() {
    assertEquals(null,activeClientList.getActiveClientSocketList());
  }

  @Test
  void setActiveClientSocketList() {
  }

  @Test
  void getConnectedUser() {
  }

  @Test
  void setConnectedUser() {
    String expect = "foo";
    Map.Entry<String, Socket> testClient = Map.entry(expect,new Socket());
    List<Entry<String, Socket>> testList = new ArrayList<>();
    testList.add(testClient);
    activeClientList.setConnectedUser(testList);
    assertEquals(testList,activeClientList.getConnectedUser());
  }

  @Test
  void getCapacity() {
    int expect = 0;
    assertEquals(expect,activeClientList.getCapacity());
  }

  @Test
  void testEqualsSameObject() {
    assertTrue(activeClientList.equals(activeClientList));
  }
  @Test
  void testEqualsNullObject() {
    assertFalse((activeClientList.equals(null) ) );
  }
  @Test
  void testEquals_similarObjects() {
    String test = new String("hello");
    assertFalse(activeClientList.equals(test));
  }
  @Test
  void testEqualsDifferent() {
    ClientHandler clientHandler =  new ClientHandler();
    assertFalse(clientHandler.equals(activeClientList));
  }
  @Test
  void testEqualsDifferentObject(){
    ActiveClientList activeClientList1 = new ActiveClientList();
    ActiveClientList activeClientList2 = new ActiveClientList();
    assertTrue(activeClientList1.equals(activeClientList2));
  }
  @Test
  void testEqualsDifferentCapacity(){
    int capacity = 3;
    activeClientList2 = new ActiveClientList(capacity);
    assertFalse(activeClientList.equals(activeClientList2));
  }
  @Test
  void testEqualsDifferentConnectedUser() throws InterruptedException {
    String expect = "foo";
    Map.Entry<String, Socket> testClient = Map.entry(expect,new Socket());
    int capacity = 1;
    activeClientList2 = new ActiveClientList(capacity);
    activeClientList2.addToClientList(testClient);
    assertFalse(activeClientList.equals(activeClientList2));
  }
  @Test
  void testHashCode() {
    ActiveClientList activeClientList1 = new ActiveClientList();
    ActiveClientList activeClientList2 = new ActiveClientList();
    assertEquals(activeClientList1.hashCode(),activeClientList2.hashCode());
  }

  @Test
  void testToString() {
    ActiveClientList activeClientList1 = new ActiveClientList();
    String s = activeClientList1.toString();
    assertEquals(s,activeClientList1.toString());
  }
}