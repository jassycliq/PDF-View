# The proguard configuration file for the following section is /Users/empowerjose/AndroidStudioProjects/PDFView/pdfview/build/intermediates/default_proguard_files/global/proguard-android-optimize.txt-8.0.0-alpha08
# This is a configuration file for ProGuard.
# http://proguard.sourceforge.net/index.html#manual/usage.html
#
# Starting with version 2.2 of the Android plugin for Gradle, this file is distributed together with
# the plugin and unpacked at build-time. The files in $ANDROID_HOME are no longer maintained and
# will be ignored by new version of the Android plugin for Gradle.

# Optimizations: If you don't want to optimize, use the proguard-android.txt configuration file
# instead of this one, which turns off the optimization flags.
# Adding optimization introduces certain risks, since for example not all optimizations performed by
# ProGuard works on all versions of Dalvik.  The following flags turn off various optimizations
# known to have issues, but the list may not be complete or up to date. (The "arithmetic"
# optimization can be used if you are only targeting Android 2.0 or later.)  Make sure you test
# thoroughly if you go this route.
-optimizations !code/simplification/cast,!field/*,!class/merging/*
-optimizationpasses 5
-allowaccessmodification

-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose

# Preserve some attributes that may be required for reflection.
-keepattributes AnnotationDefault,
                EnclosingMethod,
                InnerClasses,
                RuntimeVisibleAnnotations,
                RuntimeVisibleParameterAnnotations,
                RuntimeVisibleTypeAnnotations,
                Signature

-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService
-keep public class com.google.android.vending.licensing.ILicensingService
-dontnote com.android.vending.licensing.ILicensingService
-dontnote com.google.vending.licensing.ILicensingService
-dontnote com.google.android.vending.licensing.ILicensingService

# For native methods, see http://proguard.sourceforge.net/manual/examples.html#native
-keepclasseswithmembernames,includedescriptorclasses class * {
    native <methods>;
}

# Keep setters in Views so that animations can still work.
-keepclassmembers public class * extends android.view.View {
    void set*(***);
    *** get*();
}

# We want to keep methods in Activity that could be used in the XML attribute onClick.
-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
}

# For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclassmembers class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

# Preserve annotated Javascript interface methods.
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# The support libraries contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version. We know about them, and they are safe.
-dontnote android.support.**
-dontnote androidx.**
-dontwarn android.support.**
-dontwarn androidx.**

# This class is deprecated, but remains for backward compatibility.
-dontwarn android.util.FloatMath

# Understand the @Keep support annotation.
-keep class android.support.annotation.Keep
-keep class androidx.annotation.Keep

-keep @android.support.annotation.Keep class * {*;}
-keep @androidx.annotation.Keep class * {*;}

-keepclasseswithmembers class * {
    @android.support.annotation.Keep <methods>;
}

-keepclasseswithmembers class * {
    @androidx.annotation.Keep <methods>;
}

-keepclasseswithmembers class * {
    @android.support.annotation.Keep <fields>;
}

-keepclasseswithmembers class * {
    @androidx.annotation.Keep <fields>;
}

-keepclasseswithmembers class * {
    @android.support.annotation.Keep <init>(...);
}

-keepclasseswithmembers class * {
    @androidx.annotation.Keep <init>(...);
}

# These classes are duplicated between android.jar and org.apache.http.legacy.jar.
-dontnote org.apache.http.**
-dontnote android.net.http.**

# These classes are duplicated between android.jar and core-lambda-stubs.jar.
-dontnote java.lang.invoke.**

# End of content from /Users/empowerjose/AndroidStudioProjects/PDFView/pdfview/build/intermediates/default_proguard_files/global/proguard-android-optimize.txt-8.0.0-alpha08
# The proguard configuration file for the following section is /Users/empowerjose/AndroidStudioProjects/PDFView/pdfview/proguard-rules.pro
# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.
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
# End of content from /Users/empowerjose/AndroidStudioProjects/PDFView/pdfview/proguard-rules.pro
# The proguard configuration file for the following section is /Users/empowerjose/AndroidStudioProjects/PDFView/pdfview/build/intermediates/aapt_proguard_file/release/aapt_rules.txt
# Generated by the gradle plugin

# End of content from /Users/empowerjose/AndroidStudioProjects/PDFView/pdfview/build/intermediates/aapt_proguard_file/release/aapt_rules.txt
# The proguard configuration file for the following section is /Users/empowerjose/.gradle/caches/transforms-3/249d7393072d00e3f23a535fd8a4eb94/transformed/ui-1.3.0/proguard.txt
# Copyright (C) 2020 The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

# We supply these as stubs and are able to link to them at runtime
# because they are hidden public classes in Android. We don't want
# R8 to complain about them not being there during optimization.
-dontwarn android.view.RenderNode
-dontwarn android.view.DisplayListCanvas

-keepclassmembers class androidx.compose.ui.platform.ViewLayerContainer {
    protected void dispatchGetDisplayList();
}

-keepclassmembers class androidx.compose.ui.platform.AndroidComposeView {
    android.view.View findViewByAccessibilityIdTraversal(int);
}

# End of content from /Users/empowerjose/.gradle/caches/transforms-3/249d7393072d00e3f23a535fd8a4eb94/transformed/ui-1.3.0/proguard.txt
# The proguard configuration file for the following section is /Users/empowerjose/.gradle/caches/transforms-3/f6cf01b30d3f9259021e11a7f5d6939c/transformed/material-1.7.0/proguard.txt
# Copyright (C) 2015 The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

# CoordinatorLayout resolves the behaviors of its child components with reflection.
-keep public class * extends androidx.coordinatorlayout.widget.CoordinatorLayout$Behavior {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>();
}

# Make sure we keep annotations for CoordinatorLayout's DefaultBehavior
-keepattributes RuntimeVisible*Annotation*

# Copyright (C) 2018 The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

# AppCompatViewInflater reads the viewInflaterClass theme attribute which then
# reflectively instantiates MaterialComponentsViewInflater using the no-argument
# constructor. We only need to keep this constructor and the class name if
# AppCompatViewInflater is also being kept.
-if class androidx.appcompat.app.AppCompatViewInflater
-keep class com.google.android.material.theme.MaterialComponentsViewInflater {
    <init>();
}


# End of content from /Users/empowerjose/.gradle/caches/transforms-3/f6cf01b30d3f9259021e11a7f5d6939c/transformed/material-1.7.0/proguard.txt
# The proguard configuration file for the following section is /Users/empowerjose/.gradle/caches/transforms-3/42748544384315d172b5a5775722351c/transformed/appcompat-1.5.1/proguard.txt
# Copyright (C) 2018 The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

# aapt is not able to read app::actionViewClass and app:actionProviderClass to produce proguard
# keep rules. Add a commonly used SearchView to the keep list until b/109831488 is resolved.
-keep class androidx.appcompat.widget.SearchView { <init>(...); }

# Never inline methods, but allow shrinking and obfuscation.
-keepclassmembernames,allowobfuscation,allowshrinking class androidx.appcompat.widget.AppCompatTextViewAutoSizeHelper$Impl* {
  <methods>;
}

# End of content from /Users/empowerjose/.gradle/caches/transforms-3/42748544384315d172b5a5775722351c/transformed/appcompat-1.5.1/proguard.txt
# The proguard configuration file for the following section is /Users/empowerjose/.gradle/caches/transforms-3/81ecb2b801d14b765689de43a10be833/transformed/fragment-1.3.6/proguard.txt
# Copyright (C) 2020 The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

# The default FragmentFactory creates Fragment instances using reflection
-if public class ** extends androidx.fragment.app.Fragment
-keepclasseswithmembers,allowobfuscation public class <1> {
    public <init>();
}

# End of content from /Users/empowerjose/.gradle/caches/transforms-3/81ecb2b801d14b765689de43a10be833/transformed/fragment-1.3.6/proguard.txt
# The proguard configuration file for the following section is /Users/empowerjose/.gradle/caches/transforms-3/8adfc7188c8b6fa75190b75a1da5804c/transformed/lifecycle-viewmodel-2.5.1/proguard.txt
-keepclassmembers,allowobfuscation class * extends androidx.lifecycle.ViewModel {
    <init>();
}

-keepclassmembers,allowobfuscation class * extends androidx.lifecycle.AndroidViewModel {
    <init>(android.app.Application);
}

# End of content from /Users/empowerjose/.gradle/caches/transforms-3/8adfc7188c8b6fa75190b75a1da5804c/transformed/lifecycle-viewmodel-2.5.1/proguard.txt
# The proguard configuration file for the following section is /Users/empowerjose/.gradle/caches/transforms-3/9cd918a226d3eb3584c0163c39539e89/transformed/lifecycle-viewmodel-savedstate-2.5.1/proguard.txt
-keepclassmembers,allowobfuscation class * extends androidx.lifecycle.ViewModel {
    <init>(androidx.lifecycle.SavedStateHandle);
}

-keepclassmembers,allowobfuscation class * extends androidx.lifecycle.AndroidViewModel {
    <init>(android.app.Application,androidx.lifecycle.SavedStateHandle);
}

# End of content from /Users/empowerjose/.gradle/caches/transforms-3/9cd918a226d3eb3584c0163c39539e89/transformed/lifecycle-viewmodel-savedstate-2.5.1/proguard.txt
# The proguard configuration file for the following section is /Users/empowerjose/.gradle/caches/transforms-3/626e77db125521f96c7a994973c01318/transformed/runtime-1.3.0/proguard.txt
-assumenosideeffects public class androidx.compose.runtime.ComposerKt {
    void sourceInformation(androidx.compose.runtime.Composer,java.lang.String);
    void sourceInformationMarkerStart(androidx.compose.runtime.Composer,int,java.lang.String);
    void sourceInformationMarkerEnd(androidx.compose.runtime.Composer);
}

# End of content from /Users/empowerjose/.gradle/caches/transforms-3/626e77db125521f96c7a994973c01318/transformed/runtime-1.3.0/proguard.txt
# The proguard configuration file for the following section is /Users/empowerjose/.gradle/caches/transforms-3/0d05345e60c5b27bae802ff99aadb43f/transformed/rules/lib/META-INF/com.android.tools/r8/coroutines.pro
# When editing this file, update the following files as well:
# - META-INF/proguard/coroutines.pro
# - META-INF/com.android.tools/proguard/coroutines.pro

# Most of volatile fields are updated with AFU and should not be mangled
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}

# Same story for the standard library's SafeContinuation that also uses AtomicReferenceFieldUpdater
-keepclassmembers class kotlin.coroutines.SafeContinuation {
    volatile <fields>;
}

# These classes are only required by kotlinx.coroutines.debug.AgentPremain, which is only loaded when
# kotlinx-coroutines-core is used as a Java agent, so these are not needed in contexts where ProGuard is used.
-dontwarn java.lang.instrument.ClassFileTransformer
-dontwarn sun.misc.SignalHandler
-dontwarn java.lang.instrument.Instrumentation
-dontwarn sun.misc.Signal

# Only used in `kotlinx.coroutines.internal.ExceptionsConstructor`.
# The case when it is not available is hidden in a `try`-`catch`, as well as a check for Android.
-dontwarn java.lang.ClassValue

# An annotation used for build tooling, won't be directly accessed.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
# End of content from /Users/empowerjose/.gradle/caches/transforms-3/0d05345e60c5b27bae802ff99aadb43f/transformed/rules/lib/META-INF/com.android.tools/r8/coroutines.pro
# The proguard configuration file for the following section is /Users/empowerjose/.gradle/caches/transforms-3/6f2dd1f6002dcb6e997b35703879c262/transformed/rules/lib/META-INF/com.android.tools/r8-from-1.6.0/coroutines.pro
# Allow R8 to optimize away the FastServiceLoader.
# Together with ServiceLoader optimization in R8
# this results in direct instantiation when loading Dispatchers.Main
-assumenosideeffects class kotlinx.coroutines.internal.MainDispatcherLoader {
    boolean FAST_SERVICE_LOADER_ENABLED return false;
}

-assumenosideeffects class kotlinx.coroutines.internal.FastServiceLoaderKt {
    boolean ANDROID_DETECTED return true;
}

# Disable support for "Missing Main Dispatcher", since we always have Android main dispatcher
-assumenosideeffects class kotlinx.coroutines.internal.MainDispatchersKt {
    boolean SUPPORT_MISSING return false;
}

# Statically turn off all debugging facilities and assertions
-assumenosideeffects class kotlinx.coroutines.DebugKt {
    boolean getASSERTIONS_ENABLED() return false;
    boolean getDEBUG() return false;
    boolean getRECOVER_STACK_TRACES() return false;
}

# End of content from /Users/empowerjose/.gradle/caches/transforms-3/6f2dd1f6002dcb6e997b35703879c262/transformed/rules/lib/META-INF/com.android.tools/r8-from-1.6.0/coroutines.pro
# The proguard configuration file for the following section is /Users/empowerjose/.gradle/caches/transforms-3/c1eec07ad763ce93088f5e42107ed761/transformed/rules/lib/META-INF/proguard/okhttp3.pro
# JSR 305 annotations are for embedding nullability information.
-dontwarn javax.annotation.**

# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*

# OkHttp platform used only on JVM and when Conscrypt dependency is available.
-dontwarn okhttp3.internal.platform.ConscryptPlatform

# End of content from /Users/empowerjose/.gradle/caches/transforms-3/c1eec07ad763ce93088f5e42107ed761/transformed/rules/lib/META-INF/proguard/okhttp3.pro
# The proguard configuration file for the following section is /Users/empowerjose/.gradle/caches/transforms-3/3aec9a83a10fb0fcda2e15f53c278f5c/transformed/rules/lib/META-INF/proguard/okio.pro
# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*

# End of content from /Users/empowerjose/.gradle/caches/transforms-3/3aec9a83a10fb0fcda2e15f53c278f5c/transformed/rules/lib/META-INF/proguard/okio.pro
# The proguard configuration file for the following section is /Users/empowerjose/.gradle/caches/transforms-3/d41d6d19d48485f57db423358787d0db/transformed/coordinatorlayout-1.1.0/proguard.txt
# Copyright (C) 2016 The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

# CoordinatorLayout resolves the behaviors of its child components with reflection.
-keep public class * extends androidx.coordinatorlayout.widget.CoordinatorLayout$Behavior {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>();
}

# Make sure we keep annotations for CoordinatorLayout's DefaultBehavior and ViewPager's DecorView
-keepattributes *Annotation*

# End of content from /Users/empowerjose/.gradle/caches/transforms-3/d41d6d19d48485f57db423358787d0db/transformed/coordinatorlayout-1.1.0/proguard.txt
# The proguard configuration file for the following section is /Users/empowerjose/.gradle/caches/transforms-3/71458df192456621fbb77438ec29c245/transformed/transition-1.2.0/proguard.txt
# Copyright (C) 2017 The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

# Keep a field in transition that is used to keep a reference to weakly-referenced object
-keepclassmembers class androidx.transition.ChangeBounds$* extends android.animation.AnimatorListenerAdapter {
  androidx.transition.ChangeBounds$ViewBounds mViewBounds;
}

# End of content from /Users/empowerjose/.gradle/caches/transforms-3/71458df192456621fbb77438ec29c245/transformed/transition-1.2.0/proguard.txt
# The proguard configuration file for the following section is /Users/empowerjose/.gradle/caches/transforms-3/a91f732bb48a49eb8a3fa9479df3533f/transformed/vectordrawable-animated-1.1.0/proguard.txt
# Copyright (C) 2016 The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

# keep setters in VectorDrawables so that animations can still work.
-keepclassmembers class androidx.vectordrawable.graphics.drawable.VectorDrawableCompat$* {
   void set*(***);
   *** get*();
}

# End of content from /Users/empowerjose/.gradle/caches/transforms-3/a91f732bb48a49eb8a3fa9479df3533f/transformed/vectordrawable-animated-1.1.0/proguard.txt
# The proguard configuration file for the following section is /Users/empowerjose/.gradle/caches/transforms-3/dd4c5260e32ac729fdd8d54347fe885a/transformed/recyclerview-1.1.0/proguard.txt
# Copyright (C) 2015 The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

# When layoutManager xml attribute is used, RecyclerView inflates
#LayoutManagers' constructors using reflection.
-keep public class * extends androidx.recyclerview.widget.RecyclerView$LayoutManager {
    public <init>(android.content.Context, android.util.AttributeSet, int, int);
    public <init>();
}

-keepclassmembers class androidx.recyclerview.widget.RecyclerView {
    public void suppressLayout(boolean);
    public boolean isLayoutSuppressed();
}
# End of content from /Users/empowerjose/.gradle/caches/transforms-3/dd4c5260e32ac729fdd8d54347fe885a/transformed/recyclerview-1.1.0/proguard.txt
# The proguard configuration file for the following section is /Users/empowerjose/.gradle/caches/transforms-3/2c6b5b0aeb4da7ca28291597765efcfc/transformed/core-1.9.0/proguard.txt
# Never inline methods, but allow shrinking and obfuscation.
-keepclassmembernames,allowobfuscation,allowshrinking class androidx.core.view.ViewCompat$Api* {
  <methods>;
}
-keepclassmembernames,allowobfuscation,allowshrinking class androidx.core.view.WindowInsetsCompat$*Impl* {
  <methods>;
}
-keepclassmembernames,allowobfuscation,allowshrinking class androidx.core.app.NotificationCompat$*$Api*Impl {
  <methods>;
}
-keepclassmembernames,allowobfuscation,allowshrinking class androidx.core.os.UserHandleCompat$Api*Impl {
  <methods>;
}
-keepclassmembernames,allowobfuscation,allowshrinking class androidx.core.widget.EdgeEffectCompat$Api*Impl {
  <methods>;
}

# End of content from /Users/empowerjose/.gradle/caches/transforms-3/2c6b5b0aeb4da7ca28291597765efcfc/transformed/core-1.9.0/proguard.txt
# The proguard configuration file for the following section is /Users/empowerjose/.gradle/caches/transforms-3/ba7dd40a98f9e505da2157010758deb3/transformed/savedstate-1.2.0/proguard.txt
# Copyright (C) 2019 The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

-keepclassmembers,allowobfuscation class * implements androidx.savedstate.SavedStateRegistry$AutoRecreated {
    <init>();
}

# End of content from /Users/empowerjose/.gradle/caches/transforms-3/ba7dd40a98f9e505da2157010758deb3/transformed/savedstate-1.2.0/proguard.txt
# The proguard configuration file for the following section is /Users/empowerjose/.gradle/caches/transforms-3/d3606af858a02f87b7b951547cc7f67d/transformed/lifecycle-process-2.4.1/proguard.txt
# this rule is need to work properly when app is compiled with api 28, see b/142778206
-keepclassmembers class * extends androidx.lifecycle.EmptyActivityLifecycleCallbacks { *; }
# End of content from /Users/empowerjose/.gradle/caches/transforms-3/d3606af858a02f87b7b951547cc7f67d/transformed/lifecycle-process-2.4.1/proguard.txt
# The proguard configuration file for the following section is /Users/empowerjose/.gradle/caches/transforms-3/690c6b29b63bcb8cd1a76bb60aa42752/transformed/lifecycle-runtime-2.5.1/proguard.txt
-keepattributes AnnotationDefault,
                RuntimeVisibleAnnotations,
                RuntimeVisibleParameterAnnotations,
                RuntimeVisibleTypeAnnotations

-keepclassmembers enum androidx.lifecycle.Lifecycle$Event {
    <fields>;
}

-keep !interface * implements androidx.lifecycle.LifecycleObserver {
}

-keep class * implements androidx.lifecycle.GeneratedAdapter {
    <init>(...);
}

-keepclassmembers class ** {
    @androidx.lifecycle.OnLifecycleEvent *;
}

# this rule is need to work properly when app is compiled with api 28, see b/142778206
# Also this rule prevents registerIn from being inlined.
-keepclassmembers class androidx.lifecycle.ReportFragment$LifecycleCallbacks { *; }
# End of content from /Users/empowerjose/.gradle/caches/transforms-3/690c6b29b63bcb8cd1a76bb60aa42752/transformed/lifecycle-runtime-2.5.1/proguard.txt
# The proguard configuration file for the following section is /Users/empowerjose/.gradle/caches/transforms-3/da6665d3d18c13fe5c1b092b61ccfd6d/transformed/versionedparcelable-1.1.1/proguard.txt
-keep class * implements androidx.versionedparcelable.VersionedParcelable
-keep public class android.support.**Parcelizer { *; }
-keep public class androidx.**Parcelizer { *; }
-keep public class androidx.versionedparcelable.ParcelImpl

# End of content from /Users/empowerjose/.gradle/caches/transforms-3/da6665d3d18c13fe5c1b092b61ccfd6d/transformed/versionedparcelable-1.1.1/proguard.txt
# The proguard configuration file for the following section is /Users/empowerjose/.gradle/caches/transforms-3/52c6e7148183b9bb26aa8381a7c2ca34/transformed/startup-runtime-1.1.1/proguard.txt
# It's important that we preserve initializer names, given they are used in the AndroidManifest.xml.
-keepnames class * extends androidx.startup.Initializer

# These Proguard rules ensures that ComponentInitializers are are neither shrunk nor obfuscated,
# and are a part of the primary dex file. This is because they are discovered and instantiated
# during application startup.
-keep class * extends androidx.startup.Initializer {
    # Keep the public no-argument constructor while allowing other methods to be optimized.
    <init>();
}

-assumenosideeffects class androidx.startup.StartupLogger { public static <methods>; }

# End of content from /Users/empowerjose/.gradle/caches/transforms-3/52c6e7148183b9bb26aa8381a7c2ca34/transformed/startup-runtime-1.1.1/proguard.txt
# The proguard configuration file for the following section is /Users/empowerjose/.gradle/caches/transforms-3/2a95665ce41c429926d6ffc7ff20aed9/transformed/rules/lib/META-INF/proguard/androidx-annotations.pro
-keep,allowobfuscation @interface androidx.annotation.Keep
-keep @androidx.annotation.Keep class * {*;}

-keepclasseswithmembers class * {
    @androidx.annotation.Keep <methods>;
}

-keepclasseswithmembers class * {
    @androidx.annotation.Keep <fields>;
}

-keepclasseswithmembers class * {
    @androidx.annotation.Keep <init>(...);
}

-keepclassmembers,allowobfuscation class * {
  @androidx.annotation.DoNotInline <methods>;
}

# End of content from /Users/empowerjose/.gradle/caches/transforms-3/2a95665ce41c429926d6ffc7ff20aed9/transformed/rules/lib/META-INF/proguard/androidx-annotations.pro
# The proguard configuration file for the following section is <unknown>
-keep class **.R
-keep class **.R$* {*;}
# End of content from <unknown>