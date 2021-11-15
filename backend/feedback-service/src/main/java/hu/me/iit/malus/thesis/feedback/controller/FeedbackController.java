package hu.me.iit.malus.thesis.feedback.controller;

import hu.me.iit.malus.thesis.feedback.controller.dto.CourseCommentCreateDto;
import hu.me.iit.malus.thesis.feedback.controller.dto.CourseCommentDetailsDto;
import hu.me.iit.malus.thesis.feedback.controller.dto.TaskCommentCreateDto;
import hu.me.iit.malus.thesis.feedback.controller.dto.TaskCommentDetailsDto;
import hu.me.iit.malus.thesis.feedback.service.FeedbackService;
import hu.me.iit.malus.thesis.feedback.service.exception.CommentNotFoundException;
import hu.me.iit.malus.thesis.feedback.service.exception.ForbiddenCommentEditException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.security.Principal;
import java.util.List;

/**
 * Controller endpoint of this service
 *
 * @author Attila Szőke
 */
@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService service;

    @PostMapping("/createCourseComment")
    public @Valid CourseCommentDetailsDto create(@RequestBody @Valid CourseCommentCreateDto dto, Principal principal) {
        return service.createCourseComment(dto, principal.getName());
    }

    @PostMapping("/createTaskComment")
    public @Valid TaskCommentDetailsDto create(@RequestBody @Valid TaskCommentCreateDto dto, Principal principal) {
        return service.createTaskComment(dto, principal.getName());
    }

    @GetMapping("/getAllCourseComments/{courseId}")
    public List<@Valid CourseCommentDetailsDto> getAllCourseComments(@PathVariable @Min(1) Long courseId) {
        return service.getAllCourseComments(courseId);
    }

    @GetMapping("/getAllTaskComments/{taskId}")
    public List<@Valid TaskCommentDetailsDto> getAllTaskComments(@PathVariable @Min(1) Long taskId) {
        return service.getAllTaskComments(taskId);
    }

    @DeleteMapping("/delete/course/{commentId}")
    public void deleteCourseComment(@PathVariable Long commentId, Principal principal) throws CommentNotFoundException, ForbiddenCommentEditException {
        service.removeCourseComment(commentId, principal.getName());
    }

    @DeleteMapping("/delete/task/{commentId}")
    public void deleteTaskComment(@PathVariable Long commentId, Principal principal) throws CommentNotFoundException, ForbiddenCommentEditException {
        service.removeTaskComment(commentId, principal.getName());
    }

    @PostMapping("/prepare/course/remove/by/{courseIds}")
    public String prepareRemoveCourseCommentsByCourseIds(@PathVariable List<Long> courseIds) {
        return service.prepareRemoveCourseCommentsByCourseIds(courseIds);
    }

    @PostMapping("/commit/course/remove/{transactionKey}")
    public void commitRemoveCourseCommentsByCourseId(@PathVariable String transactionKey) {
        service.commitRemoveCourseCommentsByCourseId(transactionKey);
    }

    @PostMapping("/rollback/course/remove/{transactionKey}")
    public void rollbackRemoveCourseCommentsByCourseId(@PathVariable String transactionKey) {
        service.rollbackRemoveCourseCommentsByCourseId(transactionKey);
    }

    @PostMapping("/prepare/task/remove/by/{taskIds}")
    public String prepareRemoveTaskCommentsByTaskIds(@PathVariable List<Long> taskIds) {
        return service.prepareRemoveTaskCommentsByTaskIds(taskIds);
    }

    @PostMapping("/commit/task/remove/{transactionKey}")
    public void commitRemoveTaskCommentsByTaskIds(@PathVariable String transactionKey) {
        service.commitRemoveTaskCommentsByTaskIds(transactionKey);
    }

    @PostMapping("/rollback/task/remove/{transactionKey}")
    public void rollbackRemoveTaskCommentsByTaskIds(@PathVariable String transactionKey) {
        service.rollbackRemoveTaskCommentsByTaskIds(transactionKey);
    }
}
