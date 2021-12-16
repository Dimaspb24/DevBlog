insert into users (login, password, role, status)
values
 ('mail1@mail.ru', '$2y$04$GaHcCUVQtr9/g6Xwx3nymeg6LP719sAdEQ69E7TCJbcPY9ipqg882', 'USER', 'ACTIVE'),
 ('mail2@mail.ru', '$2y$04$SSAXMnznzwi7u.3HgzEnQuF6I.AX8VVn.5xWAuI2L3.Jd220JfbVi', 'USER', 'ACTIVE'),
 ('mail3@mail.ru', '$2y$04$Wt02LMPyc2gXIz8ieNPsYuKnWRcMF0fdUg.oAC0O730Mh030UibaW', 'USER', 'ACTIVE');


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
