#!/bin/bash
set -e -u

#
# invoke integration test
#

cd "${BASH_SOURCE%/*}/.."

#./mvnw.sh clean verify -B -D invoker.test=test-groovyscript

#./mvnw.sh clean verify -B -D invoker.test=test-javascript

#./mvnw.sh clean verify -B -D invoker.test=test-scalascript

./mvnw.sh clean verify -B -D invoker.test=test-mvelscript
