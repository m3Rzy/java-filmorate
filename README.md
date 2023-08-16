# java-filmorate
>['Filmorate' - ER-диаграмма](https://github.com/m3Rzy/java-filmorate/blob/main/Filmorate_ER-диаграмма.png)
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
-- названия 10 популярных фильмов
SELECT name
    FROM film
WHERE film_id IN (SELECT film_id, COUNT(user_id) AS likes_count
    FROM likes
GROUP BY film_id
ORDER BY likes_count DESC
LIMIT (10))
```

