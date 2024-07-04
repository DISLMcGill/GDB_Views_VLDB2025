#!/bin/bash
# Input should be set
input="test/view_use/baseline/experiment_scripts/baseline_cold_local_complex_new.txt"
while IFS= read -r line
do
  java -cp "lib2/*:src/" main.Main cold "$line"
  echo "************************************"
done < "$input"