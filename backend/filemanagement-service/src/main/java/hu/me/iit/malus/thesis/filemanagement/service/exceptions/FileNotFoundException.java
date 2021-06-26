package hu.me.iit.malus.thesis.filemanagement.service.exceptions;

public class FileNotFoundException extends Exception {

    private static final String ERROR_MSG = "This file could not be found!";

    public FileNotFoundException() {
        super(ERROR_MSG);
    }

    public FileNotFoundException(Throwable cause) {
        super(ERROR_MSG, cause);
    }
}
