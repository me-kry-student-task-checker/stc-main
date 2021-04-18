package hu.me.iit.malus.thesis.feedback.client;

import hu.me.iit.malus.thesis.feedback.client.dto.ActivitySaveDto;
import hu.me.iit.malus.thesis.feedback.client.dto.ActivityTransactionDto;
import hu.me.iit.malus.thesis.feedback.client.dto.enums.ActivityType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient("user-service")
public interface UserClient {

    @PostMapping("/api/user/saveLastActivity")
    ActivityTransactionDto saveLastActivity(@RequestBody ActivitySaveDto dto);

    @GetMapping("/api/user/notificationPreferences")
    Map<ActivityType, Boolean> getNotificationPreferences();

    @PostMapping("/api/user/rollBackActivity")
    void rollBackActivity(@RequestBody ActivityTransactionDto dto);
}
