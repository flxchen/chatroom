package chatroom;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

/**
 * Class which stores the list of active clients and also do the validation on the capacity
 * @author jialiang Chen
 */
public class ActiveClientList {
  private List<Map.Entry<String, Socket>> connectedUser;
  private int capacity;
  private List<String> activeUserList;
  private List<Socket> activeClientSocketList;

  /**
   * Constructor of the class
   * @param capacity capacity of the maximum client possible(maximum is 10)
   */
  public ActiveClientList(int capacity) {
    this.connectedUser = new ArrayList<>(capacity);
    this.capacity = capacity;
  }

  /**
   * empty constructor of the class
   */
  public ActiveClientList() {
  }

  /**
   * find client socket pair
   * @param findMe find me
   * @return  findMe
   */
  public Map.Entry<String,Socket> findClientSocketPair(String findMe){
    retrieveActiveUserList();
    for(int i = 0; i < this.connectedUser.size();i++){
      Map.Entry<String, Socket> pair = this.connectedUser.get(i);
      String name = pair.getKey();
      if(findMe.equals(name)) return pair;
    }
    return null;
  }
  /**
   * function to add the client to the list
   * @param addMe which stores the client details along with socket
   * @return connectedUser.add(addMe)
   * @throws InterruptedException throws when the interruption occurs
   */

  public boolean addToClientList(Map.Entry<String,Socket> addMe) throws InterruptedException {
    String addUserName = addMe.getKey();
    if(isFull() || isDuplicateClient(addUserName)) {
      return false;
    }
    return this.connectedUser.add(addMe);
  }

  /**
   * function to remove client from the list
   * @param removeMe hashmap, which contain the removed clients
   * @return connectedUser.remove(removeMe)
   */
  public boolean removeFromClientList(Map.Entry<String,Socket> removeMe){
    String removeUserName = removeMe.getKey();
    if(isEmpty() || !isDuplicateClient(removeUserName)) {
      return false;
    }
    return this.connectedUser.remove(removeMe);
  }

  /**
   * check if it is a valid client
   * @param checkMe hashmap, used to check the valid client
   * @return connectedUser.contains(checkMe)
   */
  public boolean isDuplicateClient(String checkMe){
    retrieveActiveUserList();
    return this.activeUserList.contains(checkMe);
  }

  /**
   * function to get the number of users
   * @return connectedUser.size()
   */
  public int getNumberOfUsers(){
    return this.connectedUser.size();
  }

  /**
   * function to retrieve the active user list
   */
  public void retrieveActiveUserList(){
    List<String> userList = new ArrayList<>();
    this.connectedUser.stream().forEach(user->{
      String name = user.getKey();
      userList.add(name);
    });
    setActiveUserList(userList);
  }

  /**
   * function to return the active client socket list
   */
  public void retrieveActiveClientSocketList(){
    List<Socket> clientSocketList = new ArrayList<>();
    this.connectedUser.stream().forEach(user->{
      Socket socket = user.getValue();
      clientSocketList.add(socket);
    });
    setActiveClientSocketList(clientSocketList);
  }

  /**
   * function to find and returns the active client socket in the arraylist
   * @param findMe map contain the active client
   * @return forwardSocket
   */
  public Socket findForwardSocket(String findMe){
    String name;
    Socket forwardSocket;
    for(int i = 0; i < this.connectedUser.size();i++){
      Map.Entry<String, Socket> pair = this.connectedUser.get(i);
      name = pair.getKey();
      forwardSocket = pair.getValue();
      if(findMe.equals(name)) return forwardSocket;
    }
    return null;
  }

  /**
   * find and returns the name of the active client socket in the arraylist,they are used to find which client to forward messages
   * @param index index of the active client
   * @return findMe
   */
  public String findForwardNameByIndex(int index){
    String findMe = this.connectedUser.get(index).getKey();
    for(int i = 0; i < this.connectedUser.size();i++){
      String name = this.connectedUser.get(i).getKey();
      if(findMe.equals(name)) return findMe;
    }
    return null;
  }

  /**
   * boolean function to check if it is a full capacity
   * @return capacity
   */
  public boolean isFull(){
    return this.connectedUser.size() == this.capacity;
  }

  /**
   * checks if buffer is empty
   * @return true if buffer is empty otherwise false
   */
  public boolean isEmpty(){
    return this.connectedUser.size() == 0;
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
   * @param activeUserList active user list
   */
  public void setActiveUserList(List<String> activeUserList) {
    this.activeUserList = activeUserList;
  }

  /**
   * function to get the active client socket list
   * @return activeClientSocketList
   */
  public List<Socket> getActiveClientSocketList() {
    return activeClientSocketList;
  }

  /**
   * function to set the active client socket list
   * @param activeClientSocketList list of active client socket list
   */

  public void setActiveClientSocketList(List<Socket> activeClientSocketList) {
    this.activeClientSocketList = activeClientSocketList;
  }

  /**
   * function to get the active connected users.
   * @return connectedUser
   */
  public List<Entry<String, Socket>> getConnectedUser() {
    return this.connectedUser;
  }

  /**
   * function to set the connected users
   * @param connectedUser list of active connected users
   */
  public void setConnectedUser(
      List<Entry<String, Socket>> connectedUser) {
    this.connectedUser = connectedUser;
  }

  /**
   * function to get the capacity
   * @return capacity
   */
  public int getCapacity() {
    return capacity;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ActiveClientList that = (ActiveClientList) o;
    return capacity == that.capacity && Objects.equals(connectedUser, that.connectedUser)
        && Objects.equals(activeUserList, that.activeUserList) && Objects.equals(
        activeClientSocketList, that.activeClientSocketList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(capacity);
  }

  @Override
  public String toString() {
    return "ActiveClientList{" +
        "connectedUser=" + connectedUser +
        ", capacity=" + capacity +
        ", activeUserList=" + activeUserList +
        ", activeClientSocketList=" + activeClientSocketList +
        '}';
  }
}