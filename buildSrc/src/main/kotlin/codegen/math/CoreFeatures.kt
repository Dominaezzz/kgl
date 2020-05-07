package codegen.math

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.KModifier.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy

internal val primitiveTypes = listOf(
	TypeInfo(BOOLEAN),
	TypeInfo(INT),
	TypeInfo(LONG),
	TypeInfo(U_INT),
	TypeInfo(U_LONG),
	TypeInfo(FLOAT),
	TypeInfo(DOUBLE)
)

internal val vectorTypes = primitiveTypes.flatMap { (type: ClassName) ->
	(2..4).map { c ->
		TypeInfo(ClassName(packageName, "${type.simpleName}Vector$c"), type, c)
	}
}

internal val allComponentNames = listOf("x", "y", "z", "w")

private val arithmetics = listOf("plus" to "+", "minus" to "-", "times" to "*", "div" to "/")

fun coreFeatures() {
	commonFunctions()
	exponentialFunctions()
	geometricFunctions()
	vectorTypes()
	vectorRelationalFunctions()
}

private fun literalOf(literal: String, type: ClassName, valueType: ClassName? = null): Pair<String, String?> {
	return when (valueType) {
		null -> when (type) {
			BOOLEAN -> if (literal == "0") "false" to null else "true" to null
			INT -> literal to null
			LONG -> "${literal}L" to null
			U_INT -> "${literal}U" to null
			U_LONG -> "${literal}UL" to null
			FLOAT -> "${literal}f" to null
			DOUBLE -> "${literal}.0" to null
			else -> error("invalid type: $type")
		}
		else -> {
			val (one) = literalOf(literal, valueType)
			one to "${type.simpleName}($one)"
		}
	}
}

