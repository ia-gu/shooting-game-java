package finalhw;
import java.awt.*;
import java.awt.image.*;
import java.io.File;      //ファイル名指定に必要
import javax.imageio.ImageIO;  //画像のサイズを把握するために必要
import java.awt.event.*;   //キーボード入力に必要
public class GameMaster extends Canvas implements KeyListener{
/****************共通変数****************/
	Image buf;                 //ダブルバッファリング用の裏紙
	BufferedImage s_image;     //スタート画面
	BufferedImage g_image;     //ゲームオーバー画面
	BufferedImage score_image; //スコア表示用の数字画像
	BufferedImage back;        //ゲームの背景
	Graphics buf_gc;           //本紙
	Dimension d;               //サイズ
	Font myFont = new Font("遊明朝",1,30);   //フォント変更
	private int imgW, imgH;     //キャンバスのサイズ
	private int b_wid,b_hei;    //背景用の画像のサイズ
	private int score=0;        //スコアを保存する
	private boolean[][] area;   //後述
/***********ゲーム内変数************/
	private int bosslash = 0;
/*敵の数*/        private int enmynum = 10;  /*プレイヤーの弾数*/  private int ftrBltNum= 50;
/*ボスの弾数*/     private int bossBltNum=20;/*ゲーム内の場面制御*/ private int mode = -1;
/*背景スクロール用*/ private int scroll=0;    /*ボスを倒した回数*/    private int c_times=0;
/*ボスの挙動を制御*/ private int act=0;    /*ボスを倒してからの時間*/  private int b_time=0;
/*負けてからの時間*/ private int d_time=0;  /*ディレイの長さを指定*/   private int t_end=30;
/*ループ用*/       private int i,j;       /*背景スクロール用*/      private int k=0;
/*背景スクロール用(2以上)*/private int l=2;
/*塗りつぶしの範囲指定（横・縦）*/ private int wblock;                private int hblock;
/*時間を計る*/     private int time=0;   /*塗りつぶすブロック数*/     private int num_block=0;
/*ボスのもともとの座標を保存する変数*/ private int prex;               private int prey;
/*ボスのもともとの速さを保存する変数*/ private double ddx=0.0;          private double ddy=0.0;  
/*ディレイを加えるかどうか*/ private boolean delay_flag=false; 
/*スクロールの画像が足りているか（横・縦）*/private boolean wid_flag=true;            private boolean hei_flag=true;
/*ボス戦に入るフラグ*/private boolean boss_flag=false;   /*ボスの挙動を変えるフラグ*/ private boolean action_flag=false;
/*後述*/ private boolean tp_flag=false;                           /*後述*/ private boolean bar_flag=false;
    Player ftr;                                              //プレイヤーの定義
	EnemyBullet enmBlt[] = new EnemyBullet[enmynum];         //敵の弾を定義
	FighterBullet ftrBlt[]=new FighterBullet[ftrBltNum];     //弾の定義
	Enemys enmy[]=new Enemys[enmynum];                       //敵の定義
	Boss boss;                                               //ボスの定義
	EnemyBullet bossBlt[]=new EnemyBullet[bossBltNum];       //ボスの弾の定義
	Enemys enm[];                                            //後述
	GameMaster(int imgW,int imgH){  //コンストラクタ
		this.imgW=imgW;this.imgH=imgH; //サイズを代入
		setSize(imgW,imgH);          //サイズの決定
		addKeyListener(this);        //キーボード入力に対応させる
		try {                        //ないとダメ
			s_image = ImageIO.read(new File("img/start.gif"));    //スタート画像
			g_image = ImageIO.read(new File("img/gameover.gif")); //ゲームオーバ-
		}catch(Exception e) {throw new RuntimeException(e);}
		ftr=new Player(imgW, imgH); //プレイヤーオブジェクトを作る
		reflesh(); //そのほかのオブジェクトをまとめて作る
	}
	private void reflesh(){             //自機を除くすべてのオブジェクトの初期化を行う   
		for(i=0;i<ftrBltNum;i++) {       //自分の弾オブジェクトを配列によって実装
			ftrBlt[i]=new FighterBullet(c_times);
		}
		for(i=0;i<enmynum;i++){     //敵オブジェクトを配列によって実装
			enmy[i] = new Enemys(imgW,imgH);
			enmBlt[i]= new EnemyBullet();   //敵の弾オブジェクトを配列によって実装
		}
		boss = new Boss(imgW,imgH);       //ボスの実装
		for(i=0;i<bossBltNum;i++) {      //ボスが使う弾を実装
			bossBlt[i]=new EnemyBullet((int)Math.round(Math.random()*10-5),7); 
			}
	}
	
