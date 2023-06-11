package finalhw;
import java.awt.*;
import java.awt.image.*;

class Enemys extends MovingObject { //Fighter�N���X�Ɠ��l�̌p���D
	Enemys(){}
	Enemys(int apWidth, int apHeight){  //�R���X�g���N�^�D
		super(apWidth,apHeight);  //MovingObject�̃R���X�g���N�^�����s
		w=20;hp=0;           //�T�C�Y�̌���Ƒ̗͂̏�����
	}
	void move(Graphics buf,int apWidth,int apHeight) { //�ړ��Ɋւ���
		getimg();
		if(hp>0) {
			buf.setColor(Color.blue);
			buf.fillRect(x-w, y-w, 2*w, 2*w);
			x+=dx;  
			y+=dy;  //�\��t���̍��W��w��p�����̂�MovingObject�N���X��collisioncheck���\�b�h�ɍ��킹�邽�߁D
			if(x+w<0 || x-w>apWidth || y+w<0 || y-w>apHeight)
				hp=0;
		}
	}
	void revive(int apWidth,int apHeight) {  //�G�͐����Ԃ�悤�ɂ���D
		double type=Math.random();     //�����𔭐������C�G����C�E�C���C���̂����ꂩ���琶��������D�v���C���[�����S�ŌŒ�̂��ߒ��S�ɂ��͓̂�Փx������
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
		dx = -(x-apWidth/2)/Math.sqrt((x-apWidth/2)*(x-apWidth/2)+(y-apHeight/2)*(y-apHeight/2))*3;    //��ʂ̒��S�֌������D
		dy = -(y-apHeight/2)/Math.sqrt((x-apWidth/2)*(x-apWidth/2)+(y-apHeight/2)*(y-apHeight/2))*3;   //���l
		hp=1;
	}
}
class Boss extends MovingObject{  //�{�X�̃I�u�W�F�N�g�𐶐�
	int delay_time;    //�{�X�����˂���e�̑҂����Ԃ�ݒ�
	Boss(){}
	Boss(int apWidth,int apHeight){
		super(apWidth,apHeight); //MovingObject�̃R���X�g���N�^�����s
		w=50;hp=15;delay_time=3;  //�{�X�I�u�W�F�N�g�̃X�e�[�^�X��ݒ�
		while(x-w<=0 || x+w>=apWidth) x=(int)Math.round(Math.random()*(apWidth-8*w))+w*2;
		y=50;dx=5;dy=0;   //���W�̐ݒ�
	}
	void move(Graphics buf, int apWidth, int apHeight) { 
		getimg();
		if(hp>0) {
			buf.drawImage(img,x-w,y-w,x+w,y+w,500,500,0,0,null);
			x+=dx;                              //���Ɉړ�����
			y+=dy;
			if(x+w>apWidth || x-w<0)  dx*=-1;
			if(y+w>apHeight || y-w<0) dy*=-1;
		}
	}
	void revive(int apWidth, int apHeight) {} //�����Ԃ�Ȃ�
}