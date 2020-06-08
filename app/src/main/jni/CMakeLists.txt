# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

set(CMAKE_VERBOSE_MAKEFILE on)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11")

#set(OPENCV_PATH "D:/study/AndroidStudioProjects/OpenCV-android-sdk")
set(OPENCV_PATH "D:/study/AndroidStudioProjects/OpenCV-4.2.0/OpenCV-android-sdk")
set(pathToProject "D:/study/AndroidStudioProjects/FaceDetection")

include_directories(${OPENCV_PATH}/sdk/native/jni/include)

add_library( # Sets the name of the library.
        detection_based_tracker

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        DetectionBasedTracker_jni.cpp)

add_library(
        rotate
        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        rotate.cpp)

#动态方式加载
add_library(
        lib_opencv
        SHARED
        IMPORTED)

set_target_properties(lib_opencv PROPERTIES IMPORTED_LOCATION ${pathToProject}/app/src/main/JNILibs/${ANDROID_ABI}/libopencv_java4.so)

target_link_libraries(detection_based_tracker lib_opencv log)

target_link_libraries(rotate lib_opencv log)
