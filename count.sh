#!/usr/bin/env bash
find . -name "*.java" -exec grep -l "implements Filter" {} \; | wc -l