//////////////////////////////////////////////////////////////////////////////////////////
// Author			:       Sreedevi Koppula
// Email			:       sreedevikoppula@my.unt.edu
// Program			:       B+ Tree Implementation
// File Name        :		SearchKey.java
//							This file defines the structure of a search key that is 
//							to be inserted into the nodes
// Arguments		: 		None
// Last modified	:		03/23/2016
//////////////////////////////////////////////////////////////////////////////////////////
package bPlusTree;

public class SearchKey implements Comparable<SearchKey>{
	private int keyValue;
	private Node leftNode;
	public String value;
	private Node rightNode;
	
	public SearchKey()
	{
		
	}
	
	public SearchKey(int keyValue,Node leftNode,Node rightNode)
	{
		this.keyValue = keyValue;
		this.leftNode = leftNode;
		this.rightNode = rightNode;
	}
	
	protected int getKeyValue() {
		return keyValue;
	}
	protected void setKeyValue(int keyValue) {
		this.keyValue = keyValue;
	}
	protected Node getLeftNode() {
		return leftNode;
	}
	protected void setLeftNode(Node leftNode) {
		this.leftNode = leftNode;
	}
	protected Node getRightNode() {
		return rightNode;
	}
	protected void setRightNode(Node rightNode) {
		this.rightNode = rightNode;
	}

	@Override
	public int compareTo(SearchKey sk) {
		return this.getKeyValue()-sk.getKeyValue();
	}
	
}
