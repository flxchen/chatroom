package protocol;

/**
 * enumeration class for storing the constant values which can be used by the client
 * @author jialiang Chen
 */
public enum State {

  /**
   * command used for waiting
   */
  WAITING,
  /**
   * command used to connect message
   */
  CONNECT_MESSAGE,
  /**
   * command used for getting all the active online client
   */
  WHO,
  /**
   * command used for direct message to a particular user
   */
  DIRECT_MESSAGE,
  /**
   * command used for broadcasting
   */
  BROADCAST,
  /**
   * command used to send an insult
   */
  SEND_INSULT,
  /**
   * command used to disconnect message
   */
  DISCONNECT_MESSAGE
}
