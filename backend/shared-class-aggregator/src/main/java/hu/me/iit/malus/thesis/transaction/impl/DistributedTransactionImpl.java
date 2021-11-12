package hu.me.iit.malus.thesis.transaction.impl;

import hu.me.iit.malus.thesis.transaction.DistributedTransaction;
import hu.me.iit.malus.thesis.transaction.TransactionCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class DistributedTransactionImpl implements DistributedTransaction {

    private final List<TransactionCommand> transactionCommands;
    private final List<TransactionCommand> doneTransactionCommands = new ArrayList<>();

    @Override
    public void prepare() {
        transactionCommands.forEach(transactionCommand -> {
            transactionCommand.prepare();
            doneTransactionCommands.add(transactionCommand);
            log.debug("Prepared transaction command with stepName: {}", transactionCommand.getStepName());
        });
    }

    @Override
    public void commit() {
        transactionCommands.forEach(transactionCommand -> {
            transactionCommand.commit();
            log.debug("Committed transaction command with stepName: {}", transactionCommand.getStepName());
        });
    }

    @Override
    public void rollback() {
        doneTransactionCommands.forEach(transactionCommand -> {
            transactionCommand.rollback();
            log.debug("Rolled back transaction command with stepName: {}", transactionCommand.getStepName());
        });
    }
}
