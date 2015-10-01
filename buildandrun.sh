#!bin/bash
VERSION=1.2
gradle build
rm -rf server-$VERSION
unzip build/distributions/server-$VERSION.zip
./server-1.2/bin/server
