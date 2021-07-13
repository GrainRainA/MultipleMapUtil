# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#百度地图
-keep class com.baidu.** {*;}
-keep class vi.com.** {*;}
-keep class com.baidu.vi.** {*;}
-dontwarn com.baidu.**

#高德地图2D
-keep class com.amap.api.maps2d.**{*;}
-keep class com.amap.api.mapcore2d.**{*;}

#高德地图定位
-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}

#腾讯地图
-keep class com.tencent.tencentmap.**{*;}
-keep class com.tencent.map.**{*;}
-keep class com.tencent.beacontmap.**{*;}
-keep class navsns.**{*;}
-dontwarn com.qq.**
-dontwarn com.tencent.**

#腾讯地图定位
-keepclassmembers class ** {  public void on*Event(...);}
-keep class c.t.**{ *;}
-keep class com.tencent.map.geolocation.**{ *;}
-keep public class com.tencent.location.**{ public protected *;}
-keepclasseswithmembernames class * { native <methods>;}
-dontwarn  org.eclipse.jdt.annotation.**
-dontwarn  c.t.**