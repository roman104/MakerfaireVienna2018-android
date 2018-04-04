#!/usr/bin/env bash

[[ -f ${FILE_VERSION_PROPERTIES} ]] && echo "Found 'version.properties' file on the CI server :)" || echo "File 'version.properties' not found on the CI server at path '${FILE_VERSION_PROPERTIES}'!"