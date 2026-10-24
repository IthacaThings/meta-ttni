# configure only compiles in the zoneinfo path if the build host has
# /usr/share/zoneinfo; set it explicitly.
EXTRA_OECONF += "--with-tzdir=/usr/share/zoneinfo"
