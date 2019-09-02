package com.maxgarfinkel.treeStore.exceptions;

import org.springframework.dao.DuplicateKeyException;

public class DuplicateEntityException extends RuntimeException {

    public DuplicateEntityException(){};

    public DuplicateEntityException(String message){super(message);}

    public DuplicateEntityException(String message, Throwable throwable){super(message, throwable);}

}
