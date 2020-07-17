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
	JMenuBar menubar;                   //�˵���
	JMenu menu;     		//�˵�
	JMenuItem item[];   //�˵���
	StartPanel startpanel;
	
	MyPanel mpanel;
	Graphics g=null;
	
	public static void main(String args[])
	{
		TankGame tank = new TankGame();
	}
	public TankGame() 
	{			//���췽��
		init();
	    setBounds(200,200,500,500);
	    setVisible(true);
	    setTitle("̹�˴�ս");
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	void init() {
		Thread th2=new Thread (mpanel);
		th2.start();
		menubar= new JMenuBar();
		this.setJMenuBar(menubar);      //��Ӳ˵���
		menu=new JMenu("��Ϸ�˵�");
		item=new JMenuItem[4];
		item[0]=new JMenuItem("��ʼ����Ϸ");
		item[1]=new JMenuItem("�����Ͼ���Ϸ");
		item[2]=new JMenuItem("�����˳�");
		item[3]=new JMenuItem("�˳���Ϸ");
		menubar.add(menu);             //��˵�������Ӳ˵�
		for(int i=0;i<4;i++)
		{
			menu.add(item[i]);        //��˵�����Ӳ˵���
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
		if(c.equals("��ʼ����Ϸ"))
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
	Vector <Enemy> ets=new Vector<Enemy>();    //��vector �洢�з�̹�˶���
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
					ets.get(i).set2.remove(j);					//����з�̹���ӵ����������Ƴ�
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
		else if(direction==3)  //�жϷ���Ϊ����
		{
			if(type==1)         //������ҷ�̹��
			g.setColor(Color.cyan);    //�ҷ�̹����ɫ
			else               //����ǵз�̹��
			g.setColor(Color.orange);//�з�̹�˳�ɫ
			g.fillRect(tx, ty, 30,5);
			g.fillRect(tx+5, ty+5, 20, 10);
			g.fillRect(tx,ty+15,30,5);
			if(type==1)           //������ҷ�̹��
			g.setColor(Color.pink);
			else                 //����ǵз�̹��
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
{	//��ʼ��Ϸ���
	public void paint(Graphics g) 
	{
		super.paint(g);
		g.setFont(new Font("����",Font.BOLD,30));
		g.setColor(Color.RED);
		g.drawString("Welcome To play Tank Game", 20, 100);
	}
}
class Tank
{
	int x;//̹������
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
		y-=5;            //�����ƶ���y-5
		direct=1;         //ǿ�Ƹı�̹�˷���Ϊ����
	}
	public void movedown(){
		y+=5;             //�����ƶ���y+5
		direct=2;		 //ǿ�Ƹı�̹�˷���Ϊ����
	}
	public void moveleft(){ 
		x-=5;				//�����ƶ���x-5
		direct=3;			//ǿ�Ƹı�̹�˷���Ϊ����
	}
	public void moveright(){
		direct=4;			//�����ƶ���x+5
		x+=5;				//ǿ�Ƹı�̹�˷���Ϊ����
	}
}
class Hero extends Tank
{	//����̹��
	Vector<Shot> set=new Vector<Shot>();
	Shot shot;
	
	public  Hero(int x1,int y1) 
	{
		x=x1;
		y=y1;
		direct=1;
		color=color.cyan;   //����̹��Ϊ��ɫ
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
		color=color.orange;   //�з�̹��Ϊ��ɫ
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		int  tim=0;
		while(true)
		{
			if(set2.size()<5&&tim++==10)
			{
				shotothers();					//��tim�����ӵ��ķ��䣬�����ӵ��ͻ�̫�ܼ�������һ��
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
					direct=(int)(Math.random()*4+1);    //���̹���ߵ��˱߽磬��ô����̹����㻻һ������
				}
				else										//���û���߽�����ŵ�ǰ���������
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
		//��ʼ���ӵ� 
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