private fun commonFunctions() {
	(primitiveTypes + vectorTypes).forEach { (type, valueType, length) ->
		val componentNames = allComponentNames.take(length ?: 0)
		val mutableType = ClassName(packageName, "Mutable${type.simpleName}")

		val boolVectorType = vectorTypes.find { it.valueType == BOOLEAN && it.length == length }?.type

		val (zero, vectorZero) = literalOf("0", type, valueType)
		val (one, vectorOne) = literalOf("1", type, valueType)
		val (two, vectorTwo) = literalOf("2", type, valueType)
		val (three, vectorThree) = literalOf("3", type, valueType)

		buildFile(packageName, "${type.simpleName}Common") {
			indent("\t")

			// abs

			when (valueType) {
				INT, LONG,
				FLOAT, DOUBLE -> {
					import("kotlin.math", "abs")

					function("abs") {
						parameter("n", type)
						returns(type)
						val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "abs(n.$it)" }
						statement("return %T($args)", type)
					}

					function("absInPlace") {
						parameter("n", mutableType)
						componentNames.forEach {
							statement("n.$it = abs(n.$it)")
						}
					}
				}
			}

			// sign

			when (valueType) {
				INT, LONG,
				FLOAT, DOUBLE -> {
					import("kotlin.math", "sign")

					extensionProperty(type, "sign", type) {
						getter {
							val args = componentNames.joinToString(",\n\t", "\n\t", "\n") {
								"$it.sign${if (valueType == LONG) ".toLong()" else ""}"
							}
							statement("return %T($args)", type)
						}
					}
				}
			}

			// floor

			when (valueType) {
				FLOAT, DOUBLE -> {
					import("kotlin.math", "floor")

					function("floor") {
						parameter("n", type)
						returns(type)
						val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "floor(n.$it)" }
						statement("return %T($args)", type)
					}

					function("floorInPlace") {
						parameter("n", mutableType)
						componentNames.forEach {
							statement("n.$it = floor(n.$it)")
						}
					}
				}
			}

			// trunc

			when (valueType) {
				FLOAT, DOUBLE -> {
					import("kotlin.math", "truncate")

					function("truncate") {
						parameter("n", type)
						returns(type)
						val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "truncate(n.$it)" }
						statement("return %T($args)", type)
					}

					function("truncateInPlace") {
						parameter("n", mutableType)
						componentNames.forEach {
							statement("n.$it = truncate(n.$it)")
						}
					}
				}
			}

			// round

			when (valueType) {
				FLOAT, DOUBLE -> {
					import("kotlin.math", "roundToInt")
					import("kotlin.math", "roundToLong")

					val intVectorType = vectorTypes.find { it.valueType == INT && it.length == length }!!.type
					val longVectorType = vectorTypes.find { it.valueType == LONG && it.length == length }!!.type

					extensionFunction(type, "roundToInt") {
						kdoc(
							"""
								Rounds each component `x` to the nearest integer and stores the result in [${intVectorType.simpleName}].
								Ties are rounded towards positive infinity.
								
								Special cases:
								- `x.roundToInt() == Int.MAX_VALUE` when `x > Int.MAX_VALUE`
								- `x.roundToInt() == Int.MIN_VALUE` when `x < Int.MIN_VALUE`
								
								@throws IllegalArgumentException when this value is `NaN`
								""".trimIndent()
						)
						returns(intVectorType)
						val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "$it.roundToInt()" }
						statement("return %T($args)", intVectorType)
					}

					extensionFunction(type, "roundToLong") {
						kdoc(
							"""
								Rounds each component `x` to the nearest integer and stores the result to [${longVectorType.simpleName}].
								Ties are rounded towards positive infinity.
								
								Special cases:
								- `x.roundToLong() == Long.MAX_VALUE` when `x > Long.MAX_VALUE`
								- `x.roundToLong() == Long.MIN_VALUE` when `x < Long.MIN_VALUE`
								
								@throws IllegalArgumentException when this value is `NaN`
								""".trimIndent()
						)
						returns(longVectorType)
						val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "$it.roundToLong()" }
						statement("return %T($args)", longVectorType)
					}
				}
			}

			// roundEven

			when (valueType) {
				FLOAT, DOUBLE -> {
					import("kotlin.math", "round")

					function("round") {
						kdoc(
							"""
								Rounds each component `x` to the closest integer with ties rounded towards even integers.
								
								Special cases:
								- `round(x)` is `x` where `x` is `NaN` or `+Inf` or `-Inf` or already a mathematical integer. 
								""".trimIndent()
						)
						parameter("n", type)
						returns(type)
						val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "round(n.$it)" }
						statement("return %T($args)", type)
					}

					function("roundInPlace") {
						kdoc(
							"""
								Rounds each component to the closest integer with ties rounded towards even integers.
								
								Special cases:
								- `round(x)` is `x` where `x` is `NaN` or `+Inf` or `-Inf` or already a mathematical integer. 
								""".trimIndent()
						)
						parameter("n", mutableType)
						componentNames.forEach {
							statement("n.$it = round(n.$it)")
						}
					}
				}
			}

			// ceil

			when (valueType) {
				FLOAT, DOUBLE -> {
					import("kotlin.math", "ceil")

					function("ceil") {
						parameter("n", type)
						returns(type)
						val args = componentNames.joinToString { "ceil(n.$it)" }
						statement("return %T($args)", type)
					}

					function("ceilInPlace") {
						parameter("n", mutableType)
						componentNames.forEach {
							statement("n.$it = ceil(n.$it)")
						}
					}
				}
			}

			// fract

			when (type) {
				FLOAT, DOUBLE -> {
					extensionProperty(type, "fraction", type) {
						getter {
							statement("return this - this.toLong()")
						}
					}
				}
			}

			when (valueType) {
				FLOAT, DOUBLE -> {
					extensionProperty(type, "fraction", type) {
						getter {
							val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "$it.fraction" }
							statement("return %T($args)", type)
						}
					}
				}
			}

			// mod

			if (valueType != null && valueType != BOOLEAN) {
				extensionFunction(type, "rem") {
					modifiers(OPERATOR)
					parameter("scalar", valueType)
					returns(type)
					val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "$it %% scalar" }
					statement("return %T($args)", type)
				}

				extensionFunction(type, "rem") {
					modifiers(OPERATOR)
					parameter("other", type)
					returns(type)
					val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "$it %% other.$it" }
					statement("return %T($args)", type)
				}

				extensionFunction(mutableType, "remAssign") {
					modifiers(OPERATOR)
					parameter("scalar", valueType)
					componentNames.forEach {
						statement("$it %%= scalar")
					}
				}

				extensionFunction(mutableType, "remAssign") {
					modifiers(OPERATOR)
					parameter("other", type)
					componentNames.forEach {
						statement("$it %%= other.$it")
					}
				}
			}

			// min

			if (valueType != null) {
				extensionFunction(type, "coerceAtMost") {
					parameter("maximumValue", valueType)
					returns(type)
					val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "$it.coerceAtMost(maximumValue)" }
					statement("return %T($args)", type)
				}

				extensionFunction(type, "coerceAtMost") {
					parameter("maximumValue", type)
					returns(type)
					val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "$it.coerceAtMost(maximumValue.$it)" }
					statement("return %T($args)", type)
				}

				extensionFunction(mutableType, "coerceAssignAtMost") {
					parameter("maximumValue", valueType)
					componentNames.forEach {
						statement("$it = $it.coerceAtMost(maximumValue)")
					}
				}

				extensionFunction(mutableType, "coerceAssignAtMost") {
					parameter("maximumValue", type)
					componentNames.forEach {
						statement("$it = $it.coerceAtMost(maximumValue.$it)")
					}
				}
			}

			// max

			if (valueType != null) {
				extensionFunction(type, "coerceAtLeast") {
					parameter("minimumValue", valueType)
					returns(type)
					val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "$it.coerceAtLeast(minimumValue)" }
					statement("return %T($args)", type)
				}

				extensionFunction(type, "coerceAtLeast") {
					parameter("minimumValue", type)
					returns(type)
					val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "$it.coerceAtLeast(minimumValue.$it)" }
					statement("return %T($args)", type)
				}

				extensionFunction(mutableType, "coerceAssignAtLeast") {
					parameter("minimumValue", valueType)
					componentNames.forEach {
						statement("$it = $it.coerceAtLeast(minimumValue)")
					}
				}

				extensionFunction(mutableType, "coerceAssignAtLeast") {
					parameter("minimumValue", type)
					componentNames.forEach {
						statement("$it = $it.coerceAtLeast(minimumValue.$it)")
					}
				}
			}

			// clamp

			if (valueType != null) {
				extensionFunction(type, "coerceIn") {
					parameter("minimumValue", valueType)
					parameter("maximumValue", valueType)
					returns(type)
					val args = componentNames.joinToString(",\n\t", "\n\t", "\n") {
						"$it.coerceIn(minimumValue, maximumValue)"
					}
					statement("return %T($args)", type)
				}

				extensionFunction(type, "coerceIn") {
					parameter("minimumValue", type)
					parameter("maximumValue", type)
					returns(type)
					val args = componentNames.joinToString(",\n\t", "\n\t", "\n") {
						"$it.coerceIn(minimumValue.$it, maximumValue.$it)"
					}
					statement("return %T($args)", type)
				}

				extensionFunction(type, "coerceIn") {
					parameter("range", ClosedRange::class.asClassName().parameterizedBy(valueType))
					returns(type)
					val args = componentNames.joinToString(",\n\t", "\n\t", "\n") {
						"$it.coerceIn(range)"
					}
					statement("return %T($args)", type)
				}

				extensionFunction(mutableType, "coerceAssignIn") {
					parameter("minimumValue", valueType)
					parameter("maximumValue", valueType)
					componentNames.forEach {
						statement("$it = $it.coerceIn(minimumValue, maximumValue)")
					}
				}

				extensionFunction(mutableType, "coerceAssignIn") {
					parameter("minimumValue", type)
					parameter("maximumValue", type)
					componentNames.forEach {
						statement("$it = $it.coerceIn(minimumValue.$it, maximumValue.$it)")
					}
				}

				extensionFunction(mutableType, "coerceAssignIn") {
					parameter("range", ClosedRange::class.asClassName().parameterizedBy(valueType))
					componentNames.forEach {
						statement("$it = $it.coerceIn(range)")
					}
				}
			}

			// mix

			function("mix") {
				parameter("x", type)
				parameter("y", type)
				parameter("a", BOOLEAN)
				returns(type)
				statement("return if (a) x else y")
			}

			when (type) {
				FLOAT, DOUBLE -> {
					function("mix") {
						parameter("x", type)
						parameter("y", type)
						parameter("a", type)
						returns(type)
						statement("return x * (1f - a) + y * a")
					}
				}
			}

			if (valueType != null) {
				function("mix") {
					parameter("x", type)
					parameter("y", type)
					parameter("a", boolVectorType!!)
					returns(type)
					val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "mix(x.$it, y.$it, a.$it)" }
					statement("return %T($args)", type)
				}
			}

			when (valueType) {
				FLOAT, DOUBLE -> {
					function("mix") {
						parameter("x", type)
						parameter("y", type)
						parameter("a", valueType)
						returns(type)
						val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "mix(x.$it, y.$it, a)" }
						statement("return %T($args)", type)
					}

					function("mix") {
						parameter("x", type)
						parameter("y", type)
						parameter("a", type)
						returns(type)
						val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "mix(x.$it, y.$it, a.$it)" }
						statement("return %T($args)", type)
					}
				}
			}

			// step

			when (valueType) {
				// is primitive type
				null -> {
					function("step") {
						parameter("edge", type)
						parameter("x", type)
						returns(type)
						statement("return mix($one, $zero, x < edge)")
					}
				}

				// is vector type
				else -> {
					function("step") {
						parameter("edge", type)
						parameter("x", type)
						returns(type)
						statement("return mix(\n\t$vectorOne,\n\t$vectorZero,\n\tx.lessThan(edge)\n)")
					}

					function("step") {
						parameter("edge", valueType)
						parameter("x", type)
						returns(type)
						statement("return step(%T(edge), x)", type)
					}
				}
			}

			// smoothStep

			when (type) {
				FLOAT, DOUBLE -> {
					function("smoothStep") {
						parameter("edge0", type)
						parameter("edge1", type)
						parameter("x", type)
						returns(type)
						statement("val tmp = ((x - edge0) / (edge1 - edge0)).coerceIn($zero, $one)")
						statement("return tmp * tmp * ($three - $two * tmp)")
					}
				}

				// is vector type
				else -> when (valueType) {
					FLOAT, DOUBLE -> {
						function("smoothStep") {
							parameter("edge0", type)
							parameter("edge1", type)
							parameter("x", type)
							returns(type)
							statement("val tmp = ((x - edge0) / (edge1 - edge0)).coerceIn($vectorZero, $vectorOne)")
							statement("return tmp * tmp * ($vectorThree - $vectorTwo * tmp)")
						}

						function("smoothStep") {
							parameter("edge0", valueType)
							parameter("edge1", valueType)
							parameter("x", type)
							returns(type)
							statement("return smoothStep(%T(edge0), %T(edge1), x)", type, type)
						}
					}
				}
			}

			// isNaN

			when (valueType) {
				FLOAT, DOUBLE -> {
					extensionFunction(type, "isNaN") {
						returns(boolVectorType!!)
						val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "$it.isNaN()" }
						statement("return %T($args)", boolVectorType)
					}
				}
			}

			// isInf

			when (valueType) {
				FLOAT, DOUBLE -> {
					extensionFunction(type, "isInfinite") {
						returns(boolVectorType!!)
						val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "$it.isInfinite()" }
						statement("return %T($args)", boolVectorType)
					}
				}
			}

			// isFinite (kotlin stdlib)

			when (valueType) {
				FLOAT, DOUBLE -> {
					extensionFunction(type, "isFinite") {
						returns(boolVectorType!!)
						val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "$it.isFinite()" }
						statement("return %T($args)", boolVectorType)
					}
				}
			}

			// floatBitsToInt/floatBitsToUInt

			fun toBitsFunction(bitsType: ClassName, unsigned: Boolean) {
				ClassName(packageName, "${bitsType.simpleName}Vector$length").let { bitsVectorType ->
					extensionFunction(type, "to${if (unsigned) "Unsigned" else ""}Bits") {
						returns(bitsVectorType)
						val args = componentNames.joinToString(",\n\t", "\n\t", "\n") {
							"$it.toBits()${if (unsigned) ".to${bitsType.simpleName}()" else ""}"
						}
						statement("return %T($args)", bitsVectorType)
					}

					extensionFunction(type, "to${if (unsigned) "Unsigned" else ""}RawBits") {
						returns(bitsVectorType)
						val args = componentNames.joinToString(",\n\t", "\n\t", "\n") {
							"$it.toRawBits()${if (unsigned) ".to${bitsType.simpleName}()" else ""}"
						}
						statement("return %T($args)", bitsVectorType)
					}
				}
			}

			when (valueType) {
				FLOAT -> {
					toBitsFunction(INT, unsigned = false)
					toBitsFunction(U_INT, unsigned = true)
				}
				DOUBLE -> {
					toBitsFunction(LONG, unsigned = false)
					toBitsFunction(U_LONG, unsigned = true)
				}
			}

			// intBitsToFloat/uintBitsToFloat

			fun fromBitsFunction(bitsType: ClassName, unsigned: Boolean) {
				ClassName(packageName, "${bitsType.simpleName}Vector$length").let { bitsVectorType ->
					extensionFunction(type.nestedClass("Companion"), "fromBits") {
						parameter("bits", bitsVectorType)
						returns(type)
						val args = componentNames.joinToString(",\n\t", "\n\t", "\n") {
							val unsignedConversion = if (unsigned) ".to${bitsType.simpleName.drop(1)}()" else ""
							"${valueType!!.simpleName}.fromBits(bits.$it$unsignedConversion)"
						}
						statement("return %T($args)", type)
					}

					extensionFunction(mutableType.nestedClass("Companion"), "fromBits") {
						parameter("bits", bitsVectorType)
						returns(type)
						val args = componentNames.joinToString(",\n\t", "\n\t", "\n") {
							val unsignedConversion = if (unsigned) ".to${bitsType.simpleName.drop(1)}()" else ""
							"${valueType!!.simpleName}.fromBits(bits.$it$unsignedConversion)"
						}
						statement("return %T($args)", type)
					}
				}
			}

			when (valueType) {
				FLOAT -> {
					fromBitsFunction(INT, false)
					fromBitsFunction(U_INT, true)
				}
				DOUBLE -> {
					fromBitsFunction(LONG, false)
					fromBitsFunction(U_LONG, true)
				}
			}

			// frexp

			when (type) {
				FLOAT -> {
					extensionFunction(type, "toFractionAndExponent") {
						returns(Pair::class.asClassName().parameterizedBy(type, INT))
						code(
							"""
								// implementation from glibc math
								var fraction = this
								var hx = fraction.toRawBits()
								var ix = hx and 0x7FFF_FFFF
								var exponent = 0
								if (ix >= 0x7F80_0000 || ix == 0) {
									return fraction + fraction to exponent
								}
								if (ix < 0x0080_0000) {
									fraction *= 2e25f
									hx = fraction.toRawBits()
									ix = hx and 0x7FFF_FFFF
									exponent = -25
								}
								exponent += (ix shr 23) - 126
								hx = (hx and 0x807F_FFFF.toInt()) or 0x3F00_0000
								fraction = Float.fromBits(hx)
								return fraction to exponent
								""".trimIndent()
						)
					}
				}
				DOUBLE -> {
					extensionFunction(type, "toFractionAndExponent") {
						returns(Pair::class.asClassName().parameterizedBy(type, INT))
						code(
							"""
								// implementation from glibc math
								var fraction = this
								var ix = fraction.toRawBits()
								var ex = 0x7FF and (ix shr 52).toInt()
								var exponent = 0

								if (ex != 0x7FF && fraction != 0.0) {
									exponent = ex - 1022
									if (ex == 0) {
										fraction *= 2e54
										ix = fraction.toRawBits()
										ex = 0x7FF and (ix shr 52).toInt()
										exponent = ex - 1022 - 54
									}
									ix = (ix and 0x800F_FFFF_FFFF_FFFFu.toLong()) or 0x3FE0_0000_0000_0000
									fraction = Double.fromBits(ix)
								} else {
									fraction += fraction
								}
								return fraction to exponent
								""".trimIndent()
						)
					}
				}
			}

			when (valueType) {
				FLOAT, DOUBLE -> {
					val intVectorType = vectorTypes.find { it.valueType == INT && it.length == length }!!.type
					extensionFunction(type, "toFractionAndExponent") {
						returns(Pair::class.asClassName().parameterizedBy(type, intVectorType))
						val (fargs, iargs) = componentNames.map {
							statement("val (f$it, i$it) = $it.toFractionAndExponent()")
							"f$it" to "i$it"
						}.toMap().run { keys.joinToString() to values.joinToString() }
						statement("return %T($fargs) to %T($iargs)", type, intVectorType)
					}
				}
			}

			// ldexp

			when (type) {
				FLOAT, DOUBLE -> {
					import("kotlin.math", "pow")

					extensionFunction(type.nestedClass("Companion"), "fromFractionAndExponent") {
						parameter("value", type)
						parameter("exp", INT)
						returns(type)
						code(
							"""
								if (value.isInfinite() || value == $zero) return value
								val result = value * $two.pow(exp)
								if (result.isInfinite() || value == $zero) error("value out of range!")
								return result
								""".trimIndent()
						)
					}
				}
			}

			when (valueType) {
				FLOAT, DOUBLE -> {
					val intVectorType = vectorTypes.find { it.valueType == INT && it.length == length }!!.type
					extensionFunction(type.nestedClass("Companion"), "fromFractionAndExponent") {
						parameter("value", type)
						parameter("exp", intVectorType)
						returns(type)
						val args = componentNames.joinToString(",\n\t", "\n\t", "\n") {
							"${valueType.simpleName}.fromFractionAndExponent(value.$it, exp.$it)"
						}
						statement("return %T($args)", type)
					}
				}
			}
		}.writeTo(commonOutDir)
	}
}

