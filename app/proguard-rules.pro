# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
#---------------------------------基本指令区----------------------------------
-optimizationpasses 5
-dontskipnonpubliclibraryclassmembers
-ignorewarnings                # 抑制警告 忽略警告
-dontshrink # 不压缩输入的类文件
#-dontoptimize #优化  不优化输入的类文件  要去掉日志 不能加这个
-printmapping proguardMapping.txt
-optimizations !code/simplification/cast,!field/*,!class/merging/*
-keepattributes *Annotation*,InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable

-dontusemixedcaseclassnames                    # 是否使用大小写混合
-dontskipnonpubliclibraryclasses               # 是否混淆第三方jar
-dontpreverify                                 # 混淆时是否做预校验
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*       # 混淆时所采用的算法
-verbose #混淆时是否记录日志
-repackageclasses com.tomze.tiu #增加反编译难度把混淆的很多个文件全部放在同一个目录下
#混淆前后的映射
-printmapping mapping.txt

#EVENTBUS
-keepattributes *Annotation*
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}
# 避免影响升级功能，需要keep住support包的类
-keep class android.support.**{*;}
#保留bean
-keep class com.tomze.tiu.bean.**{*;}
