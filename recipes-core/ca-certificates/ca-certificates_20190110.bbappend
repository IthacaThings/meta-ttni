
FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI_append = "\
	       file://0001-remove-dst-x3.patch;sha256sum=a3231ac94013b1bc6b1ceaa0d3d4bf857013d92a7a80710af15f81a1b8cbe3a6"

