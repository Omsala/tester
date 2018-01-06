hur man installerar och lägger upp databasen i ubuntu.
1. se till att du har den senaster varrianten av Erlang /otp, MySql-server och amnesia
2. Installera mysql server och ange root password till 1337 eller dyl (om annat än 1337 används så måste man ändra i raden nedan)
3. Installera Amnesisa genom att zipa upp filen kör ./configure, där efter make
sedan sudo make install (nu har du uppdaterat datorm med Amnesia paktet)
4. compelera air_database.erl i erl och kör sedan denna kod i compilern
amnesia:db_tool(air_database, [{make_hdr, "."},make_db, {dba_user, "root"}, {dba_password, "1337"}]).
om du har valt ett annat lösenord än 1337 för root se till att ändra detta på denna punkt     |
5. om allt har gått som det ska kan du nu stänga erl compilern och kolla om databasen är uppe.
förslagsvis med programmet emma eller vi terminal genom att skirva mysql -u root -p
sedan skriver du 
      mysql> use air_database;
      mysql> describe flightno;
  Då kommer flightno tabellen att visas.
tryck ctrl+d för att avsluta.
6.För att populera databasen kör då test_databas.erl i compilern.

ta Da ALLLLEEEEÅÅÅÅÅ du e klar!



The main database consists of 3 diffrent database tables as the following

flightno
---------
This table handels all the diffrent flights, it contains the following columns

Id, ( row id)
Flight_no
flight_to
flight_from
flight_take_off_time


passengerno
--------------------
main customer datatabel it contains all the informatio of the customers

id key
passenger_no
name
address
payment_info
email

seats
-------------------
Seat infomation for alla the flights


id key
flightno
seat
passengerno
seat_price
seat_res
seat_booked
