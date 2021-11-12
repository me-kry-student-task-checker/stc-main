package hu.me.iit.malus.thesis.transaction;

public interface DistributedTransaction {

    void prepare();

    void commit();

    void rollback();
}
