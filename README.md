# ClockView —— 简单的Android时钟控件

### 使用前：

先在项目中的build.gradle中添加maven：

    allprojects {
        repositories {

            maven { url 'https://jitpack.io' }

        }
    }

然后在app的build.gradle中添加依赖：

    dependencies {

        compile 'com.github.LanceShu:ClockView:1.2.1'

    }

### 使用方法：

在xml布局中：

    <com.example.lance.clockview.ClockView
        android:id="@+id/clockView"
        android:layout_width="300dp"
        android:layout_height="300dp"
        android:layout_centerInParent="true" />

属性attrs：

    <com.example.lance.clockview.ClockView
        android:id="@+id/clockView"
        android:layout_width="300dp"
        android:layout_height="300dp"
        android:layout_centerInParent="true"
        <!--边框颜色-->
        app:borderColor="#b31111"
        <!--边框样式（stroke：边框   fill：表盘覆盖）-->
        app:borderType="stroke"
        <!--边框大小-->
        app:borderSize="6"
        <!--秒针颜色-->
        app:secondColor="#b31111"
        <!--秒针大小-->
        app:secondSize="2"
        <!--分针大小-->
        app:minSize="5"
        <!--分针颜色-->
        app:minColor="#000"
        <!--时针大小-->
        app:hourSize="8"
        <!--时针颜色-->
        app:hourColor="#000"
        <!--刻度颜色-->
        app:lineColor="#3032c4"
    />

在Activity中的设置：

1. ClockView.start():使时钟开始转动；
2. ClockView.setTime(int hour,int minute,int second):设置时间值（时分秒）；

### 效果图：

![](https://i.imgur.com/TnTvggd.png) ![](https://i.imgur.com/QzMppdM.png)
