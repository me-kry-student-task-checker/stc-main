package hu.me.iit.malus.thesis.feedback.service.exception;

import org.junit.Test;

public class CommentNotFoundExceptionTest {

	@Test(expected = CommentNotFoundException.class)
	public void testCommentNotFoundException() throws CommentNotFoundException {
		throw new CommentNotFoundException();
	}

	@Test(expected = CommentNotFoundException.class)
	public void testCommentNotFoundExceptionString() throws CommentNotFoundException {
		throw new CommentNotFoundException("Test CommentNotFoundException");
	}

	@Test(expected = CommentNotFoundException.class)
	public void testCommentNotFoundExceptionStringThrowable() throws CommentNotFoundException {
		Throwable testThrowable = new Throwable();
		
		throw new CommentNotFoundException("Test CommentNotFoundException", testThrowable);
	}

	@Test(expected = CommentNotFoundException.class)
	public void testCommentNotFoundExceptionThrowable() throws CommentNotFoundException {
		Throwable testThrowable = new Throwable();
		
		throw new CommentNotFoundException(testThrowable);
	}

	@Test(expected = CommentNotFoundException.class)
	public void testCommentNotFoundExceptionStringThrowableBooleanBoolean() throws CommentNotFoundException {
		Throwable testThrowable = new Throwable();
		
		throw new CommentNotFoundException("Test CommentNotFoundException", testThrowable, true, true);
	}

}
