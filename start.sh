#!/bin/bash
mvn jetty:run -P prod -DskipTests=true -Djetty.port=9999 -Ddb.name=GQM_BLIND_PROD

