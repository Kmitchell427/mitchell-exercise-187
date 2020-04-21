/**Author: Keven Mitchell
 * Course: CPT187
 * Purpose: simulates a sod order, SodOrder supporting class that handles individual orders
 * Createdate: 4/14/20
 */


package edu.cpt187.mitchell.participation;

public class SodOrder 
{
	private final double ZERO_TOTAL = 0.0;
	private final double TAX_RATE = 0.075;
	private int[] discountCounts;
	private int[] prizeCounts;
	private char discountType = ' ';
	private int itemID = 0;
	private String itemName = "";
	private double itemPrice = 0;
	private String discountName = "";
	private double discountRate = 0.0;
	private int howMany = 0;
	private int lastItemSelectedIndex = 0;
	private String prizeName = "";
	
	//default no arg constructor
	public SodOrder()
	{
	}
	
	public void setLastItemSelectionIndex(int borrowedIndex)
	{
		lastItemSelectedIndex = borrowedIndex;
	}//end setLastItemSelectionIndex
	
	public void setItemID(int[] borrowedItemIDs)
	{
		itemID = borrowedItemIDs[lastItemSelectedIndex];
	}//End setItemID
	
	public void setItemName(String[] borrowedItemNames)
	{
		itemName = borrowedItemNames[lastItemSelectedIndex];
	}//End setItemName
	
	public void setItemPrice(double[] borrowedItemPrices)
	{
		itemPrice = borrowedItemPrices[lastItemSelectedIndex];
	}//End setItemPrice
	
	public void setHowMany(int borrowedHowMany)
	{
		howMany = borrowedHowMany;
	}//end setHowMany
	
	public void setDiscountType(char borrowedMenuSelection)
	{
		discountType = borrowedMenuSelection;
	}//end setDiscountType
	
	public void setDiscountRate(double[] borrowedDiscountRates)
	{
		if(discountType == 'A')
		{
			discountRate = borrowedDiscountRates[0];
		}//End if leg for selection a
		else if(discountType == 'B')
		{
			discountRate =  borrowedDiscountRates[1];
		}//End if leg for selection b
		else
		{
			discountRate = borrowedDiscountRates[2];
		}//End else leg for selection c
	}//End setDiscountRate
	
	public void setDiscountName(String[] borrowedDiscountNames)
	{
		if(discountCounts == null)
		{
			discountCounts = new int[borrowedDiscountNames.length];
		}
		if(discountType == 'A')
		{
			discountName = borrowedDiscountNames[0];
			discountCounts[0]++;
		}//End if leg for selection a
		else if(discountType == 'B')
		{
			discountName =  borrowedDiscountNames[1];
			discountCounts[1]++;
		}//End if leg for selection b
		else
		{
			discountName = borrowedDiscountNames[2];
			discountCounts[2]++;
		}//End else leg for selection c
	}//End setDiscountName
	
	public void setPrizeName(String[] borrowedPrizeNames, int borrowedPrizeIndex)
	{
		prizeName = borrowedPrizeNames[borrowedPrizeIndex];
		if(prizeCounts==null)
		{
			prizeCounts = new int[borrowedPrizeNames.length];
		}
		prizeCounts[borrowedPrizeIndex]++;
	}//End setPrizeName
	
	public void setDecreaseInStock(Inventory borrowedInventoryObject)
	{
		borrowedInventoryObject.setReduceStock(howMany, lastItemSelectedIndex);
	}//end setdecreaseinstock
	
	public int getInStockCount(int[] borrowedInStockCounts)
	{
		return borrowedInStockCounts[lastItemSelectedIndex];
	}//end getInStockCount
	
	public int getItemID()
	{
		return itemID;
	}//End getItemID
	
	public String getItemName()
	{
		return itemName;
	}//end getItemName
	
	public double getItemPrice()
	{
		return itemPrice;
	}//end getItemPrice
	
	public int getHowMany()
	{
		return howMany;
	}//end getHowMany
	
	public String getDiscountName()
	{
		return discountName;
	}//end getDiscountName
	
	public double getDiscountRate()
	{
		return discountRate;
	}//end getDiscountRate
	
	public int[] getDiscountCounts()
	{
		return discountCounts;
	}//end getDiscountCounts
	
	public double getDiscountAmt()
	{
		return discountRate * itemPrice;
	}//end getDiscountAmt
	
	public double getDiscountPrice()
	{
		return itemPrice - getDiscountAmt();
	}//end getDiscountPrice
	
	public String getPrizeName()
	{
		return prizeName;
	}//end getPrizeName
	
	public int[] getPrizeCounts()
	{
		return prizeCounts;
	}//end getPrizeCounts
	
	public double getSubTotal()
	{
		return getDiscountPrice() * howMany;
	}//end getSubTotal
	
	public double getTaxRate()
	{
		return TAX_RATE;
	}//End getTaxRate
	
	public double getTaxAmt()
	{
		return TAX_RATE * getSubTotal();
	}//End gettaxAmt
	
	public double getTotalCost()
	{
		if(discountType == ' ')
		{
			return ZERO_TOTAL;
		}//End if leg
		else
		{
			return getSubTotal() + getTaxAmt();
		}//end else leg
	}//End getTotalCost
}//End Class
