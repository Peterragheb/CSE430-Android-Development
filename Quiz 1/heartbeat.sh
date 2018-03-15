#!/bin/bash
echo "——————"
counter=0
while [ : ]
do
    counter=$((counter+1))
    echo "HEART-BEAT"
    date
    echo "Hostname : $(hostname)"
    sleep 10
    ping -c 1 192.168.0.101
    echo "——————"
    echo $counter
    echo "——————"
done
