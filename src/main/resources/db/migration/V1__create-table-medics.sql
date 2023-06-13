create table medics(

    id bigint not null auto_increment,
    name varchar(100) not null,
    email varchar(100) not null unique,
    register varchar(6) not null unique,
    field varchar(100) not null,
    street varchar(100) not null,
    zip varchar(9) not null,
    number varchar(20),
    state char(2) not null,
    city varchar(100) not null,

    primary key(id)

);