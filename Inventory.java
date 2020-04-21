/**Author: Keven Mitchell
 * Course: CPT187
 * Purpose: simulates a sod order, handles inventory
 * Createdate: 4/14/20
 */


package edu.cpt187.mitchell.participation;

import java.util.Scanner;
import java.util.Random;
import java.io.FileInputStream;
import java.io.IOException;

public class Inventory 
{
	private final String[] DISCOUNT_NAMES = {"Member","Senior","No Discount"};
	private final double[] DISCOUNT_RATES = {0.25,0.15,0.0};
	private final String[] PRIZE_NAMES = {"Free Hat", "Expired Coupon", "Bucket of Stuff"};
	private final int MAX_RECORDS = 5;
	private final int NOT_FOUND = -1;
	private final int RESET_VALUE = 0;
	private int[] itemIDs = new int[MAX_RECORDS];
	private String[] itemNames = new String[MAX_RECORDS];
	private double[] itemPrices = new double[MAX_RECORDS];
	private int[] orderQuantity = new int[MAX_RECORDS];
	private double[] orderTotal = new double[MAX_RECORDS];
	private int[] inStockCounts = new int[MAX_RECORDS];
	private int itemSearchIndex = 0;
	private int recordCount = 0;
	private Random prizeGenerator = new Random();
	
	//default no arg constructor
	public Inventory()
	{	
	}
	
	public void setReduceStock(int borrowedHowMany, int borrowedLastItemSelectedIndex)
	{
		inStockCounts[borrowedLastItemSelectedIndex] = inStockCounts[borrowedLastItemSelectedIndex] - borrowedHowMany;
		itemSearchIndex = RESET_VALUE;
	}//End setReduceStock
	
	public void setLoadItems(String borrowedFileName)
	{
		try 
		{
			recordCount = RESET_VALUE;
			Scanner inFile = new Scanner(new FileInputStream(borrowedFileName));
			while(inFile.hasNext() == true && recordCount < MAX_RECORDS)
			{
				itemIDs[recordCount] = inFile.nextInt();
				itemNames[recordCount] = inFile.next();
				itemPrices[recordCount] = inFile.nextDouble();
				inStockCounts[recordCount] = inFile.nextInt();
				recordCount++;
			}//End while loop to iterate through records until MAX_RECORDS is reached
			inFile.close();
			setBubbleSort();
		}
		catch(IOException exception)
		{
			recordCount = NOT_FOUND;
		}
	}//End setLoadItems
	
	public void setLoadItems(String borrowedFileName, int borrowedSize)
	{
		try 
		{
			recordCount = RESET_VALUE;
			Scanner inFile = new Scanner(new FileInputStream(borrowedFileName));
			while(inFile.hasNext() == true && recordCount < borrowedSize && recordCount < MAX_RECORDS)
			{
				itemIDs[recordCount] = inFile.nextInt();
				itemNames[recordCount] = inFile.next();
				itemPrices[recordCount] = inFile.nextDouble();
				orderQuantity[recordCount] = inFile.nextInt();
				orderTotal[recordCount] = inFile.nextDouble();
			}//End while loop to iterate through records until MAX_RECORDS is reached
			inFile.close();
			setBubbleSort();
		}
		catch(IOException exception)
		{
			recordCount = NOT_FOUND;
		}
	}//End setLoadItems overloaded
	
	public void setSearchIndex(int borrowedTargetID)
	{
		itemSearchIndex = getSearchResults(borrowedTargetID);
	}//End setSearchIndex
	
	public void setBubbleSort()
	{
		int localLast = recordCount - 1;
		int localIndex = 0;
		boolean localSwap = false;
		
		while(localLast > RESET_VALUE)
		{
			localIndex = RESET_VALUE;
			localSwap = false;
			while(localIndex < localLast)
			{
				if(itemIDs[localIndex] > itemIDs[localIndex + 1])
				{
					setSwapArrayElements(localIndex);
					localSwap = true;
				}//End if
				localIndex++;
			}//End while index<last
			if(localSwap == false)
			{
				localLast = RESET_VALUE;
			}//End if
			else
			{
				localLast = localLast - 1;
			}
		}//End while index<0
	}//End setBubbleSort
	
