package protocol;

import chatroom.ActiveClientList;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import problem1.sentencegenerator.GrammarNotDefinedException;
import problem1.sentencegenerator.SentenceGenerator;

/**
 * brian of the chat sysytem, which validate and send messages accordingly to the user request
 */
public class Protocol {

  private final int CONNECT_MESSAGE = 19, CONNECT_RESPONSE = 20, DISCONNECT_MESSAGE = 21, QUERY_CONNECTED_USERS = 22,
      QUERY_USER_RESPONSE = 23, BROADCAST_MESSAGE = 24, DIRECT_MESSAGE = 25, FAILED_MESSAGE = 26, SEND_INSULT = 27;
  private String separator = " ";
  private DataFrame dataFrame;
  private DataInputStream clientInput, serverInput;
  private DataOutputStream serverOutput, clientOutput;
  private ActiveClientList activeClientList;
  private Socket clientSocket;
  private Socket socket;
  private State STATE;
  private boolean isRead, isServer;


  /**
   * constructor of the class
   * @param dataFrame object which helps to send the data in particular format
   */
  public Protocol(DataFrame dataFrame) {
    this.dataFrame = dataFrame;
    this.STATE = State.WAITING;
  }

  /**
   * overloaded constructor of the class
   * @param activeClientList object of the active client list
   * @param dataFrame object which helps to send the data in particular format
   */
  public Protocol(ActiveClientList activeClientList,DataFrame dataFrame) {
    this.activeClientList = activeClientList;
    this.dataFrame = dataFrame;
    this.STATE = State.WAITING;
  }


  /**
   * empty constructor of the class
   */
  public Protocol() {
  }

  //server side

  /**
   * function to connect to server side
   * @param clientSocket socket detail of the client
   * @throws IOException throws when and IO exception occurs
   */
  public void connectClientIO(Socket clientSocket) throws IOException {
    setClientSocket(clientSocket);
    setServerInput(new DataInputStream(this.clientSocket.getInputStream()));
    setServerOutput(new DataOutputStream(this.clientSocket.getOutputStream()));
  }


  /**
   * function to determine client request
   * @throws IOException throws when and IO exception occurs
   */
  public void determineState() throws IOException {
    int identifier = this.serverInput.readInt();
    System.out.println("listen client request:"+identifier);
    this.dataFrame.setMessageIdentifier(identifier);
    this.serverInput.readUTF();
    if(this.dataFrame.getMessageIdentifier() == CONNECT_MESSAGE){
      setSTATE(State.CONNECT_MESSAGE);
    }
    else if (this.dataFrame.getMessageIdentifier() == DISCONNECT_MESSAGE) {
      setSTATE(State.DISCONNECT_MESSAGE);
    } else if (this.dataFrame.getMessageIdentifier() == QUERY_CONNECTED_USERS) {
      setSTATE(State.WHO);
    } else if (this.dataFrame.getMessageIdentifier() == DIRECT_MESSAGE) {
      setSTATE(State.DIRECT_MESSAGE);
    } else if (this.dataFrame.getMessageIdentifier() == BROADCAST_MESSAGE) {
      setSTATE(State.BROADCAST);
    } else if (this.dataFrame.getMessageIdentifier() == SEND_INSULT) {
      setSTATE(State.SEND_INSULT);
    } else {
      System.out.println("not a valid request...");
    }
  }

  /**
   * function to listen to client request and process it
   * @throws IOException throws when and IO exception occurs
   * @throws GrammarNotDefinedException throws if grammar isn't defined(assignment4)
   */
  public void listenClientRequest() throws IOException, GrammarNotDefinedException {
    determineState();
    switch (this.STATE) {
      case CONNECT_MESSAGE:
        readConnectMessage();
        break;
      case DISCONNECT_MESSAGE:
        readDisconnectMessage();
        break;
      case WHO:
        readQueryUser();
        break;
      case BROADCAST:
        readBroadCastMessage();
        break;
      case DIRECT_MESSAGE:
        listenDirectMessage();
        break;
      case SEND_INSULT:
        listenInsultRequest();
        break;
      default:
        System.out.println("unsupported request...");
        break;
    }
  }


