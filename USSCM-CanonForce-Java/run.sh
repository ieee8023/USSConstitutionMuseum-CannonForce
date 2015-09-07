#!/bin/bash

java -Xmx128m -cp `sh getclasspath.sh`:classes CanonForce $@
