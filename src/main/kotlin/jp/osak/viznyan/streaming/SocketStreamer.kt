package jp.osak.viznyan.streaming

import jp.osak.viznyan.loader.CompProgStateLoader
import jp.osak.viznyan.loader.Tokenizer
import jp.osak.viznyan.rendering.State
import java.net.ServerSocket
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class SocketStreamer(port: Int, consumer: (State) -> Unit) {
    private val serverSocket: ServerSocket
    private val executor: Executor

    init {
        serverSocket = ServerSocket(port)
        executor = Executors.newSingleThreadExecutor()
        executor.execute {
            val clientSocket = serverSocket.accept()
            val inputStream = clientSocket.getInputStream()
            val loader = CompProgStateLoader()
            val tokenizer = Tokenizer(inputStream.bufferedReader())

            while (true) {
                val state = loader.loadOne(tokenizer) ?: break
                consumer(state)
            }

            clientSocket.close()
        }
    }
}