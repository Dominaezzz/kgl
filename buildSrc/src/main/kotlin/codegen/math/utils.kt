package codegen.math

import com.squareup.kotlinpoet.*

@DslMarker
annotation class KotlinPoetDsl

@KotlinPoetDsl
inline fun buildFile(
	packageName: String,
	fileName: String,
	block: FileSpec.Builder.() -> Unit = {}
): FileSpec {
	return FileSpec.builder(packageName, fileName)
		.apply(block)
		.build()
}

@KotlinPoetDsl
inline fun FileSpec.Builder.annotation(type: ClassName, block: AnnotationSpec.Builder.() -> Unit): AnnotationSpec {
	return AnnotationSpec.builder(type)
		.apply(block)
		.build()
		.also { addAnnotation(it) }
}

@KotlinPoetDsl
fun AnnotationSpec.Builder.member(
	format: String,
	vararg args: Any
) {
	addMember(format, *args)
}

@KotlinPoetDsl
fun FileSpec.Builder.function(name: String, block: FunSpec.Builder.() -> Unit): FunSpec {
	return FunSpec.builder(name)
		.apply(block)
		.build()
		.also { addFunction(it) }
}

@KotlinPoetDsl
fun FileSpec.Builder.import(packageName: String, name: String) {
	addImport(packageName, name)
}

@KotlinPoetDsl
inline fun FileSpec.Builder.buildClass(
	name: String,
	block: TypeSpec.Builder.() -> Unit = {}
): TypeSpec {
	return TypeSpec.classBuilder(name)
		.apply(block)
		.build()
		.also { addType(it) }
}

@KotlinPoetDsl
inline fun FileSpec.Builder.buildClass(
	className: ClassName,
	block: TypeSpec.Builder.() -> Unit = {}
): TypeSpec {
	return TypeSpec.classBuilder(className)
		.apply(block)
		.build()
		.also { addType(it) }
}

@KotlinPoetDsl
fun TypeSpec.Builder.modifiers(vararg modifiers: KModifier) {
	addModifiers(*modifiers)
}

@KotlinPoetDsl
inline fun TypeSpec.Builder.primaryConstructor(
	block: FunSpec.Builder.() -> Unit
): FunSpec {
	return FunSpec.constructorBuilder()
		.apply(block)
		.build()
		.also { primaryConstructor(it) }
}

@KotlinPoetDsl
inline fun TypeSpec.Builder.secondaryConstructor(
	block: FunSpec.Builder.() -> Unit
): FunSpec {
	return FunSpec.constructorBuilder()
		.apply(block)
		.build()
		.also { addFunction(it) }
}

@KotlinPoetDsl
inline fun TypeSpec.Builder.property(
	name: String,
	type: TypeName,
	vararg modifiers: KModifier,
	block: PropertySpec.Builder.() -> Unit = {}
): PropertySpec {
	return PropertySpec.builder(name, type, *modifiers)
		.apply(block)
		.build()
		.also { addProperty(it) }
}

@KotlinPoetDsl
fun PropertySpec.Builder.modifiers(vararg modifiers: KModifier) {
	addModifiers(*modifiers)
}

@KotlinPoetDsl
inline fun PropertySpec.Builder.getter(
	block: FunSpec.Builder.() -> Unit
): FunSpec {
	return FunSpec.getterBuilder()
		.apply(block)
		.build()
		.also { getter(it) }
}

@KotlinPoetDsl
inline fun TypeSpec.Builder.mutableProperty(
	name: String,
	type: TypeName,
	vararg modifiers: KModifier,
	block: PropertySpec.Builder.() -> Unit = {}
): PropertySpec {
	return PropertySpec.builder(name, type, *modifiers)
		.mutable(true)
		.apply(block)
		.build()
		.also { addProperty(it) }
}

@KotlinPoetDsl
inline fun PropertySpec.Builder.setter(
	block: FunSpec.Builder.() -> Unit
): FunSpec {
	return FunSpec.setterBuilder()
		.apply(block)
		.build()
		.also { setter(it) }
}

@KotlinPoetDsl
inline fun TypeSpec.Builder.function(name: String, block: FunSpec.Builder.() -> Unit): FunSpec {
	return FunSpec.builder(name)
		.apply(block)
		.build()
		.also { addFunction(it) }
}

@KotlinPoetDsl
fun FunSpec.Builder.modifiers(vararg modifiers: KModifier) {
	addModifiers(*modifiers)
}

@KotlinPoetDsl
inline fun FunSpec.Builder.parameter(
	name: String,
	type: TypeName,
	vararg modifiers: KModifier,
	block: ParameterSpec.Builder.() -> Unit = {}
): ParameterSpec {
	return ParameterSpec.builder(name, type, *modifiers)
		.apply(block)
		.build()
		.also { addParameter(it) }
}

@KotlinPoetDsl
inline fun FunSpec.Builder.controlFlow(controlFlow: String, block: FunSpec.Builder.() -> Unit): FunSpec {
	return beginControlFlow(controlFlow)
		.apply(block)
		.endControlFlow()
		.build()
}

@KotlinPoetDsl
fun FunSpec.Builder.statement(format: String, vararg args: Any) {
	addStatement(format, *args)
}
