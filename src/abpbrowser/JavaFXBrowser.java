/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package abpbrowser;

/**
 *
 * @author ss
 */
import com.sun.glass.ui.Robot;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;

import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import testcode.KeyHook;

/**
 *
 * @web http://java-buddy.blogspot.com/
 */
public class JavaFXBrowser extends Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        KeyHook.blockWindowsKey();
        launch(args);
        KeyHook.unblockWindowsKey();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Student Examination");
        primaryStage.setAlwaysOnTop(true);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        primaryStage.toFront();

        Robot robot = com.sun.glass.ui.Application.GetApplication().createRobot();

        WebView myBrowser = new WebView();
        WebEngine myWebEngine = myBrowser.getEngine();
        myWebEngine.load("http://localhost:8085/ABP-Ver1/student/login/login.io");
        myWebEngine.setOnAlert(new EventHandler<WebEvent<String>>() {
            @Override
            public void handle(WebEvent<String> event) {
                Stage popup = new Stage();
                popup.setAlwaysOnTop(true);
                popup.initOwner(primaryStage);
                popup.initStyle(StageStyle.UTILITY);
                popup.initModality(Modality.WINDOW_MODAL);

                StackPane content = new StackPane();
                content.getChildren().setAll(
                        new Label(event.getData())
                );
                content.setPrefSize(200, 100);

                popup.setScene(new Scene(content));
                popup.showAndWait();
            }
        });

        StackPane root = new StackPane();
        root.getChildren().add(myBrowser);
        Scene scene = new Scene(root, 1280, 800);

        scene.setOnKeyPressed(event -> {

            robot.keyRelease(KeyCode.A.impl_getCode());
            robot.keyRelease(KeyCode.ALT_GRAPH.impl_getCode());
            robot.keyRelease(KeyCode.CONTROL.impl_getCode());
            robot.keyRelease(KeyCode.DELETE.impl_getCode());
        });

        scene.setOnKeyReleased(event -> {

            robot.keyRelease(KeyCode.ALT_GRAPH.impl_getCode());
            robot.keyRelease(KeyCode.CONTROL.impl_getCode());
            robot.keyRelease(KeyCode.DELETE.impl_getCode());

        });

        primaryStage.setScene(scene);

        primaryStage.show();
    }

}
