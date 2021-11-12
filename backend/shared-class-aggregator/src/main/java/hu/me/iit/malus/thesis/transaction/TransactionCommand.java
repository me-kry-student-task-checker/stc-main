package hu.me.iit.malus.thesis.transaction;

/**
 * Symbolises a step in a distributed transaction. Should be used in conjunction with a DistributedTransaction class.
 *
 * @author Attila Szőke
 */
public interface TransactionCommand {

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

    /**
     * Getter for the Command's step name. It is here, because this way every implementation should have a step variable, used for logging.
     *
     * @return the step name of the command
     */
    StepName getStepName();
}
