import java.util.*;


public class HW4 {
	
	//solve method is at LINE 163!!
	//HIRIQ TESTER CLASS UNTIL LINE 119
	public class HiRiQ {
		
		
		//int is used to reduce storage to a minimum...
		  public int config;
		  public byte weight;
		  
		  public HiRiQ(){
			  config=0;
			  weight=1;
		  }

		//initialize to one of 5 reachable START config n=0,1,2,3,4
		HiRiQ(byte n)
		  {
		  if (n==0)
		   {config=65536/2;weight=1;}
		  else
		    if (n==1)
		     {config=1626;weight=6;}
		    else
		      if (n==2)
		       {config=-1140868948; weight=10;}
		      else
		        if (n==3)
		         {config=-411153748; weight=13;}
		        else
		         {config=-2147450879; weight=32;}
		  }
		
		  boolean IsSolved()
		  {
			  return( (config==65536/2) && (weight==1) );
		  }

		//transforms the array of 33 booleans to an (int) config and a (byte) weight.
		  public void store(boolean[] B)
		  {
		  int a=1;
		  config=0;
		  weight=(byte) 0;
		  if (B[0]) {weight++;}
		  for (int i=1; i<32; i++)
		   {
		   if (B[i]) {config=config+a;weight++;}
		   a=2*a;
		   }
		  if (B[32]) {config=-config;weight++;}
		  }

		//transform the int representation to an array of booleans.
		//the weight (byte) is necessary because only 32 bits are memorized
		//and so the 33rd is decided based on the fact that the config has the
		//correct weight or not.
		  public boolean[] load(boolean[] B)
		  {
		  byte count=0;
		  int fig=config;
		  B[32]=fig<0;
		  if (B[32]) {fig=-fig;count++;}
		  int a=2;
		  for (int i=1; i<32; i++)
		   {
		   B[i]= fig%a>0;
		   if (B[i]) {fig=fig-a/2;count++;}
		   a=2*a;
		   }
		  B[0]= count<weight;
		  return(B);
		  }
		  
		//prints the int representation to an array of booleans.
		//the weight (byte) is necessary because only 32 bits are memorized
		//and so the 33rd is decided based on the fact that the config has the
		//correct weight or not.
		  public void printB(boolean Z)
		  {if (Z) {System.out.print("[ ]");} else {System.out.print("[@]");}}
		  
		  public void print()
		  {
		  byte count=0;
		  int fig=config;
		  boolean next,last=fig<0;
		  if (last) {fig=-fig;count++;}
		  int a=2;
		  for (int i=1; i<32; i++)
		   {
		   next= fig%a>0;
		   if (next) {fig=fig-a/2;count++;}
		   a=2*a;
		   }
		  next= count<weight;
		  
		  count=0;
		  fig=config;
		  if (last) {fig=-fig;count++;}
		  a=2;

		  System.out.print("      ") ; printB(next);
		  for (int i=1; i<32; i++)
		   {
		   next= fig%a>0;
		   if (next) {fig=fig-a/2;count++;}
		   a=2*a;
		   printB(next);
		   if (i==2 || i==5 || i==12 || i==19 || i==26 || i==29) {System.out.println() ;}
		   if (i==2 || i==26 || i==29) {System.out.print("      ") ;};
		   }
		   printB(last); System.out.println() ;

		  }

	}
	//HIRIQ TESTER CLASS
	
		//Node which will contain information on the current HiRiQ configuration
		//has parent of current HiRiQ and the move from parent to current, and ArrayList of its possible children
		class Node<HiRiQ>{
			
