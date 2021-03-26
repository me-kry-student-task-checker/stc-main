package hu.me.iit.malus.thesis.user.controller.dto;

import hu.me.iit.malus.thesis.user.model.Activity;
import lombok.Data;

@Data
public class ActivitySaveDto {

    private Activity.ActivityType type;

}
