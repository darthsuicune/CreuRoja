#!/bin/bash

if [ "$#" -ne 3 ]; then                                                                                                                                                                                                                                                        
        echo "Usage: $0 <folder> <pattern_to_search> <pattern_to_replace>";
        exit 1;                                                                                                                                                                                                                                                                fi; 

for i in `grep -lR $2 $1`; 
do sed -i "s/$2/$3/g" $i;
done;
exit 0
