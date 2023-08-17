# java-filmorate
>[ER-диаграмма](https://github.com/m3Rzy/java-filmorate/blob/add-database/ER-диаграмма.png)
# Пример запросов для основных операций:
```sql
-- создание таблицы `films`
CREATE TABLE IF NOT EXISTS films ()
```
```sql
-- список фильмов
SELECT * FROM films
```
```sql
-- добавление лайка на фильм под id 1 пользователем id 2
INSERT INTO likes (film_id, user_id)
    VALUES(1, 2)
```
```sql
-- удаление лайка на фильм под id 1 пользователем id 2
DELETE FROM likes
WHERE film_id = 1 AND user_id = 2
```
```sql
-- поиск фильма по id 1
SELECT *
    FROM films
WHERE film_id = 1
```
```sql
-- 5 популярных фильмов
SELECT *, COUNT(l.film_id) as count
    FROM films
LEFT JOIN likes l ON films.film_id=l.film_id
GROUP BY films.film_id
ORDER BY count DESC
LIMIT 5
```
