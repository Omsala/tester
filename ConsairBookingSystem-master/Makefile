ERLC_FLAGS=-Wall

DIRECTORIES=src/ src/backend/ src/database/

SOURCES=$(wildcard $(DIRECTORIES:/=/*.erl))

HEADERS=$(wildcard $(DIRECTORIES)*.hrl)

BEAMS:=$(addprefix ebin/, $(notdir $(SOURCES:.erl=.beam)))

APPNAME= ConsAir_Bookingsystem

.PHONY: doc doc_url skeleton start_server

all: $(BEAMS)

ebin/%.beam: src/%.erl
	erlc $(ERLC_FLAGS) -o ebin/ $<

ebin/%.beam: src/backend/%.erl
	erlc $(ERLC_FLAGS) -o ebin/ $<

ebin/%.beam: src/database/%.erl
	erlc $(ERLC_FLAGS) -o ebin/ $<

clean:
	rm -Rf ebin/*
	rm -Rf src/*.beam
	rm -Rf src/backend/*.beam
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

client_terminal:
	java -jar src/JavaNetLib/target/JavaNetLib.jar

init_db: all
	erl -pa ebin/ -run make_db init $(password) -s init stop -noshell
	./DBfiles/SQL_load.sh


### EUnit ###

# Make a comma separated list:
# http://ftp.gnu.org/old-gnu/Manuals/make-3.79.1/html_chapter/make_8.html

comma:= ,
empty:=
space:= $(empty) $(empty)

OBJECTS_LIST:= $(subst $(space),$(comma),$(OBJECTS:ebin/%.beam=%))

test: $(OBJECTS)
	erl -noshell -pa ebin -eval 'eunit:test([$(OBJECTS_LIST)], [])' -s init stop

testv: $(OBJECTS)
	erl -noshell -pa ebin -eval 'eunit:test([$(OBJECTS_LIST)], [verbose])' -s init stop

test_server: ebin/server.beam start_server server-messages/current-session
	erl -noshell -pa ebin -eval "eunit:test(server, [])" -s init stop
	erl -pa ebin/ -s server_utils stop_server -s init stop -noshell	

test_%: ebin/%.beam
	erl -noshell -pa ebin -eval "eunit:test($(subst test_,, $@), [])" -s init stop

testv_%: ebin/%.beam
	erl -noshell -pa ebin -eval "eunit:test($(subst testv_,, $@), [verbose])" -s init stop

