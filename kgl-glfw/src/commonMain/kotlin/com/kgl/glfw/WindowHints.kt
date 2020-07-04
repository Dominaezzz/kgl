package com.kgl.glfw

interface WindowHints {
	@get:Deprecated("Querying window hints is not supported", level = DeprecationLevel.ERROR)
	var resizable: Boolean

	@get:Deprecated("Querying window hints is not supported", level = DeprecationLevel.ERROR)
	var visible: Boolean

	@get:Deprecated("Querying window hints is not supported", level = DeprecationLevel.ERROR)
	var decorated: Boolean

	@get:Deprecated("Querying window hints is not supported", level = DeprecationLevel.ERROR)
	var focused: Boolean

	@get:Deprecated("Querying window hints is not supported", level = DeprecationLevel.ERROR)
	var autoIconify: Boolean

	@get:Deprecated("Querying window hints is not supported", level = DeprecationLevel.ERROR)
	var floating: Boolean

	@get:Deprecated("Querying window hints is not supported", level = DeprecationLevel.ERROR)
	var maximized: Boolean

	@get:Deprecated("Querying window hints is not supported", level = DeprecationLevel.ERROR)
	var centerCursor: Boolean

	@get:Deprecated("Querying window hints is not supported", level = DeprecationLevel.ERROR)
	var transparentFrameBuffer: Boolean

	@get:Deprecated("Querying window hints is not supported", level = DeprecationLevel.ERROR)
	var focusOnShow: Boolean

	@get:Deprecated("Querying window hints is not supported", level = DeprecationLevel.ERROR)
	var scaleToMonitor: Boolean

	@get:Deprecated("Querying window hints is not supported", level = DeprecationLevel.ERROR)
	var redBits: Int

	@get:Deprecated("Querying window hints is not supported", level = DeprecationLevel.ERROR)
	var greenBits: Int

	@get:Deprecated("Querying window hints is not supported", level = DeprecationLevel.ERROR)
	var blueBits: Int

	@get:Deprecated("Querying window hints is not supported", level = DeprecationLevel.ERROR)
	var alphaBits: Int

	@get:Deprecated("Querying window hints is not supported", level = DeprecationLevel.ERROR)
	var depthBits: Int

	@get:Deprecated("Querying window hints is not supported", level = DeprecationLevel.ERROR)
	var stencilBits: Int

	@get:Deprecated("Querying window hints is not supported", level = DeprecationLevel.ERROR)
	var accumRedBits: Int

	@get:Deprecated("Querying window hints is not supported", level = DeprecationLevel.ERROR)
	var accumGreenBits: Int

	@get:Deprecated("Querying window hints is not supported", level = DeprecationLevel.ERROR)
	var accumBlueBits: Int

	@get:Deprecated("Querying window hints is not supported", level = DeprecationLevel.ERROR)
	var accumAlphaBits: Int

	@get:Deprecated("Querying window hints is not supported", level = DeprecationLevel.ERROR)
	var auxBuffers: Int

	@get:Deprecated("Querying window hints is not supported", level = DeprecationLevel.ERROR)
	var stereo: Boolean

	@get:Deprecated("Querying window hints is not supported", level = DeprecationLevel.ERROR)
	var samples: Int

	@get:Deprecated("Querying window hints is not supported", level = DeprecationLevel.ERROR)
	var srgbCapable: Boolean

	@get:Deprecated("Querying window hints is not supported", level = DeprecationLevel.ERROR)
	var doubleBuffer: Boolean

	@get:Deprecated("Querying window hints is not supported", level = DeprecationLevel.ERROR)
	var refreshRate: Int

	@get:Deprecated("Querying window hints is not supported", level = DeprecationLevel.ERROR)
	var clientApi: ClientApi

	@get:Deprecated("Querying window hints is not supported", level = DeprecationLevel.ERROR)
	var contextCreationApi: CreationApi

	@get:Deprecated("Querying window hints is not supported", level = DeprecationLevel.ERROR)
	var contextVersionMajor: Int

	@get:Deprecated("Querying window hints is not supported", level = DeprecationLevel.ERROR)
	var contextVersionMinor: Int

	@get:Deprecated("Querying window hints is not supported", level = DeprecationLevel.ERROR)
	var openGLForwardCompat: Boolean

	@get:Deprecated("Querying window hints is not supported", level = DeprecationLevel.ERROR)
	var openGLDebugContext: Boolean

	@get:Deprecated("Querying window hints is not supported", level = DeprecationLevel.ERROR)
	var openGLProfile: OpenGLProfile

	@get:Deprecated("Querying window hints is not supported", level = DeprecationLevel.ERROR)
	var contextRobustness: Robustness

	@get:Deprecated("Querying window hints is not supported", level = DeprecationLevel.ERROR)
	var contextReleaseBehavior: ReleaseBehaviour

	@get:Deprecated("Querying window hints is not supported", level = DeprecationLevel.ERROR)
	var contextNoError: Boolean

	// macOS-specific

	@get:Deprecated("Querying window hints is not supported", level = DeprecationLevel.ERROR)
	var cocoaRetinaFrameBuffer: Boolean

	@get:Deprecated("Querying window hints is not supported", level = DeprecationLevel.ERROR)
	var cocoaFrameName: String

	@get:Deprecated("Querying window hints is not supported", level = DeprecationLevel.ERROR)
	var cocoaGraphicsSwitching: Boolean

	// X11-specific

	@get:Deprecated("Querying window hints is not supported", level = DeprecationLevel.ERROR)
	var x11ClassName: String

	@get:Deprecated("Querying window hints is not supported", level = DeprecationLevel.ERROR)
	var x11InstanceName: String

	fun restoreDefaults()
}