private fun exponentialFunctions() {
	vectorTypes.filter {
		it.valueType == FLOAT || it.valueType == DOUBLE
	}.forEach { (type, valueType, length) ->
		valueType ?: error("base type is null for vector type")

		val componentNames = allComponentNames.take(length ?: 0)
		val mutableType = ClassName(packageName, "Mutable${type.simpleName}")

		val intVectorType = vectorTypes.find { it.valueType == INT && it.length == length }?.type

		val (two) = literalOf("2", type, valueType)

		buildFile(packageName, "${type.simpleName}Exponential") {
			indent("\t")

			// pow

			import("kotlin.math", "pow")

			extensionFunction(type, "pow") {
				parameter("exp", INT)
				returns(type)
				val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "$it.pow(exp)" }
				statement("return %T($args)", type)
			}

			extensionFunction(mutableType, "powAssign") {
				parameter("exp", INT)
				componentNames.forEach {
					statement("$it = $it.pow(exp)")
				}
			}

			extensionFunction(type, "pow") {
				parameter("exp", valueType)
				returns(type)
				val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "$it.pow(exp)" }
				statement("return %T($args)", type)
			}

			extensionFunction(mutableType, "powAssign") {
				parameter("exp", valueType)
				componentNames.forEach {
					statement("$it = $it.pow(exp)")
				}
			}

			extensionFunction(type, "pow") {
				parameter("exp", intVectorType!!)
				returns(type)
				val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "$it.pow(exp.$it)" }
				statement("return %T($args)", type)
			}

			extensionFunction(mutableType, "powAssign") {
				parameter("exp", intVectorType!!)
				componentNames.forEach {
					statement("$it = $it.pow(exp.$it)")
				}
			}

			extensionFunction(type, "pow") {
				parameter("exp", type)
				returns(type)
				val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "$it.pow(exp.$it)" }
				statement("return %T($args)", type)
			}

			extensionFunction(mutableType, "powAssign") {
				parameter("exp", type)
				componentNames.forEach {
					statement("$it = $it.pow(exp.$it)")
				}
			}

			// exp

			import("kotlin.math", "exp")

			function("exp") {
				parameter("exp", type)
				returns(type)
				val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "exp(exp.$it)" }
				statement("return %T($args)", type)
			}

			function("expInPlace") {
				parameter("exp", mutableType)
				componentNames.forEach {
					statement("exp.$it = exp(exp.$it)")
				}
			}

			// log

			import("kotlin.math", "ln")

			function("ln") {
				parameter("n", type)
				returns(type)
				val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "ln(n.$it)" }
				statement("return %T($args)", type)
			}

			function("lnInPlace") {
				parameter("n", mutableType)
				componentNames.forEach {
					statement("n.$it = ln(n.$it)")
				}
			}

			// exp2

			import("kotlin.math", "exp")

			function("exp2") {
				parameter("exp", type)
				returns(type)
				val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "exp(ln($two) * exp.$it)" }
				statement("return %T($args)", type)
			}

			function("exp2InPlace") {
				parameter("exp", mutableType)
				componentNames.forEach {
					statement("exp.$it = exp(ln($two) * exp.$it)")
				}
			}

			// log2

			import("kotlin.math", "log2")

			function("log2") {
				parameter("n", type)
				returns(type)
				val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "log2(n.$it)" }
				statement("return %T($args)", type)
			}

			function("log2InPlace") {
				parameter("n", mutableType)
				componentNames.forEach {
					statement("n.$it = log2(n.$it)")
				}
			}

			// log10 (kotlin stdlib)

			import("kotlin.math", "log10")

			function("log10") {
				parameter("n", type)
				returns(type)
				val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "log10(n.$it)" }
				statement("return %T($args)", type)
			}

			function("log10InPlace") {
				parameter("n", mutableType)
				componentNames.forEach {
					statement("n.$it = log10(n.$it)")
				}
			}

			// sqrt

			import("kotlin.math", "sqrt")

			function("sqrt") {
				parameter("n", type)
				returns(type)
				val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "sqrt(n.$it)" }
				statement("return %T($args)", type)
			}

			function("sqrtInPlace") {
				parameter("n", mutableType)
				componentNames.forEach {
					statement("n.$it = sqrt(n.$it)")
				}
			}

			// inversesqrt

			import("kotlin.math", "sqrt")

			function("inverseSqrt") {
				parameter("n", type)
				returns(type)
				val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "1 / sqrt(n.$it)" }
				statement("return %T($args)", type)
			}

			function("inverseSqrtInPlace") {
				parameter("n", mutableType)
				componentNames.forEach {
					statement("n.$it = 1 / sqrt(n.$it)")
				}
			}
		}.writeTo(commonOutDir)
	}
}

