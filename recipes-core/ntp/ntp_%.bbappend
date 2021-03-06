# TTN changes to config

# Some of the ntp scripts rely on perl, but we don't use them and don
RDEPENDS_${PN}-utils_remove = "perl"
