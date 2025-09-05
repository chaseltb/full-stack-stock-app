# Capstone Project Planning

## Deliverables 
- Proposal 
- Wireframes
- Database Schema

## Proposal Outline / Example 

## Problem Statement

Context: Briefly describe the context or environment in which the problem exists. Who are the primary users or groups affected? What is the scope of the issue?

**People who have investments on multiple exchanges and want to know how their stocks have performed over time or estimate what they might owe in taxes so they don't have to stress about it later.**

Core Problem: What specific, unmet need or challenge does this project address? Explain in simple terms what is currently missing, difficult, or ineffective for users.

**There are existing tools but they are often hard to use or significantly outdated. We want to have a cost-basis tracking tool that can also store a user's portfolio and account for new buy/sell orders and return of capital (lowering cost basis from dividend).**

Impact on Users: Describe how this problem affects the users or stakeholders. What pain points do they experience as a result of this issue? What specific frustrations, inefficiencies, or missed opportunities arise from the current situation?

**Users are frustrated from having to use multiple tools that are outdated and don't have persistent data storage, so right now they have to enter their info in each time they want a calculation with no way of saving.**

Opportunity for Solution: Why is solving this problem meaningful for your intended audience? How would a solution add value to their experience or address their needs in a unique way?

**This would save them time and get them a rough idea of their tax impact or portfolio size without having to reenter data on outdated tools each time.**

---

## Technical Solution 

Overview of the Solution: Describe the high-level approach you plan to take to solve the problem outlined in your Problem Statement. What kind of application or system will you create? How will it address the core problem?

**A web application that allows users to store their stock infromation, view general stock information, and their chosen stocks and currency.**

Key Features and Functionalities: List and describe the main features of the application. What are the critical functionalities that users will need to perform in order to effectively interact with the system?

Key Features
* Viewing Stock Information
* Calculating average value of stocks accrooss exchange platforms
* Calculating dividend payments of real estate stocks for tax purposes
* Filtering stocks
Functionalities
* Users will need a chosen currency
* Users will need an account (username and passowrd)
* Users will need to choose a stock to review
* Users can favorite a stock

User Scenarios: Explain how the technical solution will support different user needs. Provide two to three specific use cases or scenarios that illustrate how users (e.g., runners, club admins) will interact with the system and what value they will gain from each interaction.

Users:
* Create accounts
* View their portfolio
* Add stocks to their portfolio
* Edit their portfolio
* Delete stocks from their portfolio
* Calculate average value of stocks accrooss exchange platforms
* Calculate dividend payments of real estate stocks for tax purposes

Admins:
* Admins can delete users (in cases of inappropriate behaviour)
* Admins can delete stocks from being displayed
* Admins can recommend stocks to users (watchlist, maybe tailored to users)

Technology Stack:
<!-- Briefly mention the key technologies, frameworks, and tools you will use to build the application. Why did you choose these tools, and how will they help you achieve your solution? -->

* Frontend: React, JavaScript, CSS, Material Design 3, HTML
* Backend: Spring Boot, Spring MVC, Java, JDBC
* Database: MySQL
* APIs: AlphaAdvantage API, OpenAPI/Swagger, OpenExchangeRates
* Cloud: AWS
* Visualization: Chart.js

---

## Glossary

<!-- In this section, you'll define key terms used within your application. Providing clear and consistent definitions for each term ensures that everyone on your team has a common understanding of the concepts. This is essential not only for your project's success but also for communicating effectively with stakeholders. -->

1. **Cost Basis**: The original value of an asset (e.g., stock or bond) for tax purposes. It is used to calculate capital gains or losses. 

&emsp;&emsp;&emsp;Example: If a user bought 100 shares of a stock for $10 each, the cost basis would be $100.

2. **Capital Gains**: The profit made from the sale of an asset, such as stocks, bonds, or real estate. It is calculates by subtracting the cost basis from the sale price of the asset.

