# The Things Network Ithaca mLinux layer

This layer depends on: meta-mlinux

## Overview

This layer modifies
[mLinux](http://www.multitech.net/developer/software/mlinux/) for
specific requirements of [The Things
Network](https://console.thethingsnetwork.org/) and for maintaining a
large number of gateways.  It does this by:

### Reducing the size of the Conduit image

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

#### Adding packages to Conduit AP

The Conduit AP system comes with a minimial configuration that does
not have packages installed needed to run TTN and Ansible.  These are
added by default so they do not need to be manually installed.

### Adding useful tools

+ Useful utilities (sudo, htop, ntp, ntp-utils, logrotate)
+ python packages required by Ansible (python-distutils, python-json,
  python-pkgutil, pythong-shell)
+ Dependencies mp-packet-forwarder (libmpsse)

A future goal is to remove node.js, but this is required to generate
the configuration files for the packet forwarder

### Modifications of existing packages

#### Monit

Replaces mLinux's monit 5.22.0 with 6.0.0.

#### openssl-bin

Remove the dependency on perl.
This breaks the _/usr/bin/c_rehash_ script, which requires perl, but
the _openssl rehash_ command still works.

#### ca-certificates

Removes the DST_CA_Root_X3 certificate.
This certificate expired and broke Let's Encrypt Certificates.

Also removes the Comodo AAA Services and emSign ECC Root CA - G3 roots,
distrusted after the October 2026 eMudhra compromise (see
[ttn-multitech-cm#134](https://github.com/IthacaThings/ttn-multitech-cm/pull/134)).
The package release is bumped so gateways upgrade from the feed.

#### Other copies of the distrusted roots

The `distrust-ca` class strips the same two roots, by SHA-256
fingerprint, from CA bundles that recipes install themselves.  It
handles PEM files and PEM files inside Python wheels, whose `RECORD`
it updates.  The build fails if a listed file is missing or holds
neither root.  It is applied to `python` for the pip wheel that
`python-misc` bundles for `ensurepip`, the only such copy in the image.
`python3-misc`, `python-pip` and `nss` also carry the roots but are
only in the feed and not installed on the gateways.

#### autossh

Passes `--with-ssh` so configure doesn't need ssh on the build host.

#### sudo and minicom

Their configure scripts probe the build host, so the values are pinned:
sudo's time zone directory is `/usr/share/zoneinfo`, and minicom's
default port is `/dev/modem`.

#### lora-basic-station

`station.conf` differs per machine, so the package is built per
machine (`PACKAGE_ARCH = "${MACHINE_ARCH}"`) instead of being shared
by all arm926ejste machines.  The release is bumped to `r4` so opkg
replaces the shared `r3` build gateways already have.

### Preserving configuration

The _preserve_ package adds two init scripts:

#### preserve_config

This runs at shutdown and if /var/volatile/do_flash_upgrade exists,
makes a list of links into /var/config and stores them as an
executable shell script.

#### restore_config

This runs at startup only at the first boot after a firmware upgrade
and runs all scripts in /var/config/restore.d and /var/oem/restore.d
with run-parts.

### DHCP by default

The default configuration with this image is DHCP on the Ethernet
interface. This configuration is only applied if the file
_/var/config/force_defaults_ exists when a Conduit is booted.

## Usage

### Configure

+ Configure the MACHINE in _conf/local.conf_ (_mtcdt_ for Conduit,
  _mtcap_ for Conduit AP)
+ If you are updating a downloaded image, copy _ROOT_PASSWORD_ and
  __ROOT_PASSWORD_HASH_ into _conf/local.conf_
+ Check out this layer and move it in place of _layers/user.layer_.
  Don't use symlinks when using the containerized build environment
  mentioned below.

### Build

Build the ttni image:

```sh
bitbake ttni-base-image
```

Deploy the images from _build/tmp/deploy/image/mtcdt_ or
_build/tmp/deploy/image/mtcap_:

```sh
image=ttni-base-image-mtcdt-upgrade-withboot.bin
scp -p build/tmp/deploy/image/mtcdt/${image} root@GATEWAYIP:
ssh root@GATEWAYIP /usr/sbin/mlinux-firmware-upgrade /home/root/${image}
```

### Source mirror

`git://` no longer works on any host.  The mlinux-be build image
fetches `git://` URLs over `https://`.  MultiTech's repositories can
also be fetched from a local mirror, which keeps builds working if
git.multitech.net goes away:

```sh
layers/meta-ttni/scripts/update-mirror /data/mLinux/mirror
MLINUX_MIRROR=/data/mLinux/mirror ../mlinux-be/bitbake_ml ttni-base-image
```

`scripts/update-mirror` clones (or updates) every public repo on
git.multitech.net and writes a `gitconfig` that redirects MultiTech
URLs to the mirror.  `conf/mirrors.inc` adds a premirror for
multitech.net tarballs.

### Scripted build

_scripts/populate_ builds the _mtcap_ and _mtcdt_ images, then copies
the upgrade images and package feeds to ttni.tech.

By default it publishes to a `-devel` version for testing on a few
gateways; `--production` publishes to the version gateways use:

```sh
# Test build: images/MACHINE/5.3.31-devel and feeds/5.3.31-devel
layers/meta-ttni/scripts/populate 5.3.31
# Release: images/MACHINE/5.3.31 and feeds/5.3.31
layers/meta-ttni/scripts/populate --production 5.3.31
```

Point test gateways at the `-devel` feed and images in their Ansible
configuration.  Both are published from the same build directory, so
package revisions keep increasing and gateways move from `-devel` back
to the production feed without downgrades.

This script assumes that the containerized build environment is
installed as _../mlinux-be/_

It also exports the PR service database (the `.N` suffix on package
revisions) as _prserv.inc_ next to the feed.  If the build directory
is lost, restore it before building so revisions don't restart at
`.0` and fall below what gateways already have:

```sh
bitbake -R conf/primport.conf -R /path/to/prserv.inc -p
```

_conf/prserv-floor.conf_ holds floors taken from the 5.3.31 feed on
ttni.tech, whose PR database was lost; import it the same way.

## See Also

+ [Pre-built mLinux images using this layer](https://github.com/IthacaThings/mlinux-images)
+ [Ansible configuration for managing Conduits](https://github.com/IthacaThings/ttn-multitech-cm)
+ [Container environment for building mLinux on newer system](https://hub.docker.com/r/jchonig/mlinux-be/)
