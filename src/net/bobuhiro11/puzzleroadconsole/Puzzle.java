package net.bobuhiro11.puzzleroadconsole;


import java.awt.*;
import java.util.*;

import android.graphics.Point;


/**
 * @author bobuhiro11
 * 
 * パズルロードの本体です．
 */
public class Puzzle {
	private Point max;
	public Point start,goal;
	
	// Puzzle(),move()以外からは変更をしない．
	public Cell[][] cells;
	
	/**
	 * パズルの盤面を作成する．完成することは保証されるが，完成はしていない．
	 * @param max 盤面の大きさ(一回り大きく)
	 * @param start 開始点
	 * @param goal 終了点 
	 */
	public Puzzle(Point max,Point start,Point goal){

		this.max = max;
		this.start = start;
		this.goal = goal;
		this.cells = this.makeRandomCells(); 
	}
	
	/**
	 * セルをパラメータによって，移動させる．
	 * @param r 何列目，または何行目
	 * @param d 移動させる方向
	 */
	public void move(int r,Direction d){
		this.move(this.cells,r,d);
	}
	
	/**
	 * セルをパラメータによって，移動させる．
	 * @param cells セルの集まり
	 * @param r 何列目，または何行目
	 * @param d 移動させる方向
	 */
	private void move(Cell[][] cells,int r,Direction d){
		Cell c;
		if(d==Direction.down){
			c = cells[r][max.y-2]; 
			for(int y=max.y-3;y>=1;y--)
				cells[r][y+1] = cells[r][y];
			cells[r][1] = c;
		}else if(d==Direction.up){
			c = cells[r][1];
			for(int y=2;y<=max.y-2;y++)
				cells[r][y-1] = cells[r][y];
			cells[r][max.y-2] = c;
		}else if(d==Direction.right){
			c = cells[max.x-2][r];
			for(int x=max.x-3;x>=1;x--)
				cells[x+1][r] = cells[x][r];
			cells[1][r] = c;
		}else if(d==Direction.left){
			c = cells[1][r];
			for(int x=2;x<=max.x-2;x++)
				cells[x-1][r] = cells[x][r];
			cells[max.x-2][r] = c;
		}
		//this.debugCells(this.cells);
		//this.debugAnswer(this.checkAnswer());
	}

	/**
	 * パズルが出来上がっているかどうか
	 */
	public boolean isComplete(){
		boolean[][] a = checkAnswer(cells,start,goal);
		boolean r = a[start.x][start.y] && a[goal.x][goal.y];
		//System.out.print(r);
		//System.out.print("\n");
		return r;
	}
	
	
	/**
	 * パズルが出来上がっているかどうか
	 * @param cells セルの集合
	 */
	private boolean isComplete(Cell[][] cells){
		boolean[][] a = checkAnswer(cells,start,goal);
		boolean r = a[start.x][start.y] && a[goal.x][goal.y];
		return r;
	}
	
	/**
	 * パズルの中で，セルが正しく配置されているところを調べる．
	 * @param cells セルの集合
	 * @return 正しく道ができているところをt,間違っているところはf
	 */
	public boolean[][] checkAnswer(){
		//スタートからの探索
		boolean[][] a = checkAnswer(cells,start,goal);
		//ゴールからの探索
		boolean[][] b = checkAnswer(cells,goal,start);
		boolean[][] c = new boolean[max.x][max.y];
		
		for(int x=0;x<max.x;x++)
			for(int y=0;y<max.y;y++)
				c[x][y] = (a[x][y]||b[x][y]);
		//debugAnswer(a);
		//debugAnswer(b);
		//debugAnswer(c);
		return c;
	}
	
	/**
	 * パズルの中で，セルが正しく配置されているところを調べる．ただし，先頭からのみ調べる
	 * @param cells セルの集合
	 * @return 正しく道ができているところをt,間違っているところはf
	 */
	public boolean[][] checkAnswerStart(){
		return checkAnswer(cells,start,goal);
	}
		
