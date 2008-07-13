#!/bin/bash

mysql --user=deuce --password=qopklm1 clmanager < $(dirname $0)/init.sql
