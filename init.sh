#!/bin/sh

# Temperature_Sensor
curl -X POST -H "X-M2M-Origin: admin:admin" -H "Content-Type: application/xml;ty=2" http://127.0.0.1:8080/~/in-cse -d '
<m2m:ae xmlns:m2m="http://www.onem2m.org/xml/protocols" rn="TEMPERATURE_SENSOR" >
    <api>temperature-sensor</api>
    <lbl>Type/sensor Category/temperature Location/home</lbl>
    <rr>false</rr>
</m2m:ae>
'

curl -X POST -H "X-M2M-Origin: admin:admin" -H "Content-Type: application/xml;ty=3" http://127.0.0.1:8080/~/in-cse/in-name/TEMPERATURE_SENSOR -d '
<m2m:cnt xmlns:m2m="http://www.onem2m.org/xml/protocols" rn="DESCRIPTOR">
</m2m:cnt>
'

curl -X POST -H "X-M2M-Origin: admin:admin" -H "Content-Type: application/xml;ty=4" http://127.0.0.1:8080/~/in-cse/in-name/TEMPERATURE_SENSOR/DESCRIPTOR -d '
<m2m:cin xmlns:m2m="http://www.onem2m.org/xml/protocols">
    <cnf>application/xml</cnf>
    <con>
        &lt;obj&gt;
            &lt;str name=&quot;type&quot; val=&quot;Temperature_Sensor&quot;/&gt;
            &lt;str name=&quot;location&quot; val=&quot;Home&quot;/&gt;
            &lt;str name=&quot;appId&quot; val=&quot;TEMPERATURE_SENSOR&quot;/&gt;
            &lt;op name=&quot;getValue&quot; href=&quot;/in-cse/in-name/TEMPERATURE_SENSOR/DATA/la&quot; 
         in=&quot;obix:Nil&quot; out=&quot;obix:Nil&quot; is=&quot;retrieve&quot;/&gt;
        &lt;/obj&gt;
    </con>
</m2m:cin>
'

curl -X POST -H "X-M2M-Origin: admin:admin" -H "Content-Type: application/xml;ty=3" http://127.0.0.1:8080/~/in-cse/in-name/TEMPERATURE_SENSOR -d '
<m2m:cnt xmlns:m2m="http://www.onem2m.org/xml/protocols" rn="DATA">
</m2m:cnt>
'

#   =======================================
#Humidity Sensor

curl -X POST -H "X-M2M-Origin: admin:admin" -H "Content-Type: application/xml;ty=2" http://127.0.0.1:8080/~/in-cse -d '
<m2m:ae xmlns:m2m="http://www.onem2m.org/xml/protocols" rn="HUMIDITY_SENSOR" >
    <api>Humidity-sensor</api>
    <lbl>Type/sensor Category/Humidity sensor</lbl>
    <rr>false</rr>
</m2m:ae>
'

curl -X POST -H "X-M2M-Origin: admin:admin" -H "Content-Type: application/xml;ty=3" http://127.0.0.1:8080/~/in-cse/in-name/HUMIDITY_SENSOR -d '
<m2m:cnt xmlns:m2m="http://www.onem2m.org/xml/protocols" rn="DESCRIPTOR">
</m2m:cnt>
'

curl -X POST -H "X-M2M-Origin: admin:admin" -H "Content-Type: application/xml;ty=4" http://127.0.0.1:8080/~/in-cse/in-name/HUMIDITY_SENSOR/DESCRIPTOR -d '
<m2m:cin xmlns:m2m="http://www.onem2m.org/xml/protocols">
    <cnf>application/xml</cnf>
    <con>
        &lt;obj&gt;
            &lt;str name=&quot;type&quot; val=&quot;Humidity_Sensor&quot;/&gt;
            &lt;str name=&quot;location&quot; val=&quot;Home&quot;/&gt;
            &lt;str name=&quot;appId&quot; val=&quot;HUMIDITY_SENSOR&quot;/&gt;
            &lt;op name=&quot;getValueFirst&quot; href=&quot;/in-cse/in-name/HUMIDITY_SENSOR/DATA/la&quot;
            in=&quot;obix:Nil&quot; out=&quot;obix:Nil&quot; is=&quot;retrieve&quot;/&gt;
        &lt;/obj&gt;
    </con>
