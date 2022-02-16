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

insert into articles (user_id, title, body, status, description, publication_date)
values (1, 'Spring Security with JWT for REST API',
        'Spring is considered a trusted framework in the Java ecosystem and is widely used. It’s no longer valid to refer to Spring as a framework, as it’s more of an umbrella term that covers various frameworks. One of these frameworks is Spring Security, which is a powerful and customizable authentication and authorization framework. It is considered the de facto standard for securing Spring-based applications.

Despite its popularity, I must admit that when it comes to single-page applications, it’s not simple and straightforward to configure. I suspect the reason is that it started more as an MVC application-oriented framework, where webpage rendering happens on the server-side and communication is session-based.

If the back end is based on Java and Spring, it makes sense to use Spring Security for authentication/authorization and configure it for stateless communication. While there are a lot of articles explaining how this is done, for me, it was still frustrating to set it up for the first time, and I had to read and sum up information from multiple sources. That’s why I decided to write this article, where I will try to summarize and cover all the required subtle details and foibles you may encounter during the configuration process.',
        'PUBLISHED',
        'The Spring Security framework is the de facto industry standard when it comes to securing Spring-based apps, but it can be tricky to configure. In this article, Toptal Software Engineer Ioram Gordadze demonstrates how you can implement it without wasting too much time.',
        timestamp '2022-01-30 10:10:35'),
       (2, 'Spring Security: Check If a User Has a Role in Java',
        'test body 1',
        'PUBLISHED',
        'In Spring Security, sometimes it is necessary to check if an authenticated user has a specific role. This can be useful to enable or disable particular features in our applications.

In this tutorial, we''ll see various ways to check user roles in Java for Spring Security.',
        timestamp '2022-01-30 10:17:35'),
       (2, 'Title 2.2', 'test body 2', 'CREATED', 'description', null),
       (3, 'Title 3.1', 'test body 1', 'CREATED', 'description', null),
       (3, 'Title 3.2', 'test body 2', 'CREATED', 'description', null),
       (3, 'Title 3.3', 'test body 3', 'PUBLISHED', 'description', timestamp '2022-01-30 10:08:35'),
       (4, 'Title 4.1', 'test body 1', 'CREATED', 'description', null),
       (4, 'Title 4.2', 'test body 2', 'PUBLISHED', 'description', timestamp '2021-12-30 12:08:35'),
       (5, 'Title 5.1', 'test body 1', 'PUBLISHED', 'description', timestamp '2022-01-05 10:30:35'),
       (6, 'Title 6.1', 'test body 1', 'CREATED', 'description', null),
       (6, 'Title 6.2', 'test body 2', 'CREATED', 'description', null),
       (6, 'Title 6.3', 'test body 3', 'PUBLISHED', 'description', timestamp '2022-01-15 15:08:35');
insert into articles (user_id, title, body, status, description, enabled, publication_date)
values (6, 'Title 6.4', 'test body 4', 'CREATED', 'description', false, null),
       (7, 'Title 7.1', 'test body 1', 'PUBLISHED', 'description', false, timestamp '2021-11-25 10:08:35'),
       (7, 'Title 7.2', 'test body 2', 'PUBLISHED', 'description', false, timestamp '2022-01-21 23:08:35'),
       (9, 'Title 9.1', 'test body 1', 'PUBLISHED', 'description', false, timestamp '2022-01-24 11:08:35'),
       (9, 'Title 9.2', 'test body 2', 'CREATED', 'description', true, null);

insert into users_articles (user_id, article_id, rating, bookmark_type)
values ('1', 8, 8, 'FAVORITE'),
       ('1', 7, 6, 'BOOKMARK'),
       ('1', 5, 5, 'BOOKMARK'),
       ('2', 1, 2, 'FAVORITE'),
       ('2', 5, 10, 'BOOKMARK'),
       ('3', 2, 8, 'FAVORITE'),
       ('4', 11, 9, 'BOOKMARK'),
       ('4', 12, 7, 'BOOKMARK'),
       ('4', 3, 6, 'FAVORITE');
insert into users_articles (user_id, article_id, rating)
values ('1', 9, 3),
       ('3', 3, 4),
       ('5', 5, 5),
       ('6', 6, 6),
       ('7', 5, 7),
       ('7', 4, 8),
       ('8', 3, 9);
insert into users_articles (user_id, article_id, bookmark_type)
values ('1', 10, 'FAVORITE'),
       ('2', 7, 'BOOKMARK'),
       ('2', 8, 'FAVORITE'),
       ('6', 2, 'FAVORITE'),
       ('8', 2, 'BOOKMARK'),
       ('9', 1, 'BOOKMARK'),
       ('9', 4, 'BOOKMARK'),
       ('9', 5, 'FAVORITE'),
       ('9', 2, 'FAVORITE');

insert into tags (name)
values ('software'),
       ('docker'),
       ('spring boot'),
       ('maven'),
       ('gradle'),
       ('spring'),
       ('database'),
       ('ACID'),
       ('transactional'),
       ('DAO');

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
       (9, 3);