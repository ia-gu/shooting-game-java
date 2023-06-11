package finalhw;
import java.awt.*;
import java.awt.image.*;
import java.io.File;      //�t�@�C�����w��ɕK�v
import javax.imageio.ImageIO;  //�摜�̃T�C�Y��c�����邽�߂ɕK�v
import java.awt.event.*;   //�L�[�{�[�h���͂ɕK�v
public class GameMaster extends Canvas implements KeyListener{
/****************���ʕϐ�****************/
	Image buf;                 //�_�u���o�b�t�@�����O�p�̗���
	BufferedImage s_image;     //�X�^�[�g���
	BufferedImage g_image;     //�Q�[���I�[�o�[���
	BufferedImage score_image; //�X�R�A�\���p�̐����摜
	BufferedImage back;        //�Q�[���̔w�i
	Graphics buf_gc;           //�{��
	Dimension d;               //�T�C�Y
	Font myFont = new Font("�V����",1,30);   //�t�H���g�ύX
	private int imgW, imgH;     //�L�����o�X�̃T�C�Y
	private int b_wid,b_hei;    //�w�i�p�̉摜�̃T�C�Y
	private int score=0;        //�X�R�A��ۑ�����
	private boolean[][] area;   //��q
/***********�Q�[�����ϐ�************/
	private int bosslash = 0;
/*�G�̐�*/        private int enmynum = 10;  /*�v���C���[�̒e��*/  private int ftrBltNum= 50;
/*�{�X�̒e��*/     private int bossBltNum=20;/*�Q�[�����̏�ʐ���*/ private int mode = -1;
/*�w�i�X�N���[���p*/ private int scroll=0;    /*�{�X��|������*/    private int c_times=0;
/*�{�X�̋����𐧌�*/ private int act=0;    /*�{�X��|���Ă���̎���*/  private int b_time=0;
/*�����Ă���̎���*/ private int d_time=0;  /*�f�B���C�̒������w��*/   private int t_end=30;
/*���[�v�p*/       private int i,j;       /*�w�i�X�N���[���p*/      private int k=0;
/*�w�i�X�N���[���p(2�ȏ�)*/private int l=2;
/*�h��Ԃ��͈͎̔w��i���E�c�j*/ private int wblock;                private int hblock;
/*���Ԃ��v��*/     private int time=0;   /*�h��Ԃ��u���b�N��*/     private int num_block=0;
/*�{�X�̂��Ƃ��Ƃ̍��W��ۑ�����ϐ�*/ private int prex;               private int prey;
/*�{�X�̂��Ƃ��Ƃ̑�����ۑ�����ϐ�*/ private double ddx=0.0;          private double ddy=0.0;  
/*�f�B���C�������邩�ǂ���*/ private boolean delay_flag=false; 
/*�X�N���[���̉摜������Ă��邩�i���E�c�j*/private boolean wid_flag=true;            private boolean hei_flag=true;
/*�{�X��ɓ���t���O*/private boolean boss_flag=false;   /*�{�X�̋�����ς���t���O*/ private boolean action_flag=false;
/*��q*/ private boolean tp_flag=false;                           /*��q*/ private boolean bar_flag=false;
    Player ftr;                                              //�v���C���[�̒�`
	EnemyBullet enmBlt[] = new EnemyBullet[enmynum];         //�G�̒e���`
	FighterBullet ftrBlt[]=new FighterBullet[ftrBltNum];     //�e�̒�`
	Enemys enmy[]=new Enemys[enmynum];                       //�G�̒�`
	Boss boss;                                               //�{�X�̒�`
	EnemyBullet bossBlt[]=new EnemyBullet[bossBltNum];       //�{�X�̒e�̒�`
	Enemys enm[];                                            //��q
	GameMaster(int imgW,int imgH){  //�R���X�g���N�^
		this.imgW=imgW;this.imgH=imgH; //�T�C�Y����
		setSize(imgW,imgH);          //�T�C�Y�̌���
		addKeyListener(this);        //�L�[�{�[�h���͂ɑΉ�������
		try {                        //�Ȃ��ƃ_��
			s_image = ImageIO.read(new File("img/start.gif"));    //�X�^�[�g�摜
			g_image = ImageIO.read(new File("img/gameover.gif")); //�Q�[���I�[�o-
		}catch(Exception e) {throw new RuntimeException(e);}
		ftr=new Player(imgW, imgH); //�v���C���[�I�u�W�F�N�g�����
		reflesh(); //���̂ق��̃I�u�W�F�N�g���܂Ƃ߂č��
	}
	private void reflesh(){             //���@���������ׂẴI�u�W�F�N�g�̏��������s��   
		for(i=0;i<ftrBltNum;i++) {       //�����̒e�I�u�W�F�N�g��z��ɂ���Ď���
			ftrBlt[i]=new FighterBullet(c_times);
		}
		for(i=0;i<enmynum;i++){     //�G�I�u�W�F�N�g��z��ɂ���Ď���
			enmy[i] = new Enemys(imgW,imgH);
			enmBlt[i]= new EnemyBullet();   //�G�̒e�I�u�W�F�N�g��z��ɂ���Ď���
		}
		boss = new Boss(imgW,imgH);       //�{�X�̎���
		for(i=0;i<bossBltNum;i++) {      //�{�X���g���e������
			bossBlt[i]=new EnemyBullet((int)Math.round(Math.random()*10-5),7); 
			}
	}
	
