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
// let 0,1,2,3 represent operator up, down, left and right respectively
// root node has -1 as the operator
class solution1 {
    static int movecount=0;
    static char[][] goalState={{'1','2','3'},{'4','5','6'},{'7','8','*'}};
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
        Node child = new Node(parent,op,parent.depth+1,childs,parent.pathcost+1);
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
    public boolean notInClosed(Stack<Node> closed, Node c)
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
    public boolean dfs(Stack<Node> open, Stack<Node> closed)
    {
        while(!open.empty())
        {
            Node current = open.pop();
            if(!notInClosed(closed,current))
                    break;
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
                    open.push(c);
            }

            // moving * up
            if(pos[0]>0)
            {
                Node c = move(current,0,pos);
                if(notInClosed(closed,c))
                    open.push(c);
            }

            // moving * right
            if(pos[1]<2){
                Node c = move(current,3,pos);
                if(notInClosed(closed,c))
                    open.push(c);
            }

 
            // moving * down
            if(pos[0]<2)
            {
                Node c = move(current,1,pos);
                if(notInClosed(closed,c))
                    open.push(c);
            }             
            closed.push(current);
        }
        return false;
    }
    //Iterative Deepening Search
    public int searchIterative(Stack<Node> open, Stack<Node> closed, int level)
    {
        boolean flag = true;
        System.out.println("At level = "+level);
        while(!open.empty())
        {
            Node current = open.pop();
            if(!notInClosed(closed,current))
                break;
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
                return 1;// 1 means goal found
            }
            printState(current.state);
            System.out.println();
            if(current.depth==level)
            {
                flag=false;
                continue;
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
                    open.push(c);
            }
            // moving * up
            if(pos[0]>0)
            {
                Node c = move(current,0,pos);
                if(notInClosed(closed,c))
                    open.push(c);
            }
            // moving * right
            if(pos[1]<2){
                Node c = move(current,3,pos);
                if(notInClosed(closed,c))
                    open.push(c);
            }
            // moving * down
            if(pos[0]<2)
            {
                Node c = move(current,1,pos);
                if(notInClosed(closed,c))
                    open.push(c);
            } 
            closed.push(current);
        }
        if(flag)
        {
            return 2;//2 means failure
        }
        else
        {
            return 0;// 0 means this level search completed but there are nodes to be explored still
        }
    }

    public boolean iterativeDeepening(Node root)
    {
        movecount=0;
        int level =0;
        while(true)
        {
            Stack<Node> open2 = new Stack<Node>();
            Stack<Node> closed2 = new Stack<Node>();
            open2.push(root);
            int res = searchIterative(open2,closed2,level);
            if(res!=0)
            {

                if(res==1)// Let action 4 represent failure
                    return true;
                else 
                    return false;
 
            }
            level++;  
        }
    }


    public static void main(String args[] ) throws Exception 
    {
        char[][] boardPosition = new char[3][3];
        Scanner s = new Scanner(System.in);
        System.out.println("Please enter the 9 input, one for each tile position");
        for(int i=0;i<3;i++)
        {
           for(int j=0;j<3;j++)
           {
                System.out.print("Enter the input for "+i+"th row and "+j+"th column: ");
                boardPosition[i][j]=s.next().charAt(0);
           }
           System.out.println();
        }
        Node root= new Node(null, -1,0,boardPosition,0);
        Stack<Node> open = new Stack<Node>();
        Stack<Node> closed = new Stack<Node>();
        open.push(root);
        solution1 sol = new solution1();
        System.out.println("Input:");
        sol.printState(root.state);
        System.out.println();
        System.out.println("1) Depth-first search");
        if(sol.dfs(open,closed)==false)
        System.out.println("Please enter a valid input");
        System.out.println();
        System.out.println("        ----******----      ");
        System.out.println();
        System.out.println("2) Iterative deepening search");
        if(sol.iterativeDeepening(root)==false)
        System.out.println("Please enter a valid input");
        System.out.println();
        System.out.println("        ----******----      ");
        System.out.println();
        System.out.println("3) A* search using a suitable heuristic");
        Astar star = new Astar();
        if(star.AstarSearch(boardPosition)==false)
        System.out.println("Please enter a valid input");
        System.out.println();
        System.out.println("        ----******----      ");
        System.out.println();

    }
}
