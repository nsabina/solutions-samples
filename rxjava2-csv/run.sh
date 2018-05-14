#! /bin/bash

declare JAR_FILENAME=target/csv-validator-1.0-SNAPSHOT-jar-with-dependencies.jar
declare JVM_OPTIONS="-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/tmp/heapdump.log"
declare INPUT=src/main/resources/input/pii.csv
declare OUTPUT=src/main/resources/output/output.json

declare JAVA_RUN_COMMAND="java ${JVM_OPTIONS} -jar ${JAR_FILENAME} ${INPUT} ${OUTPUT} "


echo ' Running consumer ******* ' ${JAVA_RUN_COMMAND}

${JAVA_RUN_COMMAND}

retval=$?
if [ $retval -ne 0 ]; then
    echo "[ERROR] $1  (err=$?)."
fi
