package io.renatofreire.core.exceptions;

public class EmailAlreadyValidatedException extends RuntimeException {

    public EmailAlreadyValidatedException(){
        super();
    }

    public EmailAlreadyValidatedException(String emailAlreadyConfirmed) {
        super(emailAlreadyConfirmed);
    }
}
