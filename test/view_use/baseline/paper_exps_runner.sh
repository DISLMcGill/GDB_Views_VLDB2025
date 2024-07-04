#!/bin/bash
for ((i=1; i<=5; i++))
do
    filename="round_${i}"
    filepath="test/view_use/baseline/experiment_scripts/paper_exps/global/${filename}.txt"

    java -cp "lib2/*:src/" main.Main "$filepath"
    echo "--------------------------------------"
done