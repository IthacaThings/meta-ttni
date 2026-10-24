# The station binary and station.conf differ per machine, so packages
# must not be shared between machines of the same architecture.
PACKAGE_ARCH = "${MACHINE_ARCH}"
