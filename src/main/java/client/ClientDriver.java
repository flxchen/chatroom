package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Objects;
import problem1.sentencegenerator.GrammarNotDefinedException;
import protocol.DataFrame;
import protocol.Protocol;

/**
 * class clientDriver represents a client socket that connects to server, it takes user input,
 * validates them before sending request to server and also listens to server response, display to
 * user
 */
public class ClientDriver {

  private Socket socket;
  private ClientInterface clientInterface;
  private Protocol protocol;
  private BufferedReader userInput;
  private boolean validateUserName;


  /**
   * empty constructor of the class
   */
  public ClientDriver() {
  }

  /**
   * Constructor of the class
   *
   * @param socket socket of the client
   * @throws InvalidUserInputException throws if user provide an invalid command
   */
  public ClientDriver(Socket socket)
      throws InvalidUserInputException {
    this.socket = socket;
    this.protocol = new Protocol(new DataFrame());
    this.clientInterface = new ClientInterface();
    this.clientInterface.setProtocol(protocol);
    this.validateUserName = false;
  }


  /**
   * function to parse the user command
   *
   * @param userCommand user command
   * @return clientInterface
   * @throws InvalidUserInputException throws if user provide an invalid command
   */
  public boolean parseUserCommand(String userCommand)
      throws InvalidUserInputException {
    this.clientInterface.setCommand(userCommand);
    return this.clientInterface.parseCommand(userCommand);
  }


  /**
   * function to get the username from the client
   */
  public void getUserName() {
    try {
      setUserInput(new BufferedReader(new InputStreamReader(System.in)));
      System.out.println("welcome to chatroom, please enter a user name:");
      String userName = null;
      while ((userName = userInput.readLine()) != null) {
        setValidateUserName(this.clientInterface.parseUserName(userName));
        if (this.clientInterface.isShowMenu()) {
          continue;
        }
        if (this.validateUserName) {
          DataFrame dataFrame = this.protocol.getDataFrame();
          dataFrame.setMessageIdentifier(this.protocol.getCONNECT_MESSAGE());
          dataFrame.setSenderUserName(userName);
          dataFrame.setSenderUserNameInByte(dataFrame.encode(userName));
          this.protocol.connectToServerIO(this.socket);
          this.protocol.sendClientRequest();
          break;
        } else {
          System.out.println(
              "not a valid user name, name only contains letter and numbers and cannot be one of the commands, please try again.");
        }
      }
    } catch (IOException | GrammarNotDefinedException e) {
      closeAll();
      System.out.println(e.getMessage());
    }
  }

  /**
   * function to listen to server response
   *
   * @throws IOException          throws if an IO exception occurs
   * @throws InterruptedException throws if any interruption occurs
   */
  public void listenServerResponse() throws IOException, InterruptedException {
    new Thread(() -> {
      while (socket.isConnected()) {
        try {
          protocol.listenServerResponse();
          String response = this.protocol.getDataFrame().getMessage();
          if (this.socket.isClosed() || response.equals("You are no longer connected.") || response.startsWith(
              "fail to connect")) {
            this.protocol.getDataFrame().setDisconnect(true);
            closeAll();
            break;
          }
          System.out.println("\nplease enter a command");
        } catch (IOException e) {
          System.out.println("IOException: Couldn't get I/O for the connection to " +
              getHost() + " at port " + getPort() + "\n" + e.getMessage());
          closeAll();
          break;
        } catch (InterruptedException e) {
          System.out.println(e.getMessage());
          closeAll();
          break;
        }
      }
    }).start();
  }


  /**
   * function to listen to user input
   *
   * @throws GrammarNotDefinedException throws grammar not defined exception( assignment 4)
   * @throws IOException                throws if an IO exception occurs
   */
  public void listenUserInput()
      throws GrammarNotDefinedException, IOException {
    while (this.socket.isConnected()) {
      try {
        String userCommand = userInput.readLine();
        if(this.protocol.getDataFrame().isDisconnect()) {
          System.out.println("you have been disconnected, bye!");
          closeAll();
          break;
        }
        if (parseUserCommand(userCommand)) {
          this.protocol.sendClientRequest();
        }
      } catch (IOException e) {
        System.out.println("IOException: Couldn't get I/O for the connection to " +
            getHost() + " at port " + getPort() + "\n"
            + e.getMessage());
        closeAll();
        break;
      } catch (InvalidUserInputException e) {
        System.out.println(e.getMessage());
        closeAll();
        break;
      }
    }
  }


  /**
   * function to get the host details
   *
   * @return socket.getInetAddress().getHostName()
   */
  public String getHost() {
    return this.socket.getInetAddress().getHostName();
  }

  /**
   * function to get the port number
   *
   * @return socket.getPort()
   */
  public int getPort() {
    return this.socket.getPort();
  }


  /**
   * function to close IO
   */
  public void closeAll() {
    try {
      this.socket.close();
      this.userInput.close();
    } catch (IOException e) {
      System.out.println("IOException: whoops, unable to close...");
    }
  }


  /**
   * boolean function to check the valid username
   *
   * @return validateUserName
   */
  public boolean isValidateUserName() {
    return validateUserName;
  }


  /**
   * function to set the valid username
   *
   * @param validateUserName boolean status if it is a valid name
   */
  public void setValidateUserName(boolean validateUserName) {
    this.validateUserName = validateUserName;
  }


  /**
   * function to get user Input
   *
   * @return userInput
   */
  public BufferedReader getUserInput() {
    return userInput;
  }


  /**
   * function to set the user input
   *
   * @param userInput user input command
   */
  public void setUserInput(BufferedReader userInput) {
    this.userInput = userInput;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ClientDriver that = (ClientDriver) o;
    return validateUserName == that.validateUserName;
  }

  @Override
  public int hashCode() {
    return Objects.hash(validateUserName);
  }

  @Override
  public String toString() {
    return "ClientDriver{" +
        "socket=" + socket +
        ", clientInterface=" + clientInterface +
        ", protocol=" + protocol +
        ", userInput=" + userInput +
        ", validateUserName=" + validateUserName +
        '}';
  }
}