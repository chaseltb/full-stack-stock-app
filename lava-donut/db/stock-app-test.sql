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

-- TEST DATA
delimiter //
create procedure set_known_good_state()
begin
	
    delete from portfolio_orders;
    alter table portfolio_order auto_increment = 1;
    delete from portfolio;
    alter table portfolio auto_increment = 1;
    delete from `user`;
    alter table `user` auto_increment = 1;
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
        (3, 'Chinese Yuan', 'CNY', 0.14);
        
	insert into stock_exchange
		(stock_exchange_id, `name`, `code`, timezone)
	values
		(1, 'New York Stock Exchange', 'NYSE', -5),
        (2, 'Frankfurt Stock Exchange', 'XETR', 1),
        (3, 'Shanghai Stock Exchange', 'SSE', 8);
	
    insert into countries
		(country_id, `name`, `code`, currency_id)
	values
		(1, 'United States of America', 'US', 1),
        (2, 'Federal Republic of Germany', 'DE', 2),
        (3, `People's Rupublic of China`, 'CN', 3);
        
	insert into stocks
		(stock_id, `name`, ticker, asset_type, industry, stock_exchange_id, country_id)
	values
		(1, 'AMERICAN AIRLINES GROUP INC', 'TEST-TICKER1', 'common stock', 'airline and aviation', 1, 1), -- price: 12.915
        (2, 'AMERICAN TEST STOCK 1', 'TEST-TICKER2', 'equity', 'agriculture', 1, 1),
        (3, 'AMERICAN TEST STOCK 2', 'TEST-TICKER3', 'bond', 'technology', 1,1),
        (4, 'GERMAN TEST STOCK 1', 'TEST-TICKER4', 'common stock', 'airline and aviation', 2, 2),
        (5, 'GERMAN TEST STOCK 2', 'TEST-TICKER5', 'equity', 'agriculture', 2, 2),
        (6, 'GERMAN TEST STOCK 3', 'TEST-TICKER6', 'bond', 'technology', 2, 2),
        (7, 'CHINESE TEST STOCK 1', 'TEST-TICKER7', 'common stock', 'airline and aviation', 3, 3),
        (8, 'CHINESE TEST STOCK 2', 'TEST-TICKER8', 'equity', 'agriculture', 3, 3),
        (9, 'CHINESE TEST STOCK 3', 'TEST-TICKER9', 'bond', 'technology', 3, 3);
        
	insert into orders
		(order_id, transaction_type, shares, price, `date`, stock_id)
	values
		(1, 'buy', 20, 12.915, '2025-05-17', 1),
        (2, 'sell', 5, 1.1, '2024-03-08', 3),
        (3, 'buy', 1, 5.075, '2022-08-09', 5),
        (4, 'sell', 100, 2, '2002-12-16', 6),
        (5, 'buy', 22, 3.75, '2015-10-01', 7),
        (6, 'sell', 5, 8.05, '2023-07-05', 9);
        
	insert into `users`
		(user_id, username, password_hashed, first_name, last_name, permission, currency_id)
	values
		(1, 'americanUser', 'toBeHashed', 'TEST FIRST NAME', 'TEST LAST NAME', 'USER', 1),
        (2, 'germanUser', 'toBeHashed', 'TEST FIRST NAME', 'TEST LAST NAME', 'ADMIN', 2),
        (3, 'chineseUser', 'toBeHashed', 'TEST FIRST NAME', 'TEST LAST NAME', 'USER', 3);
	
    insert into portfolio
		(portfolio_id, account_type, user_id)
	values
		(1, 'Retirement', 1),
        (2, 'Investment', 1),
        (3, 'Roth IRA', 2),
        (4, 'Retirement', 3),
        (5, 'Investment', 3);
        
        
        
end //
delimiter ;