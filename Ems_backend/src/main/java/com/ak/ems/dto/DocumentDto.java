package com.ak.ems.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDto {
    private Long id;
    private Long employeeId;
    private String fileName;
    private String fileType;
    private String fileUrl;   // main URL
    private LocalDateTime uploadedAt;
}
