# CONSAIR Booking-system

_________  ________    _______    _________   _____  ._____________ 
\_   ___ \ \_____  \   \      \  /   _____/  /  _  \ |   \______   \
/    \  \/  /   |   \  /   |   \ \_____  \  /  /_\  \|   ||       _/
\     \____/    |    \/    |    \/        \/    |    \   ||    |   \
 \______  /\_______  /\____|__  /_______  /\____|__  /___||____|_  /
        \/         \/         \/        \/         \/            \/ 

OSPP (1DT096) 2015 - Grupp 04.

Projektarbete på kursen Operativsystem och processorienterad
programmering (1DT096) våren 2015, Uppsala universitet.

This program is a bookingsystem for an airliner/airport.
> - https://help.github.com/articles/markdown-basics/

> Det går att redigera, förhandsvisa och spara (commit) sidan direkt i
> en webläsare via [projektsidan på github.com](./README.md).

## Installera, Kompilera och Starta

###Compiling and loading the DB.
####First make sure that you have the following software installed.
1. MySQL server ver: 5.5.43-0ubuntu0.14.04.1
2. Amnesia ver: amnesia-1.6.2
3. Erlang/OTP ver: 17
4. Maven any version
5. Java Development Kit (JDK) ver: 1.7 or above

####When you are installing the MySql-server you need to setup an root password make sure you have this in hand during this installation.

####When you have completede the prior-steps
4. run make init_db password=<your SQL rootPassword>
   This will create the Database structure and run a load script for the Database you will need to enter your password one time per Database table.
5. If you dont get any errors you are done.
 
#### Make and maven.

##### Follow these steps after setting up the database

1. Run ´make´ to compile the system code
2. Run ´make start_server´ to start the server
3. Go to src/JavaNetLib, enter ´mvn install´ to compile the network module. This will install the network module in the local maven repository on your computer. This is needed in order to compile the GUI.
4. Go to src/pivotgui and enter ´mvn package´ to compile the GUI
5. In src/pivotgui/target, enter ´java -jar PivotGUI-jar-with-dependencies.jar´ to start the GUI.


##### To reload code (using Erlangs build in code swapping)

Run ´make reload_code´


## Testa

Run ´make test_$$$$´, where $$$$ is the name of the .erl file that has tests written in eunit.

Running ´make test_server´ will override the previous statment and start a special test where a server is initiated in case there is not already one. 

To run the tests for the network library, go to src/JavaNetLib and enter ´mvn test´. (Maven must be installed in order for this to work)
## Starta systemet

#### Make

##### To start server:

Run ´make start_server´

It will, using Make, check if a server is already running. This is done by checking if there is a "current-session" file in the server-messages folder. This can cause problems in the event of a crash. In that case, manually remove or rename the current-session file.

##### To shut down server:

Run ´make stop_server´

## Documentation
To generate documentation enter ´make doc´ for the Erlang files. For the network library, go to src/JavaNetLib and enter ´mvn javadoc:javadoc´ (Maven must be installed in order for this to work) and documentation will be generated in the subfolders target/site.

## Struktur

Projektet består av följande kataloger.

### doc

Dokumentation, projektrapporter och andra viktiga dokument.

### ebin

Erlang beam-filer.

> Ändra till `bin` eller `build` eller liknande om projektet inte
> använder Erlang.

### meta

- Presentation av gruppens medlemmar.
- Gruppkontrakt.
- Projektdagböcker.
- Reflektioner på gruppens arbete.

### src

All källkod.

## Fler rubriker

> Lägg till mer information allt eftersom.
