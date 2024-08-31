#!/bin/bash
input="test/view_creation/other_algorithms/view_as_property/paper_experiment.txt"
while IFS= read -r line
do
  java -cp "lib2/*:src/" main.Main cold "$line"
  echo "************************************"
done < "$input"