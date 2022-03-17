insert into users (id, login, password, role, enabled, firstname, lastname, nickname, phone, photo, info)
values ('1', 'mail1@mail.ru', '$2a$12$EIigg.G180RiQzEoIM/DGepBGOE2dOsnJIsJ4Oy600A91Raa0Us5O', 'USER', true, 'Иосиф',
        'Сталин', 'Вождь', '89999999999',
        'https://histrf.ru/images/biographies/05/KuQYEI2HeM9mzCXevgKdebKS7FjiY87B1sm59t3t.jpg', 'Я Сталин'),
       ('2', 'mail2@mail.ru', '$2a$12$EIigg.G180RiQzEoIM/DGepBGOE2dOsnJIsJ4Oy600A91Raa0Us5O', 'USER', true, 'Дмитрий',
        'Смирнов', 'Чел', '89878789879',
        'https://histrf.ru/images/biographies/05/KuQYEI2HeM9mzCXevgKdebKS7FjiY87B1sm59t3t.jpg', 'Я Дмитрий'),
       ('3', 'mail3@mail.ru', '$2a$12$EIigg.G180RiQzEoIM/DGepBGOE2dOsnJIsJ4Oy600A91Raa0Us5O', 'USER', true, 'Алексей',
        'Афанасьев', 'Шрэк', '85555555555',
        'https://histrf.ru/images/biographies/05/KuQYEI2HeM9mzCXevgKdebKS7FjiY87B1sm59t3t.jpg', 'Я Алексей'),
       ('4', 'mail4@mail.ru', '$2a$12$EIigg.G180RiQzEoIM/DGepBGOE2dOsnJIsJ4Oy600A91Raa0Us5O', 'USER', true, 'Семён',
        'Антуфьев', 'Чувак', '87536584648',
        'https://histrf.ru/images/biographies/05/KuQYEI2HeM9mzCXevgKdebKS7FjiY87B1sm59t3t.jpg', 'Я Семён'),
       ('5', 'mail5@mail.ru', '$2a$12$EIigg.G180RiQzEoIM/DGepBGOE2dOsnJIsJ4Oy600A91Raa0Us5O', 'USER', true, 'Даниил',
        'Беседин', 'Frontend master', '87563648576',
        'https://histrf.ru/images/biographies/05/KuQYEI2HeM9mzCXevgKdebKS7FjiY87B1sm59t3t.jpg', 'Я Даниил'),
       ('6', 'mail6@mail.ru', '$2a$12$EIigg.G180RiQzEoIM/DGepBGOE2dOsnJIsJ4Oy600A91Raa0Us5O', 'USER', true, 'Иисус',
        'Христос', 'Святой', '886759987675',
        'https://ichef.bbci.co.uk/news/624/amz/worldservice/live/assets/images/2015/12/24/151224130920_jesus_1_624x485_thinkstock_nocredit.jpg',
        'Я Иисус');
insert into users (id, login, password, role, enabled)
values ('7', 'mail7@mail.ru', '$2a$12$EIigg.G180RiQzEoIM/DGepBGOE2dOsnJIsJ4Oy600A91Raa0Us5O', 'USER', true),
       ('8', 'mail8@mail.ru', '$2a$12$EIigg.G180RiQzEoIM/DGepBGOE2dOsnJIsJ4Oy600A91Raa0Us5O', 'USER', true),
       ('9', 'mail9@mail.ru', '$2a$12$EIigg.G180RiQzEoIM/DGepBGOE2dOsnJIsJ4Oy600A91Raa0Us5O', 'USER', true),
       ('10', 'mail10@mail.ru', '$2a$12$EIigg.G180RiQzEoIM/DGepBGOE2dOsnJIsJ4Oy600A91Raa0Us5O', 'USER', false);

insert into subscribers (user_id, subscriber_id)
values (1, 2),
       (1, 3),
       (1, 5),
       (3, 5),
       (3, 8),
       (3, 9),
       (3, 4),
       (4, 6),
       (4, 8),
       (4, 9),
       (4, 5),
       (5, 7),
       (5, 6),
       (6, 2),
       (6, 1),
       (7, 6),
       (7, 5),
       (8, 1),
       (8, 2),
       (8, 3),
       (8, 4),
       (8, 5),
       (8, 6),
       (8, 7),
       (8, 9),
       (9, 2),
       (9, 8),
       (9, 5);

