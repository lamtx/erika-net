package erika.core.net

/**
 * Called when copying 2 streams in the loop.
 * current: Total transferred bytes.
 * total: Size of the stream in bytes. -1 indicates that the size of stream is not available.
 * finished: Called once when the loop exits, `true` value indicates that the operator complete successfully.
 */
typealias CopyStreamListener = (current: Long, total: Long, finished: Boolean) -> Unit