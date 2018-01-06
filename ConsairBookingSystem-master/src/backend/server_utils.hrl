-import(server_utils, [translate_package/1, now_as_string_millis/0, list_to_regexp/2, flatten_tuples_to_list/1, connect_send_and_receive_manual/3, connect_send_and_receive/3, connect_send_and_receive_list/3, reload_code/0,reload_code/1, stop_server/0, stop_server/1, format_position_as_mod/2, format_position_as_mod/4, fill_with_white_space/2, pass_message_list/2, remove_first_element_in_tuples_list/1]).


-define(VERSION, "0.9").

-define(PORT, 53535).
-define(ALT_PORT, 53335).
-define(CONNECTIONOPTIONS, [binary, {packet, 0}, {active, false}]).
-define(ALLOWEDTIMEOUTS, 10). %% Amount of minutes allowed before connection is terminated
-define(ADMIN_ALLOWEDTIMEOUTS, 20). %% Amount of minutes allowed before admin connection is terminated
-define(Book_time, 10). %% Minutes before users locked seat is unlocked



%% RegExp seperators

-define(ELEMENT_SEPERATOR, "&"). 
-define(MESSAGE_SEPERATOR, "\n").


%% Codes

%% SEAT LOCK
-define(LOCK_S_AVAILABLE, 0).
-define(LOCK_S_LOCKED,    1).
-define(LOCK_S_BOOKED,    2).

%% BOOK RESPONSE CODE
-define(INIT_SUCCESS,       1).
-define(INIT_LOCKED,        2).
-define(INIT_BOOKED,        3).
-define(INIT_DID_NOT_EXIST, 4).

%% FINALIZE BOOK RESPONSE CODE
-define(FIN_BOOK_SUCCESS, 1).
-define(FIN_BOOK_FAIL,    2).


%% Package IDs
-define(LOGIN,                         1).
-define(LOGIN_RESP,                    2).
-define(ERROR,                         3).
-define(DISCONNECT,                    4).
-define(INIT_BOOK,                     5).
-define(INIT_BOOK_RESP,                6).
-define(FIN_BOOK,                      7).
-define(FIN_BOOK_RESP,                 8).
-define(ABORT_BOOK,                    9).
-define(REQ_AIRPORTS,                 10).
-define(REQ_AIRPORTS_RESP,            11).
-define(SEARCH_ROUTE,                 12).
-define(SEARCH_ROUTE_RESP,            13).
-define(REQ_FLIGHT_DETAILS,           14).
-define(REQ_FLIGHT_DETAILS_RESP,      15).
-define(REQ_SEAT_SUGGESTION,          16).
-define(REQ_SEAT_SUGGESTION_RESP,     17).
-define(REQ_SEAT_MAP,                 -1).
-define(REQ_SEAT_MAP_RESP,            -1).
-define(TERMINATE_SERVER,             24).
 
-define(HEARTBEAT,                    25).
-define(RELOAD_CODE,                  26).
-define(REQSeatLock,                 100).
-define(RESPSeatLock,                101).






-define(REQReceipt,                   17).
-define(RESPReceipt,                  18).



-define(WRITE_CONNECTION(String, Arguments, Code), 
	io:format(" C"
		  ++ fill_with_white_space(integer_to_list(ID), 3) 
		  ++ format_position_as_mod(ID, 5)
		  ++ " | " 
		  ++ fill_with_white_space(pid_to_list(self()), 10)
		  ++ " | " 
		  ++ fill_with_white_space(case is_list(User) of true -> User; _ -> atom_to_list(User) end, 14)
		  ++ " | "
		  ++ Code
		  ++ " | "
		  ++ String, 
		  Arguments)).

-define(WRITE_SPAWNER(String, Arguments, Code), 
	io:format("*CS* " 
		  ++ format_position_as_mod(-1, 5) 
		  ++ " | " 
		  ++ fill_with_white_space(pid_to_list(self()), 10)
		  ++ " | " 
		  ++ "Connections: ~p"
		  ++ " | "
		  ++ Code
		  ++ " | " 
		  ++ String, 
		  [N | Arguments])).

