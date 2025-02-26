SELECT f.*, r.rating_id, r.rating_name, COUNT(fl.user_id) AS rate
FROM FILM f
LEFT JOIN MPA_RATING r ON f.rating_MPA = r.rating_id
LEFT JOIN FILM_LIKES fl ON f.film_id = fl.film_id
GROUP BY f.film_id, r.rating_id, r.rating_name
ORDER BY rate DESC
LIMIT ?;
