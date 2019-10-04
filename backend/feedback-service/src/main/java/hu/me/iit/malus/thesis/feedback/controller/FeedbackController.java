package hu.me.iit.malus.thesis.feedback.controller;

import hu.me.iit.malus.thesis.feedback.controller.converters.Converter;
import hu.me.iit.malus.thesis.feedback.controller.dto.CourseCommentDto;
import hu.me.iit.malus.thesis.feedback.controller.dto.TaskCommentDto;
import hu.me.iit.malus.thesis.feedback.model.CourseComment;
import hu.me.iit.malus.thesis.feedback.model.TaskComment;
import hu.me.iit.malus.thesis.feedback.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller endpoint of this service
 *
 * @author Attila Szőke
 */
@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

    private FeedbackService feedbackService;

    @Autowired
    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping("/createCourseComment")
    public CourseComment create(@RequestBody CourseCommentDto courseComment) {
        return feedbackService.createCourseComment(Converter.CourseCommentDtoToCourseComment(courseComment));
    }

    @PostMapping("/createTaskComment")
    public TaskComment create(@RequestBody TaskCommentDto taskComment) {
        return feedbackService.createTaskComment(Converter.TaskCommentDtoToTaskComment(taskComment));
    }

    @GetMapping("/getAllCourseComments/{courseId}")
    public List<CourseComment> getAllCourseComments(@PathVariable Long courseId) {
        return feedbackService.getAllCourseComments(courseId);
    }

    @GetMapping("/getAllTaskComments/{taskId}")
    public List<TaskComment> getAllTaskComments(@PathVariable Long taskId) {
        return feedbackService.getAllTaskComments(taskId);
    }
}
