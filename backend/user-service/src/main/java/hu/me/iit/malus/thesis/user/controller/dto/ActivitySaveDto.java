package hu.me.iit.malus.thesis.user.controller.dto;

import hu.me.iit.malus.thesis.user.model.ActivityType;
import lombok.Data;

@Data
public class ActivitySaveDto {

    private ActivityType type;

}
