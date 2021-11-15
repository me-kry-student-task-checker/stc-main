package hu.me.iit.malus.thesis.transaction;

/**
 * A factory interface which creates DistributedTransactions based on an object.
 * Should be implemented in service where there is a need for DistributedTransaction to make instantiation logic simpler.
 *
 * @author Attila Szőke
 */
public interface DistributedTransactionFactory {

    /**
     * Creates a DistributedTransaction.
     *
     * @param object the object the Transaction is based on, should be cast to the model object which needs distributed transaction
     * @return the transaction
     */
    DistributedTransaction create(Object object);
}
