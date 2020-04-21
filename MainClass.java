/**Author: Keven Mitchell
 * Course: CPT187
 * Purpose: simulates a sod order, main class
 * Createdate: 4/14/20
 */

package edu.cpt187.mitchell.participation;

import java.util.Scanner;

public class MainClass 
{
	//Declaring and assigning constants
	public static final String[] MAIN_MENU_OPTIONS = {"Login","Create an Account","Quit"};
	public static final String[] FILE_MENU_OPTIONS = {"Load Inventory","Create Order","Return to the Main Menu"};
	public static final String[] MENU_CHARS = {"[A] for","[B] for","[Q] for"};
	public static final String[] FILE_MENU_CHARS = {"[A] for","[B] for","[R] for"};
	public static final String[] SUB_MENU_CHARS = {"[A] for","[B] for","[C] for"};
	public static final String MASTER_ACCOUNTS_FILE = "MasterUserFile.dat";
	public static final String MASTER_FILE_NAME = "MasterOrderFile.dat";
	
	public static void main(String[] args)
	{
		String userName = "";
		char menuSelection = ' ';
		
		
		Scanner input = new Scanner(System.in);
		SodOrder mySodOrder = new SodOrder();
		Inventory currentInventory = new Inventory();
		UserAccounts currentUser = new UserAccounts(MASTER_ACCOUNTS_FILE);
		OrderWriter sodOrders = new OrderWriter(MASTER_FILE_NAME);
		
		displayWelcomeBanner();
		
		menuSelection = validateMainMenu(input);
		while (menuSelection != 'Q')
		{
			if(menuSelection == 'A')
			{
				//set account arrays from masterFile if it exists
				currentUser.setUserAccountArrays();
				//attempt login
				userName = getUserName(input);
				//set the array index of username
				currentUser.setSearchedIndex(userName);
				if(currentUser.getSearchedIndex() < 0)
				{
					displayNoUser();
				}//end if file not open
				else
				{
					//Uses the array index to determine if the associated password matches input
					if(currentUser.getPasswordMatch(getPassword(input)))
					{
						//enters ordering portion of the program, inventory menu first
						menuSelection = validateFileMenu(input);
						while(menuSelection != 'R')
						{
							if(menuSelection == 'A')
							{
								currentInventory.setLoadItems(getInventoryFileName(input));
								
								if(currentInventory.getRecordCount() <= 0)
								{
									displayNotOpen();
								}//end if, had an else on the flowchart but the logic was unused
							}//End if for menu selection A
							else
							{
								currentInventory.setSearchIndex(validateSearchValue(input));
								
								if(currentInventory.getItemSearchIndex() < 0)
								{
									displayNotFound();
								}//End if
								else
								{
									mySodOrder.setLastItemSelectionIndex(currentInventory.getItemSearchIndex());
									mySodOrder.setItemID(currentInventory.getItemIDs());
									mySodOrder.setItemName(currentInventory.getItemNames());
									mySodOrder.setItemPrice(currentInventory.getItemPrices());
									mySodOrder.setHowMany(validateHowMany(input));
									
									if (mySodOrder.getInStockCount(currentInventory.getInStockCounts()) < mySodOrder.getHowMany())
									{
										displayOutOfStock();
									}//end if
									else
									{
										mySodOrder.setDiscountType(validateDiscountMenu(input, currentInventory.getDiscountNames(), currentInventory.getDiscountRates()));
										mySodOrder.setDiscountName(currentInventory.getDiscountNames());
										mySodOrder.setDiscountRate(currentInventory.getDiscountRates());
										mySodOrder.setDecreaseInStock(currentInventory);
										mySodOrder.setPrizeName(currentInventory.getPrizeNames(), currentInventory.getRandomNumber());
										
										//write order to masterfile
										sodOrders.setWriteOrder(mySodOrder.getItemID(), mySodOrder.getItemName(), mySodOrder.getItemPrice(), mySodOrder.getHowMany(), mySodOrder.getTotalCost());
										
										//conditional overloaded receipt for discounts
										if(mySodOrder.getDiscountRate() > 0.0)
										{
											displayItemReceipt(mySodOrder.getItemName(), mySodOrder.getItemPrice(), mySodOrder.getHowMany(), mySodOrder.getDiscountName(), mySodOrder.getDiscountRate(), mySodOrder.getDiscountAmt(), mySodOrder.getDiscountPrice(), mySodOrder.getSubTotal(), mySodOrder.getTaxRate(), mySodOrder.getTaxAmt(), mySodOrder.getTotalCost(), mySodOrder.getPrizeName(), mySodOrder.getInStockCount(currentInventory.getInStockCounts()));
										}//end if
										else
										{
											displayItemReceipt(mySodOrder.getItemName(), mySodOrder.getItemPrice(), mySodOrder.getHowMany(), mySodOrder.getSubTotal(), mySodOrder.getTaxRate(), mySodOrder.getTaxAmt(), mySodOrder.getTotalCost(), mySodOrder.getPrizeName(), mySodOrder.getInStockCount(currentInventory.getInStockCounts()));
										}//end else(discount rate is 0 
									}//End else(item is in stock)
								}//End else(item search successful)
							}//End else(menuSelection == B)
							menuSelection = validateFileMenu(input);
						}//End run-while not return to main menu	
					}//End If password matches
					else
					{
						displayWrongLogin();
					}//End else(password doesn't match)					
				}//End else for file is open
			}//End if for login
			else
			{
				currentUser.setWriteOneRecord(getUserName(input), getPassword(input));
			}//end else if for create an account
			menuSelection = validateMainMenu(input);
		}//End run-while not quit	
		if(mySodOrder.getTotalCost()>0.0)
		{
			//Load updated masterfile
			currentInventory.setLoadItems(sodOrders.getFileName(), sodOrders.getRecordCount());
			displayFinalReport(currentInventory.getItemIDs(), currentInventory.getItemNames(), currentInventory.getItemPrices(), currentInventory.getOrderQuantities(), currentInventory.getOrderTotals(),sodOrders.getRecordCount(), currentInventory.getDiscountNames(), mySodOrder.getDiscountCounts(), currentInventory.getPrizeNames(), mySodOrder.getPrizeCounts());
			
		}//End if total cost>0
		displayFarewellMessage(userName);
	}//End main
	
