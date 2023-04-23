plugins {
    id("com.android.library")
}

apply { from(rootProject.file("gradle/project_common.gradle").absolutePath) }

android {
    namespace = "com.github.kitakkun.foos.common"
}

dependencies {
}
