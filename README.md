# Getting Started

* Download the project

* Create the table
  
CREATE TABLE IF NOT EXISTS events </br>
  ( </br>
  id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),</br>
  description character varying(500) NOT NULL, </br>
  organizer character varying(100) NOT NULL, </br>
  topic character varying(100) NOT NULL, </br>
  event_time timestamp without time zone NOT NULL, </br>
  place character varying(100) NOT NULL, </br>
  CONSTRAINT events_pkey PRIMARY KEY (id) </br>
  ); </br>

* Change application.properties to your values

db.username = postgres </br>
db.password=varya12354 </br>
db.url=jdbc:postgresql://localhost:5432/test (url for your database with events table)

* Run the application and go to the http://localhost:8084/swagger-ui/ and check:)