	public static void displayWelcomeBanner()
	{
		System.out.println("************************************************************");
		System.out.println("Welcome to the SodNotZod Sales Program!");
		System.out.println("This program will allow you to generate receipts");
		System.out.println("And a total end of day report for sod orders");
		System.out.println("************************************************************");
		
	}//end of displayWelcomeBanner

	public static void displayItemReceipt(String borrowedItemName, Double borrowedItemPrice, Integer borrowedHowMany, String borrowedDiscountName, Double borrowedDiscountRate, Double borrowedDiscountAmt, Double borrowedDiscountPrice, Double borrowedSubTotal, Double borrowedTaxRate, Double borrowedTax, Double borrowedTotalCost, String borrowedPrizeName, int borrowedInStockCount)
	{
		//print the item report
		System.out.println("************************************************************");
		System.out.printf("%39s\n", " BEGIN ITEM REPORT");
		System.out.println("************************************************************");
		System.out.printf("%-20s%15s\n", "Item name: ", borrowedItemName);
		System.out.printf("%-20s%1s%14.2f\n", "Original Price: ","$", borrowedItemPrice);
		System.out.printf("%-20s%15d\n", "Item Quantity: ", borrowedHowMany);
		System.out.printf("%-20s%15d\n", "Stock Remaining: ", borrowedInStockCount);
		System.out.printf("%-20s%15s\n", "Discount name: ", borrowedDiscountName);
		System.out.printf("%-20s%14.1f%1s\n", "Discount Rate: ", borrowedDiscountRate * 100, "%");
		System.out.printf("%-20s%1s%14.2f\n", "Discount Amount: ","$", borrowedDiscountAmt);
		System.out.printf("%-20s%1s%14.2f\n", "Discounted Price: ","$", borrowedDiscountPrice);
		System.out.printf("%-20s%1s%14.2f\n", "SubTotal: ","$", borrowedSubTotal);
		System.out.printf("%-20s%14.1f%1s\n", "Tax Rate: ", borrowedTaxRate * 100, "%");
		System.out.printf("%-20s%1s%14.2f\n", "Tax Amount","$",borrowedTax);
		System.out.printf("%-20s%1s%14.2f\n", "Total Price: ","$", borrowedTotalCost);
		System.out.printf("%-20s%15s\n", "BONUS PRIZE!!!: ", borrowedPrizeName);
		System.out.println("************************************************************");
		System.out.printf("%38s\n"," END ITEM REPORT");
		System.out.println("************************************************************");

	}//end of displayItemReciept
	