	public void setSwapArrayElements(int borrowedIndex)
	{
		int localInt = 0;
		String localString = "";
		double localDouble = 0.0;
		//holder for the elements at borrowedIndex
		//then copy the next value into the element at borrowedIndex, then change the next value to the previous value of the element at borrowedIndex
		localInt = itemIDs[borrowedIndex];
		itemIDs[borrowedIndex] = itemIDs[borrowedIndex + 1];
		itemIDs[borrowedIndex + 1] = localInt;

		localString = itemNames[borrowedIndex];
		itemNames[borrowedIndex] = itemNames[borrowedIndex + 1];
		itemNames[borrowedIndex + 1] = localString;

		localDouble = itemPrices[borrowedIndex];
		itemPrices[borrowedIndex] = itemPrices[borrowedIndex + 1];
		itemPrices[borrowedIndex + 1] = localDouble;

		localInt = inStockCounts[borrowedIndex];
		inStockCounts[borrowedIndex] = inStockCounts[borrowedIndex + 1];
		inStockCounts[borrowedIndex + 1] = localInt;
		
		localInt = orderQuantity[borrowedIndex];
		orderQuantity[borrowedIndex] = orderQuantity[borrowedIndex + 1];
		orderQuantity[borrowedIndex + 1] = localInt;
		
		localDouble = orderTotal[borrowedIndex];
		orderTotal[borrowedIndex] = orderTotal[borrowedIndex + 1];
		orderTotal[borrowedIndex + 1] = localDouble;
	}//End setSwapArrayElements
	
	public int[] getInStockCounts()
	{
		return inStockCounts;
	}//End getInStockCounts
	
	public int[] getItemIDs()
	{
		return itemIDs;
	}//End getItemIDs
	
	public String[] getItemNames()
	{
		return itemNames;
	}//End getItemNames
	
	public double[] getItemPrices()
	{
		return itemPrices;
	}//End getItemPrices
	
	public String[] getDiscountNames()
	{
		return DISCOUNT_NAMES;
	}//End getDiscountNames
	
	public double[] getDiscountRates()
	{
		return DISCOUNT_RATES;
	}//End getDiscountRates
	
	public int[] getOrderQuantities()
	{
		return orderQuantity;
	}//End getOrderQuantities
	
	public double[] getOrderTotals()
	{
		return orderTotal;
	}//End getOrderTotals
	
	public String[] getPrizeNames()
	{
		return PRIZE_NAMES;
	}//End getPrizeNames
	
	public int getRandomNumber()
	{
		return prizeGenerator.nextInt(PRIZE_NAMES.length);
	}//end getRandomNumber
	
	public int getMaxRecords()
	{
		return MAX_RECORDS;
	}//End getMaxRecords
	
	public int getItemSearchIndex()
	{
		return itemSearchIndex;
	}//end getItemSearchIndex
	
	public int getRecordCount()
	{
		return recordCount;
	}//End getRecordCount
	
	public int getSearchResults(int borrowedTargetID)
	{
		int localMid = 0;
		int localFirst = 0;
		int localLast = recordCount - 1;
		boolean localFound = false;
		//while for main search
		while(localFirst <= localLast && localFound == false)
		{
			//set mid to the average of first and last
			localMid = (localFirst + localLast) / 2;
			if(itemIDs[localMid] == borrowedTargetID)
			{
				localFound = true;
			}//End if
			else if(itemIDs[localMid] < borrowedTargetID)
			{
				localFirst = localMid + 1;
			}//End else if
			else
			{
				localLast = localMid - 1;
			}//End else
		}//End while
		if(localFound == false)
		{
			localMid = NOT_FOUND;
		}//End if
		return localMid;
	}//End getSearchResults
	
}//End class