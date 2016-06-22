//////////////////////////////////////////////////////////////////////////////////////////
// Author			:       Sreedevi Koppula
// Email			:       sreedevikoppula@my.unt.edu
// Program			:       B+ Tree Implementation
// File Name        :		BPTree.java
//							This file contains the methods that operate on the B+ Tree structure
// Arguments		: 		None
// Last modified	:		03/23/2016
//////////////////////////////////////////////////////////////////////////////////////////
package bPlusTree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BPTree {
	private int nodeSize;
	private Node rootNode;

	public BPTree(int nodeSize)
	{
		this.nodeSize = nodeSize;
		rootNode = new Node(nodeSize);
		rootNode.setNodeType("root");
	}
	
	protected Node getRootNode() {
		return rootNode;
	}

	protected void setRootNode(Node rootNode) {
		this.rootNode = rootNode;
	}

	protected int getNodeSize() {
		return nodeSize;
	}
	
	//inserting search key value into the B+ tree
	public void insert(int key,String recordValue)
	{
		int pos = 0;
		int value =0;
		Node siblingNode;
		Node parentNode;
		
		//If the node is not full and if the node is a root node
		//insert the key into the node
		if(!checkIfNodeIsFull(rootNode) && rootNode.getHasChild() == false)
		{
			pos = getProperIndexFromNode(rootNode, key);
			rootNode.addSearchKey(pos, key, null, null);
			rootNode.setNumOfKeysFilled(rootNode.getNumOfKeysFilled()+1);
		}
		//if root node is full and it has no child
		//create a new sibling node and split the values across the nodes
		//the split criteria is that each leaf node must have keys in number between ceil of (n-1)/2 and (n-1)
		//where 'n' is the number of pointers in a node
		//create a new parent node and mark it as root
		//insert the first value of 'sibling' node into the parent node
		else if(rootNode.getHasChild() == false && checkIfNodeIsFull(rootNode))
		{
			parentNode = new Node(nodeSize);
			parentNode.setHasChild(true);
			parentNode.setNodeType("root");
			
			siblingNode = new Node(nodeSize);
			siblingNode.setParentNode(parentNode);
			siblingNode.setLeftSiblingNode(rootNode);
			siblingNode.setNodeType("leaf");
			
			rootNode.setRightSiblingNode(siblingNode);
			rootNode.setParentNode(parentNode);
			rootNode.setNodeType("leaf");
			
			value = splitValuesBetweenLeafNodes(rootNode, siblingNode, key);
			parentNode.addSearchKey(0, value, rootNode, siblingNode);
			parentNode.setNumOfKeysFilled(parentNode.getNumOfKeysFilled()+1);
			
			this.rootNode = parentNode;
			
		}
		//if the root node is full and if it has child nodes
		else if(rootNode.getHasChild() == true)
		{
			SearchKey s;
			Node n = rootNode;
			//traverse to the proper child node to insert the search key
			//after this while loop, 'n' holds the proper node, into which value is to be inserted
			while(n.getHasChild() != false)
			{
				pos = getProperIndexFromNode(n, key);
				
				if(pos == n.getNumOfKeysFilled())
				{
					//n = rootNode.getSearchKeys().get(pos-1).getRightNode();
					n = n.getSearchKeys().get(pos-1).getRightNode();
				}
				else
				{
					//n= rootNode.getSearchKeys().get(pos).getLeftNode();
					n= n.getSearchKeys().get(pos).getLeftNode();
				}
				//s = n.getSearchKeys().get(pos);
			}
			//check if the node is full
			//if it is not full, insert the key into the node.
			if(!checkIfNodeIsFull(n))
			{
				pos = getProperIndexFromNode(n, key);
				n.addSearchKey(pos, key, null, null);
				n.setNumOfKeysFilled(n.getNumOfKeysFilled()+1);
			}
			//if it is full, create a new sibling node and split the values across the nodes
			//get the first value of the sibling node 
			//this first value should be inserted into its parent node
			else
			{
				Node tempNode = null;
				int newValue = 0;
				siblingNode = new Node(nodeSize);
				siblingNode.setNodeType("leaf");
				siblingNode.setRightSiblingNode(n.getRightSiblingNode());
				siblingNode.setParentNode(n.getParentNode());
				siblingNode.setLeftSiblingNode(n);
				n.setRightSiblingNode(siblingNode);
				
				value = splitValuesBetweenLeafNodes(n, siblingNode, key);
				Node node = n.getParentNode();
				//check if the parent node is full
				//if its not full, insert the value into parent node
				if(!checkIfNodeIsFull(node)){
					pos = getProperIndexFromNode(node, value);
					node.addSearchKey(pos, value, n, siblingNode);
					node.setNumOfKeysFilled(node.getNumOfKeysFilled()+1);
					
					Node link = node.getSearchKeys().get(0).getLeftNode();
					for(int i=0;i<node.getNumOfKeysFilled();i++)
					{
						//node.getSearchKeys().get(i).setLeftNode(link);
						node.getSearchKeys().get(i).setRightNode(link.getRightSiblingNode());
						if(i!=node.getNumOfKeysFilled()-1)
						{
							node.getSearchKeys().get(i+1).setLeftNode(link.getRightSiblingNode());
						}	
						link = link.getRightSiblingNode();
						//Node ln = node.getSearchKeys().get(0).getLeftNode();
					}
				}
				else
				{
					Boolean flag = true;
					//if the parent node is full, split the parent node as will recursively
					//this procedure is repeated, until a parent node with free space is found
					while(checkIfNodeIsFull(node))
					{
						//n.setParentNode(node.getSearchKeys().get(node.getNumOfKeysFilled()-1).getRightNode().getRightSiblingNode());
						//siblingNode.setParentNode(n.getParentNode());
						siblingNode = new Node(nodeSize);
						node.setRightSiblingNode(siblingNode);
						node.setNodeType("inner");
						node.setHasChild(true);
						siblingNode.setNodeType("inner");
						siblingNode.setParentNode(node.getParentNode());
						siblingNode.setLeftSiblingNode(node);
						siblingNode.setHasChild(true);
						
						/*n.setParentNode(siblingNode);
						if(n.getRightSiblingNode() != null)
						{
							n.getRightSiblingNode().setParentNode(siblingNode);
						}*/
						
						n=n.getParentNode();
						value = splitValuesBetweenInnerNodes(node, siblingNode, value);
						tempNode = node;
						//node = node.getParentNode();
						
						if(node.getParentNode() != null)
						{
							node = node.getParentNode();
							//this.rootNode = node;
						}
						else
						{
							node = new Node(nodeSize);
							node.setNodeType("root");
							node.setHasChild(true);
							this.rootNode = node;
						}	
						siblingNode.setParentNode(node);
						tempNode.setParentNode(node);
					}
					//insert the value into the free parent node
					if(!checkIfNodeIsFull(node) && flag == true)
					{
						pos = getProperIndexFromNode(node, value);
						node.addSearchKey(pos, value, tempNode, siblingNode);
						node.setNumOfKeysFilled(node.getNumOfKeysFilled()+1);
					}
				}
			}
		}
		Node n = rootNode;
		//after this while loop, 'n' holds the proper node, into which value is to be inserted
		while(n.getHasChild() != false)
		{
			pos = getProperIndexFromNode(n, key);
			
			if(pos == n.getNumOfKeysFilled())
			{
				//n = rootNode.getSearchKeys().get(pos-1).getRightNode();
				n = n.getSearchKeys().get(pos-1).getRightNode();
			}
			else
			{
				//n= rootNode.getSearchKeys().get(pos).getLeftNode();
				n= n.getSearchKeys().get(pos).getLeftNode();
			}
			//s = n.getSearchKeys().get(pos);
		}
		//associate the record's values with the search keys in all the leaf nodes
		pos = getProperIndexFromNode(n, key);
		if(pos == n.getNumOfKeysFilled())
		{
			
		}
		else
		{
			n.getSearchKeys().get(pos).value = recordValue;
		}
	}
	
	//Method to split the values between the intermediate nodes, when a node is full
	private int splitValuesBetweenInnerNodes(Node currentNode,Node newNode,int key)
	{
		int value = 0;
		
		ArrayList<Integer> allSearchKeys = new ArrayList<Integer>();
		allSearchKeys.add(key);
		for(SearchKey k:currentNode.getSearchKeys())
		{
			allSearchKeys.add(k.getKeyValue());
		}
		/*for(SearchKey k:newNode.getSearchKeys())
		{
			allSearchKeys.add(k.getKeyValue());
		}*/
		
		Collections.sort(allSearchKeys);
		//int midValue = (int)Math.ceil(allSearchKeys.size()/2.0);
		int midValue = allSearchKeys.size()/2;
		value = allSearchKeys.get(midValue);
		int z = 0;
		for(int k: allSearchKeys)
		{
			if(k< value)
			{
				currentNode.getSearchKeys().get(z).setKeyValue(k);
				currentNode.getSearchKeys().get(z).setLeftNode(currentNode.getSearchKeys().get(z).getLeftNode());
				currentNode.getSearchKeys().get(z).setRightNode(currentNode.getSearchKeys().get(z).getLeftNode().getRightSiblingNode());
				z++;
			}
			else
			{
				try
				{
					currentNode.getSearchKeys().remove(z);
					currentNode.setNumOfKeysFilled(currentNode.getNumOfKeysFilled()-1);
				}
				catch(Exception e)
				{
					
				}	
			}
		}
		z=0;
		for(int k: allSearchKeys)
		{
			if(k> value)
			{
				try
				{
					if(z==0)
					{
						newNode.addSearchKey(z, k, currentNode.getSearchKeys().get(currentNode.getNumOfKeysFilled()-1).getRightNode().getRightSiblingNode(), currentNode.getSearchKeys().get(currentNode.getNumOfKeysFilled()-1).getRightNode().getRightSiblingNode().getRightSiblingNode());
						currentNode.getSearchKeys().get(currentNode.getNumOfKeysFilled()-1).getRightNode().getRightSiblingNode().setParentNode(newNode);
						currentNode.getSearchKeys().get(currentNode.getNumOfKeysFilled()-1).getRightNode().getRightSiblingNode().getRightSiblingNode().setParentNode(newNode);
					}
					else
					{
						newNode.addSearchKey(z, k, newNode.getSearchKeys().get(z-1).getRightNode(), newNode.getSearchKeys().get(z-1).getRightNode().getRightSiblingNode());
						newNode.getSearchKeys().get(z-1).getRightNode().setParentNode(newNode);
						newNode.getSearchKeys().get(z-1).getRightNode().getRightSiblingNode().setParentNode(newNode);
						
					}
					newNode.setNumOfKeysFilled(newNode.getNumOfKeysFilled()+1);
					z++;
				}
				catch(Exception e)
				{
					System.out.println("There is an exception");
				}	
			}
			else
			{

			}
		}
		return value;
	}
	
	
	//Method to split the values between the leaf nodes,when a node is full
	private int splitValuesBetweenLeafNodes(Node currentNode,Node newNode,int key)
	{
		int value = 0;
		int pos = 0;
		int num = 0;
		
		//====================================
		ArrayList list = new ArrayList();
		for(SearchKey k:currentNode.getSearchKeys())
		{
			list.add(k.getKeyValue());
		}
		list.add(key);
		Collections.sort(list);
		
		int midValue = (int) Math.ceil(nodeSize/2.0);
		for(int i=0;i<midValue;i++){
			currentNode.getSearchKeys().get(i).setKeyValue((int) list.get(i));
		}
		
		//====================================
		//int midValue = (int) Math.ceil(currentNode.getNumOfKeysFilled()/2.0);
		
		//int midValue = (int) Math.ceil(nodeSize/2.0); -- 1st
		
		//Copying half of the values from current node to new sibling node
		//for(int i=midValue;i<currentNode.getNumOfKeysFilled();i++) //--- 4th
		for(int i=midValue;i<=nodeSize;i++)
		{
			//num = currentNode.getSearchKeys().get(i).getKeyValue(); -- 2nd
			num = (int)list.get(i);
			pos = getProperIndexFromNode(newNode, num);
			newNode.addSearchKey(pos, num, null, null); //the left and right children fields are null, as these are leaf nodes
			newNode.setNumOfKeysFilled(newNode.getNumOfKeysFilled()+1);
			
		}
		
		//removing the values which are copied from the current node.
		for(int i=currentNode.getNumOfKeysFilled()-1;i>=midValue;i--)
		//for(int i=midValue;i<currentNode.getNumOfKeysFilled();i++)
		{
			currentNode.removeSearchKey(i);
			currentNode.setNumOfKeysFilled(currentNode.getNumOfKeysFilled()-1);
		}
		
		//find the node, into which the value to be inserted fits
		//if(key < currentNode.getParentNode().getSearchKeys().get(currentNode.getParentNode().getNumOfKeysFilled()-1).getKeyValue())
		/*if(key < newNode.getSearchKeys().get(0).getKeyValue())
		{
			//insert the key into proper position in the proper node
			pos = getProperIndexFromNode(currentNode, key);
			currentNode.addSearchKey(pos, key, null, null);
			currentNode.setNumOfKeysFilled(currentNode.getNumOfKeysFilled()+1);
		}
		else
		{
			//insert the key into proper position in the proper node
			pos = getProperIndexFromNode(newNode, key);
			newNode.addSearchKey(pos, key, null, null);
			newNode.setNumOfKeysFilled(newNode.getNumOfKeysFilled()+1);
		}
		3rd*/
		
		
		/*pos = getProperIndexFromNode(newNode, key);
		newNode.addSearchKey(pos, key, null, null);
		newNode.setNumOfKeysFilled(newNode.getNumOfKeysFilled()+1);
		*/
		value = newNode.getSearchKeys().get(0).getKeyValue();
		
		return value;
	}
	
	//Method to get an index of a node, at which a key has to be inserted
	private int getProperIndexFromNode(Node node, int key) {
		int pos = 0;
		
		for(int i=0;i<node.getNumOfKeysFilled();i++)
		{
			if(node.getSearchKeys().get(i).getKeyValue() < key)
			{
				pos = i+1;
				continue;
			}
			else
			{
				pos = i;
				break;
			}
		}
		return pos;
	}
	
	//Method to print a tree details. The details are returned as a string by the method
	public String printTree() {
		StringBuilder sb = new StringBuilder();
		
		String output = "";
		int level = 0;
		SearchKey previous = null;
		Node n = getRootNode();
		Queue<Node> queue = new LinkedList<Node>();
		Queue<Node> tempQueue = new LinkedList<Node>();
		int count = 0;
		queue.add(n);
		int i=0;
		int j=0;
		while(!queue.isEmpty())
		{
			n = queue.remove();
			if(n!=null)
			{
				if(n.getNodeType() == "root")
				{
					System.out.print("[Root node ]: ");
					sb.append("[Root node ]: ");
				}
				else if(n.getNodeType() == "inner")
				{
					//System.out.print("Intermediate "+level+" "+i+" : ");
					System.out.print("[Intermediate level "+level+","+"node "+i+" ] : ");
					sb.append("[Intermediate level "+level+","+"node "+i+" ] : ");
					i++;
				}
				else if(n.getNodeType() == "leaf"){
					System.out.print("[Leaf node "+j+" ] : ");
					sb.append("[Leaf node "+j+" ] : ");
					count= count+n.getNumOfKeysFilled();;
					
					j++;
				}
				for(SearchKey k : n.getSearchKeys())
				{
					if(k!= null)
					{
						System.out.print(k.getKeyValue()+"  ");
						sb.append(k.getKeyValue()+"  ");
						tempQueue.add(k.getLeftNode());
						
						//queue.add(k.getLeftNode());
						previous = k;
					}
					else
					{
						
					}	
				}
				tempQueue.add(previous.getRightNode());
				//queue.add(previous.getRightNode());
				System.out.println();
				sb.append("\n");
				if(queue.isEmpty())
				{
					i=0;
					level = level+1;
					queue.addAll(tempQueue);
					tempQueue.removeAll(queue);
				}
				//System.out.println("<<<< count is: "+count+">>>>>>>");
			}
		}	
		return sb.toString();
	}
	
	//Method to check if a node is full
	private boolean checkIfNodeIsFull(Node n)
	{
		if(n.getNumOfKeysFilled() < nodeSize)
		{
			return false;
		}
		else
		{
			return true;
		}
	}

}
	