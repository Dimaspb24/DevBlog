insert into users (id, login, password, role, enabled)
values ('1', 'mail1@mail.ru', '$2a$12$UMR/uPkgKgh7bqWv.Mq2l.TNLahQLmb4zHAwmFmmNgdyZHNXciNhe', 'USER', true),
       ('2', 'mail2@mail.ru', '$2a$12$YJIjbu9U/Z4/qMuK1.OPquLc4M8U5v10iIRmQPpiD6JIyD3V/X8ia', 'USER', true),
       ('3', 'mail3@mail.ru', '$2a$12$C68.hcvWF6IGz12xmei2le39YzCZZWs93EqPyfedeVM1eIkR9U/22', 'USER', true),
       ('4', 'mail4@mail.ru', '$2a$12$OxKKjpzg2sa1uZwpTYX4b.gdpieXi/8tjWXeBqVe4B72vCJFiCdX2', 'USER', true),
       ('5', 'mail5@mail.ru', '$2a$12$VUthC/o/Iqk4hQ5med3yb.iek/2gr1h519sS.ky/GrznZHOmcsoN.', 'USER', true),
       ('6', 'mail6@mail.ru', '$2a$12$pyg9ZqA1mPXeXroylVABk.p.OVqzN3EJwfsu5KhNgpAH6ltWr7cke', 'USER', true),
       ('7', 'mail7@mail.ru', '$2a$12$DH2sZpGNuh5QwebSUMnoHu1lGr93SLXdbpQnKJDcJ9fNLNNxOLxb6', 'USER', true),
       ('8', 'mail8@mail.ru', '$2a$12$xssrjunPTqqO1JrfvE14s.UbdISqgQKJy4fu/tO9Hqb.YZM1h8Uie', 'USER', true),
       ('9', 'mail9@mail.ru', '$2a$12$zjkfNpYaXQqkxnK2QMlP7OFu77U8IjbPTqioOUc59Gnu2fyaAu5Hi', 'USER', true),
       ('10', 'mail10@mail.ru', '$2a$12$2xAmiPGAybXLZ3Pb4MtvZ.MGzjJ0QO1cFkPh8.p4MVvTQuao8Fk8O', 'USER', false);


insert into articles (user_id, title, body, status, description)
values (1, 'Title 1.1', 'test body 1', 'CREATED', 'description'),
       (2, 'Title 2.1', 'test body 1', 'CREATED', 'description'),
       (2, 'Title 2.2', 'test body 2', 'CREATED', 'description'),
       (3, 'Title 3.1', 'test body 1', 'CREATED', 'description'),
       (3, 'Title 3.2', 'test body 2', 'CREATED', 'description'),
       (3, 'Title 3.3', 'test body 3', 'PUBLISHED', 'description'),
       (4, 'Title 4.1', 'test body 1', 'CREATED', 'description'),
       (4, 'Title 4.2', 'test body 2', 'PUBLISHED', 'description'),
       (5, 'Title 5.1', 'test body 1', 'PUBLISHED', 'description'),
       (6, 'Title 6.1', 'test body 1', 'CREATED', 'description'),
       (6, 'Title 6.2', 'test body 2', 'CREATED', 'description'),
       (6, 'Title 6.3', 'test body 3', 'PUBLISHED', 'description'),
       (6, 'Title 6.4', 'test body 4', 'CREATED', 'description'),
       (7, 'Title 7.1', 'test body 1', 'PUBLISHED', 'description'),
       (7, 'Title 7.2', 'test body 2', 'PUBLISHED', 'description'),
       (9, 'Title 9.1', 'test body 1', 'PUBLISHED', 'description'),
       (9, 'Title 9.2', 'test body 2', 'CREATED', 'description');

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