  /**
   * function to process the client request
   * @throws IOException throws when and IO exception occurs
   * @throws GrammarNotDefinedException throws if grammar isn't defined(assignment4)
   * @throws InterruptedException throws when an interruption has occured
   */
  public void processClientRequest()
      throws IOException, GrammarNotDefinedException, InterruptedException {
    System.out.println("process client request:"+this.STATE);
    switch (this.STATE) {
      case DISCONNECT_MESSAGE:
        sendConnectResponse();
        setSTATE(State.DISCONNECT_MESSAGE);
        break;
      case CONNECT_MESSAGE:
        sendConnectResponse();
        setSTATE(State.WAITING);
        break;
      case WHO:
        if (isValidClient(true)) {
          sendQueryUserResponse();
        }
        setSTATE(State.WAITING);
        break;
      case BROADCAST:
        if (!isValidClient(true)) {
          sendFailedMessage();
        } else {
          forwardBroadCastMessage();
        }
        setSTATE(State.WAITING);
        break;
      case DIRECT_MESSAGE:
        if (isValidClient(true) || isValidClient(false)) {
          forwardDirectMessage();
        } else {
          this.dataFrame.setMessage("Invalid client, forward denied...");
          sendFailedMessage();
        }
        setSTATE(State.WAITING);
        break;
      case SEND_INSULT:
        sendInsult();
        this.STATE = State.WAITING;
        break;
      default:
        System.out.println("fail to fulfill...");
        setSTATE(State.WAITING);
        break;
    }
  }

  /**
   * function to listen to insult request
   * @throws IOException throws when and IO exception occurs
   * @throws GrammarNotDefinedException throws if grammar isn't defined(assignment4)
   */
  public void listenInsultRequest() throws IOException, GrammarNotDefinedException {
    setServer(true);
   setRead(true);
   insult();
  }

  /**
   * function to send an insult grammar, if requested
   * @throws IOException throws when and IO exception occurs
   * @throws GrammarNotDefinedException throws if grammar isn't defined(assignment4)
   */
  public void sendInsult() throws GrammarNotDefinedException, IOException {
    setServer(true);
    setRead(false);
    insult();
  }

  /**
   * function to forward the connection
   * @param index index of the client
   * @return forwardSocket
   * @throws IOException throws when and IO exception occurs
   */
  public Socket forward(int index) throws IOException {
    String forwardName = this.activeClientList.findForwardNameByIndex(index);
    Socket forwardSocket = this.activeClientList.findForwardSocket(forwardName);
    return forwardSocket;
  }

  /**
   * function to read the user query
   * @throws IOException throws when and IO exception occurs
   */
  public void readQueryUser() throws IOException {
    setServer(true);
    queryUsers();
  }

  /**
   * function to send failed message prompt
   * @throws IOException throws when and IO exception occurs
   */
  public void sendFailedMessage() throws IOException {
    setServer(true);
    failedMessage();
  }


  /**
   * function read a broadcast message from the user
   * @throws IOException throws when and IO exception occurs
   */
  public void readBroadCastMessage() throws IOException {
    setServer(true);
    setRead(true);
    broadcastMessage();
  }


  /**
   * function to forward a broadcast message
   * @throws IOException throws when and IO exception occurs
   */
  public void forwardBroadCastMessage() throws IOException {
    setServer(true);
    setRead(false);
    broadcastMessage();
  }


  /**
   * function to check if it is a valid command
   * @param isSender check if it is a valid sender
   * @return boolean status if it is valid client
   */
  public boolean isValidClient(boolean isSender) {
    boolean valid = false;
    if (isSender) {//check if sender exists
      valid = this.activeClientList.isDuplicateClient(this.dataFrame.getSenderUserName());
    } else {//check if receiver exists
      valid = this.activeClientList.isDuplicateClient(this.dataFrame.getRecipientUserName());
    }
    return valid;
  }

  /**
   * function to check the status if it is active client
   * @return addMe
   * @throws InterruptedException throws when an interruption occurs
   */
  public boolean addToActiveClientList() throws InterruptedException {
    Map.Entry<String, Socket> addMe = Map.entry(this.dataFrame.getSenderUserName(),
        getClientSocket());
    return this.activeClientList.addToClientList(addMe);
  }


  /**
   * function to check and remove from the active client list
   * @return activeClientList.removeFromClientList(removeMe)
   */
  public boolean removeFromActiveClientList() {
    Map.Entry<String, Socket> removeMe = Map.entry(this.dataFrame.getSenderUserName(),
        getClientSocket());
    return this.activeClientList.removeFromClientList(removeMe);
  }


  /**
   * function to send query user response
   * @throws IOException throws when and IO exception occurs
   */
  public void sendQueryUserResponse() throws IOException {
    setServer(true);
    queryResponse();
  }

  /**
   * function to listen to direct message
   * @throws IOException throws when and IO exception occurs
   */
  public void listenDirectMessage() throws IOException {
    setServer(true);
    setRead(true);
    directMessage();
  }


  /**
   * function to forward direct message
   * @throws IOException throws when and IO exception occurs
   */
  public void forwardDirectMessage() throws IOException {
    setServer(true);
    setRead(false);
    directMessage();
  }

