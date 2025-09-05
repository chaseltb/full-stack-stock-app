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

Admins:
* Admins can delete users (in cases of inappropriate behaviour)
* Admins can delete stocks from being displayed
* Admins can recommend stocks to users (watchlist, maybe tailored to users)

Technology Stack: Briefly mention the key technologies, frameworks, and tools you will use to build the application. Why did you choose these tools, and how will they help you achieve your solution?

* Frontend: React, JavaScript, CSS, Material Design 3 (or Bootstrap), HTML
* Backend: Spring Boot, Spring MVC, Java, JDBC
* Database: MySQL
* APIs: AlphaAdvantage API (still deciding), OpenAPI/Swagger, 
* Cloud: AWS
* Visualization: Still deciding on a tool

---

## Glossary

In this section, you'll define key terms used within your application. Providing clear and consistent definitions for each term ensures that everyone on your team has a common understanding of the concepts. This is essential not only for your project's success but also for communicating effectively with stakeholders.

---

## High Level Requirements

- Manage **6–10 database tables (entities)** that are independent concepts. A simple bridge table doesn't count.  
- **MySQL** for data management.  
- **Spring Boot, MVC, JDBC, Testing, React**.  
- An HTML and CSS UI that's built with React.  
- Sensible layering and pattern choices.  
- A full backend test suite that covers the domain and data layers (unit + integration).  
- Must have at least **2 roles** (example: User and Admin).  
- Must provide an **OpenAPI/Swagger specification** documenting all endpoints.  
- Must be **hosted on a cloud provider** (AWS, Azure, or GCP).  

In this section, you will outline how you plan to meet the high-level requirements of your project. For each requirement, explain the specific steps you will take, the tools or technologies you will use, and how you'll implement them to meet the project’s objectives.

---

## User Stories

In this section, you will outline how each user story will be implemented in your application. For each user story, explain the user’s goals, the actions they will take, the preconditions required for each action, and the postconditions that follow. This will help clarify how you plan to meet the functional needs of the project.

---

## Learning Goals (Mandatory: 3 minimum)

- Each team must select **at least three learning goals** outside of the technologies covered in class.  
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
elastic search to filter stocks

**Learning Goal: We want to gather and store realtime stock info**

Application: We will use Alpha Vantage API to gather realltime stock data
Reseach and Resources: We'll start with the base documentation and the example jsons that they provide.
Challenges: We anticipate the API returning information that may not be initially compatible with our API, we may have to adjust our API in order to accomodate.
Success Criteria: If we are able to gather stock data in a timely manner, then we will consider this a success.

Example:

Learning Goal: I want to learn how to integrate Google Maps into a web application.

Application: I will use Google Maps API to display the location of each run on an interactive map within the app.  
Research and Resources: I’ll start with the official Google Maps API documentation and a Udemy course on map APIs in JavaScript.  
Challenges: I anticipate needing to figure out how to dynamically load map locations and handle API key security. To address this, I’ll practice with dummy data first and research security best practices for frontend applications.  
Success Criteria: If users can see a Google Maps widget in the app that dynamically updates with each run location, then I’ll consider this learning goal achieved.

---

## Class Diagram 

Provide a visual representation of the relationships between the main classes in your application. The class diagram should illustrate how different entities in your system are connected and how they interact. Include classes, attributes, methods, and relationships.  

---

## Class List 

List all the classes in your application. For each class, provide a brief description of its role, its methods, and its fields.  

---

## Task List with Estimated Completion Times 

Break down all tasks required to complete the project.  
- No task should exceed 4 hours (break down if longer).  
- Organize tasks logically (database setup, backend services, UI, testing, documentation).  
- Include estimates for each task to ensure proper workload management.  
