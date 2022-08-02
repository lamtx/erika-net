package erika.core.net

object HttpHeaders {

    /**
     * The HTTP `Accept` header field name.

     * @see [Section 5.3.2 of RFC 7231](http://tools.ietf.org/html/rfc7231.section-5.3.2)
     */
    const val ACCEPT = "Accept"
    /**
     * The HTTP `Accept-Charset` header field name.

     * @see [Section 5.3.3 of RFC 7231](http://tools.ietf.org/html/rfc7231.section-5.3.3)
     */
    const val ACCEPT_CHARSET = "Accept-Charset"
    /**
     * The HTTP `Accept-Encoding` header field name.

     * @see [Section 5.3.4 of RFC 7231](http://tools.ietf.org/html/rfc7231.section-5.3.4)
     */
    const val ACCEPT_ENCODING = "Accept-Encoding"
    /**
     * The HTTP `Accept-Language` header field name.

     * @see [Section 5.3.5 of RFC 7231](http://tools.ietf.org/html/rfc7231.section-5.3.5)
     */
    const val ACCEPT_LANGUAGE = "Accept-Language"
    /**
     * The HTTP `Accept-Ranges` header field name.

     * @see [Section 5.3.5 of RFC 7233](http://tools.ietf.org/html/rfc7233.section-2.3)
     */
    const val ACCEPT_RANGES = "Accept-Ranges"
    /**
     * The HTTP `Age` header field name.

     * @see [Section 5.1 of RFC 7234](http://tools.ietf.org/html/rfc7234.section-5.1)
     */
    const val AGE = "Age"
    /**
     * The HTTP `Allow` header field name.

     * @see [Section 7.4.1 of RFC 7231](http://tools.ietf.org/html/rfc7231.section-7.4.1)
     */
    const val ALLOW = "Allow"
    /**
     * The HTTP `Authorization` header field name.

     * @see [Section 4.2 of RFC 7235](http://tools.ietf.org/html/rfc7235.section-4.2)
     */
    const val AUTHORIZATION = "Authorization"
    /**
     * The HTTP `Cache-Control` header field name.

     * @see [Section 5.2 of RFC 7234](http://tools.ietf.org/html/rfc7234.section-5.2)
     */
    const val CACHE_CONTROL = "Cache-Control"
    /**
     * The HTTP `Connection` header field name.

     * @see [Section 6.1 of RFC 7230](http://tools.ietf.org/html/rfc7230.section-6.1)
     */
    const val CONNECTION = "Connection"
    /**
     * The HTTP `Content-Encoding` header field name.

     * @see [Section 3.1.2.2 of RFC 7231](http://tools.ietf.org/html/rfc7231.section-3.1.2.2)
     */
    const val CONTENT_ENCODING = "Content-Encoding"
    /**
     * The HTTP `Content-Disposition` header field name

     * @see [RFC 6266](http://tools.ietf.org/html/rfc6266)
     */
    const val CONTENT_DISPOSITION = "Content-Disposition"
    /**
     * The HTTP `Content-Language` header field name.

     * @see [Section 3.1.3.2 of RFC 7231](http://tools.ietf.org/html/rfc7231.section-3.1.3.2)
     */
    const val CONTENT_LANGUAGE = "Content-Language"
    /**
     * The HTTP `Content-Length` header field name.

     * @see [Section 3.3.2 of RFC 7230](http://tools.ietf.org/html/rfc7230.section-3.3.2)
     */
    const val CONTENT_LENGTH = "Content-Length"
    /**
     * The HTTP `Content-Location` header field name.

     * @see [Section 3.1.4.2 of RFC 7231](http://tools.ietf.org/html/rfc7231.section-3.1.4.2)
     */
    const val CONTENT_LOCATION = "Content-Location"
    /**
     * The HTTP `Content-Range` header field name.

     * @see [Section 4.2 of RFC 7233](http://tools.ietf.org/html/rfc7233.section-4.2)
     */
    const val CONTENT_RANGE = "content-range"
    /**
     * The HTTP `Content-Type` header field name.

     * @see [Section 3.1.1.5 of RFC 7231](http://tools.ietf.org/html/rfc7231.section-3.1.1.5)
     */
    const val ContentType = "content-type"
    /**
     * The HTTP `Cookie` header field name.

     * @see [Section 4.3.4 of RFC 2109](http://tools.ietf.org/html/rfc2109.section-4.3.4)
     */
    const val COOKIE = "Cookie"
    /**
     * The HTTP `Date` header field name.

     * @see [Section 7.1.1.2 of RFC 7231](http://tools.ietf.org/html/rfc7231.section-7.1.1.2)
     */
    const val DATE = "Date"
    /**
     * The HTTP `ETag` header field name.

     * @see [Section 2.3 of RFC 7232](http://tools.ietf.org/html/rfc7232.section-2.3)
     */
    const val ETAG = "ETag"
    /**
     * The HTTP `Expect` header field name.

     * @see [Section 5.1.1 of RFC 7231](http://tools.ietf.org/html/rfc7231.section-5.1.1)
     */
    const val EXPECT = "Expect"
    /**
     * The HTTP `Expires` header field name.

     * @see [Section 5.3 of RFC 7234](http://tools.ietf.org/html/rfc7234.section-5.3)
     */
    const val EXPIRES = "Expires"
    /**
     * The HTTP `From` header field name.

     * @see [Section 5.5.1 of RFC 7231](http://tools.ietf.org/html/rfc7231.section-5.5.1)
     */
    const val FROM = "From"
    /**
     * The HTTP `Host` header field name.

     * @see [Section 5.4 of RFC 7230](http://tools.ietf.org/html/rfc7230.section-5.4)
     */
    const val HOST = "Host"
    /**
     * The HTTP `If-Match` header field name.

     * @see [Section 3.1 of RFC 7232](http://tools.ietf.org/html/rfc7232.section-3.1)
     */
    const val IF_MATCH = "If-Match"
    /**
     * The HTTP `If-Modified-Since` header field name.

     * @see [Section 3.3 of RFC 7232](http://tools.ietf.org/html/rfc7232.section-3.3)
     */
    const val IF_MODIFIED_SINCE = "If-Modified-Since"
    /**
     * The HTTP `If-None-Match` header field name.

     * @see [Section 3.2 of RFC 7232](http://tools.ietf.org/html/rfc7232.section-3.2)
     */
    const val IF_NONE_MATCH = "If-None-Match"
    /**
     * The HTTP `If-Range` header field name.

     * @see [Section 3.2 of RFC 7233](http://tools.ietf.org/html/rfc7233.section-3.2)
     */
    const val IF_RANGE = "If-Range"
    /**
     * The HTTP `If-Unmodified-Since` header field name.

     * @see [Section 3.4 of RFC 7232](http://tools.ietf.org/html/rfc7232.section-3.4)
     */
    const val IF_UNMODIFIED_SINCE = "If-Unmodified-Since"
    /**
     * The HTTP `Last-Modified` header field name.

     * @see [Section 2.2 of RFC 7232](http://tools.ietf.org/html/rfc7232.section-2.2)
     */
    const val LAST_MODIFIED = "Last-Modified"
    /**
     * The HTTP `Link` header field name.

     * @see [RFC 5988](http://tools.ietf.org/html/rfc5988)
     */
    const val LINK = "Link"
    /**
     * The HTTP `Location` header field name.

     * @see [Section 7.1.2 of RFC 7231](http://tools.ietf.org/html/rfc7231.section-7.1.2)
     */
    const val Location = "location"
    /**
     * The HTTP `Max-Forwards` header field name.

     * @see [Section 5.1.2 of RFC 7231](http://tools.ietf.org/html/rfc7231.section-5.1.2)
     */
    const val MAX_FORWARDS = "Max-Forwards"
    /**
     * The HTTP `Origin` header field name.

     * @see [RFC 6454](http://tools.ietf.org/html/rfc6454)
     */
    const val ORIGIN = "Origin"
    /**
     * The HTTP `Pragma` header field name.

     * @see [Section 5.4 of RFC 7234](http://tools.ietf.org/html/rfc7234.section-5.4)
     */
    const val PRAGMA = "Pragma"
    /**
     * The HTTP `Proxy-Authenticate` header field name.

     * @see [Section 4.3 of RFC 7235](http://tools.ietf.org/html/rfc7235.section-4.3)
     */
    const val PROXY_AUTHENTICATE = "Proxy-Authenticate"
    /**
     * The HTTP `Proxy-Authorization` header field name.

     * @see [Section 4.4 of RFC 7235](http://tools.ietf.org/html/rfc7235.section-4.4)
     */
    const val PROXY_AUTHORIZATION = "Proxy-Authorization"
    /**
     * The HTTP `Range` header field name.

     * @see [Section 3.1 of RFC 7233](http://tools.ietf.org/html/rfc7233.section-3.1)
     */
    const val Range = "range"
    /**
     * The HTTP `Referer` header field name.

     * @see [Section 5.5.2 of RFC 7231](http://tools.ietf.org/html/rfc7231.section-5.5.2)
     */
    const val REFERER = "Referer"
    /**
     * The HTTP `Retry-After` header field name.

     * @see [Section 7.1.3 of RFC 7231](http://tools.ietf.org/html/rfc7231.section-7.1.3)
     */
    const val RETRY_AFTER = "Retry-After"
    /**
     * The HTTP `Server` header field name.

     * @see [Section 7.4.2 of RFC 7231](http://tools.ietf.org/html/rfc7231.section-7.4.2)
     */
    const val SERVER = "Server"
    /**
     * The HTTP `Set-Cookie` header field name.

     * @see [Section 4.2.2 of RFC 2109](http://tools.ietf.org/html/rfc2109.section-4.2.2)
     */
    const val SET_COOKIE = "Set-Cookie"
    /**
     * The HTTP `Set-Cookie2` header field name.

     * @see [RFC 2965](http://tools.ietf.org/html/rfc2965)
     */
    const val SET_COOKIE2 = "Set-Cookie2"
    /**
     * The HTTP `TE` header field name.

     * @see [Section 4.3 of RFC 7230](http://tools.ietf.org/html/rfc7230.section-4.3)
     */
    const val TE = "TE"
    /**
     * The HTTP `Trailer` header field name.

     * @see [Section 4.4 of RFC 7230](http://tools.ietf.org/html/rfc7230.section-4.4)
     */
    const val TRAILER = "Trailer"
    /**
     * The HTTP `Transfer-Encoding` header field name.

     * @see [Section 3.3.1 of RFC 7230](http://tools.ietf.org/html/rfc7230.section-3.3.1)
     */
    const val TRANSFER_ENCODING = "Transfer-Encoding"
    /**
     * The HTTP `Upgrade` header field name.

     * @see [Section 6.7 of RFC 7230](http://tools.ietf.org/html/rfc7230.section-6.7)
     */
    const val UPGRADE = "Upgrade"
    /**
     * The HTTP `User-Agent` header field name.

     * @see [Section 5.5.3 of RFC 7231](http://tools.ietf.org/html/rfc7231.section-5.5.3)
     */
    const val USER_AGENT = "User-Agent"
    /**
     * The HTTP `Vary` header field name.

     * @see [Section 7.1.4 of RFC 7231](http://tools.ietf.org/html/rfc7231.section-7.1.4)
     */
    const val VARY = "Vary"
    /**
     * The HTTP `Via` header field name.

     * @see [Section 5.7.1 of RFC 7230](http://tools.ietf.org/html/rfc7230.section-5.7.1)
     */
    const val VIA = "Via"
    /**
     * The HTTP `Warning` header field name.

     * @see [Section 5.5 of RFC 7234](http://tools.ietf.org/html/rfc7234.section-5.5)
     */
    const val WARNING = "Warning"
    /**
     * The HTTP `WWW-Authenticate` header field name.

     * @see [Section 4.1 of RFC 7235](http://tools.ietf.org/html/rfc7235.section-4.1)
     */
    const val WWW_AUTHENTICATE = "WWW-Authenticate"

}
