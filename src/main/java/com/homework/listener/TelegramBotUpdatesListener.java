package com.homework.listener;

import com.homework.model.Category;
import com.homework.model.Command;
import com.homework.repository.CategoryRepository;
import com.homework.repository.DocumentRepository;
import com.homework.service.CategoryService;
import com.homework.service.DocumentService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendDocument;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetFileResponse;
import com.pengrad.telegrambot.response.SendResponse;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private DocumentService documentService;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    public TelegramBotUpdatesListener(TelegramBot telegramBot, CategoryRepository categoryRepository, CategoryService categoryService, DocumentRepository documentRepository, DocumentService documentService) {
        this.telegramBot = telegramBot;
        this.categoryRepository = categoryRepository;
        this.categoryService = categoryService;
        this.documentRepository = documentRepository;
        this.documentService = documentService;
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            if (update.callbackQuery() != null) {
                return;
            }
          //пришел текст
            if (update.message().text()!=null){
                String textUpdate = update.message().text();
                long chatIdPerson = update.message().chat().id();
                 processCommands(textUpdate, chatIdPerson);
            }
            //пришел текст с документом
            if(update.message().caption()!=null){
                String captionUpdate = update.message().caption();
                long chatIdPerson = update.message().chat().id();
                try {
                    processDocuments(captionUpdate, chatIdPerson, update.message());
                }
                catch (Exception e ) {
                }

            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    //upload - загрузка документа
    public void processDocuments(String captionUpdate, long chatIdPerson, Message message) throws IOException {
     if (captionUpdate.contains(Command.upload.name())) {
         Document documentBot =  message.document();
         GetFileResponse response = telegramBot.execute(new GetFile(documentBot.fileId()));
         String path = telegramBot.getFullFilePath(response.file());
         byte[] data = telegramBot.getFileContent(response.file());
         documentService.createDocument(path,documentBot.fileSize(), documentBot.mimeType(),data );
        }
    }

    public void processCommands(String textUpdate, long chatIdPerson) {
        //команда addElement
        if (textUpdate.contains(Command.addElement.name())) {
            addElement(textUpdate, chatIdPerson);
        }
        //команда viewTree
        else if (textUpdate.contains(Command.viewTree.name())){
            viewTree(textUpdate, chatIdPerson);
        }
        //команда removeElement
        else if(textUpdate.contains(Command.removeElement.name())){
            removeElement(textUpdate, chatIdPerson);
          //команда help
        }
        else if (textUpdate.contains(Command.help.name())) {
            help(textUpdate, chatIdPerson);
        }
        else if (textUpdate.contains(Command.download.name())) {
            try {
                downloadExcel(chatIdPerson);
                }
            catch (Exception e){

            }
        }
    }


    public void addElement(String textUpdate,  long chatIdPerson){
        String text = textUpdate.replace("/" + Command.addElement.name() + " ", "");
        String[] array = text.split("/");
        if (array.length == 2) {
            Category parent = categoryRepository.getByName(array[0]);
            if(parent ==null){
                sendMessage(chatIdPerson, "Нет элемента  с именем - " +array[0] );
                return;
            }
            categoryService.addElement(array[1], array[0]);
        } else if (array.length == 1) {
            categoryService.addElement(array[0]);
        }
    }

    public void viewTree(String textUpdate,  long chatIdPerson){
        List<Category> categories = categoryRepository.getViewTree();
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer text = new StringBuffer();
        for (Category category:
                categories) {
            if(category.getParentName()==null){
                text = new StringBuffer();
            }
            stringBuffer.append(text + category.getName());
            stringBuffer.append("\n");
            text.append(" ");
        }
        sendMessage(chatIdPerson, stringBuffer.toString());
    }

    public void removeElement(String textUpdate,  long chatIdPerson){
        String text = textUpdate.replace("/" + Command.removeElement.name() + " ", "");


        Category element = categoryRepository.getByName(text);
        if(element == null){
            sendMessage(chatIdPerson, "Нет элемента  с именем - " + text);
            return;
        }
        categoryService.removeElement(text);
    }


    public void help(String textUpdate,  long chatIdPerson){
        Command[] commands = Command.values();
        StringBuffer stringBuffer = new StringBuffer();
        for (Command command :
                commands) {
            stringBuffer.append(command.toString() + "\n");
        }
        sendMessage(chatIdPerson, stringBuffer.toString());
    }

    public void downloadExcel( long chatIdPerson) throws IOException {

        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("Дерево категорий");

        List<Category> categories = categoryRepository.getViewTree();

        int rowCount = 0;
        Row row = sheet.createRow(rowCount);
        int cellCounter = 0;
        int num = 0;
        for (Category category:
                categories) {
            if( num!=0 && category.getParentName()==null){
                rowCount ++;
                 row = sheet.createRow(rowCount);
                cellCounter = 0;
            }
            row.createCell(cellCounter).setCellValue(category.getName());
            cellCounter++;
            num++;
        }

        java.io.File file = java.io.File.createTempFile("example", ".xlsx");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            wb.write(fos);
        }
        SendDocument sendDocument = new SendDocument(chatIdPerson,file);
        try {
            SendResponse execute = telegramBot.execute(sendDocument);
        } catch (Exception e) {
        }
    }

    public String getFileLink(String fileId) {
        GetFile getFile = new GetFile(fileId);
        GetFileResponse fileResponse = telegramBot.execute(getFile);
        File file = fileResponse.file();
        return telegramBot.getFullFilePath(file);
    }

    public void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage(chatId, text);
        telegramBot.execute(message);
    }

}
