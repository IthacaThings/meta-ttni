# Remove distrusted CA roots from certificate bundles that recipes ship
# outside of ca-certificates.
#
# DISTRUSTED_CA_PEM: globs under ${D} of PEM bundles, or of wheels
# containing them, to strip after do_install.  Every file must exist and
# contain at least one of the roots, so the build fails if an upgraded
# recipe moves or drops a bundle.

# SHA-256 fingerprints of the roots distrusted after the October 2026
# eMudhra compromise (CVE-2026-111943): Comodo AAA Services root and
# emSign ECC Root CA - G3
DISTRUSTED_CA_SHA256 ?= "\
    d7a7a0fb5d7e2731d771e9484ebcdef71d5f0c3e0a2948782bc83ee0ea699ef4 \
    86a1ecba089c4a8d3bbe2734c612ba341d813e043cf9e8a862cd5c57a36bbe6b \
"
DISTRUSTED_CA_PEM ?= ""

def distrust_ca_pem(text, fingerprints):
    """Return text without distrusted certificates and their comments,
    and the number removed"""
    import base64, hashlib
    out, pending, cert, removed = [], [], None, 0
    for line in text.splitlines(True):
        if cert is not None:
            cert.append(line)
            if line.startswith("-----END CERTIFICATE-----"):
                body = "".join(l.strip() for l in cert[1:-1])
                der = base64.b64decode(body)
                if hashlib.sha256(der).hexdigest() in fingerprints:
                    removed += 1
                else:
                    out += pending + cert
                pending, cert = [], None
        elif line.startswith("-----BEGIN CERTIFICATE-----"):
            cert = [line]
        elif line.startswith("#"):
            pending.append(line)
        else:
            out += pending + [line]
            pending = []
    return "".join(out + pending), removed

def distrust_ca_wheel(path, fingerprints):
    """Strip every *.pem in a wheel and update its RECORD"""
    import base64, hashlib, zipfile
    with zipfile.ZipFile(path) as z:
        entries = [(i, z.read(i.filename)) for i in z.infolist()]
    removed, changed = 0, {}
    for info, data in entries:
        if info.filename.endswith(".pem"):
            text, n = distrust_ca_pem(data.decode("utf-8"), fingerprints)
            if n:
                removed += n
                changed[info.filename] = text.encode("utf-8")
    if not changed:
        return 0
    with zipfile.ZipFile(path, "w", zipfile.ZIP_DEFLATED) as z:
        for info, data in entries:
            data = changed.get(info.filename, data)
            if info.filename.endswith(".dist-info/RECORD"):
                lines = []
                for row in data.decode("utf-8").splitlines():
                    name = row.split(",")[0]
                    if name in changed:
                        digest = base64.urlsafe_b64encode(
                            hashlib.sha256(changed[name]).digest())
                        row = "%s,sha256=%s,%d" % (
                            name, digest.decode().rstrip("="), len(changed[name]))
                    lines.append(row)
                data = ("\n".join(lines) + "\n").encode("utf-8")
            z.writestr(info, data)
    return removed

python distrust_ca_install() {
    import glob
    fingerprints = set(d.getVar("DISTRUSTED_CA_SHA256").split())
    dest = d.getVar("D")
    for pattern in (d.getVar("DISTRUSTED_CA_PEM") or "").split():
        paths = glob.glob(dest + pattern)
        if not paths:
            bb.fatal("distrust-ca: no files match %s" % pattern)
        for path in paths:
            if path.endswith(".whl"):
                removed = distrust_ca_wheel(path, fingerprints)
            else:
                with open(path) as f:
                    text, removed = distrust_ca_pem(f.read(), fingerprints)
                if removed:
                    with open(path, "w") as f:
                        f.write(text)
            if not removed:
                bb.fatal("distrust-ca: no distrusted roots in %s" % path)
            bb.note("distrust-ca: removed %d from %s" % (removed, path))
}
do_install[postfuncs] += "distrust_ca_install"
do_install[vardeps] += "DISTRUSTED_CA_SHA256 DISTRUSTED_CA_PEM"
