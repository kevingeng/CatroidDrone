CatroidDrone
============

Provides the ARDrone functions to the Catroid/Pocket Code project.

## Build APK

1. Download and extract the actual Android [SDK](http://developer.android.com/sdk/index.html?utm_source=weibolife) and [NDK](http://developer.android.com/tools/sdk/ndk/index.html)
2. Open the file ``<your path>/CatroidDrone/Examples/Android/trunk/FreeFlight2``
3. Set the following environment variables according to your setup: ``ANDROID_SDK_PATH``, ``ANDROID_NDK_PATH`` and ``ARDRONE_LIB_PATH=<repository dir>/ARDroneLib``
4. Change to the ``<repository dir>/CatroidDrone/CatroidDrone`` directory
5. Plug in your device and run ``./build.sh release`` (make sure ``ANDROID_SDK_PATH/platfrom-tools`` is added to your ``PATH`` variable

##Development
1. Follow the [Build APK Guide](https://github.com/wagnergerald/CatroidDrone/blob/master/README.md#build-apk)
2. Download Eclipse and install the ADT plugin
3. Import the project from ``<repository dir>/CatroidDrone/CatroidDrone``
