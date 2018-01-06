# How to demo server_prototype

Open two terminals and go to `src/`.

Open the erlang shell in both terminals using `erl`.

Compile the server_prototype module using `c(server_prototype).`.

Choose one terminal to be server and one to be client.

## Terminal

Run `server_prototype:start_servet(PORT).`.

* **PORT**: Port number you want to use. 
* *Ex*: 33333.

The server is now ready to print to terminal and respond to client. 

The server will wait for a connection and spawn a process to deal with each seperate connection. It will then receive a message and send back an applicable answer according to this table:
	
| "Hello"   | "Hello"                    |
|-----------|----------------------------|
| "Foo"     | "Bar"                      |
| "Ipsum"   | "Lorem"                    |
| <<1,2,3>> | "One, Two, Three"          |
| _         | "I didn't understand that" |

There is no way to close the server using commands yet, so just kill the process with ^G, K N (kill job N), S (start new shell), and C N (connect to job N). 

## Client

Run `server_prototype:start_client(IP, PORT, MESSAGE).`.

* **IP**: The IP you want to use. When using two terminals on the same computer use "localhost" (including the quotation marks)
* *Ex*: 123.456.789.910 or "localhost"

* **PORT**: Port number you want to use. 
* *Ex*: 33333.

* **MESSAGE**: A list of messages to send. This list should only include bit strings or regular string (as they can be considered bit strings).
* *Ex*: ["Hello", "World", <<"Foo">>, <<"Bar">>, <<1,2,3>>, <<1,2,3,4,5,6,7,8,9,10>>]

*Ex*: `server_prototype:start_client("localhost", 3333, ["Hello", "Foo", "Ipsum", <<1,2,3>>, "What"]).`

When this is run, it will spawn a process for each message. The process will wait a random amount of time, then send the message. It will then recieve a message from the server and the connection is terminated. 