&emsp;&emsp;&emsp;Example: If a user sold a stock for $1,500 that they purchased for $1,000, their capital gain is $500.

3. **Capital Loss**: A loss incurred when an asset is sold for less than its cost basis. This can offset capital gains for tax purposes.

&emsp;&emsp;&emsp;Example: If a user bought a stock for $1,000 and sold it for $800, their capital loss is $200.

4. **Dividend**: A portion of a company's earnings that is distributed to shareholders, typically in cash or additional stock. Dividends are often paid quarterly.

&emsp;&emsp;&emsp;Example: A company paying a $2 dividend per share on 100 shares would distribute $200 to a shareholder with 100 shares.

5. **Stock Ticker**: A unique identifier assigned to each publicly traded stock, typically consisting of a 3-4 letter abbreviation.

6. **Asset Type**: A category that classifies an investment, such as stocks, bonds, ETFs, or real estate. A stock would be categorized under the "Stock" asset type.

7. **Portfolio**: A collection of investments, such as stocks, bonds, ETFs, or mutual funds, held by an individual or institution.

&emsp;&emsp;&emsp;Example: A user’s portfolio might contain 100 shares of Apple, 200 shares of Tesla, and a bond investment.

8. **Exchange Rate**: The rate at which one currency can be exchanged for another, used when displaying stock prices in different currencies.

&emsp;&emsp;&emsp;Example: If the exchange rate between USD and EUR is 1 USD = 0.85 EUR, then a stock priced at $100 USD would be displayed as €85 EUR.

9. **Portfolio Diversification**: The practice of spreading investments across different asset types or sectors to reduce risk.An investor might hold a mix of stocks, bonds, and real estate to diversify their portfolio.

10. **FIFO (First In, First Out)**: FIFO is a method of calculating the cost basis of stocks where the first shares purchased are considered the first ones sold. This is commonly used in tax calculations to determine capital gains.

&emsp;&emsp;&emsp;Example: If a user bought 100 shares of stock at $10 each, and later bought another 100 at $15, selling 100 shares would mean the cost basis is calculated using the first 100 shares purchased at $10, regardless of the current market price.

11. **Dividend Adjustments**: Dividend adjustments refer to modifications in the cost basis of stocks based on dividend payments that affect the stock's value. For example, if a stock pays dividends, the cost basis may be adjusted to account for return of capital, which effectively reduces the taxable gain when the stock is sold.

&emsp;&emsp;&emsp;Example: If a company pays a dividend and a user reinvest it in more shares of the stock, the cost basis of the user's investment in that stock will be adjusted upwards to reflect the additional shares bought with the dividend income.

---

## High Level Requirements

<!-- - Manage **6–10 database tables (entities)** that are independent concepts. A simple bridge table doesn't count.  
- **MySQL** for data management.  
- **Spring Boot, MVC, JDBC, Testing, React**.  
- An HTML and CSS UI that's built with React.  
- Sensible layering and pattern choices.  
- A full backend test suite that covers the domain and data layers (unit + integration).  
- Must have at least **2 roles** (example: User and Admin).  
- Must provide an **OpenAPI/Swagger specification** documenting all endpoints.  
- Must be **hosted on a cloud provider** (AWS, Azure, or GCP).   -->
1. Database Design and Management (6 - 10 Entities)
- The database tables include: 
  - Users: To store user information such as name, username, password, etc.
  - Stocks: Contains details like stock name, ticker, asset type, and country.
  - Orders: Tracks the buy/sell transactions including share quantity, price, date, and transaction type.
  - Currencies: Stores currency details like name, code, and exchange rate against USD.
  - Countries: Holds information about different countries for stock and user association.
  - Portfolios: User-specific portfolios to track owned stocks.
  - StockExchanges: Data related to stock exchanges (e.g., NYSE, NASDAQ).
