/**
 * Copyright [2019] [Dominic Fischer]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package codegen.vulkan

import codegen.*
import com.squareup.kotlinpoet.*


inline fun <TKey, TValue> buildMap(block: MutableMap<TKey, TValue>.() -> Unit): Map<TKey, TValue> {
	val map = mutableMapOf<TKey, TValue>()
	map.apply(block)
	return map
}

data class MultiPlatform<T>(val common: T, val jvm: T, val native: T)
enum class Platform { COMMON, JVM, NATIVE }

fun buildTypeSpec(
	init: () -> TypeSpec.Builder,
	block: TypeSpec.Builder.(platform: Platform) -> Unit
): MultiPlatform<TypeSpec> {
	return MultiPlatform(
		init().addModifiers(KModifier.EXPECT).apply { block(Platform.COMMON) }.build(),
		init().addModifiers(KModifier.ACTUAL).apply { block(Platform.JVM) }.build(),
		init().addModifiers(KModifier.ACTUAL).apply { block(Platform.NATIVE) }.build()
	)
}

fun buildFunSpec(
	init: () -> FunSpec.Builder,
	auto: Boolean = true,
	block: FunSpec.Builder.(platform: Platform) -> Unit
): MultiPlatform<FunSpec> {
	val jvm = init().addModifiers(KModifier.ACTUAL).apply { block(Platform.JVM) }.build()
	val native = init().addModifiers(KModifier.ACTUAL).apply { block(Platform.NATIVE) }.build()

	val commonBuilder = init().addModifiers(KModifier.EXPECT)
	if (auto) {
		commonBuilder.addParameters(native.parameters)
		native.receiverType?.also(commonBuilder::receiver)
		native.returnType?.also(commonBuilder::returns)
		commonBuilder.addModifiers(native.modifiers - KModifier.ACTUAL)
	} else {
		commonBuilder.block(Platform.COMMON)
	}
	return MultiPlatform(commonBuilder.build(), jvm, native)
}

fun buildPropertySpec(
	init: () -> PropertySpec.Builder,
	block: PropertySpec.Builder.(platform: Platform) -> Unit
): MultiPlatform<PropertySpec> {
	val jvm = init().addModifiers(KModifier.ACTUAL).apply { block(Platform.JVM) }.build()
	val native = init().addModifiers(KModifier.ACTUAL).apply { block(Platform.NATIVE) }.build()

	val commonBuilder = init().addModifiers(KModifier.EXPECT)
	commonBuilder.mutable(native.mutable)

	return MultiPlatform(commonBuilder.build(), jvm, native)
}

fun buildPlatformFunction(
	init: () -> FunSpec.Builder,
	block: FunSpec.Builder.(platform: Platform) -> Unit
): MultiPlatform<FunSpec?> {
	return MultiPlatform(
		null,
		init().apply { block(Platform.JVM) }.build(),
		init().apply { block(Platform.NATIVE) }.build()
	)
}

inline fun <TIn, TOut> MultiPlatform<TIn>.map(block: (TIn) -> TOut): MultiPlatform<TOut> {
	return MultiPlatform(block(common), block(jvm), block(native))
}

inline fun <TIn> MultiPlatform<TIn>.forEach(block: TIn.() -> Unit) {
	common.block()
	jvm.block()
	native.block()
}

inline fun <TIn, TOut> MultiPlatform<TIn>.doForEach(right: MultiPlatform<TOut>, block: TIn.(TOut) -> Unit) {
	common.block(right.common)
	jvm.block(right.jvm)
	native.block(right.native)
}

val CTypeDecl.isWritable: Boolean get() = !isConst && asteriskCount > 0

val camelCase = Regex("([a-z])([A-Z]+)")

private val abbreviations = setOf(
	"gcn",
	"glsl",
	"gpu",
	"pvrtc"
)

fun String.pascalToSnakeCase(): String {
	return replace(camelCase, "\$1_\$2")
}

fun String.snakeToPascalCase(): String {
	return splitToSequence("_")
		.map {
			val lower = it.toLowerCase()
			if (lower in abbreviations) {
				it.toUpperCase()
			} else {
				lower.capitalize()
			}
		}
		.joinToString("")
}
