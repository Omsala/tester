-module(package_handler).			
%-export([handle_package/2]).
-compile(export_all).

-include("server_utils.hrl").

-include_lib("eunit/include/eunit.hrl").

%% @doc - A package handler which translates the received package from
%% bitstring to a tuple, calls the appropriate function, and
%% translates the answer back to bitstring.

handle_package(Package, User) when is_bitstring(Package) ->
    {Timestamp, Translated_package} = translate_package(Package),
    {Timestamp, handle_package(Translated_package, User)};
handle_package({?LOGIN, [Username, Password]}, User) -> %% ID 1 - Handshake
    %% grants either user or admin privilege alternatively a failure message in case handshake didn't work. 
    %% failed if username or password is incorrect. 
    
    %% 0x00 - user
   %% 0x10 - admin
    %% 0xff - failed
    
    case booking_agent:login(Username, Password) of
	{ok, admin} when User =:= null ->
	    {ok, {admin, translate_package({?LOGIN_RESP, [2]})}};
	{ok, New_user} when User =:= null ->
	    {ok, {New_user, translate_package({?LOGIN_RESP, [1]})}};
	{ok, admin} ->
	    logout(User),
	    {ok, {admin, translate_package({?LOGIN_RESP, [2]})}};
	{ok, New_user} ->
	    logout(User),
	    {ok, {New_user, translate_package({?LOGIN_RESP, [1]})}};
	{error, no_such_user} ->
	    {ok, translate_package({?LOGIN_RESP, [3]})};
	{error, wrong_password} ->
	    {ok, translate_package({?LOGIN_RESP, [3]})};
	{error, Error} ->
	    {error, Error}
    end;
  
handle_package({?HEARTBEAT}, _) -> 
    {ok, translate_package({?HEARTBEAT})}; 

handle_package({?DISCONNECT}, User) -> 
    case User of
	null ->
	    {ok, disconnect};
	admin ->
	    {ok, disconnect};
	_ ->
	    case logout(User) of
		ok ->
		    {ok, disconnect};
		{error, Error} ->
		    {error, Error}
	    end
    end;

handle_package({?ERROR, Error}, _) ->
    {client_error, Error};

%%--------------------------------------------------------------%%

handle_package({?INIT_BOOK, Seat_ID_list_as_string}, User) ->
    Seat_ID_list = [list_to_integer(X) || X <- Seat_ID_list_as_string],
    case booking_agent:start_booking(User, Seat_ID_list) of
	ok ->
	    {ok, translate_package({?INIT_BOOK_RESP, [?INIT_SUCCESS, ?Book_time]})};
	{error, seat_locked} ->
	    {ok, translate_package({?INIT_BOOK_RESP, [?INIT_LOCKED, ?Book_time]})};
	{error, seat_booked} ->
	    {ok, translate_package({?INIT_BOOK_RESP, [?INIT_BOOKED, ?Book_time]})};
	{error, no_such_seat} ->
	    {ok, translate_package({?INIT_BOOK_RESP, [?INIT_DID_NOT_EXIST, ?Book_time]})};
	{error, Error} ->
	    {error, Error}
    end;

handle_package({?FIN_BOOK}, User) -> 
    case booking_agent:finalize_booking(User) of
	ok ->
	    {ok, translate_package({?FIN_BOOK_RESP, [?FIN_BOOK_SUCCESS]})};
	{error, _} ->
	    {ok, translate_package({?FIN_BOOK_RESP, [?FIN_BOOK_FAIL]})}
    end;
    
handle_package({?ABORT_BOOK}, User) -> 
    case booking_agent:abort_booking(User) of
	ok ->
	    ok;
	{error, Error} ->
	    {error, Error}
    end;

handle_package({?REQ_AIRPORTS}, _) -> 
    case booking_agent:airport_list() of
	{ok, Airport_list} ->
	    {ok, translate_package({?REQ_AIRPORTS_RESP, flatten_tuples_to_list(Airport_list)})}; 
	{error, Error} ->
	    {error, Error}
    end;

handle_package({?REQ_AIRPORTS, Airport_ID}, _) -> 
    case booking_agent:airport_list(Airport_ID) of
	{ok, Airport_list} ->
	    {ok, translate_package({?REQ_AIRPORTS_RESP, flatten_tuples_to_list(Airport_list)})};
	{error, Error} ->
	    {error, Error}
    end;

handle_package({?SEARCH_ROUTE, [Airport_A, Airport_B]}, _) ->
    case booking_agent:route_search(Airport_A, Airport_B) of
	{ok, Flight_list} ->
	    {ok, translate_package({?SEARCH_ROUTE_RESP, 
				    flatten_tuples_to_list(lists:flatten(Flight_list))})};
	{error, Error} ->
	    {error, Error}
    end;
	
