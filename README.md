# Kotlin Graphics Libraries
Kotlin Multiplatform libraries for graphics.
- [GLFW](https://www.glfw.org)
- [Vulkan](https://www.khronos.org/vulkan)
- [OpenGL](https://www.opengl.org) (TODO)

|Platform|Status|
|-----|-----|
|Linux/MacOS|[![Build Status](https://travis-ci.com/Dominaezzz/kgl.svg?branch=master)](https://travis-ci.com/Dominaezzz/kgl)|
|Windows|[![Build status](https://ci.appveyor.com/api/projects/status/github/Dominaezzz/kgl?branch=master&svg=true)](https://ci.appveyor.com/project/Dominaezzz/kgl)|

[![Download](https://api.bintray.com/packages/dominaezzz/kotlin-native/kgl/images/download.svg)](https://bintray.com/dominaezzz/kotlin-native/kgl/_latestVersion)

KGL uses LWJGL for the JVM target and the respective native libraries on the native targets.
It provides a thin OOP wrapper with DSLs to make programming with vulkan easier.

You can find some samples [here](https://github.com/Dominaezzz/kgl-vulkan-samples).

## Design
The main goal of this library is to hide the verbosity of working with vulkan.

For example in C++, to create a vulkan instance one would have to write code like,
```C++
std::vector<std::string> layers = TODO();
std::vector<std::string> extensions = TODO();

VkApplicationInfo applicationInfo = {};
applicationInfo.sType = VK_STRUCTURE_TYPE_APPLICATION_INFO;
applicationInfo.pNext = nullptr;
applicationInfo.pApplicationName = "Kgl App";
applicationInfo.applicationVersion = VK_MAKE_VERSION(1, 0, 0);
applicationInfo.pEngineName = "No Engine yet";
applicationInfo.engineVersion = VK_MAKE_VERSION(1, 0, 0);
applicationInfo.apiVersion = VK_VERSION_1_1;

VkInstanceCreateInfo createInfo = {};
createInfo.sType = VK_STRUCTURE_TYPE_INSTANCE_CREATE_INFO;
createInfo.pNext = nullptr;
createInfo.flags = 0;
createInfo.pApplicationInfo = &applicationInfo;
createInfo.enabledLayerCount = layers.size();
createInfo.ppEnabledLayerNames = layers.data();
createInfo.enabledExtensionCount = extensions.size();
createInfo.ppEnabledExtensionNames = extensions.data();

VkInstance instance;
if (vkCreateInstance(&createInfo, nullptr, &instance) != VK_SUCCESS) {
    throw std::runtime_error("Failed to create instance!");
}
```
but in Kotlin (with the help of KGL),
```kotlin
val layers: List<String> = TODO()
val extensions: List<String> = TODO()

val instance = Instance.create(layers, extensions) {
    applicationInfo {
        applicationName = "Kgl App"
        applicationVersion = VkVersion(1u, 1u, 0u)
        engineName = "No engine yet"
        engineVersion = VkVersion(1u, 0u, 0u)
        apiVersion = VkVersion(1u, 1u, 0u)
    }
}
```

To create a device in C++,
```C++
uint32_t deviceCount = 1;
VkPhysicalDevice physicalDevice;
vkEnumeratePhysicalDevices(instance, &deviceCount, &physicalDevice);
if (deviceCount < 1) throw std::runtime_error("Failed to find GPU with vulkan support.");

std::vector<VkDeviceQueueCreateInfo> queueCreateInfos(2);

float queuePriority = 1.0f;

VkDeviceQueueCreateInfo& queueCreateInfo1 = queueCreateInfos[0];
queueCreateInfo1.sType = VK_STRUCTURE_TYPE_DEVICE_QUEUE_CREATE_INFO;
queueCreateInfo1.pNext = nullptr;
queueCreateInfo1.flags = VK_DEVICE_QUEUE_CREATE_PROTECTED;
queueCreateInfo1.queueFamilyIndex = 1;
queueCreateInfo1.queueCount = 1;
queueCreateInfo1.pQueuePriorities = &queuePriority;

VkDeviceQueueCreateInfo& queueCreateInfo2 = queueCreateInfos[1];
queueCreateInfo2.sType = VK_STRUCTURE_TYPE_DEVICE_QUEUE_CREATE_INFO;
queueCreateInfo2.pNext = nullptr;
queueCreateInfo2.flags = 0;
queueCreateInfo2.queueFamilyIndex = 2;
queueCreateInfo2.queueCount = 1;
queueCreateInfo2.pQueuePriorities = &queuePriority;

VkPhysicalDeviceFeatures deviceFeatures = {};
deviceFeatures.samplerAnisotropy = VK_TRUE;
deviceFeatures.geometryShader = VK_TRUE;
deviceFeatures.depthClamp = VK_TRUE;

std::vector<std::string> layers = TODO();
std::vector<std::string> extensions = TODO();

VkDeviceCreateInfo createInfo = {};
createInfo.sType = VK_STRUCTURE_TYPE_DEVICE_CREATE_INFO;
createInfo.pNext = nullptr;
createInfo.pQueueCreateInfos = queueCreateInfos.data();
createInfo.queueCreateInfoCount = queueCreateInfos.size();
createInfo.pEnabledFeatures = &deviceFeatures;
createInfo.enabledExtensionCount = extensions.size();
createInfo.ppEnabledExtensionNames = extensions.data();
createInfo.enabledLayerCount = layers.size();
createInfo.ppEnabledLayerNames = layers.data();

VkDevice device;
if (vkCreateDevice(physicalDevice, &createInfo, nullptr, &device) != VK_SUCCESS) {
    throw std::runtime_error("failed to create logical device!");
}
```
but in Kotlin,
```kotlin
val physicalDevice = instance.physicalDevices.first()

val device = physicalDevice.createDevice(layers, extensions) {
    queues {
        queue(1u, 1.0f) {
            flags = DeviceQueueCreate.PROTECTED
        }

        queue(2u, 1.0f)
    }

    enabledFeatures {
        samplerAnisotropy = true
        geometryShader = true
        depthClamp = true
    }
}
```

### Handles
Every vulkan handle has a class of it's own. The name of the class being the name of the handle without the `Vk` prefix.
Like `Instance` for `VkInstance`, `CommandBuffer` for `VkCommandBuffer`, etc.
All handles keep a reference to their parent, to be able to destroy or free themselves later.
Some handles hold a few values from their creation. Like `Image` has `size`, `layers` and `format` properies.

### Structs
Input structs on the other hand have a DSL builder class.
Output structs have a corresponding data class.
```C++
typedef struct VkLayerProperties {
    char        layerName[VK_MAX_EXTENSION_NAME_SIZE];
    uint32_t    specVersion;
    uint32_t    implementationVersion;
    char        description[VK_MAX_DESCRIPTION_SIZE];
} VkLayerProperties;
```
```kotlin
data class LayerProperties(
    val layerName: String,
    val specVersion: VkVersion,
    val implementationVersion: UInt,
    val description: String
)
```

### Enums
Enums are represented with a kotlin enum.
If the enum is a part a bitmask then it extends `VkFlag<T>` to allow for type-safe bit fiddling.
```C++
typedef VkFlags VkCullModeFlags;
typedef enum VkCullModeFlagBits {
    VK_CULL_MODE_NONE = 0,
    VK_CULL_MODE_FRONT_BIT = 0x00000001,
    VK_CULL_MODE_BACK_BIT = 0x00000002,
    VK_CULL_MODE_FRONT_AND_BACK = 0x00000003,
} VkCullModeFlagBits;

VkCullModeFlags flags = VK_CULL_MODE_FRONT_BIT | VK_CULL_MODE_BACK_BIT;
```
```kotlin
enum class CullMode : VkFlag<CullMode> {
    NONE,
    FRONT,
    BACK
}

val flags: VkFlag<CullMode> = CullMode.FRONT or CullMode.BACK
```
Although this does mean that bitwise operations create new objects.
Once inline enums are implemented in Kotlin, we'll get the type-safety without the allocations.

### Commands
Every command's function pointer has been explicitly loaded using `vkGetDeviceProcAddr` and `vkGetInstanceProcAddr` for optimal command calling performance.
This also means you don't need to have the [Vulkan SDK](https://vulkan.lunarg.com/sdk/home) installed to get started with kgl-vulkan.

At the moment every core and extension (non-platform specific) command has been implemented as a member function/property of a handle class.
In most cases it is a member of the last of the first consecutive handles in the parameter list.
In other cases, it is moved to a handle class that makes the most sense.
```C
VkResult vkMapMemory(VkDevice device, VkDeviceMemory memory, VkDeviceSize offset, VkDeviceSize size, VkMemoryMapFlags flags, void** ppData);
```
```kotlin
fun DeviceMemory.map(offset: ULong, size: ULong, flags: VkFlag<MemoryMap>? = null): IoBuffer
```
or
```C++
VkResult vkEnumeratePhysicalDevices(VkInstance instance, uint32_t* pPhysicalDeviceCount, VkPhysicalDevice* pPhysicalDevices);
```
```kotlin
val Instance.physicalDevices: List<PhysicalDevice>
```

### GLFW Example
```kotlin
val window = Window(1080, 720, "Sample!") {
    clientApi = ClientApi.None
    resizable = false
    visible = true
}

val (width, height) = window.size
val mode = Glfw.primaryMonitor!!.videoMode
window.position = ((mode.width - width) / 2) to ((mode.height - height) / 2)
```

## Limitations
- Platform specific extensions have not been implemented yet. Mostly because of [this](https://youtrack.jetbrains.com/issue/KT-27801).
- Support for pNext has not been implemented yet.
- Some parts of the api that use an `IoBuffer` have not yet been implemented as it requires bespoke design.
- Documentation has not been generated yet.
- Until the bulk of library is under codegen, only version `1.1.92` will be supported.
