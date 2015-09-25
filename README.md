# USSConstitutionMuseum-CanonForce

Use raspi-config to enable SPI, I2C, and Force audio to 3.5mm jack.

Load the correct I2C module by putting these lines in /etc/modules

i2c-bcm2708 
i2c-dev

Run these commands to set this code to launch as scratch would and then use raspi-config to set boot to scratch:

sudo mv /usr/bin/scratch /usr/bin/scratch-old

sudo ln -s /home/pi/USSConstitutionMuseum-CannonForce/USSCM-CannonForce-Java/run.sh /usr/bin/scratch

For debugging

git clone git://git.drogon.net/wiringPi
./build
