package hu.me.iit.malus.thesis.course.model.transaction.impl;

import hu.me.iit.malus.thesis.course.client.FeedbackClient;
import hu.me.iit.malus.thesis.course.client.FileManagementClient;
import hu.me.iit.malus.thesis.course.client.TaskClient;
import hu.me.iit.malus.thesis.course.model.Course;
import hu.me.iit.malus.thesis.course.model.transaction.TransactionCommand;
import hu.me.iit.malus.thesis.course.model.transaction.TransactionCommandListFactory;
import hu.me.iit.malus.thesis.dto.CourseComment;
import hu.me.iit.malus.thesis.dto.ServiceType;
import hu.me.iit.malus.thesis.dto.Task;
import hu.me.iit.malus.thesis.dto.TaskComment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TransactionCommandListFactoryImpl implements TransactionCommandListFactory {

    private final TaskClient taskClient;
    private final FeedbackClient feedbackClient;
    private final FileManagementClient fileManagementClient;

    @Override
    public List<TransactionCommand> create(Course course) {
        List<TransactionCommand> commands = new ArrayList<>();
        Long courseId = course.getId();
        List<Long> taskIds = course.getTasks().stream().map(Task::getId).collect(Collectors.toList());
        List<Long> taskCommentIds = course.getTasks().stream()
                .map(Task::getComments)
                .flatMap(Collection::stream)
                .map(TaskComment::getId)
                .collect(Collectors.toList());
        List<Long> courseCommentIds = course.getComments().stream()
                .map(CourseComment::getId)
                .collect(Collectors.toList());

        if (!course.getTasks().isEmpty()) {
            commands.add(new RemoveCourseDataTransactionCommand(
                    taskIds,
                    taskClient::prepareRemoveTaskByTaskIds,
                    taskClient::commitRemoveTaskByCourseId,
                    taskClient::rollbackRemoveTaskByCourseId)
            );
        }
        if (!taskIds.isEmpty()) {
            commands.add(new RemoveCourseDataTransactionCommand(
                    taskIds,
                    feedbackClient::prepareRemoveTaskCommentsByTaskIds,
                    feedbackClient::commitRemoveTaskCommentsByTaskIds,
                    feedbackClient::rollbackRemoveTaskCommentsByTaskIds)
            );
            commands.add(new RemoveFileTransactionCommand(
                    ServiceType.TASK, taskIds,
                    fileManagementClient::prepareRemoveFilesByServiceTypeAndTagIds,
                    fileManagementClient::commitRemoveFilesByServiceTypeAndTagIds,
                    fileManagementClient::rollbackRemoveFilesByServiceTypeAndTagIds)
            );
        }
        if (!taskCommentIds.isEmpty()) {
            commands.add(new RemoveFileTransactionCommand(
                    ServiceType.FEEDBACK, taskCommentIds,
                    fileManagementClient::prepareRemoveFilesByServiceTypeAndTagIds,
                    fileManagementClient::commitRemoveFilesByServiceTypeAndTagIds,
                    fileManagementClient::rollbackRemoveFilesByServiceTypeAndTagIds)
            );
        }
        // Removal of course comments and everything connected to it
        if (!course.getComments().isEmpty()) {
            commands.add(new RemoveCourseDataTransactionCommand(
                    List.of(courseId),
                    feedbackClient::prepareRemoveCourseCommentsByCourseIds,
                    feedbackClient::commitRemoveCourseCommentsByCourseId,
                    feedbackClient::rollbackRemoveCourseCommentsByCourseId)
            );
        }
        if (!courseCommentIds.isEmpty()) {
            commands.add(new RemoveFileTransactionCommand(
                    ServiceType.FEEDBACK, courseCommentIds,
                    fileManagementClient::prepareRemoveFilesByServiceTypeAndTagIds,
                    fileManagementClient::commitRemoveFilesByServiceTypeAndTagIds,
                    fileManagementClient::rollbackRemoveFilesByServiceTypeAndTagIds)
            );
        }
        // Removal of course files
        if (!course.getFiles().isEmpty()) {
            commands.add(new RemoveFileTransactionCommand(
                    ServiceType.COURSE, List.of(courseId),
                    fileManagementClient::prepareRemoveFilesByServiceTypeAndTagIds,
                    fileManagementClient::commitRemoveFilesByServiceTypeAndTagIds,
                    fileManagementClient::rollbackRemoveFilesByServiceTypeAndTagIds)
            );
        }
        return commands;
    }
}
