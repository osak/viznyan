package jp.osak.viznyan.streaming

import jp.osak.viznyan.loader.CommandLoader
import jp.osak.viznyan.loader.CompProgStateLoader
import jp.osak.viznyan.loader.Tokenizer
import jp.osak.viznyan.rendering.State
import jp.osak.viznyan.rendering.command.Frame
import java.net.ServerSocket
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class SocketStreamer(port: Int, consumer: (Frame) -> Unit) {
    private val serverSocket: ServerSocket
    private val executor: Executor

    init {
        serverSocket = ServerSocket(port)
        executor = Executors.newSingleThreadExecutor()
        executor.execute {
            val clientSocket = serverSocket.accept()
            val inputStream = clientSocket.getInputStream()
            val loader = CommandLoader()
            val tokenizer = Tokenizer(inputStream.bufferedReader())

            while (true) {
                val frame = loader.readFrame(tokenizer) ?: break
                consumer(frame)
            }

            clientSocket.close()
        }
    }
}