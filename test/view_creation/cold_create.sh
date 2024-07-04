#!/bin/bash
input="test/view_creation/create_experiment_paper.txt"
while IFS= read -r line
do
  java -cp "lib2/*:src/" main.Main cold "$line"
  echo "************************************"
done < "$input"