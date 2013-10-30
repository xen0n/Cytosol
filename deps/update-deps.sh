#!/bin/sh

# Commands
GIT_CMD="${GIT_CMD:-git}"
MVN_CMD="${MVN_CMD:-mvn}"


# Semi-hardcoded dependencies
use_custom_abs=false
abs_name=ActionBarSherlock
abs_repo_author=xen0n
abs_ver=4.4.0-xen0n
abs_pull=false

use_custom_vpi=false
vpi_name=Android-ViewPagerIndicator
vpi_repo_author=xen0n
vpi_ver=2.4.1-xen0n
vpi_pull=false

use_custom_smenu=true
smenu_name=SlidingMenu
smenu_repo_author=xen0n
smenu_ver=master
smenu_pull=true

# Helpers
echoinfo () {
    echo -e "\033[1;32m * \033[m$@" ;
}

# stderr Helper
# http://stackoverflow.com/questions/2990414/echo-that-outputs-to-stderr
echoerr () {
    echo -e "$@" >&2;
}


errexit () {
    exitcode=$1
    reason=$2

    echoerr ""
    echoerr "\033[1;31m * \033[mBailing out: ${reason}"
    echoerr ""

    exit "${exitcode}"
}


# Maven version
mvn_version () {
    "${MVN_CMD}" --version | head -1
}


mvn_version_fail () {
    mvnver=$1

    echoerr "\033[1;31m !!!\033[m Incompatible Maven version detected."
    echoerr ""
    echoerr "Building dependencies currently requires Maven 3.0.x, but your"
    echoerr "Maven advertised this instead:"
    echoerr ""
    echoerr "${mvnver}"
    echoerr ""
    echoerr "Please provide a Maven 3.0.x executable via the MVN_CMD"
    echoerr "environment variable, like this:"
    echoerr ""
    echoerr "    $ MVN_CMD=mvn-3.0 ./update-deps.sh"

    errexit 2 "Maven version check failed"
}


# Clone or update dependency GitHub repos
gh_repo_sync () {
    repoauthor=$1
    repo=$2

    if [ -d "$repo" ]; then
        # directory exists
        echoinfo "Updating ${repoauthor}/${repo}" ;

        ( cd "$repo" && "${GIT_CMD}" fetch origin ) || errexit 3 "git pull failed" ;
    else
        # directory doesn't exist
        # don't process the rare circumstance where $repo exists but IS A FILE
        # that's bound to fail so don't even attempt to rescue
        echoinfo "Cloning ${repoauthor}/${repo}" ;

        "${GIT_CMD}" clone "https://github.com/${repoauthor}/${repo}.git" || errexit 4 "git clone failed" ;
    fi
}


# Build helpers
git_co () {
    ver=$1
    do_pull=$2

    "${GIT_CMD}" checkout "${ver}" || errexit 5 "git checkout failed"

    if ${do_pull}; then
        "${GIT_CMD}" pull || errexit 6 "git pull failed"
    fi
}


domvn () {
    "${MVN_CMD}" $@ || errexit 7 "domvn failed"
}


# custom ABS
build_abs () {
    if ${use_custom_abs}; then
        echoinfo "Building and installing ActionBarSherlock library"

        gh_repo_sync "${abs_repo_author}" "${abs_name}"

        cd "${abs_name}"
        git_co "${abs_ver}" "${abs_pull}"

        cd actionbarsherlock || errexit 102 "directory layout unrecognized"
        domvn clean install

        cd ..
        echoinfo "Installing ActionBarSherlock parent POM"
        domvn install -N -q

        cd ..
    else
        echoinfo "Using ActionBarSherlock from Maven Central"
    fi
}


# custom VPI
build_vpi () {
    if ${use_custom_vpi}; then
        echoinfo "Building and installing ViewPagerIndicator"

        gh_repo_sync "${vpi_repo_author}" "${vpi_name}"

        cd "${vpi_name}"
        git_co "${vpi_ver}" "${vpi_pull}"

        domvn clean install -q

        cd ..
    else
        echoinfo "Using ViewPagerIndicator from Maven Central"
    fi
}


# custom SlidingMenu
build_smenu () {
    if ${use_custom_smenu}; then
        echoinfo "Building and installing SlidingMenu library"

        gh_repo_sync "${smenu_repo_author}" "${smenu_name}"

        cd "${smenu_name}"
        git_co "${smenu_ver}" "${smenu_pull}"

        cd library || errexit 102 "directory layout unrecognized"
        domvn clean install -q

        cd ..
        echoinfo "Installing SlidingMenu parent POM"
        domvn install -N -q

        cd ..
    else
        echoinfo "Using SlidingMenu from Maven Central"
    fi
}


# Main part
echoinfo "Using git: ${GIT_CMD}"
echoinfo "Using mvn: ${MVN_CMD}"
echoinfo ""

_mvnver="$( mvn_version )"
echo "${_mvnver}" | grep '^Apache Maven 3.0' > /dev/null 2>&1 || mvn_version_fail "${_mvnver}"
unset _mvnver

build_abs
build_vpi
build_smenu

echoinfo ""
echoinfo "Dependencies successfully set up"
echoinfo ""
exit 0


# vim:ai:et:ts=4:sw=4:sts=4:fenc=utf-8:
