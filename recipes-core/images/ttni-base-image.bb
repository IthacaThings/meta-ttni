# mLinux image for TTN
# Removes, Java, Ruby, Perl and some other code
# Adds python dependencies for Ansible and sudo

require recipes-core/images/mlinux-base-image.bb

DESCRIPTION = "mLinux image for TTN"

LIGHTTPD = "lighttpd \
            lighttpd-module-cgi lighttpd-module-indexfile \
            lighttpd-module-redirect lighttpd-module-auth  \
            lighttpd-module-access lighttpd-module-accesslog \
            lighttpd-module-rewrite lighttpd-module-proxy lighttpd-module-fastcgi \
            lighttpd-module-scgi lighttpd-module-alias \
            lighttpd-module-dirlisting  lighttpd-module-staticfile \
            "

# Lighttpd web server
#TTN IMAGE_INSTALL += "${LIGHTTPD}"

#TTN IMAGE_INSTALL += "sqlite3"

IMAGE_INSTALL += "autossh"

# Monit system/process monitor
IMAGE_INSTALL += "monit"

# LoRa support (MTAC-LORA accessory card)
#TTN IMAGE_INSTALL += "lora-gateway-utils lora-network-server lora-query lora-packet-forwarder-usb"

# MQTT server
#TTN IMAGE_INSTALL += "mosquitto mosquitto-clients"

# Perl support
#TTN IMAGE_INSTALL += "perl"
#TTN IMAGE_INSTALL += "perl-module-io perl-module-fcntl"
# not in meta-oe or oe-core...
#IMAGE_INSTALL += "libdevice-serialport-perl"
#IMAGE_INSTALL += "libexpect-perl"

# Python support
IMAGE_INSTALL += "python"
# Python modules
IMAGE_INSTALL += "python-async \
python-argparse \
python-compression \
python-dateutil \
python-html \
python-psutil \
python-pycurl \
python-pyopenssl \
python-pyserial \
python-pyudev \
python-pyusb \
python-simplejson \
python-syslog \
python-textutils \
python-unixadmin \
python-xml \
"

# TTN
IMAGE_INSTALL += "python-distutils python-pkgutil"

# Ruby support
#TTN IMAGE_INSTALL += "ruby"
#TTN IMAGE_INSTALL += "ruby-sqlite3"
#TTN IMAGE_INSTALL += "ruby-serialport"

# OpenJDK Java runtime
#TTN IMAGE_INSTALL += "openjdk-7-jre"
# OpenJDK with JamVM VM (Multi-Tech default)
#TTN IMAGE_INSTALL += "openjdk-7-vm-jamvm"
# OpenJDK with CACAO VM (run with 'java -cacao')
#TTN IMAGE_INSTALL += "openjdk-7-vm-cacao"
# OpenJDK Zero VM (run with 'java -zero')
#TTN IMAGE_INSTALL += "openjdk-7-vm-zero"

# PHP support
#TTN IMAGE_INSTALL += "php php-cli php-cgi"

# Node.js support
IMAGE_INSTALL += "nodejs nodejs-npm"

# Multi-Tech SMS Utility (see http://git.multitech.net)
IMAGE_INSTALL += "sms-utils"
# Multi-Tech GPS Utility
IMAGE_INSTALL += "venus-gps"
IMAGE_INSTALL += "pps-tools"

# When ntp is to use the GPS, gps-utils is required
IMAGE_INSTALL += "gpsd ntp ntp-utils gps-utils gpsd-udev"

IMAGE_INSTALL += "uvccapture"

#TTN
IMAGE_INSTALL += "sudo"
