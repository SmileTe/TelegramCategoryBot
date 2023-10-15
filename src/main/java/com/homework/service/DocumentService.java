package com.homework.service;

import com.homework.model.Document;
import com.homework.repository.CategoryRepository;
import com.homework.repository.DocumentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DocumentService {
    private final Logger logger = LoggerFactory.getLogger(DocumentService.class);

    private final DocumentRepository documentRepository;

    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public Document createDocument(Document document) {
        logger.debug("Creating a new Document:{}", document);
        final var save = documentRepository.save(document);
        logger.debug("A new Document{}", save);
        return save;
    }


    public Document createDocument(String filePath, long fileSize, String mediaType, byte[] data) {
       Document document = new Document(filePath, fileSize, mediaType,data);

        logger.debug("Creating a new Document:{}", document);
        final var save = documentRepository.save(document);
        logger.debug("A new Document{}", save);
        return save;
    }


}
