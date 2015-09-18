#!/bin/bash

DIR=/home/pi/USSConstitutionMuseum-CannonForce/USSCM-CannonForce-Java


#X_ARGS="-Dsun.java2d.noddraw=true -Dsun.java2d.opengl=false"
export DISPLAY=:0.0
echo $DIR
cd $DIR
sudo java -Xmx128m -cp `sh getclasspath.sh`:classes Main




