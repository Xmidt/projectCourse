#!/bin/bash
FILES=/home/mads/DIKU/ps/git/projectCourse/eSim/repo/*.jar
cd /DIKU/ps/git/projectCourse/eSim/repo/


for f in $FILES
do
   filename=$(basename "$f")
   echo "Processing $f files.."
   echo $filename
   mkdir ${filename%.*}
   mkdir ${filename%.*}/${filename%.*} 
   mkdir ${filename%.*}/${filename%.*}/3.0.0
   mv $f ${filename%.*}/${filename%.*}/3.0.0/${filename%.*}-3.0.0.jar

   echo "<dependency>" >> pomentries.xml
   echo "  <groupId>${filename%.*}</groupId>" >> pomentries.xml
   echo "  <artifactId>${filename%.*}</artifactId>" >> pomentries.xml
   echo "  <version>3.0.0</version>" >> pomentries.xml
   echo "</dependency>" >> pomentries.xml

done
