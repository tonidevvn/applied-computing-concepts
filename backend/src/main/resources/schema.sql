CREATE TABLE product_data (
 id bigint not null AUTO_INCREMENT,
 name varchar(256) not null,
 store varchar(256) not null,
 category varchar(256) not null,
 price float not null,
 image varchar(max),
 url varchar(max),
 description varchar(max),
 PRIMARY KEY ( id )
);