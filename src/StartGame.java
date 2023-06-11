package finalhw;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.*;
import java.awt.image.*;

public class StartGame extends Frame implements Runnable{
	Thread th;   //�X���b�h�N���X�̃I�u�W�F�N�g���쐬�D
	GameMaster gm;  //�Q�[���}�X�^�[�N���X�̃I�u�W�F�N�g���쐬�D
	public static void main(String[] args) {new StartGame();}//���C�����\�b�h
	StartGame(){
		super("GAME");//�e�N���X�����s�����D�����Frame
		int cW=800,cH=600;    //�L�����o�X�̃T�C�Y�����߂�D
		this.setSize(cW+30,cH+40);  //�t���[���̃T�C�Y�����߂�D
		this.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10)); //�Z�b�g
		gm=new GameMaster(cW,cH);  //�Q�[���}�X�^�[�����s�����D
		this.add(gm);  //�Z�b�g
		this.setVisible(true);  //�����鉻����
		
		th=new Thread(this);  //�X���b�h�N���X�̃I�u�W�F�N�g�����D 
		th.start();           //�Q�[���J�n
		requestFocusInWindow();  //�t�H�[�J�X�𓾂�D�L�[�{�[�h����Ȃǂł͂��ꂪ�Ȃ���
	}                            //�ςȓ�������邱�Ƃ�����i�炵�������ɖڗ������ω��͂Ȃ������j�D
	public void run() {  //Runnable�C���^�[�t�F�[�X�ɂ�苭���I�Ɏ�������
		try {            //�X���b�h�ł̏��������
			while(true) {
				th.sleep(20);  //��莞�Ԃ�
				gm.repaint();  //��ʂ��X�V����D
			}
		}  //�ȉ��͗�O�����D
		catch(Exception e) {System.out.println("Exception:"+e);}
	}
}
