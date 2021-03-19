package hu.me.iit.malus.thesis.user.controller.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.validation.ObjectError;

import java.util.List;

@Data
public class RegistrationResponse {
    private String message;
    private String error;

    public RegistrationResponse(final String message) {
        this.message = message;
    }

    public RegistrationResponse(final String message, final String error) {
        this.message = message;
        this.error = error;
    }

    public RegistrationResponse(List<ObjectError> globalErrors, String message) {
        super();
        ObjectMapper mapper = new ObjectMapper();
        try {
            this.message = mapper.writeValueAsString(message);
            this.error = mapper.writeValueAsString(globalErrors);
        } catch (JsonProcessingException e) {
            this.message = "";
            this.error = "";
        }
    }
}
