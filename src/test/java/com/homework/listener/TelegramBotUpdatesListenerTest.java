package com.homework.listener;

import com.homework.model.Category;
import com.homework.repository.CategoryRepository;
import com.homework.repository.DocumentRepository;
import com.homework.service.CategoryService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.atLeastOnce;
@ExtendWith(MockitoExtension.class)
class TelegramBotUpdatesListenerTest {
    @Mock
    private TelegramBot telegramBot;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryService categoryService;
    @Mock
    private DocumentRepository documentRepository;

    @InjectMocks
    private TelegramBotUpdatesListener telegramBotUpdatesListener;


    @Test
    void addElement() throws  Exception{
//        Category category = new Category("test");
//
//        TelegramBot mockedTelegramBot = mock(TelegramBot.class);
//        CategoryService mockedCategoryService = mock(CategoryService.class);
//        CategoryRepository mockedcategoryRepository1 = mock(CategoryService.class);
//        CategoryService mockedCategoryService = mock(CategoryService.class);
//      ///  doNothing().when(mockedCategoryService).addElement(category.getName());
//        TelegramBotUpdatesListener telegramBotUpdatesListener = new TelegramBotUpdatesListener();
//        telegramBotUpdatesListener.addElement(category.getName(), 1L);

    }

    @Test
    void viewTree() {
    }

    @Test
    void removeElement() {
    }

    @Test
    void help() {
    }

    @Test
    void downloadExcel() {
    }
}