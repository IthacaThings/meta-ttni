# TTN changes to config

# Some of the ntp scripts rely on perl, but we don't use them so we'll remove the dependency.
RDEPENDS_${PN}-utils_remove = "perl"
