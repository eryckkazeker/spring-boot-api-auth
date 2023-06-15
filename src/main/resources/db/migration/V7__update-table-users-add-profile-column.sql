alter table users add profile varchar(30);
update users set profile="ROLE_ADMIN" where login='admin';