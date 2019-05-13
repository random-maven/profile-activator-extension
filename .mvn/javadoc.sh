#!/usr/bin/env bash
set -e -u

#
# produce documentation site
#

cd "${BASH_SOURCE%/*}/.."

#./mvnw.sh javadoc:jar -B -P skip-test

./mvnw.sh help:active-profiles clean install -B -P skip-test,attach-sources,attach-javadoc,sign-artifacts