	public void addNotify() {  //�G���[�h�~�̂��߂̏���
		super.addNotify();
		buf=createImage(imgW,imgH);
		buf_gc = buf.getGraphics();
	}
	
	public void bang(Player ftr) {                   //�v���C���[�̒e���������郁�\�b�h
		if(ftr.sflag == true && ftr.delaytime==0) {  //�X�y�[�X�������ꂽ�Ƃ��e��ł�
			for(i=0;i<ftrBltNum;i++) {
				if(ftrBlt[i].hp==0) {                //�v���C���[�������Ă���Ƃ��ɒe��ł�
					ftrBlt[i].revive(ftr.x,ftr.y);
					ftr.delaytime=3;                 //�҂����Ԃ�����D
					break;
				}
			}
		}else if (ftr.delaytime>0) ftr.delaytime--;  //�҂����Ԃ�0�łȂ��Ƃ��͑҂����Ԃ����炷�D
	}
	public void bossbang(){                            //�{�X�̒e���������郁�\�b�h
		if(Math.random()<0.1*(c_times+1) && boss.delay_time==0) {  //�{�X�̒e�̑h���������_���ōs��
			for(i=0;i<bossBltNum;i++) {
				if(bossBlt[i].hp==0) {
					bossBlt[i].revive(boss.x,boss.y,imgW,imgH);
					boss.delay_time=10-c_times;
					break;
				}
			}
		}else if (boss.delay_time>0) boss.delay_time--;
	}
	
	public void show_status(Player ftr) {
		buf_gc.setColor(Color.yellow);            
		buf_gc.setFont(myFont);  //�t�H���g�̕ύX
		buf_gc.drawString("HP:"+ftr.hp,10,30);     //�v���C���[���̎c�@�̕\���D
		buf_gc.drawString("score:"+score, 10, 50); //�X�R�A�\��
	}
	
