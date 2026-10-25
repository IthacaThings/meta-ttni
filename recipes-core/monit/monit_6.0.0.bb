DESCRIPTION = "Monit"
HOMEPAGE = "http://mmonit.com/"
LICENSE = "AGPL-3.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=79ca9f26f1ecb1e377e90383109ede64"
DEPENDS = "libpam zlib openssl"

PR = "r0"

SRC_URI = "http://mmonit.com/monit/dist/monit-6.0.0.tar.gz \
           file://monitrc \
           file://monit.init \
           file://monit.default \
           "

SRC_URI[md5sum] = "5ff8668b131e4b6bbf0f2c9246565f6c"
SRC_URI[sha256sum] = "ddacd2a8120aeb2351e4486ee04a17782b5004aee99f2041d829bc4dcf2a5b3b"

inherit autotools-brokensep

EXTRA_OECONF += "--with-ssl-lib-dir=${STAGING_LIBDIR} \
                --with-ssl-incl-dir=${STAGING_INCDIR} \
                "

# Regenerate autoconfig generated aclocal.m4/m4 contained in the tarball
EXTRA_AUTORECONF += " --force"

do_configure_prepend() {
    rm ${S}/aclocal.m4
    rm -Rf ${S}/m4
}


inherit update-rc.d

INITSCRIPT_NAME = "monit"
INITSCRIPT_PARAMS = "defaults 99 10"

do_install_append() {
    install -d ${D}${sysconfdir}
    install -d ${D}${sysconfdir}/monit.d
    install -m 0600 ${WORKDIR}/monitrc ${D}${sysconfdir}/

    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/monit.init ${D}${sysconfdir}/init.d/monit

    install -d ${D}${sysconfdir}/default
    install -m 0644 ${WORKDIR}/monit.default ${D}${sysconfdir}/default/monit
}

CONFFILES_${PN} += "${sysconfdir}/monitrc ${sysconfdir}/default/monit"
