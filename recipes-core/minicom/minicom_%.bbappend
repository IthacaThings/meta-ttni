# configure picks the default port from devices on the build host; pin
# the value earlier builds have used.
EXTRA_OECONF += "--enable-dfl-port=/dev/modem"
