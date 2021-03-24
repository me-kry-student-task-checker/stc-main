package hu.me.iit.malus.thesis.feedback.client.dto;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("user-service")
public interface UserClient {

    @PostMapping("/api/user/saveLastActivity")
    void saveLastActivity(@RequestBody ActivitySaveDto dto);
}
