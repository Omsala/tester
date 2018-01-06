-module(webserver).
-export([start/0,con_manager/5,connector_inbox/6,start/1]).
-include_lib("eunit/include/eunit.hrl").

start()-> start(?PORT).

-spec start(Port)-> ok when
    Port::interger().

start(Port)->
    case gen_tcp:listen(Port,?CONNECTIONOPTIONS) of
        {ok,LSock} ->
            io:fwrite("Connections on:: ~p~n", [LSock]),
            {ok, Ifaddrs} = inet:getifaddrs(),
            case lists:keyfind("wlan0", 1, Ifaddrs) of
            false ->
                ok;
            {_, WLAN} ->
                case lists:keyfind(addr, 1, WLAN) of
                false ->
                    ok;
                {_, W_IP} ->
                    io:fwrite("Wireless IP Address: ~p~n", [W_IP])
                end
            end,
            case lists:keyfind("etho0", 1, Ifaddrs) of 
            false ->
                ok;
            {_, ETH} ->
                case lists:keyfind(addr, 1, ETH) of
                false ->
                    ok;
                {_, E_IP} ->
                    io:fwrite("Ethernet IP Address: ~p~n", [E_IP])
                end
            end,
            connector_handler(LSock, 0);
        {error, eaddrinuse} ->
            ?DRAW_LOGO,
            ?DRAW_TITLE("Port " ++ integer_to_list(Port) ++ " busy "),
            {error, eaddrinuse};
        _ ->
            {error, could_not_listen}		
        end.


-spec connector_handler(LSock, N) -> 
    {error, Error} | ok when
    LSock::gen_tcp:socket(), 
    N::integer(),
    Error::atom().

connector_handler(LSock, N) ->
 %% receive message from other processes for 100ms
 receive
 exit ->        %% if received exit -> exit the loop (connection processes are still alive)
     gen_tcp:close(LSock);
 disconnect ->  %% if received terminated -> reduce amount of connections
     connector_handler(LSock, N-1);
 reload_code ->
        %%Reloads all moduals if the server funtions are donw add all mods...
     connector_handler(LSock, N);
 {error, Error} ->
     ?WRITE_SPAWNER("Error received! {error, ~p}~n", [Error], "E"),
     connector_handler(LSock, N)
 after 50 ->
     %% try connecting to other device for 100ms
     case gen_tcp:accept(LSock, 50) of
     {ok, Sock} ->
         %% spawn process to handle this connection
         New_con_handler = spawn(?MODULE, con_manager, [Sock, N+1, 0, null, self()]),
         spawn(?MODULE, connector_inbox, [Sock, N+1, 0, null, self(), New_con_handler]),
         ?WRITE_SPAWNER("New connection establishing: ~p~n", [New_con_handler], "N"),
         connector_handler(LSock, N+1);
     {error, timeout} ->
         connector_handler(LSock, N);
     {error, Error} ->
         io:fwrite("Error: ~p~n", [{error, Error}])
     end
 end.

-spec connector_inbox(Sock, ID, Timeouts, User, Parent_PID, Handler_PID) -> 
              {ok, Package} | {error, Error} when
   Sock::gen_tcp:socket(),
   ID::integer(),
   Timeouts::integer(),
   User::string(),
   Parent_PID::pid(),
   Handler_PID::pid(),
   Package::bitstring(),
   Error::atom().

connector_inbox(_, ID, ?ALLOWEDTIMEOUTS, User, Parent_PID, Handler_PID) ->
 ?WRITE_CONNECTION("~p timeouts reached, connection terminated~n", [?ALLOWEDTIMEOUTS], "D"),

 Handler_PID ! disconnect,
 Parent_PID  ! disconnect,
 {error, timeout};