private fun geometricFunctions() {
	vectorTypes.filter {
		it.valueType == FLOAT || it.valueType == DOUBLE
	}.forEach { (type, valueType, length) ->
		valueType ?: error("base type is null for vector type")
		val componentNames = allComponentNames.take(length ?: 0)
		val mutableType = ClassName(packageName, "Mutable${type.simpleName}")

		val (zero) = literalOf("0", type, valueType)
		val (one) = literalOf("1", type, valueType)
		val (two) = literalOf("2", type, valueType)

		buildFile(packageName, "${type.simpleName}Geometric") {
			indent("\t")

			// length

			import("kotlin.math", "sqrt")

			extensionProperty(type, "length", valueType) {
				getter {
					statement("return sqrt(this dot this)")
				}
			}

			// distance

			import("kotlin.math", "abs")

			function("distance") {
				parameter("from", type)
				parameter("to", type)
				returns(valueType)
				statement("return (to - from).length")
			}

			// dot

			extensionFunction(type, "dot") {
				modifiers(INFIX)
				parameter("other", type)
				returns(valueType)
				val result = componentNames.joinToString(" + ") { "$it * other.$it" }
				statement("return \n\t($result)")
			}

			// cross

			if (length == 3) {
				extensionFunction(type, "cross") {
					modifiers(INFIX)
					parameter("other", type)
					returns(type)
					val args = (componentNames.drop(1) + componentNames.take(2)).zipWithNext()
						.joinToString(",\n\t", "\n\t", "\n") { (c1, c2) ->
							"$c1 * other.$c2 - other.$c1 * $c2"
						}
					statement("return %T($args)", type)
				}
			}

			// normalize

			extensionFunction(type, "normalized") {
				returns(type)
				statement("val length = length")
				val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "$it / length" }
				statement("return %T($args)", type)
			}

			extensionFunction(mutableType, "normalize") {
				statement("val length = length")
				componentNames.forEach {
					statement("$it /= length")
				}
			}

			// faceforward

			extensionFunction(type, "facedForward") {
				parameter("incident", type)
				parameter("normalRef", type)
				returns(type)
				controlFlow("return if (normalRef dot incident < 0)") {
					statement("this.copy()")
				}
				controlFlow("else") {
					statement("-this")
				}
			}

			extensionFunction(mutableType, "faceForward") {
				parameter("incident", type)
				parameter("normal", type)
				controlFlow("if (normal dot incident >= 0)") {
					componentNames.forEach {
						statement("$it = -$it")
					}
				}
			}

			// reflect

			extensionFunction(type, "reflected") {
				parameter("normal", type)
				returns(type)
				statement("return this - normal * (normal dot this) * $two")
			}

			extensionFunction(mutableType, "reflect") {
				parameter("normal", type)
				statement("val dot = normal dot this")
				componentNames.forEach {
					statement("$it -= normal.$it * dot * $two")
				}
			}

			// refract

			extensionFunction(type, "refracted") {
				parameter("normal", type)
				parameter("eta", valueType)
				returns(type)
				statement("val dot = normal dot this")
				statement("val k = $one - eta * eta * ($one - dot * dot)")
				controlFlow("return if (k >= 0)") {
					statement("eta * this - (eta * dot + sqrt(k)) * normal")
				}
				controlFlow("else") {
					statement("%T()", type)
				}
			}

			extensionFunction(mutableType, "refract") {
				parameter("normal", type)
				parameter("eta", valueType)
				statement("val dot = normal dot this")
				statement("val k = $one - eta * eta * ($one - dot * dot)")
				controlFlow("if (k >= 0)") {
					componentNames.forEach {
						statement("$it = eta * $it - (eta * dot + sqrt(k)) * normal.$it")
					}
				}
				controlFlow("else") {
					componentNames.forEach { statement("$it = $zero") }
				}
			}
		}.writeTo(commonOutDir)
	}
}

