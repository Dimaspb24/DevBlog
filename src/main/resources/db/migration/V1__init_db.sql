create table if not exists users
(
    id                text primary key,
    login             text      not null,
    password          text      not null,
    role              text      not null,
    status            text      not null,
    firstname         text      null,
    lastname          text      null,
    nickname          text      not null default concat('user#', gen_random_uuid()),
    photo             text      null,
    info              text      null,
    phone             text      null,
    enabled           boolean   null     default true,
    creation_date     timestamp not null default now(),
    modification_date timestamp not null default now(),
    constraint unique_nickname unique (nickname),
    constraint unique_phone unique (phone),
    constraint unique_login unique (login)
);

create table if not exists articles
(
    id                text primary key,
    user_id           text       not null,
    title             text      not null,
    body              text      not null,
    status            text      not null,
    description       text      not null,
    enabled           boolean   not null default true,
    creation_date     timestamp not null default now(),
    modification_date timestamp not null default now(),
    publication_date  timestamp null,
    deletion_date     timestamp null,
    constraint id_fk_articles_users foreign key (user_id) references users (id) --on delete  -- При удалении юзера нужно удалять его посты?
);

create table if not exists comments
(
    id                text primary key,
    article_id        text       not null,
    author_id         text       not null,
    receiver_id       text       null,
    message           text      not null,
    enabled           boolean   not null default true,
    creation_date     timestamp not null default now(),
    modification_date timestamp not null default now(),
    deletion_date     timestamp null,
    constraint id_fk_comments_articleles foreign key (article_id) references articles (id) on delete cascade,
    constraint id_fk_comments_author_id__users foreign key (author_id) references users (id),
    constraint id_fk_comments_receiver_id__users foreign key (receiver_id) references users (id)
);


create table if not exists tags
(
    id   text primary key,
    name text not null,
    constraint unique_name unique (name)
);

create table if not exists articles_tags
(
    article_id text not null,
    tag_id     text not null,
    primary key (article_id, tag_id),
    constraint id_fk_articles_tags__articles foreign key (article_id) references articles (id) on delete cascade,
    constraint id_fk_articles_tags__tags foreign key (tag_id) references tags (id) on delete cascade
);

create table if not exists subscribers
(
    user_id       text not null,
    subscriber_id text not null,
    primary key (user_id, subscriber_id),
    constraint id_fk_subscribers_user_id__users foreign key (user_id) references users (id) on delete cascade,
    constraint id_fk_subscribers_subscriber_id__users foreign key (subscriber_id) references users (id) on delete cascade
);

create table if not exists users_articles
(
    id                text primary key,
    user_id           text       not null,
    article_id        text       not null,
    rating            int       null,
    bookmark_type     text      null,
    creation_date     timestamp not null default now(),
    modification_date timestamp not null default now(),
    constraint unique_user_id_article_id unique (user_id, article_id),
    constraint id_fk_users_articles__articles foreign key (article_id) references articles (id) on delete cascade,
    constraint id_fk_users_articles__users foreign key (user_id) references users (id) on delete cascade
);
