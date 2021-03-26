package hu.me.iit.malus.thesis.user.controller.dto;

import hu.me.iit.malus.thesis.user.model.Activity;
import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class ActivityDto {

    @Min(1)
    private Long id;

    private Activity.ActivityType type;
}
