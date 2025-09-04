# Stock App Plan (Detailed)

## High-level Requirements

User is a stock holder.

- Creates an account (login/username + password).
- Buy/Add a stock to portfolio.
- Sell/Delete a stock from portfolio.
- View all stocks in portfolio.
- View a graph of a chosen stocks historical price.


Admin

- Delete a user
- Disable/delete a stock from being bought/sold.
- Admins can recommend stocks to users (watchlist, maybe tailored to users).

## User

### Data
- **Username**: name that identifies the user
- **password_hashed**: user special password for logging in
- **currency_type_id**: users preferred currency.
- **first_name**: users first name
- **last_name**: users last name
- **permission**: admin/user (defaults to user)

### Validation
- **Username**: required, unique username
- **password_hashed**: required, cannot be null or blank, must be atleast 8 chars
- **currency_type_id**: required
- **first_name**: not blank
- **last_name**: not blank
- **permission**: enum/boolean to signify admin or user 

## Stocks

### Data

- **Name**: the name of the stock 
- **Ticker**: the unique identifier/abbreviation of the stock name 
- **Asset Type**: enum for the type of asset (stock, bond, ETF) 
- **Industry**: the industry the company is in 
- **Country**: the country that the company is in

Asset Type must be a Java enum.

### Validation

- **Name**: required
- **Ticker**: required, unique
- **Asset Type**: required
- **Industry**: optional
- **Country**: required


## Orders

### Data
- **Transaction Type**: buy/sell (Enum or Boolean), 
- **Stock**: reference to a Stock object, 
- **Shares**: # shares, 
- **Date**: the date the order was executed, 
- **Price**: the price that the order was executed at, 
- **Owner**: reference to the person that did the order

### Validation
- **Transaction Type**: required
- **Stock**: required references stock ticker or stock id
- **Shares**: required, greater than 0
- **Date**: required, cannot be in the future
- **Price**: required, greater than 0
- **Owner**: required, references user id 

## Countries

### Data
- **Country name**: the country name
- **Country code**: short country code (~2-3 letters)
- **country currency**: the primary currency of the country

### Validation
- **Country name**: required, not blank
- **Country code**: unique
- **country currency**: references currency id

## Currencies

### Data
- **currency name**: the currency name
- **currency code**: the FX currency code
- **value/usd**: value of currency to the us dollar

### Validation
- **currency name**: required, not null
- **currency code**: unique
- **value/usd**: greater than 0

## Stock exchange

### Data
- **Exchange name**: the exchanges name
- **exchange code**: the shorthand code for the exchange 
- **TimeZone**: exchange timezone

### Validation
- **Exchange name**: required, not blank
- **exchange code**: unique
- **TimeZone**: required, valid timezone

## Portfolio

### Data
- **user id**: the portfolio users id
- **Stocks**: the stocks in the portfolio
- **Account type**: the account type

### Validation
- **user id**: required, references users id
- **Stocks**: 
- **Account type**: required,  account type enum


## Technical Requirements

- Three layer architecture
- Data stored in a SQL databse.
- Repositories should throw a custom exception, never file-specific exceptions.
- Repository and service classes must be fully tested with both negative and positive cases. Do not use your "production" data file to test your repository.
- account type enum with the investment account (retirement, investing).
- Asset Type enum with the type of stock 
- Country enum with country names and codes
- Currency enum with currency signs and names

## Package/Class Overview

```
src
├───main
│   └───java
│       └───learn
│           └───solar
│               │   App.java                      -- app entry point
│               │
│               ├───data
│               │       DataException.java        -- data layer custom exception
│               │       PanelFileRepository.java  -- concrete repository
|               |       
│               │       PanelRepository.java      -- repository interface
│               │
│               ├───domain
│               │       PanelResult.java          -- domain result for handling success/failure
│               │       PanelService.java         -- panel validation/rules
│               │
│               ├───models
│               │       Material.java             -- enum representing the 5 materials
│               │       Panel.java                -- solar panel model
│               │
│               └───ui
│                       Controller.java           -- UI controller
│                       View.java                 -- all console input/output
│
└───test
    └───java
        └───learn
            └───solar
                ├───data
                │       PanelFileRepositoryTest.java    -- PanelFileRepository tests
                │       PanelRepositoryTestDouble.java  -- helps tests the service, implements PanelRepository
                │
                └───domain
                        PanelServiceTest.java           -- PanelService tests
```

