%%This file is to populate the database for dev and for test of the system


-module ('pop_database').
-include ("consair_database.hrl").

-export ([populate_airport/0, pop/0]).

populate_airport() ->

    %%adding airports
    {ok, Pid} = amnesia:open(consair_database),
    {ok,Air1} = amnesia:add_new (Pid, #airport {iata = "ARN",
						   airport_name= "ARLANDA"}),
    {ok,Air2} = amnesia:add_new (Pid, #airport {iata = "ERN",
						   airport_name= "ARNAAIRPORT UPPSALA"}),
    {ok,Air3} = amnesia:add_new (Pid, #airport {iata = "LAX",
						   airport_name= "L.A. International Airport"}),
    {ok, Air4} = amnesia:add_new (Pid, #airport {iata = "OSE",
						    airport_name= "Ostersund flygplats"}),
    {ok, Air5} = amnesia:add_new (Pid, #airport {iata = "DRP",
						    airport_name= "DerpTown Airport"}),
    {ok, Air6} = amnesia:add_new (Pid, #airport {iata = "TEX",
						    airport_name= "Texas internatonal Airport"}),

    %% {ok,Air11} = amnesia:add_new (Pid, #airport_from {iata_f = "ARN",
    %% 						      airport_name_f= "ARLANDA"}),
    %% {ok,Air12} = amnesia:add_new (Pid, #airport_from {iata_f = "ERN",
    %% 						      airport_name_f= "ARNAAIRPORT UPPSALA"}),
    %% {ok,Air13} = amnesia:add_new (Pid, #airport_from {iata_f = "LAX",
    %% 						      airport_name_f= "L.A. International Airport"}),
    %% {ok, Air14} = amnesia:add_new (Pid, #airport_from {iata_f = "OSE",
    %% 						       airport_name_f= "Ostersund flygplats"}),
    %% {ok, Air15} = amnesia:add_new (Pid, #airport_from {iata_f = "DRP",
    %% 						       airport_name_f= "DerpTown Airport"}),
    %% {ok, Air16} = amnesia:add_new (Pid, #airport_from {iata_f = "TEX",
    %% 						       airport_name_f= "Texas internatonal Airport"}),

    

    %%adding flights

    {ok, F1} = amnesia:add_new (Pid, #flights {airport = Air1,
					       arrival_point = "TEX",
					       departure_date = {{2015, 10, 03}, {13, 37, 00}}
					      ,arrival_date = {{2015, 10, 04}, {01, 01, 10}},
					       flight_nr = "SKS101"}),

    {ok, F2} = amnesia:add_new (Pid, #flights {airport = Air2, 
					       arrival_point = "ARN",
					       departure_date = {{2015, 10, 03}, {13, 37, 00}},
					       arrival_date = {{2015, 10, 04}, {01, 01, 10}}, 
					       flight_nr = "SKS102"}),

    {ok, F5} = amnesia:add_new (Pid, #flights {airport = Air3,
					       arrival_point ="ERN", 
					       departure_date = {{2015, 10, 03}, {13, 37, 00}},
					       arrival_date = {{2015, 10, 04}, {01, 01, 10}}, 
					       flight_nr = "SKS103"}),

    {ok, F4} = amnesia:add_new (Pid, #flights {airport = Air4, 
					       arrival_point = "LAX" , 
					       departure_date = {{2015, 10, 03}, {13, 37, 00}},
					       arrival_date = {{2015, 10, 04}, {01, 01, 10}}, 
					       flight_nr = "SKS106"}),

    %%adding users



    {ok, U0} = amnesia:add_new (Pid, #user {user_name="NULL",
					    user_password="n/a",
					    user_class=1,
					    user_email="n@a.se"}),
    


    {ok, U1} = amnesia:add_new (Pid, #user {user_name="OlleDerpatron",
					    user_password="hej",
					    user_class=1,
					    user_email="olle@derp.se"}),


    {ok, U2} = amnesia:add_new (Pid, #user {user_name="Kalle",
					    user_password="hehe",
					    user_class=2,
					    user_email="kalle@derp.se"}),

    {ok, U3} = amnesia:add_new (Pid, #user {user_name="pelle",
					    user_password="asd",
					    user_class=1,
					    user_email="pelle@derp.se"}),

    {ok, U4} = amnesia:add_new (Pid, #user {user_name="carl",
					    user_password="asdasd",
					    user_class=2,
					    user_email="carl@derp.se"}),

    {ok, U5} = amnesia:add_new (Pid, #user {user_name="sir herp",
					    user_password="asd",
					    user_class=2,
					    user_email="herp@derp.se"}),

    {ok, U6} = amnesia:add_new (Pid, #user {user_name="meeeh",
					    user_password="asd",
					    user_class=1,
					    user_email="meeeh@derp.se"}),

    %%adding seats

    
    ok.

pop() -> 
    {ok, Pid} = amnesia:open(consair_database),
    {ok, S6} = amnesia:add_new(Pid ,#seats{flights = 1,
					   class = 1, 
					   user= 2,
					   window=0,
					   aisle=0,
					   row="1",
					   col="B",
					   price = 1337, 
					   lock_s =0}),
   
    {ok, S1} = amnesia:add_new (Pid, #seats {flights = 2,
    					     class = 1,
    					     user = 3,
    					     window = 0,
    					     aisle = 0,
    					     row = "1", 
    					     col ="A", 
    					     price = 1337, 
    					     lock_s = 0}),

    {ok, S2} = amnesia:add_new (Pid, #seats {flights = 1,
    					     class = 1,
    					     user = 1,
    					     window = 0,
    					     aisle = 0,
    					     row = "1", 
    					     col ="B", 
    					     price = 1337, 
    					     lock_s = 0}),

    {ok, S3} = amnesia:add_new (Pid, #seats {flights = 1,
    					     class = 1,
    					     user = 1,
    					     window = 0,
    					     aisle = 0, 
    					     row = "1", 
    					     col ="C", 
    					     price = 1337, 
    					     lock_s = 0}),

    {ok, S4} = amnesia:add_new (Pid, #seats {flights = 2,
    					     class = 1,
    					     user = 1,
    					     window = 0,
    					     aisle = 0,
    					     row = "1", 
    					     col ="D", 
    					     price = 1337, 
    					     lock_s = 0}),

    ok.