private fun vectorTypes() {
	vectorTypes.forEach { (type, valueType, length) ->
		val mutableVectorType = ClassName(packageName, "Mutable${type.simpleName}")
		val componentNames = allComponentNames.take(length!!)
		valueType ?: error("null base type invalid for vectors")

		val (zero) = literalOf("0", valueType)
		val (one) = literalOf("1", valueType)

		buildFile(packageName, type.simpleName) {
			indent("\t")

			import("kotlin.math", "sqrt")

			buildClass(type) {
				modifiers(SEALED)

				companionObject {
					property("ZERO", type) {
						initializer("%T($zero)", type)
					}
					property("ONE", type) {
						initializer("%T($one)", type)
					}
					componentNames.forEach { outer ->
						property(outer.toUpperCase(), type) {
							val args = componentNames.joinToString { inner ->
								if (outer == inner) one else zero
							}
							initializer("%T($args)", type)
						}
					}
				}

				componentNames.forEach {
					property(it, valueType, ABSTRACT)
				}

				function("get") {
					modifiers(OPERATOR)
					parameter("index", INT)
					returns(valueType)
					controlFlow("return when (index)") {
						componentNames.forEachIndexed { i, it ->
							statement("$i -> $it")
						}
						statement("else -> throw IndexOutOfBoundsException()")
					}
				}

				// copy

				function("copy") {
					returns(type)
					val args = componentNames.joinToString {
						parameter(it, valueType) {
							defaultValue("this.$it")
						}
						it
					}
					statement("return %T($args)", type)
				}

				// arithmetic functions

				if (valueType != BOOLEAN) {
					for ((name, op) in arithmetics) {
						function(name) {
							modifiers(OPERATOR)
							parameter("scalar", valueType)
							returns(type)
							val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "$it $op scalar" }
							statement("return %T($args)", type)
						}

						function(name) {
							modifiers(OPERATOR)
							parameter("other", type)
							returns(type)
							val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "$it $op other.$it" }
							statement("return %T($args)", type)
						}
					}

					// unary

					function("unaryPlus") {
						modifiers(OPERATOR)
						returns(type)
						statement("return copy()")
					}

					when (valueType) {
						INT, LONG, FLOAT, DOUBLE -> {
							function("unaryMinus") {
								modifiers(OPERATOR)
								returns(type)
								val args = componentNames.joinToString { "-$it" }
								statement("return copy($args)")
							}
						}
					}
				}
			}

			buildClass(mutableVectorType) {
				superclass(type)

				companionObject()

				primaryConstructor {
					componentNames.forEach { name ->
						parameter(name, valueType)
					}
					callSuperConstructor()
				}

				secondaryConstructor {
					parameter("scalar", valueType)
					callThisConstructor(*componentNames.map { "scalar" }.toTypedArray())
				}

				secondaryConstructor {
					callThisConstructor(*componentNames.map { zero }.toTypedArray())
				}

				componentNames.forEach { name ->
					mutableProperty(name, valueType, OVERRIDE) {
						initializer(name)
					}
				}

				function("set") {
					modifiers(OPERATOR)
					parameter("index", INT)
					parameter("value", valueType)
					controlFlow("when (index)") {
						componentNames.forEachIndexed { i, it ->
							statement("$i -> $it = value")
						}
						statement("else -> throw IndexOutOfBoundsException()")
					}
				}

				if (valueType != BOOLEAN) {
					for ((funName, op) in arithmetics) {
						function("${funName}Assign") {
							modifiers(OPERATOR)
							parameter("scalar", valueType)
							componentNames.forEach {
								statement("$it $op= scalar")
							}
						}

						function("${funName}Assign") {
							modifiers(OPERATOR)
							parameter("other", type)
							componentNames.forEach {
								statement("$it $op= other.$it")
							}
						}
					}
				}

				function("equals") {
					modifiers(OVERRIDE)
					parameter("other", ANY.copy(nullable = true))
					returns(BOOLEAN)
					statement("if (other === this) return true")
					statement("if (other == null) return false")
					statement("other as ${type.simpleName}")
					componentNames.forEach {
						statement("if ($it != other.$it) return false")
					}
					statement("return true")
				}

				function("hashCode") {
					modifiers(OVERRIDE)
					returns(INT)
					componentNames.forEachIndexed { i, it ->
						statement(
							if (i == 0) "var result = $it.hashCode()" else "result = 31 * result + $it.hashCode()"
						)
					}
					statement("return result")
				}

				function("toString") {
					modifiers(OVERRIDE)
					returns(STRING)
					statement("return %P", componentNames.joinToString(", ", "[", "]") { "$$it" })
				}
			}

			// Immutable Constructors

			function(type.simpleName) {
				componentNames.forEach { name ->
					parameter(name, valueType)
				}
				returns(type)
				statement("return %T(${componentNames.joinToString()})", mutableVectorType)
			}

			function(type.simpleName) {
				parameter("scalar", valueType) {
					defaultValue(zero)
				}
				returns(type)
				statement("return %T(scalar)", mutableVectorType)
			}

			// Destructuring Operators

			componentNames.forEachIndexed { i, name ->
				extensionFunction(type, "component${i + 1}") {
					modifiers(INLINE, OPERATOR)
					returns(valueType)
					statement("return $name")
				}
			}

			// primitive arithmetic

			if (valueType != BOOLEAN) {
				for ((name, op) in arithmetics) {
					extensionFunction(valueType, name) {
						modifiers(OPERATOR)
						parameter("vector", type)
						returns(type)
						val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "this $op vector.$it" }
						statement("return %T($args)", type)
					}
				}
			}

			// conversions

			if (valueType != BOOLEAN) {
				vectorTypes.filter {
					it.valueType != BOOLEAN && it.length == length
				}.forEach { other ->
					if (other.valueType != valueType) {
						extensionFunction(type, "to${other.type.simpleName}") {
							returns(other.type)
							val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "$it.to${other.valueType?.simpleName}()" }
							statement("return %T($args)", other.type)
						}
					}
					if (other.valueType == valueType) {
						extensionFunction(type, "to${other.type.simpleName}") {
							returns(other.type)
							val args = componentNames.joinToString()
							statement("return %T($args)", other.type)
						}
					}
				}
			}
		}.writeTo(commonOutDir)
	}
}

