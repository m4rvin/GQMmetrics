#!/bin/bash
mvn jetty:run -P prod -DskipTests=true -Djetty.port=9999 -Ddb.name=gqm_s_ms_prod

