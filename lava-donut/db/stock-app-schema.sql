drop database if exists stock_app;
create database stock_app;
use stock_app;

-- SCHEMA
-- create tables and table relationships in accordance to ERD.png in root file
create table currencies (
	currency_id int primary key auto_increment,
    `name` varchar(50) not null, -- EX: Euros
    `code` varchar(12) not null,  -- EX: EUR 
    value_to_usd decimal(10,4) not null, -- EX: 1.17
    UNIQUE (`code`),
    CHECK (value_to_usd > 0)
);

create table stock_exchange (
	stock_exchange_id int primary key auto_increment,
    `name` varchar(125) not null, -- EX: New York Stock Exchange
    `code` varchar(12) not null, -- EX: NYSE
	timezone varchar(12) not null, -- EX: -5
    UNIQUE (`code`)
); 

-- https://www.isin.net/country-codes/
create table countries (
	country_id int primary key auto_increment,
    `name` varchar(125) not null, -- EX: United States of America
    `code` varchar(4) not null, -- EX: US
    currency_id int not null,
    CONSTRAINT fk_countries_currency_id
		foreign key (currency_id)
        references currencies(currency_id),
	UNIQUE (`code`)
);

create table stocks (
	stock_id int primary key auto_increment,
    `name` varchar(50) not null,
    `ticker` varchar(25) not null,
    asset_type varchar(50) not null,
    industry varchar(50) null,
    stock_exhange_id int not null,
    country_id int not null,
    CONSTRAINT fk_stock_stock_exhange_id
		foreign key (stock_exhange_id)
        references stock_exchange(stock_exchange_id),
	CONSTRAINT fk_stock_country_id
		foreign key (country_id)
        references countries(country_id),
	UNIQUE (`ticker`)
);

create table orders (
	order_id int primary key auto_increment,
    transaction_type varchar(4) not null, 
    shares int not null,
    price decimal(10,4) not null,
    `date` date not null,
    stock_id int not null,
    CONSTRAINT fk_orders_stock_id
		foreign key (stock_id)
        references stocks(stock_id),
	CHECK (price > 0 AND shares > 0)
);

create table `user` (
	user_id int primary key auto_increment,
    username varchar(25) not null,
    password_hashed varchar(150) not null,
    first_name varchar(150) not null,
    last_name varchar(150) not null,
    permission varchar(15) not null,
    currency_id int not null,
    CONSTRAINT fk_user_currency_id
		foreign key (currency_id)
        references currencies(currency_id),
	UNIQUE (username)
);

create table portfolio (
	portfolio_id int primary key auto_increment,
    account_type varchar(50) not null,
    user_id int not null,
    CONSTRAINT fk_portfolio_user_id
		foreign key (user_id)
        references `user`(user_id)
);

create table portfolio_orders (
	port_order_id int primary key auto_increment,
    portfolio_id int not null,
    order_id int not null,
    CONSTRAINT fk_portfolio_orders_portfolio_id
		foreign key (portfolio_id)
        references portfolio(portfolio_id),
	CONSTRAINT fk_portfolio_orders_order_id
		foreign key (order_id)
        references orders(order_id)
);
