#!/bin/bash
for ((i=1; i<=5; i++))
do
    filename="round_${i}"
    filepath="test/view_use/materialized/experiment_scripts/local_complex/paper_exps/${filename}.txt"

    java -cp "lib2/*:src/" main.Main "$filepath"
    echo "--------------------------------------"
done