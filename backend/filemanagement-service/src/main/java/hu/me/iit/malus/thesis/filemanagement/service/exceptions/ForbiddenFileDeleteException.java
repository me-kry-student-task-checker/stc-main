package hu.me.iit.malus.thesis.filemanagement.service.exceptions;

public class ForbiddenFileDeleteException extends Exception {

    private static final String ERROR_MSG = "File deletion is not permitted for this user!";

    public ForbiddenFileDeleteException() {
        super(ERROR_MSG);
    }
}
