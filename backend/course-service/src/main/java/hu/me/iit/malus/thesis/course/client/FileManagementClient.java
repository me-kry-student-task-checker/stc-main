package hu.me.iit.malus.thesis.course.client;

import hu.me.iit.malus.thesis.dto.File;
import hu.me.iit.malus.thesis.dto.ServiceType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Set;

@FeignClient("filemanagement-service")
public interface FileManagementClient {

    @GetMapping("/api/filemanagement/download/getByTagId/{serviceType}/{tagId}")
    Set<File> getAllFilesByTagId(@PathVariable ServiceType serviceType, @PathVariable Long tagId);

    @PostMapping("/api/filemanagement/prepare/remove/by/service/{service}/and/{tagIds}")
    String prepareRemoveFilesByServiceTypeAndTagIds(@PathVariable ServiceType service, @PathVariable List<Long> tagIds);

    @PostMapping("/api/filemanagement/commit/remove/{fileTransactionKey}")
    void commitRemoveFilesByServiceTypeAndTagIds(@PathVariable String fileTransactionKey);

    @PostMapping("/api/filemanagement/rollback/remove/{fileTransactionKey}")
    void rollbackRemoveFilesByServiceTypeAndTagIds(@PathVariable String fileTransactionKey);
}
