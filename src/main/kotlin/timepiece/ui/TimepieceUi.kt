package timepiece.ui

import javafx.application.Platform
import javafx.embed.swing.SwingFXUtils
import javafx.scene.Scene
import javafx.stage.Stage
import timepiece.ui.views.Root
import toBufferedImage
import tornadofx.App
import tornadofx.FX
import tornadofx.UIComponent
import tornadofx.addStageIcon
import java.awt.Image
import java.awt.SystemTray
import javax.imageio.ImageIO


class TimepieceUi : App(Root::class) {

    override fun start(stage: Stage) {
        super.start(stage)

        val img = ImageIO.read(Thread.currentThread().contextClassLoader.getResourceAsStream("tpiece.png"))
        val width = SystemTray.getSystemTray().trayIconSize.width
        val scaled = img.getScaledInstance(width, -1, Image.SCALE_SMOOTH)

        addStageIcon(SwingFXUtils.toFXImage(toBufferedImage(scaled), null))

        trayicon(
            toBufferedImage(scaled),
            "Timepiece timetracker"
        ) {
            setOnMouseClicked(fxThread = true) {
                FX.primaryStage.show()
                FX.primaryStage.toFront()
            }

            menu("MyApp") {
                item("Show...") {
                    setOnAction(fxThread = true) {
                        FX.primaryStage.show()
                        FX.primaryStage.toFront()
                    }
                }
                item("Exit") {
                    setOnAction(fxThread = true) {
                        Platform.exit()
                    }
                }
            }
        }
    }

    override fun createPrimaryScene(view: UIComponent): Scene {
        return Scene(view.root, 1200.0, 600.0)
    }
}
