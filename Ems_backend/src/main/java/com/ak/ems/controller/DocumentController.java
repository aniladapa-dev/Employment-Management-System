package com.ak.ems.controller;
import com.ak.ems.dto.DocumentDto;
import com.ak.ems.response.ApiResponse;
import com.ak.ems.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private static final Logger logger = LoggerFactory.getLogger(DocumentController.class);

    private final DocumentService documentService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<DocumentDto>> uploadDocument(
            @RequestParam("employeeId") Long employeeId,
            @RequestParam("file") MultipartFile file) {
        logger.info("RECEIVED upload request for employeeId: {}", employeeId);
        DocumentDto doc = documentService.uploadDocument(employeeId, file);
        return ResponseEntity.ok(ApiResponse.success("Document uploaded successfully", doc));
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<ApiResponse<List<DocumentDto>>> getDocumentsByEmployee(@PathVariable("employeeId") Long employeeId) {
        List<DocumentDto> docs = documentService.getDocumentsByEmployee(employeeId);
        return ResponseEntity.ok(ApiResponse.success("Documents fetched successfully", docs));
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Void> downloadDocument(@PathVariable Long id) {
    String fileUrl = documentService.getDownloadUrl(id);

        return ResponseEntity
            .status(302)
            .header(HttpHeaders.LOCATION, fileUrl)
            .build();
    }
}
