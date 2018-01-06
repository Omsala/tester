
-module(webserver).
-export([start/0, start/1, connector_inbox/6, connector_handler/5]).
-include("webserver.hrl").
-include_lib("eunit/include/eunit.hrl").


-spec start() -> ok.

start() ->
    start(?PORT).

-spec start(Port) -> ok when
Port::integer().

start(Port) ->
%% Open port
case gen_tcp:listen(Port, ?CONNECTIONOPTIONS) of 
{ok, LSock} ->
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
  connector_spawner(LSock, 0);
{error, eaddrinuse} ->
  {error, eaddrinuse};
_ ->
  {error, could_not_listen}		
end.


%%-------------------------------------------------
-spec connector_spawner(LSock, N) -> 
    {error, Error} | ok when
LSock::gen_tcp:socket(), 
N::integer(),
Error::atom().

connector_spawner(LSock, N) ->
%% receive message from other processes for 100ms
receive
exit ->        %% exit
gen_tcp:close(LSock);
disconnect ->  %% if received terminated -> reduce amount of connections
connector_spawner(LSock, N-1);
reload_code ->
code:load_file(server_functions),
code:purge(server_functions),
connector_spawner(LSock, N);
{error, Error} ->
?WRITE_SPAWNER("Error !!!!!! {error, ~p}~n", [Error], "E"),
connector_spawner(LSock, N)
after 50 ->
%% try connecting to other device for 100ms
case gen_tcp:accept(LSock, 50) of
{ok, Sock} ->
%% spawn process to handle this connection
New_connector_handler = spawn(?MODULE, connector_handler, [Sock, N+1, 0, null, self()]),
spawn(?MODULE, connector_inbox, [Sock, N+1, 0, null, self(), New_connector_handler]),
?WRITE_SPAWNER("New connection: ~p~n", [New_connector_handler], "N"),
connector_spawner(LSock, N+1);
{error, timeout} ->
connector_spawner(LSock, N);
{error, Error} ->
io:fwrite("Error: ~p~n", [{error, Error}])
end
end.
%%-----------------------------------------------------------
-spec connector_inbox(Sock, ID, Timeouts, Parent_PID, Handler_PID) -> 
    {ok, Package} | {error, Error} when
Sock::gen_tcp:socket(),
ID::integer(),
Timeouts::integer(),
Parent_PID::pid(),
Handler_PID::pid(),
Package::bitstring(),
Error::atom().

connector_inbox(_, ID, ?ALLOWEDTIMEOUTS, Parent_PID, Handler_PID) ->
?WRITE_CONNECTION("~p timeout, terminated~n", [?ALLOWEDTIMEOUTS], "D"),

Handler_PID ! disconnect,
Parent_PID  ! disconnect,
{error, timeout};

connector_inbox(Sock, ID, Timeouts, Parent_PID, Handler_PID) ->
case gen_tcp:recv(Sock, 0, 60000) of
{error, timeout} ->
?WRITE_CONNECTION("Timeout ~p, ~p tries remaining~n", [Timeouts+1, ?ALLOWEDTIMEOUTS - Timeouts], "T"),
connector_inbox(Sock, ID, Timeouts+1, Parent_PID, Handler_PID);
{error, closed} ->
Handler_PID ! {error, closed};
{error, Error} ->
?WRITE_CONNECTION("{error, ~p}~n", [Error], "E"),
Parent_PID ! disconnect;
{ok, Package} ->
Package_list = lists:droplast(re:split(Package, ?MESSAGE_SEPERATOR)),
         
%%---------- SEND TO HANDLER ------------%%
pass_message_list(Package_list, Handler_PID),

connector_inbox(Sock, ID, Timeouts, Parent_PID, Handler_PID)
end.

connector_handler(Sock, ID, Timeouts, Parent_PID) ->
    receive
	{ok, Package} -> %% In case of package handle and responde
	    ?WRITE_CONNECTION("Message received: <<<<<  ~p~n", [Package], "<"),

	    %% Timestamp calculation
	    {Incoming_timestamp, Handled_package} = package_handler:handle_package(Package),
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
		{ok, Response} ->
		    ?WRITE_CONNECTION("Message sent:     >>>>>  ~n", [], ">"),    
		    gen_tcp:send(Sock, Response),
		    connector_handler(Sock, ID, 0, Parent_PID);
		ok ->
		    connector_handler(Sock, ID, 0, Parent_PID);
		{error, Error} ->
       		    Parent_PID ! {error, Error},
		    case is_list(Error) of
			true ->
			    gen_tcp:send(Sock, translate_package({?ERROR, [Error]}));
			_ ->
			    gen_tcp:send(Sock, translate_package({?ERROR, [atom_to_list(Error)]}))
		    end,
		    connector_handler(Sock, ID, 0, Parent_PID);
		{client_error, Error} ->
		    ?WRITE_CONNECTION("{client_error, ~p}~n", [Error], "E"),
		    connector_handler(Sock, ID, 0, Parent_PID)
	    end;
	disconnect ->
	    ?WRITE_CONNECTION("Connection  closed", [], "D"),

    after 5000 -> 
	    connector_handler(Sock, ID, Timeouts, Parent_PID)
    end.
	
