CatroidDrone
============

Provides ARDrone functionality to the Catroid/Pocket Code project.

### Build APK

**Software Versions used:** Ubuntu Release **12.04 LTS (precise) 64-bit**, Android NDK **r9c 32bit**, OpenJDK **1.7.0_51 64-Bit**

1. Download and extract the actual Android [SDK](http://developer.android.com/sdk/index.html?utm_source=weibolife) and [NDK](http://developer.android.com/tools/sdk/ndk/index.html)
2. On 64 bit systems install the IA32 libs 
 
   ``sudo apt-get install ia32-libs`` 

    Note: On newer Ubuntu versions the ia32-libs [do not exist anymore.](http://askubuntu.com/questions/107230/what-happened-to-the-ia32-libs-package).
    You have to install the following libs instead:
    
    ``sudo apt-get install lib32z1 lib32ncurses5 lib32bz2-1.0``
    
    ``sudo apt-get install lib32stdc++6``

3. Install the build essentials:

   ``sudo apt-get install build-essential``
   
   ``sudo apt-get install libgtk2.0-dev`` 

   ``sudo apt-get install libsdl1.2-dev`` 
   
   ``sudo apt-get install libiw-dev``
   
4. Set the following environment variables according to your setup: 
 
 ``ANDROID_SDK_PATH`` ``ANDROID_NDK_PATH`` and ``ARDRONE_LIB_PATH=<repository dir>/ARDroneLib``

  On Ubuntu add the path variables to the ``/etc/bash.bashrc`` 
  
  Add the following lines:
  
  ```
  export ANDROID_NDK_PATH=/home/gerald/dev_ardrone/android-ndk-r9c
  export NDK_PATH=$ANDROID_NDK_PATH
  export ANDROID_SDK_PATH=/home/gerald/dev_ardrone/android-sdk-linux
  ```
  To configure your ``$PATH`` also add this lines to your ``/etc/bash.bashrc``
  
   ```
  export PATH=$PATH:$NDK_PATH
  export PATH=$PATH:$ANDROID_SDK_PATH/tools
  export PATH=$PATH:$ANDROID_SDK_PATH/platform-tools
   ```
  
5. Check your java version with the following command.

   ``java -version``
   
   which should be
   
   ```
   java version "1.7.0_51"
   OpenJDK Runtime Environment (IcedTea 2.4.4) (7u51-2.4.4-0ubuntu0.12.04.2)
   OpenJDK 64-Bit Server VM (build 24.45-b08, mixed mode)
   ```
   To install java execute:
   
   ``sudo apt-get install openjdk-7-jdk openjdk-7-source``
   
   To set the default java version run the following command:
   
   ``sudo update-alternatives --config java``
   
6. Open a terminal and change to the ``<repository dir>/CatroidDrone/CatroidDrone`` directory.
7. Plug in your device and run ``./build.sh release``
8. Sometimes the script hangs. In this case run ``./build.sh clean`` and try again.

###Development-IDE Eclipse
1. Follow the [Build APK Guide](https://github.com/wagnergerald/CatroidDrone/#build-apk)
2. Download Eclipse and install the ADT plugin
3. Set the NDK path in Eclipse: Window->Prefrences->Android->NDK
4. Import the project from ``<repository dir>/CatroidDrone/CatroidDrone``
5. Click 'Run Configuration' and set a new configuration. Launch your Activity of choice. In our case we set it to CatroidDummy and save it. Now you are able to execute it.

###Development with Windows
To develope with Windows the native libraries (``<repository dir>\CatroidDrone\CatroidDrone\libs\armeabi*\*.so``) have to be built once. Please follow the [build APK Guide](https://github.com/wagnergerald/CatroidDrone/#build-apk).

1. Download the [ADT-Bundle for Windows](http://developer.android.com/sdk/index.html?utm_source=weibolife).
2. Clone the project. 
3. Import the project from ``<repository dir>/CatroidDrone/CatroidDrone`` in Eclipse.
4. Disable the CDT builder: Right click on project -> Properties -> Builders -> (uncheck) CDT-Builder 

###Development-IDE Netbeans
For those of you who prefer the Netbeans IDE, here a short guide. The steps are mostly the same.

1. Follow the [Build APK Guide](https://github.com/wagnergerald/CatroidDrone/#build-apk)
2. Install Netbeans and the [Android Plugin](http://plugins.netbeans.org/plugin/19545/nbandroid)
3. Import the project from ``<repository dir>/CatroidDrone/CatroidDrone``
4. Click Project/Properties/Run and choose your preferred Activity.
