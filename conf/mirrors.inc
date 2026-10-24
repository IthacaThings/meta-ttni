# Source mirrors for building against a local copy of MultiTech's
# sources (see mlinux-be's bitbake_ml MLINUX_MIRROR, mounted at /mirror).
#
# MultiTech git repos are redirected to /mirror/git by git itself
# (/mirror/gitconfig, included by the build image's /etc/gitconfig),
# which also covers ls-remote for tag and branch SRCREVs.  git:// is
# rewritten to https:// for all other hosts the same way.

PREMIRRORS_prepend = "\
    https?://.*multitech\.net/.* file:///mirror/downloads/ \n \
"

# Turn every successful git fetch into a tarball that can seed the mirror
BB_GENERATE_MIRROR_TARBALLS = "1"
