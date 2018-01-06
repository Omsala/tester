ERLC_FLAGS=-Wall

.PHONY: doc 

doc:
	erl -noshell -run edoc_run files  '["get_database.erl","consair_database.erl"{./doc}]' 

#erl -noshell -run edoc_run application "'$(APPNAME)'"  '"."' '[{def,{vsn,"$(VSN)"}}, {stylesheet, "my_style.css"}]'

