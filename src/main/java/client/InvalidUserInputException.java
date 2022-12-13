package client;

/**
 * interruption class, it is triggered if client give an invalid command
 */

public class InvalidUserInputException extends Throwable {

  /**
   * constructor of the interruption class
   * @param message throws the reason for the error
   */

  public InvalidUserInputException(String message) {
    super(message);
  }
}
