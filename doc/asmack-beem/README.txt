INTRODUCTION
------------
asmack http://code.google.com/p/asmack/ is a portage of the Smack library for
the Android platform. The project is was maintained by Rene Treffer.

Florian Schmaus maintains a more up to date version of this project. This
version is used by many XMPP clients for Android.

You can find the sources at
  http://github.com/rtreffer/asmack     for Rene version
  http://github.com/Flowdalic/asmack    for Florian version

The asmack project is based on the development version of the Smack library.
This version is constantly moving but we want a fixed version for BEEM.
library.
Currently, we use the changeset 0ffd380698bca3502a6f25c4d755fe79f6977b49 of
the Florian's Smack git repository.

COMPILE
-------

First check out the last version of asmack
> git clone git://github.com/Flowdalic/asmack.git

Add the beem flavour to the patch repository
> rm -rf patch/beem
> cp -R beem_patches patch/beem
>

Edit your local.properties file to contains the path of the android SDK. See
local.properties.example

Build asmack with beem patches
> ./build.bash -c -b 0ffd380698bca3502a6f25c4d755fe79f6977b49
>

The build directory will contains the files :
 * asmack-android-$VERSION-beem.jar for asmack binaries for android $VERSION
 * asmack-android-$VERSION-source-beem.zip for the asmack custom sources for
   android $VERSION.

