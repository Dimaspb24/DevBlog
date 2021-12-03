create table if not exists users
(
    id           serial    not null primary key,
    first_name   text      not null,
    last_name    text      not null,
    nickname     text      not null,
    photo        text      null,
    info         text      null,
    phone        text      null,
    enabled      boolean   null default true,
    created_date timestamp not null,
    updated_date timestamp not null,
    constraint unique_nickname unique (nickname),
    constraint unique_phone unique (phone)
);

create table if not exists security_users
(
    user_id  serial not null primary key,
    login    text   not null,
    password text   not null,
    role     text   not null,
    status   text   not null,
    constraint id_fk_security_users__users foreign key (user_id) references users (id),
    constraint unique_login unique (login)
);

create table if not exists posts
(
    id             serial    not null primary key,
    user_id        int       not null,
    title          text      not null,
    body           text      not null,
    status         text      not null,
    description    text      not null,
    enabled        boolean   not null default true,
    created_date   timestamp not null,
    updated_date   timestamp not null,
    published_date timestamp not null,
    deleted_date   timestamp null,
    constraint id_fk_posts_users
        foreign key (user_id) references users (id)
);

create table if not exists comments
(
    id           serial    not null primary key,
    post_id      int       not null,
    author_id    int       not null,
    receiver_id  int       null,
    message      text      not null,
    enabled      boolean   not null default true,
    created_date timestamp not null,
    updated_date timestamp not null,
    deleted_date timestamp null,
    constraint id_fk_comments_post_id__posts
        foreign key (post_id) references posts (id),
    constraint id_fk_comments_author_id__users
        foreign key (author_id) references users (id),
    constraint id_fk_comments_receiver_id__users
        foreign key (receiver_id) references users (id)
);


create table if not exists subscribers
(
    user_id       int       not null,
    subscriber_id int       not null,
    created_date  timestamp not null,
    primary key (user_id, subscriber_id),
    constraint id_fk_subscribers_user_id__users
        foreign key (user_id) references users (id),
    constraint id_fk_subscribers_subscriber_id__users
        foreign key (subscriber_id) references users (id)
);

create table if not exists tags
(
    id   serial not null primary key,
    name text   not null,
    constraint unique_name unique (name)
);

create table if not exists posts_tags
(
    post_id int not null,
    tags_id int not null,
    primary key (post_id, tags_id),
    constraint id_fk_posts_tags__posts
        foreign key (post_id) references posts (id),
    constraint id_fk_posts_tags__tags
        foreign key (tags_id) references tags (id)
);

create table if not exists users_posts
(
    user_id       int      not null,
    post_id       int      not null,
    rating        smallint null,
    bookmark_type text     null,
    primary key (user_id, post_id),
    constraint id_fk_users_posts__posts
        foreign key (post_id) references posts (id),
    constraint id_fk_users_posts__users
        foreign key (user_id) references users (id)
);