## Class Details

### App
- `public static void main(String[])` -- instantiate all required classes with valid arguments, dependency injection. run controller

### data.DataException

Custom data layer exception.

- `public DataException(String, Throwable)` -- constructor, Throwable arg is the root cause exception

### data.PanelFileRepository
- `private String filePath`
- `public List<Panel> findBySection(String)` -- finds all Panels in a section, uses the private `findAll` method
- `public Panel add(Panel)` -- create a Panel
- `public boolean update(Panel)` -- update a Panel
- `public boolean deleteById(int)` -- delete a Panel by its id
- `private List<Panel> findAll()` -- finds all Panels in the data source (file), does not need to be public
- `private String serialize(Panel)` -- convert a Panel into a String (a line) in the file
- `private Panel deserialize(String)` -- convert a String into a Panel

### data.PanelRepository (interface)

Contract for PanelFileRepository and PanelRepositoryTestDouble.

- `List<Panel> findBySection(String)`
- `Panel add(Panel)`
- `boolean update(Panel)`
- `boolean deleteById(int)`

### domain.PanelResult
- `private ArrayList<String> messages` -- error messages
- `private Panel panel` -- an optional Panel
- `public boolean isSuccess()` -- calculated getter, true if no error messages
- `public List<String> getMessages()` -- messages getter, create a new list
- `public Panel getPanel()` -- Panel getter
- `public void setPanel(Panel)` -- Panel setter
- `public void addMessage(String)` -- adds an error message to messages

### domain.PanelService
-  `private PanelRepository repository` -- required data dependency
-  `public PanelService(PanelRepository)` -- constructor
-  `public List<Panel> findBySection(String)` -- pass-through to repository
-  `public PanelResult add(Panel)` -- validate, then add via repository
-  `public PanelResult update(Panel)` -- validate, then update via repository
-  `public PanelResult deleteById(int)` -- pass-through to repository
-  `private PanelResult validate(Panel)` -- general-purpose validation routine

### models.Material

An enum with five values: multicrystalline silicon, monocrystalline silicon, amorphous silicon, cadmium telluride, copper indium gallium selenide. Can use industry abbreviations or full names.

### models.Panel
- `private int id`
- `private String section`
- `private int row`
- `private int column`
- `private int installationYear`
- `private Material material`
- `private boolean tracking`
- Full getters and setters
- override `equals` and `hashCode`

### ui.Controller
- `private View view` -- required View dependency
- `private PanelService service` -- required service dependency
- `public Controller(View, PanelService)` -- constructor with dependencies
- `public void run()` -- kicks off the whole app, menu loop
- `private void viewBySection()` -- coordinates between service and view to display panels in a section
- `private void addPanel()` -- coordinates between service and view to add a new panel
- `private void updatePanel()` -- coordinates between service and view to update a panel
- `private void deletePanel()` -- coordinates between service and view to delete a panel

### ui.View
- `private Scanner console` -- a Scanner to be used across all methods
- `public int chooseOptionFromMenu()` -- display the main menu and select an option, used to Controller.run()
- `public void printHeader(String)` -- display text to the console with emphasis
- `public void printResult(PanelResult)` -- display a PanelResult with either success or failure w/ messages included
- `public void printPanels(String sectionName, List<Panel>)` -- display panels in a section with the section's name
- `public Panel choosePanel(String sectionName, List<Panel>)` -- choose a panel from a list of options (useful for update and delete)
- `public Panel makePanel()` -- make a panel from scratch, used in Controller.addPanel
- `public Panel update(Panel)` -- accept and existing Panel, update it, and return it, used in Controller.updatePanel
- `public String readSection()` -- reads a section name, used in Controller: viewBySection, updatePanel (must search first), and deletePanel
- `private String readString(String)` -- general-purpose console method for reading a string
- `private String readRequiredString(String)` -- general-purpose console method for reading a required string
- `private int readInt(String)` -- general-purpose console method for reading an integer
- `private int readInt(String, int min, int max)` -- general-purpose console method for reading an integer with a min and max
- `private Material readMaterial()` -- domain-specific console method for choosing a Material

