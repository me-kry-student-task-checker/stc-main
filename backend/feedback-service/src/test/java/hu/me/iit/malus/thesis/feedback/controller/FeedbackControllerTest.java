package hu.me.iit.malus.thesis.feedback.controller;

import hu.me.iit.malus.thesis.feedback.service.FeedbackService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FeedbackControllerTest {

    @Mock
    private FeedbackService service;

    @InjectMocks
    private FeedbackController controller;

    @Test
    public void create() {
    }

    @Test
    public void testCreate() {
    }

    @Test
    public void getAllCourseComments() {
    }

    @Test
    public void getAllTaskComments() {
    }

    @Test
    public void deleteCourseComment() {
    }

    @Test
    public void deleteTaskComment() {
    }

    @Test
    public void removeCourseCommentsByCourseId() {
    }

    @Test
    public void removeTaskCommentsByTaskId() {
    }
}