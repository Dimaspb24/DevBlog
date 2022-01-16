insert into users (id, login, password, role, status)
values ('userTestId1', 'mail1@mail.ru', '$2y$04$GaHcCUVQtr9/g6Xwx3nymeg6LP719sAdEQ69E7TCJbcPY9ipqg882', 'USER', 'ACTIVE'),
       ('userTestId2', 'mail2@mail.ru', '$2y$04$SSAXMnznzwi7u.3HgzEnQuF6I.AX8VVn.5xWAuI2L3.Jd220JfbVi', 'USER', 'ACTIVE'),
       ('userTestId3', 'mail3@mail.ru', '$2y$04$Wt02LMPyc2gXIz8ieNPsYuKnWRcMF0fdUg.oAC0O730Mh030UibaW', 'USER', 'ACTIVE');


insert into articles (id, user_id, title, body, status, description)
values ('articleTestId1', 'userTestId1', 'test title 1', 'test body 1', 'CREATED', 'test description'),
       ('articleTestId2', 'userTestId2', 'test title 2', 'test body 2', 'CREATED', 'test description'),
       ('articleTestId3', 'userTestId2', 'test title 2', 'test body 2', 'CREATED', 'test description'),
       ('articleTestId4', 'userTestId3', 'test title 3', 'test body 3', 'CREATED', 'test description'),
       ('articleTestId5', 'userTestId3', 'test title 3', 'test body 3', 'CREATED', 'test description'),
       ('articleTestId6', 'userTestId3', 'test title 3', 'test body 3', 'CREATED', 'test description');

insert into tags (id, name)
values ('tagTestId1', 'software'),
       ('tagTestId2', 'docker'),
       ('tagTestId3', 'spring boot'),
       ('tagTestId4', 'maven'),
       ('tagTestId5', 'gradle'),
       ('tagTestId6', 'spring'),
       ('tagTestId7', 'database'),
       ('tagTestId8', 'ACID'),
       ('tagTestId9', 'transactional'),
       ('tagTestId10', 'DAO');
