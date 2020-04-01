#!/bin/sh

cd env/integration || exit
docker build -t trello-server-base:1.0 .
