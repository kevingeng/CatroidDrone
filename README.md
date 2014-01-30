CatroidDrone
============

Provides the ARDrone functions to the Catroid/Pocket Code project.

## Build APK

1. Download and extract the actual Android [SDK](http://developer.android.com/sdk/index.html?utm_source=weibolife) and [NDK](http://developer.android.com/tools/sdk/ndk/index.html)
2. Open the file ``<your path>/CatroidDrone/Examples/Android/trunk/FreeFlight2``
3. Set the ``ANDROID_SDK_PATH``, ``#ANDROID_NDK_PATH`` and ``ARDRONE_LIB_PATH=`` according to your folder structure
4. Change to the ``<your path>/CatroidDrone/Examples/Android/trunk/FreeFlight2/`` directory
5. Run ``./build.sh release`` 
6. Install the app with ``adb install -r bin/FreeFlight-release.apk``

##Development
1. Follow the [Build APK Guide](https://github.com/wagnergerald/CatroidDrone/blob/master/README.md#build-apk)
2. 
