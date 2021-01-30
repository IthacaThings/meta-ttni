DESCRIPTION = "Monit"
HOMEPAGE = "http://mmonit.com/"
LICENSE = "AGPL-3.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=ea116a7defaf0e93b3bb73b2a34a3f51"
DEPENDS = "libpam zlib openssl"

PR = "r0"

SRC_URI = "http://mmonit.com/monit/dist/monit-5.27.2.tar.gz \
           file://monitrc \
           file://monit.init \
           file://monit.default \
           "

SRC_URI[md5sum] = "13a0804dae18ef08198c3ace72f9dd60"
SRC_URI[sha256sum] = "d8809c78d5dc1ed7a7ba32a5a55c5114855132cc4da4805f8d3aaf8cf46eaa4c"

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

EXTRA_OECONF_append_arm = " libmonit_cv_setjmp_available=yes libmonit_cv_vsnprintf_c99_conformant=yes "

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
