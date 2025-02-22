# MyRKeeper

MyRKeeper - это система управления рестораном, разработанная для упрощения процесса приема заказов, их обработки и
управления персоналом. Этот проект предоставляет администратору и официантам удобный интерфейс для работы с заказами,
клиентами и меню.

## Возможности

- Авторизация и регистрация пользователей (администраторов и официантов)
- Просмотр и управление заказами
- Управление меню
- Установка скидок на заказы
- Создание и удаление заказов
- Разграничение доступа для администраторов и официантов

## Требования

- Java 17 
- Spring Boot 3.3.5 
- PostgreSQL
- H2 Database (для тестов)
- Flyway (для миграций) 
- Mockito (для модульного тестирования) 
- JUnit 5 (для тестирования) 
- Thymeleaf
- Docker 

## Установка и запуск

1. Склонируйте репозиторий:

    ```sh
    git clone https://github.com/AlexanderTarasevich/myR-Keeper.git
    cd myR-Keeper
    ```

2. Настройте базу данных PostgreSQL:

    - Создайте базу данных `myrkeeper`.
    - Обновите `application.properties` с вашими настройками подключения к базе данных.

   **Пример `application.properties`:**

    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/myrkeeper
    spring.datasource.username=your_username
    spring.datasource.password=your_password
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
   # Настройка Flyway 
   spring.flyway.enabled=true 
   spring.flyway.locations=classpath:db/migration
    ```

3. Установите зависимости и запустите приложение:

    ```sh
    mvn clean install
    mvn spring-boot:run
    ```

4. Приложение будет доступно по адресу `http://localhost:8080`.

## Структура проекта

- **controller**: содержит контроллеры для обработки запросов.
- **entity**: содержит сущности для базы данных.
- **repository**: содержит репозитории для взаимодействия с базой данных.
- **service**: содержит бизнес-логику приложения.
- **config**: содержит конфигурационные файлы приложения.
- **templates**: содержит шаблоны Thymeleaf для фронтенда.
- **db/migration**: содержит файлы миграций Flyway.

## Тестирование

Для тестирования используется база данных H2, а также Mockito и JUnit 5. Для запуска тестов используйте следующую
команду:

```sh
mvn test
```

# Примеры использования

## Авторизация и регистрация

- Перейдите на страницу http://localhost:8080/login для авторизации.

- Перейдите на страницу http://localhost:8080/register для регистрации нового пользователя.

## Управление заказами

- Администраторы могут просматривать все заказы на странице http://localhost:8080/admin/orders.

- Официанты могут просматривать свои заказы на странице http://localhost:8080/orders.

## Контакты

- Если у вас есть вопросы или предложения, пожалуйста, свяжитесь с нами по
  адресу www.linkedin.com/in/alexander-tarasevich-91314a2b7

**Этот проект является частью учебного задания и не предназначен для коммерческого использования.**