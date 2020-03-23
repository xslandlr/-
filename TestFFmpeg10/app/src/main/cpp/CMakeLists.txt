cmake_minimum_required(VERSION 3.4.1)

file(GLOB allCpp *.cpp)

add_library(
         native-lib
        SHARED
         native-lib.cpp
        ${allCpp})

find_library(
        log-lib
        log)
# 引入FFmpeg的头文件
include_directories(${CMAKE_SOURCE_DIR}/include)
message(WARNING "Enter cmake ${CMAKE_SOURCE_DIR}")

# 引入FFmpeg的库文件，设置内部的方式引入，指定库的目录是 -L  指定具体的库-l
# set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -L${CMAKE_SOURCE_DIR}/${CMAKE_ANDROID_ARCH_ABI}")
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -L${CMAKE_SOURCE_DIR}/${CMAKE_ANDROID_ARCH_ABI}")
message(WARNING "Enter cmake ${CMAKE_CXX_FLAGS}")
message(WARNING "Enter cmake ${CMAKE_SOURCE_DIR}")
message(WARNING "Enter cmake ${CMAKE_ANDROID_ARCH_ABI}")

target_link_libraries(
        native-lib
        libavformat.so libavcodec.so libavfilter.so  libavutil.so libswresample.so libswscale.so
        ${log-lib} )