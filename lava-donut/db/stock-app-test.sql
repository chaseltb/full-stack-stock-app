drop database if exists stock_app_test;
create database stock_app_test;
use stock_app_test;

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
    current_price decimal(10,4) not null,
    stock_exchange_id int not null,
    country_id int not null,
    CONSTRAINT fk_stock_stock_exchange_id
		foreign key (stock_exchange_id)
        references stock_exchange(stock_exchange_id),
	CONSTRAINT fk_stock_country_id
		foreign key (country_id)
        references countries(country_id),
	UNIQUE (`ticker`),
    CHECK (asset_type IN ('STOCK', 'BOND', 'ETF'))
);

create table orders (
	order_id int primary key auto_increment,
    transaction_type varchar(10) not null,
    shares int not null,
    price decimal(10,4) not null,
    `date` date not null,
    stock_id int not null,
    CONSTRAINT fk_orders_stock_id
		foreign key (stock_id)
        references stocks(stock_id),
	CHECK (price > 0 AND shares > 0),
    CHECK (transaction_type IN ('BUY', 'SELL'))
);

create table app_user (
    app_user_id int primary key auto_increment,
    username varchar(50) not null unique,
    password_hash varchar(2048) not null,
    disabled boolean not null default(0)
);

create table app_role (
    app_role_id int primary key auto_increment,
    `name` varchar(50) not null unique
);

create table app_user_role (
    app_user_id int not null,
    app_role_id int not null,
    constraint pk_app_user_role
        primary key (app_user_id, app_role_id),
    constraint fk_app_user_role_user_id
        foreign key (app_user_id)
        references app_user(app_user_id),
    constraint fk_app_user_role_role_id
        foreign key (app_role_id)
        references app_role(app_role_id)
);

create table `user` (
	user_id int primary key auto_increment,
    first_name varchar(150) not null,
    last_name varchar(150) not null,
    currency_id int not null,
    app_user_id int not null,
    CONSTRAINT fk_user_currency_id
		foreign key (currency_id)
        references currencies(currency_id),
    CONSTRAINT fk_user_app_user_id
    		foreign key (app_user_id)
            references app_user(app_user_id)
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

-- TEST DATA
delimiter //
create procedure set_known_good_state()
begin

    delete from portfolio_orders;
    alter table portfolio_orders auto_increment = 1;
    delete from portfolio;
    alter table portfolio auto_increment = 1;
    delete from `user`;
    alter table `user` auto_increment = 1;
    delete from app_user_role;
    delete from app_user;
    alter table app_user auto_increment = 1;
    delete from app_role;
    alter table app_role auto_increment = 1;
    delete from orders;
    alter table orders auto_increment = 1;
    delete from stocks;
    alter table stocks auto_increment = 1;
    delete from countries;
    alter table countries auto_increment = 1;
    delete from stock_exchange;
    alter table stock_exchange auto_increment = 1;
    delete from currencies;
    alter table currencies auto_increment = 1;

    insert into currencies
		(currency_id, `name`, `code`, value_to_usd)
	values
		(1, 'United States dollar', 'USD', 1.0),
        (2, 'Euro', 'EUR', 1.17),
        (3, 'Chinese Yuan', 'CNY', 0.14),
        (4, 'Brazilian Real', 'BRL', 0.19); -- for testing delete in repo tests


	insert into stock_exchange
		(stock_exchange_id, `name`, `code`, timezone)
	values
		(1, 'New York Stock Exchange', 'NYSE', -5),
        (2, 'Frankfurt Stock Exchange', 'XETR', 1),
        (3, 'Shanghai Stock Exchange', 'SSE', 8),
        (4, 'London Stock Exchange', 'LSE', 0);

    insert into countries
		(country_id, `name`, `code`, currency_id)
	values
		(1, 'United States of America', 'US', 1),
        (2, 'Federal Republic of Germany', 'DE', 2),
        (3, 'People''s Republic of China', 'CN', 3);

	insert into stocks
		(stock_id, `name`, ticker, asset_type, industry, current_price, stock_exchange_id, country_id)
	values
		(1, 'AMERICAN AIRLINES GROUP INC', 'TEST-TICKER1', 'STOCK', 'airline and aviation', 12.915, 1, 1), -- price: 12.915
        (2, 'AMERICAN TEST STOCK 1', 'TEST-TICKER2', 'ETF', 'agriculture', 5.0, 1, 1),
        (3, 'AMERICAN TEST STOCK 2', 'TEST-TICKER3', 'BOND', 'technology', 6.8, 1,1),
        (4, 'GERMAN TEST STOCK 1', 'TEST-TICKER4', 'STOCK', 'airline and aviation', 9.6, 2, 2),
        (5, 'GERMAN TEST STOCK 2', 'TEST-TICKER5', 'ETF', 'agriculture', 78.5, 2, 2),
        (6, 'GERMAN TEST STOCK 3', 'TEST-TICKER6', 'STOCK', 'technology', 95.4, 2, 2),
        (7, 'CHINESE TEST STOCK 1', 'TEST-TICKER7', 'STOCK', 'airline and aviation', 0.01, 3, 3),
        (8, 'CHINESE TEST STOCK 2', 'TEST-TICKER8', 'ETF', 'agriculture', 0.45, 3, 3),
        (9, 'CHINESE TEST STOCK 3', 'TEST-TICKER9', 'BOND', 'technology', 0.001, 3, 3);

	insert into orders
		(order_id, transaction_type, shares, price, `date`, stock_id)
	values
		(1, 'BUY', 20, 2000, '2025-05-17', 1),
        (2, 'SELL', 5, 232.50, '2024-03-08', 3),
        (3, 'BUY', 1, 250.876, '2022-08-09', 5),
        (4, 'SELL', 100, 45.085, '2002-12-16', 6),
        (5, 'BUY', 22, 67.95, '2015-10-01', 7),
        (6, 'SELL', 5, 64.73, '2023-07-05', 9);

	insert into app_role (`name`) values
        ('USER'),
        ('ADMIN');

    -- passwords are set to "P@ssw0rd!"
    insert into app_user (username, password_hash, disabled)
        values
        ('john@smith.com', '$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa', 0),
        ('sally@jones.com', '$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa', 0),
        ('tim@bob.com', '$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnsadfsfSi8Z3IZzQa', 0); -- FOR TESTING DELETE IN USER

    insert into app_user_role
        values
        (1, 2),
        (2, 1);

	insert into `user`
		(user_id, first_name, last_name, currency_id, app_user_id)
	values
		(1, 'TEST FIRST NAME', 'TEST LAST NAME', 1, 1),
        (2, 'TEST FIRST NAME', 'TEST LAST NAME', 2, 2),
        (3, 'TEST FIRST NAME', 'TEST LAST NAME', 3, 3);  -- FOR TESTING DELETE, DOESNT CONNECT TO ANYTHING

    -- ERROR IS HERE WITH REFERENCING USER ID
    insert into portfolio
		(portfolio_id, account_type, user_id)
	values
		(1, 'Retirement', 1),
        (2, 'Investment', 1),
        (3, 'Roth IRA', 2),
        (4, 'Retirement', 2),
        (5, 'Investment', 2);

    insert into portfolio_orders
		(port_order_id, portfolio_id, order_id)
	values
		(1, 1, 1),
        (2, 1, 2),
        (3, 2, 3),
        (4, 3, 4),
        (5, 4, 5),
        (6, 4, 6);
        
end //
delimiter ;