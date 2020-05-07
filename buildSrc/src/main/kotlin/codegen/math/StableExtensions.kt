package codegen.math

import com.squareup.kotlinpoet.BOOLEAN
import com.squareup.kotlinpoet.U_INT
import com.squareup.kotlinpoet.U_LONG

fun stableExtensions() {
	vectorRelational()
}

private fun vectorRelational() {
	vectorTypes.forEach { (type, valueType, length) ->
		valueType ?: error("base type is null for vector type")

		val componentNames = allComponentNames.take(length ?: 0)

		val boolVectorType = vectorTypes.find { it.valueType == BOOLEAN && it.length == length }!!.type

		buildFile(packageName, "${type.simpleName}VectorRelational") {
			indent("\t")

			// equal

			extensionFunction(type, "equal") {
				parameter("other", type)
				parameter("epsilon", valueType)
				returns(boolVectorType)
				statement("return this.equal(other, %T(epsilon))", type)
			}

			when (valueType) {
				BOOLEAN -> {
					extensionFunction(type, "equal") {
						parameter("other", type)
						parameter("epsilon", type)
						returns(boolVectorType)
						val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "$it xor other.$it" }
						statement("return %T($args).lessThanEqual(epsilon)", type)
					}
				}
				U_INT, U_LONG -> {
					extensionFunction(type, "equal") {
						parameter("other", type)
						parameter("epsilon", type)
						returns(boolVectorType)
						val convert = "to${type.simpleName.drop(1)}()"
						statement("return abs(this.$convert - other.$convert).to${type.simpleName}().lessThanEqual(epsilon)")
					}
				}
				else -> {
					extensionFunction(type, "equal") {
						parameter("other", type)
						parameter("epsilon", type)
						returns(boolVectorType)
						statement("return abs(this - other).lessThanEqual(epsilon)")
					}
				}
			}
		}.writeTo(commonOutDir)
	}
}
