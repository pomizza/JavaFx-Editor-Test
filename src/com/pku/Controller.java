package com.pku;
import com.pku.concurrency.RunProgram;
import com.pku.model.FileItem;
import com.pku.utils.ReadFromFile;
import com.sun.javafx.scene.control.skin.TreeViewSkin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.web.HTMLEditor;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Stack;

public class Controller  implements Initializable {

    //图标图片，空文件夹
    private final Image folderImg =
            new Image(getClass().getResourceAsStream("folder.png"),24,24,true,true);
    //图标图片，有内容的文件夹
    private final Image emptyFolderImg =
            new Image(getClass().getResourceAsStream("emptyFolder.png"),24,24,true,true);
    //图标图片，文件
    private final Image fileImg =
            new Image(getClass().getResourceAsStream("file.jpg"),24,24,true,true);

    @FXML
    private TextArea preview;

    @FXML
    private TabPane tabPane;

    @FXML
    private TreeView<FileItem> tv_dir_1;

    @FXML
    private Tab previewTab;

    @FXML
    private HTMLEditor previewHTML;

    //根节点
    TreeItem<FileItem> rootNode;

    //用作递归中的临时变量
    TreeItem<FileItem> currentNode;

    //当前展示在预览中的文件
    File currentPreview;

    //递归解析文件夹树的栈，进入文件夹时入栈，退出文件夹时出栈
    Stack stack = new Stack();

    /**
     *  打开一个文件夹作为新项目
     */
    @FXML public void newProject(MouseEvent event) {
        File file = Main.openDir(Main.primaryStage);
        rootNode = new TreeItem<>(new FileItem(file));
        rootNode.setExpanded(true);
        currentNode = rootNode;
        stack.push(rootNode);
        getDirectory(file);
        tv_dir_1.setRoot(rootNode);
    }

    @FXML public void runProgram(){
        Thread thread = null;
        thread = new Thread(new RunProgram(Main.openDir));
        thread.start();
    }

    /**
     *  打开当前选中项目的文件夹
     */
    @FXML public void viewInBrowser(){
        try {
            FileItem fileItem = tv_dir_1.getSelectionModel().getSelectedItems().get(0).getValue();
            java.awt.Desktop.getDesktop().open(fileItem.getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  程序启动时调用的初始化方法
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //为展现文件夹树做准备，先初始化根节点
        rootNode = new TreeItem<>(new FileItem(new File(Main.openDir)));
        rootNode.setExpanded(true);
        currentNode = rootNode;
        stack.push(rootNode);
        //通过递归调用求出文件夹的所有元素的树信息
        getDirectory(new File(Main.openDir));
        tv_dir_1.setRoot(rootNode);
        tv_dir_1.addEventHandler(MouseEvent.MOUSE_PRESSED,new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                //监听树形控件的鼠标事件与键盘事件
                TreeView<FileItem> treeView= (TreeView)me.getSource();
                FileItem fileItem = treeView.getSelectionModel().getSelectedItems().get(0).getValue();

                if(currentPreview==null||!currentPreview.equals(fileItem.getFile())){
                    File select = fileItem.getFile();
                    if(!select.isDirectory()){
                        currentPreview = fileItem.getFile();
                        previewTab.setText("预览-"+currentPreview.getName());
                        if(currentPreview.length()<100000){
                            String content = ReadFromFile.readToString(currentPreview.getPath());
                            preview.setText(content);
                            previewHTML.setHtmlText(content);
                        }else{
                            preview.setText("文本过长");
                            previewHTML.setHtmlText("文本过长");
                        }
                    }
                }
            }
        });
    }

    /**
     *  通过递归调用求出文件夹的所有元素的树信息
     */
    private void getDirectory(File file) {
        File flist[] = file.listFiles();
        if (flist == null || flist.length == 0) {
            return;
        }
        flist = sort(flist);
        for (File f : flist) {
            if (f.isDirectory()) {
                //这里将列出所有的文件夹
                TreeItem<FileItem> dir;

                //判断文件夹是否是空的
                File files[] = f.listFiles();
                if (files == null || files.length == 0) {
                    dir = new TreeItem<>(new FileItem(f), new ImageView(emptyFolderImg));
                }else{
                    dir = new TreeItem<>(new FileItem(f), new ImageView(folderImg));
                }
                //增加树的节点
                currentNode.getChildren().add(dir);
                //入栈
                stack.push(currentNode);
                currentNode = dir;
                //递归调用
                getDirectory(f);
                //出栈
                currentNode = (TreeItem<FileItem>) stack.pop();
            } else {
                //这里将列出所有的文件
                TreeItem<FileItem> fileItem = new TreeItem<>(new FileItem(f),new ImageView(fileImg));
//                fileItem.setValue(f.getName());
                currentNode.getChildren().add(fileItem);
            }
        }
    }

    /**
     * 将文件数组排序，目录放在上面，文件在下面
     * @param file
     * @return
     */
    private static File[] sort(File[] file)
    {
        ArrayList<File> list = new ArrayList<File>();
        //放入所有目录
        for (File f : file)
        {
            if (f.isDirectory())
            {
                list.add(f);
            }
        }
        //放入所有文件
        for (File f : file)
        {
            if (f.isFile())
            {
                list.add(f);
            }
        }

        return list.toArray(new File[file.length]);
    }
}
