package finalhw;
import java.awt.*;
class Player extends MovingObject{ //MovingObject���p���Dabstract�^�͕K����������D
	boolean lflag,rflag,uflag,dflag,sflag;  //����̃L�[�������ꂽ���ǂ����𔻒肷��t���O
	int delaytime;  //�҂�����
	Image ftrimg;
	Player(int apWidth,int apHeight){  //�R���X�g���N�^�D�I�u�W�F�N�g�����D
		x=apWidth/2;y=apHeight/2;
		w=30;
		delaytime=5;
	}
	void revive(int apWidth, int apHeight) {} //�v���C���[�͕������Ȃ������
	void move(Graphics buf, int apWidth,int apHeight) { //�ړ��ɂ��Ẵ��\�b�h
		if(hp>0) {
			getimg();
			if(!rflag && lflag) direction += 3;      //������ύX����   
			if(rflag && !lflag) direction -=3;
			direction+=100;                          //l23�Ő���
			if(direction>360) direction-=360;        //[0,360]�Ɏ��܂�悤�ɒ���
			else if(direction<-360) direction+=360;
			buf.fillOval(x-w,y-w,2*w,2*w);           //���@�`��
			buf.setColor(Color.red);
			buf.fillOval((int)(x+w*Math.sin(direction*Math.PI/180)), (int)(y+w*Math.cos(direction*Math.PI/180)),15,15);  //�ǂ̕����ɑł��������Ă���D�e���Ɠ����悤�Ɏw�肷���
			direction-=100;                                                                                              //���������ꂽ���߁C�ꎞ�I��direction�����炷���Ƃɂ���Ē��������D
			buf.setColor(Color.yellow);
			if(uflag&&dflag) hp=0;    //�O���Ă��Q�[�����痣�E�ł���I�I�I�I
		}
	}
	void burst(Graphics buf) {
		buf.drawImage(img,x-w,y-w,x+w,y+w,400,500,720,830,null);
	}
	
}