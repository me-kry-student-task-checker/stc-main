package hu.me.iit.malus.thesis.task.model.transaction;

import hu.me.iit.malus.thesis.dto.ServiceType;
import hu.me.iit.malus.thesis.dto.TaskComment;
import hu.me.iit.malus.thesis.task.client.FeedbackClient;
import hu.me.iit.malus.thesis.task.client.FileManagementClient;
import hu.me.iit.malus.thesis.task.model.Task;
import hu.me.iit.malus.thesis.transaction.DistributedTransaction;
import hu.me.iit.malus.thesis.transaction.DistributedTransactionFactory;
import hu.me.iit.malus.thesis.transaction.StepName;
import hu.me.iit.malus.thesis.transaction.TransactionCommand;
import hu.me.iit.malus.thesis.transaction.impl.DistributedTransactionImpl;
import hu.me.iit.malus.thesis.transaction.impl.RemoveByIdListTransactionCommand;
import hu.me.iit.malus.thesis.transaction.impl.RemoveFileTransactionCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of DistributedTransactionFactory, specialized for Task objects.
 *
 * @author Attila Szőke
 */
@Component
@RequiredArgsConstructor
public class TaskDistributedTransactionFactory implements DistributedTransactionFactory {

    private final FeedbackClient feedbackClient;
    private final FileManagementClient fileManagementClient;

    @Override
    public DistributedTransaction create(Object object) {
        Task task = (Task) object;
        List<TransactionCommand> commands = new ArrayList<>();

        Long taskId = task.getId();
        List<Long> taskCommentIds = feedbackClient.getAllTaskComments(taskId).stream().map(TaskComment::getId).collect(Collectors.toList());

        if (!taskCommentIds.isEmpty()) {
            commands.add(new RemoveByIdListTransactionCommand(
                    StepName.TASK_COMMENT_REMOVAL, List.of(taskId),
                    feedbackClient::prepareRemoveTaskCommentsByTaskIds,
                    feedbackClient::commitRemoveTaskCommentsByTaskIds,
                    feedbackClient::rollbackRemoveTaskCommentsByTaskIds
            ));
            commands.add(new RemoveFileTransactionCommand(
                    StepName.TASK_COMMENT_FILE_REMOVAL, ServiceType.FEEDBACK, taskCommentIds,
                    fileManagementClient::prepareRemoveFilesByServiceTypeAndTagIds,
                    fileManagementClient::commitRemoveFilesByServiceTypeAndTagIds,
                    fileManagementClient::rollbackRemoveFilesByServiceTypeAndTagIds
            ));
        }
        commands.add(new RemoveFileTransactionCommand(
                StepName.TASK_FILE_REMOVAL, ServiceType.TASK, List.of(taskId),
                fileManagementClient::prepareRemoveFilesByServiceTypeAndTagIds,
                fileManagementClient::commitRemoveFilesByServiceTypeAndTagIds,
                fileManagementClient::rollbackRemoveFilesByServiceTypeAndTagIds
        ));
        return new DistributedTransactionImpl(commands);
    }
}
