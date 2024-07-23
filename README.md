<h2>“Ticket to ride“ application</h2>

<h3>Functionality:</h3>
The application provides an API with two endpoints:
1. Allow to calculate price and find the most optimal travel between two towns.
2. Check if traveller has enough money and save the ticket in the storage if he/she does.
Authentication & authorization: Traveller registration endpoint.
3. When starting the application, the Configuration loads a file with the primary list of roads (routes.txt),
   according to Roads Schema into the database.


<h3>Technologies:</h3>
     Java 21, Spring Boot, Spring Data, Spring Security, JUnit 5, Mockito, PostgreSQL, Maven


<h3>API endpoints:</h3>

_Find a ticket_
* Public endpoint, that allows to calculate price and find the most optimal travel between two towns (points).
*    URL: /api/find_ticket 
*  Method: Get
* Query parameters: from, to 
* Response example:
<br>{
<br> "id": null,
<br> "routesChain": "LONDON - READING - SWINDON - BRISTOL",
<br> "price": 25.00,
<br> "segments": 7
<br>}

_Save a ticket_
* Private endpoint. Check if traveller has enough money and save the ticket in the storage if he/she does.
*  URL: /api/ticket
*   Method: Post
*    Query parameters: amount
*    Request example:
   <br>{
    <br>"id": null,
    <br>"routesChain": "LONDON - READING - SWINDON - BRISTOL",
   <br> "segments": 7,
   <br> "price": 25
   <br>}  
*    Response example:
   <br>{
    <br>"result": "success",
    <br>"change": "1.00",
    <br>"currency": "GBP"
  <br> }

_Traveller registration_
 *    URL: /register/traveller
 *    Method: Post
 *    Request example:
   <br> {
   <br> "username": "traveller",
   <br> "password": "12345"
   <br> }
 *    Response example:
   <br> {
    <br>"id": 1,
    <br>"username": "traveller",
    <br>"password": "$2a$10$IuYPFQrnPXyQ792lNwSliupQiCJel4J4KyhiyUYx8evlEVAtQ2h7S"
   <br> }
