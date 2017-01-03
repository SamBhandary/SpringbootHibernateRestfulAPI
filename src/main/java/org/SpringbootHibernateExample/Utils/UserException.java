package org.SpringbootHibernateExample.Utils;

/**
 * UserException for exceptions.
 */
public class UserException extends Exception {

    public UserException(final String message) {
        super(message);
    }

    public UserException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
