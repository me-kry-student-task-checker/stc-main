package hu.me.iit.malus.thesis.transaction;

public interface TransactionCommand {

    void prepare();

    void commit();

    void rollback();

    StepName getStepName();
}
