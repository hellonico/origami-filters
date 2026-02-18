#!/bin/bash
if [ -d "$HOME/.sdkman/candidates/java/17.0.14-jbr" ]; then
    export JAVA_HOME="$HOME/.sdkman/candidates/java/17.0.14-jbr"
elif [ -d "$HOME/.sdkman/candidates/java/11.0.14.1-jbr" ]; then 
    export JAVA_HOME="$HOME/.sdkman/candidates/java/11.0.14.1-jbr"
fi
./gradlew publishToMavenLocal
