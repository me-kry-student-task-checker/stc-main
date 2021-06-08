package hu.me.iit.malus.thesis.feedback.client;

import hu.me.iit.malus.thesis.dto.File;
import hu.me.iit.malus.thesis.dto.Service;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Set;

@FeignClient("filemanagement-service")

public interface FileManagementClient {
    @GetMapping("/api/filemanagement/download/getByTagId/{service}/{tagId}")
    Set<File> getAllFilesByTagId(@PathVariable Service service, @PathVariable Long tagId);

    @DeleteMapping("/api/filemanagement/deleteAll/{service}/{tagId}")
    void removeFilesByServiceAndTagId(@PathVariable Service service, @PathVariable Long tagId);
}
