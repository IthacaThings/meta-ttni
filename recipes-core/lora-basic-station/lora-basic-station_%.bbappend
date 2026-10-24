# station.conf differs per machine, so packages must not be shared
# between machines of the same architecture.
PACKAGE_ARCH = "${MACHINE_ARCH}"

# Must sort above the shared arm926ejste 2.0.5-8-r3.0 that gateways
# have installed (and r3.1 on some MTCAPs), or opkg keeps that build
PR = "r4"