## Steps

1. Create a Maven project.
2. Add jUnit 5, Jupiter, as a Maven dependency and refresh Maven
3. Create packages.
4. Create the `Panel` model.
5. Create the `Material` enum.
6. Create the data layer's custom `DataException`
7. Create the `PanelFileRepository` class.

    All methods should catch IOExceptions and throw `DataException`.

    - add the filePath field and create a constructor to initialize the field
    - generate tests for `PanelFileRepository`, should be located in `src/test/java/learn/solar/data/PanelFileRepositoryTest`
    - create a `data` directory in the project root. add test, seed, and production data files
    - implement `findAll`, `serialize`, and `deserialize`. these are all private method. may be useful to make `findAll` public temporarily and test it quick.
    - implement `findBySection`, it uses `findAll`. test it naively (no known-good-state for now)
    - implement `add`
    - improve tests by establishing known-good-state with `@BeforeAll`
    - test `add`
    - implement `update`
    - test `update`
    - implement `deleteById`
    - test `deleteById`

8. Extract the `PanelRepository` interface (IntelliJ: Refactor -> Extract Interface) from `PanelFileRepository`.
9. Create `PanelResult`.
10. Create `PanelService`.

    - add a `PanelRepository` (interface) field with a corresponding constructor
    - generate tests for `PanelService`
    - create `PanelRepositoryTestDouble` to support service testing, this test class implements `PanelRepository`
    - implement `findBySection` and test, implement just enough code in `PanelRepositoryTestDouble` to enable service testing
    - implement `add` and test, requires validation
    - implement `update` and test, requires validation
    - implement `deleteById` and test

11. Create `View`

    - add `Scanner` field
    - create read* methods: `readString`, `readRequiredString`, `readInt`, `readInt` (with min/max)

12. Create `Controller`

    - add fields for service and view with corresponding constructor
    - add a `run` method

13. Create `App` and the `main` method.

    - instantiate all required objects: repository, service, view, and controller
    - run the controller

14. Work back and forth between controller and view.

    Run early and often. Add `System.out.println`s as breadcrumbs in controller, but don't forget to remove them when development is complete.

    Determine the correct sequence for service calls and view calls. What is the order?

    - implement `chooseOptionFromMenu` and `printHeader` in view
    - use them in the controller's `run`
    - implement `viewBySection` in controller, complete required view methods: `readSection`, `printPanels`
    - implement `addPanel` in controller, complete required view methods: `makePanel`, `readMaterial`, `printResult`
    - implement `updatePanel` in controller, complete required view methods: `choosePanel`, `update`
    - implement `deletePanel` in controller, complete required view methods (`deletePanel` can re-use `choosePanel`)

## Controller Perspectives

### View Panels by Section
1. collect section name from the view
2. use the name to fetch panels from the service
3. use the view to display panels

### Add a Panel
1. collect a complete and new panel from the view
2. use the service to add the panel and grab its result
3. display the result in the view

### Update a Panel
1. collect section name from the view
2. use the name to fetch panels from the service
3. display the panels in the view and allow the user to choose a panel (if no panel selected, abort)
4. update panel properties (setters) in the view
5. use the service to update/save the panel and grab its result
6. display the result in the view

### Delete a Panel
1. collect section name from the view
2. use the name to fetch panels from the service
3. display the panels in the view and allow the user to choose a panel (if no panel selected, abort)
4. use the service to delete the panel by its identifier
5. display success or failure in the view