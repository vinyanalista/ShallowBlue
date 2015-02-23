#!/bin/sh
SCRIPT_FOLDER=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )
cd $SCRIPT_FOLDER
xboard -cp -fcp "java -jar ShallowBlue.jar -xboard" -fd "$SCRIPT_FOLDER" -scp "java -jar ShallowBlue.jar -xboard" -sd "$SCRIPT_FOLDER" -coords -debug