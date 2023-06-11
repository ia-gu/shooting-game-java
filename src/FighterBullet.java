package finalhw;
import java.awt.Color;
import java.awt.Graphics;
class FighterBullet extends MovingObject{  //�v���C���[�̒e�����D
	int s;double dx;double dy;
	FighterBullet(int s){ 
		w=15;          //�T�C�Y�����D
		hp=0;
		this.s=s;
		dx=Math.cos(direction*Math.PI/180)*(5+s);dy=Math.sin(direction*Math.PI/180)*(-5-s);   //�N���A����x�ɒe�����オ��D
	}
	void move(Graphics buf,int apWidth,int apHeight) {  //�ړ�
		if(hp>0) {
			//getimg();
			buf.setColor(Color.red);
			buf.fillOval(x-w, y-w, 2*w, 2*w);
			if(y>0 && y<apHeight && x>0 && x<apWidth) {
				x+=dx;
				y+=dy; //�g���Ȃ�ړ�������
			}
			else hp=0;  //�g�O�Ȃ���ł���悤�ɂ���D
		}
	}
	void revive(int x,int y) { //�e�͉������ł��߁C�h�����K�v
		this.x=x; //�v���C���[�̈ʒu�ɕ\�������΂悢
		this.y=y;
		dx=Math.cos(direction*Math.PI/180)*10;dy=Math.sin(direction*Math.PI/180)*(-10); //�O�p��ɂ������𐧌䂷��Ddirection�͓x�������Ccos,sin�͌ʓx����͂Ƃ���̂ŕϊ�����D
		hp=1;  //�e��1��������Ə�����D
	}
}
