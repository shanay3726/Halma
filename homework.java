import java.util.*;
import java.io.*;
import java.io.BufferedWriter;
import java.io.BufferedReader;
class Point{
	int x,y;
	public Point(int x,int y)
	{
		this.x=x;
		this.y=y;
	}
	@Override
    public String toString() {
        return "[" + x + ", " + y + "]";
    }
}
class PointsAndScore{
	int score,x,y;
	Point point;
	public PointsAndScore(int score, Point point,int x,int y)
	{
		this.score=score;
		this.point=point;
		this.x=x;
		this.y=y;
	}
	@Override
    public String toString() {
        return "[" +score+" "+point+" from "+ x + ", " + y + "]";
    }
}
class homework{
	static int no;
	static int [][] board;
	static int [][] boardini;
	static int [][] place;
	static int [][] placetrue;
	static int [][] placeopp;
	static int [] inside;
	static int [] insideb;
	static int [] outside;
	static int [] outsideb;
	static int [] heuristic;
	static int [][] parent1;
	static int [][] parent2;
	static int ps1,ps2;
	static int p1,p2,t1,t2;
	static int hello;
	static int start;
	static int gg;
	static String color;
	static ArrayList<ArrayList<PointsAndScore>> children;
	static long starttime,endtime;
	
	public static void main(String [] args)throws IOException
	{
		//starttime = System.nanoTime();
		int i,j,k=0,l,kk=0;
		board = new int[16][16];
		boardini = new int[16][16];
		inside=new int[19];
		outside=new int[19];
		insideb=new int[19];
		outsideb=new int[19];
		place = new int[19][2];
		placetrue = new int[19][2];
		placeopp = new int[19][2];
		parent1 = new int[16][16];
		parent2 = new int[16][16];
		Scanner sc = new Scanner(System.in);
		File file=new File("input.txt");
		BufferedReader br= new BufferedReader(new FileReader(file));
		String type,st,timing;
		type=br.readLine();
		color=br.readLine();
		timing=br.readLine();
		if(color.equals("WHITE"))
		{
			for(i=0;i<16;i++)
			{	st=br.readLine();
				for(j=0;j<16;j++)
				{
					if(st.charAt(j)=='W')
					{
						board[i][j]=1;
						place[k][0]=i;
						place[k][1]=j;
						placetrue[k][0]=i;
						placetrue[k][1]=j;
						k++;
					}
					else if(st.charAt(j)=='B')
					{	
						board[i][j]=2;
						placeopp[kk][0]=i;
						placeopp[kk][1]=j;
						kk++;
					}
					else if(st.charAt(j)=='.')
						board[i][j]=0;
				}
			}
			t2=0;
			t1=0;
		}
		if(color.equals("BLACK"))
		{
			for(i=0;i<16;i++)
			{	st=br.readLine();
				for(j=0;j<16;j++)
				{
					if(st.charAt(j)=='B')
					{
						board[i][j]=1;
						place[k][0]=i;
						place[k][1]=j;
						placetrue[k][0]=i;
						placetrue[k][1]=j;
						k++;
					}
					else if(st.charAt(j)=='W')
					{
						board[i][j]=2;
						placeopp[kk][0]=i;
						placeopp[kk][1]=j;
						kk++;
					}
					else if(st.charAt(j)=='.')
						board[i][j]=0;
				}
			}
			t1=15;
			t2=15;
		}
		for(i=0;i<16;i++)
		{
			for(j=0;j<16;j++)
			{
				boardini[i][j]=board[i][j];
			}
		}
		for(i=0;i<19;i++)
		{
			if(checkoutsidepoint(placetrue[i][0],placetrue[i][1])==1)
			{
				no=no+1;
			}
		}
		
		if(type.equals("GAME"))
		{
			game();
		}
		else if(type.equals("SINGLE"))
		{
			single();
		}
	}
	public static void game()
	{
		
		callminimax(0,1);
		
		Point ab=returnBestMove();
		int [][] dones = new int[16][16];
		int [][] jumping = new int [16][16]; 
		parent1[ps1][ps2]=999;
		parent2[ps1][ps2]=999;
		writefile(ps1,ps2,ab,0,dones,jumping);

	}
	public static void callminimax(int depth,int turn)
	{
		children = new ArrayList<ArrayList<PointsAndScore>>();
		minimax(depth,turn);
		
	}
	public static int minimax(int depth, int turn)
	{
		int i,j,k,l,m,n;
		if(depth==1 && no>=14)
		{
			return evaluate();
		}
		if(depth==3)
		{
			return evaluate();
		}
		ArrayList<Integer> scores = new ArrayList<Integer>();
		if(turn==1)
		{
			ArrayList<ArrayList<Point>> allchild=getallpoints(turn);
			
			for(i=0;i<allchild.size();i++)
			{
				ArrayList<PointsAndScore> all= new ArrayList<PointsAndScore>();
				
				for(j=0;j<allchild.get(i).size();j++)
				{	
					Point p=allchild.get(i).get(j);
					
					m=place[i][0];				//to remember original place[i][0] and place[i][1] in m and n
					n=place[i][1];
					placeAmove(p,i,turn);
					int currentscore = minimax(depth+1,2);
					scores.add(currentscore);
					if(depth==0)
					{
						all.add(new PointsAndScore(currentscore,p,m,n));
						
					}
					board[p.x][p.y]=0;
					place[i][0]=m;
					place[i][1]=n;
					board[m][n]=1;
				}
				children.add(all);
			}
		}
		else if(turn == 2)
		{
			ArrayList<ArrayList<Point>> allchildb= getallpoints(turn);
			
			for(i=0;i<allchildb.size();i++)
			{
				for(j=0;j<allchildb.get(i).size();j++)
				{	
					Point p=allchildb.get(i).get(j);
					m=placeopp[i][0];
					n=placeopp[i][1];
					placeAmove(p,i,turn);
					int currentscore=minimax(depth+1,1);
					scores.add(currentscore);
					board[p.x][p.y]=0;
					placeopp[i][0]=m;
					placeopp[i][1]=n;
					board[m][n]=2;
				}
			}
		}
		if(turn==1)
			return returnMin(scores);
		else
			return returnMax(scores);
		
	}
	public static int returnMin(ArrayList<Integer> scores)
	{
		int i,index=-1,minimum=Integer.MAX_VALUE;
		for(i=0;i<scores.size();i++)
		{
			if(scores.get(i)<minimum)
			{
				minimum=scores.get(i);
				index=i;

			}
		}
		return scores.get(index);
	}
	public static int returnMax(ArrayList<Integer> scores)
	{
		int i,index=-1,maximum=Integer.MIN_VALUE;
		for(i=0;i<scores.size();i++)
		{
			if(scores.get(i)>maximum)
			{
				maximum=scores.get(i);
				index=i;

			}
		}
		return scores.get(index);
	}
	public static ArrayList<ArrayList<Point>> getallpoints(int turn)
	{	int i,j,k,l,n,m,a,b;
		
		ArrayList<ArrayList<Point>> available = new ArrayList<ArrayList<Point>>();
		if(turn==1)
		{
			if(checkinside(turn)==1)
			{
				for(i=0;i<19;i++)
				{
					ArrayList<Point> ava = new ArrayList<Point>();
					ArrayList<Point> ava1 = new ArrayList<Point>();
					int [][] done = new int[16][16];
					if(inside[i]==1)
					{
						ava1=allpoints(place[i][0],place[i][1],0,ava,done);
					}
					
					else
					{
						ava1=allpoints(place[i][0],place[i][1],0,ava,done);
					}
					available.add(ava1);

				}
				if(available.isEmpty())
				{
					for(i=0;i<19;i++)
					{	
						ArrayList <Point> ava = new ArrayList<Point>();
						ArrayList <Point> ava1 = new ArrayList<Point>();
						int [][] done = new int[16][16];
						ava1 = allpoints(place[i][0],place[i][1],0,ava,done);
						available.add(ava1);
						
					}
				}
			}
			else
			{
				for(i=0;i<19;i++)
				{	
					ArrayList <Point> ava = new ArrayList<Point>();
					ArrayList <Point> ava1 = new ArrayList<Point>();
					int [][] done = new int[16][16];
					ava1 = allpoints(place[i][0],place[i][1],0,ava,done);
					available.add(ava1);
					
				}
			}
		}
		else if(turn==2)
		{
			if(checkinside(2)==1)
			{
				for(i=0;i<19;i++)
				{
					ArrayList<Point> ava = new ArrayList<Point>();
					ArrayList<Point> ava1 = new ArrayList<Point>();
					int [][] done = new int[16][16];
					if(inside[i]==1)
					{
						ava1=allpoints(placeopp[i][0],placeopp[i][1],0,ava,done);
					}
					available.add(ava1);

				}
				if(available.isEmpty())
				{
					for(i=0;i<19;i++)
					{	
						ArrayList <Point> ava = new ArrayList<Point>();
						ArrayList <Point> ava1 = new ArrayList<Point>();
						int [][] done = new int[16][16];
						ava1 = allpoints(placeopp[i][0],placeopp[i][1],0,ava,done);
						available.add(ava1);
						
					}
				}
			}
			else
			{
				for(i=0;i<19;i++)
				{	
					ArrayList <Point> ava = new ArrayList<Point>();
					ArrayList <Point> ava1 = new ArrayList<Point>();
					int [][] done = new int[16][16];
					ava1 = allpoints(placeopp[i][0],placeopp[i][1],0,ava,done);
					available.add(ava1);
					
				}
			}	
		}
		return available;
	}
	public static ArrayList<Point> allpoints(int x,int y,int again,ArrayList<Point> ava,int [][] done)
	{
		
		int i,j,k,l,m,n;
		done[x][y]=1;
		for(i=-1;i<2;i++)
		{
			for(j=-1;j<2;j++)
			{
				if(i!=0 || j!=0)
				{	
					if((x+2*i)>=0 && (y+2*j)>=0 && (x+2*i)<16 && (y+2*j)<16)
					{	
						if((done[x+2*i][y+2*j]==0) && board[x+i][y+j]!=0 && board[x+2*i][y+2*j]==0)
						{	
							ava.add(new Point(x+2*i,y+2*j));
							
							//again = 1;
							done[x+2*i][y+2*j]=1;
							allpoints(x+2*i,y+2*j,1,ava,done);
						}
					}
				}
			}
		}
		if(again==0)
		{
			for(i=-1;i<2;i++)
			{
				for(j=-1;j<2;j++)
				{
					if(i!=0 || j!=0)
					{	
						if((x+i)>=0 && (y+j)>=0 && (x+i)<16 && (y+j)<16)
						{	
							if(done[x+i][y+j]==0 && board[x+i][y+j]==0)
							{	
								ava.add(new Point(x+i,y+j));
								done[x+i][y+j]=1;
							}
						}
					}
				}
			}
		}
		return ava;
	}
	public static void placeAmove(Point p,int index, int turn)
	{
		
		if(turn==1)
		{
			board[place[index][0]][place[index][1]]=0;
			board[p.x][p.y]=1;
			place[index][0]=p.x;
			place[index][1]=p.y;
		}
		else
		{
			board[placeopp[index][0]][placeopp[index][1]]=0;
			board[p.x][p.y]=2;
			placeopp[index][0]=p.x;
			placeopp[index][1]=p.y;	
		}
	}
	public static int checkinside(int turn)
	{	
		int i,j,k;
		if((color.equals("WHITE") && turn==1))
		{
			for(i=0;i<19;i++)
			{	
				if((place[i][0]==15&&place[i][1]==15)||(place[i][0]==15&&place[i][1]==14)||(place[i][00]==15&&place[i][1]==13)||(place[i][0]==15&&place[i][1]==12)||(place[i][0]==15&&place[i][1]==11)||(place[i][0]==14&&place[i][1]==15)||(place[i][0]==14&&place[i][1]==14)||(place[i][0]==14&&place[i][1]==13)||(place[i][0]==14&&place[i][1]==12)||(place[i][0]==14&&place[i][1]==11)||(place[i][0]==13&&place[i][1]==15)||(place[i][0]==13&&place[i][1]==14)||(place[i][0]==13&&place[i][1]==13)||(place[i][0]==13&&place[i][1]==12)||(place[i][0]==12&&place[i][1]==15)||(place[i][0]==12&&place[i][1]==14)||(place[i][0]==12&&place[i][1]==13)||(place[i][0]==11&&place[i][1]==15)||(place[i][0]==11&&place[i][1]==14))
				{	
					inside[i]=1;
				}
			}
			for(i=0;i<19;i++)
			{	
				if((place[i][0]==15&&place[i][1]==15)||(place[i][0]==15&&place[i][1]==14)||(place[i][0]==15&&place[i][1]==13)||(place[i][0]==15&&place[i][1]==12)||(place[i][0]==15&&place[i][1]==11)||(place[i][0]==14&&place[i][1]==15)||(place[i][0]==14&&place[i][1]==14)||(place[i][0]==14&&place[i][1]==13)||(place[i][0]==14&&place[i][1]==12)||(place[i][0]==14&&place[i][1]==11)||(place[i][0]==13&&place[i][1]==15)||(place[i][0]==13&&place[i][1]==14)||(place[i][0]==13&&place[i][1]==13)||(place[i][0]==13&&place[i][1]==12)||(place[i][0]==12&&place[i][1]==15)||(place[i][0]==12&&place[i][1]==14)||(place[i][0]==12&&place[i][1]==13)||(place[i][0]==11&&place[i][1]==15)||(place[i][0]==11&&place[i][1]==14))
				{	
					return 1;
				}
			}
		
		}
		else if((color.equals("BLACK") && turn==2))
		{
			for(i=0;i<19;i++)
			{	
				if((placeopp[i][0]==15&&placeopp[i][1]==15)||(placeopp[i][0]==15&&placeopp[i][1]==14)||(placeopp[i][00]==15&&placeopp[i][1]==13)||(placeopp[i][0]==15&&placeopp[i][1]==12)||(placeopp[i][0]==15&&placeopp[i][1]==11)||(placeopp[i][0]==14&&placeopp[i][1]==15)||(placeopp[i][0]==14&&placeopp[i][1]==14)||(placeopp[i][0]==14&&placeopp[i][1]==13)||(placeopp[i][0]==14&&placeopp[i][1]==12)||(placeopp[i][0]==14&&placeopp[i][1]==11)||(placeopp[i][0]==13&&placeopp[i][1]==15)||(placeopp[i][0]==13&&placeopp[i][1]==14)||(placeopp[i][0]==13&&placeopp[i][1]==13)||(placeopp[i][0]==13&&placeopp[i][1]==12)||(placeopp[i][0]==12&&placeopp[i][1]==15)||(placeopp[i][0]==12&&placeopp[i][1]==14)||(placeopp[i][0]==12&&placeopp[i][1]==13)||(placeopp[i][0]==11&&placeopp[i][1]==15)||(placeopp[i][0]==11&&placeopp[i][1]==14))
				{	
					inside[i]=1;
				}
			}
			for(i=0;i<19;i++)
			{	
				if((placeopp[i][0]==15&&placeopp[i][1]==15)||(placeopp[i][0]==15&&placeopp[i][1]==14)||(placeopp[i][0]==15&&placeopp[i][1]==13)||(placeopp[i][0]==15&&placeopp[i][1]==12)||(placeopp[i][0]==15&&placeopp[i][1]==11)||(placeopp[i][0]==14&&placeopp[i][1]==15)||(placeopp[i][0]==14&&placeopp[i][1]==14)||(placeopp[i][0]==14&&placeopp[i][1]==13)||(placeopp[i][0]==14&&placeopp[i][1]==12)||(placeopp[i][0]==14&&placeopp[i][1]==11)||(placeopp[i][0]==13&&placeopp[i][1]==15)||(placeopp[i][0]==13&&placeopp[i][1]==14)||(placeopp[i][0]==13&&placeopp[i][1]==13)||(placeopp[i][0]==13&&placeopp[i][1]==12)||(placeopp[i][0]==12&&placeopp[i][1]==15)||(placeopp[i][0]==12&&placeopp[i][1]==14)||(placeopp[i][0]==12&&placeopp[i][1]==13)||(placeopp[i][0]==11&&placeopp[i][1]==15)||(placeopp[i][0]==11&&placeopp[i][1]==14))
				{	
					return 1;
				}
			}
		
		}
		else if((color.equals("WHITE") && turn==2))
		{
			for(i=0;i<19;i++)
			{
				if((placeopp[i][0]==0&&placeopp[i][1]==0)||(placeopp[i][0]==0&&placeopp[i][1]==1)||(placeopp[i][00]==0&&placeopp[i][1]==2)||(placeopp[i][0]==0&&placeopp[i][1]==3)||(placeopp[i][0]==0&&placeopp[i][1]==4)||(placeopp[i][0]==1&&placeopp[i][1]==0)||(placeopp[i][0]==1&&placeopp[i][1]==1)||(placeopp[i][0]==1&&placeopp[i][1]==2)||(placeopp[i][0]==1&&placeopp[i][1]==3)||(placeopp[i][0]==1&&placeopp[i][1]==4)||(placeopp[i][0]==2&&placeopp[i][1]==0)||(placeopp[i][0]==2&&placeopp[i][1]==1)||(placeopp[i][0]==2&&placeopp[i][1]==2)||(placeopp[i][0]==2&&placeopp[i][1]==3)||(placeopp[i][0]==3&&placeopp[i][1]==0)||(placeopp[i][0]==3&&placeopp[i][1]==1)||(placeopp[i][0]==3&&placeopp[i][1]==2)||(placeopp[i][0]==4&&placeopp[i][1]==0)||(placeopp[i][0]==4&&placeopp[i][1]==1))
				{	
					inside[i]=1;
				}
			}
			for(i=0;i<19;i++)
			{
				if((placeopp[i][0]==0&&placeopp[i][1]==0)||(placeopp[i][0]==0&&placeopp[i][1]==1)||(placeopp[i][00]==0&&placeopp[i][1]==2)||(placeopp[i][0]==0&&placeopp[i][1]==3)||(placeopp[i][0]==0&&placeopp[i][1]==4)||(placeopp[i][0]==1&&placeopp[i][1]==0)||(placeopp[i][0]==1&&placeopp[i][1]==1)||(placeopp[i][0]==1&&placeopp[i][1]==2)||(placeopp[i][0]==1&&placeopp[i][1]==3)||(placeopp[i][0]==1&&placeopp[i][1]==4)||(placeopp[i][0]==2&&placeopp[i][1]==0)||(placeopp[i][0]==2&&placeopp[i][1]==1)||(placeopp[i][0]==2&&placeopp[i][1]==2)||(placeopp[i][0]==2&&placeopp[i][1]==3)||(placeopp[i][0]==3&&placeopp[i][1]==0)||(placeopp[i][0]==3&&placeopp[i][1]==1)||(placeopp[i][0]==3&&placeopp[i][1]==2)||(placeopp[i][0]==4&&placeopp[i][1]==0)||(placeopp[i][0]==4&&placeopp[i][1]==1))
				{	
					return 1;

				}
			}
			
		}
		else if((color.equals("BLACK") && turn==1))
		{
			for(i=0;i<19;i++)
			{
				if((place[i][0]==0&&place[i][1]==0)||(place[i][0]==0&&place[i][1]==1)||(place[i][00]==0&&place[i][1]==2)||(place[i][0]==0&&place[i][1]==3)||(place[i][0]==0&&place[i][1]==4)||(place[i][0]==1&&place[i][1]==0)||(place[i][0]==1&&place[i][1]==1)||(place[i][0]==1&&place[i][1]==2)||(place[i][0]==1&&place[i][1]==3)||(place[i][0]==1&&place[i][1]==4)||(place[i][0]==2&&place[i][1]==0)||(place[i][0]==2&&place[i][1]==1)||(place[i][0]==2&&place[i][1]==2)||(place[i][0]==2&&place[i][1]==3)||(place[i][0]==3&&place[i][1]==0)||(place[i][0]==3&&place[i][1]==1)||(place[i][0]==3&&place[i][1]==2)||(place[i][0]==4&&place[i][1]==0)||(place[i][0]==4&&place[i][1]==1))
				{	
					inside[i]=1;
				}
			}
			for(i=0;i<19;i++)
			{
				if((place[i][0]==0&&place[i][1]==0)||(place[i][0]==0&&place[i][1]==1)||(place[i][00]==0&&place[i][1]==2)||(place[i][0]==0&&place[i][1]==3)||(place[i][0]==0&&place[i][1]==4)||(place[i][0]==1&&place[i][1]==0)||(place[i][0]==1&&place[i][1]==1)||(place[i][0]==1&&place[i][1]==2)||(place[i][0]==1&&place[i][1]==3)||(place[i][0]==1&&place[i][1]==4)||(place[i][0]==2&&place[i][1]==0)||(place[i][0]==2&&place[i][1]==1)||(place[i][0]==2&&place[i][1]==2)||(place[i][0]==2&&place[i][1]==3)||(place[i][0]==3&&place[i][1]==0)||(place[i][0]==3&&place[i][1]==1)||(place[i][0]==3&&place[i][1]==2)||(place[i][0]==4&&place[i][1]==0)||(place[i][0]==4&&place[i][1]==1))
				{	
					return 1;

				}
			}
			
		}
		
		return 0;
	}
	public static int evaluate()
	{	int sum=0,i,j;
		ArrayList<Point> heya = new ArrayList<Point>(); 
		if(no>=14)
		{	
			if(color.equals("WHITE"))
			{
				for(i=0;i<5;i++)
				{
					for(j=0;j<5;j++)
					{
						if(boardini[i][j]==0 && (i==0 || i==1) && j<5)
						{	
							heya.add(new Point(i,j));
						}
						else if(boardini[i][j]==0 && i==2 && j<4)
						{	
							heya.add(new Point(i,j));
						}
						else if(boardini[i][j]==0 && i==3 && j<3)
						{	
							heya.add(new Point(i,j));
						}
						else if(boardini[i][j]==0 && i==4 && j<2)
						{	
							heya.add(new Point(i,j));
						}
					}
				}
				for(i=0;i<19;i++)
				{	
					if(checkoutsidepoint(placetrue[i][0],placetrue[i][1])==0)
					{	
						for(j=0;j<heya.size();j++)
						{	
							sum=sum+distanceend(place[i][0],place[i][1],heya.get(j));
						}
					}
				}
				return sum;
			}
			else if(color.equals("BLACK"))
			{
				for(i=15;i>10;i--)
				{
					for(j=15;j>10;j--)
					{
						if(boardini[i][j]==0 && (i==15 || i==14) && j>10)
						{
							heya.add(new Point(i,j));
						}
						else if(boardini[i][j]==0 && i==13 && j>11)
						{
							heya.add(new Point(i,j));
						}
						else if(boardini[i][j]==0 && i==12 && j>12)
						{
							heya.add(new Point(i,j));
						}
						else if(boardini[i][j]==0 && i==11 && j>13)
						{
							heya.add(new Point(i,j));
						}
					}
				}
				for(i=0;i<19;i++)
				{	
					if(checkoutsidepoint(placetrue[i][0],placetrue[i][1])==0)
					{	
						for(j=0;j<heya.size();j++)
						{
							sum=sum+distanceend(place[i][0],place[i][1],heya.get(j));
						}
					}
				}
			return sum;
			}
			return sum;
		}
		if(color.equals("WHITE"))
		{
			for(i=0;i<19;i++)
			{
				
				sum = sum + distance(place[i][0],place[i][1]);
				sum = sum - distanceb(placeopp[i][0],placeopp[i][1]);
			}
		}
		else
		{
			for(i=0;i<19;i++)
			{
				
					sum = sum + distanceb(place[i][0],place[i][1]);
					sum = sum - distance(placeopp[i][0],placeopp[i][1]);
			}
		}
		return sum;
	}
	public static int distanceend(int x,int y,Point f)
	{
		int a,b,dist;		
		a=f.x;
		b=f.y;
		dist=Math.max(Math.abs(x-a),Math.abs(y-b));
	
		return dist;
	}
	public static int distance(int x, int y)
	{
		int dist=0,targetx=0, targety=0,a,b;
		a=x;
		b=y;
		while(x!=targetx && y!=targety)
		{
			dist++;
			x--;
			y--;
		}
		if(x!=targetx)
		{
			dist+=x;
		}
		if(y!=targety)
		{
			dist+=y;
		}
		return dist;
	}
	public static int distanceb(int x, int y)
	{
		int dist=0,targetx=15, targety=15,a,b;
		a=x;
		b=y;
		while(x!=targetx && y!=targety)
		{	
			dist++;
			x++;
			y++;
		}
		if(x!=targetx)
		{
			dist=dist+targetx-x;
			
		}
		if(y!=targety)
		{
			dist=dist+targety-y;
			
		}
		return dist;
	}
	public static Point returnBestMove()
	{
		int MIN=9999,i,j,a=-1,b=-1;
		int best1=-1,best2=-1;
		if(no==18)
		{
			for(i=0;i<children.size();i++)
			{
				for(j=0;j<children.get(i).size();j++)
				{
					if(checkoutsidepoint(children.get(i).get(j).x,children.get(i).get(j).y)==0)
					{
						if(checkoutsidepoint(children.get(i).get(j).point.x,children.get(i).get(j).point.y)==1)
						{
							if(MIN>children.get(i).get(j).score)
							{
								MIN=children.get(i).get(j).score;
								PointsAndScore p=children.get(i).get(j);
								best1=i;
								best2=j;
								ps1=p.x;
								ps2=p.y;
								break;
							}
						}
						else if(checkinsidepoint(children.get(i).get(j).point.x,children.get(i).get(j).point.y)==0)
						{
							if(MIN>children.get(i).get(j).score)
							{
								MIN=children.get(i).get(j).score;
								PointsAndScore p=children.get(i).get(j);
								best1=i;
								best2=j;
								ps1=p.x;
								ps2=p.y;
							}
						}
					}
				}
			}
		return children.get(best1).get(best2).point;
		}
		
		if(checkinside(1)==1)
		{	
			for(i=0;i<children.size();i++)
			{
				for(j=0;j<children.get(i).size();j++)
				{   
					if(checkinsidepoint(children.get(i).get(j).x,children.get(i).get(j).y)==1)
					{	
						if(MIN>children.get(i).get(j).score && checkinsidepoint(children.get(i).get(j).point.x,children.get(i).get(j).point.y)==0)
						{	
								
								MIN=children.get(i).get(j).score;
								PointsAndScore p=children.get(i).get(j);
								best1=i;
								best2=j;
								ps1=p.x;
								ps2=p.y;
							
						}
					}
				}
			}
			if(MIN==9999)
			{
				for(i=0;i<children.size();i++)
				{
					for(j=0;j<children.get(i).size();j++)
					{	if(checkinsidepoint(children.get(i).get(j).x,children.get(i).get(j).y)==1)
						{
							if(MIN>children.get(i).get(j).score && checkinsidepoint(children.get(i).get(j).point.x,children.get(i).get(j).point.y)==1)
							{	
								if((color.equals("WHITE") && children.get(i).get(j).point.x<=children.get(i).get(j).x && children.get(i).get(j).point.y<=children.get(i).get(j).y)||(color.equals("BLACK") && children.get(i).get(j).point.x>=children.get(i).get(j).x && children.get(i).get(j).point.y>=children.get(i).get(j).y))
								{	
									MIN=children.get(i).get(j).score;
									PointsAndScore p=children.get(i).get(j);
									best1=i;
									best2=j;
									ps1=p.x;
									ps2=p.y;
								}
							}
						}
					}
				}
			}
			if(MIN==9999)
			{
				for(i=0;i<children.size();i++)
				{
					for(j=0;j<children.get(i).size();j++)
					{	if(checkinsidepoint(children.get(i).get(j).x,children.get(i).get(j).y)==0)
						{
							if(MIN>children.get(i).get(j).score && checkinsidepoint(children.get(i).get(j).point.x,children.get(i).get(j).point.y)==0)
							{	
								//if((color.equals("WHITE") && children.get(i).get(j).point.x<=children.get(i).get(j).x && children.get(i).get(j).point.y<=children.get(i).get(j).y)||(color.equals("BLACK") && children.get(i).get(j).point.x>=children.get(i).get(j).x && children.get(i).get(j).point.y>=children.get(i).get(j).y))
								//{	
									MIN=children.get(i).get(j).score;
									PointsAndScore p=children.get(i).get(j);
									best1=i;
									best2=j;
									ps1=p.x;
									ps2=p.y;
								//}
							}
						}
					}
				}
			}

		}
		else 
		{
			for(i=0;i<children.size();i++)
			{
				for(j=0;j<children.get(i).size();j++)
				{
					if(checkoutsidepoint(children.get(i).get(j).x,children.get(i).get(j).y)==0)
					{
						if(checkoutsidepoint(children.get(i).get(j).point.x,children.get(i).get(j).point.y)==1)
						{
							if(MIN>children.get(i).get(j).score)
							{
								MIN=children.get(i).get(j).score;
								PointsAndScore p=children.get(i).get(j);
								best1=i;
								best2=j;
								ps1=p.x;
								ps2=p.y;
								//break;
							}
						}
						else if(checkinsidepoint(children.get(i).get(j).point.x,children.get(i).get(j).point.y)==0)
						{
							if(MIN>children.get(i).get(j).score)
							{
								MIN=children.get(i).get(j).score;
								PointsAndScore p=children.get(i).get(j);
								best1=i;
								best2=j;
								ps1=p.x;
								ps2=p.y;
							}
						}
					}
					else if(checkoutsidepoint(children.get(i).get(j).x,children.get(i).get(j).y)==1)
					{
						if(checkoutsidepoint(children.get(i).get(j).point.x,children.get(i).get(j).point.y)==1)
						{
							if(MIN>children.get(i).get(j).score)
							{
								MIN=children.get(i).get(j).score;
								PointsAndScore p=children.get(i).get(j);
								best1=i;
								best2=j;
								ps1=p.x;
								ps2=p.y;
							}
						}	
					}
				}
			}
			
		}
		if(MIN==9999)
		{
			for(i=0;i<children.size();i++)
			{
				for(j=0;j<children.get(i).size();j++)
				{
					if(checkinsidepoint(children.get(i).get(j).point.x,children.get(i).get(j).point.y)==0)
					{
						if(checkinsidepoint(children.get(i).get(j).point.x,children.get(i).get(j).point.y)==0)
						{
							if(MIN>children.get(i).get(j).score)
							{
								MIN=children.get(i).get(j).score;
								PointsAndScore p=children.get(i).get(j);
								best1=i;
								best2=j;
								ps1=p.x;
								ps2=p.y;
							}
						}
					}
				}
			}
		}
		start=best1;

		return children.get(best1).get(best2).point;
	} 
	public static void writefile(int x,int y,Point p,int abc,int [][] done,int [][] jumping)
	{	// x and y are start points, p is the destination point, abc is jump or step, jump=1 and step=0
		//System.out.println("In write file"+x+","+y+" "+p);
		int a,b,i,j,k,l;
		a=p.x;
		b=p.y;
		done[x][y]=1;
		if(abc==0)
		{
			for(i=-1;i<2;i++)
			{
				for(j=-1;j<2;j++)
				{
					if(i!=0 || j!=0)
					{
						if((x+i)>=0 && (y+j)>=0 && (x+i)<16 && (y+j)<16)
						{	
							if(done[x+i][y+j]==0 && board[x+i][y+j]==0)
							{
								
								parent1[x+i][y+j]=x;
								parent2[x+i][y+j]=y;
								done[x+i][y+j]=1;
								jumping[x+i][y+j]=0;
								if((x+i==a) && (y+j==b))
									printpath(ps1,ps2,a,b,jumping);
							}
						}
					}
				}
			}
		}
		for(i=-1;i<2;i++)
		{
			for(j=-1;j<2;j++)
			{
				if(i!=0 || j!=0)
				{	
					if((x+2*i)>=0 && (y+2*j)>=0 && (x+2*i)<16 && (y+2*j)<16)
					{	
						if((done[x+2*i][y+2*j]==0) && board[x+i][y+j]!=0 && board[x+2*i][y+2*j]==0)
						{
							parent1[x+2*i][y+2*j]=x;
							parent2[x+2*i][y+2*j]=y;
							jumping[x+2*i][y+2*j]=1;
							if((x+2*i)==a && (y+2*j==b))
								printpath(ps1,ps2,a,b,jumping);
							//abc = 1;
							done[x+2*i][y+2*j]=1;
							writefile(x+2*i,y+2*j,p,1,done,jumping);
						}
					}
				}
			}
		}
		


	}
	public static void printpath(int x,int y,int a,int b, int [][] jumping)
	{
		try{
		FileWriter file1 = new FileWriter("output.txt");
		PrintWriter p1 = new PrintWriter(file1);
		ArrayList<Point> abc = new ArrayList<Point>();
		int i=0,c,d;
		abc.add(new Point(a,b));
		if(jumping[a][b]==1)
		{
			while(parent1[a][b]!=999 || parent2[a][b]!=999)
			{	
				
				c=parent1[a][b];
				d=parent2[a][b];
				abc.add(new Point(c,d));
				a=c;
				b=d;
				
			}
			
			for(i=abc.size()-1;i>0;i--)
			{
				p1.println("J "+abc.get(i).y+","+abc.get(i).x+" "+abc.get(i-1).y+","+abc.get(i-1).x);
			}
		}
		else
		{
			p1.println("E "+y+","+x+" "+b+","+a);
		}
		file1.close();
		//endtime = System.nanoTime();
		//long totalTime = (endtime - starttime)/1000000;
		//System.out.println(totalTime);
		System.exit(0);
		}
		catch(IOException e)
		{
			System.out.println("ERROR");
		}

		
	}
	public static void single()
	{
		int check=0,a,aa=-1,b,mainindex=-1;
		if(color.equals("WHITE"))
			check=checkinsidesingle();
		else if(color.equals("BLACK"))
			check = checkinsidesingleb();
		ArrayList<ArrayList<Point>> shanay = new ArrayList<ArrayList<Point>>();
		shanay = search(check);
		selectwho(shanay);
	}

