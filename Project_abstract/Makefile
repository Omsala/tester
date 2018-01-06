ERLC_FLAGS=-Wall

DIRECTORIES=src/ src/backend/ src/database/

SOURCES=$(wildcard $(DIRECTORIES:/=/*.erl))

HEADERS=$(wildcard $(DIRECTORIES)*.hrl)

BEAMS:=$(addprefix ebin/, $(notdir $(SOURCES:.erl=.beam)))

APPNAME= Basic_Webserver

.PHONY: doc doc_url skeleton start_server

all: $(BEAMS)

ebin/%.beam: src/%.erl
	erlc $(ERLC_FLAGS) -o ebin/ $<


clean:
	rm -Rf ebin/*
	rm -Rf src/*.beam
	rm -Rf doc/*.html


doc: 	
	@echo
	@echo "To make the documents for erlang please go to" 
	@echo "./doc and run the makefile"
	@echo	

doc_url:
	@echo 
	@echo "EDoc index page available at file://$(PWD)/doc/index.html"
	@echo

edoc_firefox:
	firfox /doc/index.html

start_server: all server-messages/current-session

server-messages/current-session:
	gnome-terminal -x sh -c "erl -pa ebin/ -s server -s init stop -noshell | tee server-messages/current-session; ./server-messages/save-session; bash;"

start_server_noterm: all
	erl -pa ebin/ -s server -s init stop -noshell | tee server-messages/current-session; ./server-messages/save-session; bash;

stop_server: all
	erl -pa ebin/ -s server_utils stop_server -s init stop -noshell

reload_code: all
	erl -pa ebin/ -s server_utils reload_code -s init stop -noshell



