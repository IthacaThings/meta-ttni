FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI_append = "\
	       file://0002-remove-dst-x3.patch;sha256sum=c9fa40ba8e1853990bb4596076fdad33fd1309e09b00a1baddbadfe17071b57a \
	       file://0003-remove-emudhra-compromise.patch;sha256sum=dff2a5ef80fa7c624d1aaedd13831e5aae4297edd882c87e0e726180e96871c6 \
	       "

# Must sort above 20210119-r1.1 on ttni.tech so gateways upgrade
PR = "r2"