</m2m:cin>
'

curl -X POST -H "X-M2M-Origin: admin:admin" -H "Content-Type: application/xml;ty=3" http://127.0.0.1:8080/~/in-cse/in-name/HUMIDITY_SENSOR -d '
<m2m:cnt xmlns:m2m="http://www.onem2m.org/xml/protocols" rn="DATA">
</m2m:cnt>
'

#   =======================================
#Light Sensor

curl -X POST -H "X-M2M-Origin: admin:admin" -H "Content-Type: application/xml;ty=2" http://127.0.0.1:8080/~/in-cse -d '
<m2m:ae xmlns:m2m="http://www.onem2m.org/xml/protocols" rn="LIGHT_SENSOR" >
    <api>Light-sensor</api>
    <lbl>Type/sensor Category/Light sensor</lbl>
    <rr>false</rr>
</m2m:ae>
'

curl -X POST -H "X-M2M-Origin: admin:admin" -H "Content-Type: application/xml;ty=3" http://127.0.0.1:8080/~/in-cse/in-name/LIGHT_SENSOR -d '
<m2m:cnt xmlns:m2m="http://www.onem2m.org/xml/protocols" rn="DESCRIPTOR">
</m2m:cnt>
'

curl -X POST -H "X-M2M-Origin: admin:admin" -H "Content-Type: application/xml;ty=4" http://127.0.0.1:8080/~/in-cse/in-name/LIGHT_SENSOR/DESCRIPTOR -d '
<m2m:cin xmlns:m2m="http://www.onem2m.org/xml/protocols">
    <cnf>application/xml</cnf>
    <con>
        &lt;obj&gt;
            &lt;str name=&quot;type&quot; val=&quot;Light_Sensor&quot;/&gt;
            &lt;str name=&quot;location&quot; val=&quot;Home&quot;/&gt;
            &lt;str name=&quot;appId&quot; val=&quot;LIGHT_SENSOR&quot;/&gt;
            &lt;op name=&quot;getValueFirst&quot; href=&quot;/in-cse/in-name/LIGHT_SENSOR/DATA/la&quot;
            in=&quot;obix:Nil&quot; out=&quot;obix:Nil&quot; is=&quot;retrieve&quot;/&gt;
        &lt;/obj&gt;
    </con>
</m2m:cin>
'

curl -X POST -H "X-M2M-Origin: admin:admin" -H "Content-Type: application/xml;ty=3" http://127.0.0.1:8080/~/in-cse/in-name/LIGHT_SENSOR -d '
<m2m:cnt xmlns:m2m="http://www.onem2m.org/xml/protocols" rn="DATA">
</m2m:cnt>
'
#   =======================================
#Temperature on Board Sensor

curl -X POST -H "X-M2M-Origin: admin:admin" -H "Content-Type: application/xml;ty=2" http://127.0.0.1:8080/~/in-cse -d '
<m2m:ae xmlns:m2m="http://www.onem2m.org/xml/protocols" rn="TEMPONBOARD_SENSOR" >
    <api>TemperatureOnBoard-sensor</api>
    <lbl>Type/sensor Category/Temperature on Board sensor</lbl>
    <rr>false</rr>
</m2m:ae>
'

curl -X POST -H "X-M2M-Origin: admin:admin" -H "Content-Type: application/xml;ty=3" http://127.0.0.1:8080/~/in-cse/in-name/TEMPONBOARD_SENSOR -d '
<m2m:cnt xmlns:m2m="http://www.onem2m.org/xml/protocols" rn="DESCRIPTOR">
</m2m:cnt>
'

