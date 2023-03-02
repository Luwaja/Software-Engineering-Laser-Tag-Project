#!/bin/bash
set -u -e
javac Player.java LaserTag.java
java LaserTag