  /**
   * function to connect message
   * @throws IOException throws when and IO exception occurs
   */
  public void connectMessage() throws IOException {
    if(this.isServer){
      int size = serverInput.readInt();
      this.serverInput.readUTF();
      byte[] name = new byte[size];
      this.serverInput.read(name);
      this.dataFrame.setSenderUserNameInByte(name);
      this.dataFrame.setSenderUserName(
          this.dataFrame.decode(this.dataFrame.getSenderUserNameInByte()));
    }else {
      byte[] nameInByte = this.dataFrame.encode(this.dataFrame.getSenderUserName());
      this.clientOutput.writeInt(this.CONNECT_MESSAGE);
      this.clientOutput.writeUTF(this.separator);
      this.clientOutput.writeInt(this.dataFrame.getSenderUsernameSize());
      this.clientOutput.writeUTF(this.separator);
      this.clientOutput.write(nameInByte);
    }
  }

  /**
   * function to connect to the response of the client
   * @throws IOException throws when and IO exception occurs
   * @throws InterruptedException throws when an interruption occurs
   */
  public void connectResponse() throws IOException, InterruptedException {
    if(this.isServer){
      if(this.dataFrame.isDisconnect()){//disconnect client
        if(isValidClient(true)) {
          this.dataFrame.setSuccess(true);
          removeFromActiveClientList();
          this.dataFrame.setMessage("You are no longer connected.");
        }else {
          this.dataFrame.setSuccess(false);
          this.dataFrame.setMessage("fail to disConnect, not a valid client...");
        }
      }
      else {//connect client
        this.dataFrame.setSuccess(addToActiveClientList());
        if (this.dataFrame.isSuccess()) {
          this.dataFrame.setDisconnect(false);
          this.dataFrame.setMessage(
              "Connection successful!\nThere are " + (
                  this.activeClientList.getConnectedUser().size() - 1)
                  + " other connected clients.");
          System.out.println("connect " + this.dataFrame.getSenderUserName());
        } else {
          this.dataFrame.setDisconnect(true);
          if(this.activeClientList.isFull()){
            this.dataFrame.setMessage("fail to connect, chatroom is full...");
          }
          else this.dataFrame.setMessage("fail to connect, duplicate name...");
        }
      }
      this.serverOutput.writeInt(CONNECT_RESPONSE);
      this.serverOutput.writeUTF(this.separator);
      this.serverOutput.writeBoolean(this.dataFrame.isSuccess());
      this.serverOutput.writeUTF(this.separator);
      this.serverOutput.writeInt(this.dataFrame.getMessageSize());
      this.serverOutput.writeUTF(this.separator);
      this.dataFrame.setMessageInByte(this.dataFrame.encode(this.dataFrame.getMessage()));
      this.serverOutput.write(this.dataFrame.getMessageInByte());
    }else {//client read connect response
      this.dataFrame.setSuccess(this.clientInput.readBoolean());
      this.clientInput.readUTF();
      int size = this.clientInput.readInt();
      this.clientInput.readUTF();
      byte[] message = new byte[size];
      this.clientInput.read(message);
      this.dataFrame.setMessageInByte(message);
      this.dataFrame.setMessage(this.dataFrame.decode(this.dataFrame.getMessageInByte()));
    }
  }

  /**
   * function to disconnect a message
   * @throws IOException throws when and IO exception occurs
   */
  public void disconnectMessage() throws IOException {
    if(this.isServer){
      this.dataFrame.setDisconnect(true);
      int size = this.serverInput.readInt();
      byte[] senderName = new byte[size];
      this.serverInput.readUTF();
      this.serverInput.read(senderName);
      this.dataFrame.setSenderUserNameInByte(senderName);
      this.dataFrame.setSenderUserName(
          this.dataFrame.decode(senderName));
    }else {
      int size = this.dataFrame.getSenderUsernameSize();
      this.clientOutput.writeInt(DISCONNECT_MESSAGE);
      this.dataFrame.setDisconnect(true);
      this.clientOutput.writeUTF(this.separator);
      this.clientOutput.writeInt(size);
      this.clientOutput.writeUTF(this.separator);
      this.clientOutput.write(this.dataFrame.encode(this.dataFrame.getSenderUserName()));
    }
  }

  /**
   * function to query the user request
   * @throws IOException throws when and IO exception occurs
   */
  public void queryUsers() throws IOException {
    if(this.isServer){
      int size = this.serverInput.readInt();
      this.serverInput.readUTF();
      byte[] name = new byte[size];
      this.serverInput.read(name);
      this.dataFrame.setSenderUserNameInByte(name);
      this.dataFrame.setSenderUserName(
          this.dataFrame.decode(this.dataFrame.getSenderUserNameInByte()));
    }else {
      this.clientOutput.writeInt(QUERY_CONNECTED_USERS);
      this.clientOutput.writeUTF(this.separator);
      this.clientOutput.writeInt(this.dataFrame.getSenderUsernameSize());
      this.clientOutput.writeUTF(this.separator);
      this.clientOutput.write(this.dataFrame.encode(this.dataFrame.getSenderUserName()));
    }
  }

