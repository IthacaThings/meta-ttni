SUMMARY = "Preserve config over a flash upgrade"
DESCRIPTION = "Preserve config over a flash upgrade"
SECTION = "base"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
PR = "r1"

INHIBIT_DEFAULT_DEPS = "1"

SRC_URI = "file://preserve_config \
	   file://restore_config \
"

DEPENDS_append = " update-rc.d-native"

do_install () {
	install -d ${D}/${sysconfdir}/init.d

	install -m 0755    ${WORKDIR}/preserve_config	${D}${sysconfdir}/init.d
	install -m 0755    ${WORKDIR}/restore_config	${D}${sysconfdir}/init.d

	update-rc.d -r ${D} preserve_config start 39 0 6 .
	update-rc.d -r ${D} restore_config start 32 S .
}

FILES_${PN} += "${sysconfdir}/init.d/preserve_config ${sysconfdir}/init.d/restore_config"
