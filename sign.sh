#!/bin/bash

PASS=123456
KEYSTORE=../keystore
VERSION=$(perl -0lne 'm|<version>(.*)</version>|g && print "$1"' pom.xml)

if [ ! -e ${KEYSTORE} ]; then
  keytool -genkey -alias starjeweled -keystore ${KEYSTORE} -storepass $PASS \
    -keypass $PASS -validity 3650
fi

jarsigner -keystore ${KEYSTORE} -storepass $PASS -keypass $PASS \
  target/starjeweled-${VERSION}.jar starjeweled
