drop database if exists stock_schema;
create database stock_schema;
use stock_schema;

-- create tables and table relationships in accordance to ERD.png in root file
create table currencies (
	currency_id int primary key auto_increment,
    `name` varchar(50) not null, -- EX: Euros
    `code` varchar(12) not null,  -- EX: ISO 4217  
    value_to_usd decimal not null -- EX: 1.17
);

create table stock_exchange (
	stock_exchange_id int primary key auto_increment,
    `name` varchar(125) not null, -- EX: New York Stock Exchange
    `code` varchar(12) not null, -- EX: NYSE
	timezone int not null -- EX: -- EX: -5
); 