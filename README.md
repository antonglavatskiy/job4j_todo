# job4j_todo

+ [О проекте](#Описание-проекта)
+ [Стек технологий](#Для-реализации-проекта-используются)
+ [Требования к окружению](#Требования-к-окружению)
+ [Запуск проекта](#Запуск-проекта)
+ [Взаимодействие с приложением](#Взаимодействие-с-приложением)
+ [Контакты](#Контакты)

## Описание проекта

**Приложение TODO список**

</br>Отображает список всех заданий, позволяет *добавлять* задания, *удалять* и *редактировать*.
</br>Каждое задание имеет *описание*, *дату создания* и *статус выполнения*.

## Для реализации проекта используются

+ **Java 17**
+ **Spring Boot 2.7.6**
+ **Hibernate 5.6.11**
+ **Lombok 1.18.22**
+ **Checkstyle 10.3.1**
+ **HTML 5**
+ **Thymeleaf 3.0.15**
+ **Bootstrap 5.2.3**
+ **Postgresql 14**
+ **Liquibase 4.15.0**
+ **H2 2.1.214**
+ **Junit5**
+ **Mockito 3.5.13**

## Требования к окружению

+ **Java 17**
+ **Maven 3.8.7**
+ **Postgresql 14**

## Запуск проекта

+ **Скачайте проект**
  </br><a href="https://github.com/antonglavatskiy/job4j_todo/archive/refs/heads/main.zip">job4j_todo</a>
+ **Создайте базу данных todo_db**
  </br>*username=postgres; password=password*
``` shell 
create database todo_db;
```
+ **Выполните скрипт 001_ddl_create_tasks_table.sql из директории:**
  </br>*job4j_todo-main/db/scripts*
+ **В терминале перейдите в проект**
+ **Запустите проект**
``` shell 
mvn spring-boot:run
```
+ **В браузере перейдите на страницу**
  </br><a href="http://localhost:8080/index" target="_blank">localhost:8080/index</a>

## Взаимодействие с приложением

Главная страница. Общая информация о ресурсе
![Главная страница](images/01.png)

Список заданий. Страница со списком всех заданий
![Список заданий](images/02.png)

Список заданий. Страница со списком новых заданий
![Список заданий Новые](images/03.png)

Список заданий. Страница со списком выполненных заданий
![Список заданий Выполненные](images/04.png)

Добавление задания. Страница добавления нового задания
![Добавление задания](images/05.png)

Задание. Страница с подробным описанием задания
![Задание](images/06.png)

Редактирование. Страница с редактированием задания
![Редактирование](images/07.png)

## Контакты

+ <a href="https://t.me/GlaAnton">Telegram</a>

***