	/**
	 * @param cells セルの集合
	 * @param s　スタート地点
	 * @param g　ゴール地点
	 * @return sからgへ正しく道ができているところをt,間違っているところはf
	 */
	private boolean[][] checkAnswer(Cell[][] cells,Point s,Point g){
		boolean[][] a = new boolean[max.x][max.y];
		for(int x=0;x<max.x;x++)
			for(int y=0;y<max.y;y++)
				a[x][y]=false;
		
		//Point p = (Point) s.clone();
		Point p = new Point(s.x,s.y);
		while(true){
			a[p.x][p.y] = true;
			
			//ゴールへ到達
			if(p.equals(g)){
				break;
			}
			else if(p.x==0){
				//右へ移動する
				if(cells[p.x+1][p.y].left){
					p.x++;
				}else{
					break;
				}
			}else if(p.x==max.x-1){
				//左へ移動する
				if(cells[p.x-1][p.y].right){
					p.x--;
				}else{
					break;
				}
			}else if(p.y==0){
				//下へ移動する
				if(cells[p.x][p.y+1].up){
					p.y++;
				}else{
					break;
				}
			}else if(p.y==max.y-1){
				//上へ移動する
				if(cells[p.x][p.y-1].down){
					p.y--;
				}else{
					break;
				}
			}else if(cells[p.x][p.y].right && 
					(cells[p.x+1][p.y].left||p.x+1==g.x&&p.y==g.y) && 
					!a[p.x+1][p.y]
					){
				//右へ移動する
				p.x++;
			}else if(cells[p.x][p.y].left && 
					(cells[p.x-1][p.y].right||p.x-1==g.x&&p.y==g.y) &&
					!a[p.x-1][p.y]
					){
				//左へ移動する
				p.x--;
			}else if(cells[p.x][p.y].up && 
					(cells[p.x][p.y-1].down	||p.x==g.x&&p.y-1==g.y) &&
					!a[p.x][p.y-1])
				{
				//上へ移動する
				p.y--;
			}else if(cells[p.x][p.y].down &&
					(cells[p.x][p.y+1].up||p.x==g.x&&p.y+1==g.y) && 
					!a[p.x][p.y+1]
					){
				//下へ移動する
				p.y++;
			}else{
				break;
			}
			
				
		}
		//debugAnswer(a);
		return a;
		
	}
	
	/**
	 * @return 正しい道順を含むセルの集まり，さらにそれをランダムに混ぜたもの
	 */
	private Cell[][] makeRandomCells(){
		Cell[][] cells = makeCells();
		Random rand = new Random();
		int r;
		Direction d;
		
		//混ぜる回数
		int n = rand.nextInt(20);
		for(int i=0;i<n;i++){
			if(rand.nextInt(2)==1){
				d=Direction.down;
				r=rand.nextInt(max.x-2)+1;
				this.move(cells,r,d);
			}else{
				d=Direction.up;
				r=rand.nextInt(max.y-2)+1;
				this.move(cells,r,d);
			}
		}
		//いきなり成功するのは防ぐ
		while(isComplete(cells)){
			if(rand.nextInt(2)==1){
				d=Direction.down;
				r=rand.nextInt(max.x-2)+1;
				this.move(cells,r,d);
			}else{
				d=Direction.up;
				r=rand.nextInt(max.y-2)+1;
				this.move(cells,r,d);
			}
			
		}
		
		debugCells(cells);
		return cells;
	}

	/**
	 * @return 完成されたセルの集まり,何かの方向が入っている．
	 */
	private Cell[][] makeCells(){
		Cell[][] cells = makeIncompleteCells();
		for(int x=0;x<=max.x-1;x++)
			for(int y=0;y<=max.y-1;y++)
				if(cells[x][y]==null){
					Cell cell= new Cell();
					if(x!=0 && x!=max.x-1 && y!=0 && y!=max.y-1)
						cell.setRandom();
					cells[x][y] = cell;
				}
		//debugCells(cells);
		return cells;
	}
	
	/**
	 * @return 経路だけのセルの集まり，ほかはnull
	 */
	private Cell[][] makeIncompleteCells(){
		Cell[][] cells = new Cell[max.x][max.y];
		Vector<Point> state = makeState();
		
		for(int i=1; i<=state.size()-2; i++){
			Point pivot = state.get(i);
			//セルに二つの方向を書き込む
			Cell cell = new Cell();
			Direction d1 = relation(pivot,state.get(i-1));
			cell.set(d1);
			Direction d2 = relation(pivot,state.get(i+1));
			cell.set(d2);
			
			cells[pivot.x][pivot.y] = cell;
		}
		//debugCells(cells);
		return cells;
	}
	