	public void paint(Graphics g) {   //�L�����o�X�E�I�u�W�F�N�g�Ȃǂ���̓I�ɋL�q����
		
		
		  
//		bosslash = (1+c_times)*(-2);                     //����1�s������Ԃ̓{�X�킩��n�܂�D�s�v�ȊԂ̓R�����g�ɂ��Ă���
		
		
		
		if(boss_flag && mode==1 && ftr.hp>0) {       //�G���X�e�[�W����{�X�X�e�[�W�ֈڍs���邽�߂̏����D
			mode++;  
			reflesh();                    //���������Ȃ��ƁC�����̒e���c�����܂܂ɂȂ�D
		}
		if(d_time>=t_end && ftr.hp<1) {   //���񂾂Ƃ�
			d_time=0;mode=-2;             //����delay���g���ƁC��ʑS�̂��ł܂��Ă��܂����߁C�ʂ̕��@��������D
		}
		if(delay_flag==true) {       //�����Ȃ�X�e�[�W���؂�ւ��̂����f�Ȃ̂ŏ����Ԃ�������
			try {Thread.sleep(1500);}         
			catch(InterruptedException e) {} //�����ƃR���p�C���G���[
			delay_flag=false;        //�t���O�������Ȃ��Ɩ�����s����Ă��܂��D
		}
		buf_gc.setColor(Color.gray);
		buf_gc.fillRect(0, 0, imgW, imgH);    //��p����p��
		buf_gc.setFont(myFont);
		buf_gc.setColor(Color.yellow);
		if(mode==3) {   //�{�X��͔w�i�摜�̃I�[�g�X�N���[�����s���D
			scroll++;   //�I�[�g�X�N���[���p
			for(i=k;i<l;i++) {        //�ȉ��͔w�i�̃X�N���[���𖳌��ɍs�������ł���D�������Ⴆ��for(i=0;i>1000000;i++){,,,}
				for(j=k;j<l;j++) {    //�Ƃ������[�v�ł��^���I�Ȗ����̃X�N���[���͎����\�ł��邪�C�ǂ��l���Ă����������傫�����邽��
					buf_gc.drawImage(back, 0, 0, imgW, imgH,-scroll + b_wid*i,-scroll + b_hei*j,-scroll + b_wid*(i+1),-scroll + b_hei*(j+1), null);
				}  
			}      //�܂��C�I�[�g�X�N���[���͕ϐ�scroll�ɂ���������D���̕ϐ����w�i�摜�̏c���邢�͉��ɒB����Ƃ��̑��ɉ摜���ꖇ�ǉ�����D
			if(scroll > b_hei && scroll < b_wid && hei_flag==true) { //�t���O�𗧂ĂȂ��ƁCscroll���c�������ɒB����܂ŉ摜��ǉ���������
				l++;hei_flag=false;                                  //���ƂɂȂ�C�O�q�̖����p�ӂ��邱�Ƃƕς��Ȃ��Ȃ��Ă��܂��D  
			}
			if(scroll < b_hei && scroll > b_wid && wid_flag==true) {
				k++;wid_flag=false;//�t���O��������
			}
			if(scroll > b_hei && scroll > b_wid) {scroll=0;hei_flag=true;wid_flag=true;} 	//�t���O���グ�Ȃ���
		}
		switch (mode) {  //mode�ɂ���ď�Ԃ𐧌䂷��D
		case -2:    //���񂾂Ƃ�
			buf_gc.drawString("Your Final Score is     "+score,imgW/2-150,imgH/2+80); //�X�R�A�̕\��
			c_times=0;   //�������D�����ăQ�[��������ꍇ���߂Ă����Ƀ{�X �Ƃ������ۂ�h������
			break;
			
		case -1:   //�^�C�g�����
			ftr=new Player(imgW, imgH); 
			buf_gc.drawImage(s_image, 0, 0, imgW, imgH, null);   //�X�^�[�g���(��)
			break;
			
		case 0:   //�X�e�[�W�؂�ւ��̍ۂɃX�e�[�W�����L���D
			try {
				back = ImageIO.read(new File("img/map0"+(c_times+1)+".jpg"));
			}catch(Exception e) {throw new RuntimeException(e);}
			b_wid = back.getWidth();
			b_hei = back.getHeight();
			boss_flag = false;
			num_block = 3+2*c_times+bosslash;    //�h��Ԃ������G���A���̐ݒ�D��ɂ��Ȃ��ƁC�����ʒu���h��ɂ���            //////////////////////////////////////////////
			area = new boolean[num_block][num_block];   //�h��Ԃ������ǂ������L������z��  �h��Ă����true
			area[num_block/2][num_block/2] = true;      //�����ʒu��true�ɂ���D
			reflesh();                                  //boss->�G���̃X�e�[�W�ړ����l�����ď�����
			wblock = imgW/num_block;                    //�h��Ԃ������G���A�͈̔͂�ݒ肷��
			hblock = imgH/num_block;                    
			
			buf_gc.drawString("STAGE" +(c_times+1) +"START", imgW/2-100, imgH/2-20);
			delay_flag=true; //�t���O�𗧂ĂĂ�Ƃ�����
			mode++;          //���̃X�e�[�W��
			break;
			
		case 1:    //�Q�[����
			boss_flag=true;                 //�{�X��ړ��̃t���O�𗧂Ă� 
			for(i=0;i<num_block;i++) {      
				for(j=0;j<num_block;j++) {  
					if(area[i][j])          //�h��Ԃ��̃t���O�������Ă���G���A�͓h��Ԃ��C�t���O�������Ă��Ȃ��G���A��1�ł�����΃{�X��ړ��̃t���O��������D
						buf_gc.drawImage(back, wblock*(i+1), hblock*(j+1), wblock*(i), hblock*(j),b_wid/num_block*(i+1),b_hei/num_block*(j+1),b_wid/num_block*i,b_hei/num_block*j, null);
					else                   
						boss_flag=false;
				}
			}
			makeEnmy: if(Math.random()<0.1) {  //���m���œG�����D
				for(i=0;i<enmynum;i++) {
					if(enmy[i].hp==0) {    //�G�������Ă����ꍇ�ɋN���邽�߁C������͈��
						enmy[i].revive(imgW,imgH);  //�G�h��
						break makeEnmy;
					}
				}
			}
			show_status(ftr);
			bang(ftr);                               //�v���C���[�̒e�����D
			for(i=0;i<enmynum;i++) {                   //�G�̒e�̑h�����s��
				if(Math.random()<0.01*(c_times+1)*(c_times+1) && enmy[i].hp!=0 && enmBlt[i].hp==0) {
					enmBlt[i].revive(enmy[i].x,enmy[i].y, imgW,imgH);
				}
			}
			for(i=0;i<enmynum;i++) {    //���ׂĂ̐������G�ɑ΂��ďՓ˔�����s��
				if(enmy[i].hp>0) {      
					ftr.collisionCheck(enmy[i]);  //�v���C���[�ƓG�̔���
					for(j=0;j<ftrBltNum;j++) {
						if(ftrBlt[j].hp>0) {      //�v���C���[�̒e�ƓG�̔���
							if(ftrBlt[j].collisionCheck(enmy[i])) {
								score+=100*(c_times+1);                      //�X�R�A���Z
								buf_gc.setColor(Color.orange);
								buf_gc.fillOval(enmy[i].x-enmy[i].w, enmy[i].y-enmy[i].w, enmy[i].w*4, enmy[i].w*4);
								int xblock = enmy[i].x/wblock;   //�|�����G�̍��W���G���A��������
								int yblock = enmy[i].y/hblock;
								if(xblock>=num_block) xblock-=1;  //�G���A�̃Y������
								if(yblock>=num_block) yblock-=1;
								if(!area[xblock][yblock])
									area[xblock][yblock]=true;   //�h��Ԃ��̃t���O�𗧂Ă�
							};
							enmBlt[i].collisionCheck(ftrBlt[j]);  //�e���m
						}
					}
					ftr.collisionCheck(enmBlt[i]);  //�G�̒e�ƃv���C���[�̔���
				}
			}
			if(ftr.hp<1){   //���񂾂Ƃ�
				ftr.burst(buf_gc);
				d_time++;
			}
			ftr.move(buf_gc,imgW,imgH);   //�e�I�u�W�F�N�g�̈ړ�
			for(i=0;i<enmynum;i++) {
				enmy[i].move(buf_gc,imgW,imgH);
				enmBlt[i].move(buf_gc, imgW,imgH);
			}
			for(i=0;i<ftrBltNum;i++) {
				ftrBlt[i].move(buf_gc,imgW,imgH);
			}
			break;
			
		case 2:
			buf_gc.drawImage(back, 0, 0, imgW, imgH,0,0,b_wid,b_hei, null);
			buf_gc.drawString("BOSS BATTLE", imgW/2-100, imgH/2-20);  //�X�e�[�W����m�点��
			delay_flag=true;
			mode++;
			break;
			
		case 3:  //�{�X�X�e�[�W
			time++;
			while(time>(299/(c_times+1))) {    //���Ԋu�ŁC�{�X�̋�����ς���D���̊Ԋu�̓N���A���邽�тɒZ���Ȃ�
				int a=(int)Math.floor(Math.random()*5);                     //���[�h�̕ϑJ�̓����_��
				if(act!=a) {                                                //�������[�h�ɂ͂Ȃ�Ȃ�
					if(act==0) {prex=boss.x;prey=boss.y;}                   //���W�̒u������
					else {boss.x=prex;boss.y=prey;boss.dx=ddx;boss.dy=ddy;} //�f�t�H���g�̃X�e�[�^�X���ꎞ�ۑ�
					tp_flag=false;                                          //�ϐ��̃��Z�b�g
					act=a;time=0;action_flag=true;break;                    //���[�h�ύX�̃t���O
				}
			}
			show_status(ftr);
			buf_gc.drawString("BOSS_LIFE"+boss.hp,10,80);  //�{�X�̗̑͂�\��
			bang(ftr);
			if(action_flag) {
				switch(act) {    //���[�h�ύX��switch���ōs��
				case 0:          //�f�t�H���g
				break;
				case 1:          //�㉺���]
					boss.y=(boss.y-imgH)*-1;
					break;
				case 2:          //�㉺���E�ړ�
					boss.dx=Math.ceil(Math.random()*5);
					boss.dy=Math.ceil(Math.random()*5);
					break;
				case 3:          //�����܂��D
					tp_flag=true;
					boss.dx=Math.ceil(Math.random()*5);
					boss.dy=Math.ceil(Math.random()*5);
					break;
				case 4:         //�U�R�G�����D�U�R�G�̐��̓N���A�񐔂ɉ����đ�������D
					enm = new Enemys[5+c_times*c_times];
					for(i=0;i<5+c_times*c_times;i++) {
						enm[i] = new Enemys(imgW,imgH);
						enm[i].revive(imgW,imgH);
						enm[i].dx=enm[i].dy=0;
					}
					bar_flag=true;
					break;
				}
				action_flag=false;   //�t���O��������
			}
			bossbang();
			int cnt=0;
			if(bar_flag) {  //�U�R�G������Ԃ̓{�X�ɒe�������炸�Cact�̃J�E���g���~�܂�D
				time--;
				for(i=0;i<5+c_times*c_times;i++) {
					enm[i].move(buf_gc,imgW,imgH);
					if(enm[i].hp==0) cnt++;
					for(j=0;j<ftrBltNum;j++) {
						ftrBlt[j].collisionCheck(enm[i]);
					}
				}
				if(cnt==5+c_times*c_times)bar_flag=false;
			}
			ftr.move(buf_gc,imgW,imgH);
			if(!tp_flag) boss.move(buf_gc,imgW,imgH);  //�X�e���X�ł͎p���f��Ȃ��悤�ɂ��������߁C���ŕʂɈړ�������
			else {boss.x+=boss.dx; boss.y+=boss.dy;if(boss.x<0 || boss.x+boss.w>imgW)boss.x*=-1; if(boss.y<0 || boss.y+boss.w>imgH)boss.y*=-1;}
			for(i=0;i<ftrBltNum;i++) {
				ftrBlt[i].move(buf_gc,imgW,imgH);
			}
			for(i=0;i<bossBltNum;i++) {
				bossBlt[i].move(buf_gc, imgW, imgH);
			}
			if(ftr.collisionCheck(boss)) {
				boss.dx*=-1;boss.dy*=-1;
			}
			for(j=0;j<ftrBltNum;j++) {   //�Փ˔���
				if(ftrBlt[j].hp>0) {
					if(!bar_flag) {
						if(ftrBlt[j].collisionCheck(boss)) {  //�v���C���[�̒e�ƃ{�X�̏Փ˔���
							score+=100;          //�����邽�тɃX�R�A���Z
						}
					}
					for(i=0;i<bossBltNum;i++) {
						bossBlt[i].collisionCheck(ftrBlt[j]);
					}
				}
			}
			for(i=0;i<bossBltNum;i++) {
				bossBlt[i].collisionCheck(ftr);  //�{�X�̒e�ƃv���C���[�̏Փ˔���
			}
			if(ftr.hp<1) {
				ftr.burst(buf_gc);
				d_time++;
			}
			if(boss.hp<1) {  //�{�X��|������|������
				boss.burst(buf_gc);
				b_time++;          //�{�X��|���Ă����������ʂ𓮂���������
				if(b_time>t_end) {
					score+=1000*(c_times+1)*(c_times+1);  //�X�R�A�����Z
					ftr.hp+=5;    //hp��5���₷
					mode++;       //���̃��[�h��
					b_time=0;
					reflesh();
				}
			}
			break;
		case 4:    //�����ŏI���
			buf_gc.drawString("Cleared", imgW/2-80, imgH/2-20);
			delay_flag=true;  //�t���O���グ��
			c_times++;        //�N���A�������Z
			if(c_times==5) c_times--;     //�N���A�񐔏���i5�ȏ�̓G���[���o�����߁j
			mode=0;           //��ԏ��߂ɖ߂�
			break;
		}
		g.setFont(myFont);
		g.drawImage(buf, 0, 0, this);    //�����𒣂�t����
	}
	public void update(Graphics gc) {
		paint(gc);
	}
	public void keyTyped(KeyEvent ke) {/*���͂��ꂽ�����L�[�͑���Ɏg���݂̂Ȃ̂ł����͋@�\�̎����Ȃ��D*/}
	public void keyPressed(KeyEvent ke) {
		int cd = ke.getKeyCode();
		switch(cd) {
		case KeyEvent.VK_LEFT:    //���L�[����������
			ftr.lflag=true;break;
		case KeyEvent.VK_RIGHT:   //�E�L�[����������
			ftr.rflag=true;break;
		case KeyEvent.VK_UP:      //��L�[
			ftr.uflag=true;break;
		case KeyEvent.VK_DOWN:    //���L�[
			ftr.dflag=true;break;
		case KeyEvent.VK_SPACE:   //�X�y�[�X�L�[
			ftr.sflag=true;
			if(this.mode<0) {
				this.mode++;  //�^�C�g����ʂȂ�Q�[���J�n�D�Q�[���I�[�o�[��ʂȂ�^�C�g����ʂֈڂ�
				ftr.hp=1000;
				score=0;
				ddx=boss.dx;ddy=boss.dy;
			}
			break;
		}
	}
	public void keyReleased(KeyEvent ke) {
		int cd = ke.getKeyCode();
		switch(cd) {
		case KeyEvent.VK_LEFT:
			ftr.lflag=false;break;
		case KeyEvent.VK_RIGHT:
			ftr.rflag=false;break;
		case KeyEvent.VK_UP:
			ftr.uflag=false;break;
		case KeyEvent.VK_DOWN:
			ftr.dflag=false;break;
		case KeyEvent.VK_SPACE:
			ftr.sflag=false;break;
		}
	}
}
