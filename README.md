# The Things Network Ithaca mLinux layer

This layer depends on: meta-mlinux

# Overview

This layer modifies
[mLinux](http://www.multitech.net/developer/software/mlinux/) for
specific requirements of [The Things
Network](https://console.thethingsnetwork.org/) and maintaining a
large number of Conduits.  It does this by:

## Reducing the size of the Conduit image

Reducing the amount of bandwidth and storage that is needed for an
update.  The following packages are removed. They can be re-installed
via _opkg_ command.

+ mosquitto 
+ PHP
+ gpsd
+ java
+ lighttpd
+ lora packages
+ perl
+ ruby
+ sqlite3

### Adding packages to Conduit AP

The Conduit AP system comes with a minimial configuration that does
not have packages installed needed to run TTN and Ansible.  These are
added by default so they do not need to be installed.

## Adding useful tools

+ Useful utilities (sudo, htop, ntp, ntp-utils, logrotate)
+ python packages required by Ansible (python-distutils, python-pkgutil)
+ Dependencies mp-packet-forwarder (libmpsse)

A future goal is to remove node.js, but this is required to generate
the configuration files for the packet forwarder

## Preserving configuration

The _preserve_ package adds two init scripts:

### preserve_config

This runs at shutdown and if /var/volatile/do_flash_upgrade exists,
makes a list of links into /var/config and stores them as an
executable shell script.

### restore_config

This runs at startup only at the first boot after a firmware upgrade
and runs all scripts in /var/config/restore.d and /var/oem/restore.d
with run-parts.

## DHCP by default

THe default configuration with this image is DHCP on the Ethernet
interface. This configuration is only applied if the file
_/var/config/force_defaults_ exists when a Conduit is booted.

# Usage

+. Configure the MACHINE in conf/local.conf (_mtcdt_ for Conduit,
   _mtcap_ for Conduit AP)
+. Check out this layer and move it in place of *layers/user.layer*.
+. Build the ttni image
```
$ bitbake ttni-base-image
```
+. Deploy the images from _build/tmp/deploy/image/mtcdt_ or
   _build/tmp/deploy/image/mtcap_.
```
$ scp -p build/tmp/deploy/image/mtcdt/ttni-base-image-mtcdt-upgrade-withboot.bin root@GATEWAYIP
$ ssh root@GATEWAYIP /usr/sbin/mlinux-firmware-upgrade ttni-base-image-mtcdt-upgrade-withboot.bin
```

# See Also

+ [Pre-built mLinux images using this layer](https://github.com/IthacaThings/mlinux-images)
+ [Ansible configuration for managing Conduits](https://github.com/IthacaThings/ttn-multitech-cm)
+ [Container environment for building mLinux on newer system](https://hub.docker.com/r/jchonig/mlinux-be/)
