package hu.me.iit.malus.thesis.course.model.transaction;

public interface DistributedTransaction {

    void prepare();

    void commit();

    void rollback();
}
