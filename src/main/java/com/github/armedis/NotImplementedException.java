
package com.github.armedis;

public class NotImplementedException extends RuntimeException {

    /**
     * Generated serial version id
     */
    private static final long serialVersionUID = -5270403754855383564L;

    /** 
     * Constructs a new not implemented exception.
     * The cause is not initialized
     */
    public NotImplementedException() {
        super();
    }

    /**
     * Constructs a new not implemented exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     * 
     * @param message the detail message. The detail message is saved for later retrieval by the {@link #getMessage()} method.
     */
    public NotImplementedException(String message) {
        super(message);
    }
}
