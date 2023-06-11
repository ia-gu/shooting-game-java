package finalhw;
import java.awt.*;
import java.awt.image.*;

class Enemys extends MovingObject { //Fighterクラスと同様の継承．
	Enemys(){}
	Enemys(int apWidth, int apHeight){  //コンストラクタ．
		super(apWidth,apHeight);  //MovingObjectのコンストラクタを実行
		w=20;hp=0;           //サイズの決定と体力の初期化
	}
	void move(Graphics buf,int apWidth,int apHeight) { //移動に関して
		getimg();
		if(hp>0) {
			buf.setColor(Color.blue);
			buf.fillRect(x-w, y-w, 2*w, 2*w);
			x+=dx;  
			y+=dy;  //貼り付けの座標にwを用いたのはMovingObjectクラスのcollisioncheckメソッドに合わせるため．
			if(x+w<0 || x-w>apWidth || y+w<0 || y-w>apHeight)
				hp=0;
		}
	}
	void revive(int apWidth,int apHeight) {  //敵は生き返るようにする．
		double type=Math.random();     //乱数を発生させ，敵を上，右，左，下のいずれかから生成させる．プレイヤーが中心で固定のため中心によるのは難易度が高い
		if(type<0.25) {
			x=(int)(Math.random()*(apWidth));
			y=0;
		}
		else if(type<0.5) {
			x=(int)(Math.random()*(apWidth));
			y=apHeight;
		}
		else if(type<0.75) {
			x=0;
			y=(int)(Math.random()*(apHeight));
		}
		else {
			x=apWidth;
			y=(int)(Math.random()*(apHeight));
		}
		dx = -(x-apWidth/2)/Math.sqrt((x-apWidth/2)*(x-apWidth/2)+(y-apHeight/2)*(y-apHeight/2))*3;    //画面の中心へ向かう．
		dy = -(y-apHeight/2)/Math.sqrt((x-apWidth/2)*(x-apWidth/2)+(y-apHeight/2)*(y-apHeight/2))*3;   //同様
		hp=1;
	}
}
class Boss extends MovingObject{  //ボスのオブジェクトを生成
	int delay_time;    //ボスが発射する弾の待ち時間を設定
	Boss(){}
	Boss(int apWidth,int apHeight){
		super(apWidth,apHeight); //MovingObjectのコンストラクタを実行
		w=50;hp=15;delay_time=3;  //ボスオブジェクトのステータスを設定
		while(x-w<=0 || x+w>=apWidth) x=(int)Math.round(Math.random()*(apWidth-8*w))+w*2;
		y=50;dx=5;dy=0;   //座標の設定
	}
	void move(Graphics buf, int apWidth, int apHeight) { 
		getimg();
		if(hp>0) {
			buf.drawImage(img,x-w,y-w,x+w,y+w,500,500,0,0,null);
			x+=dx;                              //横に移動する
			y+=dy;
			if(x+w>apWidth || x-w<0)  dx*=-1;
			if(y+w>apHeight || y-w<0) dy*=-1;
		}
	}
	void revive(int apWidth, int apHeight) {} //生き返らない
}