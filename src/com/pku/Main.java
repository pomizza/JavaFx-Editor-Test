package com.pku;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

import static javafx.application.Platform.exit;

public class Main extends Application {
    public static String openDir = "";
    static final DirectoryChooser dirChooser = new DirectoryChooser();

    //选择文件夹，返回File对象
    public static File openDir(Stage stage){
        dirChooser.setTitle("打开文件夹作为新项目");
        dirChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        File file = dirChooser.showDialog(stage);
        return file;
    }
    public static Stage primaryStage;
    @Override
    public void start(Stage primaryStage) throws Exception{
        File projectDir = openDir(primaryStage);
        this.primaryStage = primaryStage;
        if(projectDir == null){
            exit();
        }else{
            openDir = projectDir.getPath();
            Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
            //primaryStage.setResizable(false);
            primaryStage.setTitle("极简Java文本编译器");
            primaryStage.setScene(new Scene(root, 1280, 768));
            primaryStage.show();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