curl -X POST -H "X-M2M-Origin: admin:admin" -H "Content-Type: application/xml;ty=4" http://127.0.0.1:8080/~/in-cse/in-name/TEMPONBOARD_SENSOR/DESCRIPTOR -d '
<m2m:cin xmlns:m2m="http://www.onem2m.org/xml/protocols">
    <cnf>application/xml</cnf>
    <con>
        &lt;obj&gt;
            &lt;str name=&quot;type&quot; val=&quot;TempOnBoard_Sensor&quot;/&gt;
            &lt;str name=&quot;location&quot; val=&quot;Home&quot;/&gt;
            &lt;str name=&quot;appId&quot; val=&quot;TEMPONBOARD_SENSOR&quot;/&gt;
            &lt;op name=&quot;getValueFirst&quot; href=&quot;/in-cse/in-name/TEMPONBOARD_SENSOR/DATA/la&quot;
            in=&quot;obix:Nil&quot; out=&quot;obix:Nil&quot; is=&quot;retrieve&quot;/&gt;
        &lt;/obj&gt;
    </con>
</m2m:cin>
'

curl -X POST -H "X-M2M-Origin: admin:admin" -H "Content-Type: application/xml;ty=3" http://127.0.0.1:8080/~/in-cse/in-name/TEMPONBOARD_SENSOR -d '
<m2m:cnt xmlns:m2m="http://www.onem2m.org/xml/protocols" rn="DATA">
</m2m:cnt>
'

#   =======================================
#Battery Sensor

curl -X POST -H "X-M2M-Origin: admin:admin" -H "Content-Type: application/xml;ty=2" http://127.0.0.1:8080/~/in-cse -d '
<m2m:ae xmlns:m2m="http://www.onem2m.org/xml/protocols" rn="BATTERY_SENSOR" >
    <api>Battery-sensor</api>
    <lbl>Type/sensor Category/Battery sensor</lbl>
    <rr>false</rr>
</m2m:ae>
'

curl -X POST -H "X-M2M-Origin: admin:admin" -H "Content-Type: application/xml;ty=3" http://127.0.0.1:8080/~/in-cse/in-name/BATTERY_SENSOR -d '
<m2m:cnt xmlns:m2m="http://www.onem2m.org/xml/protocols" rn="DESCRIPTOR">
</m2m:cnt>
'

curl -X POST -H "X-M2M-Origin: admin:admin" -H "Content-Type: application/xml;ty=4" http://127.0.0.1:8080/~/in-cse/in-name/BATTERY_SENSOR/DESCRIPTOR -d '
<m2m:cin xmlns:m2m="http://www.onem2m.org/xml/protocols">
    <cnf>application/xml</cnf>
    <con>
        &lt;obj&gt;
            &lt;str name=&quot;type&quot; val=&quot;Battery_Sensor&quot;/&gt;
            &lt;str name=&quot;location&quot; val=&quot;Home&quot;/&gt;
            &lt;str name=&quot;appId&quot; val=&quot;BATTERY_SENSOR&quot;/&gt;
            &lt;op name=&quot;getValueFirst&quot; href=&quot;/in-cse/in-name/BATTERY_SENSOR/DATA/la&quot;
            in=&quot;obix:Nil&quot; out=&quot;obix:Nil&quot; is=&quot;retrieve&quot;/&gt;
        &lt;/obj&gt;
    </con>
</m2m:cin>
'

curl -X POST -H "X-M2M-Origin: admin:admin" -H "Content-Type: application/xml;ty=3" http://127.0.0.1:8080/~/in-cse/in-name/BATTERY_SENSOR -d '
<m2m:cnt xmlns:m2m="http://www.onem2m.org/xml/protocols" rn="DATA">
</m2m:cnt>
'

#   =======================================
#RE-Mote Board

curl -X POST -H "X-M2M-Origin: admin:admin" -H "Content-Type: application/xml;ty=2" http://127.0.0.1:8080/~/in-cse -d '
<m2m:ae xmlns:m2m="http://www.onem2m.org/xml/protocols" rn="RE-Mote" >
    <api>RE-Mote-zolertia</api>
    <lbl>Type/sensor Category/RE-Mote Zolertia</lbl>
    <rr>false</rr>
