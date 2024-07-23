<h2>“TICKET TO RIDE“ application</h2>

<h3>Purpose:</h3>
To develop an application which allows to find the most optimal travel between two towns and to save the ticket in the storage if traveller has enough money.

<h3>Technologies:</h3>
     Java 21, Spring Boot, Spring Data, Spring Security, JUnit 5, Mockito, PostgreSQL, Maven

<h3>How to use:</h3>
After the cloning this repository you have to configure DataBase properties in application.properties file;
<br>When starting the application, the Configuration loads primary list of roads from the routes.txt file,
according to Roads Schema into the database;
<br>To find an optimal travel go to /api/find_ticket end point and specify point From and point To;
<br>The price of the travel through:
<li>1 segment is 5 GBP</li>
<li>2 segments is 7 GBP</li>
<li>3 segments is 10 GBP</li>
 To buy a ticket you have to provide your selected ticket and amount you going to pay at the /api/ticket endpoint;
<br>It is necessary to be authenticated user to buy a ticket at the /api/ticket endpoint;
<br>To register follow to register/traveller endpoint;
<br>Task description you can find in TicketToRide.pdf file.

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