  /**
   * function to query user response
   * @throws IOException throws when and IO exception occurs
   */
  public void queryResponse() throws IOException {
    if(this.isServer){
      this.activeClientList.retrieveActiveClientSocketList();
      DataOutputStream forwardOutput = null;
      this.activeClientList.retrieveActiveUserList();
      this.dataFrame.createActiveUserInByte(this.activeClientList.getActiveUserList());
      List<Socket> activeClientSocketList = this.activeClientList.getActiveClientSocketList();
      List<byte[]> activeUserInByte = this.dataFrame.getActiveUserListInByte();
      List<String> activeUserList = this.activeClientList.getActiveUserList();
      int numberOfUsers = this.activeClientList.getNumberOfUsers();
      for(int j = 0; j < numberOfUsers;j++) {
        forwardOutput = new DataOutputStream(forward(j).getOutputStream());
        String forwardUser = activeUserList.get(j);
        forwardOutput.writeInt(this.QUERY_USER_RESPONSE);
        forwardOutput.writeUTF(this.separator);
        forwardOutput.writeInt(numberOfUsers-1);
        for (int i = 0; i < numberOfUsers; i++) {
          if(numberOfUsers > 1 && forwardUser.equals(activeUserList.get(i)))
          {
            System.out.println("skip "+ forwardUser);
            continue;
          }
          forwardOutput.writeUTF(this.separator);
          int size = activeUserInByte.get(i).length;
          forwardOutput.writeInt(size);
          forwardOutput.writeUTF(this.separator);
          forwardOutput.write(activeUserInByte.get(i));
        }
      }
    }else{
      int numberOfUsers = this.clientInput.readInt();
      System.out.println("There are "+ numberOfUsers+" other clients connected now.");
      if(numberOfUsers == 0){
        this.clientInput.readUTF();
        int size = this.clientInput.readInt();
        this.clientInput.readUTF();
        byte[] nameInByte = new byte[size];
        this.clientInput.read(nameInByte);
        String name = this.dataFrame.decode(nameInByte);
        System.out.println("1:"+name);
      }else {
        for (int i = 0; i < numberOfUsers; i++) {
          this.clientInput.readUTF();
          int size = this.clientInput.readInt();
          this.clientInput.readUTF();
          byte[] nameInByte = new byte[size];
          this.clientInput.read(nameInByte);
          String name = this.dataFrame.decode(nameInByte);
          System.out.println(i + 1 + ":" + name);
        }
      }
    }
  }

  /**
   * function to broadcast message
   * @throws IOException throws when and IO exception occurs
   */
  public void broadcastMessage() throws IOException {
    if(this.isServer){
      if(this.isRead){
        int size = this.serverInput.readInt();
        this.serverInput.readUTF();
        byte[] name = new byte[size];
        this.serverInput.read(name);
        this.dataFrame.setSenderUserNameInByte(name);
        this.dataFrame.setSenderUserName(
            this.dataFrame.decode(this.dataFrame.getSenderUserNameInByte()));
        this.serverInput.readUTF();
        size = this.serverInput.readInt();
        byte[] message = new byte[size];
        this.serverInput.readUTF();
        this.serverInput.read(message);
        this.dataFrame.setMessageInByte(message);
        this.dataFrame.setMessage(this.dataFrame.decode(message));
      }else{
        String forwardName = null;
        DataOutputStream forwardOutput = null;
        int numberOfActiveUsers = this.activeClientList.getNumberOfUsers();
        for(int i = 0;i < numberOfActiveUsers;i++){
          forwardName = this.activeClientList.findForwardNameByIndex(i);
          forwardOutput = new DataOutputStream(forward(i).getOutputStream());
          forwardOutput.writeInt(BROADCAST_MESSAGE);
          forwardOutput.writeUTF(this.separator);
          forwardOutput.writeInt(this.dataFrame.getSenderUsernameSize());
          forwardOutput.writeUTF(this.separator);
          forwardOutput.write(this.dataFrame.getSenderUserNameInByte());
          forwardOutput.writeUTF(this.separator);
          forwardOutput.writeInt(this.dataFrame.getMessageSize());
          forwardOutput.writeUTF(this.separator);
          forwardOutput.write(this.dataFrame.getMessageInByte());
        }
      }
    }else{
      if(this.isRead){
        int size = this.clientInput.readInt();
        this.clientInput.readUTF();
        byte[] senderName = new byte[size];
        this.clientInput.read(senderName);
        this.clientInput.readUTF();
        size = this.clientInput.readInt();
        this.clientInput.readUTF();
        byte[] message = new byte[size];
        this.clientInput.read(message);
        System.out.println("from "+this.dataFrame.decode(senderName)+" to everyone:"+this.dataFrame.decode(message));
      }else {
        this.clientOutput.writeInt(BROADCAST_MESSAGE);
        this.clientOutput.writeUTF(this.separator);
        this.clientOutput.writeInt(this.dataFrame.getSenderUsernameSize());
        this.clientOutput.writeUTF(this.separator);
        this.clientOutput.write(this.dataFrame.getSenderUserNameInByte());
        this.clientOutput.writeUTF(this.separator);
        this.clientOutput.writeInt(this.dataFrame.getMessageSize());
        this.clientOutput.writeUTF(this.separator);
        this.clientOutput.write(this.dataFrame.getMessageInByte());
      }
    }
  }

