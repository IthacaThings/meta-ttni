# mLinux image for TTN
# Removes, Java, Ruby, Perl and some other code
# Adds python dependencies for Ansible and sudo

require recipes-core/images/mlinux-base-image.bb

DESCRIPTION = "mLinux image for TTN"

# Lighttpd web server
LIGHTTPD = "lighttpd \
            lighttpd-module-cgi lighttpd-module-indexfile \
            lighttpd-module-redirect lighttpd-module-auth  \
            lighttpd-module-access lighttpd-module-accesslog \
            lighttpd-module-rewrite lighttpd-module-proxy lighttpd-module-fastcgi \
            lighttpd-module-scgi lighttpd-module-alias \
            lighttpd-module-dirlisting  lighttpd-module-staticfile \
            "
#IMAGE_INSTALL_append += "${LIGHTTPD}"

SQLITE3 = "sqlite3"
#IMAGE_INSTALL_append += "${SQLITE3}"

# Monit system/process monitor
MONIT = "monit"
IMAGE_INSTALL_append += "${MONIT}"

# UFW firewall control
UFW = "ufw"
IMAGE_INSTALL_append += "${UFW}"

# LoRa support (MTAC-LORA accessory card)
LORA = "lora-gateway-utils \
      	lora-network-server \
	lora-query \
	lora-packet-forwarder-usb \
	"
#IMAGE_INSTALL_append = "${LORA}"

# LoRa Basics Station
LORA_BASICS = "lora-basic-station"
IMAGE_INSTALL_append += "${LORA_BASICS}"

# MQTT server
MOSQUITTO = "mosquitto \
	     mosquitto-clients \
	     "
#IMAGE_INSTALL_append += "${MOSQUITTO}"

# Perl support
PERL = "perl \
        perl-module-io perl-module-fcntl \
	"
#IMAGE_INSTALL_append += "${PERL}"

# Python support
PYTHON = "python \
	  python-argparse \
	  python-compression \
	  python-dateutil \
          python-email \
	  python-html \
	  python-psutil \
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
IMAGE_INSTALL_append += "${PYTHON}"

# Ruby support
RUBY = "ruby \
        ${@bb.utils.contains('IMAGE_INSTALL', '${SQLITE3}', 'ruby-sqlite3', '', d)} \
	ruby-serialport \
	"
#IMAGE_INSTALL_append += "${RUBY}"

# OpenJDK Java runtime
OPENJDK = "openjdk-7-jre \
	   openjdk-7-vm-jamvm \
	   openjdk-7-vm-cacao \ 
	   openjdk-7-vm-zero \
	   "
#IMAGE_INSTALL_append += "${OPENJDK}"

# PHP support
PHP = "php \
       php-cli \
       php-cgi \
       "
#IMAGE_INSTALL_append += "${PHP}"

# NTP support
NTP = "ntp \
       ntp-utils \
       "
IMAGE_INSTALL_append += "${NTP}"

# GPS support (conflicts with Kersing packet forwarder)
GPS = "venus-gps \
       pps-tools \
       gpsd \
       gps-utils \
       gps-udev \
       "
#IMAGE_INSTALL_append += "${GPS}"

# Required to run Ansible
ANSIBLE = "python-distutils \
	   python-json \
	   python-multiprocessing \
	   python-pkgutil \
	   python-shell \
	   python-terminal \
	   sudo \
	   "
IMAGE_INSTALL_append += "${ANSIBLE}"

# To preserve configuration through a firmware update
IMAGE_INSTALL_append += "preserve"

# Required for ssh tunnels
IMAGE_INSTALL_append += "autossh"

# Required for mp_packet_forwarder
IMAGE_INSTALL_append += "libmpsse"

# Useful utilities
IMAGE_INSTALL_append += "htop logrotate"

# Remove these to make the image smaller
REMOVES=""

#  FILESYSTEM_FEATURES
REMOVES += "dosfstools \
       	    cifs-utils"
#  NETWORKING_FEATURES
REMOVES += "bridge-utils \
            inetutils-ftp"
#  WIFI_FEATURES
REMOVES += "hostapd \
            hostapd-cfg"
#  BLUETOOTH_FEATURES
REMOVES += "bluez5 python-pybluez"
#  MISC_FEATURES
REMOVES += "lrzsz"

IMAGE_INSTALL_remove += "${REMOVES}"

# Remove these kernel modules we don't plan to use
IMAGE_INSTALL_remove += "kernel-module-mtac-eth \
                          kernel-module-mtac-gpiob \
                          kernel-module-mtac-mfser \
                          kernel-module-mtac-pulse \
                          kernel-module-mtac-xdot"
