/**Author: Keven Mitchell
 * Course: CPT187
 * Purpose: user account handling, supporting class 
 * Createdate: 4/14/20
 */

package edu.cpt187.mitchell.participation;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.FileWriter;

public class UserAccounts 
{
	private final boolean FILE_FOUND = true;
	private final boolean FILE_NOT_FOUND = false;
	private final int NOT_FOUND = -1;
	private final int RESET_VALUE = 0;
	private final int MAXIMUM_RECORDS = 50;
	
	private String[] userNames = new String[MAXIMUM_RECORDS];
	private String[] passwords = new String[MAXIMUM_RECORDS];
	private boolean fileFoundFlag = false;
	private String masterFileName = "";
	private int recordCount = 0;
	private int searchedIndex = 0;
	
	public UserAccounts(String borrowedFileName)
	{
		masterFileName = borrowedFileName;
	}//end of constructor
	
	public void setUserAccountArrays()
	{
		try 
		{
			Scanner inFile = new Scanner(new FileInputStream(masterFileName));
			while(recordCount < MAXIMUM_RECORDS && inFile.hasNext() == true)
			{
				userNames[recordCount] = inFile.next();
				passwords[recordCount] = inFile.next();
				recordCount++;
			}//End while to iterate through the file
			inFile.close();
			fileFoundFlag = FILE_FOUND;
		}//end try
		catch(IOException ex)
		{
			fileFoundFlag = FILE_NOT_FOUND;
			recordCount = RESET_VALUE;
		}//End catch
	}//end setUserAccountArrays
	
	public void setSearchedIndex(String borrowedUserName)
	{
		searchedIndex = getSeqSearch(borrowedUserName);
	}//End setSearchedIndex
	
	public void setSearchedIndex(String borrowedUserName, String borrowedPassword)
	{
		searchedIndex = getSeqSearch(borrowedUserName);
	}//End setSearchedIndex overloaded
	
	public void setWriteOneRecord(String borrowedUserName, String borrowedPassword)
	{
		try
		{
			PrintWriter outFile = new PrintWriter(new FileWriter(masterFileName, true));
			//instantiates a new printwriter to the masterFile
			outFile.printf("%n%s\t%s%n", borrowedUserName, borrowedPassword);
			//prints to the masterfile and closes the PrintWriter
			outFile.close();
			fileFoundFlag = FILE_FOUND;
		}//end try
		catch(IOException ex)
		{
			fileFoundFlag = FILE_NOT_FOUND;
		}
	}//End setWriteOneRecord
	
	public int getSeqSearch(String borrowedBorrowedUserName)
	{
		int localIndex = 0;
		boolean localFound = false;
		while(localIndex < userNames.length && localFound == false)
		{
			if(borrowedBorrowedUserName.equals(userNames[localIndex]))
			{
				localFound = FILE_FOUND;
			}//End if the username matches an array element, exits the loop with localIndex as the index of the found username
			else
			{
				localIndex++;				
			}//End else, 
		}//End while to iterate through and search for a matching username
		if(localFound == false)
		{
			localIndex = NOT_FOUND;
		}//only return the index if the search succeeds
		return localIndex;			
	}//End getSeqSearch
	
	public boolean getPasswordMatch(String borrowedBorrowedPassword)
	{
		boolean localMatch = false;
//		if(fileFoundFlag != localMatch)
//		{
			localMatch = borrowedBorrowedPassword.equals(passwords[searchedIndex]);
//		}//End if to catch file not found on login attempt
		return localMatch;
	}//End getPasswordMatch
	
	public String getFileFoundFlag()
	{
		return String.valueOf(fileFoundFlag);
	}//End getFileFoundFlag
	
	public String getFileName()
	{
		return masterFileName;
	}//End getFileName
	
	public int getRecordCount()
	{
		return recordCount;
	}//End getRecordCount
	
	public int getSearchedIndex()
	{
		return searchedIndex;
	}//End getSearchedIndex
}//End class
