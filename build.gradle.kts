buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.0")
        classpath("com.google.android.gms:play-services-auth:20.7.0")
        classpath ("com.android.tools.build:gradle:8.1.3")
    }
    repositories {
        google()
        mavenCentral()
        jcenter()
        gradlePluginPortal()
        maven { url = uri("https://jitpack.io")
        }
        maven { url = uri("https://naver.jfrog.io/artifactory/maven/")
        }
    }

}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.3" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.gms.google-services") version "4.4.0" apply false
}
