% %------------------------
% % @title sql server quary file
%% @version 1.0.0
%% {@date}
%% @author Oskar Ahlberg
%% @doc This file contains all funtions called from the booking_agent file ea. all the sql server calls and some dev funtions
%% @end
%%------------------------

-module(get_database).
-export ([get_user_from_seat/1,
	  get_user_from_db/1,
	  get_all_users_from_db/0,
	  get_airport_from_db/0,
	  get_filter_seat_from_user_id/1,
	  get_airport_from_db_filter/1,
	  get_airport_id_from_iata/1,
	  get_airport_iata_from_id/1,
	  get_flights_from_to_airport/2,
	  get_flights_date_to_from_airport/3,
	  get_flight_from_db_f/1,
	  get_seats_from_flight/1,
	  get_seats_from_fid/1,
	  get_airport_iata_to_id/1,
	  get_flights_details_from_flight/1,
	  get_seats_id_from_db/1,
	  get_flight_from_db/0,
	  get_all_flights_from_airport/1,
	  get_user_from_seats/1,
	  get_seat_ids/0,
	  update_seat_lock/2,
	  update_seat_lock_user/2,
	  update_seat_user/2,
	  rollback_booking/1,
	  add_new_user/4,
	  add_new_airport/2,
	  remove_airport/1,
	  remove_user/1]).

-include ("consair_database.hrl").
-include("amnesia.hrl").

%%-include_lib("amnesia/include/amnesia_db_def.hrl").
%%-compile(export_all).

%% @doc get_user_from_seat
%% Input: Seat_id
%% Example: 1
%% Output: a userID
%% @end

get_user_from_seat(Seat_id) ->
    {ok, Pid} = amnesia:open(consair_database),
    {ok, [{_,_,_,_,User,_,_,_,_,_,_}]} = amnesia:fetch(Pid, seats, {"id=$1", [Seat_id]}),	
    {ok, User}.
    	

%% @doc - get_user_from_db
%% Input: Username
%% Example: Kalle
%% Ouput: A tuppel of the of the users line in the server.
%% @end

get_user_from_db(Usernamne)->
    {ok,Pid}=amnesia:open(consair_database),
    amnesia:fetch(Pid, user,{"user_name = $1",[Usernamne]}).