connector_inbox(Sock, ID, Timeouts, User, Parent_PID, Handler_PID) ->
 case gen_tcp:recv(Sock, 0, 60000) of
 {error, timeout} ->
     ?WRITE_CONNECTION("Timeout ~p, ~p tries remaining~n", [Timeouts+1, ?ALLOWEDTIMEOUTS - Timeouts], "T"),
     connector_inbox(Sock, ID, Timeouts+1, User, Parent_PID, Handler_PID);
 {error, closed} ->
     Handler_PID ! {error, closed};
 {error, Error} ->
     ?WRITE_CONNECTION("{error, ~p}~n", [Error], "E"),
     Parent_PID ! disconnect;
 {ok, Package} ->
     Package_list = lists:droplast(re:split(Package, ?MESSAGE_SEPERATOR)),
     pass_message_list(Package_list, Handler_PID),
     connector_inbox(Sock, ID, Timeouts, User, Parent_PID, Handler_PID)
 end.

 con_manager(Sock, ID, Timeouts, User, Parent_PID) ->
 receive
 {ok, Package} -> %% In case of package handle and responde
     ?WRITE_CONNECTION("Message received: <<<<<  ~p~n", [Package], "<"),

     %% Timestamp calculation
     {Incoming_timestamp, Handled_package} = package_handler:handle_package(Package, User),
     {Mega_S, S, Micro_S} = now(),
     Time_taken = ((((Mega_S * 1000000) + S) * 1000000) + Micro_S) div 1000 - Incoming_timestamp,
     ?WRITE_CONNECTION("Time to handle package: ~p~n", [Time_taken], " "),

     %% case to handle package
     case Handled_package of
     {ok, exit} ->
         ?WRITE_CONNECTION("Exit request~n", [], "X"),    
         Parent_PID ! exit;
     {ok, disconnect} ->
         ?WRITE_CONNECTION("Disconnecting~n", [], "D"),
         gen_tcp:close(Sock),
         Parent_PID ! disconnect;
     {ok, reload_code} ->
        ?WRITE_CONNECTION("Code reload request~n", [], "R"),    
        Parent_PID ! reload_code;
     {ok, {admin, Response}} ->
         ?WRITE_CONNECTION("Message sent:     >>>>> ~n", [], ">"),    
         gen_tcp:send(Sock, Response),
         ?WRITE_CONNECTION("Logged in as Admin~n", [], " "),
         con_manager(Sock, ID, 0, admin, Parent_PID);
     {ok, {New_user, Response}} ->
         ?WRITE_CONNECTION("Message sent:     >>>>> ~n", [], ">"),    
         gen_tcp:send(Sock, Response),
         ?WRITE_CONNECTION("Logged in as ~p~n", [New_user], " "),
         con_manager(Sock, ID, 0, New_user, Parent_PID);
     {ok, Response} ->
         ?WRITE_CONNECTION("Message sent:     >>>>>  ~n", [], ">"),    
         gen_tcp:send(Sock, Response),
         con_manager(Sock, ID, 0, User, Parent_PID);
     ok ->
        con_manager(Sock, ID, 0, User, Parent_PID);
     {error, Error} ->
                Parent_PID ! {error, Error},
         case is_list(Error) of
         true ->
             gen_tcp:send(Sock, translate_package({?ERROR, [Error]}));
         _ ->
             gen_tcp:send(Sock, translate_package({?ERROR, [atom_to_list(Error)]}))
         end,
         con_manager(Sock, ID, 0, User, Parent_PID);
     {client_error, Error} ->
         ?WRITE_CONNECTION("{client_error, ~p}~n", [Error], "E"),
         con_manager(Sock, ID, 0, User, Parent_PID)
     end;
 disconnect ->
     ?WRITE_CONNECTION("Connection unexpectantly closed, logging out user.~n", [], "D"),
     case package_handler:logout(User) of 
     ok -> 
         ok;
     {error, Error} -> 
         Parent_PID ! {error, Error},
         {error, Error}
     end;
 {error, closed} ->
     ?WRITE_CONNECTION("Connection unexpentantly closed, logging out user. ~n", [], "D"),
     case package_handler:logout(User) of
     {error, no_user} ->
         ok;
     {error, Error} ->
         Parent_PID ! {error, Error};
     ok ->
         Parent_PID ! disconnect
     end

 after 5000 -> 
        con_manager(Sock, ID, Timeouts, User, Parent_PID)
 end.