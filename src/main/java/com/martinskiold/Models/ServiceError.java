package com.martinskiold.Models;

/**
 * Additional error information.
 *
 * Created by martinskiold on 11/27/16.
 */
public class ServiceError
{
    private String errorCode;
    private String errorMessage;
    private String developerMessage;

    public ServiceError(String errorCode, String errorMessage, String developerMessage)
    {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.developerMessage = developerMessage;
    }

    public String getErrorCode()
    {
        return errorCode;
    }
    public String getErrorMessage()
    {
        return errorMessage;
    }
    public String getDeveloperMessage()
    {
        return developerMessage;
    }

    public void setErrorCode(String errorCode)
    {
        this.errorCode = errorCode;
    }
    public void setErrorMessage(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }
    public void setDeveloperMessage(String developerMessage)
    {
        this.developerMessage = developerMessage;
    }
}
