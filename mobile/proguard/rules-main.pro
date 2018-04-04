# DOCUMENTATION ====================================================================================

# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified in
# C:/Program Files (x86)/Android/android-studio/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the ProGuard include property in project.properties.
#
# For more details, see http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following and specify the fully qualified class
# name to the JavaScript interface class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# CONFIGURATION ====================================================================================

# Rules only for PROGUARD configuration phase. These should be 'removed' for the production ready version.
# --------------------------------------------------------------------------------------------------
#-renamesourcefileattribute SourceFile
# Keep source file attributes and line numbers. Can be useful when examining thrown exceptions.
# Keep this uncommented for the Crashlitics to show crashes more readable.
#-keepattributes SourceFile, LineNumberTable
# Keep names of all classes and theirs methods. Can be useful when examining thrown exceptions.
#-keepnames class ** { *; }

# BASE SETUP =======================================================================================

# Keep attributes for the mentioned components.
-keepattributes Signature, Exceptions, InnerClasses, EnclosingMethod, *Annotation*
# Keep all annotations.
-keep public @interface * { *; }

# DEPENDENCIES SPECIFIC RULES ======================================================================

### ARCHITECTURE [start] ---------------------------------------------------------------------------
-keep class android.arch.lifecycle.** { *; }
### ARCHITECTURE [end] -----------------------------------------------------------------------------

### OTTO [start] -----------------------------------------------------------------------------------
-keepclassmembers class ** {
    @com.squareup.otto.Subscribe public *;
    @com.squareup.otto.Produce public *;
}
### OTTO [end] -------------------------------------------------------------------------------------

# APPLICATION SPECIFIC RULES =======================================================================

# Keep implementation details for application services.
-keepnames class com.surglogs.app.service.api.Services { *; }
-keepnames class com.surglogs.app.service.api.AuthServices { *; }
-keep class com.surglogs.app.service.model.** { *; }
-keep class com.surglogs.app.service.api.request.** { *; }
-keep class com.surglogs.app.service.api.response.** { *; }
# Keep custom implementations of EntityModel factories.
-keepclassmembers class * implements universum.studios.android.database.model.EntityModel {
    public static final com.surglogs.app.data.entity.EntityPoolFactory FACTORY;
}
# Keep all public constructors of view models.
-keep class * extends android.arch.lifecycle.ViewModel {
    public <init>(...);
}

# DO NOT WARN ======================================================================================

# Add others -dontwarn only if app is working properly without proguard.
-dontwarn dagger.android.**
-dontwarn android.support.**
-dontwarn com.google.android.**
-dontwarn universum.studios.android.**
-dontwarn butterknife.**
-dontwarn okio.**
-dontwarn okhttp3.**
-dontwarn retrofit2.**
-dontwarn org.joda.time.**
-dontwarn org.apache.commons.**