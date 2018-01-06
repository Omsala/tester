#!/bin/bash
clear
echo START EXECUTING SQL INSERT TO DB.
echo ENTER PASSWORD FOR ROOT SQL USER
echo YOU NEED TO ENTER THIS 4 TIMES
mysql -u root -p consair_database < ./src/DatabaseBackend/pop/user.sql
mysql -u root -p consair_database < airport.sql
mysql -u root -p consair_database < flights.sql
mysql -u root -p consair_database < seats.sql
echo DB is now populated.
