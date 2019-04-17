package com.kgl.stb

import org.lwjgl.stb.STBImage

actual enum class Channels(val value: Int) {
	GREY(STBImage.STBI_grey),
	GREY_ALPHA(STBImage.STBI_grey_alpha),
	RGB(STBImage.STBI_rgb),
	RGB_ALPHA(STBImage.STBI_rgb_alpha)
}
