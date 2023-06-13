create table patients(

    id bigint not null auto_increment,
    name varchar(100) not null,
    email varchar(100) not null unique,
    ssn varchar(14) not null unique,
    street varchar(100) not null,
    zip varchar(9) not null,
    number varchar(20),
    state char(2) not null,
    city varchar(100) not null,
    telephone varchar(20) not null,
    active tinyint not null,

    primary key(id)

);