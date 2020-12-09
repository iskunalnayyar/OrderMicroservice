## Order Microservice

### Database Schema

- The schema provided was all in 1 table in the legacy schema, so I decided to make it Normalized, and divide up the data into its respective tables.
- I split the schema into 5 tables (Order, Item, Billing_details, Shipping_details, Payment) with associations defined properly to meet the requirements and be a foundation for the APIs.
#### Order 
- The first table is the `Order` table with Customer details (like `customer_name`, `customer_email`, `customer_address`), Order `status`, additional `notes`, system generated `created_date`, and `modified_date` as part of identifiable data and metadata living on the Order itself.
- This `Order` table is connected to `Item` table in a 1-to-many relationship.
#### Item
- `Item` table has `item_name`, `item_qty`, `order_shipping_charges`, `order_subtotal`, `order_tax`.
- An item has a 1-to-1 relationship with `billing_details` table, and with `shipping_details` whereas it can have multiple `payments`. 
#### Payment 
- Having a 1-to-many relationship with Item satisfies the requirement of allowing the user use multiple `payment_methods` which can be set up using a controller function to calculate the split amount using a service layer method in the future.
    - Also, stored is the Payment_method (which can be modified to use only Enums of values 'CreditCard', 'DebitCard' in the future),
    - Along with `payment_amount`, `payment_date`, `payment_confirmation_number`, `payment_card_number` and `payment_cvv` for now.
#### Billing_details
- There will be 1 billing detail record for an item.
- Stores details of the address the order is to billed at using fields like `address_line_1`, `address_line_2`, `state`, `city`, `zipcode`.
 
#### Shipping_details
- There will be 1 shipping details record for an item.
- There can be more than 1 `shipping_method`s based on constraints on the column in this table to check if the values are from an enum list of `In-store pickup`, `Curbside delivery`, `Ship to home`, `Third-party delivery`, etc.
- Stores details of the address the order is to shipped at using fields like `address_line_1`, `address_line_2`, `state`, `city`, `zipcode`.
- We can add a functionality to keep this the same as the billing address or vice versa as an added feature in the future.

<img width="860" alt="Screen Shot 2020-12-09 at 1 50 26 AM" src="https://user-images.githubusercontent.com/47041461/101615138-a2a39400-39c2-11eb-85f9-bef75907f6ec.png">
 
  
### Installations

