package Tank_Game;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Vector;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.util.Vector;
import javax.swing.*;
import javax.imageio.*;
import java.io.*;

public class TankGame extends JFrame implements ActionListener
{
	JMenuBar menubar;                   //菜单条
	JMenu menu;     		//菜单
	JMenuItem item[];   //菜单项
	StartPanel startpanel;
	
	MyPanel mpanel;
	Graphics g=null;
	
	public static void main(String args[])
	{
		TankGame tank = new TankGame();
	}
	public TankGame() 
	{			//构造方法
		init();
	    setBounds(200,200,500,500);
	    setVisible(true);
	    setTitle("坦克大战");
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	void init() {
		Thread th2=new Thread (mpanel);
		th2.start();
		menubar= new JMenuBar();
		this.setJMenuBar(menubar);      //添加菜单条
		menu=new JMenu("游戏菜单");
		item=new JMenuItem[4];
		item[0]=new JMenuItem("开始新游戏");
		item[1]=new JMenuItem("接着上局游戏");
		item[2]=new JMenuItem("存盘退出");
		item[3]=new JMenuItem("退出游戏");
		menubar.add(menu);             //向菜单条中添加菜单
		for(int i=0;i<4;i++)
		{
			menu.add(item[i]);        //向菜单中添加菜单项
			item[i].addActionListener(this);
		}
		
		try {
			mpanel=new MyPanel();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		startpanel=new StartPanel();
		
		this.addKeyListener(mpanel);
		add(startpanel,BorderLayout.CENTER);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String c = e.getActionCommand();
		if(c.equals("开始新游戏"))
		{
			remove(startpanel);
			//mpanel=new MyPanel();
			
			add(mpanel,BorderLayout.CENTER);
			setVisible(true);
		}
		
		add(mpanel);
		addKeyListener(mpanel);

		setVisible(true);
		Thread t1=new Thread(mpanel);
		t1.start();
	
	}
}
class  MyPanel extends JPanel implements KeyListener,Runnable{
	Hero hero=null;
	Vector <Enemy> ets=new Vector<Enemy>();    //用vector 存储敌方坦克对象
	Vector <Bomb>bombs=new Vector<Bomb>();
	Image im1,im2,im3;
	public void paint(Graphics g) 
	{
		super.paint(g);
		setBackground(Color.black);
		if(hero.alive)
		{
			for(int i=0;i<ets.size();i++)
			{
				for(int j=0;j<ets.get(i).set2.size();j++)
				{
					HitTank(ets.get(i).set2.get(j),hero);
				}
			}
		}
		if(hero.alive)
		this.drawTank(hero.x,hero.y,hero.direct,1,g);
		
		Enemy temp=new Enemy();
		for(int j=0;j<ets.size();j++)
		{
			for(int i=0;i<hero.set.size();i++)
			{
				destroyed(hero.set.get(i),ets.get(j));
			}
		}
		for(int i=0;i<bombs.size();i++)
		{
			Bomb b=bombs.get(i);
			if(b.lifetime>6)
			{
				g.drawImage(im1, b.x, b.y, 30, 30, this );
			}
			else if(b.lifetime>3)
			{
				g.drawImage(im2, b.x, b.y, 30, 30, this );
			}
			else 
			{
				g.drawImage(im3, b.x, b.y, 30, 30, this );
			}
			b.lifedown();
			if(b.lifetime==0)
			{
				bombs.remove(b);
			}
		}
		for(int i=0;i<ets.size();i++)
		{
			if(!ets.get(i).alive) 
				ets.remove(i);
		}
		for(int i=0;i<ets.size();i++)
		{
			temp=ets.get(i);
			this.drawTank(temp.x,temp.y,temp.direct,2,g);
		}
		g.setColor(Color.red);
		for(int i=0;i<hero.set.size();i++)
		{
			if(!hero.set.get(i).isalive)
			{
				hero.set.remove(i);
			}
		}
		for(int i=0;i<ets.size();i++)
		{
			for(int j=0;j<ets.get(i).set2.size();j++)
			{
				if(!ets.get(i).set2.get(j).isalive)
				{
					ets.get(i).set2.remove(j);					//如果敌方坦克子弹死亡，则移除
				}
			}
		}
		for(int i=0;i<hero.set.size();i++)
		{
			if(hero.shot!=null)
				g.fillOval(hero.set.get(i).x,hero.set.get(i).y,6,6);
		}

		for(int i=0;i<ets.size();i++)
		{
			for(int j=0;j<ets.get(i).set2.size();j++)
			{
				g.setColor(Color.blue);
				g.fillOval(ets.get(i).set2.get(j).x,ets.get(i).set2.get(j).y,6,6);
			}
		}
		
	}
	public void drawTank(int tx,int ty,int direction,int type,Graphics g) 
	{
		if(direction==1)
		{
			if(type==1)
			g.setColor(Color.cyan);
			else 
			g.setColor(Color.orange);
			g.fillRect(tx, ty, 5, 30);
			g.fillRect(tx+5, ty+5, 10, 20);
			g.fillRect(tx+15, ty, 5, 30);
			if(type==1)
			g.setColor(Color.pink);
			else 
			g.setColor(Color.gray);
			g.fillOval(tx+5, ty+10,10,10); 
			g.drawLine(tx+10,ty+10,tx+10,ty-5);
		}
		else if(direction==2)
		{
			if(type==1)
			g.setColor(Color.cyan);
			else 
			g.setColor(Color.orange);
			g.fillRect(tx, ty, 5, 30);
			g.fillRect(tx+5, ty+5, 10, 20);
			g.fillRect(tx+15, ty, 5, 30);
			if(type==1)
			g.setColor(Color.pink);
			else 
			g.setColor(Color.gray);
			g.fillOval(tx+5, ty+10,10,10); 
			g.drawLine(tx+10,ty+20,tx+10,ty+35);
		}
		else if(direction==3)  //判断方向为向左
		{
			if(type==1)         //如果是我方坦克
			g.setColor(Color.cyan);    //我方坦克青色
			else               //如果是敌方坦克
			g.setColor(Color.orange);//敌方坦克橙色
			g.fillRect(tx, ty, 30,5);
			g.fillRect(tx+5, ty+5, 20, 10);
			g.fillRect(tx,ty+15,30,5);
			if(type==1)           //如果是我方坦克
			g.setColor(Color.pink);
			else                 //如果是敌方坦克
			g.setColor(Color.gray);
			g.fillOval(tx+10, ty+5, 10, 10);
			g.drawLine(tx-5,ty+10,tx+15,ty+10);
		}
		else if(direction==4)
		{
			if(type==1)
			g.setColor(Color.cyan);
			else 
			g.setColor(Color.orange);
			g.fillRect(tx, ty, 30,5);
			g.fillRect(tx+5, ty+5, 20, 10);
			g.fillRect(tx,ty+15,30,5);
			if(type==1)
			g.setColor(Color.pink);
			else 
			g.setColor(Color.gray);	
			g.fillOval(tx+10, ty+5, 10, 10);
			g.drawLine(tx+10,ty+10,tx+35,ty+10);
		}
	}
	public MyPanel() throws IOException 
	{
		hero=new Hero(100,300);
		Enemy e[]=new Enemy[4];
		e[0]= new Enemy(50,100);
		e[1]= new Enemy(100,100);
		e[2]= new Enemy(150,100);
		e[3]= new Enemy(200,100);
		for(int i=0;i<4;i++)
		{
			ets.add(e[i]);
		}
		for(int i=0;i<ets.size();i++)
		{
			Thread th3=new Thread(ets.get(i));
			th3.start();
			ets.get(i).shotothers();
		}
		im1=ImageIO.read(new File("C:\\Users\\dell\\eclipse-workspace\\Tank_Game\\src\\Bomb_a.gif"));
		im2=ImageIO.read(new File("C:\\Users\\dell\\eclipse-workspace\\Tank_Game\\src\\Bomb_b.gif"));
		im3=ImageIO.read(new File("C:\\Users\\dell\\eclipse-workspace\\Tank_Game\\src\\Bomb_c.gif"));
	}

	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode()==KeyEvent.VK_LEFT) 
			hero.moveleft();
		if(e.getKeyCode()==KeyEvent.VK_RIGHT)
			hero.moveright();
		if(e.getKeyCode()==KeyEvent.VK_UP)
			hero.moveup();
		if(e.getKeyCode()==KeyEvent.VK_DOWN)
			hero.movedown();
		if(e.getKeyCode()==KeyEvent.VK_SPACE) 
		{
			hero.shotothers();
		}
		repaint();
	}
	
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode()==KeyEvent.VK_LEFT) 
			hero.moveleft();
		if(e.getKeyCode()==KeyEvent.VK_RIGHT)
			hero.moveright();
		if(e.getKeyCode()==KeyEvent.VK_UP)
			hero.moveup();
		if(e.getKeyCode()==KeyEvent.VK_DOWN)
			hero.movedown();
		
		repaint();
	}
	public void destroyed(Shot shot,Enemy en)
	{
		if(shot.x>en.x&&shot.x<(en.x+20)&&shot.y>en.y&&shot.y<(en.y+30))
		{
			en.alive=false;
			shot.isalive=false;
		}
		if(!en.alive)
		{
			Bomb bomb=new Bomb(en.x,en.y);
			bombs.add(bomb);
		}
	}
	public void HitTank(Shot shot,Hero h)
	{
		if(shot.x>h.x&&shot.x<(h.x+20)&&shot.y>h.y&&shot.y<(h.y+30))
		{
			h.alive=false;
			shot.isalive=false;
		}
		if(!h.alive)
		{
			Bomb bomb=new Bomb(h.x,h.y);
			bombs.add(bomb);
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true)
		{
			repaint();
			
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


}
class StartPanel extends JPanel
{	//开始游戏面板
	public void paint(Graphics g) 
	{
		super.paint(g);
		g.setFont(new Font("宋体",Font.BOLD,30));
		g.setColor(Color.RED);
		g.drawString("Welcome To play Tank Game", 20, 100);
	}
}
class Tank
{
	int x;//坦克坐标
	int y;
	int direct;
	int speed=1;
	Color color;
	boolean alive=true;
	public Tank() {}
	public Tank(int x1,int y1,int d1){
		x=x1;
		y=y1;
		direct=d1;
	}
	public void moveup(){
		y-=5;            //向上移动，y-5
		direct=1;         //强制改变坦克方向为向上
	}
	public void movedown(){
		y+=5;             //向下移动，y+5
		direct=2;		 //强制改变坦克方向为向上
	}
	public void moveleft(){ 
		x-=5;				//向左移动，x-5
		direct=3;			//强制改变坦克方向为向左
	}
	public void moveright(){
		direct=4;			//向右移动，x+5
		x+=5;				//强制改变坦克方向为向右
	}
}
class Hero extends Tank
{	//己方坦克
	Vector<Shot> set=new Vector<Shot>();
	Shot shot;
	
	public  Hero(int x1,int y1) 
	{
		x=x1;
		y=y1;
		direct=1;
		color=color.cyan;   //己方坦克为青色
	}	
	public void shotothers() 
	{
		if(this.direct==1) 
		{
			shot=new Shot(x+10,y-5,this.direct);
			set.add(shot);
		}
		else if(this.direct==2)
		{
			shot=new Shot(x+10,y+35,this.direct);
			set.add(shot);
		}
		else if(this.direct==3)
		{
			shot=new Shot(x+15,y+10,this.direct);
			set.add(shot);
		}
		else
		{
			shot=new Shot(this.x+35,this.y+10,this.direct);
			set.add(shot);
		}
		Thread th=new Thread (this.shot);
		th.start();
	}
}
class Enemy extends Tank implements Runnable
{
	public Enemy() {}
	Vector<Shot> set2=new Vector<Shot>();
	Shot shot;
	public void shotothers() 
	{
		if(this.direct==1) 
		{
			shot=new Shot(x+10,y-5,this.direct);
			set2.add(shot);
		}
		else if(this.direct==2)
		{
			shot=new Shot(x+10,y+35,this.direct);
			set2.add(shot);
		}
		else if(this.direct==3)
		{
			shot=new Shot(x+15,y+10,this.direct);
			set2.add(shot);
		}
		else
		{
			shot=new Shot(this.x+35,this.y+10,this.direct);
			set2.add(shot);
		}
		Thread th=new Thread (this.shot);
		th.start();
	}
	public Enemy(int x1,int y1) 
	{
		x=x1;
		y=y1;
		direct=(int)(Math.random()*4+1);
		speed=1;
		color=color.orange;   //敌方坦克为橙色
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		int  tim=0;
		while(true)
		{
			if(set2.size()<5&&tim++==10)
			{
				shotothers();					//用tim控制子弹的发射，否则子弹就会太密集，连在一起
				tim=0;
			}
			if(!alive)
			{
				break;
			}
			
			if(direct==1)
			{
				if(y<=10)
				{
					//direct=2;
					y+=speed;							
					direct=(int)(Math.random()*4+1);    //如果坦克走到了边界，那么就让坦克随便换一个方向
				}
				else										//如果没到边界就沿着当前方向继续走
				y-=speed;
			}
			if(direct==2)
			{
				if(y>=350)
				{
					direct=1;
					y-=speed;
					direct=(int)(Math.random()*4+1);
				}
				y+=speed;
			}
			if(direct==3)
			{
				if(x<=10)
				{
					direct=4;
					x+=speed;
					direct=(int)(Math.random()*4+1);
				}
				else
				x-=speed;
			}
			if(direct==4)
			{
				if(x>=350)
				{
					direct=3;
					x-=speed;
					direct=(int)(Math.random()*4+1);
				}
				else
				x+=speed;
			}
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

class Shot implements Runnable{
	int x;
	int y;
	int direct;
	int speed=10;
	boolean isalive=false;
	
	public Shot(int x1,int y1,int dir) 
	{
		//初始化子弹 
		x=x1;
		y=y1;
		this.direct=dir;
		isalive=true;
	}

	@Override
	public void run() 
	{
		// TODO Auto-generated method stub
		while(true)
		{
			
			if(direct==1){
				y-=speed;
				if(y<=0)
					isalive=false;	
			}
			else if(direct==2){
				y+=speed;	
				if(y>=500)
					isalive=false;
			}
			else if(direct==3){
				x-=speed;
				if(x<=0)
					isalive=false;
			}	
			else if(direct==4){
				x+=speed;
				if(x>=500)
					isalive=false;
			}
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	}	
}
class Bomb{
	int x;
	int y;
	int lifetime=9;
	boolean isalive;
	public Bomb(int x,int y){
		this.x=x;
		this.y=y;
		this.isalive=true;
	}
	public void lifedown(){
		if(lifetime>0){
			lifetime--;
		}
		else {
			isalive=false;
		}
	}
}
