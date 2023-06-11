package finalhw;
import java.awt.Color;
import java.awt.Graphics;
class FighterBullet extends MovingObject{  //プレイヤーの弾を作る．
	int s;double dx;double dy;
	FighterBullet(int s){ 
		w=15;          //サイズを作る．
		hp=0;
		this.s=s;
		dx=Math.cos(direction*Math.PI/180)*(5+s);dy=Math.sin(direction*Math.PI/180)*(-5-s);   //クリアする度に弾速が上がる．
	}
	void move(Graphics buf,int apWidth,int apHeight) {  //移動
		if(hp>0) {
			//getimg();
			buf.setColor(Color.red);
			buf.fillOval(x-w, y-w, 2*w, 2*w);
			if(y>0 && y<apHeight && x>0 && x<apWidth) {
				x+=dx;
				y+=dy; //枠内なら移動させる
			}
			else hp=0;  //枠外なら消滅するようにする．
		}
	}
	void revive(int x,int y) { //弾は何発も打つため，蘇生が必要
		this.x=x; //プレイヤーの位置に表示されればよい
		this.y=y;
		dx=Math.cos(direction*Math.PI/180)*10;dy=Math.sin(direction*Math.PI/180)*(-10); //三角比により方向を制御する．directionは度数だが，cos,sinは弧度を入力とするので変換する．
		hp=1;  //弾は1発当たると消える．
	}
}
