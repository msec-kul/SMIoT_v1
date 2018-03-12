# Developing Maintainable Application-Centric IoT Ecosystems

This Android framework is the first implementation of the SMIoT architecture, and is released in the context of the paper 'Developing Maintainable Application-Centric IoT Ecosystems', which is currently under review for submission in the [IEEE ICIOT 2018](http://conferences.computer.org/iciot/2018/) Conference. This implementation mainly focusses on the Virtual IoT Device Layer.

## The Framework
This version of the framework contains support for several device types and technologies.

### Supported Device Types
* Lamp
* Temperature Sensor
* Humidity Sensor
* Pressure Sensor
* Light Sensor
* Presence Sensor

### Supported Technologies
#### Lamps
* [Philips Hue](https://www.philips.be/c-m-li/hue)
* [Osram Lightify](https://www.osram.com/cb/lightify/index.jsp)

#### Sensors
* [VersaSense](https://www.versasense.com/)

## Getting Started
This commit contains an application for monitoring environment parameters (temperature, humidity and pressure) and actuating the lightning in a room. In order to use the application without modifications, at least one lamp technology (Osram or Hue) and one VersaSense sensor kit is required.

To run this application, the IoT device parameters should be supplied in the configuration file [config.json](https://github.com/msec-kul/SMIoT_v1/blob/master/app/src/main/assets/config.json).

### Philips Hue
#### Gateway
* \<ip_address>: this is the ip address of the Hue gateway on the local network.
* \<token>: this is the authentication token of the hue gateway that can be acquired by following [this](https://www.developers.meethue.com/documentation/getting-started) procedure.

#### Lamp
* \<unique_id>: this is the MAC address of the lamp.

### Osram Lightify
#### Gateway
* \<username>: the email address used to register the Osram Lightify account.
* \<password>: the password used to register the Osram Lightify account.
* \<gateway_id>: this is the id of the gateway. This can be found on the back of the device (always starts with *OSR*).

#### Lamp
* \<unique_id>: this is the name of the lamp (customizable).

### VersaSense
#### Gateway
* \<gateway_address>: this is the ip address of the VersaSense gateway on the local network.

#### Sensors
* \<unique_id>: this is the MAC address of the node, followed by the identifier of the peripheral, separated by '/'.
example:
```
xx-xx-xx-xx-xx-xx-xx-xx/yyyyyyyyy
```
