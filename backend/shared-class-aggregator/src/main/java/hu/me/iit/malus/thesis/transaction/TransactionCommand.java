package hu.me.iit.malus.thesis.transaction;

/**
 * Symbolises a step in a distributed transaction. Should be used in conjunction with a DistributedTransaction implementation.
 *
 * @author Attila Szőke
 */
public interface TransactionCommand extends DistributedTransaction {

    /**
     * Getter for the Command's step name. It is here, because this way every implementation should have a step variable, used for logging.
     *
     * @return the step name of the command
     */
    StepName getStepName();
}
