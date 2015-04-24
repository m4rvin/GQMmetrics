#!/bin/bash
mvn jetty:run -P prod -DskipTests=false -Djetty.port=9999 -Ddb.name=gqm_s_ms_prod

