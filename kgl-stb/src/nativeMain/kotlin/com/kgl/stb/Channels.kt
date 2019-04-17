package com.kgl.stb

import cstb.STBI_grey
import cstb.STBI_grey_alpha
import cstb.STBI_rgb
import cstb.STBI_rgb_alpha

actual enum class Channels(val value: Int) {
	GREY(STBI_grey.toInt()),
	GREY_ALPHA(STBI_grey_alpha.toInt()),
	RGB(STBI_rgb.toInt()),
	RGB_ALPHA(STBI_rgb_alpha.toInt())
}
