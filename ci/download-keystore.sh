#!/usr/bin/env bash

pwd > /path.txt
mkdir -p ~/.ssh
echo -e "Host *\n\tStrictHostKeyChecking no\n\n" > ~/.ssh/config
echo -e "${CI_ANDROID_KEYSTORE_DEPLOY_KEY}" > ~/.ssh/id_rsa
chmod 600 ~/.ssh/id_rsa
git clone --depth 1 --branch master git@bitbucket.org:android-universum/keystore.git /keystore
cd "$(< /path.txt)"