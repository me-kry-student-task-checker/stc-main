package hu.me.iit.malus.thesis.transaction.impl;

import hu.me.iit.malus.thesis.dto.ServiceType;
import hu.me.iit.malus.thesis.transaction.StepName;
import hu.me.iit.malus.thesis.transaction.TransactionCommand;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * Implementation of TransactionCommand interface.
 * It's used when calling the 2PC methods of the file service.
 *
 * @author Attila Szőke
 */
@RequiredArgsConstructor
public class RemoveFileTransactionCommand implements TransactionCommand {

    private final StepName stepName;
    private final ServiceType serviceType;
    private final List<Long> tagIds;
    private final BiFunction<ServiceType, List<Long>, String> prepare;
    private final Consumer<String> commit;
    private final Consumer<String> rollback;
    private String transactionKey = "";

    @Override
    public void prepare() {
        transactionKey = prepare.apply(serviceType, tagIds);
    }

    @Override
    public void commit() { // nincs param beallitottat hasznal
        commit.accept(transactionKey);
    }

    @Override
    public void rollback() {
        rollback.accept(transactionKey);
    }

    @Override
    public StepName getStepName() {
        return stepName;
    }
}
