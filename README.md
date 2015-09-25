# Install

Use raspi-config to enable SPI, I2C, and Force audio to 3.5mm jack.

Load the correct I2C module by putting these lines in /etc/modules

i2c-bcm2708 
i2c-dev

Run these commands to set this code to launch as scratch would and then use raspi-config to set boot to scratch:

sudo mv /usr/bin/scratch /usr/bin/scratch-old

sudo ln -s /home/pi/USSConstitutionMuseum-CannonForce/USSCM-CannonForce-Java/run.sh /usr/bin/scratch

For debugging the led panels use the command "gpio i2cd" which can be installed using the following command:

git clone git://git.drogon.net/wiringPi

./build

# Hardware setup

##serial line

1 gray top left

2 white mid left

3 black btm left

4 brown main btn

6   red   power

7 orange led

8 yellow btm right

9   green  mid right switch

10 blue top right


##LCD

VCC : 5v

GND : gnd

SDA : GP0

SCL : GP1


##Potentiometer 

5v <-> AD1

GP8 : CSnA

GP9 : MISO

GP10 : MOSI

GP11 : SCLK

##Switches

GP21 : 1 gray top left : Wood Type 1

GP22 : 2 white mid left : Wood Type 2

GP23 :  3 black btm left : Wood Type 3

GP24 : 7 orange led

GP25 : 4 brown main btn



# Dev

The application starts with run.sh. The Java class "Main" is started. 




