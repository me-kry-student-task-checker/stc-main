package hu.me.iit.malus.thesis.task.service.exception;

/**
 * Exception class, which is thrown when a student id can not be found in a list
 *
 * @author Attila Szőke
 */
public class StudentIdNotFoundException extends Exception {

    private static final String ERROR_MSG = "Student with this id could not be found!";

    public StudentIdNotFoundException() {
        super(ERROR_MSG);
    }
}