insert into articles (id, user_id, title, body, status, description, publication_date)
values (1, 1, 'Spring Security with JWT', 'Some body', 'PUBLISHED', 'The Spring Security framework is ... ',
        timestamp '2022-01-30 10:10:35'),
       (2, 2, 'Spring Security', 'test body 1', 'PUBLISHED', 'In Spring Security ...', timestamp '2022-01-30 10:17:35'),
       (3, 2, 'Title 2.2', 'test body 2', 'CREATED', 'description', null),
       (4, 3, 'Title 3.1', 'test body 1', 'CREATED', 'description', null),
       (5, 3, 'Title 3.2', 'test body 2', 'CREATED', 'description', null),
       (6, 3, 'Title 3.3', 'test body 3', 'PUBLISHED', 'description', timestamp '2022-01-30 10:08:35'),
       (7, 4, 'Title 4.1', 'test body 1', 'CREATED', 'description', null),
       (8, 4, 'Title 4.2', 'test body 2', 'PUBLISHED', 'description', timestamp '2021-12-30 12:08:35'),
       (9, 5, 'Title 5.1', 'test body 1', 'PUBLISHED', 'description', timestamp '2022-01-05 10:30:35'),
       (10, 6, 'Title 6.1', 'test body 1', 'CREATED', 'description', null),
       (11, 6, 'Title 6.2', 'test body 2', 'CREATED', 'description', null),
       (12, 6, 'Title 6.3', 'test body 3', 'PUBLISHED', 'description', timestamp '2022-01-15 15:08:35');
insert into articles (id, user_id, title, body, status, description, enabled, publication_date)
values (13, 6, 'Title 6.4', 'test body 4', 'CREATED', 'description', false, null),
       (14, 7, 'Title 7.1', 'test body 1', 'PUBLISHED', 'description', false, timestamp '2021-11-25 10:08:35'),
       (15, 7, 'Title 7.2', 'test body 2', 'PUBLISHED', 'description', false, timestamp '2022-01-21 23:08:35'),
       (16, 9, 'Title 9.1', 'test body 1', 'PUBLISHED', 'description', false, timestamp '2022-01-24 11:08:35'),
       (17, 9, 'Title 9.2', 'test body 2', 'CREATED', 'description', true, null);
-- select setval('articles_id_seq', (select max(id) from articles));

insert into users_articles (id, user_id, article_id, rating, bookmark_type)
values (1, '1', 8, 8, 'FAVORITE'),
       (2, '1', 7, 6, 'BOOKMARK'),
       (3, '1', 5, 5, 'BOOKMARK'),
       (4, '2', 1, 2, 'FAVORITE'),
       (5, '2', 5, 10, 'BOOKMARK'),
       (6, '3', 2, 8, 'FAVORITE'),
       (7, '4', 11, 9, 'BOOKMARK'),
       (8, '4', 12, 7, 'BOOKMARK'),
       (9, '4', 3, 6, 'FAVORITE');
insert into users_articles (id, user_id, article_id, rating)
values (10, '1', 9, 3),
       (11, '3', 3, 4),
       (12, '5', 5, 5),
       (13, '6', 6, 6),
       (14, '7', 5, 7),
       (15, '7', 4, 8),
       (16, '8', 3, 9);
insert into users_articles (id, user_id, article_id, bookmark_type)
values (17, '1', 10, 'FAVORITE'),
       (18, '2', 7, 'BOOKMARK'),
       (19, '2', 8, 'FAVORITE'),
       (20, '6', 2, 'FAVORITE'),
       (21, '8', 2, 'BOOKMARK'),
       (22, '9', 1, 'BOOKMARK'),
       (23, '9', 4, 'BOOKMARK'),
       (24, '9', 5, 'FAVORITE'),
       (25, '9', 2, 'FAVORITE');
-- select setval('users_articles_id_seq', (select max(id) from users_articles));

insert into tags (id, name)
values (1, 'software'),
       (2, 'docker'),
       (3, 'spring boot'),
       (4, 'maven'),
       (5, 'gradle'),
       (6, 'spring'),
       (7, 'database'),
       (8, 'ACID'),
       (9, 'transactional'),
       (10, 'DAO');
-- select setval('tags_id_seq', (select max(id) from tags));

insert into articles_tags (article_id, tag_id)
values (1, 1),
       (1, 2),
       (1, 8),
       (2, 2),
       (2, 10),
       (2, 7),
       (2, 3),
       (3, 1),
       (3, 6),
       (4, 2),
       (4, 6),
       (5, 4),
       (5, 5),
       (5, 10),
       (6, 8),
       (7, 4),
       (8, 8),
       (8, 10),
       (9, 2),
       (9, 3),
       (14, 4),
       (15, 4);