  /**
   * function to send the direct message to any particular client
   * @throws IOException throws when and IO exception occurs
   */
  public void directMessage() throws IOException {
    if (this.isServer) {
      if (this.isRead) {
        int size = this.serverInput.readInt(); System.out.println("direct message sender size:"+size);
        this.serverInput.readUTF();
        byte[] senderName = new byte[size];
        this.serverInput.read(senderName);
        this.dataFrame.setSenderUserNameInByte(senderName);
        this.dataFrame.setSenderUserName(this.dataFrame.decode(senderName));
        this.serverInput.readUTF();
        size = this.serverInput.readInt();
        byte[] recipientName = new byte[size];
        this.serverInput.readUTF();
        this.serverInput.read(recipientName);
        this.serverInput.readUTF();
        size = this.serverInput.readInt();
        this.serverInput.readUTF();
        byte[] message = new byte[size];
        this.serverInput.read(message);
        this.dataFrame.setRecipientUserNameInByte(recipientName);
        this.dataFrame.setRecipientUserName(this.dataFrame.decode(recipientName));
        this.dataFrame.setMessageInByte(message);
        this.dataFrame.setMessage(this.dataFrame.decode(message));
      } else {
        DataOutputStream forwardOutput = new DataOutputStream(this.activeClientList.findForwardSocket(this.dataFrame.getRecipientUserName()).getOutputStream());
        forwardOutput.writeInt(DIRECT_MESSAGE);
        forwardOutput.writeUTF(this.separator);
        forwardOutput.writeInt(this.dataFrame.getSenderUsernameSize());
        forwardOutput.writeUTF(this.separator);
        forwardOutput.write(this.dataFrame.getSenderUserNameInByte());
        forwardOutput.writeUTF(this.separator);
        forwardOutput.writeInt(this.dataFrame.getRecipientUserNameSize());
        forwardOutput.writeUTF(this.separator);
        forwardOutput.write(this.dataFrame.getRecipientUserNameInByte());
        forwardOutput.writeUTF(this.separator);
        forwardOutput.writeInt(this.dataFrame.getMessageSize());
        forwardOutput.writeUTF(this.separator);
        forwardOutput.write(this.dataFrame.getMessageInByte());
      }
    } else {
      if (this.isRead) {
        int size = this.clientInput.readInt();
        this.clientInput.readUTF();
        byte[] senderName = new byte[size];
        this.clientInput.read(senderName);
        this.clientInput.readUTF();
        size = this.clientInput.readInt();
        this.clientInput.readUTF();
        byte[] recipientName = new byte[size];
        this.clientInput.read(recipientName);
        this.clientInput.readUTF();
        size = this.clientInput.readInt();
        byte[] message = new byte[size];
        this.clientInput.readUTF();
        this.clientInput.read(message);
        System.out.println(this.dataFrame.decode(senderName)+"->"+this.dataFrame.decode(recipientName)+":"+this.dataFrame.decode(message));
      } else {
        this.clientOutput.writeInt(DIRECT_MESSAGE);
        this.clientOutput.writeUTF(this.separator);
        this.clientOutput.writeInt(this.dataFrame.getSenderUsernameSize());
        this.clientOutput.writeUTF(this.separator);
        this.clientOutput.write(this.dataFrame.getSenderUserNameInByte());
        this.clientOutput.writeUTF(this.separator);
        int size = this.dataFrame.getRecipientUserNameSize();
        this.clientOutput.writeInt(size);
        this.clientOutput.writeUTF(this.separator);
        this.clientOutput.write(this.dataFrame.getRecipientUserNameInByte());
        this.clientOutput.writeUTF(this.separator);
        this.clientOutput.writeInt(this.dataFrame.getMessageSize());
        this.clientOutput.writeUTF(this.separator);
        this.clientOutput.write(this.dataFrame.getMessageInByte());
      }
    }
  }

  /**
   * function to show the failed message
   * @throws IOException throws when and IO exception occurs
   */
  public void failedMessage() throws IOException {
    if(this.isServer){
      this.serverOutput.writeInt(FAILED_MESSAGE);
      this.serverOutput.writeUTF(this.separator);
      this.serverOutput.writeInt(this.dataFrame.getMessageSize());
      this.serverOutput.writeUTF(this.separator);
      this.serverOutput.write(this.dataFrame.getMessageInByte());
    }else {
      int size = this.clientInput.readInt();
      this.clientInput.readUTF();
      byte[] message = new byte[size];
      this.clientInput.read(message);
      System.out.println(this.dataFrame.decode(message));
    }
  }

