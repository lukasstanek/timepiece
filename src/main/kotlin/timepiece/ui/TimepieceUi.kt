package timepiece.ui

import javafx.scene.Scene
import timepiece.ui.views.Root
import tornadofx.App
import tornadofx.UIComponent


class TimepieceUi: App(Root::class){
    override fun createPrimaryScene(view: UIComponent): Scene {
        return Scene(view.root, 1200.0, 600.0)
    }
}