	public static void displayItemReceipt(String borrowedItemName, Double borrowedItemPrice, Integer borrowedHowMany, Double borrowedSubTotal, Double borrowedTaxRate, Double borrowedTax, Double borrowedTotalCost, String borrowedPrizeName, int borrowedInStockCount)
	{
		//print the item report
		System.out.println("************************************************************");
		System.out.printf("%39s\n", " BEGIN ITEM REPORT");
		System.out.println("************************************************************");
		System.out.printf("%-20s%15s\n", "Item name: ", borrowedItemName);
		System.out.printf("%-20s%1s%14.2f\n", "Original Price: ","$", borrowedItemPrice);
		System.out.printf("%-20s%15d\n", "Item Quantity: ", borrowedHowMany);
		System.out.printf("%-20s%15d\n", "Stock Remaining: ", borrowedInStockCount);
		System.out.printf("%-20s%1s%14.2f\n", "SubTotal: ","$", borrowedSubTotal);
		System.out.printf("%-20s%14.1f%1s\n", "Tax Rate: ", borrowedTaxRate * 100, "%");
		System.out.printf("%-20s%1s%14.2f\n", "Tax Amount","$",borrowedTax);
		System.out.printf("%-20s%1s%14.2f\n", "Total Price: ","$", borrowedTotalCost);
		System.out.printf("%-20s%15s\n", "BONUS PRIZE!!!: ", borrowedPrizeName);
		System.out.println("************************************************************");
		System.out.printf("%38s\n"," END ITEM REPORT");
		System.out.println("************************************************************");

	}//end of displayItemReciept
	
	public static void displayFinalReport(int[] borrowedIDs, String[] borrowedItemNames, double[] borrowedItemPrices, int[] borrowedItemQuantities, double[] borrowedTotals, int borrowedRecordCount, String[] borrowedDiscountNames, int[] borrowedDiscountCounts, String[] borrowedPrizeNames, int[] borrowedPrizeCounts)
	{
		int localIndex = 0;
		int localDiscountIndex = 0;
		int localPrizeIndex = 0;
		double localTotal = 0.0;
		//print the final report
		System.out.println("************************************************************");
		System.out.printf("%39s\n", "  BEGIN EOD REPORT");
		System.out.println("************************************************************");
		System.out.printf("%-8s%-15s%-10s%-5s%-10s\n","ITEM ID", "NAME", "PRICE", "QTY", "TOTAL");
		while(localIndex<borrowedItemNames.length)
		{
			System.out.printf("%-8d%-15s%-2s%-8.2f%-5d%-2s%-8.2f\n", borrowedIDs[localIndex], borrowedItemNames[localIndex],"$ ", borrowedItemPrices[localIndex], borrowedItemQuantities[localIndex],"$ ", borrowedTotals[localIndex]);
			localTotal = localTotal + borrowedTotals[localIndex];
			localIndex++;
		}//End while for master file records
		System.out.println("");
		System.out.printf("%-20s%20s\n","RECORD COUNT", "ORDER TOTAL");
		System.out.printf("%-20d%-2s%18.2f\n", borrowedRecordCount,"$ ", localTotal);
		System.out.println("");
		while(localDiscountIndex < borrowedDiscountNames.length)
		{
			System.out.printf("%-20s%8s%10s\n", borrowedDiscountNames[localDiscountIndex]," count: ", borrowedDiscountCounts[localDiscountIndex]);
			localDiscountIndex++;
		}//end while for discounts
		System.out.println("");
		while(localPrizeIndex < borrowedPrizeNames.length)
		{
			System.out.printf("%-20s%8s%10s\n", borrowedPrizeNames[localPrizeIndex]," count: ", borrowedPrizeCounts[localPrizeIndex]);
			localPrizeIndex++;
		}//end while for prizes
		System.out.println("************************************************************");
		System.out.printf("%38s\n","  END EOD REPORT");
		System.out.println("************************************************************");

	}//end of final report

	public static void displayFarewellMessage(String borrowedUserName)
	{
		System.out.println("Thank you for using the program, ");
		System.out.println("have a good day" + borrowedUserName);
	}//end of displayFarewellMessage
	
	public static void displayFileMenu()
	{

		System.out.println("Please make a selection");
		System.out.println("************************************************************");
		System.out.printf("%-30s\n", "Inventory Menu");
		int localIndex = 0;
		while(localIndex<FILE_MENU_CHARS.length)
		{
			System.out.printf("%-7s%1s%-25s\n", FILE_MENU_CHARS[localIndex]," ", FILE_MENU_OPTIONS[localIndex]);			
			localIndex++;
		}
		System.out.print("Please make your selection here: ");

	}//End displayFileMenu
	
	public static void displayMainMenu()
	{

		System.out.println("Please make a selection");
		System.out.println("************************************************************");
		System.out.printf("%-30s\n", "Main Menu");
		int localIndex = 0;
		while(localIndex<MENU_CHARS.length)
		{
			System.out.printf("%-7s%1s%-25s\n", MENU_CHARS[localIndex]," ", MAIN_MENU_OPTIONS[localIndex]);			
			localIndex++;
		}
		System.out.print("Please make your selection here: ");

	}//End displayMainMenu
	
	public static void displayDiscountMenu(Scanner borrowedInput, String[] borrowedDiscountNames, double[] borrowedDiscountRates)
	{
		System.out.println("Please make a discount selection");
		System.out.println("************************************************************");
		System.out.printf("%-30s\n", "Discount Menu");
		int localIndex = 0;
		while(localIndex<SUB_MENU_CHARS.length)
		{
			System.out.printf("%-7s%1s%-25s%3.1f%1s\n", SUB_MENU_CHARS[localIndex]," ", borrowedDiscountNames[localIndex], borrowedDiscountRates[localIndex] * 100, "%");
			localIndex++;
		}
		System.out.print("Please make your selection here: ");
	}//End displayDiscountMenu
	
