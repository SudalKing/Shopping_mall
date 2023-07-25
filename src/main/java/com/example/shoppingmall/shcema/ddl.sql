create table User
(
    id int auto_increment,
    email varchar(20) not null,
    password varchar(20) not null,
    createdAt datetime not null,
    constraint user_id_uindex
        primary key (id)
);



