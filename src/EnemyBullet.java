package finalhw;
import java.awt.Color;
import java.awt.Graphics;
class EnemyBullet extends MovingObject{//�G�̒e����������D
	EnemyBullet(){
		w=10;     //�T�C�Y
		dx=0;dy=3;hp=0; //�e�̑��x�Ə�����
	}
	EnemyBullet(int dx,int dy){  //���d��`
		w=10;
		this.dx=dx;
		this.dy=dy;
		hp=0;
	}
	void move(Graphics buf,int apWidth,int apHeight) {  //�ړ�
		if(hp>0) {
			buf.setColor(Color.blue); //�e��`�悷��D
			buf.fillOval(x-w, y-w, 2*w, 2*w);
			if(y>0 && y<apHeight && x>0 && x<apWidth) {x+=dx;y+=dy;} //�g���Ȃ�ړ������C
			else hp=0;  //�g�O�Ȃ���ł���悤�ɂ���D
		}
	}
	void revive(int x,int y, int apWidth, int apHeight) { //�e�͉������ł��߁C�h�����K�v
		this.x=x; //���˂����@�̂ɍ��W�����킹��
		this.y=y;
		dx = -(x-apWidth/2)/Math.sqrt((x-apWidth/2)*(x-apWidth/2)+(y-apHeight/2)*(y-apHeight/2))*5;  //���S���W�֌������悤�ɂ���D�@�̂ƒ��S�̋��������߁C�P�ʃx�N�g���𓾂�D
		dy = -(y-apHeight/2)/Math.sqrt((x-apWidth/2)*(x-apWidth/2)+(y-apHeight/2)*(y-apHeight/2))*5; //�e���͊|���Ȃ����D
		hp=1;  //�e��1��������Ə�����D
	}
	void revive(int x, int y) {}
}
