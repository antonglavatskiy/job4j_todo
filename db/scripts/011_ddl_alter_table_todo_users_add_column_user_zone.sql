ALTER TABLE todo_users
ADD COLUMN IF NOT EXISTS user_zone varchar
NOT NULL DEFAULT 'UTC';