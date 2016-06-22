//////////////////////////////////////////////////////////////////////////////////////////
// Author			:       Sreedevi Koppula
// Email			:       sreedevikoppula@my.unt.edu
// Program			:       B+ Tree Implementation
// File Name        :		Node.java   
//							This file denotes the structure of a node and 
//							methods to access the nodes
// Arguments		: 		None
// Last modified	:		03/23/2016
//////////////////////////////////////////////////////////////////////////////////////////
package bPlusTree;

import java.util.ArrayList;

public class Node {
	
	private String nodeType;
	private Boolean hasChild;
	private Node parentNode;
	private Node rightSiblingNode;
	private Node leftSiblingNode;
	private int numOfKeysFilled;
	private ArrayList<SearchKey>  searchKeys;
	
	public Node(int nodeSize) {
		searchKeys = new ArrayList<SearchKey>(nodeSize);
		setHasChild(false);
	}
	
	/*public Node()
	{
		setHasChild(false);
	}
	*/
	protected ArrayList<SearchKey> getSearchKeys() {
		return searchKeys;
	}	
	
	protected void addSearchKey(int pos,int key,Node left,Node right)
	{
		searchKeys.add(pos,new SearchKey(key,left,right));
	}
	
	protected void removeSearchKey(int index)
	{
		searchKeys.remove(index);
	}
	
	protected String getNodeType() {
		return nodeType;
	}

	protected void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	protected Node getParentNode() {
		return parentNode;
	}

	protected void setParentNode(Node parentNode) {
		this.parentNode = parentNode;
	}

	protected Node getRightSiblingNode() {
		return rightSiblingNode;
	}

	protected void setRightSiblingNode(Node rightSiblingNode) {
		this.rightSiblingNode = rightSiblingNode;
	}

	protected Node getLeftSiblingNode() {
		return leftSiblingNode;
	}

	protected void setLeftSiblingNode(Node leftSiblingNode) {
		this.leftSiblingNode = leftSiblingNode;
	}

	protected int getNumOfKeysFilled() {
		return numOfKeysFilled;
	}

	protected void setNumOfKeysFilled(int numOfKeysFilled) {
		this.numOfKeysFilled = numOfKeysFilled;
	}

	public Boolean getHasChild() {
		return hasChild;
	}

	public void setHasChild(Boolean hasChild) {
		this.hasChild = hasChild;
	}

	
}
