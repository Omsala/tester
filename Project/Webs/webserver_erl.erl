-module(webserver_erl).
-export([start/1,start/2]).

start(Manager)->
    start(Manager, 8000).

start(Manager, Port)->
    {ok, Req} = gen_tcp:listen(Port, [{usedaddress,true},bin,{backlog,1024}]),
    spawn(fun()->accept(Req,Manager) end),
    receive stop -> gen_tcp:close(Req) end. 

accept(Req, Manager)->
    {ok,M} = gen_tcp:accept(Req),
    ok = inet:setopts(M,[{package,http}]),
    spawn(fun()->accept(Req,Manager) end),
    server(M, Manager,[{headers,[]}]).


server(M, Manager,Rec)->
    ok = inet:setopts(M,[{active,on}]),
    HttpM = receive
                {http,M,Payload}->Payload;
                _->gen_tcp:close(M)
            end,
    case HttpM of 
        {http_req,Mes,{path, U},V}->
            NRec = [{method,Mes},{u,U},{ver,V}|Rec],
            server(M,Manager,NRec);
        {http_head,_,Hls,_,Value}->
            {headers,Hds} =lists:keyfind(headers,1,Rec),
            server(M,Manager,lists:keystore(headers,1,Rec,{headers,[{Hls,Value}|Hds]}));
        
        http_echo ->
            ok= inet:setopts(M,[{packet,raw}]),
        {Status,Hd,Respons} = try Manager(M,Rec) catch _:_ -> {500,[],<<>>} end,
        ok = gen_tcp:send(M,["HTTP - FIRST RUN ", integer_to_list(Status), "\r\n",[[H, ": ",V, "\r\n"] || {H,V}<-Hd],"\r\n",Respons]),
        gen_tcp:close(M);
        {http_error, Error}->
            exit(Error);
        ok->ok
    end.
