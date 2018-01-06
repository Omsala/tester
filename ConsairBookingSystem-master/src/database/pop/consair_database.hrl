
-record (user, {
	id = null,
	user_name = null,
	user_password = null,
	user_class = null,
	user_email = null}).

-record (airport, {
	id = null,
	iata = null,
	airport_name = null}).

-record (flights, {
	id = null,
	airport = null,
	arrival_point = null,
	departure_date = null,
	arrival_date = null,
	flight_nr = null}).

-record (seats, {
	id = null,
	flights = null,
	class = null,
	user = null,
	window = null,
	aisle = null,
	row = null,
	col = null,
	price = null,
	lock_s = null}).

