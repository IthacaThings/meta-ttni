# TTN changes to config

# Openssl ships with c_rehash which depends on perl, but it's big and we don't want it.
RDEPENDS_${PN}-bin_remove = "perl"
