package lab;

import java.io.File;
import java.util.InputMismatchException;
import java.util.Scanner;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Maze extends Pane{
	public int rownum,colnum;
	public int width = 40,height = 40;//方格的尺寸
	public int[][] map = new int[100][100];
	public int[][] flag = new int[4000][4000];//用来标记墙
	
	public Pane pane = new Pane();
	public ImageView imageViewMouse = new ImageView("file:老鼠.jpg");//起点
	public ImageView imageViewBurger = new ImageView("file:汉堡.jpg");//终点
	

	public Maze() {
		getChildren().add(pane);
	}
	
	//从文件读取地图
	public void readMap()throws Exception{
	    try{
	        //读取迷宫文件
	        File file = new File("map.txt");
	        Scanner input = new Scanner(file);
	        //获取地图的宽和高
	        rownum = input.nextInt();
	        colnum = input.nextInt();
	        //加载地图
	        for (int row = 0; row < rownum; row++)
	            for (int col = 0; col < colnum; col++)
	                map[row][col] = input.nextInt();
	        input.close();
	    }
	    catch (InputMismatchException ex){
	        System.out.println("error!");
	    }
	}
	
	//画地图
	public void drawPicture(){
	    for (int row = 0; row < rownum; row++){
	        for (int col = 0; col < colnum; col++){
	        	Rectangle rectangle = new Rectangle(col * width, row * height, width, height);;
	        	
	            //可走的路--白色
	            if (map[row][col] == 1 || map[row][col] == 2 || map[row][col] == 3){                   
	                rectangle.setFill(Color.WHITE); 
	                flag[col * width][row * height] = 1;
	            }
	            
	            //不能走的路--棕色
	            if (map[row][col] == 0){                  
	                rectangle.setFill(Color.BROWN);
	                flag[col * width][row * height] = 0;
	            }
	            
	            pane.getChildren().add(rectangle);
	            
	            //起点图片
	            if(map[row][col] == 2) {          	
	            	imageViewMouse.setFitHeight(height);
	            	imageViewMouse.setFitWidth(width);
	            	imageViewMouse.setX(col * width);
	            	imageViewMouse.setY(row * height);
	                pane.getChildren().add(imageViewMouse);
	                
	                //老鼠逐渐显现
	                FadeTransition ft = new FadeTransition(Duration.millis(5000),imageViewMouse);
	                ft.setFromValue(0);
	                ft.setToValue(1);
	                ft.play();
	
	            }
	            	
	            //终点图片
	            if(map[row][col] == 3) { 
	            	imageViewBurger.setFitHeight(height);
	            	imageViewBurger.setFitWidth(width);
	            	imageViewBurger.setX(col * width);
	            	imageViewBurger.setY(row * height);
	                pane.getChildren().add(imageViewBurger);
	                
	                //汉堡逐渐显现
	                FadeTransition ft = new FadeTransition(Duration.millis(5000),imageViewBurger);
	                ft.setFromValue(0);
	                ft.setToValue(1);
	                ft.play();
	            }
	        }
	    }
	}
	
	//移动老鼠
	public void move() {
		
        //先判断是否往墙上撞，若是则阻止，否则允许
        imageViewMouse.setOnKeyPressed(e -> {
        	
        	//注册处理器以响应键盘事件
       		switch(e.getCode()) {
       		
       			//向下走
	       		case DOWN : 
	       			if(flag[(int)imageViewMouse.getX()][(int)imageViewMouse.getY() + height] == 1) {
	       				imageViewMouse.setY(imageViewMouse.getY() + height);
	       				//使老鼠头向下
	       				imageViewMouse.setRotate(90);
	       				break;
	       			}	
	       			else break;
	       			
	       		//向上走		
	       		case UP : 
	       			if(flag[(int)imageViewMouse.getX()][(int)imageViewMouse.getY() - height] == 1) {
	       				imageViewMouse.setY(imageViewMouse.getY() - height); 
	       				//使老鼠头向上
	       				imageViewMouse.setRotate(-90);
	       				break;
	       			}	
	       			else break;
	       			
	       		//向左走
	       		case LEFT : 
	       			if(flag[(int)imageViewMouse.getX() - width][(int)imageViewMouse.getY()] == 1) {
	       				imageViewMouse.setX(imageViewMouse.getX() - width); 
	       				//让老鼠保持原样
	       				imageViewMouse.setRotate(0);
	       				break;
	       			}	
	       			else break;
	       			
	       		//向右走
	       		case RIGHT : 
	       			if(flag[(int)imageViewMouse.getX() + width][(int)imageViewMouse.getY()] == 1) {
	       				imageViewMouse.setX(imageViewMouse.getX() + width);
	       				//使老鼠头向右（保持原样）
	       				imageViewMouse.setRotate(0);
	       				break;
	       			}	
	       			else break;
	       			
	       		default:
	       			break;
       		}   
       		
       		//每移动一次，就把图片从面板移除，再加进去
       		//否则图片会被背景遮住
       		pane.getChildren().remove(imageViewMouse);
       		pane.getChildren().add(imageViewMouse);
       		
       		//当老鼠吃到汉堡，到达终点
       		if(imageViewMouse.getX() == imageViewBurger.getX() && imageViewMouse.getY() == imageViewBurger.getY()) {
       			
       			//汉堡被吃完，移除汉堡
       			pane.getChildren().remove(imageViewBurger);
       			
//       			//播放闯关成功音乐
//       			String url = "闯关成功.mp3";//闯关成功的音频
//       			Media media = new Media(url);
//       			MediaPlayer mediaPlayer = new MediaPlayer(media); 
//       		     			
//       			mediaPlayer.setAutoPlay(true);
//       			mediaPlayer.setCycleCount(20);
//       			mediaPlayer.play();

       			
       			//提示闯关成功（第二个舞台）
       			StackPane stackPane = new StackPane();
       			Button button = new Button("闯关成功！");
       			stackPane.getChildren().add(button);  
       			
       			Stage stage = new Stage();
       			stage.setTitle("结果提示");
       			stage.setScene(new Scene(stackPane,250,150));
       			stage.show();
       			
       			//变量名不能相同，前面有个e了，要使用其他变量名
       			button.setOnAction(e1 -> {
           			stage.close();
       			});
       			
       			//闯关成功，老鼠逐渐消失
                FadeTransition ft = new FadeTransition(Duration.millis(5000),imageViewMouse);
                ft.setFromValue(1);
                ft.setToValue(0);
                ft.play();
       		}     		
       	});
        
	}
}

	