private fun vectorRelationalFunctions() {
	vectorTypes.forEach { (type, valueType, length) ->
		val componentNames = allComponentNames.take(length!!)
		valueType!!

		val boolVectorType = vectorTypes.find { it.valueType == BOOLEAN && it.length == length }!!

		buildFile(packageName, "${type.simpleName}Relational") {

			// lessThan

			extensionFunction(type, "lessThan") {
				modifiers(INFIX)
				parameter("other", type)
				returns(boolVectorType.type)
				val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "$it < other.$it" }
				statement("return %T($args)", boolVectorType.type)
			}

			// lessThanEqual

			extensionFunction(type, "lessThanEqual") {
				modifiers(INFIX)
				parameter("other", type)
				returns(boolVectorType.type)
				val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "$it <= other.$it" }
				statement("return %T($args)", boolVectorType.type)
			}

			// greaterThan

			extensionFunction(type, "greaterThan") {
				modifiers(INFIX)
				parameter("other", type)
				returns(boolVectorType.type)
				val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "$it > other.$it" }
				statement("return %T($args)", boolVectorType.type)
			}

			// greaterThanEqual

			extensionFunction(type, "greaterThanEqual") {
				modifiers(INFIX)
				parameter("other", type)
				returns(boolVectorType.type)
				val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "$it >= other.$it" }
				statement("return %T($args)", boolVectorType.type)
			}

			// equal

			extensionFunction(type, "equal") {
				modifiers(INFIX)
				parameter("other", type)
				returns(boolVectorType.type)
				val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "$it == other.$it" }
				statement("return %T($args)", boolVectorType.type)
			}

			// notEqual

			extensionFunction(type, "notEqual") {
				modifiers(INFIX)
				parameter("other", type)
				returns(boolVectorType.type)
				val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "$it != other.$it" }
				statement("return %T($args)", boolVectorType.type)
			}

			if (valueType == BOOLEAN) {
				// should the `any` and `all` functionality for collections from stdlib be generalized to all vector types?

				// any

				extensionFunction(type, "any") {
					returns(BOOLEAN)
					val result = componentNames.joinToString(" || ") { it }
					statement("return $result")
				}

				// all

				extensionFunction(type, "all") {
					returns(BOOLEAN)
					val result = componentNames.joinToString(" && ") { it }
					statement("return $result")
				}

				// not_

				extensionFunction(type, "not") {
					modifiers(OPERATOR)
					returns(type)
					val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "!$it" }
					statement("return %T($args)", type)
				}
			}
		}.writeTo(commonOutDir)
	}
}
