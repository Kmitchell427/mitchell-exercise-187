/**Author: Keven Mitchell
 * Course: CPT187
 * Purpose: simulates a sod order, SodOrder supporting class that writes orders to a file
 * Createdate: 4/14/20
 */


package edu.cpt187.mitchell.participation;

import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;

public class OrderWriter 
{
	private final boolean FOUND = true;
	private final boolean NOT_FOUND = false;
	private boolean fileFoundFlag = false;
	private String masterFileName = "";
	private int recordCount = 0;
	
	public OrderWriter(String borrowedFileName)
	{
		masterFileName = borrowedFileName;
	}
	
	public void setWriteOrder(int borrowedItemID, String borrowedItemName, double borrowedItemPrice, int borrowedQuantity, double borrowedOrderCost)
	{
		fileFoundFlag = NOT_FOUND;
		try
		{
			PrintWriter filePW = new PrintWriter(new FileWriter(masterFileName, true));
			filePW.printf("%n%d\t%s\t%f\t%d\t%f%n", borrowedItemID, borrowedItemName, borrowedItemPrice, borrowedQuantity, borrowedOrderCost);
			fileFoundFlag = FOUND;
			recordCount++;
			filePW.close();
		}
		catch(IOException ex)
		{
		}
	}//End setWriteOrder
	
	public boolean getFileFoundFlag()
	{
		return fileFoundFlag;
	}//End getFileFound
	
	public String getFileName()
	{
		return masterFileName;
	}//End getFileName
	
	public int getRecordCount()
	{
		return recordCount;
	}//End getRecordCount
	
}//End class
