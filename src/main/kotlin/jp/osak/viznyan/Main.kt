package jp.osak.viznyan

import javafx.application.Application
import javafx.stage.Stage
import jp.osak.viznyan.scene.VisualizerSceneManager

class Main : Application() {
    private val visualizerSceneManager = VisualizerSceneManager()

    override fun start(primaryStage: Stage) {
        primaryStage.scene = visualizerSceneManager.scene
        primaryStage.show()
        visualizerSceneManager.repaint()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(Main::class.java, *args)
        }
    }
}