package finalhw;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.*;
import java.awt.image.*;

public class StartGame extends Frame implements Runnable{
	Thread th;   //スレッドクラスのオブジェクトを作成．
	GameMaster gm;  //ゲームマスタークラスのオブジェクトを作成．
	public static void main(String[] args) {new StartGame();}//メインメソッド
	StartGame(){
		super("GAME");//親クラスが実行される．今回はFrame
		int cW=800,cH=600;    //キャンバスのサイズを決める．
		this.setSize(cW+30,cH+40);  //フレームのサイズを決める．
		this.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10)); //セット
		gm=new GameMaster(cW,cH);  //ゲームマスターが実行される．
		this.add(gm);  //セット
		this.setVisible(true);  //見える化する
		
		th=new Thread(this);  //スレッドクラスのオブジェクトを作る． 
		th.start();           //ゲーム開始
		requestFocusInWindow();  //フォーカスを得る．キーボード操作などではこれがないと
	}                            //変な動作をすることがある（らしいが特に目立った変化はなかった）．
	public void run() {  //Runnableインターフェースにより強制的に実装する
		try {            //スレッドでの処理を作る
			while(true) {
				th.sleep(20);  //一定時間で
				gm.repaint();  //画面を更新する．
			}
		}  //以下は例外処理．
		catch(Exception e) {System.out.println("Exception:"+e);}
	}
}