  /**
   * function to trigger an insult to the user
   * @throws IOException throws when and IO exception occurs
   * @throws GrammarNotDefinedException throws when grammar isn't defined
   */
  public void insult() throws IOException, GrammarNotDefinedException {
    if(this.isServer){
      if(this.isRead){
        int size = this.serverInput.readInt();
        this.serverInput.readUTF();
        byte[] senderName = new byte[size];
        this.serverInput.read(senderName);
        serverInput.readUTF();
        size = this.serverInput.readInt();
        this.serverInput.readUTF();
        byte[] recipientName = new byte[size];
        this.serverInput.read(recipientName);
        this.dataFrame.setSenderUserNameInByte(senderName);
        this.dataFrame.setSenderUserName(this.dataFrame.decode(senderName));
        this.dataFrame.setRecipientUserNameInByte(recipientName);
        this.dataFrame.setRecipientUserName(this.dataFrame.decode(recipientName));
      }else {
        SentenceGenerator sentenceGenerator = new SentenceGenerator("template","insult_grammar.json");
        sentenceGenerator.sentenceParser();
        String randomSentence = sentenceGenerator.getRandomSentence();
        this.dataFrame.setMessage(randomSentence);
        byte[] randomSentenceInByte = this.dataFrame.encode(randomSentence);
        this.dataFrame.setMessageInByte(randomSentenceInByte);
        this.dataFrame.setMessageIdentifier(BROADCAST_MESSAGE);
        forwardBroadCastMessage();
      }
    }else {
      this.clientOutput.writeInt(SEND_INSULT);
      this.clientOutput.writeUTF(this.separator);
      this.clientOutput.writeInt(this.dataFrame.getSenderUsernameSize());
      this.clientOutput.writeUTF(this.separator);
      this.clientOutput.write(this.dataFrame.getSenderUserNameInByte());
      this.clientOutput.writeUTF(this.separator);
      this.clientOutput.writeInt(this.dataFrame.getRecipientUserNameSize());
      this.clientOutput.writeUTF(this.separator);
      this.clientOutput.write(this.dataFrame.getRecipientUserNameInByte());
    }
  }

  /**
   * function to read  connect message
   * @throws IOException throws when and IO exception occurs
   */
  public void readConnectMessage() throws IOException {
   setServer(true);
   connectMessage();
  }


  /**
   * function to read disconnect message
   * @throws IOException throws when and IO exception occurs
   */
  public void readDisconnectMessage() throws IOException {
    setServer(true);
    disconnectMessage();
  }


  /**
   * function to send connect response
   * @throws IOException throws when and IO exception occurs
   * @throws InterruptedException throws when an interruption occurs
   */
  public void sendConnectResponse() throws IOException, InterruptedException {
    setServer(true);
    connectResponse();
  }

  /**
   * function to connect to server side
   * @param socket socket to the server side for connection
   * @throws IOException throws when and IO exception occurs
   */
  public void connectToServerIO(Socket socket) throws IOException {
    setSocket(socket);
    setClientInput(new DataInputStream(this.socket.getInputStream()));
    setClientOutput(new DataOutputStream(this.socket.getOutputStream()));
  }

  /**
   * function to query the connected user
   * @throws IOException throws when and IO exception occurs
   */
  public void queryConnectedUsers() throws IOException {
    setServer(false);
    queryUsers();
  }

  /**
   * function to listen to query response from the user
   * @throws IOException throws when and IO exception occurs
   */
  public void listenQueryResponse() throws IOException {
    setServer(false);
    queryResponse();
  }


  /**
   * function to send  client request
   * @throws IOException throws when and IO exception occurs
   * @throws GrammarNotDefinedException grammar not defined exception
   */
  public void sendClientRequest() throws IOException, GrammarNotDefinedException {
    setDataFrame(dataFrame);
    int identifier = this.dataFrame.getMessageIdentifier();
    switch (identifier) {
      case CONNECT_MESSAGE:
        sendConnectMessage();
        break;
      case QUERY_CONNECTED_USERS:
        queryConnectedUsers();
        break;
      case DISCONNECT_MESSAGE:
        sendDisconnectMessage();
        break;
      case DIRECT_MESSAGE:
        sendDirectMessage();
        break;
      case BROADCAST_MESSAGE:
        sendBroadcastMessage();
        break;
      case SEND_INSULT:
        sendInsultRequest();
        break;
      default:
        System.out.println("bad request...");
        break;
    }
  }


