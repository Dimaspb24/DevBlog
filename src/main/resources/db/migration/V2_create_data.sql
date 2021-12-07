insert into users (login, password, role, status)
values
 ('mail1@mail.ru', '$2y$04$EznSb6sGOJBU/Dt6KF05F.Ou4UJSGuyo9TGVZEcNesB14SkCAt9ei', 'USER', 'ACTIVE'),
 ('mail2@mail.ru', '$2y$04$vLDO99YB0Za5DSFQFelkieEeC9KkIflNU2mkd2dPYoIaJSRcKXCv2', 'USER', 'ACTIVE'),
 ('mail3@mail.ru', '$2y$04$9rQ9Vw9jDf3nyZUOUicnxecN4TqmLaVggLNI00gjjmOSxk109ZiQe', 'USER', 'ACTIVE');


insert into posts (user_id, title, body, status, description)
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
