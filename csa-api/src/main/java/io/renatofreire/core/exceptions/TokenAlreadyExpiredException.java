package io.renatofreire.core.exceptions;

public class TokenAlreadyExpiredException extends RuntimeException {

    public TokenAlreadyExpiredException(){
        super();
    }

    public TokenAlreadyExpiredException(String tokenExpired) {
        super(tokenExpired);
    }
}
