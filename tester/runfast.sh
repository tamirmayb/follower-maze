#!/bin/bash
set -eux -o pipefail

export totalEvents=100000
export concurrencyLevel=3

time java -Xmx1G -jar "$(dirname "$0")/follower-maze-2.0.jar"