  /**
   * function to listen the server response
   * @throws IOException throws when and IO exception occurs
   * @throws InterruptedException throws when an interruption has occurred
   */
  public void listenServerResponse() throws IOException, InterruptedException {
    int messageIdentifier = this.clientInput.readInt();
    this.clientInput.readUTF();
    switch (messageIdentifier) {
      case CONNECT_RESPONSE:
        listenConnectResponse();
        String lineBreaker = "--------------------------------\n";
        System.out.println(lineBreaker+this.dataFrame.getMessage());
        break;
      case QUERY_USER_RESPONSE:
        listenQueryResponse();
        break;
      case DIRECT_MESSAGE:
        listenDirectMessageResponse();
        break;
      case BROADCAST_MESSAGE:
        listenBroadcastMessage();
        break;
      case FAILED_MESSAGE:
        listenFailedMessage();
        break;
      default:
        System.out.println("not recognize the response...");
        break;
    }
  }



  /**
   * function to read operation read message identifier and separator in advance
   * @throws IOException throws when and IO exception occurs
   * @throws InterruptedException throws when an interruption has occurred
   */
  public void listenConnectResponse() throws IOException, InterruptedException {
    setServer(false);
    connectResponse();
  }


  /**
   * function to send connect message
   * @throws IOException throws when and IO exception occurs
   */
  public void sendConnectMessage() throws IOException {
    setServer(false);
    connectMessage();
  }

  /**
   * function to send the disconnect message
   * @throws IOException throws when and IO exception occurs
   */
  public void sendDisconnectMessage() throws IOException {
    setServer(false);
    disconnectMessage();
  }

  /**
   * function used to send direct message
   * @throws IOException throws when and IO exception occurs
   */
  public void sendDirectMessage() throws IOException {
    setServer(false);
    setRead(false);
    directMessage();
  }

  /**
   * function to listen a direct message response
   * @throws IOException throws when and IO exception occurs
   */
  public void listenDirectMessageResponse() throws IOException {
    setServer(false);
    setRead(true);
    directMessage();
  }

  /**
   * function to send broadcast message
   * @throws IOException throws when and IO exception occurs
   */
  public void sendBroadcastMessage() throws IOException {
    setServer(false);
    setRead(false);
    broadcastMessage();
  }

  /**
   * function to listen to broadcast message
   * @throws IOException throws when and IO exception occurs
   */
  public void listenBroadcastMessage() throws IOException {
    setServer(false);
    setRead(true);
    broadcastMessage();
  }

  /**
   * function to listen failed message
   * @throws IOException throws when and IO exception occurs
   */
  public void listenFailedMessage() throws IOException {
    setServer(false);
    failedMessage();
  }

  /**
   * function to send an insult request
   * @throws GrammarNotDefinedException grammar not defined exception
   * @throws IOException throws when and IO exception occurs
   */
  public void sendInsultRequest() throws GrammarNotDefinedException, IOException {
    setServer(false);
    setRead(false);
    insult();
  }

  /**
   * function to check the read status
   * @return isRead
   */
  public boolean isRead() {
    return isRead;
  }

  /**
   * function to set the read status
   * @param read read the user request
   */
  public void setRead(boolean read) {
    isRead = read;
  }

  /**
   * function to check the status of the server
   * @return isServer
   */
  public boolean isServer() {
    return isServer;
  }

  /**
   * function to set the server status
   * @param server server side of the chat room
   */
  public void setServer(boolean server) {
    isServer = server;
  }

  /**
   * function to get socket
   * @return socket
   */
  public Socket getSocket() {
    return socket;
  }

  /**
   * function set the socket for the connection
   * @param socket used for connection
   */
  public void setSocket(Socket socket) {
    this.socket = socket;
  }


  /**
   * function to get the active client list
   * @return activeClientList
   */
  public ActiveClientList getActiveClientList() {
    return activeClientList;
  }


  /**
   * function to set the active client list
   * @param activeClientList active Client List
   */
  public void setActiveClientList(ActiveClientList activeClientList) {
    this.activeClientList = activeClientList;
  }

  /**
   * function to get the client socket
   * @return clientSocket
   */
  public Socket getClientSocket() {
    return clientSocket;
  }


  /**
   * function set the client socket
   * @param clientSocket client socket for connecting to the server side
   */
  public void setClientSocket(Socket clientSocket) {
    this.clientSocket = clientSocket;
  }

  /**
   * function to get the state
   * @return STATE
   */
  public State getSTATE() {
    return STATE;
  }

  /**
   * function set the state
   * @param STATE state of the message
   */
  public void setSTATE(State STATE) {
    this.STATE = STATE;
  }

  /**
   * function to set the date frame for sending the message
   * @return dataFrame
   */
  public DataFrame getDataFrame() {
    return dataFrame;
  }


  /**
   * function to set the dataframe
   * @param dataFrame data frame format for sending the message
   */
  public void setDataFrame(DataFrame dataFrame) {
    this.dataFrame = dataFrame;
  }

