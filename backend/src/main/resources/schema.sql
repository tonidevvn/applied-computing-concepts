CREATE TABLE product_data (
 id bigint not null AUTO_INCREMENT,
 name varchar(256) not null,
 price varchar(256),
 image varchar(max),
 url varchar(max),
 description varchar(max),
 PRIMARY KEY ( id )
);