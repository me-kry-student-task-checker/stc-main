package hu.me.iit.malus.thesis.feedback.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ActivityTransactionDto {

    private String userId;
    private ActivityDto oldActivity;
    private ActivityDto newActivity;
}
