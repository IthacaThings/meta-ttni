
FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI_append = "\
	       file://0002-remove-dst-x3.patch;sha256sum=c9fa40ba8e1853990bb4596076fdad33fd1309e09b00a1baddbadfe17071b57a"

