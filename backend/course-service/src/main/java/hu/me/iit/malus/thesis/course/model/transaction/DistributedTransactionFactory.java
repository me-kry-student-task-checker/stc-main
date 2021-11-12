package hu.me.iit.malus.thesis.course.model.transaction;

public interface DistributedTransactionFactory {
    DistributedTransaction create(Object object);
}
