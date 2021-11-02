package hu.me.iit.malus.thesis.feedback.client;

import hu.me.iit.malus.thesis.dto.File;
import hu.me.iit.malus.thesis.dto.ServiceType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Set;

@FeignClient("filemanagement-service")
public interface FileManagementClient {
    @GetMapping("/api/filemanagement/download/getByTagId/{serviceType}/{tagId}")
    Set<File> getAllFilesByTagId(@PathVariable ServiceType serviceType, @PathVariable Long tagId);

    @DeleteMapping("/api/filemanagement/deleteAll/{serviceType}/{tagId}")
    void removeFilesByServiceAndTagId(@PathVariable ServiceType serviceType, @PathVariable Long tagId);
}
