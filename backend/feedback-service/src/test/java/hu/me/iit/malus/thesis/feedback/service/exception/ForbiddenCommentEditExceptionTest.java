package hu.me.iit.malus.thesis.feedback.service.exception;

import static org.junit.Assert.*;


import org.junit.Test;

public class ForbiddenCommentEditExceptionTest {

	@Test(expected = ForbiddenCommentEditException.class)
	public void testForbiddenCommentEditException() throws ForbiddenCommentEditException {
		throw new ForbiddenCommentEditException();
	}

	@Test(expected = ForbiddenCommentEditException.class)
	public void testForbiddenCommentEditExceptionString() throws ForbiddenCommentEditException {
		throw new ForbiddenCommentEditException("Test Forbidden comment edit");
	}

	@Test(expected = ForbiddenCommentEditException.class)
	public void testForbiddenCommentEditExceptionStringThrowable() throws ForbiddenCommentEditException {
		Throwable testThrowable = new Throwable();
		throw new ForbiddenCommentEditException("Test Forbidden comment edit", testThrowable);
	}

	@Test(expected = ForbiddenCommentEditException.class)
	public void testForbiddenCommentEditExceptionThrowable() throws ForbiddenCommentEditException {
		Throwable testThrowable = new Throwable();
		throw new ForbiddenCommentEditException(testThrowable);
	}

	@Test(expected = ForbiddenCommentEditException.class)
	public void testForbiddenCommentEditExceptionStringThrowableBooleanBoolean() throws ForbiddenCommentEditException {
		Throwable testThrowable = new Throwable();
		throw new ForbiddenCommentEditException("Test Forbidden comment edit", testThrowable, true, true);
	}

}
