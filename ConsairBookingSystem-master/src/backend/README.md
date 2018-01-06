# Source code for backend-related files

## Compiling and testing

### Make.

#### To compile everything

Run ´make´

#### To reload code (using Erlangs build in code swapping)

Run ´make reload_code´

#### To test

Run ´make test_$$$$´, where $$$$ is the name of the .erl file that has tests written in eunit.

Running ´make test_server´ will override the previous statment and start a special test where a server is initiated in case there is not already one. 

## Running

### Make

#### To start server:

Run ´make start_server´

It will, using Make, check if a server is already running. This is done by checking if there is a "current-session" file in the server-messages folder. This can cause problems in the event of a crash. In that case, manually remove or rename the current-session file.

#### To shut down server:

Run ´make stop_server´