	public static int checkinsidesingle()
	{
		int i,j,k;
		for(i=0;i<19;i++)
		{
			if((place[i][0]==15&&place[i][1]==15)||(place[i][0]==15&&place[i][1]==14)||(place[i][00]==15&&place[i][1]==13)||(place[i][0]==15&&place[i][1]==12)||(place[i][0]==15&&place[i][1]==11)||(place[i][0]==14&&place[i][1]==15)||(place[i][0]==14&&place[i][1]==14)||(place[i][0]==14&&place[i][1]==13)||(place[i][0]==14&&place[i][1]==12)||(place[i][0]==14&&place[i][1]==11)||(place[i][0]==13&&place[i][1]==15)||(place[i][0]==13&&place[i][1]==14)||(place[i][0]==13&&place[i][1]==13)||(place[i][0]==13&&place[i][1]==12)||(place[i][0]==12&&place[i][1]==15)||(place[i][0]==12&&place[i][1]==14)||(place[i][0]==12&&place[i][1]==13)||(place[i][0]==11&&place[i][1]==15)||(place[i][0]==11&&place[i][1]==14))
				inside[i]=1;
		}
		for(i=0;i<19;i++)
		{
			if((place[i][0]==15&&place[i][1]==15)||(place[i][0]==15&&place[i][1]==14)||(place[i][0]==15&&place[i][1]==13)||(place[i][0]==15&&place[i][1]==12)||(place[i][0]==15&&place[i][1]==11)||(place[i][0]==14&&place[i][1]==15)||(place[i][0]==14&&place[i][1]==14)||(place[i][0]==14&&place[i][1]==13)||(place[i][0]==14&&place[i][1]==12)||(place[i][0]==14&&place[i][1]==11)||(place[i][0]==13&&place[i][1]==15)||(place[i][0]==13&&place[i][1]==14)||(place[i][0]==13&&place[i][1]==13)||(place[i][0]==13&&place[i][1]==12)||(place[i][0]==12&&place[i][1]==15)||(place[i][0]==12&&place[i][1]==14)||(place[i][0]==12&&place[i][1]==13)||(place[i][0]==11&&place[i][1]==15)||(place[i][0]==11&&place[i][1]==14))
				return 1;
		}
		return 0;
	}
	public static int checkinsidesingleb()
	{
		int i,j,k;
		for(i=0;i<19;i++)
		{
			if((place[i][0]==0&&place[i][1]==0)||(place[i][0]==0&&place[i][1]==1)||(place[i][00]==0&&place[i][1]==2)||(place[i][0]==0&&place[i][1]==3)||(place[i][0]==0&&place[i][1]==4)||(place[i][0]==1&&place[i][1]==0)||(place[i][0]==1&&place[i][1]==1)||(place[i][0]==1&&place[i][1]==2)||(place[i][0]==1&&place[i][1]==3)||(place[i][0]==1&&place[i][1]==4)||(place[i][0]==2&&place[i][1]==0)||(place[i][0]==2&&place[i][1]==1)||(place[i][0]==2&&place[i][1]==2)||(place[i][0]==2&&place[i][1]==3)||(place[i][0]==3&&place[i][1]==0)||(place[i][0]==3&&place[i][1]==1)||(place[i][0]==3&&place[i][1]==2)||(place[i][0]==4&&place[i][1]==0)||(place[i][0]==4&&place[i][1]==1))
				inside[i]=1;
		}
		for(i=0;i<19;i++)
		{
			if((place[i][0]==0&&place[i][1]==0)||(place[i][0]==0&&place[i][1]==1)||(place[i][00]==0&&place[i][1]==2)||(place[i][0]==0&&place[i][1]==3)||(place[i][0]==0&&place[i][1]==4)||(place[i][0]==1&&place[i][1]==0)||(place[i][0]==1&&place[i][1]==1)||(place[i][0]==1&&place[i][1]==2)||(place[i][0]==1&&place[i][1]==3)||(place[i][0]==1&&place[i][1]==4)||(place[i][0]==2&&place[i][1]==0)||(place[i][0]==2&&place[i][1]==1)||(place[i][0]==2&&place[i][1]==2)||(place[i][0]==2&&place[i][1]==3)||(place[i][0]==3&&place[i][1]==0)||(place[i][0]==3&&place[i][1]==1)||(place[i][0]==3&&place[i][1]==2)||(place[i][0]==4&&place[i][1]==0)||(place[i][0]==4&&place[i][1]==1))
				return 1;
		}
		return 0;
	}
	public static int checkinsidepoint(int x,int y)
	{	if(color.equals("WHITE"))
		{
			if((x==15&&y==15)||(x==15&&y==14)||(x==15&&y==13)||(x==15&&y==12)||(x==15&&y==11)||(x==14&&y==15)||(x==14&&y==14)||(x==14&&y==13)||(x==14&&y==12)||(x==14&&y==11)||(x==13&&y==15)||(x==13&&y==14)||(x==13&&y==13)||(x==13&&y==12)||(x==12&&y==15)||(x==12&&y==14)||(x==12&&y==13)||(x==11&&y==15)||(x==11&&y==14))
				return 1;
		}
		else if(color.equals("BLACK"))
		{
			if((x==0&&y==0)||(x==0&&y==1)||(x==0&&y==2)||(x==0&&y==3)||(x==0&&y==4)||(x==1&&y==0)||(x==1&&y==1)||(x==1&&y==2)||(x==1&&y==3)||(x==1&&y==4)||(x==2&&y==0)||(x==2&&y==1)||(x==2&&y==2)||(x==2&&y==3)||(x==3&&y==0)||(x==3&&y==1)||(x==3&&y==2)||(x==4&&y==0)||(x==4&&y==1))
				return 1;	
		}
		return 0;
	}
	public static int checkoutsidepoint(int x,int y)
	{
		if(color.equals("BLACK"))
		{
			if((x==15&&y==15)||(x==15&&y==14)||(x==15&&y==13)||(x==15&&y==12)||(x==15&&y==11)||(x==14&&y==15)||(x==14&&y==14)||(x==14&&y==13)||(x==14&&y==12)||(x==14&&y==11)||(x==13&&y==15)||(x==13&&y==14)||(x==13&&y==13)||(x==13&&y==12)||(x==12&&y==15)||(x==12&&y==14)||(x==12&&y==13)||(x==11&&y==15)||(x==11&&y==14))
				return 1;
		}
		else if(color.equals("WHITE"))
		{
			if((x==0&&y==0)||(x==0&&y==1)||(x==0&&y==2)||(x==0&&y==3)||(x==0&&y==4)||(x==1&&y==0)||(x==1&&y==1)||(x==1&&y==2)||(x==1&&y==3)||(x==1&&y==4)||(x==2&&y==0)||(x==2&&y==1)||(x==2&&y==2)||(x==2&&y==3)||(x==3&&y==0)||(x==3&&y==1)||(x==3&&y==2)||(x==4&&y==0)||(x==4&&y==1))
				return 1;	
		}
		return 0;
	}
	public static int checkoutside()
	{
		int i,j,k;
		for(i=0;i<19;i++)
		{
			if((place[i][0]==0&&place[i][1]==0)||(place[i][0]==0&&place[i][1]==1)||(place[i][00]==0&&place[i][1]==2)||(place[i][0]==0&&place[i][1]==3)||(place[i][0]==0&&place[i][1]==4)||(place[i][0]==1&&place[i][1]==0)||(place[i][0]==1&&place[i][1]==1)||(place[i][0]==1&&place[i][1]==2)||(place[i][0]==1&&place[i][1]==3)||(place[i][0]==1&&place[i][1]==4)||(place[i][0]==2&&place[i][1]==0)||(place[i][0]==2&&place[i][1]==1)||(place[i][0]==2&&place[i][1]==2)||(place[i][0]==2&&place[i][1]==3)||(place[i][0]==3&&place[i][1]==0)||(place[i][0]==3&&place[i][1]==1)||(place[i][0]==3&&place[i][1]==2)||(place[i][0]==4&&place[i][1]==0)||(place[i][0]==4&&place[i][1]==1))
				outside[i]=1;
		}
		for(i=0;i<19;i++)
		{
			if((place[i][0]==0&&place[i][1]==0)||(place[i][0]==0&&place[i][1]==1)||(place[i][00]==0&&place[i][1]==2)||(place[i][0]==0&&place[i][1]==3)||(place[i][0]==0&&place[i][1]==4)||(place[i][0]==1&&place[i][1]==0)||(place[i][0]==1&&place[i][1]==1)||(place[i][0]==1&&place[i][1]==2)||(place[i][0]==1&&place[i][1]==3)||(place[i][0]==1&&place[i][1]==4)||(place[i][0]==2&&place[i][1]==0)||(place[i][0]==2&&place[i][1]==1)||(place[i][0]==2&&place[i][1]==2)||(place[i][0]==2&&place[i][1]==3)||(place[i][0]==3&&place[i][1]==0)||(place[i][0]==3&&place[i][1]==1)||(place[i][0]==3&&place[i][1]==2)||(place[i][0]==4&&place[i][1]==0)||(place[i][0]==4&&place[i][1]==1))
				return 1;
		}
		return 0;
	}
	public static int checkoutsideb()
	{
		int i,j,k;
		for(i=0;i<19;i++)
		{
			if((place[i][0]==15&&place[i][1]==15)||(place[i][0]==15&&place[i][1]==14)||(place[i][00]==15&&place[i][1]==13)||(place[i][0]==15&&place[i][1]==12)||(place[i][0]==15&&place[i][1]==11)||(place[i][0]==14&&place[i][1]==15)||(place[i][0]==14&&place[i][1]==14)||(place[i][0]==14&&place[i][1]==13)||(place[i][0]==14&&place[i][1]==12)||(place[i][0]==14&&place[i][1]==11)||(place[i][0]==13&&place[i][1]==15)||(place[i][0]==13&&place[i][1]==14)||(place[i][0]==13&&place[i][1]==13)||(place[i][0]==13&&place[i][1]==12)||(place[i][0]==12&&place[i][1]==15)||(place[i][0]==12&&place[i][1]==14)||(place[i][0]==12&&place[i][1]==13)||(place[i][0]==11&&place[i][1]==15)||(place[i][0]==11&&place[i][1]==14))
				outside[i]=1;
		}
		for(i=0;i<19;i++)
		{
			if((place[i][0]==15&&place[i][1]==15)||(place[i][0]==15&&place[i][1]==14)||(place[i][0]==15&&place[i][1]==13)||(place[i][0]==15&&place[i][1]==12)||(place[i][0]==15&&place[i][1]==11)||(place[i][0]==14&&place[i][1]==15)||(place[i][0]==14&&place[i][1]==14)||(place[i][0]==14&&place[i][1]==13)||(place[i][0]==14&&place[i][1]==12)||(place[i][0]==14&&place[i][1]==11)||(place[i][0]==13&&place[i][1]==15)||(place[i][0]==13&&place[i][1]==14)||(place[i][0]==13&&place[i][1]==13)||(place[i][0]==13&&place[i][1]==12)||(place[i][0]==12&&place[i][1]==15)||(place[i][0]==12&&place[i][1]==14)||(place[i][0]==12&&place[i][1]==13)||(place[i][0]==11&&place[i][1]==15)||(place[i][0]==11&&place[i][1]==14))
				return 1;
		}
		return 0;
	}
	public static ArrayList<ArrayList<Point>> search(int check)
	{	
		ArrayList<ArrayList<Point>> available = new ArrayList<ArrayList<Point>>();
		
			for(int i=0;i<19;i++)
			{
				ArrayList <Point> ava = new ArrayList<>();
				ArrayList <Point> ava1 = new ArrayList<>();
				int [][] done=new int [16][16];
				ava1 = allpointssingle(place[i][0],place[i][1],0,ava,done,board);
				available.add(ava1);
			}
	

		return available;
	}
	public static ArrayList<Point> allpointssingle(int x,int y,int abc, ArrayList<Point> ava,int [][] done,int [][] board)
	{	
		
		int again=0,i,j;
		done[x][y]=1;
		
		for(i=-1;i<2;i++)
		{
			for(j=-1;j<2;j++)
			{
				if(i!=0 || j!=0)
				{	
					if((x+2*i)>=0 && (y+2*j)>=0 && (x+2*i)<16 && (y+2*j)<16)
					{	
						if((done[x+2*i][y+2*j]==0) && board[x+i][y+j]!=0 && board[x+2*i][y+2*j]==0)
						{
							ava.add(new Point(x+2*i,y+2*j));
							again = 1;
							done[x+2*i][y+2*j]=1;
							allpointssingle(x+2*i,y+2*j,1,ava,done,board);
						}
					}
				}
			}
		}
		if(abc==0)
		{
			for(i=-1;i<2;i++)
			{
				for(j=-1;j<2;j++)
				{
					if(i!=0 || j!=0)
					{
						if((x+i)>=0 && (y+j)>=0 && (x+i)<16 && (y+j)<16)
						{	
							if(done[x+i][y+j]==0 && board[x+i][y+j]==0)
							{
								ava.add(new Point(x+i,y+j));
								done[x+i][y+j]=1;
							}
						}
					}
				}
			}
		}
		return ava;
	}
	public static void selectwho(ArrayList<ArrayList<Point>> shanay)
	{
		int check=0,checko=0,i,j,k,l,m,n;
		int [][] done = new int [16][16];
		int [][] jumping = new int [16][16];
		if(color.equals("WHITE"))
		{
			check=checkinsidesingle();
			checko=checkoutside();
		}
		else if(color.equals("BLACK"))
		{
			check=checkinsidesingleb();
			checko=checkoutsideb();
		}
		if(check==1)
		{
			for(i=0;i<shanay.size();i++)
			{	p1=place[i][0];
				p2=place[i][1];
				if(inside[i]==1)
				{
					for(j=0;j<shanay.get(i).size();j++)
					{
						if(checkinsidepoint(shanay.get(i).get(j).x,shanay.get(i).get(j).y)==0)
						{	
							parent1[p1][p2] = 999;
							parent2[p1][p2] = 999;
							writefilesingle(place[i][0],place[i][1],shanay.get(i).get(j),0,done,jumping);
						}
					}


				}
			}
			for(i=0;i<shanay.size();i++)
			{	p1=place[i][0];
				p2=place[i][1];
				if(inside[i]==1)
				{
					for(j=0;j<shanay.get(i).size();j++)
					{
						if(checkinsidepoint(shanay.get(i).get(j).x,shanay.get(i).get(j).y)==1)
						{	if(color.equals("WHITE"))
							{	if(shanay.get(i).get(j).x<=place[i][0] && shanay.get(i).get(j).y<=place[i][1])
								{	
									parent1[p1][p2] = 999;
									parent2[p1][p2] = 999;
									writefilesingle(place[i][0],place[i][1],shanay.get(i).get(j),0,done,jumping);
								}
							}
							else if(color.equals("BLACK"))
							{	if(shanay.get(i).get(j).x>=place[i][0] && shanay.get(i).get(j).y>=place[i][1])
								{	
									parent1[p1][p2] = 999;
									parent2[p1][p2] = 999;
									writefilesingle(place[i][0],place[i][1],shanay.get(i).get(j),0,done,jumping);
								}
							}
						}
					}
				}
			}
			for(i=0;i<shanay.size();i++)
			{	p1=place[i][0];
				p2=place[i][1];
				if(inside[i]==0)
				{
					for(j=0;j<shanay.get(i).size();j++)
					{
						if(checkinsidepoint(shanay.get(i).get(j).x,shanay.get(i).get(j).y)==0)
						{	if(color.equals("WHITE"))
							{	if(shanay.get(i).get(j).x<=place[i][0] && shanay.get(i).get(j).y<=place[i][1])
								{
									parent1[p1][p2] = 999;
									parent2[p1][p2] = 999;
									writefilesingle(place[i][0],place[i][1],shanay.get(i).get(j),0,done,jumping);
								}
							}
							else if(color.equals("BLACK"))
							{	if(shanay.get(i).get(j).x>=place[i][0] && shanay.get(i).get(j).y>=place[i][1])
								{	
									parent1[p1][p2] = 999;
									parent2[p1][p2] = 999;
									writefilesingle(place[i][0],place[i][1],shanay.get(i).get(j),0,done,jumping);
								}
							}
						}
					}
				}
			}
		}
		else if(check==0)
		{
			for(i=0;i<shanay.size();i++)
			{
				p1=place[i][0];
				p2=place[i][1];
				for(j=0;j<shanay.get(i).size();j++)
				{
					if(checkoutsidepoint(place[i][0],place[i][1])==0)
					{
						if(checkinsidepoint(shanay.get(i).get(j).x,shanay.get(i).get(j).y)==0)
						{									
							parent1[p1][p2] = 999;
							parent2[p1][p2] = 999;
							writefilesingle(place[i][0],place[i][1],shanay.get(i).get(j),0,done,jumping);
						}
					}
					
				}
			}
			for(i=0;i<shanay.size();i++)
			{
				p1=place[i][0];
				p2=place[i][1];
				for(j=0;j<shanay.get(i).size();j++)
				{
					if(checkoutsidepoint(place[i][0],place[i][1])==1)
					{
						if(checkoutsidepoint(shanay.get(i).get(j).x,shanay.get(i).get(j).y)==1)
						{	
							parent1[p1][p2] = 999;
							parent2[p1][p2] = 999;
							writefilesingle(place[i][0],place[i][1],shanay.get(i).get(j),0,done,jumping);
						}
					}
				}
			}
		}
	}
	public static void writefilesingle(int x,int y,Point p,int abc,int [][] done,int [][] jumping)
	{	// x and y are start points, p is the destination point, abc is jump or step, jump=1 and step=0
		int a,b,i,j,k,l;
		a=p.x;
		b=p.y;
		done[x][y]=1;
		
		for(i=-1;i<2;i++)
		{
			for(j=-1;j<2;j++)
			{
				if(i!=0 || j!=0)
				{	
					if((x+2*i)>=0 && (y+2*j)>=0 && (x+2*i)<16 && (y+2*j)<16)
					{	
						if((done[x+2*i][y+2*j]==0) && board[x+i][y+j]!=0 && board[x+2*i][y+2*j]==0)
						{
							parent1[x+2*i][y+2*j]=x;
							parent2[x+2*i][y+2*j]=y;
							jumping[x+2*i][y+2*j]=1;
							if((x+2*i)==a && (y+2*j==b))
								printpathsingle(p1,p2,a,b,jumping);
							abc = 1;
							done[x+2*i][y+2*j]=1;
							writefilesingle(x+2*i,y+2*j,p,1,done,jumping);
						}
					}
				}
			}
		}
		if(abc==0)
		{
			for(i=-1;i<2;i++)
			{
				for(j=-1;j<2;j++)
				{
					if(i!=0 || j!=0)
					{
						if((x+i)>=0 && (y+j)>=0 && (x+i)<16 && (y+j)<16)
						{	
							if(done[x+i][y+j]==0 && board[x+i][y+j]==0)
							{
								parent1[x+i][y+j]=x;
								parent2[x+i][y+j]=y;
								done[x+i][y+j]=1;
								jumping[x+i][y+j]=0;
								if((x+i==a) && (y+j==b))
									printpathsingle(p1,p2,a,b,jumping);
							}
						}
					}
				}
			}
		}


	}
	public static void printpathsingle(int x,int y,int a,int b, int [][] jumping)
	{
		try{
		FileWriter file1 = new FileWriter("output.txt");
		PrintWriter p1 = new PrintWriter(file1);
		ArrayList<Point> abc = new ArrayList<Point>();
		int i=0,c,d;
		abc.add(new Point(a,b));
		if(jumping[a][b]==1)
		{
			while(parent1[a][b]!=999 || parent2[a][b]!=999)
			{	
				c=parent1[a][b];
				d=parent2[a][b];
				abc.add(new Point(c,d));
				a=c;
				b=d;
				
			}
			for(i=abc.size()-1;i>0;i--)
			{
				p1.println("J "+abc.get(i).y+","+abc.get(i).x+" "+abc.get(i-1).y+","+abc.get(i-1).x);
			}
		}
		else
		{
			p1.println("E "+y+","+x+" "+b+","+a);
		}
		file1.close();
		System.exit(0);
		}
		catch(IOException e)
		{
			System.out.println("ERROR");
		}

		
	}
}