- Data Integrity and Relationships:
  - Tables should maintain foreign key relationships where necessary
  - Validations should be applied accross all entities
  - Bridge tables should implemented where necessary

2. Spring Boot Backend
- Spring Boot will be used to configute the development and deployment of the application.
- Spring MVC will handle the infrastructure for handling HTTP requess, managing views, and interacting with the business logic.
- Security: JWT authentication will be used to secure endpoints for users and admins. Role-based access control will ensure that users cannot access admin-only features.
- Backend Testing: Unit and integration tests will be wrtitten for services and repositories with JUnit and Mockito.
- Error Handling: Global exception handlers will be used for standardized error responses.

3. Frontend with React
- The React frontend will communicate with the backend API through the endpoints.
- Material Design 3 will be used to style the application for a more modern, clean, and responsive design.
- Chart.js will be used to display stock data visually, showing historical performance and other key metrics.
- State management will be handled with React's built-in hooks.

4. API Documentation with Swagger/OpenAPI
- Swagger will be used to document the backend API, providing clear docuemntation for all available endpoints.
- The Swagger UI will allow for exploring available API routes, parameters, and expected responses.

5. Clound Hosting on AWS
- The entire application will be hosted on AWS.
- The backend API will be hosted on Amazon EC2 and the database will be stored with Amazon RDS. 

6. Multi-Currency Support
- The OpenExchangeRates API will be used to get the latest exchange rates which will be stores in the Currenct table.

7. Real-Time Data and Tax Calculations
- AlphaVantageAPI will be used to get stock data (real-time getched more frequently than historical)
- Historial stock data will be integrated for visualization
- Implement logic for calculating cost basis using FIFO
- If the stock pays a dividend and the user has reinvested it, the dividend will adjust the cost basis.
- Each stock transaction (buy/sell) will be logged in an Orders table
- Implement methods to calculate capital gains by comparing the sale price with the initial purchase price using the selected tax calculation method.

<!-- In this section, you will outline how you plan to meet the high-level requirements of your project. For each requirement, explain the specific steps you will take, the tools or technologies you will use, and how you'll implement them to meet the project’s objectives. -->

---

## User Stories

<!-- In this section, you will outline how each user story will be implemented in your application. For each user story, explain the user’s goals, the actions they will take, the preconditions required for each action, and the postconditions that follow. This will help clarify how you plan to meet the functional needs of the project. -->

### User Role
- As a user, I want to create an account so that I can log in and start managing my stock portfolio
- As a user, I want to add stocks to my portfolio so that I can track my investments
- As a user, I want to view all the stocks in my portfolio so that I can track their performance, average value, and investment status
- As a user, I want to remove stock when I no longer want to track them or have sold them
- As a user, I want to see detailed information about a stock, includeing its hostorical performance, so that I can make informed decisions about my investments <!-- Charts, Recent prices -->
- As a user, I want to choose my preferred currency so that the stock values in my portfolio can be shown in my selected currency
- As a user, I want to calculate the average values of my stocks accreoss different exchange platforms do that I can get a clear idea of their total worth, even if I've purchsed them from different exchanges
- As a user, I want to calculate the dividend payments for my real estate stocks so that I can estimate the tax impact of those dividends
- As a user, I want to filter stocks in my portfolio based on different criteria so that I can easily find and compare specific stocks in my portfolio


### Admin Role
- As an admin, I want to delete users from the system if necessary (e.g., for inappropriate behaviour), so that the user base remains safe and appropriate
- As an admin, I want to disable or delete stocks from being available for users to buy/sell in case of market changes or other concerns
- As an admins, I want to recommend stocks to users based on their portfolios or investment preferences, so users can discover new investment opportunities <!-- e.g., a stock of the week or auto stock of the day -->

---

## Learning Goals <!--(Mandatory: 3 minimum)-->

