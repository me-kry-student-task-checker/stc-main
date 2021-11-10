package hu.me.iit.malus.thesis.course.model.transaction.impl;

import hu.me.iit.malus.thesis.course.model.transaction.TransactionCommand;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

@RequiredArgsConstructor
public class RemoveCourseDataTransactionCommand implements TransactionCommand {

    private final List<Long> ids;
    private final Function<List<Long>, String> prepare;
    private final Consumer<String> commit;
    private final Consumer<String> rollback;
    private String transactionKey = "";

    @Override
    public void prepare() {
        transactionKey = prepare.apply(ids);
    }

    @Override
    public void commit() { // nincs param beallitottat hasznal
        commit.accept(transactionKey);
    }

    @Override
    public void rollback() {
        rollback.accept(transactionKey);
    }
}
