BK OM2M 
==========

Tham khảo [eclipse.org/om2m](http://www.eclipse.org/om2m/)

## Yêu cầu
* JDK >= 1.7.
* Apache Maven 3.
* Eclipse (coding, recommend version `Luna Service Release 2 (4.4.2) for Eclipse Java EE IDE for Web Developers.`)

## Buiding project
* Nhảy đến thư mục gốc của project "org.eclipse.om2m.hust" và gõ lệnh sau:
	```
	$ mvn clean install
	```
* Có thể build từng module bằng lệnh
	```
	$ mvn clean install -rf :<module>
	```
* Sau khi build xong thì IN-CSE có trong thư mục sau: `org.eclipse.om2m.hust/org.eclipse.om2m.site.in-cse/target/products/in-cse/*os*/*ws*/*arch*`
* Sau khi build xong thì MN-CSE có trong thư mục sau: `org.eclipse.om2m.hust/org.eclipse.om2m.site.mn-cse/target/products/mn-cse/*os*/*ws*/*arch*`

## Cấu hình và start IN-CSE (MN-CSE)
* Nhảy đến thưc mục IN-CSE (MN-CSE)
* Có thể cấu hình lại file config "configuration/config.ini" của IN-CSE (MN-CSE)
* Mở trình duyệt và truy cập địa chỉ của 

  > IN-CSE `http://127.0.0.1:8080/webpage`
  
  > MN-CSE `http://127.0.0.1:8282/webpage`

## Chuyển đổi database `org.eclipse.om2m.persistence.service`
* Sửa lại jdbc trong class `org.eclipse.om2m.persistence.service.DBConstants`
* Inject driver tưởng ứng trong folder libs và config `buildpath` cho nó.
* Sau khi build thì sửa lại config trong in-cse và mn-cse


## Giao diện Admin chung
Tham khảo file README.md trong thư mục [org.eclipse.om2m.webapp.resourcesbrowser.ui](./org.eclipse.om2m.webapp.resourcesbrowser.ui)

`localhost:8080/iotworkshop`
