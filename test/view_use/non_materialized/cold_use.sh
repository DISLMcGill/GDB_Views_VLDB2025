#!/bin/bash
input="test/view_use/non_materialized/experiment_scripts/local_basic_cold_experiment.txt"
while IFS= read -r line
do
  # The command shoud be modified with the type of the cold query you want to run
  java -cp "lib2/*:src/" main.Main cold_use "$line"
  echo "************************************"
done < "$input"