package finalhw;
import java.awt.*;
class Player extends MovingObject{ //MovingObjectを継承．abstract型は必ず実装する．
	boolean lflag,rflag,uflag,dflag,sflag;  //特定のキーが押されたかどうかを判定するフラグ
	int delaytime;  //待ち時間
	Image ftrimg;
	Player(int apWidth,int apHeight){  //コンストラクタ．オブジェクトを作る．
		x=apWidth/2;y=apHeight/2;
		w=30;
		delaytime=5;
	}
	void revive(int apWidth, int apHeight) {} //プレイヤーは復活しないから空
	void move(Graphics buf, int apWidth,int apHeight) { //移動についてのメソッド
		if(hp>0) {
			getimg();
			if(!rflag && lflag) direction += 3;      //方向を変更する   
			if(rflag && !lflag) direction -=3;
			direction+=100;                          //l23で説明
			if(direction>360) direction-=360;        //[0,360]に収まるように調整
			else if(direction<-360) direction+=360;
			buf.fillOval(x-w,y-w,2*w,2*w);           //自機描画
			buf.setColor(Color.red);
			buf.fillOval((int)(x+w*Math.sin(direction*Math.PI/180)), (int)(y+w*Math.cos(direction*Math.PI/180)),15,15);  //どの方向に打つかを示している．弾道と同じように指定すると
			direction-=100;                                                                                              //方向がずれたため，一時的にdirectionをずらすことによって調整した．
			buf.setColor(Color.yellow);
			if(uflag&&dflag) hp=0;    //飽きてもゲームから離脱できる！！！！
		}
	}
	void burst(Graphics buf) {
		buf.drawImage(img,x-w,y-w,x+w,y+w,400,500,720,830,null);
	}
	
}