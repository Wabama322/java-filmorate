SELECT COUNT(*) AS status
FROM USER_FRIENDS
WHERE (user_id = ? AND friend_id = ?)
   OR (user_id = ? AND friend_id = ?)
HAVING COUNT(*) = 2;