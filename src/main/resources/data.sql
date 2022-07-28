create table users(
	id integer not null primary key,
    username varchar(50) not null,
	password varchar(500) not null
);
insert into users (id, username, password) values (1, 'cammy', '{bcrypt}$2a$10$6vznRZg9sYnyY7EVPRNA9.AMvWwpTSttV5YN/cqwkop2hceKDp4eq');
insert into users (id, username, password) values(2, 'chun-li', '{bcrypt}$2a$10$6vznRZg9sYnyY7EVPRNA9.AMvWwpTSttV5YN/cqwkop2hceKDp4eq');
insert into users (id, username, password) values(3, 'juri', '{bcrypt}$2a$10$6vznRZg9sYnyY7EVPRNA9.AMvWwpTSttV5YN/cqwkop2hceKDp4eq');