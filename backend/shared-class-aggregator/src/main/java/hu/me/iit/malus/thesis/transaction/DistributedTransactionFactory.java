package hu.me.iit.malus.thesis.transaction;

public interface DistributedTransactionFactory {
    DistributedTransaction create(Object object);
}
