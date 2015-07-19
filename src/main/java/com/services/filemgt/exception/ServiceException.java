package com.services.filemgt.exception;

import java.io.Serializable;

/**
 * Created by Bandula Gamage on 19/07/2015.
 */
public class ServiceException extends Exception implements Serializable {

    private static final long serialVersionUID = 1L;

    public ServiceException() {
        super();
    }
    public ServiceException(String msg)   {
        super(msg);
    }
    public ServiceException(String msg, Exception e)  {
        super(msg, e);
    }
}
