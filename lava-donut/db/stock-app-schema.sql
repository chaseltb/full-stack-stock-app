drop database if exists stock_schema;
create database stock_schema;
use stock_schema;

-- create tables and table relationships in accordance to ERD.png in root file
create table currencies (
	currency_id int primary key auto_increment,
    `name` varchar
);
