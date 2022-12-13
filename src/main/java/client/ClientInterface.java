package client;


import java.util.Objects;
import java.util.regex.Pattern;
import protocol.DataFrame;
import protocol.Protocol;

/**
 * class for providing the user interface to the client
 */

public class ClientInterface {


  /**
   * list of commands, that can be used by any client
   */
  public static final String[] COMMAND_LIST = {"?","logoff","who","@user","@all","!user"},
  /**
   * command messages
   */
  COMMAND_MESSAGE = {"show all commands","sends a DISCONNECT_MESSAGE to the server","sends a QUERY_CONNECTED_USERS to the server","sends a DIRECT_MESSAGE to the specified user to the server",
      "sends a BROADCAST_MESSAGE to the server, to be sent to all users connected","sends a SEND_INSULT message to the server to be sent to the specified user"};
  private String command, message, recipientName;

  private Protocol protocol;
  private boolean showMenu;


  /**
   * constructor of the class
   * @param command command from the user
   * @param protocol protocol that need to be applied
   */
  public ClientInterface(String command, Protocol protocol) {
    this.command = command;
    this.protocol = protocol;
    setShowMenu(false);
  }


  /**
   * empty constructor of the class
   */
  public ClientInterface() {
  }

  /**
   * boolean function to get the status to parse the direct message request
   * @param input input from the user
   * @return boolean status if it is parsable
   */
  public boolean parseDirectMessage(String input){
    String[] split = split(input);
    if(split.length < 2) return false;
    if(input.charAt(0) != '@') return false;
    if(split[0].equals(COMMAND_LIST[4])) return false;
    setCommand(split[0]);
    setMessage(input.substring(split[0].length()+1));
    setRecipientName(split[0].substring(1));
    return true;
  }

  /**
   * boolean function to get the status to parse the BroadCast message request
   * @param input input from the user
   * @return boolean status if it is parsable
   */
  public boolean parseBroadCast(String input){
    String[] split = split(input);
    if(split.length < 2) return false;
    if(!split[0].equals(COMMAND_LIST[4])) return false;
    setMessage(input.substring(COMMAND_LIST[4].length()+1));
    return true;
  }

  /**
   * function to split the user input
   * @param input  input from the client
   * @return input
   */
  private String[] split(String input){
    return input.split("\\s");
  }

  /**
   * function to parse insult grammar(assignment 4)
   * @param input input from the client
   * @return boolean status if it is possible
   */
  public boolean parseInsult(String input){
    String[] split = split(input);
    if(split.length != 1) return false;
    if(input.charAt(0) != '!') return false;
    setCommand(input);
    setRecipientName(split[0].substring(1));
    return true;
  }

  /**
   * function to validate and parse the username
   * @param input input from the client
   * @return boolean status if it is possible
   */
  public boolean parseUserName(String input){
    if(input.equals(COMMAND_LIST[0])) {
      showMenu();
      setShowMenu(true);
      return false;
    }
    setShowMenu(false);
    if(input.equals(COMMAND_LIST[1]) || input.equals(COMMAND_LIST[2])) return false;
    return Pattern.compile("\\w+").matcher(input).matches();
  }


  /**
   * Boolean function to check and read through the user command
   * @param input input from the client
   * @return boolean status if it is possible
   * @throws InvalidUserInputException if an invalid valid is given by the user
   */
  public boolean parseCommand(String input) throws InvalidUserInputException{
    DataFrame dataFrame = this.protocol.getDataFrame();
    if((input == null) || input.equals("")  ) throw new InvalidUserInputException("InvalidUserInputException: command cannot be empty");
    if(input.equals(COMMAND_LIST[0])) {
      showMenu();
      setShowMenu(true);
      return false;
    }
    else if(input.startsWith(COMMAND_LIST[1])){
       dataFrame.setMessageIdentifier(this.protocol.getDISCONNECT_MESSAGE());
       return true;
    } else if (input.equals(COMMAND_LIST[2])) {
      dataFrame.setMessageIdentifier(this.protocol.getQUERY_CONNECTED_USERS());
      return true;
    } else if (parseDirectMessage(input)) {
      dataFrame.setMessageIdentifier(this.protocol.getDIRECT_MESSAGE());
      dataFrame.setRecipientUserName(recipientName);
      dataFrame.setRecipientUserNameInByte(dataFrame.encode(recipientName));
      dataFrame.setMessage(this.message);
      dataFrame.setMessageInByte(dataFrame.encode(this.message));
      return true;
    } else if (parseBroadCast(input)) {
      dataFrame.setMessageIdentifier(this.protocol.getBROADCAST_MESSAGE());
      dataFrame.setMessage(this.message);
      dataFrame.setMessageInByte(dataFrame.encode(this.message));
      return true;
    } else if (parseInsult(input)) {
      dataFrame.setMessageIdentifier(this.protocol.getSEND_INSULT());
      dataFrame.setRecipientUserName(this.recipientName);
      dataFrame.setRecipientUserNameInByte(dataFrame.encode(this.recipientName));
      return true;
    }
    else throw new InvalidUserInputException("InvalidUserInputException: not a valid command...");
  }


  /**
   * function to show the menu to the client
   */
  public void showMenu(){
    for(int i = 0;i< COMMAND_MESSAGE.length;i++) {
      System.out.print(COMMAND_LIST[i]+":");
      System.out.println(COMMAND_MESSAGE[i]);
    }
  }


  /**
   * function to get recipient Name
   * @return recipientName
   */
  public String getRecipientName() {
    return recipientName;
  }


  /**
   * function to set the recipient Name
   * @param recipientName name of the recipient name
   */
  public void setRecipientName(String recipientName) {
    this.recipientName = recipientName;
  }


  /**
   * function to get command list
   * @return  COMMAND_LIST
   */
  public String[] getCommandList() {
    return COMMAND_LIST;
  }

  /**
   * function to show the boolean status of the menu
   * @return showMenu
   */
  public boolean isShowMenu() {
    return showMenu;
  }


  /**
   * function to set boolean status of the menu to the user
   * @param showMenu boolean value for showing the menu to the user
   */
  public void setShowMenu(boolean showMenu) {
    this.showMenu = showMenu;
  }


  /**
   * function to get the command messages
   * @return COMMAND_MESSAGE
   */
  public String[] getCommandMessage() {
    return COMMAND_MESSAGE;
  }


  /**
   * function to get message
   * @return message
   */
  public String getMessage() {
    return message;
  }


  /**
   * function to set message
   * @param message message from the client
   */
  public void setMessage(String message) {
    this.message = message;
  }


  /**
   * function to get command
   * @return command
   */
  public String getCommand() {
    return command;
  }

  /**
   * function to set the command
   * @param command command from the user
   */
  public void setCommand(String command) {
    this.command = command;
  }


  /**
   * function to get the protocol
   * @return protocol
   */
  public Protocol getProtocol() {
    return protocol;
  }


  /**
   * function to set the protocol
   * @param protocol protocol need to taken for each command
   */
  public void setProtocol(Protocol protocol) {
    this.protocol = protocol;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ClientInterface that = (ClientInterface) o;
    return Objects.equals(command, that.command);
  }

  @Override
  public int hashCode() {
    return Objects.hash(command);
  }

  @Override
  public String toString() {
    return "ClientInterface{" +
        "command='" + command + '\'' +
        ", message='" + message + '\'' +
        ", recipientName='" + recipientName + '\'' +
        ", protocol=" + protocol +
        ", showMenu=" + showMenu +
        '}';
  }
}
