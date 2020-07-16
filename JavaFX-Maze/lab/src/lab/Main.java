package lab;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{

	Maze maze = new Maze();
	    
	@Override
    public void start(Stage primaryStage) throws Exception{
		
        //读取地图
	   maze.readMap();
	   
        //画地图
	   maze.drawPicture();
	   
        //接受键盘输入控制老鼠移动
	   maze.setOnKeyPressed( e -> maze.move() );    

	   Scene scene = new Scene(maze, maze.colnum * maze.width, maze.rownum * maze.height);
	   primaryStage.setTitle("老鼠迷宫");
	   primaryStage.setScene(scene);
	   primaryStage.show();
     
        //使得老鼠图片可以接受键盘输入
       maze.imageViewMouse.requestFocus();

        
//     public static void main(String[] args) {
//        Application.launch(args);
//     }
    }
}




