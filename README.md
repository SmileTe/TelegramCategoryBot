# CategoryBot
Telegram-бот для управления деревом категорий

Основные функциональные возможности:

**Команда: /viewTree**

  Дерево должно отображаться в структурированном виде.
  
**Команда: /addElement <название элемента>**

  Этот элемент будет корневым, если у него нет родителя.
  Добавление дочернего элемента к существующему элементу.
  
**Команда: /addElement <родительский элемент> <дочерний элемент>**

  Если родительский элемент не существует, выводить соответствующее
  сообщение.
  
**Команда: /removeElement <название элемента>**

  При удалении родительского элемента, все дочерние элементы также
  должны быть удалены. Если элемент не найден, выводить
  соответствующее сообщение.
  
**Команда: /help**

  Выводит список всех доступных команд и краткое их описание.

**Команда: /download**

  Скачивает Excel документ с деревом категорий, формат на ваше
  усмотрение
  
**Команда: /upload**

  Принимает Excel документ с деревом категорий и сохраняет все
  элементы в базе данных
