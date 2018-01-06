#!/bin/bash
clear
echo START EXECUTING SQL INSERT TO DB.
echo ENTER PASSWORD FOR ROOT SQL USER
echo YOU NEED TO ENTER THIS 4 TIME
echo Loading User
mysql -u root -p consair_database < ./DBfiles/user.sql
echo Loading Airport
mysql -u root -p consair_database < ./DBfiles/airport.sql
echo Loading Flights
mysql -u root -p consair_database < ./DBfiles/flights.sql
echo Loading Seats
mysql -u root -p consair_database < ./DBfiles/seats.sql
echo DB is now Loaded.
echo ────────────────────░███░
echo ───────────────────░█░░░█░
echo ──────────────────░█░░░░░█░
echo ─────────────────░█░░░░░█░
echo ──────────░░░───░█░░░░░░█░
echo ─────────░███░──░█░░░░░█░
echo ───────░██░░░██░█░░░░░█░
echo ──────░█░░█░░░░██░░░░░█░
echo ────░██░░█░░░░░░█░░░░█░
echo ───░█░░░█░░░░░░░██░░░█░
echo ──░█░░░░█░░░░░░░░█░░░█░
echo ──░█░░░░░█░░░░░░░░█░░░█░
echo ──░█░░█░░░█░░░░░░░░█░░█░
echo ─░█░░░█░░░░██░░░░░░█░░█░
echo ─░█░░░░█░░░░░██░░░█░░░█░
echo ─░█░█░░░█░░░░░░███░░░░█░
echo ░█░░░█░░░██░░░░░█░░░░░█░
echo ░█░░░░█░░░░█████░░░░░█░
echo ░█░░░░░█░░░░░░░█░░░░░█░
echo ░█░█░░░░██░░░░█░░░░░█░
echo ─░█░█░░░░░████░░░░██░
echo ─░█░░█░░░░░░░█░░██░█░
echo ──░█░░██░░░██░░█░░░█░
echo ───░██░░███░░██░█░░█░
echo ────░██░░░███░░░█░░░█░
echo ──────░███░░░░░░█░░░█░
echo ──────░█░░░░░░░░█░░░█░
echo ──────░█░░░░░░░░░░░░█░
echo ──────░█░░░░░░░░░░░░░█░
echo ──────░█░░░░░░░░░░░░░█░
echo AWESOME