  /**
   * function to get client input
   * @return  clientInput
   */
  public DataInputStream getClientInput() {
    return clientInput;
  }

  /**
   * function to set the client input
   * @param clientInput input from the client
   */
  public void setClientInput(DataInputStream clientInput) {
    this.clientInput = clientInput;
  }

  /**
   * function to get server input
   * @return serverInput
   */
  public DataInputStream getServerInput() {
    return serverInput;
  }

  /**
   * function to set server input
   * @param serverInput input from the server
   */
  public void setServerInput(DataInputStream serverInput) {
    this.serverInput = serverInput;
  }

  /**
   * function to get server output
   * @return serverOutput
   */
  public DataOutputStream getServerOutput() {
    return serverOutput;
  }


  /**
   * function to set server output
   * @param serverOutput output from the server
   */
  public void setServerOutput(DataOutputStream serverOutput) {
    this.serverOutput = serverOutput;
  }

  /**
   * function to get the client output
   * @return clientOutput
   */
  public DataOutputStream getClientOutput() {
    return clientOutput;
  }


  /**
   * function to set the client output
   * @param clientOutput output to the client
   */
  public void setClientOutput(DataOutputStream clientOutput) {
    this.clientOutput = clientOutput;
  }

  /**
   * function to get the separator
   * @return separator
   */
  public String getSeparator() {
    return separator;
  }


  /**
   * function to set the separator
   * @param separator separator for the message
   */
  public void setSeparator(String separator) {
    this.separator = separator;
  }


  /**
   * function to get connect response request from the client
   * @return CONNECT_MESSAGE
   */
  public int getCONNECT_MESSAGE() {
    return CONNECT_MESSAGE;
  }

  /**
   * function to get connect response request from the client
   * @return CONNECT_RESPONSE
   */
  public int getCONNECT_RESPONSE() {
    return CONNECT_RESPONSE;
  }

  /**
   * function to get disconnect response request from the client
   * @return DISCONNECT_MESSAGE
   */
  public int getDISCONNECT_MESSAGE() {
    return DISCONNECT_MESSAGE;
  }


  /**
   * function to get query request to connected users
   * @return QUERY_CONNECTED_USERS
   */
  public int getQUERY_CONNECTED_USERS() {
    return QUERY_CONNECTED_USERS;
  }

  /**
   * function to get query request to user response
   * @return QUERY_USER_RESPONSE
   */
  public int getQUERY_USER_RESPONSE() {
    return QUERY_USER_RESPONSE;
  }


  /**
   * function to get broadcast message from the user
   * @return BROADCAST_MESSAGE
   */
  public int getBROADCAST_MESSAGE() {
    return BROADCAST_MESSAGE;
  }


  /**
   * function to get direct message request
   * @return DIRECT_MESSAGE
   */
  public int getDIRECT_MESSAGE() {
    return DIRECT_MESSAGE;
  }


  /**
   * function to get failed message
   * @return FAILED_MESSAGE
   */
  public int getFAILED_MESSAGE() {
    return FAILED_MESSAGE;
  }


  /**
   * function to get send insult request
   * @return SEND_INSULT
   */
  public int getSEND_INSULT() {
    return SEND_INSULT;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Protocol protocol = (Protocol) o;
    return CONNECT_MESSAGE == protocol.CONNECT_MESSAGE;
  }

  @Override
  public int hashCode() {
    return Objects.hash(CONNECT_MESSAGE);
  }

  @Override
  public String toString() {
    return "Protocol{" +
        "CONNECT_MESSAGE=" + CONNECT_MESSAGE +
        ", CONNECT_RESPONSE=" + CONNECT_RESPONSE +
        ", DISCONNECT_MESSAGE=" + DISCONNECT_MESSAGE +
        ", QUERY_CONNECTED_USERS=" + QUERY_CONNECTED_USERS +
        ", QUERY_USER_RESPONSE=" + QUERY_USER_RESPONSE +
        ", BROADCAST_MESSAGE=" + BROADCAST_MESSAGE +
        ", DIRECT_MESSAGE=" + DIRECT_MESSAGE +
        ", FAILED_MESSAGE=" + FAILED_MESSAGE +
        ", SEND_INSULT=" + SEND_INSULT +
        ", separator='" + separator + '\'' +
        ", dataFrame=" + dataFrame +
        ", clientInput=" + clientInput +
        ", serverInput=" + serverInput +
        ", serverOutput=" + serverOutput +
        ", clientOutput=" + clientOutput +
        ", activeClientList=" + activeClientList +
        ", clientSocket=" + clientSocket +
        ", socket=" + socket +
        ", STATE=" + STATE +
        ", isRead=" + isRead +
        ", isServer=" + isServer +
        '}';
  }
}
