package hu.me.iit.malus.thesis.feedback.controller;

import hu.me.iit.malus.thesis.feedback.controller.converters.Converter;
import hu.me.iit.malus.thesis.feedback.controller.dto.CourseCommentDto;
import hu.me.iit.malus.thesis.feedback.controller.dto.TaskCommentDto;
import hu.me.iit.malus.thesis.feedback.model.CourseComment;
import hu.me.iit.malus.thesis.feedback.model.TaskComment;
import hu.me.iit.malus.thesis.feedback.service.FeedbackService;
import hu.me.iit.malus.thesis.feedback.service.exception.CommentNotFoundException;
import hu.me.iit.malus.thesis.feedback.service.exception.ForbiddenCommentEditException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * Controller endpoint of this service
 *
 * @author Attila Szőke
 */
@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    private final FeedbackService service;

    @Autowired
    public FeedbackController(FeedbackService service) {
        this.service = service;
    }

    @PostMapping("/createCourseComment")
    public CourseComment create(@RequestBody CourseCommentDto courseComment, Principal principal) {
        CourseComment cc = Converter.CourseCommentDtoToCourseComment(courseComment);
        cc.setAuthorId(principal.getName());
        return service.createCourseComment(cc);
    }

    @PostMapping("/createTaskComment")
    public TaskComment create(@RequestBody TaskCommentDto taskComment, Principal principal) {
        TaskComment tc = Converter.TaskCommentDtoToTaskComment(taskComment);
        tc.setAuthorId(principal.getName());
        return service.createTaskComment(tc);
    }

    @GetMapping("/getAllCourseComments/{courseId}")
    public List<CourseComment> getAllCourseComments(@PathVariable Long courseId) {
        return service.getAllCourseComments(courseId);
    }

    @GetMapping("/getAllTaskComments/{taskId}")
    public List<TaskComment> getAllTaskComments(@PathVariable Long taskId) {
        return service.getAllTaskComments(taskId);
    }

    @DeleteMapping("/delete/course/{commentId}")
    public void deleteCourseComment(@PathVariable Long commentId, Authentication authentication) throws CommentNotFoundException, ForbiddenCommentEditException {
        service.removeCourseComment(commentId, authentication.getName());
    }

    @DeleteMapping("/delete/task/{commentId}")
    public void deleteTaskComment(@PathVariable Long commentId, Authentication authentication) throws CommentNotFoundException, ForbiddenCommentEditException {
        service.removeTaskComment(commentId, authentication.getName());
    }

    @DeleteMapping("/removeCourseCommentsByCourseId/{courseId}")
    @PreAuthorize("hasRole('ROLE_Teacher')")
    public void removeCourseCommentsByCourseId(@PathVariable Long courseId) {
        service.removeFeedbacksByCourseId(courseId);
    }

    @DeleteMapping("/removeTaskCommentsByTaskId/{taskId}")
    @PreAuthorize("hasRole('ROLE_Teacher')")
    public void removeTaskCommentsByTaskId(@PathVariable Long taskId) {
        service.removeFeedbacksByTaskId(taskId);
    }
}
