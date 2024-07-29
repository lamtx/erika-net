package erika.core.net.datacontract

import java.io.ByteArrayOutputStream

class ExposingBufferByteArrayOutputStream(size: Int) : ByteArrayOutputStream(size) {
    val buffer: ByteArray get() = buf
}