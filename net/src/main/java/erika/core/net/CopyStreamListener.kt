package erika.core.net

/**
 * should break or not
 * @return true if should stop operator
 */

typealias CopyStreamListener = (current: Long, total: Long, finished: Boolean) -> Boolean