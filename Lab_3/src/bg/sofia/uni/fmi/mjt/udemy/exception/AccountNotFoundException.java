package bg.sofia.uni.fmi.mjt.udemy.exception;

import bg.sofia.uni.fmi.mjt.udemy.account.AccountBase;

public class AccountNotFoundException extends Exception {
    public AccountNotFoundException(String message) {super(message);}
}
