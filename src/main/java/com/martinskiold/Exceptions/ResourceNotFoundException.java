package com.martinskiold.Exceptions;

import com.martinskiold.Models.ServiceError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * An exception that represents HTTP 404 Not Found error.
 * When thrown from a controller, Spring invokes a HTTP 404 Not Found error message.
 *
 * Created by martinskiold on 11/27/16.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException
{
    private ServiceError error;

    public ResourceNotFoundException()
    {
        super();
    }
    public ResourceNotFoundException(String message, String developerMessage)
    {
        super(message);
        this.error = new ServiceError("404 Not Found", message, developerMessage);
    }
}
