package hu.me.iit.malus.thesis.course.model.transaction.impl;

import hu.me.iit.malus.thesis.course.model.transaction.StepName;
import hu.me.iit.malus.thesis.course.model.transaction.TransactionCommand;
import hu.me.iit.malus.thesis.dto.ServiceType;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class RemoveFileTransactionCommand implements TransactionCommand {

    private final StepName stepName;
    private final ServiceType serviceType;
    private final List<Long> ids;
    private final BiFunction<ServiceType, List<Long>, String> prepare;
    private final Consumer<String> commit;
    private final Consumer<String> rollback;
    private String transactionKey = "";

    @Override
    public void prepare() {
        transactionKey = prepare.apply(serviceType, ids);
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
