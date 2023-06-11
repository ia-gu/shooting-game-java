package finalhw;
import java.awt.*;
import java.awt.Graphics;//描画に必要
abstract class MovingObject extends Frame{  //動体の親クラス．フレームは先に継承しないと子クラスで使えない
	int x,y,w,hp; //フィールド変数
	double dx,dy;
	Image img;
	static int direction=90;
	
	MovingObject(){}  //引数がないときは何もしない．
	MovingObject(int width,int height){
		x=0;
		y=0;   
		dx=(int) (width/2/Math.sqrt(width*width/4+height*height/4));     //敵オブジェクトは中心に向かう必要があるため．
		dy=(int) (height/2/Math.sqrt(width*width/4+height*height/4));   //速度は進行方向に対しておおよそ１
		w=2;hp=10;                     //サイズ・体力に必要な変数
	}
	abstract void move(Graphics buf, int apWidth, int apHeight);//後で実装
	abstract void revive(int w,int h);       //後で実装
	boolean collisionCheck(MovingObject obj) { //衝突をチェック．2物体の座標の距離が2物体合計のサイズより小さいなら衝突している
		if(this.hp!=0 && obj.hp!=0 && Math.abs(this.x-obj.x)<=(this.w+obj.w) && Math.abs(this.y-obj.y)<=(this.w+obj.w)) {
			this.hp--;   //体力を下げる
			obj.hp--;    //同上
			return true; //衝突したことを伝える
		}
		else return false;  //衝突しなかったことを伝える．
	}
	void getimg() {
		img = getToolkit().getImage("img/s_game.gif");  //画像取り込み
	}
	void burst(Graphics buf) {   //爆発エフェクトの用意．getimg()が働いたことは前提
		buf.drawImage(img,x-w,y-w,x+w,y+w,50,550,350,850,null);
	}
}

