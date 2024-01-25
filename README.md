# java-filmorate

Фильмов много — и с каждым годом становится всё больше. Чем их больше, тем больше разных оценок. Чем больше оценок, тем сложнее сделать выбор. Однако не время сдаваться! Перед вами - бэкенд для сервиса, который будет работать с фильмами и оценками пользователей, а также возвращать топ фильмов, рекомендованных к просмотру.

Функциональность приложения эквивалентна API, посредством которого происходит взаимодействие.
## Реализованные эндпоинты:

<details>
  <summary><h3>Пользователи</h3></summary>
  
* **POST** /users - создание пользователя
* **PUT** /users - редактирование пользователя
* **GET** /users - получение списка всех пользователей
* **GET** /users/{userId} - получение информации о пользователе по его id
* **PUT** /users/{id}/friends/{friendId} — добавление в друзья
* **DELETE** /users/{id}/friends/{friendId} — удаление из друзей
* **GET** /users/{id}/friends — возвращает список пользователей, являющихся его друзьями
* **GET** /users/{id}/friends/common/{otherId} — список друзей, общих с другим пользователем

</details>
<details>
  <summary><h3>Фильмы</h3></summary>
  
* **POST** /films - создание фильма
* **PUT** /films - редактирование фильма
* **GET** /films - получение списка всех фильмов
* **GET** /films/{filmId} - получение информации о фильме по его id
* **PUT** /films/{id}/like/{userId} — пользователь ставит лайк фильму
* **DELETE** /films/{id}/like/{userId} — пользователь удаляет лайк
* **GET** /films/popular?count={count} — возвращает список из первых count фильмов по количеству лайков. Если значение параметра count не задано, возвращает первые 10

</details>
<details>
  <summary><h3>Жанры</h3></summary>
  
* **GET** /genres - получение списка всех жанров
* **GET** /genres/{id} - получение информации о жанре по его id

</details>
<details>
  <summary><h3>Рейтинги</h3></summary>
  
* **GET** /mpa - получение списка всех рейтингов
* **GET** /mpa/{id} - получение информации о рейтинге по его id

</details>

## Валидация

Входные данные, которые приходят в запросе на добавление нового фильма или пользователя, проходят проверку. Эти данные должны соответствовать следующим критериям:

<details>
  <summary><h3>Для пользователей:</h3></summary>
  
* электронная почта не может быть пустой и должна содержать символ @;
* логин не может быть пустым и содержать пробелы;
* имя для отображения может быть пустым — в таком случае будет использован логин;
* дата рождения не может быть в будущем.

</details>
<details>
  <summary><h3>Для фильмов:</h3></summary>
  
* название не может быть пустым;
* максимальная длина описания — 200 символов;
* дата релиза — не раньше 28 декабря 1895 года;
* продолжительность фильма должна быть положительной;
* рейтинг не может быть null.

</details>

<details>
  <summary><h3>Для пользователей:</h3></summary>
  
* создание пользователя
```SQL
INSERT INTO users (email, login, name, birthday)
VALUES ( ?, ?, ?, ? );
```
* редактирование пользователя
```SQL
UPDATE users
SET email = ?,
    login = ?,
    name = ?,
    birthday = ?
WHERE user_id = ?
```
* получение списка всех пользователей
```SQL
SELECT *
FROM users
```
* получение информации о пользователе по его `id`
```SQL
SELECT *
FROM users
WHERE user_id = ?
```
* добавление в друзья
```SQL
INSERT IGNORE INTO user_friends (user_id, friend_id)
VALUES (?, ?)
```
* удаление из друзей
```SQL
DELETE
FROM user_friends
WHERE user_id = ? AND friend_id = ?
```
* возвращает список пользователей, являющихся его друзьями
```SQL
SELECT users.*
FROM users
INNER JOIN user_friends ON users.user_id = user_friends.friend_id
WHERE user_friends.user_id = ?
```
* список друзей, общих с другим пользователем
```SQL
SELECT users.*
FROM users
INNER JOIN user_friends ON users.user_id = user_friends.friend_id
WHERE user_friends.user_id = ?

INTERSECT

SELECT users.*
FROM users
INNER JOIN user_friends ON users.user_id = user_friends.friend_id
WHERE user_friends.user_id = ?
```

</details>
<details>
  <summary><h3>Для фильмов:</h3></summary>

* создание фильма
```SQL
INSERT INTO films (name, description, release_date, duration_in_minutes, mpa_rating_id)
VALUES (?, ?, ?, ?, ?)
```
* редактирование фильма
```SQL
UPDATE films
SET name = ?,
    description = ?,
    release_date = ?,
    duration_in_minutes = ?,
    mpa_rating_id = ?
WHERE film_id = ?
```
* получение списка всех фильмов
```SQL
SELECT films.*, mpa_rating.mpa_name, COUNT(film_likes.user_id) AS rate
FROM films
LEFT JOIN mpa_rating ON films.mpa_rating_id = mpa_rating.mpa_rating_id
LEFT JOIN film_likes ON films.film_id = film_likes.film_id
GROUP BY films.film_id
ORDER BY films.film_id
```
* получение информации о фильме по его `id`
```SQL
SELECT films.*, mpa_rating.mpa_name, COUNT(film_likes.user_id) AS rate
FROM films
LEFT JOIN mpa_rating ON films.mpa_rating_id = mpa_rating.mpa_rating_id
LEFT JOIN film_likes ON films.film_id = film_likes.film_id
WHERE films.film_id = ?
GROUP BY films.film_id
```
* пользователь ставит лайк фильму
```SQL
INSERT IGNORE INTO film_likes (film_id, user_id)
VALUES (?, ?)
```
* пользователь удаляет лайк
```SQL
DELETE
FROM film_likes
WHERE film_id = ? AND user_id = ?
```
* возвращает список из первых `count` фильмов по количеству лайков
```SQL
SELECT films.*, mpa_rating.mpa_name, COUNT(film_likes.user_id) AS rate
FROM films
         LEFT JOIN mpa_rating ON films.mpa_rating_id = mpa_rating.mpa_rating_id
         LEFT JOIN film_likes ON films.film_id = film_likes.film_id
GROUP BY films.film_id
ORDER BY rate DESC, films.film_id
LIMIT ?
```

</details>
<details>
  <summary><h3>Для жанров:</h3></summary>
  
* получение списка всех жанров
```SQL
SELECT *
FROM genres
ORDER BY genre_id
```
* получение информации о жанре по его `id`
```SQL
SELECT *
FROM genres
WHERE genre_id = ?
```
  
</details>
<details>
  <summary><h3>Для рейтингов:</h3></summary>
    
* получение списка всех рейтингов
```SQL
SELECT *
FROM mpa_rating
ORDER BY mpa_rating_id
```
* получение информации о рейтинге по его `id`  
```SQL
SELECT *
FROM mpa_rating
WHERE mpa_rating_id = ?
```
  
</details>

>[ER-диаграмма](https://github.com/m3Rzy/java-filmorate/blob/add-database/ER-диаграмма.png)
