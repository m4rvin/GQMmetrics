#!/bin/bash
mvn jetty:run -P prod -DskipTests=false -Djetty.port=9999 -Ddb.name=GQM_BLIND_PROD

