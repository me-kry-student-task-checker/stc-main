package hu.me.iit.malus.thesis.course.model.transaction.impl;

import hu.me.iit.malus.thesis.course.model.transaction.DistributedTransaction;
import hu.me.iit.malus.thesis.course.model.transaction.TransactionCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class CourseDistributedTransaction implements DistributedTransaction {

    private final List<TransactionCommand> transactionCommands;
    private final List<TransactionCommand> doneTransactionCommands = new ArrayList<>();

    @Override
    public void prepare() {
        transactionCommands.forEach(transactionCommand -> {
            transactionCommand.prepare();
            doneTransactionCommands.add(transactionCommand);
            log.info("Prepared transaction command with stepName: {}", transactionCommand.getStepName());
        });
    }

    @Override
    public void commit() {
        transactionCommands.forEach(transactionCommand -> {
            transactionCommand.commit();
            log.info("Committed transaction command with stepName: {}", transactionCommand.getStepName());
        });
    }

    @Override
    public void rollback() {
        doneTransactionCommands.forEach(transactionCommand -> {
            transactionCommand.rollback();
            log.info("Rolled back transaction command with stepName: {}", transactionCommand.getStepName());
        });
    }
}