	/**
	 * @param pivot 基準
	 * @param p もう一つの座標
	 * @return 基準に対するもう一つの座標の方向,エラーの時null
	 */
	private Direction relation(Point pivot,Point p){
		if(pivot.y==p.y){
			if(pivot.x-1==p.x)		return Direction.left;
			else if(pivot.x+1==p.x)	return Direction.right;
		}
		else if(pivot.x==p.x){
			if(pivot.y-1==p.y)		return Direction.up;
			else if(pivot.y+1==p.y)	return Direction.down;
		}	
		
		return null;	
	}
	
	/**
	 * @return 道順の座標の集合 失敗したらnull
	 */
	@SuppressWarnings("unchecked")
	private Vector<Point> makeState(){
		Random random = new Random();
		
		//探索開始状態
		Vector<Point> vector = new Vector<Point>();
		vector.add(this.start);
		
		//深さ優先探索
		ArrayList< Vector<Point> > stack = new ArrayList< Vector<Point> >();
		stack.add(vector);
		
		while(!stack.isEmpty()){
			//状態をランダムに一つ取り出す．
			int index = random.nextInt(stack.size());
			Vector<Point> state = stack.get(index);
			stack.remove(index);
			
			//debugState(state);
			//その状態の最後の座標
			Point p=state.lastElement();	
			
			//探索完了
			if(p.equals(goal))
				return state;
			
			//新しい状態
			Vector<Point> u = (Vector<Point>) state.clone();
			u.add(new Point(p.x,p.y-1));
			Vector<Point> r = (Vector<Point>) state.clone();
			r.add(new Point(p.x+1,p.y));
			Vector<Point> d = (Vector<Point>) state.clone();
			d.add(new Point(p.x,p.y+1));
			Vector<Point> l = (Vector<Point>) state.clone();
			l.add(new Point(p.x-1,p.y));
			
			if(safe(u))
				stack.add(u);
			if(safe(r))
				stack.add(r);
			if(safe(d))
				stack.add(d);
			if(safe(l))
				stack.add(l);
			
		}
			
		//作成失敗
		return null;
	}
	
	/**
	 * @param state 状態
	 * @return 安全な状態かどうか
	 */
	private boolean safe(Vector<Point> state){
		boolean r = false;
		Point p = state.lastElement();
		state.remove(p);
		
		if( p.x>=1 && p.y>=1 && p.x<=max.x-2 && p.y <=max.y-2 && !state.contains(p) 
				|| p.equals(goal))
			r = true;
			
		state.add(p);
		return r;
	}
	
	/**
	 * @param ans 答え
	 */
	private void debugAnswer(boolean[][] ans){
		for(int y=0;y<=max.y-1;y++){
			for(int x=0;x<=max.x-1;x++){
				if(ans[x][y])
					System.out.print(" * ");
				else 
					System.out.print(" _ ");
			}
			System.out.print("\n");
		}
	}
	
	/**
	 * @param cells セルの集合
	 */
	private void debugCells(Cell[][] cells){
		for(int y=0;y<=max.y-1;y++){
			for(int x=0;x<=max.x-1;x++){
				debugCell(cells[x][y]);
				System.out.print(" ");
			}
			System.out.print("\n");
		}
	}
	
	/**
	 * @param cell セル
	 */
	private void debugCell(Cell cell){
		if(cell==null)
			System.out.print("ER");
		else if(cell.isAllFalse())
			System.out.print("**");
		else{
			if(cell.up)
				System.out.print("u");
			if(cell.right)
				System.out.print("r");
			if(cell.down)
				System.out.print("d");
			if(cell.left)
				System.out.print("l");
		}
	}
	
	/**
	 * @param State 状態
	 */
	private void debugState(Vector<Point> State){
		for(Point p:State){
			debugPoint(p);
		}
		System.out.print("\n");
	}
	
	/**
	 * @param p 座標
	 */
	private void debugPoint(Point p){
			System.out.print("("+p.x+","+p.y+") ");
	}
}