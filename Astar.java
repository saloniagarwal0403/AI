import java.io.InputStreamReader;
import java.util.*;
class Node
{
    Node parent;
    int action;
    int depth;
    char[][] state;
    int pathcost;
    Node(Node parent,int action,int depth,char[][] state,int pathcost)
    {
        this.parent=parent;
        this.action=action;
        this.depth=depth;
        this.state=state;
        this.pathcost=pathcost;
    }
}
class Astar{
	static int movecount=0;
    static char[][] goalState={{'1','2','3'},{'4','5','6'},{'7','8','*'}};
 
	//A* algorithm
    //Calculating the h* function for a given state
    public int hstarCalculation(char[][] state)
    {
        int res =0;
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++)
            {
                char t = state[i][j];
                switch(t)
                {
                    case '1':
                        res+=Math.abs(i-0)+Math.abs(j-0);
                        break;
                    case '2':
                        res+=Math.abs(i-0)+Math.abs(j-1);
                        break;
                    case '3':
                        res+=Math.abs(i-0)+Math.abs(j-2);
                        break;
                    case '4':
                        res+=Math.abs(i-1)+Math.abs(j-0);
                        break;
                    case '5':
                        res+=Math.abs(i-1)+Math.abs(j-1);
                        break;
                    case '6':
                        res+=Math.abs(i-1)+Math.abs(j-2);
                        break;
                    case '7':
                        res+=Math.abs(i-2)+Math.abs(j-0);
                        break;
                    case '8':
                        res+=Math.abs(i-2)+Math.abs(j-1);
                        break; 
                }
            }
        }
        return res;
    } 


    boolean compareStates(char[][] a, char[][] b)// This function compares two states and returns true if they are same
    {
        boolean f = true;
        for(int i=0;i<3;i++)
            for(int j=0;j<3;j++)
                if(!(a[i][j]==b[i][j]))
                {
                    f=false;
                    break;
                }
        return f;
    }
    void printState(char[][] c)
    {
        for(int i=0;i<3;i++)
        {
            for(int j=0;j<3;j++)
            {
                System.out.print(c[i][j]+"  ");
            }
            System.out.println();
        }
    }
    public Node move(Node parent, int op,int[] pos)// Let op represent the operator to be used
    {
        char[][] childs = new char[3][3];
        for(int i=0;i<3;i++)
        {
            for(int j=0;j<3;j++)
                childs[i][j]=parent.state[i][j];
        }
        Node child = new Node(parent,op,parent.depth+1,childs,parent.depth+1);
        char temp;
        switch(op)
        {
            case 0:
                temp =child.state[pos[0]][pos[1]];
                child.state[pos[0]][pos[1]]=child.state[pos[0]-1][pos[1]];
                child.state[pos[0]-1][pos[1]]=temp;
                break;
            case 1:
                temp =child.state[pos[0]][pos[1]];
                child.state[pos[0]][pos[1]]=child.state[pos[0]+1][pos[1]];
                child.state[pos[0]+1][pos[1]]=temp;
                break;
            case 2:
                temp =child.state[pos[0]][pos[1]];
                child.state[pos[0]][pos[1]]=child.state[pos[0]][pos[1]-1];
                child.state[pos[0]][pos[1]-1]=temp;
                break;
            case 3:
                temp =child.state[pos[0]][pos[1]];
                child.state[pos[0]][pos[1]]=child.state[pos[0]][pos[1]+1];
                child.state[pos[0]][pos[1]+1]=temp;
                break;
        }
        child.pathcost+=hstarCalculation(child.state);
        return child;

    }
    public void printp(Node current)
    {
        if(current.parent!=null)
        {
            movecount++;
            printp(current.parent);
        }
        printState(current.state);
        System.out.println();
    }
    public boolean notInClosed(List<Node> closed, Node c)
    {
        boolean f = true;
        for(Node n : closed)
        {
            if(compareStates(n.state,c.state))
            {
                f=false;
                break;
            }
       }
       return f;
    }
    public void updateopen(List<Node> open,Node n)
    {
    	boolean toBeAdded= true;
    	for(Node x: open)
    	{
    		if(compareStates(x.state,n.state))
    		{
    			toBeAdded=false;
    			if(x.pathcost>n.pathcost)
    			{
    				x=n;	
    			}
    		}
    	}
    	if(toBeAdded)
    		open.add(n);
    }
    public boolean AstarSearch(char[][] state)
    {
    	List<Node> closed = new ArrayList<Node>();
		List<Node> open = new ArrayList<Node>();
    	open.add(new Node(null,-1,0,state,hstarCalculation(state)));
    	while(!open.isEmpty())
    	{
    		Node current= new Node(null,0,0,null,0);
    		int mincost=Integer.MAX_VALUE;
    		for(Node n: open)
    		{
    			if(n.pathcost<mincost)
    			{
    				mincost=n.pathcost;
    				current=n;
    			}
    		}
    		open.remove(current);
    		boolean res =compareStates(current.state,goalState);
            if(res)
            {
                System.out.println("Output:");
                List<char[][]> l = new ArrayList<char[][]>();
                while(current!=null)
                {
                    l.add(current.state);
                    current=current.parent;
                    movecount++;
                } 
                int a = l.size()-1;
                while(a>=0)
                {
                    printState(l.get(a));
                    System.out.println();
                    a--;
                }
                System.out.println("Number of moves = "+(movecount-1));
                int enquedstate=0;
                for (Node w: closed){
                    enquedstate++;
                }
                System.out.println("Number of states enqueued = "+enquedstate);
                return res;
            }

    		int[] pos = new int[2];
            for(int i=0;i<3;i++)
                for(int j=0;j<3;j++)
                {
                    if(current.state[i][j]=='*')
                    {
                        pos[0]=i;
                        pos[1]=j;
                        break;
                    }
                }
            // moving * left          
            if(pos[1]>0){
                Node c = move(current,2,pos);
                if(notInClosed(closed,c))
                    updateopen(open,c);            
            }

    		// moving * up
            if(pos[0]>0)
            {
                Node c = move(current,0,pos);
                if(notInClosed(closed,c))
                    updateopen(open,c);
            }
            // moving * right
            if(pos[1]<2){
                Node c = move(current,3,pos);
                if(notInClosed(closed,c))
                    updateopen(open,c);
            }

            // moving * down
            if(pos[0]<2)
            {
                Node c = move(current,1,pos);
                if(notInClosed(closed,c))
                    updateopen(open,c);
            } 
            closed.add(current);
    	}
    	return false;
    }
}