package finalhw;
import java.awt.Color;
import java.awt.Graphics;
class EnemyBullet extends MovingObject{//敵の弾を実装する．
	EnemyBullet(){
		w=10;     //サイズ
		dx=0;dy=3;hp=0; //弾の速度と初期化
	}
	EnemyBullet(int dx,int dy){  //多重定義
		w=10;
		this.dx=dx;
		this.dy=dy;
		hp=0;
	}
	void move(Graphics buf,int apWidth,int apHeight) {  //移動
		if(hp>0) {
			buf.setColor(Color.blue); //弾を描画する．
			buf.fillOval(x-w, y-w, 2*w, 2*w);
			if(y>0 && y<apHeight && x>0 && x<apWidth) {x+=dx;y+=dy;} //枠内なら移動させ，
			else hp=0;  //枠外なら消滅するようにする．
		}
	}
	void revive(int x,int y, int apWidth, int apHeight) { //弾は何発も打つため，蘇生が必要
		this.x=x; //発射した機体に座標を合わせる
		this.y=y;
		dx = -(x-apWidth/2)/Math.sqrt((x-apWidth/2)*(x-apWidth/2)+(y-apHeight/2)*(y-apHeight/2))*5;  //中心座標へ向かうようにする．機体と中心の距離を求め，単位ベクトルを得る．
		dy = -(y-apHeight/2)/Math.sqrt((x-apWidth/2)*(x-apWidth/2)+(y-apHeight/2)*(y-apHeight/2))*5; //弾速は掛けなおす．
		hp=1;  //弾は1発当たると消える．
	}
	void revive(int x, int y) {}
}
