## Сервис авторизации на Spring Boot

REST API-сервис для регистрации, аутентификации и управления пользователями, реализующий JWT-базированную авторизацию с ролями (`ROLE_GUEST`, `ROLE_PREMIUM_USER`, `ROLE_ADMIN`). Проект использует PostgreSQL, Liquibase и Spring Security.

---

## Возможности

* Регистрация и логин пользователей
* JWT Access и Refresh токены
* Отзыв (logout) refresh-токена (через blacklist)
* Защищённые эндпоинты по ролям:

  * `/api/guest` — только для `ROLE_GUEST`
  * `/api/premium_user` — только для `ROLE_PREMIUM_USER`
  * `/api/admin` — только для `ROLE_ADMIN`
* Обработка ошибок в едином формате
* Swagger UI с авторизацией через JWT

---



## Установка и запуск

### 🔧 1. Клонировать репозиторий

```bash
git clone https://github.com/TusyaSonne/AuthServiceT1.git
cd AuthServiceT1
```

### 2. Запустить через Docker Compose

```bash
docker-compose up --build
```

После запуска:

* Backend доступен на: `http://localhost:8080`
* Swagger UI: `http://localhost:8080/swagger-ui/index.html`

---

## Формат JWT-токенов

* Access Token: действителен ограниченное время (15 мин)
* Refresh Token: используется для обновления access токена (действителен 7 дней)
* После **logout** refresh-токен отправляется в блеклист в БД (и больше для получения access-токена без аутентификайции его использовать нельзя)

---

## Swagger-документация

Доступна по адресу:

После запуска перейдите на **http://localhost:8080/swagger-ui.html**, чтобы увидеть описание всех доступных эндпоинтов.

* Поддерживает авторизацию через Bearer-токен (`Authorize`)
* Документированы все основные запросы: `/register`, `/login`, `/refresh`, `/logout`

---
## Аутентификация через JWT

Для работы с защищёнными эндпоинтами необходимо **авторизоваться с помощью JWT-токена**.

1. Сначала необходимо **зарегистрироваться** через эндпоинт `/api/auth/register`, указав никнейм пользователя, почту, пароль и роли (если пользователь не укажет роли по умолчанию будет присвоена роль `ROLE_GUEST`).

Пример:
  ```json
  {
    "username": "john doe",
    "email": "john@example.com",
    "password": "123456",
    "roles": [
      "ROLE_GUEST",
      "ROLE_ADMIN"
    ]
  }
  ```
3. В ответе на успешную регистрацию вы получите **JWT-токены** - `access-токен` и `refresh-токен`.
4. Для доступа к защищённым ресурсам вставьте полученный access-токен в окно **Authorize** (в правом верхнем углу Swagger UI).
> Вставлять токен необходимо в формате:  
> ```text
> eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
> ```



---

## Миграции Liquibase

Liquibase использует YAML-файлы и запускается автоматически при старте приложения.

* Главный файл: `db/changelog/master-changelog.yml`
* Таблицы: `users`, `roles`, `user_roles`, `revoked_tokens`
* Предопределённые роли вставляются через `INSERT`-changelog
