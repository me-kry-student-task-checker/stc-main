package hu.me.iit.malus.thesis.transaction;

/**
 * Symbolises a 2PC distributed transaction. Should be used in conjunction with TransactionCommand classes.
 *
 * @author Attila Szőke
 */
public interface DistributedTransaction {

    /**
     * Prepares the transaction to commit.
     */
    void prepare();

    /**
     * Commits the transaction.
     */
    void commit();

    /**
     * Rolls back the transaction in case of error.
     */
    void rollback();
}