</m2m:ae>
'

curl -X POST -H "X-M2M-Origin: admin:admin" -H "Content-Type: application/xml;ty=3" http://127.0.0.1:8080/~/in-cse/in-name/RE-Mote -d '
<m2m:cnt xmlns:m2m="http://www.onem2m.org/xml/protocols" rn="DESCRIPTOR">
</m2m:cnt>
'

curl -X POST -H "X-M2M-Origin: admin:admin" -H "Content-Type: application/xml;ty=4" http://127.0.0.1:8080/~/in-cse/in-name/RE-Mote/DESCRIPTOR -d '
<m2m:cin xmlns:m2m="http://www.onem2m.org/xml/protocols">
    <cnf>application/xml</cnf>
    <con>
        &lt;obj&gt;
            &lt;str name=&quot;type&quot; val=&quot;RE-Mote&quot; /&gt;
            &lt;str name=&quot;location&quot; val=&quot;IoT workshop&quot; /&gt;
            &lt;str name=&quot;appId&quot; val=&quot;RE-Mote&quot; /&gt;
            &lt;op name=&quot;getValueFirst&quot; href=&quot;/in-cse/in-name/RE-Mote/DATA/la&quot; in=&quot;obix:Nil&quot; out=&quot;obix:Nil&quot; is=&quot;retrieve&quot; /&gt;
            &lt;op name=&quot;search_ip&quot; href=&quot;/in-cse/in-name/RE-Mote?ip=aaaa::1&quot; in=&quot;obix:Nil&quot; out=&quot;obix:Nil&quot; is=&quot;execute&quot; /&gt;
            &lt;op name=&quot;setOnLebRed&quot; href=&quot;/in-cse/in-name/RE-Mote?op=setOnLebRed&quot; in=&quot;obix:Nil&quot; out=&quot;obix:Nil&quot; is=&quot;execute&quot; /&gt;
            &lt;op name=&quot;setOnLebGreen&quot; href=&quot;/in-cse/in-name/RE-Mote?op=setOnLebGreen&quot; in=&quot;obix:Nil&quot; out=&quot;obix:Nil&quot; is=&quot;execute&quot; /&gt;
            &lt;op name=&quot;setOnLebBlue&quot; href=&quot;/in-cse/in-name/RE-Mote?op=setOnLebBlue&quot; in=&quot;obix:Nil&quot; out=&quot;obix:Nil&quot; is=&quot;execute&quot; /&gt;
            &lt;op name=&quot;setOffLebRed&quot; href=&quot;/in-cse/in-name/RE-Mote?op=setOffLebRed&quot; in=&quot;obix:Nil&quot; out=&quot;obix:Nil&quot; is=&quot;execute&quot; /&gt;
            &lt;op name=&quot;setOffLebGreen&quot; href=&quot;/in-cse/in-name/RE-Mote?op=setOffLebGreen&quot; in=&quot;obix:Nil&quot; out=&quot;obix:Nil&quot; is=&quot;execute&quot; /&gt;
            &lt;op name=&quot;setOffLebBlue&quot; href=&quot;/in-cse/in-name/RE-Mote?op=setOffLebBlue&quot; in=&quot;obix:Nil&quot; out=&quot;obix:Nil&quot; is=&quot;execute&quot; /&gt;
            &lt;op name=&quot;TogglePin&quot; href=&quot;/in-cse/in-name/RE-Mote?op=TogglePin&quot; in=&quot;obix:Nil&quot; out=&quot;obix:Nil&quot; is=&quot;execute&quot; /&gt;
        &lt;/obj&gt;
    </con>
</m2m:cin>
'

curl -X POST -H "X-M2M-Origin: admin:admin" -H "Content-Type: application/xml;ty=3" http://127.0.0.1:8080/~/in-cse/in-name/RE-Mote -d '
<m2m:cnt xmlns:m2m="http://www.onem2m.org/xml/protocols" rn="DATA">
</m2m:cnt>
'