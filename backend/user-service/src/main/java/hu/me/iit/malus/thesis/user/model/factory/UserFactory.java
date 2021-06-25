package hu.me.iit.malus.thesis.user.model.factory;

import hu.me.iit.malus.thesis.user.controller.dto.RegistrationRequest;
import hu.me.iit.malus.thesis.user.model.User;

public interface UserFactory {

    User create(RegistrationRequest registrationRequest);
}