- Install Java (SDK 14 or later) (https://www.oracle.com/java/technologies/)
- Download PostgreSQL (https://www.postgresql.org/download/)
- Download Docker (optional) (https://www.docker.com/products/docker-desktop)
- Install Postman for testing API endpoints (https://www.postman.com/downloads/)
- We use Java, SpringBoot, JPA, Hibernate, PostgreSQL, Docker, Swagger, Postman

### Setup
- Open this repository in your local IDE
- Right click the root folder `order-service` in the IDE, go to `maven` then `download sources & documentation` to install dependencies
- Update the username & password in the `application.properties` file.
- Note the database name as `orderservicedb`
- You will need to setup the database in your PostgreSQL instance
    - Open terminal and use this command to start a PostgreSQL server `sudo -u postgres psql`
    - You could also install the PostgreSQL GUI instead of using CLI
    - Run the command to create a new database `create database orderservicedb;`
    - Then choose this database `\c orderservicedb;`
    - You can see the table definitions by `\dt`
    - And query to see the records in the tables going forward. 
    - We are also going to test our API endpoints using a REST Client like Postman.
- To run this application in a Docker container (Work in progress):
    - Make sure `Dockerfile` and `docker-compose` are in the root directory of the main SpringBoot Application.
    - Use command `docker-compose up` to run our application in containarized mode.
    - There should be 1 instance for PostgreSQL and 1 for the SpringBoot application, and the Docker should interface between the two for us. 
    - Currently facing issues with connectivity on port 5432 on my local machine.
    - A follow up would be to spend more time to fix this.
       
### Microservice & API Design
- Firstly, deciding the schema helped in laying the foundations of the applications as per the requirements.
- Breaking down the legacy schema into a more normalized schema will help with data maintenance, scalability, associations, and building API on top of this 5 table schema.
- Database schema section talks about how the database requirements are met, let's talk about the microservice.
- There is 1 microservice for **Order Processing** that allows RESTful clients to work with single order objects.

- The layers in the application are as follows Controller uses Service layer, which connects to Repository layer that extends JPARepository
- For simplicity purposes, I have implemented two controllers OrderController and ItemController and their respective service and repository classes. I've added the functionality for items to give the user the ability to add/delete items from an order. This is an assumption, and can very well be refactored if needed.
- A follow up would be to get requirements for a way to add items inside an Order, add Payment, Shipping and Billing Details for an item, and implement the design in that way.


- In SpringBoot, we use annotations to set up the framework to do the behind the scenes wiring for us.
- We use @Entity while defining the entity on a modal to define a table, @Column for columns, and we can add Constraints here as well, on the datatype, size, values etc (as a follow up).
- Mapping of OneToMany, OneToOne, and ManyToMany is also defined as per the schema discussed above.
- All data model properties have their public getters and setters.
- @RestController annotation is used when declaring the controller to be used as a REST resource, in this case both OrderController,and ItemController are RESTControllers.
- Dependency Injections by means of using @Autowired is done to pull in service and repository layers in the controllers.
 
 
- The root of the RestController is ("/api") as that is required for Swagger.
- Our OrderController, exposes the following endpoints:

1. GET: **findAllOrders** ("/orders"):
    - This does not take any parameters, and returns a list of orders in the `Order` table.
2. GET: **findByOrderId** ("/orders/{orderId"}):
    - This route takes in an `orderID` and returns the Order associated with that OrderId. Since this is the PRIMARY KEY on the table, it is already indexed for quicker reads.
3. POST: **createOrder** ("/orders/create"):
    - This takes in the **RequestBody** from the POST request and saves that order.
    - Save method in the OrderService class, will set the `createdDate` to UTC.now using the **Instant** class
    - It will also save the `Status` on the order as **`RECEIVED`**.    
4. GET: **cancelOrder** ("/orders/cancel/{orderId}")
    - This method will update the `Status` of the existing order and set it to `CANCELED`. It should throw an error if the order is not present meaning not found.
5. GET: **deleteOrder** ("/orders/delete/{orderId}")
    - This endpoint receives the orderId, looks up the order, and if it exists, deletes that order record.
    - Cascade option is set to `Cascadetype.ALL` to ensure that FOREIGN KEY constraints are met and all associated records from other tables are deleted as well.

- Our ItemController, exposes the following endpoints:

1. GET: **findAllItems** ("/items"):
    - This does not take any parameters, and returns a list of items in the `Item` table.
    - Since there is a FOREIGN_KEY of `order_id` in Item table, we can see which item is from which order.
    - Again, we can have multiple Items for the same order.    
2. GET: **getItemsByOrderId** ("/items/{orderId}"):
    - This route takes in an `orderID` and returns the Items associated with that OrderId. 
    - We can have multple items in one order.   
3. GET: **getItem** ("/items/search/{itemId}"):
    - This is set up in case we wish to search by `itemId` which is accepted through the parameterized URL.    
4. POST: **addItemsToOrder** ("/items/add/{orderId}"):
    - This takes in the **RequestBody** from the POST request which is the Item object.
    - It looks up the OrderId using the findById function of the OrderService, and if the order is present, maps this Item to that order, and saved the Item in the Item table with the associated OrderID.   
5. GET: **deleteOrder** ("/items/delete/{itemId}")
    - This endpoint receives the itemId, looks up the item, and if it exists, deletes that item record.
    - Cascade option is set to `Cascadetype.ALL` to ensure that FOREIGN KEY constraints are met and all associated records from other tables are deleted as well.


### Testing using Postman

- We can set up Postman Client to test our REST endpoints.

- CreateOrder
    - Make sure to put the necessary fields in the Request Body like:
```
{
    "customerId": "1",
    "customerName": "Customer 1",
    "customerEmail": "customer1@gmail.com"
    
}
```

- AddItemToOrder

![add-item-to-order](https://user-images.githubusercontent.com/47041461/101615656-3b3a1400-39c3-11eb-9972-7e0b0c7f6531.png)

- GetAllOrders

![get-all-orders](https://user-images.githubusercontent.com/47041461/101615660-3c6b4100-39c3-11eb-8d5a-076e0e7d0464.png)

- GetAllItems

![get-all-api-items](https://user-images.githubusercontent.com/47041461/101615653-3aa17d80-39c3-11eb-893e-88779bf9c6d0.png)

- DeleteItemById

![delete-item-id-3](https://user-images.githubusercontent.com/47041461/101615644-383f2380-39c3-11eb-8407-aca138e61c2a.png)

- CancelOrderById

<img width="1369" alt="Screen Shot 2020-12-08 at 8 03 03 PM" src="https://user-images.githubusercontent.com/47041461/101615864-7c322880-39c3-11eb-845a-0f9785fbb6e2.png">

- Check Cancelled order by using GetAllOrders

<img width="1347" alt="Screen Shot 2020-12-08 at 8 03 30 PM" src="https://user-images.githubusercontent.com/47041461/101615914-8eac6200-39c3-11eb-9e26-ebfb2c80ba28.png">

- GetApiDocumentation:
    - Sends a GET request to http://localhost:8080/v2/api-docs to get the Swagger generated documentation.
    - An example of a CreateOrder endpoint as described by Swagger.
    ```
  "/api/orders/create": {
              "post": {
                  "tags": [
                      "order-controller"
                  ],
                  "summary": "createOrder",
                  "operationId": "createOrderUsingPOST",
                  "consumes": [
                      "application/json"
                  ],
                  "produces": [
                      "*/*"
                  ],
                  "parameters": [
                      {
                          "in": "body",
                          "name": "order",
                          "description": "order",
                          "required": true,
                          "schema": {
                              "$ref": "#/definitions/Order"
                          }
                      }
                  ],
                  "responses": {
                      "200": {
                          "description": "OK",
                          "schema": {
                              "$ref": "#/definitions/Order"
                          }
                      },
                      "201": {
                          "description": "Created"
                      },
                      "401": {
                          "description": "Unauthorized"
                      },
                      "403": {
                          "description": "Forbidden"
                      },
                      "404": {
                          "description": "Not Found"
                      }
                  }
              }
          }
  ``` 
    
    
    
- You can also test the local PostgreSQL database. Here are a few query results: 



![initialize-db](https://user-images.githubusercontent.com/47041461/101616137-d7fcb180-39c3-11eb-8086-bd4f5dd4582f.png)

![3](https://user-images.githubusercontent.com/47041461/101616244-01b5d880-39c4-11eb-9f16-b5e823ae7ad7.png)

![2](https://user-images.githubusercontent.com/47041461/101616245-024e6f00-39c4-11eb-9ad2-155a11ef3158.png)


### API Documentation using Swagger UI
- I've used Swagger to document all HTTP endpoints, routes, their HTTP definitions, and the data schema.
- The package used is `springfox-boot-starter` which includes both the UI and the local route for API documentations.
- Earlier `springfox-swagger2` & `springfox-swagger-ui` both were needed to be injected but the new package combines the functionalities of these two.
- You can use the request to `http://localhost:8080/v2/api-docs` in Postman or -
- Go to `http://localhost:8080/swagger-ui/` to view and test the API endpoints as shown below
 
- There are 2 controllers wired up: 1 is the main one for Orders and the other is to manage Items

!<img width="1245" alt="Screen Shot 2020-12-08 at 8 53 43 AM" src="https://user-images.githubusercontent.com/47041461/101616422-31fd7700-39c4-11eb-8fa0-22e609c6943a.png">
<img width="1159" alt="Screen Shot 2020-12-08 at 8 53 58 AM" src="https://user-images.githubusercontent.com/47041461/101616431-345fd100-39c4-11eb-840c-13aa68030920.png">
<img width="1222" alt="Screen Shot 2020-12-08 at 8 54 09 AM" src="https://user-images.githubusercontent.com/47041461/101616440-375ac180-39c4-11eb-971c-99b1cd71cfe3.png">

- Here is the schema of the Data Models which show that we use entire objects to be sent to their parent class.
- An order will have a List of Items, an Item will have an object of BillingDetails, ShippingDetails and a list of Payment objects.

<img width="913" alt="Screen Shot 2020-12-08 at 8 54 33 AM" src="https://user-images.githubusercontent.com/47041461/101616451-39bd1b80-39c4-11eb-8961-e502f4543f11.png">

### Unit & Integration Testing
- I added some Unit and Integration tests to test the repository and service layers
- I used mocking with MockBean, Mockito and also using @WebMvcTest approach.
- I added a test to `whenValidOrderId_thenOrderShouldBeFound`
- By mocking `orderService` Bean and `OrderRepository` using @MockBean, we can setup the test to check a valid order should be returned when we test the function findById on OrderRepository.
 
```@Before
       public void setUp() {
           Order order = new Order(OrderId, "1", "Alex", "alex@gmail.com");
   
           Mockito.when(orderRepository.findById(order.getOrderId())).thenReturn(java.util.Optional.of(order));
       }
       
       @Test
       public void whenValidOrderId_thenOrderShouldBeFound() {
           Optional<Order> found = orderService.findByOrderId(OrderId);
   
           assertThat(found.get().getOrderId()).isEqualTo(OrderId);
       }
```

- Another Application test currently setup and passing is testing the GetOrders method.
- It should return a 200 HTTP OK status response, and the APPLICATION/JSON data should have a customer name matching the one in the order object that was sent.

```
@Test
	public void givenOrders_whenGetOrders_thenReturnJsonArray()
			throws Exception {

		// given
		Order order1 = new Order(OrderId, "1", "Alex", "alex@gmail.com");
		List<Order> allOrders = Collections.singletonList(order1);

		// when
		given(orderService.getAllOrders()).willReturn(allOrders);

		// then
		mvc.perform(get("/api/orders")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].customerName", is(order1.getCustomerName())));
	}
```

- I would follow up with adding more tests, and adding auto-configured tests using something like @RestClientTest.

### Logging
- The tool used for Logging is from Lombok's Slf4j package dependency to help us with logging information that can be helpful while debugging and troubleshooting problems faced on stage and production servers.
- Often times, to answer queries of issue tickets from customers, we need to access logs which makes this feature very important.
- The logs display the object/variable values at the instance of logging.
- It would help in identifying records from the database quickly using `orderId`, `customerId`, `itemId`, etc as per these application logs.
- We can also setup Azure/Dynatrace logs and monitoring when using production servers.

### Seed Data (Optional)
- Currently not set up but we can setup the local database to be inialized and auto configured with seed data for local development & testing purposes.
- As a follow up there can be a script that reset and preloads local database.
- It will need changes to the application.properties to enable hibernate and not JPA to load the schema
- Here are two snippets of what the `schema-postgres.sql` and Seed data `data-postgres.sql` look like. 
- Of course the files have the complete versions in them.
```
drop table IF EXISTS orders cascade;
create TABLE orders(
    order_id serial PRIMARY KEY NOT NULL,
    customer_id VARCHAR(255),
    customer_name VARCHAR(50),
    customer_email VARCHAR(20),
    status VARCHAR(15),
    notes VARCHAR(255),
    created_date DATE,
    modified_date DATE
    
    CONSTRAINT chk_status CHECK (status in ('shipped','received','processing','canceled'))
);

drop table IF EXISTS items cascade;
create TABLE items(
    item_id serial PRIMARY KEY NOT NULL,
    order_id INTEGER,
    item_name VARCHAR(255),
    item_qty INTEGER,
    order_shipping_charges DECIMAL,
    order_subtotal DECIMAL,
    order_total DECIMAL,
    order_tax DECIMAL

    CONSTRAINT fk_order
      FOREIGN KEY(order_id)
	  REFERENCES orders(order_id)
	  ON DELETE CASCADE
);


drop table IF EXISTS shipping_details cascade;
create TABLE shipping_details(
    shipping_details_id serial PRIMARY KEY NOT NULL,
    item_id INTEGER,
    shipping_method VARCHAR(25),
    address_line_1 VARCHAR(255),
    address_line_2 VARCHAR(255),
    city VARCHAR(255),
    state VARCHAR(255),
    zipcode VARCHAR(5)

    CONSTRAINT chk_shipping_method CHECK (shipping_method in ('in-store pickup','curbside delivery','ship to home','third-party delivery')),

    CONSTRAINT fk_items
      FOREIGN KEY(item_id)
	  REFERENCES items(item_id)
	  ON DELETE CASCADE

);
```

```
INSERT INTO orders(customer_id, customer_name, customer_email, notes, created_date)
VALUES
('1', 'Customer 1', 'customer1@gmail.com', 'internal note 1', current_date)
('2', 'Customer 2', 'customer2@gmail.com', 'internal note 2', current_date),
('3', 'Customer 3', 'customer3@gmail.com', 'internal note 3', current_date),
('4', 'Customer 4', 'customer4@gmail.com', 'internal note 4', current_date),
('5', 'Customer 5', 'customer5@gmail.com', 'internal note 5', current_date);


INSERT INTO items(order_id, item_name, item_qty, order_shipping_charges, order_subtotal, order_tax, order_total)
VALUES
(1,'Witcher 3', 1, 11.75, 57.05, 3.20, 83.0),
(2,'Call of Duty', 2, 10.75, 80.05, 3.50, 100.0),
(1,'Spiderman', 1, 16.75, 97.05, 2.20, 123.5);

```

