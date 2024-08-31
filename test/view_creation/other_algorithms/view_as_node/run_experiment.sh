#!/bin/bash
input="test/view_creation/other_algorithms/view_as_node/create_experiment.txt"

#for i in {1..5}
#do
   # Running the python script to shuffle the commands
echo "Shuffling the create commands"
python3 test/view_creation/other_algorithms/view_as_node/random_cmd_gen.py
java -cp "lib2/*:src/" main.Main method2 $input
echo "************************************"
#done