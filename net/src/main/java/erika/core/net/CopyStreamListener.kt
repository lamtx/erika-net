package erika.core.net

/**
 * Called when copying 2 streams in the loop.
 * @param[current] Total transferred bytes.
 * @param[total] Size of the stream in bytes. -1 indicates that the size of stream is not available.
 * @param[finished] Called once when the loop exits, `true` value indicates that the operator complete successfully.
 */
typealias CopyStreamListener = (current: Long, total: Long, finished: Boolean) -> Unit