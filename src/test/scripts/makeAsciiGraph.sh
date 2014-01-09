#!/bin/bash


count=0
ys=$(grep '<count>' $1 | cut -d'<' -f2 | cut -d'>' -f2)

for y in $ys; do
  echo $count $y
  count=$[count+1]
done