insert into users (login, password, role, status)
values
 ('mail1@mail.ru', 'password1', 'USER', 'ACTIVE'),
 ('mail2@mail.ru', 'password2', 'USER', 'ACTIVE'),
 ('mail3@mail.ru', 'password3', 'USER', 'ACTIVE');


insert into articles (user_id, title, body, status, description)
values
(1, 'test title 1', 'test body 1','CREATED', 'test description'),
(2, 'test title 2', 'test body 2','CREATED', 'test description'),
(2, 'test title 2', 'test body 2','CREATED', 'test description'),
(3, 'test title 3', 'test body 3','CREATED', 'test description'),
(3, 'test title 3', 'test body 3','CREATED', 'test description'),
(3, 'test title 3', 'test body 3','CREATED', 'test description');

insert into tags (name)
values
('software'),
('docker'),
('spring boot'),
('maven'),
('gradle'),
('spring'),
('database'),
('ACID'),
('transactional'),
('DAO');
