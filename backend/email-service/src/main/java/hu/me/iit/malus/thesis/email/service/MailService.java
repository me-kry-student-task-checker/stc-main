package hu.me.iit.malus.thesis.email.service;

import hu.me.iit.malus.thesis.email.controller.dto.MailDto;
import hu.me.iit.malus.thesis.email.service.exception.MailCouldNotBeSentException;

/**
 * The main interface definition of the service.
 *
 * @author Ilku Krisztián
 * @author Attila Szőke
 */
public interface MailService {

    /**
     * Sends an email.
     *
     * @param mail the mail parameters
     */
    void sendEmail(MailDto mail) throws MailCouldNotBeSentException;
}
