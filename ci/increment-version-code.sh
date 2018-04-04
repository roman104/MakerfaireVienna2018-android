#!/usr/bin/env bash

# Version properties file is expected as argument.
FILE=${1}
if [ ! -f ${FILE} ]; then
	# File does not exist.
    exit 1
fi

# Function which extracts a specific property by its name/key from properties file.
function prop {
    grep "${1}" ${FILE} | cut -d'=' -f2
}

# Obtain current build code, increment it, and write that incremented value back
# to the properties file.
BUILD_CODE=$(prop 'build.code')
echo "Incrementing build code '${BUILD_CODE}' by +1."
((BUILD_CODE=BUILD_CODE+1))
echo "build.code=${BUILD_CODE}" > ${FILE}

exit 0