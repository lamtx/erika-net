package erika.core.net

enum class HttpMethod {
    Get, Post, Put, Patch, Head, Delete;

    companion object {
        @Deprecated("Use `Get`", replaceWith = ReplaceWith("HttpMethod.Get"))
        inline val GET: HttpMethod
            get() = Get

        @Deprecated("Use `Post`", replaceWith = ReplaceWith("HttpMethod.Post"))
        inline val POST: HttpMethod
            get() = Post

        @Deprecated("Use `Put`", replaceWith = ReplaceWith("HttpMethod.Put"))
        inline val PUT: HttpMethod
            get() = Put

        @Deprecated("Use `Patch`", replaceWith = ReplaceWith("HttpMethod.Patch"))
        inline val PATCH: HttpMethod
            get() = Patch

        @Deprecated("Use `Head`", replaceWith = ReplaceWith("HttpMethod.Head"))
        inline val HEAD: HttpMethod
            get() = Head

        @Deprecated("Use `Delete`", replaceWith = ReplaceWith("HttpMethod.Delete"))
        inline val DELETE: HttpMethod
            get() = Delete
    }
}