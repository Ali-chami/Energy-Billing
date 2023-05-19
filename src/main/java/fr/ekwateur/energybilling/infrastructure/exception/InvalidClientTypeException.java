package fr.ekwateur.energybilling.infrastructure.exception;

public class InvalidClientTypeException extends RuntimeException {

  public InvalidClientTypeException(String message) {
    super(message);
  }
}
