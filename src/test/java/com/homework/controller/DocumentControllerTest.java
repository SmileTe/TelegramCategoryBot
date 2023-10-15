package com.homework.controller;

import com.homework.model.Document;
import com.homework.repository.DocumentRepository;
import com.homework.service.DocumentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DocumentController.class)
class DocumentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private DocumentRepository documentRepository;

    @SpyBean
    private DocumentService documentService;

    @InjectMocks
    private DocumentController documentController;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createDocument() throws Exception{
        Long id = 1L;
        String filePath = "test";
        long fileSize = 1L;
        String mediaType = ".xls";

        JSONObject documentObject = new JSONObject();
        //documentObject.put("id", id);
        documentObject.put("filePath", filePath);
        documentObject.put("fileSize", fileSize);
        documentObject.put("mediaType", mediaType);


        Document document = new Document();
       // document.setId(id);
        document.setFilePath(filePath);
        document.setFileSize(fileSize);
        document.setMediaType(mediaType);

        MockMultipartFile file
                = new MockMultipartFile(
                "test",
                "test.xls",
                MediaType.ALL_VALUE,
                DocumentControllerTest.class.getResourceAsStream("test.xls")
        );


        document.setData(file.getBytes());
        documentObject.put("data", document.getData());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/documents")
                                .content(documentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
              //  .andExpect(jsonPath("$.filePath").value(filePath))
//                .andExpect(jsonPath("$.fileSize").value(fileSize))
//                .andExpect(jsonPath("$.mediaType").value(mediaType))
                ;


    }
}