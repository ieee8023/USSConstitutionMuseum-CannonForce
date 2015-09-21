CPU = 16F877A
MHZ = 20
CONFIG 16246

'I/O DIRECTIONS
TRISA = %00110010
TRISB = %10111111  
TRISC = %00000000
TRISD = %00000000
'TRISE = %11111011

Output E0
Input E1
Output E2

'_1 : Borrowed from Iterfacing technologies (Attune RTD)

'_8 : Works pretty well.  mag2 a bit screwy.
'_9 : Ok, works well now.  IF Distance =0, Fire will be lit !!  Fix.

'_11 : FW for new Display board.  Will drive MAX7219
'		Removed references to LCD
'_12 : Now must take a number (0-9999) and display on 4 digit LED - Beli
'_13 : Get second display working
'_14 : use distance and force
'_15 : clean up
'_16 : add in switch states
'_17 : Entire systme working well.  Just a bit blinky.  Smooth ADC and "zero out" least significant digit of Distance

'Initial States




'Update 5/9/12 for USS Constitution project

'SPI - MSB first, data latched on CLKs rising edge

RS 	con C0 			'OUT : CLK to Max7219 -Serial-Clock Input. 10MHz maximum rate. On CLK’s rising edge, data is shifted into the internal
					'shift register. On CLK’s falling edge, data is clocked out of DOUT. 
E 	con C1 			'OUT : LOAD to Max7219 - Load-Data Input. The last 16 bits of serial data are latched on LOAD’s rising edge.
DB5 CON C5			'OUT DOUT to Max7219 - Serial-Data Input. Data is loaded into the internal 16-bit shift register on CLK’s rising edge.

SW6 con A4			'IN - Magnetic Switch 6
FIRE con A5			'IN TO PIC - Magnetic Switch 7

SW3 con B0 			'IN TO PIC - Magnetic Switch 3
SW2 con B1			'IN TO PIC - Magnetic Switch 2
SW5 con B2			'IN TO PIC - Magnetic Switch 5
SW1 CON B4			'IN TO PIC - Magnetic Switch 1
SW4 con B5			'IN TO PIC - Magnetic Switch 4
'FIRELED con B6			'OUT FIRE LED


RA0 con A0			'Spare (IN/OUT) TO PIC
	
IO con A1			'BIDIR- RTC DATA
Clk	 con A3			'OUT FROM PIC - RTC CLK
CE con A2			'OUT FROM PIC - RTC CE




SW9 con B7			'IN TO PIC - ACON Input



LIGHTS con C2		'OUT FROM PIC - pwm output - Dim Switch LEDs

TX con B3			'IN to PIC - RS232 Input
RX	con C3			'OUT FROM PIC - RS232 output 

DB4 CON C4			'OUT FROM PIC - LCD Data bit 

DB6 CON C6			'OUT FROM PIC - LCD Data bit 
DB7 CON C7			'OUT FROM PIC - LCD Data bit 

AUXON con D0		'OUT FROM PIC - Aux. Relay
VAON con D1 		'OUT FROM PIC , High = power to relay
VBON con D2 		'OUT FROM PIC			
VBDIR con D3 		'OUT FROM PIC	
CONON con D4		'OUT FROM PIC			
VCDIR con D5 		'OUT FROM PIC			
VCON con D6 		'OUT FROM PIC		
VADIR con D7		'OUT FROM PIC , Low = CW  , High=CCW , normally LOW (CW)

FIRELED con E0 		'OUT FROM PIC	
'LED3 con E1			'OUT FROM PIC
LED2 con E2			'OUT FROM PIC


;Variables
;---------------	

debounce var byte	

Switch Var Byte

work var byte

mag1 var bit
mag2 var bit
mag3 var bit
mag4 var bit
mag5 var bit
mag6 var bit
mag7 var bit

LCDNib  	Var PortC.Nib1	'C4 - Pin 13
							'C5 - Pin 14
							'C6 - Pin 15 
							'C7 - Pin 16
				
				
ADCounts Var	Word
ad var word(5)


ADCLK con 2
ADSETUP con %10001110

baud Con i19200
							
Distance var word

moved var bit


Olddistancelow var word
Olddistancehigh var word

force var word

mag var byte
dis var byte

i var word
n var byte

j var word
m var byte



digval var byte(9)



showforce var bit



