package codegen.math

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.DOUBLE
import com.squareup.kotlinpoet.FLOAT

internal fun experimentalExtensions() {
	logBase()
}

private fun logBase() {
	(primitiveTypes + vectorTypes).forEach { (type, baseType, componentCount) ->
		val componentNames = allComponentNames.take(componentCount ?: 0)
		val mutableType = ClassName(packageName, "Mutable${type.simpleName}")

		buildFile(packageName, "${type.simpleName}LogBase") {
			indent("\t")

			// log

			when (baseType) {
				FLOAT, DOUBLE -> {
					import("kotlin.math", "log")

					function("log") {
						parameter("n", type)
						parameter("base", type)
						returns(type)
						val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "log(n.$it, base.$it)" }
						statement("return %T($args)", type)
					}

					function("logInPlace") {
						parameter("n", mutableType)
						parameter("base", type)
						componentNames.forEach {
							statement("n.$it = log(n.$it, base.$it)")
						}
					}
				}
			}
		}.writeTo(commonOutDir)
	}
}
