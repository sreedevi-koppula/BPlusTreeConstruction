//////////////////////////////////////////////////////////////////////////////////////////
// Author			:       Sreedevi Koppula
// Email			:       sreedevikoppula@my.unt.edu
// Program			:       B+ Tree Implementation
// File Name        :		Main.java
// Arguments		: 		inputFile (i.e table.txt), outputFile (ex: out.txt)
//							Arguments are given as command line arguments
//							More details are in README file
// Last modified	:		03/23/2016
// Course			: 		CSCE 5350
// Semester:        : 		Spring 2016
// University		: 		University of North Texas
//////////////////////////////////////////////////////////////////////////////////////////
package bPlusTree;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class Main {
	public static void main(String args[])
	{
		//Specifying the path of input file as a command line argument 
		//this file will be indexed using B+ tree indexing
		String inputfilePath = args[0];
		
		//Specifying the path of output file as a command line argument 
		//this file will be the output of B+ tree indexing
		String outputfilePath = args[1];
		
		String line;
		
		//Constructing a B+ Tree object
		//Here the parameter '39' specifies the number of key values to be present in each node of the b+ tree
		BPTree tree = new BPTree(39);
		
		//reading each records from the file specified at the beginning. Here it is 'table.txt'
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(inputfilePath))));
			while((line = br.readLine()) != null)
			{
				String[] list = new String[2];
				list = line.split(",",2);
				
				//inserting each record's 'id' and rest of record's content into the b+ tree
				//'id' column is used as the search key value
				tree.insert(Integer.parseInt(list[0]),list[1]);		
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		//Reading the content of the tree and writing it to the output file specified in command line arg
		String treeContent = tree.printTree();
		try {
			PrintWriter pw = new PrintWriter(outputfilePath);
			pw.write(treeContent);
			pw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