mag1=0
mag2=0
mag3=0
mag4=0
mag5=0
mag6=0
mag7=0


mag=0
dis=0

ADCounts=0

showforce=0

moved=0



setup:

'test LED

'@high LED1
'pause 500


'@low LED1
'pause 500
'goto setup



@high E	  'Load (CS) deselected max7219

gosub init_max7219
Gosub BLANK



SSPCON.BIT5 = 0


Olddistancelow=4000


@low FIRELED

'Fire Light OFF
'Display Distance based on linear position POT
'LBS of Force not shown

Main:

Gosub adc

gosub digits 






If Olddistancelow=4000 then
goto firsttimethrough
endif

'Add Detection of Magnetic switch CHANGE
'If


If Distance < Olddistancelow OR Distance > Olddistancehigh AND Distance> 10 Then    'distance changed morethan 5 yds - goto Fire Light ON
moved=1
else 
moved=0
endif




firsttimethrough:

Olddistancelow = distance - 10

If Olddistancelow < 0 Then
Olddistancelow=0
endif

Olddistancehigh = distance + 10




	If  moved=1 Then   'detect cannon movement, Fire light on
	moved=0
	goto Firelighton
	endif


'pause 200   'smooth out blinking

goto Main



Firelighton:

@high FIRELED	'set LED lights on (100%)
		
Gosub adc

gosub digits 		

    
If porta.bit5 = 0 then	'FIRE button pressed
	stay:
	While porta.bit5 = 0 '
	goto stay
	wend
	
	@low FIRELED	'set LED lights off	

	'Gosub checkmags
	
	showforce=1
	
	Goto firescreen
endif



'Gosub checkmags

'pause 200   'smooth out blinking

Goto Firelighton
	






adc:

'Try smoothing adc readings

For i=1 to 5

	ADIN RA0,ADCLK,ADSETUP,AD(i)	'reads Battery (fed into PIC ADC)
	
	next
	
	ADCounts = (AD(1)+AD(2)+AD(3)+AD(4)+AD(5))/5
	

	Distance = (ADCounts * 3) '+ 15

If Distance<0 Then
Distance=0
endif

If Distance > 2800 Then
Distance=2800
endif

dis=distance/100   'just for sending to serial port

force = 167 - (distance*4/100)


	Return





checkmags:

If portb.bit4 = 0 then	
mag1=1
else
mag1=0
endif

If portb.bit1 = 0 then	
mag2=1
else
mag2=0
endif

If portb.bit0 = 0 then	
mag3=1
else
mag3=0
endif

If portb.bit5 = 0 then	
mag4=1
else
mag4=0
endif


If portb.bit2 = 0 then	
mag5=1
else
mag5=0
endif