-define(DRAW_LOGO,
	io:fwrite("                                                                                                        ~n"),
	io:fwrite("                                                                                                        ~n"),
	io:fwrite("                                                                                                        ~n"),
	io:fwrite("  CONS AIR BOOKING SYSTEM                                                  sMMN/                        ~n"),
	io:fwrite("     VERSION "++?VERSION++"                                                 `:+o/     hMMMo    `:+sy/              ~n"),
	io:fwrite("                                                                 sMMMMo     :ooo. `hMMMMMN`             ~n"),
	io:fwrite("                                                                `hMMMMMy`   oMMMs` dMMMo..              ~n"),
	io:fwrite("                                                                /NMMNMMMm:  +NMMNo /MMMd                ~n"),
	io:fwrite("                                                 -+oyhy/        +NMMmmMMMN/ `mMMMm` mMMM:               ~n"),
	io:fwrite("                                      `-://-`  `yNMMMMMd    `.- oNMMN.yMMMN+ oNMMN/ +MMMh               ~n"),
	io:fwrite("             ./shdmdy/             -sdNMMMMMNo`+NMMNs++--ydNMMMhsNMMNdNNMMMN+`mMMM+ `mMMN-              ~n"),
	io:fwrite("           `yMMMMMMMMs   :sdmNds:  hMMMMNNMMMMh/NMMMmy+-oNMMMMMmmMMMMMMMNMMMN+oMMMm` +MMMy              ~n"),
	io:fwrite("           hMMMh-``.-` `hMMMMMMMMh./NMMMh`yMMMMo/mMMMMMMNNy+/-. sMMMNs/:-hMMMNyNMMM/ `hhy+              ~n"),
	io:fwrite("          .NMMN`       oNMMNsoNMMMd:dMMMN-.NMMMm` :/mNMMMMs     sMMMN- -oohMNNd+o+-    -//-             ~n"),
	io:fwrite("          .NMMN.       yMMMN- +NMMMy/NMMMs sMMMN+ :.dNNMMMd     oNMMm. `-//+/.  -:.     :///.           ~n"),
	io:fwrite("           dMMM+       oNMMNo `mMMMN.dMMMN.-NMMMm/NMMMMMMN/     `/:-`    `.:///:-///:::://////////:`    ~n"),
	io:fwrite("           /MMMm`      .mMMMm- yMMMN-+NMMMs yMMMNohdNNmh+.                  `.-:////////////////-.`     ~n"),
	io:fwrite("            dMMMh`    `:sNMMMNhNMMMd `mMMMd .os+:                                `..-------..``         ~n"),
	io:fwrite("            .dMMMm+::yNMNmdNMMMMMNh.  .::.                                                              ~n"),
	io:fwrite("             `sNMMMMMMMMN+ -+sso/os:                                                                    ~n"),
	io:fwrite("               `/syyso/.         .ss-                                                                   ~n"),
	io:fwrite("                                  -ss/             ..                                                   ~n"),
	io:fwrite("                                   -oso-           -so.                                                 ~n"),
	io:fwrite("                                    `/sso:` :+-     +ss:                                                ~n"),
	io:fwrite("                                      `/oss+:oso/:::/sss+...`                                           ~n"),
	io:fwrite("                                         ./ossssssssssssssssso/                                         ~n"),
	io:fwrite("                                            `-:/+ossssssoo+/:.`                                         ~n"),
	io:fwrite("                                                    ```                                                 ~n"),
	io:fwrite("                                                                                                        ~n"),
	io:fwrite("                                                                                                        ~n")).
                                                                                                    
                                                                                                    
                                                                                                 


-define(BEAUTIFUL_LINE, "========================================================-=-=-=-=-=-=-=-=-=-=---------------~n").

-define(DRAW_TITLE(String),
	
	io:fwrite(?BEAUTIFUL_LINE),
	io:fwrite(String ++ "~n"),
	io:fwrite(?BEAUTIFUL_LINE)).


-define(DRAW_TABLE_HEADER,
	
	io:fwrite(?BEAUTIFUL_LINE),
	io:fwrite(" Connection |    PID     |  Login / #Con  |   | Message~n"),
	io:fwrite(?BEAUTIFUL_LINE)).
