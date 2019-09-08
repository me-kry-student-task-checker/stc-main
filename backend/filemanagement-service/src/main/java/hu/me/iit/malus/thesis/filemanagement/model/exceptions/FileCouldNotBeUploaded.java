package hu.me.iit.malus.thesis.filemanagement.model.exceptions;

/**
 * @author Ilku Krisztian
 */

public class FileCouldNotBeUploaded extends Exception {
    public FileCouldNotBeUploaded() {
        super();
    }

    public FileCouldNotBeUploaded(String message) {
        super(message);
    }

    public FileCouldNotBeUploaded(String message, Throwable cause) {
        super(message, cause);
    }

    public FileCouldNotBeUploaded(Throwable cause) {
        super(cause);
    }

    protected FileCouldNotBeUploaded(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