handle_package({?SEARCH_ROUTE, [Airport_A, Airport_B, Year, Month, Day]}, _) ->
    case booking_agent:route_search(Airport_A, Airport_B, {Year, Month, Day}) of
	{ok, Flight_list} ->
	    {ok, translate_package({?SEARCH_ROUTE_RESP, 
				    flatten_tuples_to_list(lists:flatten(Flight_list))})};
	{error, Error} ->
	    {error, Error}
    end;

handle_package({?REQ_FLIGHT_DETAILS, Flight_ID}, admin) -> 
    case booking_agent:flight_details(Flight_ID) of
	{ok, Flight_details} ->
	    {ok, translate_package({?REQ_FLIGHT_DETAILS_RESP,
				    flatten_tuples_to_list(lists:flatten(Flight_details))})};
	{error, Error} ->
	    {error, Error}
    end;

handle_package({?REQ_FLIGHT_DETAILS, Flight_ID}, _) -> 
    case booking_agent:flight_details(Flight_ID) of
	{ok, Flight_details} ->
	    {ok, translate_package({?REQ_FLIGHT_DETAILS_RESP,
				    flatten_tuples_to_list(lists:flatten(Flight_details))})};
	{error, Error} ->
	    {error, Error}
    end;



handle_package({?REQSeatLock, Seat_ID, Flight_ID}, User) ->
    case booking_agent:seat_lock(Seat_ID, Flight_ID) of
	{ok, Lock} ->
	    case User of
		admin ->
		    {ok, translate_package({?RESPSeatLock, Lock})};
		_ ->
		    {ok, translate_package({?RESPSeatLock, (case Lock of 2 -> 1; _ -> Lock end)})}
	    end;
	{error, Error} ->
	    {error, Error}
    end;


handle_package({?REQSeatLock, Seat_ID}, User) ->
    case booking_agent:seat_lock(Seat_ID, User) of
	{ok, Lock} ->
	    {ok, translate_package({?RESPSeatLock, Lock})};
	{error, Error} ->
	    {error, Error}
    end;

handle_package({?REQ_SEAT_SUGGESTION, [Flight_ID, Group_size]}, _User) -> 
    case booking_agent:suggest_seat(Flight_ID, list_to_integer(Group_size)) of
	{ok, Chain_list} ->
	    {ok, translate_package({?REQ_SEAT_SUGGESTION_RESP, lists:flatten(lists:map(fun server_utils:flatten_tuples_to_list/1, Chain_list))})};
	{error, Error} ->
	    {error, Error}
    end;

handle_package({?REQ_SEAT_SUGGESTION, Flight_ID}, _User) -> 
    case booking_agent:suggest_seat(Flight_ID, 1) of
	{ok, Chain_list} ->
	    {ok, translate_package({?REQ_SEAT_SUGGESTION_RESP, flatten_tuples_to_list(Chain_list)})};
	{error, Error} ->
	    {error, Error}
    end;

handle_package({?REQReceipt, _Message}, _User) -> 
    % booking_agent:receipt(),
    {error, not_yet_implemented};

%%--------------------------------------------------------------%%


handle_package({?TERMINATE_SERVER}, admin) ->
    {ok, exit};

handle_package({?RELOAD_CODE}, admin) ->
    {ok, reload_code};

%%--------------------------------------------------------------%%

handle_package(_, _) ->
    {error, wrong_message_format}.

handle_package(_) ->
    {error, wrong_message_format}.

%%--------------------------------------------------------------%%

logout(User) ->
    case User of
	null ->
	    {error, no_user};
	_ ->
	    case booking_agent:disconnect(User) of
		ok ->
		    ok;
		{error, Error} ->
		    {error, Error}
	    end
    end.


%% airport_tuple_to_list({Airport_ID, IATA, Name}) ->
%%     [Airport_ID, IATA, Name].

%% basic_flight_tuple_to_list({Flight_ID, Airport_A, Airport_B, {{Year, Month, Day},{Hour, Minute, Second}}}) ->
%%     lists:append(lists:append(lists:append([Flight_ID], 
%% 					   airport_tuple_to_list(Airport_A)), 
%% 			      airport_tuple_to_list(Airport_B)), 
%% 		 [Year, Month, Day, Hour, Minute, Second]).

%% flight_tuple_to_list({Flight_ID, Airport_A, Airport_B, Seat_list, {{Year_D, Month_D, Day_D},{Hour_D, Minute_D, Second_D}}, {{Year_A, Month_A, Day_A},{Hour_A, Minute_A, Second_A}}}) ->
%%     lists:append(lists:append(lists:append(lists:append([Flight_ID],
%% 							airport_tuple_to_list(Airport_A)), 
%% 					   airport_tuple_to_list(Airport_B)),
%% 			      lists:foldr(fun lists:append/2, [],
%% 					  lists:map(fun seat_tuple_to_list/1, Seat_list))),
%% 		 [Year_D, Month_D, Day_D, Hour_D, Minute_D, Second_D, Year_A, Month_A, Day_A, Hour_A, Minute_A, Second_A]).

