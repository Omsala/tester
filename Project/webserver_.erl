-module(webserver_).
-export([]).

-define(PORT, 53535).
-define(CONNECTIONOPTIONS, [binary,{packet,0},{active,false}]).
-define(LOGIN,                          1).
-define(DISCONNECT,                     4).
-define(TERMINATE_SERVER,              24).
-define(RELOAD_CODE,                   26).


translate_package({ID})->



list_to_exp([T|[]],_)->
    string:concat(
    case is_int(T) of 
        true -> int_list(T);
        _-> T
    end,
    ?ELEMENT_SEPERATOR++ ?MESSAGE_SEPERATOR);


list_to_exp([H|T], seperator)->
    string:concat(string:concat(
case is_int(H) of 
    true -> int_list(H);
    _->H
    end,
    seperator),list_to_exp(T,seperator)).

tuppels_list(Tuppel)->
    tuppels_list([Tuppel],[]).

tuppels_list([],Temp)->
    Temp;
tuppels_list([H|T_list],Temp) when is_tupple(H)->
    tuppels_list(tupples_list)