%% @doc - get_all_users_from_db
%% Input: -
%% Example:-
%% Output: {ok,[{user,1,"PENDING NEW BOOKING","n/a",1,"n@a.se"}}
%% A list of all users in the User tabel in the database
%% @end

get_all_users_from_db()->
    {ok, Pid} = amnesia:open(consair_database),
    amnesia:fetch(Pid, user).

%% @doc - get_airport_from_db
%% Input: -
%% Example:-
%% Output: {ok,[{airport,1,"ARN","ARLANDA"}}
%% A list of all the airports in the airport table of the database
%% @end

get_airport_from_db() -> 
    {ok,Pid}=amnesia:open(consair_database),
    amnesia:fetch(Pid,airport).



get_filter_seat_from_user_id(User_id)->
    {ok, Pid} = amnesia:open(consair_database),
    amnesia:fetch(Pid, seats, {"user_id = $1", [User_id]}).
    

%% @doc - get_airport_from_db_filter
%% Input: Airport_id
%% Example:1
%% Output: {ok,[{airport,1,"ARN","ARLANDA"}}
%% Returns the airport with the index of Airport_ID
%% @end

get_airport_from_db_filter (Airport_id) ->
    {ok,Pid}=amnesia:open(consair_database),    
    amnesia:fetch(Pid,airport, {"id = $1", [Airport_id]}).


%% @doc - get_airport_id_from_iata
%% Input: Aid (airport id)
%% Example: 1 
%% Output: ARN
%% @end

get_airport_id_from_iata(Aid)->
    {ok, Pid} = amnesia:open(consair_database),
    case amnesia:fetch(Pid, airport, {"id=$1", [Aid]}) of
	{ok,[{_,_,IATA,_}]} ->
	    {ok, IATA};
	{ok, []} ->
	    {error, no_such_flight};
	{error, Error} ->
	    {error, Error}
    end.

%% @doc - get_airport_id_from_iata
%% Input: Aid (airport id)
%% Example: 1 
%% Output: ARN
%% @end

get_airport_iata_from_id(Aid)->
    {ok, Pid} = amnesia:open(consair_database),
    case amnesia:fetch(Pid, airport, {"iata=$1", [Aid]}) of
	{ok,[{_,ID,_,_}]} ->
	    {ok, ID};
	{ok, []} ->
	    {error, no_such_flight};
	{error, Error} ->
	    {error, Error}
    end.


%% @doc - get_airport_from_to_airport
%% Input:From,To 
%% Example: 4, 3
%% Output: A flight from Airport to Airport
%% @end


get_flights_from_to_airport (From,To)->
    {ok, Pid} = amnesia:open(consair_database),
    case get_airport_id_from_iata(To) of
	{ok, IATA} -> 
	    amnesia:fetch(Pid, flights,{"airport_id=$1 and arrival_point=$2",[From,IATA]});
	{error, Error} ->
	    {error, Error}
    end.

%% @doc get_flights_date_to_from_airport
%% Input: From To Date
%% Example: 
%% Output: 
%% @end

get_flights_date_to_from_airport (From,To,Date)->
    {ok, Pid} = amnesia:open(consair_database),
    case get_airport_id_from_iata(To) of
	{ok, IATA} ->
	    amnesia:fetch(Pid, flights,{"airport_id=$1 and arrival_point=$2 and departure_date =$3",[From,IATA,Date]});
	{error, Error} ->
	    {error, Error}
    end.
    

%% @doc - get_flight_from_db_f
%% Input: Flight
%% Example: 2
%% Output: givs a flight of Flight_ID
%% @end

get_flight_from_db_f (Flight) ->
    {ok, Pid}=amnesia:open(consair_database),
    amnesia:fetch(Pid,flights, {"id = $1", [Flight]}).

%% @doc - get_seats_from_flight
%% Input: Flight
%% Example: 2
%% Output: A list of all the seats on Flight of Flight_nr
%% @end

get_seats_from_flight (Flight) ->
    {ok, Pid} = amnesia:open(consair_database),
    amnesia:fetch(Pid,[flights,?JOIN,seats],{"flights_id = $1",[Flight]}).   

%% @doc - get_seats_from_fid
%% Input: Flight
%% Example: 1
%% Output: a list of all seats with flight_id = flight
%% @end




get_seats_from_fid(F_id) ->
    {ok,Pid} = amnesia:open(consair_database),
    amnesia:fetch(Pid,seats,{"flights_id = $1",[F_id]}).


%% @doc - get_airport_iata_to_id
%% Input: IATA
%% Example: ARN
%% Output: the id of the airport with the corresponding IATA
%% @end
get_airport_iata_to_id(IATA)->
    {ok, Pid} = amnesia:open(consair_database),
    {_,[{_,AIR_ID,_,_}]} = amnesia:fetch(Pid,airport,{"iata=$1",[IATA]}),
    AIR_ID.


%% @doc - get_flights_details_from_flight
%% Input: F_id(Flight)
%% Example: 1
%% Output: All info from flight F_id
%% @end

get_flights_details_from_flight(F_id)->
    {ok,[{DB,Fid,Air_a,Air_b,Date_a,Date_b,Flight_name}]} = get_flight_from_db_f(F_id),
    Air_id_b=get_airport_iata_to_id(Air_b),
    {ok,[Airport_a]} = get_airport_from_db_filter(Air_a),
    {ok,[Airport_b]} = get_airport_from_db_filter(Air_id_b),
    {ok,SEATS} = get_seats_from_fid(F_id),
    {ok,[{DB,Fid,Airport_a,Airport_b,Date_a,Date_b,Flight_name,SEATS}]}.


%% @doc - get_seats_id_from_db
%% Input: Seat_id
%% Example: 3
%% Output: A tuppel of the row of seat_id in seats
%% @end
get_seats_id_from_db(Seat_id)->
    {ok, Pid} = amnesia:open(consair_database),
    amnesia:fetch(Pid,seats,{"id = $1",[Seat_id]}).

%% @doc - get_flight_from_db
%% Input: -
%% Example: -
%% Output: A list of all flights in the database
%% @end
get_flight_from_db()->
    {ok,Pid}=amnesia:open(consair_database),
    amnesia:fetch(Pid,flights).


%% @doc - get_all_flights_from_airport
%% Input: Airport
%% Example: ARN
%% Output: A list of all flights that Departs from Airport
%% @end
    
get_all_flights_from_airport(Airport)->
    {ok, Pid} = amnesia:open(consair_database),
    amnesia:fetch(Pid, [airport, ?JOIN ,flights],{"iata= $1",[Airport]}).

%% @doc - get_user_from_seats
%% Input: User
%% Example: Kalle
%% Output: a list of all the seat that user Kalle have booked
%% @end
get_user_from_seats(User) ->
    {ok, Pid} = amnesia:open(consair_database),
    amnesia:fetch(Pid, [seats, ?JOIN,user],{"user_name = $1",[User]}).
   
%% @doc -get_seat_ids
%% Input: -
%% Example:-
%% Output: Dumps the seats table
%% @end
get_seat_ids()->
    {ok, Pid} = amnesia:open(consair_database),
    amnesia:fetch(Pid,seats).

%%------------------------------------------------------------
%%Update and remove funtions for DB

%% @doc -update_seat_lock
%% Input: Seat_id, Lock
%% Example:-
%% Output: Updates the bookinglock of seat_id
%% @end

update_seat_lock(Seat_id, Lock) ->
    {ok, Pid} = amnesia:open(consair_database),
    {ok,[NewLock]} = amnesia:fetch(Pid, seats, {"id = $1",[Seat_id]}),
    NewL = NewLock#seats{lock_s = Lock },
    amnesia:update(Pid,NewL). %%Updates the lock

%% @doc -update_seat_lock_user
%% Input: User, Lock
%% Example: 1,2
%% Output: Updates the lock for the seats books or reserved for User in the seats table
%% @end
update_seat_lock_user(User_id, Lock) ->
    {ok, Pid} = amnesia:open(consair_database),
    {ok, Users_seats} = amnesia:fetch(Pid, seats, {"user_id = $1",[User_id]}),
    lists:map(fun(Seat) -> amnesia:update(Pid, Seat#seats{lock_s = Lock}) end, Users_seats).
    %% NewL = Users_seats#seats{lock_s = Lock },
    %% amnesia:update(Pid,NewL). %%Updates the lock


%% @doc -update_seat_user
%% Input: Seat_ID, User_ID
%% Example:2,2
%% Output: Update a seat of Seat_id with User of user_id
%% @end
update_seat_user(Seat_id, User_id) ->
    {ok, Pid} = amnesia:open(consair_database),
    {ok,[NewUser]} = amnesia:fetch(Pid, seats, {"id = $1",[Seat_id]}),
    NewU = NewUser#seats{user = User_id },
    amnesia:update(Pid,NewU). %%Updates the User

%% @doc -rollback_booking
%% Input: User_ID
%% Example:2
%% Output: Changes the User from User_ID to Pending Booking user and sets the lock of seat to Free
%% @end
rollback_booking(User_id)->
    case amnesia:open(consair_database) of
	{ok, Pid} ->
	    case amnesia:fetch(Pid, seats, {"user_id = $1",[User_id]}) of 
		{ok, [Roll]} ->
		    NewRoll = Roll#seats{user = 1, lock_s = 0},
		    amnesia:update(Pid,NewRoll);
		{ok, []} ->
		    ok;
		{error, Error} ->
		    {error, Error}
	    end;
	{error, Error} ->
	    {error, Error}
    end.


%% @doc - add_new_user
%% Input: Name, Password, UserClass, Email
%% Example:"Kalle", "Pass",1,"kalle@flyg.se"
%% Output: Adds a new user to the user table
%% @end
add_new_user (Name,Password,UserClass,Email) ->
    {ok, Pid} = amnesia:open(consair_database),
    amnesia:add_new(Pid, #user { user_name = Name,
			         user_password = Password,
				 user_class = UserClass,
				 user_email = Email}).

%% @doc - add_new_airport
%% Input: Iata,PlainText
%% Example:"ARN", "Arlanda international Airport"
%% Output: Adds a new Airport to the airport table
%% @end
add_new_airport(Iata,PlainText) ->
    {ok, Pid} = amnesia:open(consair_database),
    amnesia:add_new(Pid, #airport {iata = Iata,
				   airport_name = PlainText}).

%% @doc - remove_airport
%% Input: Iata
%% Example:"ARN"
%% Output: Removes the airport of Iata from the Airport tabel
%% @end
remove_airport(Iata) ->
    {ok, Pid} = amnesia:open(consair_database),
    {ok, [Del]} = amnesia:fetch(Pid, airport,{"iata = $1",[Iata]}),
    amnesia:delete(Pid,Del).


%% @doc - remove_user
%% Input: Name
%% Example:"Kalle"
%% Output: removes user of Name from user table
%% @end
remove_user(Name)->
    {ok,Pid} = amnesia:open(consair_database),
    {ok, [Del]} = amnesia:fetch(Pid, user,{ "user_name = $1",[Name]}),
    amnesia:delete(Pid,Del).



%%--------------------------____TEST-_________---------------------    


%% get_test()->
%%     {ok, Pid} = amnesia:open(consair_database),
%%     amnesia:fetch(Pid, [airport, ?JOIN, flights, ?JOIN, seats, ?JOIN, user]).



%% get_airport_from_db_filter_id (Airport_id) ->
%%     {ok,Pid}=amnesia:open(consair_database),    
%%     amnesia:fetch(Pid,airport, {"id = $1", [Airport_id]}).



%%{ok,[%% {user,5,"carl","asdasd",2,"carl@derp.se"}]}
%% start_booking(User,Seat_id)->
%%     {_,[{_,User_id,_,_,_,_}]} = get_database:get_user_from_db(User),
%%     get_database:update_seat_lock(Seat_id,1),
%%     get_database:update_seat_user(Seat_id,User_id),
%%     ok.
    



%% seat_details([Last_seat_ID]) ->
%%     [get_database:get_seats_id_from_db(Last_seat_ID)];
%% seat_details([Head_seat_ID | Tail_seat_ID]) ->
%%     lists:append([case get_database:get_seats_id_from_db(Head_seat_ID) of
%% 		      {ok,[{DB,S_id,Flight,Class,User,Window,Aisle,Row,Col,Price,2}]} ->    
%% 			  {ok,[{DB,S_id,Flight,Class,User,Window,Aisle,Row,Col,Price,1}]};
%% 		      Seat -> Seat
%% 		  end],						    
%% 		 seat_details(Tail_seat_ID)).



%% seat_details([Last_seat_ID],admin) ->
%%     [get_database:get_seats_id_from_db(Last_seat_ID)];
%% seat_details([Head_seat_ID | Tail_seat_ID],admin) ->
%%     lists:append([get_database:get_seats_id_from_db(Head_seat_ID)], seat_details(Tail_seat_ID,admin)).


%% seat_lock(Seat_ID) ->
%%     %% return availability of Seat_ID in Flight_ID
%%     Data = get_database:get_seats_id_from_db(Seat_ID),
%%     case Data of
%% 	{_,[{_,_,_,_,_,_,_,_,_,_,Lock_s}]} ->
%% 	    case Lock_s of
%% 		0->
%% 		    0;
%% 		1->
%% 		    1;
%% 		2 ->
%% 		    2
%% 	    end;
%% 	_->
%% 	    {error,no_seat}
%%     end.
	

%% test_login(Username, Password) ->
%%     {_,[{_,_,Name,Pass,Ulvl,_}]} = get_user_from_db(Username),

%%     case Username =:= Name of
%% 	true ->
%% 	    case Password =:= Pass of
%% 		true->
%% 		    case Ulvl =:= 1 of
%% 			true ->
%% 			    admin;
%% 			false ->
%% 			    user
%% 		    end;
%% 		false->
%% 		    wrong_pass
%% 	    end;
%% 	false ->
%% 	    no_user
%%     end.


%%     %% {ok,[[{seats,2,2,1,3,0,0,"1","A",1337,0},
%%     %%   {user,3,"Kalle","hehe",2,"kalle@derp.se"}]]}

       
%% disconnect(Username) ->
%%     Data  = get_database:get_user_from_seats(Username),
%%     case Data of
%% 	{_,[[{_,S_id,_,_,_,_,_,_,_,_,Lock_s},{_,_,U_name,_,_,_}]]} ->
%% 	    case U_name =:= Username of
%% 		true ->
%% 		    case Lock_s of
%% 			1 ->
%% 			    get_database:update_seat_lock(S_id,0),
%% 			    ok;
%% 			_ ->
%% 			    ok
%% 		    end;
%% 		_->    
%% 		    ok
%% 	    end;
%% 	_ ->
%% 	    ok
%%     end.


%% get_id_from_airport(A)->
%%     {ok, Pid} = amnesia:open(consair_database),
%%     amnesia:fetch(Pid, airport,{"iata=$1",[A]}).

%% get_sec_airport(Airport_id_from,Airport_to) ->
%%     {ok, Pid} = amnesia:open(consair_database),
%%     amnesia:fetch(Pid, flights,{"arrival_point = $2","airport_id = $1",[Airport_id_from,Airport_to]}).

%% a


    
%% get_stuff(A_id)->
%%     {ok, Pid} = amnesia:open(consair_database),
%%     %%{_,[{_,A_id,_,_}]}=get_database:get_id_from_airport(A),
%%     {ok, [Flight]} = amnesia:fetch(Pid, flights, {"arrival_point = $1", [A_id]}),
%%     %%{ok, Flight} = amnesia:fetch(Pid, flights, {"airport_id = $2", [B_id]}),
%%     amnesia:load_referenced(Pid,Flight).

%% get_stuff_f(A,B)->
%%       = get_database:get_stuff(A)