	public static void displayOutOfStock()
	{
		System.out.println("Unfortunately we currently dont have enough stock to");
		System.out.println("cover this order, please try a lower quantity");
		System.out.println("or different item");
	}//End displayOutOfStock
	
	public static void displayNotFound()
	{
		System.out.println("The selected item could not be found in our records");
	}//End displayNotFound
	
	public static void displayNotOpen()
	{
		System.out.println("The currently selected file");
		System.out.println("is empty or invalid");
	}//End displayNotOpen
	
	public static void displayWrongLogin()
	{
		System.out.println("The entered username or password does not match");
		System.out.println("our records");
	}//End displayWrongPass
	
	public static void displayNoUser()
	{
		System.out.println("The entered username could not be found.");
	}//End displayNoUser
	
	public static int validateSearchValue(Scanner borrowedInput)
	{
		int localSearchValue = 0;
		System.out.print("Please enter the item ID of your selection: ");
		localSearchValue = borrowedInput.nextInt();
		while(localSearchValue <= 0)
		{
			System.out.println("Please enter a valid item ID: ");
			localSearchValue = borrowedInput.nextInt();
		}
		return localSearchValue;
	}//End validateSearchValue
	
	public static String getInventoryFileName(Scanner borrowedInput)
	{
		String localFileName = "";
		
		System.out.print("Please enter the inventory file name: ");
		localFileName = borrowedInput.next();
		
		return localFileName;
	}//End getFileName
	
	public static String getAccountFileName(Scanner borrowedInput)
	{
		String localFileName = "";
		
		System.out.print("Please enter the account file name: ");
		localFileName = borrowedInput.next();
		
		return localFileName;
	}//End getAccountFileName
	
	public static String getUserName(Scanner borrowedInput)
	{
		System.out.print("Please enter username: ");
		return borrowedInput.next();
	}//end of getUserName
	
	public static String getPassword(Scanner borrowedInput)
	{
		System.out.print("Please enter password: ");
		return borrowedInput.next();
	}//end of getPassword
	
	public static Character validateFileMenu(Scanner borrowedInput)
	{
		Character localSelection = ' ';
		//display main menu
		displayFileMenu();
		//parse menu input
		localSelection = borrowedInput.next().toUpperCase().charAt(0);
		//checks input against menu selections, loops if input is invalid
		while(localSelection != 'A' && localSelection != 'B' && localSelection != 'R')
		{
			displayFileMenu();
			localSelection = borrowedInput.next().toUpperCase().charAt(0);
		}//end loop
		
		return localSelection;
	}//end of validateFileMenu
	
	public static Character validateMainMenu(Scanner borrowedInput)
	{
		Character localSelection = ' ';
		//display main menu
		displayMainMenu();
		//parse menu input
		localSelection = borrowedInput.next().toUpperCase().charAt(0);
		//checks input against menu selections, loops if input is invalid
		while(localSelection != 'A' && localSelection != 'B' && localSelection != 'Q')
		{
			displayMainMenu();
			localSelection = borrowedInput.next().toUpperCase().charAt(0);
		}//end loop
		
		return localSelection;
	}//end of validateMainMenu

	public static Character validateDiscountMenu(Scanner borrowedInput, String[] borrowedDiscountNames, double[] borrowedDiscountRates)
	{
		Character localSelection = ' ';
		//display discount menu
		displayDiscountMenu(borrowedInput, borrowedDiscountNames, borrowedDiscountRates);
		//parse menu input
		localSelection = borrowedInput.next().toUpperCase().charAt(0);
		
		//checks input against menu selections, loops if input is invalid
		while(localSelection != 'A' && localSelection != 'B' && localSelection != 'C')
		{
			displayDiscountMenu(borrowedInput, borrowedDiscountNames, borrowedDiscountRates);
			localSelection = borrowedInput.next().toUpperCase().charAt(0);
		}//end loop
		
		return localSelection;
	}//end of validateDiscountMenu
	
	public static Integer validateHowMany(Scanner borrowedInput)
	{
		//declare and initialize local howMany
		Integer localHowMany = 0;
		//prompt
		System.out.print("Please enter item quantity: ");
		//take input
		localHowMany = borrowedInput.nextInt();
		//validation loop
		while(localHowMany <= 0)
		{
			System.out.println("Please enter a valid item quantity");
			localHowMany = borrowedInput.nextInt();
		}//end validation loop
		return localHowMany;
	}//end of validateHowMany
	
}//End class
