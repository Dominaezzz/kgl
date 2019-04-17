package com.kgl.stb

class STBException(failureReason: String?) : RuntimeException("Failed to load image. Reason: $failureReason")
