package hu.me.iit.malus.thesis.feedback.client.dto;

import hu.me.iit.malus.thesis.feedback.client.dto.enums.ActivityType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ActivitySaveDto {

    private ActivityType type;
    private Long parentId;

}
