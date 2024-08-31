#!/bin/bash
input="test/view_use/materialized/other_algorithms/view_as_property/experiment.txt"

for i in {1..5}
do
  while IFS= read -r line
  do
    # Be sure to set the command to the correct thing
    java -cp "lib2/*:src/" main.Main cold "$line"
    echo "************************************"
  done < "$input"
done