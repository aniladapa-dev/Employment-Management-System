package com.ak.ems.service.impl;

import com.ak.ems.dto.DocumentDto;
import com.ak.ems.entity.Document;
import com.ak.ems.repository.DocumentRepository;
import com.ak.ems.service.DocumentService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final Cloudinary cloudinary;

    @Override
    public DocumentDto uploadDocument(Long employeeId, MultipartFile file) {
        try {
            // Upload to Cloudinary
            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap("resource_type", "auto")
            );

            String fileUrl = uploadResult.get("secure_url").toString();

            // Save metadata in DB
            Document document = new Document();
            document.setEmployeeId(employeeId);
            document.setFileName(file.getOriginalFilename());
            document.setFileType(file.getContentType());
            document.setFileUrl(fileUrl);
            document.setUploadedAt(LocalDateTime.now());

            Document saved = documentRepository.save(document);

            return mapToDto(saved);

        } catch (Exception e) {
            throw new RuntimeException("Cloudinary upload failed: " + e.getMessage());
        }
    }

    @Override
    public List<DocumentDto> getDocumentsByEmployee(Long employeeId) {
        return documentRepository.findByEmployeeId(employeeId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public DocumentDto getDocumentInfo(Long documentId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        return mapToDto(document);
    }

    //  Mapper
    private DocumentDto mapToDto(Document document) {
        return new DocumentDto(
                document.getId(),
                document.getEmployeeId(),
                document.getFileName(),
                document.getFileType(),
                document.getFileUrl(),
                document.getUploadedAt()
        );
    }

    @Override
    public String getDownloadUrl(Long documentId) {
    Document document = documentRepository.findById(documentId)
            .orElseThrow(() -> new RuntimeException("Document not found"));

    return document.getFileUrl(); // Cloudinary URL
}
}