# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Android\SDK/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-keepparameternames
-renamesourcefileattribute SourceFile
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,EnclosingMethod

-optimizations !code/simplification/arithmetic

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.content.ContentProvider


-keepclassmembers class * {
   void *(android.view.View);
   *** *Click(...);
   *** *Event(...);
}
-keep public class * {
    public protected *;
}
-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * extends android.content.Context {
   public void *(android.view.View);
   public void *(android.view.MenuItem);
}
-keepclassmembers class * implements android.os.Parcelable {
    static android.os.Parcelable$Creator CREATOR;
}
-keepclassmembers class **.R$* {
  public static <fields>;
}
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
-keep public interface com.android.vending.licensing.ILicensingService
-dontnote com.android.vending.licensing.ILicensingService


-dontpreverify
-repackageclasses ''
-allowaccessmodification

-dontwarn android.**
-keep interface android.** { *; }
-keep class android.** { *; }
-keep public class * extends android.**

-keepclassmembers class * extends java.lang.Enum {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class com.fanglin.fenhong.microbuyer.base.model.** { *;}

#--------------- BEGIN: Gson防混淆 ----------
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }
-keep class com.idea.fifaalarmclock.entity.***
#-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.** { *; }
#--------------- END ----------

-dontwarn com.daimajia.**
-keep class com.daimajia.** { *;}

#--------------- BEGIN:信鸽防混淆 ----------
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep class com.tencent.android.tpush.**  {* ;}
-keep class com.tencent.mid.**  {* ;}
#--------------- END ----------

#--------------- BEGIN: SafeWebViewBridge  ----------
-keepclassmembers class cn.pedant.SafeWebViewBridge.sample.HostJsScope$RetJavaObj{ *; }
-keepclassmembers class cn.pedant.SafeWebViewBridge.sample.HostJsScope{ *; }
#--------------- END ----------

# -----友盟-------------------
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keep public class com.fanglin.fenhong.microbuyer.R$*{
   public static final int *;
}
#--------------- END ----------