%% admin_flight_tuple_to_list({Flight_ID, Airport_A, Airport_B, Seat_list, {{Year_D, Month_D, Day_D},{Hour_D, Minute_D, Second_D}}, {{Year_A, Month_A, Day_A},{Hour_A, Minute_A, Second_A}}}) ->
%%     lists:append(lists:append(lists:append(lists:append([Flight_ID],
%% 							airport_tuple_to_list(Airport_A)), 
%% 					   airport_tuple_to_list(Airport_B)),
%% 			      lists:foldr(fun lists:append/2, [], 
%% 					  lists:map(fun admin_seat_tuple_to_list/1, Seat_list))),
%% 		 [Year_D, Month_D, Day_D, Hour_D, Minute_D, Second_D, Year_A, Month_A, Day_A, Hour_A, Minute_A, Second_A]).

%% seat_tuple_to_list({Seat_ID, Flight_ID, Class, _, Window, Aisle, Row, Col, Price, Lock_s}) ->
%%     [Seat_ID, Flight_ID, 
%%      Class, Window, Aisle, 
%%      Row, Col, Price, 
%%      (case Lock_s of 
%% 	  0 -> 0;
%% 	  1 -> 1;
%% 	  2 -> 1 end)];
%% seat_tuple_to_list(_) ->
%%     {error, wrong_format}.

%% admin_seat_tuple_to_list({Seat_ID, Flight_ID, Class, User, Window, Aisle, Row, Col, Price, Lock_s}) ->
%%     [Seat_ID, Flight_ID, 
%%      Class, User, Window, Aisle, 
%%      Row, Col, Price, Lock_s];
%% admin_seat_tuple_to_list(_) ->
%%     {error, wrong_format}.




%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%                         EUnit Test Cases                                  %%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

package_handler_test() ->
    ?assertMatch(true, true).

translate_package_test() ->
    List_of_strings = ["a", "b", "c", "d", "e", "f"],
    List_of_numbers = [1, 2, 3, 4, 5, 6, 7, 8],
    List_of_mixed = ["a", 1, "b", 2, "c", 3, "d", 4],

    ?assertMatch(<<"1&a&b&c&d&e&f&">>, translate_package({1, List_of_strings})),
    ?assertMatch(<<"2&1&2&3&4&5&6&7&8&">>, translate_package({2, List_of_numbers})),
    ?assertMatch(<<"3&a&1&b&2&c&3&d&4&&">>, translate_package({3, List_of_mixed})).

tuple_to_list_test() ->
    Airport_A = {2, "ARN", "Arlanda Airport"},
    Airport_B = {3, "LAX", "Los Angeles International Airport"},
    Seat_list = [{"A32", 2, 1, "Carl",    1, 0, 32, "A", 500,  2},
		 {"B32", 2, 1, "Carl",    0, 1, 32, "B", 500,  0},
		 {"C2",  2, 2, "Andreas", 0, 0, 2,  "C", 1200, 1}],

    ?assertMatch([2, "ARN", "Arlanda Airport"], 
		 flatten_tuples_to_list(Airport_A)),

    ?assertMatch([3, 2, "ARN", "Arlanda Airport", 3, "LAX", "Los Angeles International Airport", 2015, 05, 21, 13, 25, 00],
		 flatten_tuples_to_list({3, Airport_A, Airport_B, {{2015, 05, 21},{13, 25, 00}}})),


    ?assertMatch(["A32", 2, 1, 1, 0, 32, "A", 500, 1],
		 flatten_tuples_to_list(hd(Seat_list))),

    ?assertMatch(["A32", 2, 1, "Carl", 1, 0, 32, "A", 500, 2],
		 flatten_tuples_to_list(hd(Seat_list))),

    ?assertMatch([3, 2, "ARN", "Arlanda Airport", 3, "LAX", "Los Angeles International Airport", "A32", 2, 1, 1, 0, 32, "A", 500, 1, "B32", 2, 1, 0, 1, 32, "B", 500, 0, "C2", 2, 2, 0, 0, 2, "C", 1200, 1, 2015, 5, 21, 13, 25, 0, 2015, 5, 22, 1, 21, 12],
       flatten_tuples_to_list({3, Airport_A, Airport_B, Seat_list, {{2015, 05, 21},{13, 25, 00}}, {{2015, 05, 22},{01, 21, 12}}})),

    ?assertMatch([3, 2, "ARN", "Arlanda Airport", 3, "LAX", "Los Angeles International Airport", "A32", 2, 1, "Carl", 1, 0, 32, "A", 500, 2, "B32", 2, 1, "Carl", 0, 1, 32, "B", 500, 0, "C2", 2, 2, "Andreas", 0, 0, 2, "C", 1200, 1, 2015, 5, 21, 13, 25, 0, 2015, 5, 22, 1, 21, 12],
       flatten_tuples_to_list({3, Airport_A, Airport_B, Seat_list, {{2015, 05, 21},{13, 25, 00}}, {{2015, 05, 22},{01, 21, 12}}})).

