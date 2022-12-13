package client;

import java.util.Objects;

/**
 * Class used to check the IP of the host and port number
 * @author jialiang Chen
 */
public class IPHostChecker {
  private int port;
  private String hostName;


  /**
   * empty constructor of the class
   */
  public IPHostChecker() {
  }

  /**
   * constructor of the class
   * @param input input from the command line
   * @throws InvalidUserInputException throws, if an invalid user command is given
   */
  public IPHostChecker(String...input) throws InvalidUserInputException {
    verify(input);
  }

  /**
   * function to verify the port number and host name
   * @param input input from the command line
   * @throws InvalidUserInputException  throws, if an invalid user command is given
   */
  public void verify(String...input) throws InvalidUserInputException {
    if(input.length==2){
      setHostName(input[0]);
      setPort(Integer.parseInt(input[1]));
    }
    else throw new InvalidUserInputException("InvalidUserInputException: requires 2 arguments, first is host ,and second is port number.");
  }


  /**
   * function to get the port number
   * @return port number
   */
  public int getPort() {
    return port;
  }

  /**
   * function to set the port number
   * @param port port number
   */

  public void setPort(int port) {
    this.port = port;
  }


  /**
   * function to get the host name
   * @return hostName
   */
  public String getHostName() {
    return hostName;
  }


  /**
   * function to set the host name
   * @param hostName hostName
   */
  public void setHostName(String hostName) {
    this.hostName = hostName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IPHostChecker that = (IPHostChecker) o;
    return Objects.equals(hostName, that.hostName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(hostName);
  }

  @Override
  public String toString() {
    return "IPHostChecker{" +
        "port=" + port +
        ", hostName='" + hostName + '\'' +
        '}';
  }
}
