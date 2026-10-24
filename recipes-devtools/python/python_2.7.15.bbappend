# The pip wheel bundled for ensurepip vendors its own CA bundles
inherit distrust-ca
DISTRUSTED_CA_PEM = "${libdir}/python2.7/ensurepip/_bundled/pip-*.whl"
