plugins {
    id("com.android.application")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("com.google.gms.google-services") // Google Services plugin
}

apply { from(rootProject.file("gradle/project_common.gradle").absolutePath) }

android {
    defaultConfig {
        applicationId = "com.github.kitakkun.foos"
    }
    namespace = "com.github.kitakkun.foos"
}

dependencies {
    implementation(project(":user"))
    implementation(project(":customview"))
    implementation(project(":common"))
    implementation(project(":post"))
}
