package opengl

data class CTypeDecl(val name: String, val isConst: Boolean, val asteriskCount: Int, val count: String) {
    override fun toString(): String {
        return buildString {
            if (isConst) append("const ")
            append(name)
            repeat(asteriskCount) {
                append('*')
            }
            if (count.isNotBlank()) {
                append('[')
                append(count)
                append(']')
            }
        }
    }
}