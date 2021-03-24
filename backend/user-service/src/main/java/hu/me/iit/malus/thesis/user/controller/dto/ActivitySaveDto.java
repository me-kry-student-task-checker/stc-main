package hu.me.iit.malus.thesis.user.controller.dto;

import hu.me.iit.malus.thesis.user.model.ActivityType;
import lombok.Data;

import javax.validation.constraints.Min;

@Data
public class ActivitySaveDto {

    private ActivityType type;

    @Min(1)
    private Long tagId;
}
