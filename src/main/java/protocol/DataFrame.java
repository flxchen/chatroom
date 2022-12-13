package protocol;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * class which stores the data format specified in protocol
 * @author jialiang Chen
 */
public class DataFrame {
  private int messageIdentifier;
  private boolean success;
  private boolean disconnect;
  private byte[] messageInByte, senderUserNameInByte, recipientUserNameInByte;
  private String message,senderUserName, recipientUserName;
  private List<byte[]> activeUserListInByte;

  /**
   * list of active username
   */
  public List<String> activeUserList;

  /**
   * constructor of the class
   */
  public DataFrame() {
    this.activeUserListInByte = new ArrayList<>();
  }

  /**
   * function for encoding the data
   * @param input input from the user
   * @return encodeMe
   */
  public byte[] encode(String input){
    byte[] encodeMe = input.getBytes(StandardCharsets.UTF_8);
    return encodeMe.clone();
  }

  /**
   * function to decode the message
   * @param input input from the user
   * @return decodeMe
   */
  public String decode(byte[] input){
    String decodeMe = new String(input,StandardCharsets.UTF_8);
    return decodeMe;
  }

  /**
   * function to get the message size
   * @return message.length()
   */
  public int getMessageSize(){
    return this.message.length();
  }

  /**
   * function to get the message identifier
   * @return messageIdentifier
   */
  public int getMessageIdentifier() {
    return messageIdentifier;
  }

  /**
   * function to create active user in bytes
   * @param userList list of users
   */
  public void createActiveUserInByte(List<String> userList){
    userList.stream().forEach(user->{
      byte[] userInByte = encode(user);
      this.activeUserListInByte.add(userInByte);
    });
  }

  /**
   * function to get the active user list
   * @return activeUserList
   */
  public List<String> getActiveUserList() {
    return activeUserList;
  }


  /**
   * function to set the active user list
   * @param activeUserList  active user list
   */
  public void setActiveUserList(List<String> activeUserList) {
    this.activeUserList = activeUserList;
  }


  /**
   * function to get active user list in byte
   * @return activeUserListInByte
   */
  public List<byte[]> getActiveUserListInByte() {
    return activeUserListInByte;
  }


  /**
   * function set active user list in bytes
   * @param activeUserListInByte active user list in bytes
   */
  public void setActiveUserListInByte(List<byte[]> activeUserListInByte) {
    this.activeUserListInByte = activeUserListInByte;
  }


  /**
   * function to get sender user name size
   * @return senderUserName.length()
   */
  public int getSenderUsernameSize(){
    return this.senderUserName.length();
  }

  /**
   * function to get recipient user name size
   * @return recipientUserName.length()
   */
  public int getRecipientUserNameSize(){return this.recipientUserName.length();}

  /**
   * function to set message identifier
   * @param messageIdentifier message Identifier
   */
  public void setMessageIdentifier(int messageIdentifier) {
    this.messageIdentifier = messageIdentifier;
  }

  /**
   * boolean function to check if it is connected
   * @return disconnect
   */
  public boolean isDisconnect() {
    return disconnect;
  }


  /**
   * function to set the boolean status of the connection
   * @param disconnect True =  if it is disconnected
   */
  public void setDisconnect(boolean disconnect) {
    this.disconnect = disconnect;
  }


  /**
   * function to check the success status
   * @return success
   */
  public boolean isSuccess() {
    return success;
  }


  /**
   * function to set success
   * @param success success state of the connection
   */
  public void setSuccess(boolean success) {
    this.success = success;
  }


  /**
   * function to get the message in byte
   * @return messageInByte
   */
  public byte[] getMessageInByte() {
    return messageInByte;
  }


  /**
   * function to set the message in bytes
   * @param messageInByte message represented in byte
   */
  public void setMessageInByte(byte[] messageInByte) {
    this.messageInByte = messageInByte;
  }


  /**
   * function to get the sender username in byte
   * @return senderUserNameInByte
   */
  public byte[] getSenderUserNameInByte() {
    return senderUserNameInByte;
  }


  /**
   * function to set the sender username in byte
   * @param senderUserNameInByte sender username in byte
   */
  public void setSenderUserNameInByte(byte[] senderUserNameInByte) {
    this.senderUserNameInByte = senderUserNameInByte;
  }


  /**
   * function to get the recipient username in byte
   * @return recipientUserNameInByte
   */
  public byte[] getRecipientUserNameInByte() {
    return recipientUserNameInByte;
  }


  /**
   * function to set recipient username in byte
   * @param recipientUserNameInByte recipient username in byte
   */
  public void setRecipientUserNameInByte(byte[] recipientUserNameInByte) {
    this.recipientUserNameInByte = recipientUserNameInByte;
  }

  /**
   * function to get the message
   * @return message
   */
  public String getMessage() {
    return message;
  }


  /**
   * function to set the message
   * @param message message frm the user
   */
  public void setMessage(String message) {
    this.message = message;
  }


  /**
   * function to get the sender username
   * @return senderUserName
   */
  public String getSenderUserName() {
    return senderUserName;
  }


  /**
   * function to set the sender username
   * @param senderUserName sender username
   */
  public void setSenderUserName(String senderUserName) {
    this.senderUserName = senderUserName;
  }


  /**
   * function to get the recipient username
   * @return recipientUserName
   */
  public String getRecipientUserName() {
    return recipientUserName;
  }


  /**
   * function to set the recipient username
   * @param recipientUserName recipient username
   */
  public void setRecipientUserName(String recipientUserName) {
    this.recipientUserName = recipientUserName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DataFrame dataFrame = (DataFrame) o;
    return success == dataFrame.success;
  }

  @Override
  public int hashCode() {
    return Objects.hash(success);
  }

  @Override
  public String toString() {
    return "DataFrame{" +
        "messageIdentifier=" + messageIdentifier +
        ", success=" + success +
        ", disconnect=" + disconnect +
        ", messageInByte=" + Arrays.toString(messageInByte) +
        ", senderUserNameInByte=" + Arrays.toString(senderUserNameInByte) +
        ", recipientUserNameInByte=" + Arrays.toString(recipientUserNameInByte) +
        ", message='" + message + '\'' +
        ", senderUserName='" + senderUserName + '\'' +
        ", recipientUserName='" + recipientUserName + '\'' +
        ", activeUserListInByte=" + activeUserListInByte +
        ", activeUserList=" + activeUserList +
        '}';
  }
}