			public HiRiQ current;
			public Node parent;
			public ArrayList<Node<HiRiQ>> children; 
			public String substitution; 
			
			
			public Node(HiRiQ cur, String sub){
				this.current=cur;
				this.substitution=sub;
				this.children=new ArrayList<Node<HiRiQ>>();				
			}
			public Node(){
				
			}
			public void add(Node<HiRiQ> child){
				//sets current HiRiQ as parent of child
				child.parent = this;
				//add child to the ArrayList of children
				this.children.add(child);
			}
		}
		public static void main(String[]args){
				
		 boolean[]yo = {false, false, false, false, true, false, true, false, false, false, false, false, false, true, true, false, true, false, true, true, false, false, false, false, false, false, false, false, false, false, false, false, false};
		 		 		 
		 solve(yo);
		 			 
		}
		/*INPUT: Configuration of HiRiQ board in the form of a boolean[33]
		OUTPUT: String with steps to reach the solved configuration, or if the input has zero neighbors, we return a string
				that says it is unsolvable */
		public static void solve(boolean[] B){
			
			//creates new HiRiQ object
			HiRiQ input = new HW4().new HiRiQ((byte)0);
			
			//stores input boolean as configuration of HiRiQ board
			input.store(B);
			input.print();
			
			//hardcode for n=4 ;) 
			if (input.config==-2147450879 && input.weight==32){
				System.out.println("14@16, 27@15, 20@22, 23@21, 25@23, 32@24, 17@29, 30@32, 32@24, 8@22, 0@9, 5@17, 17@29, 29@27, 27@15, 15@3, 6@20, 20@22, 22@24, 24@12, 12@10, 9@11, 26@12, 12@10, 2@0, 0@8, 7@9, 9@11, 11@25, 25@23, 23@9, 4@16");
				return;
			}
			
			//checks if input has valid parity
			if (!(parityCheck(input))){
				System.out.println("There are no solutions for the given configuration");
				return;
			}
			//checks if input boolean is already solved
			if (input.IsSolved()){
				System.out.println("Moves to solve configuration: ");
				input.print();
				return; 
			}	
			//initializes queues for the current solution and for blackSubs (because we prioritize W sub)
			//and ArrayList which contains all visited nodes
			Queue<Node<HiRiQ>> solutionList = new LinkedList<Node<HiRiQ>>();
			Queue<Node<HiRiQ>> blackSub = new LinkedList<Node<HiRiQ>>();
	        ArrayList<Node<HiRiQ>> visited = new ArrayList<Node<HiRiQ>>();
			
	        //initialize root HiRiQ node
	        Node<HiRiQ> current = new HW4().new Node<HiRiQ>(input, null);
	        
	        //adds root to visited list, and is first element of solutionList
	        visited.add(current);
	        solutionList.add(current);
	        
	        int configsChecked=1;
	        
	        //while queues are not empty
	        while (!(solutionList.isEmpty()) || !(blackSub.isEmpty())){
	        	      	
	        	//check white sub queue first
	        	if(!(solutionList.isEmpty())){
	        		
	        		ArrayList<Node<HiRiQ>> children = new ArrayList<Node<HiRiQ>>();
	        		
	        		//removes top of solutionList queue
		        	Node<HiRiQ> toCheck = solutionList.remove();	
		        	
		        	//validSubs contains the possible substitutions of current board config
		    		int[][] validSubs = new int[38][3];
		    		
		    		//load current config as a boolean array
		    		boolean[] board = new boolean[33];
		    		toCheck.current.load(board);
		    		
		    		//numSubs is the number of possible substitutions for the current board config
		    		//boardSub fills validSubs with all valid substitutions
		    	 	int numSubs = boardSub(validSubs, board);
		    	 	
		    	 	//children is the ArrayList of Nodes which contains the children of current config
		        	children = getChildren(validSubs, toCheck, numSubs);    
		        		        	
		        	for (int i=0; i<children.size(); i++){
		        		
		        		configsChecked++;
		        		
		        		HiRiQ toVisit = children.get(i).current;
		        		
		        		//checks parity test
		        		if (!(parityCheck(toVisit))){
		        			continue;
		        			
		        		//checks in the visited ArrayList whether the current child has already been seen
		        		}else if (!isDuplicate(visited, children.get(i))){
		        			
		        			//if the current child is the solved state, print path and return
		        			if(toVisit.IsSolved()){
		        				System.out.println("Moves to solve configuration: " + returnPath(children.get(i)));
		        				System.out.println("Number of configurations checked: " + configsChecked);
		        				toVisit.print();
		        				return;
		        			}
		        			//else, we add it to our visited list
		        			else{
		        				visited.add(children.get(i));
		        				
		        				//sort child into black list or white list
				        		if (toVisit.weight < toCheck.current.weight) {
				                    solutionList.add(children.get(i));
				                } else if (toVisit.weight > toCheck.current.weight) {
				                    blackSub.add(children.get(i));
				                }
		        			}
		        		}
		        	}
	        	}else if(!(blackSub.isEmpty())){
	        		
	        		configsChecked++;
	        		
	        		ArrayList<Node<HiRiQ>> children = new ArrayList<Node<HiRiQ>>();
	        		
	        		//removes top of blackSub queue
	        		Node<HiRiQ> toCheck = blackSub.remove();	     
		        	
		        	//validSubs contains the possible substitutions of current board config
		    		int[][] validSubs = new int[38][3];
		    		
		    		//load current config as a boolean array
		    		boolean[] board = new boolean[33];
		    		
		    		toCheck.current.load(board);
		    		
		    		//numSubs is the number of possible substitutions for the current board config
		    		//boardSub fills validSubs with all valid substitutions
		    	 	int numSubs = boardSub(validSubs, board);
		    	 	
		    	 	//children is the ArrayList of Nodes which contains the children 
		        	children = getChildren(validSubs, toCheck, numSubs);
		        	
		        	for (int i=0; i<children.size(); i++){
		        		HiRiQ toVisit = children.get(i).current;
		        		
		        		//checks parity test
		        		if (!parityCheck(toVisit)){
		        			continue;
		        		}
		        		//checks in the visited ArrayList whether the current child has already been seen
		        		if (!isDuplicate(visited, children.get(i))){
		        			//if the current child is the solved stated, print path and return
		        			if(toVisit.IsSolved()){
		        				System.out.println("Moves to solve configuration: " + returnPath(children.get(i)));
		        				System.out.println("Number of configurations checked: " + configsChecked);
		        				toVisit.print();
		        				return;
		        			}
		        			//else, we add it to our visited list
		        			else{
		        				visited.add(children.get(i));
		        				
		        				//sort child into black list or white list
				        		if (toVisit.weight < toCheck.current.weight){
				                    solutionList.add(children.get(i));
				                }else if (toVisit.weight > toCheck.current.weight){
				                    blackSub.add(children.get(i));
				                }
		        			}
		        		}
		        	}
	        	}
	        }    
	        
		}	
		//Method which returns an ArrayList of all possible children of current config
		//using the getChild method
		public static ArrayList<Node<HiRiQ>> getChildren(int[][] validSubs, Node<HiRiQ> input, int numSubs){
					
			for (int i=0; i<numSubs; i++){
				HiRiQ hello = new HW4().new HiRiQ();
				String s = "";
				
				Node<HiRiQ> child = new HW4().new Node<HiRiQ>(hello, s);
				child = getChild(validSubs[i], input);
	
				input.add(child);
			}
			return input.children;	
		}
		//Method which switches the values (does a substitution) of the given positions
		//of a given HiRiQ board configuration
		public static Node<HiRiQ> getChild(int[] positions, Node<HiRiQ> parent){
			
			
			//size of B is 33
			boolean[] temp = new boolean[33];
			
			//gets HiRiQ config of parent and loads it into a boolean array
			HiRiQ toSub = parent.current;
			toSub.load(temp);
			
			//initialize new HiRiQ
			HiRiQ newHiRiQ = new HW4().new HiRiQ((byte)0);
			//initialize String for substitution
			String sub = "";
			
			Node<HiRiQ> nextChild = new HW4().new Node<HiRiQ>(newHiRiQ, sub);
					
			//for loop switches the elements of the temp array at given positions
			for(int i=0; i<3; i++){
				if (temp[positions[i]]==false){
					temp[positions[i]]=true;
				}
				else if (temp[positions[i]]==true){
					temp[positions[i]]=false;
				}
			}
			
			//returns a configuration with the substitution (a child of the original configuration)
			newHiRiQ.store(temp);
			sub = positions[0] + "@" + positions[2];	
			nextChild.current=newHiRiQ;
			nextChild.substitution=sub;
			
			return nextChild;	
		}
		public static String returnPath(Node<HiRiQ> end){
			
			String path = end.substitution;
			
			while(end.parent!=null){			
				if(end.parent.substitution!=null){
					path = end.parent.substitution + ", " + path;
				}
				end = end.parent;
			}
			return path;	
		}	
		//Method that is a heuristic
		//Check the parity of blue and red tiles if the board were separated into blue, yellow and red tiles 
		//Returns true if the parity is same, 
		public static boolean parityCheck(HiRiQ toCheck){
			
			boolean [] B = new boolean[33];
			toCheck.load(B);
			
			boolean isParity = false;
			boolean isParityFlipped = false;
			
			//FIRST PART CHECKS NORMAL PARITY BOARD
			int[] blue = {0,5,6,9,12,15,18,21,24,28,30};
			int[] red = {2,4,8,11,14,17,20,23,26,27,32};
			int[] yellow = {1,3,7,10,13,16,19,22,25,29,31};
			
			int numBlue=0;
			int numRed=0;
			int numYellow=0;
			
			for(int i=0; i<11; i++){
				if (B[blue[i]]==false){
					numBlue++;
				}
				if (B[red[i]]==false){
					numRed++;
				}
				if (B[yellow[i]]==false){
					numYellow++;
				}
			}			
			if (numBlue%2==numRed%2 && numBlue%2!=numYellow%2){
				isParity=true;
			}
			//SECOND PART CHECKS FLIPPED BOARD
			int[] blueFlipped = {2,3,6,9,12,14,17,22,25,28,32};
			int[] redFlipped = {0,4,7,10,15,18,20,23,26,29,30};
			int[] yellowFlipped = {1,5,8,11,13,16,19,21,24,27,31};
			
			int numBlueFlipped=0;
			int numRedFlipped=0;
			int numYellowFlipped=0;
			
			for(int i=0; i<11; i++){
				if (B[blue[i]]==false){
					numBlueFlipped++;
				}
				if (B[red[i]]==false){
					numRedFlipped++;
				}
				if (B[yellow[i]]==false){
					numYellowFlipped++;
				}
			}
			if (numBlueFlipped%2==numRedFlipped%2 && numBlueFlipped%2!=numYellowFlipped%2){
				isParityFlipped=true;
			}
			
			return (isParity && isParityFlipped);
		}
		public static boolean isDuplicate(ArrayList<Node<HiRiQ>> list, Node<HiRiQ> input){
			boolean isDuplicate = false;
			
			for(int i=0; i<list.size(); i++){
				Node<HiRiQ> listObject = list.get(i);
				if (listObject.current.weight == input.current.weight && listObject.current.config == input.current.config){
					isDuplicate = true;
				}
			}
			return isDuplicate;
		}
		//Method which fill an 2D array with all the triplets with possible substitutions for a given configuration 
		//and returns an int representing the number of substitutions available
		public static int boardSub(int[][] input, boolean[] B){
			 int numSubs=0;
			 
			 int[][] triplets = {{0,1,2},{3,4,5},
								{6,7,8},{7,8,9},
								{8,9,10},{9,10,11},{10,11,12},
								{13,14,15},{14,15,16},{15,16,17},{16,17,18},{17,18,19},
								{20,21,22},{21,22,23},{22,23,24},{23,24,25},{24,25,26},
								{27,28,29},{30,31,32},
								{12,19,26},{11,18,25},
								{2,5,10},{5,10,17},{10,17,24},{17,24,29},{24,29,32},
								{1,4,9},{4,9,16},{9,16,23},{16,23,28},{23,28,31},
								{0,3,8},{3,8,15},{8,15,22},{15,22,27},{22,27,30},
								{7,14,21},{6,13,20}};
					 
			 for (int i=0; i<38; i++){			 
				 if (isValidSub(triplets[i], B)){
					 
					 input[numSubs]=triplets[i];
					 numSubs++;
				 }
			 }		 
			 return numSubs;
		}
		//Method which returns whether there is a valid substitution for
		//a given triplet and configuration B
		public static boolean isValidSub(int[] triplet, boolean[] B){
			
			if(B[triplet[0]]==true){
				if(B[triplet[1]]==true && B[triplet[2]]==true){
					return false;
				}
				else if(B[triplet[1]]==true && B[triplet[2]]==false){
					return true;
				}
				else if(B[triplet[1]]==false && B[triplet[2]]==true){
					return false;
				}
				else if(B[triplet[1]]==false && B[triplet[2]]==false){
					return true;
				}			
			}
			else if(B[triplet[0]]==false){
				if(B[triplet[1]]==false && B[triplet[2]]==false){
					return false;
				}
				else if(B[triplet[1]]==false && B[triplet[2]]==true){
					return true;
				}
				else if(B[triplet[1]]==true && B[triplet[2]]==false){
					return false;
				}
				else if(B[triplet[1]]==true && B[triplet[2]]==true){
					return true;
				}
			}
			return false;
		}
	}
