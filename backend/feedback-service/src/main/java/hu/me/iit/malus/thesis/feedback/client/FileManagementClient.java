package hu.me.iit.malus.thesis.feedback.client;

import hu.me.iit.malus.thesis.feedback.client.dto.File;
import hu.me.iit.malus.thesis.feedback.client.dto.Service;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Set;

@FeignClient("filemanagement-service")
public interface FileManagementClient {
    @GetMapping("/api/filemanagement/download/getByTagId/{service}/{tagId}")
    ResponseEntity<Set<File>> getAllFilesByTagId(@PathVariable Service service, @PathVariable Long tagId);
}
