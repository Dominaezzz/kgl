# Kotlin Graphics Libraries
Kotlin Multiplatform libraries for graphics.
- GLFW
- Vulkan
- OpenGL (TODO)

|Platform|Status|
|-----|-----|
|Linux/MacOS|[![Build Status](https://travis-ci.com/Dominaezzz/kgl.svg?branch=master)](https://travis-ci.com/Dominaezzz/kgl)|
|Windows|[![Build status](https://ci.appveyor.com/api/projects/status/github/Dominaezzz/kgl?branch=master&svg=true)](https://ci.appveyor.com/project/Dominaezzz/kgl)|

[ ![Download](https://api.bintray.com/packages/dominaezzz/kotlin-native/kgl/images/download.svg) ](https://bintray.com/dominaezzz/kotlin-native/kgl/0.1.0/link)

KGL uses LWJGL for the JVM target and the respective native libraries on the native targets.
It provides a thin OOP wrapper with DSLs to make programming with vulkan easier.

## Usage
```kotlin
val window = Window(1080, 720, "Sample!") {
    clientApi = ClientApi.None
    resizable = false
    visible = true
}

val (width, height) = window.size
val mode = Glfw.primaryMonitor!!.videoMode
window.position = ((mode.width - width) / 2) to ((mode.height - height) / 2)

val instance = Instance.create(layers, extensions) {
    applicationInfo {
        applicationName = "Test"
        applicationVersion = VkVersion(1U, 1U, 0U)
        engineName = "No engine!"
        engineVersion = VkVersion(1U, 0U, 0U)
        apiVersion = VkVersion(1u, 0u, 0u)
    }
}

val physicalDevice = instance.physicalDevices.first()

val device = physicalDevice.createDevice(layers, extensions) {
    queues {
        queue(1U, 1f, 1f) {
            flags = DeviceQueueCreate.PROTECTED
        }

        queue(4U, 1f, 0.5f, 0.6f)
    }

    enabledFeatures {
        samplerAnisotropy = true
        geometryShader = true
        depthClamp = true
    }
}
```
