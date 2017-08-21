#!/bin/sh

#   =======================================
#Temperature Sensor
for i in $(seq 1 2)
do
  curl -X POST -H "X-M2M-Origin: admin:admin" -H "Content-Type: application/xml;ty=4" http://127.0.0.1:8080/~/in-cse/in-name/TEMPERATURE_SENSOR/DATA -d '<m2m:cin xmlns:m2m="http://www.onem2m.org/xml/protocols">
      <cnf>message</cnf>
      <con>
        &lt;obj&gt;
          &lt;str name=&quot;appId&quot; val=&quot;TEMPERATURE_SENSOR&quot;/&gt;
          &lt;str name=&quot;category&quot; val=&quot;temperature &quot;/&gt;
          &lt;int name=&quot;data&quot; val=&quot;'`shuf -i20-45 -n1`'&quot;/&gt;
          &lt;int name=&quot;unit&quot; val=&quot;celsius&quot;/&gt;
        &lt;/obj&gt;
      </con>
  </m2m:cin>'
done

#   =======================================
#Humidity Sensor
for i in $(seq 1 2)
do
  curl -X POST -H "X-M2M-Origin: admin:admin" -H "Content-Type: application/xml;ty=4" http://127.0.0.1:8080/~/in-cse/in-name/HUMIDITY_SENSOR/DATA -d '<m2m:cin xmlns:m2m="http://www.onem2m.org/xml/protocols">
      <cnf>message</cnf>
      <con>
        &lt;obj&gt;
          &lt;str name=&quot;appId&quot; val=&quot;HUMIDITY_SENSOR&quot;/&gt;
          &lt;str name=&quot;category&quot; val=&quot;humidity &quot;/&gt;
          &lt;int name=&quot;data&quot; val=&quot;'`shuf -i10-95 -n1`'&quot;/&gt;
          &lt;int name=&quot;unit&quot; val=&quot;%&quot;/&gt;
        &lt;/obj&gt;
      </con>
  </m2m:cin>'
done
#   =======================================
#Light Sensor
for i in $(seq 1 2)
do
  curl -X POST -H "X-M2M-Origin: admin:admin" -H "Content-Type: application/xml;ty=4" http://127.0.0.1:8080/~/in-cse/in-name/LIGHT_SENSOR/DATA -d '<m2m:cin xmlns:m2m="http://www.onem2m.org/xml/protocols">
      <cnf>message</cnf>
      <con>
        &lt;obj&gt;
          &lt;str name=&quot;appId&quot; val=&quot;LIGHT_SENSOR&quot;/&gt;
          &lt;str name=&quot;category&quot; val=&quot;light &quot;/&gt;
          &lt;int name=&quot;data&quot; val=&quot;'`shuf -i1-1000 -n1`'&quot;/&gt;
          &lt;int name=&quot;unit&quot; val=&quot;%&quot;/&gt;
        &lt;/obj&gt;
      </con>
  </m2m:cin>'
done
#   =======================================
#Temperature on Board Sensor
for i in $(seq 1 2)
do
  curl -X POST -H "X-M2M-Origin: admin:admin" -H "Content-Type: application/xml;ty=4" http://127.0.0.1:8080/~/in-cse/in-name/TEMPONBOARD_SENSOR/DATA -d '<m2m:cin xmlns:m2m="http://www.onem2m.org/xml/protocols">
      <cnf>message</cnf>
      <con>
        &lt;obj&gt;
          &lt;str name=&quot;appId&quot; val=&quot;TEMPONBOARD_SENSOR&quot;/&gt;
          &lt;str name=&quot;category&quot; val=&quot;temperature on board &quot;/&gt;
          &lt;int name=&quot;data&quot; val=&quot;'`shuf -i10-45 -n1`'&quot;/&gt;
          &lt;int name=&quot;unit&quot; val=&quot;%&quot;/&gt;
        &lt;/obj&gt;
      </con>
  </m2m:cin>'
done
#   =======================================
#Battery Sensor
for i in $(seq 1 2)
do
  curl -X POST -H "X-M2M-Origin: admin:admin" -H "Content-Type: application/xml;ty=4" http://127.0.0.1:8080/~/in-cse/in-name/BATTERY_SENSOR/DATA -d '<m2m:cin xmlns:m2m="http://www.onem2m.org/xml/protocols">
      <cnf>message</cnf>
      <con>
        &lt;obj&gt;
          &lt;str name=&quot;appId&quot; val=&quot;BATTERY_SENSOR&quot;/&gt;
          &lt;str name=&quot;category&quot; val=&quot;battery &quot;/&gt;
          &lt;int name=&quot;data&quot; val=&quot;'`shuf -i1-5 -n1`'&quot;/&gt;
          &lt;int name=&quot;unit&quot; val=&quot;volt&quot;/&gt;
        &lt;/obj&gt;
      </con>
  </m2m:cin>'
done
#   =======================================
#RE-Mote Board
for i in $(seq 1 2)
do
  curl -X POST -H "X-M2M-Origin: admin:admin" -H "Content-Type: application/xml;ty=4" http://127.0.0.1:8080/~/in-cse/in-name/RE-Mote/DATA -d '<m2m:cin xmlns:m2m="http://www.onem2m.org/xml/protocols">
      <cnf>message</cnf>
      <con>
        &lt;obj&gt;
            &lt;int name=&quot;adc1&quot; val=&quot;'`shuf -i100-152 -n1`'&quot; /&gt;
            &lt;int name=&quot;adc2&quot; val=&quot;'`shuf -i1-15 -n1`'&quot; /&gt;
            &lt;int name=&quot;adc3&quot; val=&quot;'`shuf -i1000-1500 -n1`'&quot; /&gt;
            &lt;int name=&quot;battery&quot; val=&quot;'`shuf -i3000-3300 -n1`'&quot; /&gt;
            &lt;int name=&quot;temperature&quot; val=&quot;'`shuf -i33000-33300 -n1`'&quot; /&gt;
            &lt;int name=&quot;sensor_temperature&quot; val=&quot;'`shuf -i1000-99000 -n1`'&quot; /&gt;
            &lt;int name=&quot;sensor_humidity&quot; val=&quot;'`shuf -i40-100 -n1`'&quot; /&gt;
        &lt;/obj&gt;
      </con>
  </m2m:cin>'
done