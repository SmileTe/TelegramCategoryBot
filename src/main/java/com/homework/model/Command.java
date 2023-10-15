package com.homework.model;

public enum Command {
    viewTree("/viewTree - Вывести дерево категорий"),
    addElement("/addElement <родительский элемент>/<дочерний элемент> - Добавить элемент"),
    removeElement("/removeElement <название элемента> - Удалить элемент"),
    help("/help - Вывести список команд"),
    download("/download - Скачать Excel документ с деревом категорий"),
    upload("/upload - Принять Excel документ с деревом категорий");

    private final String title;

    Command(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return  title ;
    }
}


