# CDIO_Robot
CDIO project for group 14


## [OpenCV 4.1](https://opencv-java-tutorials.readthedocs.io/en/latest/01-installing-opencv-for-java.html)

### MacOS
```bash
brew install ant
brew edit opencv
```
Change `-DBUILD_opencv_java=OFF` to `-DBUILD_opencv_java=ON`

```bash
brew install -s opencv
```

### Linux
```bash
lsb_release -a; getconf LONG_BIT
apt install build-essential cmake git pkg-config libgtk-3-dev libavcodec-dev libavformat-dev libswscale-dev libv4l-dev libxvidcore-dev libx264-dev libjpeg-dev libpng-dev libtiff-dev gfortran openexr python3-dev python3-numpy libtbb2 libtbb-dev libdc1394-22-dev -y
mkdir opencv_build && cd opencv_build
git clone https://github.com/opencv/opencv.git
git clone https://github.com/opencv/opencv_contrib.git
cd opencv
mkdir build && cd build
cmake -D CMAKE_BUILD_TYPE=RELEASE \ -D CMAKE_INSTALL_PREFIX=/usr/local \ -D INSTALL_C_EXAMPLES=ON \ -D INSTALL_PYTHON_EXAMPLES=ON \ -D OPENCV_EXTRA_MODULES_PATH=~/opencv_build/opencv_contrib/modules \ -D BUILD_EXAMPLES=ON ..
```

```bash
make -j{number_of_processors}
```

```bash
make install
```

If issues with cmake:
```bash
rm CMakeCache.txt
```
