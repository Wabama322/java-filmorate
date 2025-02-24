SELECT fg.film_id, g.*
FROM FILM_GENRES fg
JOIN GENRES g ON fg.genre_id = g.genre_id
WHERE fg.film_id IN (:filmIds);