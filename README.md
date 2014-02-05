CatroidDrone
============

Provides the ARDrone functions to the Catroid/Pocket Code project.

## Build APK

1. Download and extract the actual Android [SDK](http://developer.android.com/sdk/index.html?utm_source=weibolife) and [NDK](http://developer.android.com/tools/sdk/ndk/index.html)
2. Set the following environment variables according to your setup: ``ANDROID_SDK_PATH``, ``ANDROID_NDK_PATH`` and ``ARDRONE_LIB_PATH=<repository dir>/ARDroneLib``

  On Ubuntu add the Path Variables to the ``/etc/bash.bashrc`` 
  
  Add the following lines:
  
  ``export ANDROID_NDK_PATH=/home/gerald/dev_ardrone/android-ndk-r9c``
  
  ``export NDK_PATH=$ANDROID_NDK_PATH``
  
  ``export ANDROID_SDK_PATH=/home/gerald/dev_ardrone/android-sdk-linux``
  
  To configure your ``$PATH`` also add this lines to your ``/etc/bash.bashrc``
  
  ``export PATH=$PATH:$NDK_PATH``
  
  ``export PATH=$PATH:$ANDROID_SDK_PATH/tools``
  
  ``export PATH=$PATH:$ANDROID_SDK_PATH/platform-tools``
  
3. Open a terminal and change to the ``<repository dir>/CatroidDrone/CatroidDrone`` directory.
4. Plug in your device and run ``./build.sh release``
5. Software Versions used: Ubuntu Release 12.04 (precise) 64-bit, Android NDK r9c, OpenJDK 64-Bit 1.7.0_51

##Development
1. Follow the [Build APK Guide](https://github.com/wagnergerald/CatroidDrone/blob/master/README.md#build-apk)
2. Download Eclipse and install the ADT plugin
3. Import the project from ``<repository dir>/CatroidDrone/CatroidDrone``
