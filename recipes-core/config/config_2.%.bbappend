# TTN changes to config

# Provide our version of network/interfaces that is configured for
# DHCP on the wired interface

SRC_URI += "file://network \
	   "

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