If porta.bit4 = 0 then	
mag6=1
else
mag6=0
endif
	
	mag.BIT0 = mag1
	mag.BIT1 = mag2
	mag.BIT2 = mag3
	mag.BIT3 = mag4
	mag.BIT4 = mag5
	mag.BIT5 = mag6
	mag.BIT6 = 0
	mag.BIT7 = 0
	
	
	Return
	
	
	

	
	SerialOut:   'Send Packet out Rs232 transceiver
				'Will send one of ten numbers, representing one of 10 (animated) scenes to be displayed on the PC
	
	
	

	
	serout C3,baud,[mag,dis,13]	'works ok
	
	
	
	Return
	
	
		firescreen:


			gosub digits ' send distance and force to LEDs
			
			gosub checkmags
			
			gosub SerialOut
			
			Pause 10000
			
			showforce=0
			
			Gosub Blank
			
			moved=0
			
			Olddistancelow=4000
			
	Goto Main
	
	
	
	
	init_max7219:
	
	
	@low E			' Select MAX7219
	shiftout DB5,RS,MSBPRE,[0x09\8]	' BCD mode for digit decoding
	shiftout DB5,RS,MSBPRE,[0xFF\8]	'
	@high E			 ' Deselect MAX7219
	
	@low E			' Select MAX7219
	shiftout DB5,RS,MSBPRE,[0x0A\8]	'Segment luminosity intensity
	shiftout DB5,RS,MSBPRE,[0x0F\8]	'
	@high E			 ' Deselect MAX7219
	
	@low E			' Select MAX7219
	shiftout DB5,RS,MSBPRE,[0x0B\8]	' Set scan-limit
	shiftout DB5,RS,MSBPRE,[0x07\8]	'Display all 8 digits
	@high E			 ' Deselect MAX7219
	
	@low E			' Select MAX7219
	shiftout DB5,RS,MSBPRE,[0x0C\8]	' Set Shutdown register
	shiftout DB5,RS,MSBPRE,[0x01\8]	'Normal operation
	@high E			 ' Deselect MAX7219

	@low E			' Select MAX7219
	shiftout DB5,RS,MSBPRE,[0xFF\8]	' 
	shiftout DB5,RS,MSBPRE,[0x00\8]	'No test
	@high E			 ' Deselect MAX7219
	
              

	Return
	
	
	
	    Blank:
    
      for i = 1 to 8			'1 is DIGIT 0 , 8 is DIGIT 7   (DIG 0-7)
    @low E                  ' Select MAX7219
    shiftout DB5,RS,MSBPRE,[i\8]	' Send i to MAX7219 (digit place)
	shiftout DB5,RS,MSBPRE,[0x0F\8] 'Send 8-i to MAX7219 (digit value)
    @high E                  ' DeSelect MAX7219
       
  next 
    
    
	Return
	
	
	
	digits:
	
	
	i=distance
	
	
	 	If i<10 Then  '0-9
 	digval(1)=0x0F		'blank
 	digval(2)=0x0F
 	digval(3)=0x0F
 	digval(4)=i
 	 	
 	elseif i<100    '10-99
 	digval(1)=0x0F		'blank
 	digval(2)=0x0F
 	digval(3)=i/10
 	digval(4)=i-(digval(3)*10)
 	 	
 	elseif i<1000	'100-999
 	
 	digval(1)=0x0F		'blank
 	digval(2)=i/100
 	digval(3)=(i-(digval(2)*100))/10
 	digval(4)=(i-(digval(2)*100))-(digval(3)*10)
 	
 	else	'1000-9999

 	digval(1)=i/1000					
 	digval(2)=(i-(digval(1)*1000))/100  
 	digval(3)=((i-(digval(1)*1000))-(digval(2)*100))/10
 	digval(4)=i-(digval(1)*1000)-(digval(2)*100)-(digval(3)*10)

endif

'pause 100

 	If force<10 Then  '0-9
 	digval(5)=0x0F		'blank
 	digval(6)=0x0F
 	digval(7)=0x0F
 	digval(8)=force
 	 	
 	elseif force<100    '10-99
 	digval(5)=0x0F		'blank
 	digval(6)=0x0F
 	digval(7)=force/10
 	digval(8)=force-(digval(7)*10)
 	 	
 	elseif force<1000	'100-999
 	
 	digval(5)=0x0F		'blank
 	digval(6)=force/100
 	digval(7)=(force-(digval(6)*100))/10
 	digval(8)=(force-(digval(6)*100))-(digval(7)*10)
 	
 	else	'1000-9999

 	digval(5)=force/1000					
 	digval(6)=(force-(digval(5)*1000))/100  
 	digval(7)=((force-(digval(5)*1000))-(digval(6)*100))/10
 	digval(8)=force-(digval(5)*1000)-(digval(6)*100)-(digval(7)*10)

endif
	
	If showforce=0 Then     'Fire button not pushed so Force LEDs blank
	 	digval(5)=0x0F		'blank
 		digval(6)=0x0F
 		digval(7)=0x0F
 		digval(8)=0x0F
	endif
	
	'Test
	digval(4)=0 ' Try to stiop blinking by setting least significant digit of Distance to 0
	
	
	For n=1 to 4 ' Distance display test
    @low E                  ' Select MAX7219
    shiftout DB5,RS,MSBPRE,[n\8]	' Send i to MAX7219 (digit place)
	shiftout DB5,RS,MSBPRE,[digval(n)\8] 'Send 8-i to MAX7219 (digit value)
	@high E                  ' DeSelect MAX7219
    
    @low E                  ' Select MAX7219
    shiftout DB5,RS,MSBPRE,[(n+4)\8]	' Send i to MAX7219 (digit place)
	shiftout DB5,RS,MSBPRE,[digval(n+4)\8] 'Send 8-i to MAX7219 (digit value)
    @high E 
    
    
    
   'pause 1000
    
  next    						' The result is "0123" written on the 7-Seg displays
    
    return
	
	
	
end


