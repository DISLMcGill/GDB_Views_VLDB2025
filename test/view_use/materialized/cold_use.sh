#!/bin/bash
input="test/view_use/materialized/experiment_scripts/thesis/cold_experiment.txt"
while IFS= read -r line
do
  # Be sure to set the command to the correct thing
  java -cp "lib2/*:src/" main.Main cold_use "$line"
  echo "************************************"
done < "$input"