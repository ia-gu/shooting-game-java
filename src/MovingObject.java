package finalhw;
import java.awt.*;
import java.awt.Graphics;//�`��ɕK�v
abstract class MovingObject extends Frame{  //���̂̐e�N���X�D�t���[���͐�Ɍp�����Ȃ��Ǝq�N���X�Ŏg���Ȃ�
	int x,y,w,hp; //�t�B�[���h�ϐ�
	double dx,dy;
	Image img;
	static int direction=90;
	
	MovingObject(){}  //�������Ȃ��Ƃ��͉������Ȃ��D
	MovingObject(int width,int height){
		x=0;
		y=0;   
		dx=(int) (width/2/Math.sqrt(width*width/4+height*height/4));     //�G�I�u�W�F�N�g�͒��S�Ɍ������K�v�����邽�߁D
		dy=(int) (height/2/Math.sqrt(width*width/4+height*height/4));   //���x�͐i�s�����ɑ΂��Ă����悻�P
		w=2;hp=10;                     //�T�C�Y�E�̗͂ɕK�v�ȕϐ�
	}
	abstract void move(Graphics buf, int apWidth, int apHeight);//��Ŏ���
	abstract void revive(int w,int h);       //��Ŏ���
	boolean collisionCheck(MovingObject obj) { //�Փ˂��`�F�b�N�D2���̂̍��W�̋�����2���̍��v�̃T�C�Y��菬�����Ȃ�Փ˂��Ă���
		if(this.hp!=0 && obj.hp!=0 && Math.abs(this.x-obj.x)<=(this.w+obj.w) && Math.abs(this.y-obj.y)<=(this.w+obj.w)) {
			this.hp--;   //�̗͂�������
			obj.hp--;    //����
			return true; //�Փ˂������Ƃ�`����
		}
		else return false;  //�Փ˂��Ȃ��������Ƃ�`����D
	}
	void getimg() {
		img = getToolkit().getImage("img/s_game.gif");  //�摜��荞��
	}
	void burst(Graphics buf) {   //�����G�t�F�N�g�̗p�ӁDgetimg()�����������Ƃ͑O��
		buf.drawImage(img,x-w,y-w,x+w,y+w,50,550,350,850,null);
	}
}

