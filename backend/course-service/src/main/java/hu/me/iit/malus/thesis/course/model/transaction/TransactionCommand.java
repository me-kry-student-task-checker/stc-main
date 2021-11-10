package hu.me.iit.malus.thesis.course.model.transaction;

public interface TransactionCommand {

    void prepare();

    void commit();

    void rollback();
}
