#!/bin/bash

export GWT_HOME=/Users/deuce/Downloads/gwt-mac-1.4.62

CL="src/main/java:.:$GWT_HOME/gwt-user.jar:$GWT_HOME/gwt-dev-mac.jar:/Users/deuce/Downloads/Applications/mygwt-0.5.2/mygwt.jar:/Users/deuce/.m2/repository/gwttk/gwttk/0.2.3/gwttk-0.2.3.jar:/Users/deuce/.m2/repository/gwtext/gwtext/2.0.3/gwtext-2.0.3.jar"

java -Xmx512m -cp "$CL" com.google.gwt.dev.GWTCompiler -out "src/main/webapp" net.deuce.clmanager.gwt.main.Main
