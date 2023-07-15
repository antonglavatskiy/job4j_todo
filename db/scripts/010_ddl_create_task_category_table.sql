CREATE TABLE task_category (
   id serial PRIMARY KEY,
   task_id int REFERENCES tasks(id) not null,
   category_id int REFERENCES categories(id) not null
);