<!-- - Each team must select **at least three learning goals** outside of the technologies covered in class.  
- Each learning goal must directly improve the project.  
- Document for each goal:  
  - What you learned,  
  - Where it is applied in the project,  
  - Challenges faced,  
  - Resources used.
 
ideas:
currency conversion (real time api)
modern CSS framework (eg material design 3)
graphing stocks/visualization
real time stock api
saving user accounts and their info
elastic search to filter stocks -->

**Learning Goal: We want to gather and store realtime and historical stock info**

- Application: We will use Alpha Vantage API to gather realltime stock data
- Reseach and Resources: We will start by familiarizing ourselves with the Alpha Vantage documentation to understand how to request real-time data and handle the different formats and limits of API responses. We will also use example JSON responses provided by Alpha Vantage to understand how the data is structured and how to integrate it into our backend system.
- Challenges: 
  - We anticipate having some difficulties making the graph designs consistent with the rest of the front end, to address this we will practice desiging the graphs first in order to build the site around what we are able to do with it.
  - Alpha Vantage has strict rate limits, especially on the free tier (5 calls per minute, 500 calls per day). As such, we must ensure that our application doesn't exceed these limits, especially with frequent real-time price updates. We will need to implement rate-limiting and caching strategies to manage API calls effectively.
  - Real-time data needs to be integrated with historical data for effective analysis. We must figure out how to store both real-time price updates and historical data in a consistent manner. Real-time data will be updated frequently (e.g., every minute), while historical data will be queried less often and could be stored at intervals (e.g., daily).
- Success Criteria: If we are able to gather stock data in a timely manner and manage API call rates efficiently, ensuring that we stay within the API's usage limits without hitting errors, then we will consider this a success.

**Learning Goal: We want to display the stock data in a digestible way**

- Application: We will use the Chart.js library to easily display graphs according to the information gathered by Alpha Vantage API.
- Research and Resources: We'll likely start with thier base documentation then look for examples online on how others used this library, either through stack overflow or youtube.
- Challenges: We anticipate having some difficulties making the graph designs consistent with the rest of the front end, to address this we will practice desiging the graphs first in order to build the site around what we are able to do with it.
- Success Criteria:  If we are able to display stock data in a graph, that both displays the information accurately and in a pleasant manner, we will consider it a success.  

**Learning Goal: We want to make our site look modern**

- Application: We will use Material Design 3 in, instead of Bootstrap, in order to handle the styling of our site
- Research and Resources: We will start by reading the documentation then referencing other industrial sites for inspiration on how to create a modern look.
- Challenges: We anticipate having some difficulties with the syntax of this specific library, so we will have to carefully reference documentation and examples.
- Success Criteria: We will consider this a success if this tool makes it easier for us to create a well designed and good looking user interface.

<!-- Example:

Learning Goal: I want to learn how to integrate Google Maps into a web application.

Application: I will use Google Maps API to display the location of each run on an interactive map within the app.  
Research and Resources: I’ll start with the official Google Maps API documentation and a Udemy course on map APIs in JavaScript.  
Challenges: I anticipate needing to figure out how to dynamically load map locations and handle API key security. To address this, I’ll practice with dummy data first and research security best practices for frontend applications.  
Success Criteria: If users can see a Google Maps widget in the app that dynamically updates with each run location, then I’ll consider this learning goal achieved. -->

---

## Class Diagram 

Provide a visual representation of the relationships between the main classes in your application. The class diagram should illustrate how different entities in your system are connected and how they interact. Include classes, attributes, methods, and relationships.  

---

## Class List 

List all the classes in your application. For each class, provide a brief description of its role, its methods, and its fields.

**Included in the stock-app-plan-detailed.md file.**

---

## Task List with Estimated Completion Times 

Break down all tasks required to complete the project.  
- No task should exceed 4 hours (break down if longer).  
- Organize tasks logically (database setup, backend services, UI, testing, documentation).  
- Include estimates for each task to ensure proper workload management.  
