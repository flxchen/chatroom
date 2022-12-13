package protocol;

import static org.junit.jupiter.api.Assertions.*;

import chatroom.ActiveClientList;
import chatroom.ClientHandler;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DataFrameTest {

  DataFrame dataFrame;

  @BeforeEach
  void setUp() {
    dataFrame = new DataFrame();
  }

  @Test
  void encode() {
  }

  @Test
  void decode() {
  }

  @Test
  void getMessageSize() {
  }

  @Test
  void getMessageIdentifier() {
  }

  @Test
  void createActiveUserInByte() {
    List<String> userList = new ArrayList<>();
    dataFrame.createActiveUserInByte(userList);
  }

  @Test
  void getActiveUserList() {
    dataFrame.setActiveUserList(null);
    assertNull(dataFrame.getActiveUserList());
  }

  @Test
  void setActiveUserList() {
  }

  @Test
  void getActiveUserListInByte() {
    dataFrame.setActiveUserListInByte(null);
    assertNull(dataFrame.getActiveUserListInByte());
  }

  @Test
  void setActiveUserListInByte() {
  }

  @Test
  void getSenderUsernameSize() {
    dataFrame.setRecipientUserName("hai");
    assertEquals(3,dataFrame.getRecipientUserNameSize());

  }

  @Test
  void getRecipientUserNameSize() {
  }

  @Test
  void setMessageIdentifier() {
  }

  @Test
  void isDisconnect() {
  }

  @Test
  void setDisconnect() {
  }

  @Test
  void isSuccess() {

  }

  @Test
  void setSuccess() {
  }

  @Test
  void getMessageInByte() {
    dataFrame.setMessageInByte(null);
    assertNull(dataFrame.getMessageInByte());
  }

  @Test
  void setMessageInByte() {
  }

  @Test
  void getSenderUserNameInByte() {
    dataFrame.setSenderUserNameInByte(null);
    assertNull(dataFrame.getSenderUserNameInByte());
  }

  @Test
  void setSenderUserNameInByte() {
  }

  @Test
  void getRecipientUserNameInByte() {
    dataFrame.setRecipientUserNameInByte(null);
    assertNull(dataFrame.getRecipientUserNameInByte());
  }

  @Test
  void setRecipientUserNameInByte() {
  }

  @Test
  void getMessage() {
    dataFrame.setMessage(null);
    assertNull(dataFrame.getMessage());
  }

  @Test
  void setMessage() {
  }

  @Test
  void getSenderUserName() {
    dataFrame.setSenderUserName(null);
    assertNull(dataFrame.getSenderUserName());
  }

  @Test
  void setSenderUserName() {
  }

  @Test
  void getRecipientUserName() {
    dataFrame.setRecipientUserName(null);
    assertNull(dataFrame.getRecipientUserName());
  }

  @Test
  void setRecipientUserName() {

  }

  @Test
  void testEqualsSameObject() {
    assertTrue(dataFrame.equals(dataFrame));
  }
  @Test
  void testEqualsNullObject() {
    assertFalse((dataFrame.equals(null) ) );
  }
  @Test
  void testEquals_similarObjects() {
    String test = new String("hello");
    assertFalse(dataFrame.equals(test));
  }

  void testEqualsDifferent() {
    ClientHandler clientHandler =  new ClientHandler();
    assertTrue(clientHandler.equals(dataFrame));
  }
  @Test
  void testEqualsDifferentObject(){
    DataFrame dataFrame1 = new DataFrame();
    DataFrame dataFrame2 = new DataFrame();
    assertTrue(dataFrame1.equals(dataFrame2));
  }


  @Test
  void testHashCode() {
    DataFrame dataFrame1 = new DataFrame();
    DataFrame dataFrame2 = new DataFrame();
    assertEquals(dataFrame1.hashCode(),dataFrame2.hashCode());
  }

  @Test
  void testToString() {
    String s = dataFrame.toString();
    assertEquals(s,dataFrame.toString());
  }
}