	public void addNotify() {  //エラー防止のための処理
		super.addNotify();
		buf=createImage(imgW,imgH);
		buf_gc = buf.getGraphics();
	}
	
	public void bang(Player ftr) {                   //プレイヤーの弾を実装するメソッド
		if(ftr.sflag == true && ftr.delaytime==0) {  //スペースが押されたとき弾を打つ
			for(i=0;i<ftrBltNum;i++) {
				if(ftrBlt[i].hp==0) {                //プレイヤーが生きているときに弾を打つ
					ftrBlt[i].revive(ftr.x,ftr.y);
					ftr.delaytime=3;                 //待ち時間をつくる．
					break;
				}
			}
		}else if (ftr.delaytime>0) ftr.delaytime--;  //待ち時間が0でないときは待ち時間を減らす．
	}
	public void bossbang(){                            //ボスの弾を実装するメソッド
		if(Math.random()<0.1*(c_times+1) && boss.delay_time==0) {  //ボスの弾の蘇生をランダムで行う
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
		buf_gc.setFont(myFont);  //フォントの変更
		buf_gc.drawString("HP:"+ftr.hp,10,30);     //プレイヤー側の残機の表示．
		buf_gc.drawString("score:"+score, 10, 50); //スコア表示
	}
	
	public void paint(Graphics g) {   //キャンバス・オブジェクトなどを具体的に記述する
		
		
		  
//		bosslash = (1+c_times)*(-2);                     //この1行がある間はボス戦から始まる．不要な間はコメントにしておく
		
		
		
		if(boss_flag && mode==1 && ftr.hp>0) {       //雑魚ステージからボスステージへ移行するための処理．
			mode++;  
			reflesh();                    //初期化しないと，自分の弾が残ったままになる．
		}
		if(d_time>=t_end && ftr.hp<1) {   //死んだとき
			d_time=0;mode=-2;             //下のdelayを使うと，画面全体が固まってしまうため，別の方法を取った．
		}
		if(delay_flag==true) {       //いきなりステージが切り替わるのも質素なので少し間をあける
			try {Thread.sleep(1500);}         
			catch(InterruptedException e) {} //無いとコンパイルエラー
			delay_flag=false;        //フラグを下げないと毎回実行されてしまう．
		}
		buf_gc.setColor(Color.gray);
		buf_gc.fillRect(0, 0, imgW, imgH);    //画用紙を用意
		buf_gc.setFont(myFont);
		buf_gc.setColor(Color.yellow);
		if(mode==3) {   //ボス戦は背景画像のオートスクロールを行う．
			scroll++;   //オートスクロール用
			for(i=k;i<l;i++) {        //以下は背景のスクロールを無限に行う処理である．ただし例えばfor(i=0;i>1000000;i++){,,,}
				for(j=k;j<l;j++) {    //というループでも疑似的な無限のスクロールは実現可能であるが，どう考えてもメモリが大きすぎるため
					buf_gc.drawImage(back, 0, 0, imgW, imgH,-scroll + b_wid*i,-scroll + b_hei*j,-scroll + b_wid*(i+1),-scroll + b_hei*(j+1), null);
				}  
			}      //まず，オートスクロールは変数scrollにより実装する．この変数が背景画像の縦あるいは横に達するとその側に画像を一枚追加する．
			if(scroll > b_hei && scroll < b_wid && hei_flag==true) { //フラグを立てないと，scrollが縦横両方に達するまで画像を追加し続ける
				l++;hei_flag=false;                                  //ことになり，前述の無限個用意することと変わらなくなってしまう．  
			}
			if(scroll < b_hei && scroll > b_wid && wid_flag==true) {
				k++;wid_flag=false;//フラグを下げる
			}
			if(scroll > b_hei && scroll > b_wid) {scroll=0;hei_flag=true;wid_flag=true;} 	//フラグを上げなおす
		}
		switch (mode) {  //modeによって状態を制御する．
		case -2:    //死んだとき
			buf_gc.drawString("Your Final Score is     "+score,imgW/2-150,imgH/2+80); //スコアの表示
			c_times=0;   //初期化．続けてゲームをする場合初めてすぐにボス という現象を防ぐため
			break;
			
		case -1:   //タイトル画面
			ftr=new Player(imgW, imgH); 
			buf_gc.drawImage(s_image, 0, 0, imgW, imgH, null);   //スタート画面(仮)
			break;
			
		case 0:   //ステージ切り替えの際にステージ名を記す．
			try {
				back = ImageIO.read(new File("img/map0"+(c_times+1)+".jpg"));
			}catch(Exception e) {throw new RuntimeException(e);}
			b_wid = back.getWidth();
			b_hei = back.getHeight();
			boss_flag = false;
			num_block = 3+2*c_times+bosslash;    //塗りつぶす分割エリア数の設定．奇数にしないと，初期位置が塗りにくい            //////////////////////////////////////////////
			area = new boolean[num_block][num_block];   //塗りつぶしたかどうかを記憶する配列  塗れているとtrue
			area[num_block/2][num_block/2] = true;      //初期位置をtrueにする．
			reflesh();                                  //boss->雑魚のステージ移動を考慮して初期化
			wblock = imgW/num_block;                    //塗りつぶす分割エリアの範囲を設定する
			hblock = imgH/num_block;                    
			
			buf_gc.drawString("STAGE" +(c_times+1) +"START", imgW/2-100, imgH/2-20);
			delay_flag=true; //フラグを立ててゆとりを作る
			mode++;          //つぎのステージへ
			break;
			
		case 1:    //ゲーム中
			boss_flag=true;                 //ボス戦移動のフラグを立てる 
			for(i=0;i<num_block;i++) {      
				for(j=0;j<num_block;j++) {  
					if(area[i][j])          //塗りつぶしのフラグが立っているエリアは塗りつぶし，フラグが立っていないエリアが1つでもあればボス戦移動のフラグを下げる．
						buf_gc.drawImage(back, wblock*(i+1), hblock*(j+1), wblock*(i), hblock*(j),b_wid/num_block*(i+1),b_hei/num_block*(j+1),b_wid/num_block*i,b_hei/num_block*j, null);
					else                   
						boss_flag=false;
				}
			}
			makeEnmy: if(Math.random()<0.1) {  //一定確率で敵を作る．
				for(i=0;i<enmynum;i++) {
					if(enmy[i].hp==0) {    //敵が減っていた場合に起きるため，上限数は一定
						enmy[i].revive(imgW,imgH);  //敵蘇生
						break makeEnmy;
					}
				}
			}
			show_status(ftr);
			bang(ftr);                               //プレイヤーの弾を作る．
			for(i=0;i<enmynum;i++) {                   //敵の弾の蘇生を行う
				if(Math.random()<0.01*(c_times+1)*(c_times+1) && enmy[i].hp!=0 && enmBlt[i].hp==0) {
					enmBlt[i].revive(enmy[i].x,enmy[i].y, imgW,imgH);
				}
			}
			for(i=0;i<enmynum;i++) {    //すべての生きた敵に対して衝突判定を行う
				if(enmy[i].hp>0) {      
					ftr.collisionCheck(enmy[i]);  //プレイヤーと敵の判定
					for(j=0;j<ftrBltNum;j++) {
						if(ftrBlt[j].hp>0) {      //プレイヤーの弾と敵の判定
							if(ftrBlt[j].collisionCheck(enmy[i])) {
								score+=100*(c_times+1);                      //スコア加算
								buf_gc.setColor(Color.orange);
								buf_gc.fillOval(enmy[i].x-enmy[i].w, enmy[i].y-enmy[i].w, enmy[i].w*4, enmy[i].w*4);
								int xblock = enmy[i].x/wblock;   //倒した敵の座標をエリア分割する
								int yblock = enmy[i].y/hblock;
								if(xblock>=num_block) xblock-=1;  //エリアのズレ調整
								if(yblock>=num_block) yblock-=1;
								if(!area[xblock][yblock])
									area[xblock][yblock]=true;   //塗りつぶしのフラグを立てる
							};
							enmBlt[i].collisionCheck(ftrBlt[j]);  //弾同士
						}
					}
					ftr.collisionCheck(enmBlt[i]);  //敵の弾とプレイヤーの判定
				}
			}
			if(ftr.hp<1){   //死んだとき
				ftr.burst(buf_gc);
				d_time++;
			}
			ftr.move(buf_gc,imgW,imgH);   //各オブジェクトの移動
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
			buf_gc.drawString("BOSS BATTLE", imgW/2-100, imgH/2-20);  //ステージ名を知らせる
			delay_flag=true;
			mode++;
			break;
			
		case 3:  //ボスステージ
			time++;
			while(time>(299/(c_times+1))) {    //一定間隔で，ボスの挙動を変える．この間隔はクリアするたびに短くなる
				int a=(int)Math.floor(Math.random()*5);                     //モードの変遷はランダム
				if(act!=a) {                                                //同じモードにはならない
					if(act==0) {prex=boss.x;prey=boss.y;}                   //座標の置き換え
					else {boss.x=prex;boss.y=prey;boss.dx=ddx;boss.dy=ddy;} //デフォルトのステータスを一時保存
					tp_flag=false;                                          //変数のリセット
					act=a;time=0;action_flag=true;break;                    //モード変更のフラグ
				}
			}
			show_status(ftr);
			buf_gc.drawString("BOSS_LIFE"+boss.hp,10,80);  //ボスの体力を表示
			bang(ftr);
			if(action_flag) {
				switch(act) {    //モード変更はswitch文で行う
				case 0:          //デフォルト
				break;
				case 1:          //上下反転
					boss.y=(boss.y-imgH)*-1;
					break;
				case 2:          //上下左右移動
					boss.dx=Math.ceil(Math.random()*5);
					boss.dy=Math.ceil(Math.random()*5);
					break;
				case 3:          //消えます．
					tp_flag=true;
					boss.dx=Math.ceil(Math.random()*5);
					boss.dy=Math.ceil(Math.random()*5);
					break;
				case 4:         //ザコ敵を作る．ザコ敵の数はクリア回数に応じて増加する．
					enm = new Enemys[5+c_times*c_times];
					for(i=0;i<5+c_times*c_times;i++) {
						enm[i] = new Enemys(imgW,imgH);
						enm[i].revive(imgW,imgH);
						enm[i].dx=enm[i].dy=0;
					}
					bar_flag=true;
					break;
				}
				action_flag=false;   //フラグを下げる
			}
			bossbang();
			int cnt=0;
			if(bar_flag) {  //ザコ敵がいる間はボスに弾が当たらず，actのカウントも止まる．
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
			if(!tp_flag) boss.move(buf_gc,imgW,imgH);  //ステルスでは姿が映らないようにしたいため，下で別に移動させる
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
			for(j=0;j<ftrBltNum;j++) {   //衝突判定
				if(ftrBlt[j].hp>0) {
					if(!bar_flag) {
						if(ftrBlt[j].collisionCheck(boss)) {  //プレイヤーの弾とボスの衝突判定
							score+=100;          //当たるたびにスコア加算
						}
					}
					for(i=0;i<bossBltNum;i++) {
						bossBlt[i].collisionCheck(ftrBlt[j]);
					}
				}
			}
			for(i=0;i<bossBltNum;i++) {
				bossBlt[i].collisionCheck(ftr);  //ボスの弾とプレイヤーの衝突判定
			}
			if(ftr.hp<1) {
				ftr.burst(buf_gc);
				d_time++;
			}
			if(boss.hp<1) {  //ボスを倒したら倒したら
				boss.burst(buf_gc);
				b_time++;          //ボスを倒してからも少し画面を動かし続ける
				if(b_time>t_end) {
					score+=1000*(c_times+1)*(c_times+1);  //スコアを加算
					ftr.hp+=5;    //hpを5増やす
					mode++;       //次のモードへ
					b_time=0;
					reflesh();
				}
			}
			break;
		case 4:    //ここで終わり
			buf_gc.drawString("Cleared", imgW/2-80, imgH/2-20);
			delay_flag=true;  //フラグを上げる
			c_times++;        //クリア数を加算
			if(c_times==5) c_times--;     //クリア回数上限（5以上はエラーが出たため）
			mode=0;           //一番初めに戻る
			break;
		}
		g.setFont(myFont);
		g.drawImage(buf, 0, 0, this);    //裏紙を張り付ける
	}
	public void update(Graphics gc) {
		paint(gc);
	}
	public void keyTyped(KeyEvent ke) {/*入力された文字キーは操作に使うのみなのでここは機能の実装なし．*/}
	public void keyPressed(KeyEvent ke) {
		int cd = ke.getKeyCode();
		switch(cd) {
		case KeyEvent.VK_LEFT:    //左キーが押されると
			ftr.lflag=true;break;
		case KeyEvent.VK_RIGHT:   //右キーが押されると
			ftr.rflag=true;break;
		case KeyEvent.VK_UP:      //上キー
			ftr.uflag=true;break;
		case KeyEvent.VK_DOWN:    //下キー
			ftr.dflag=true;break;
		case KeyEvent.VK_SPACE:   //スペースキー
			ftr.sflag=true;
			if(this.mode<0) {
				this.mode++;  //タイトル画面ならゲーム開始．ゲームオーバー画面ならタイトル画面へ移る
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
