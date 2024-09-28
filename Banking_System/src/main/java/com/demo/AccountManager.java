package com.demo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
// This class is used to perform all operation on account
public class AccountManager {
private Connection connection;
private Scanner sc;

AccountManager(Connection connection, Scanner sc){
	this.connection=connection;
	this.sc=sc;
}
public void credit_money(long account_number)
{
	System.out.println("Enter Amount: ");
	double amount=sc.nextDouble();
	sc.nextLine();
	System.out.println("Enter Security Pin: ");
	String security_pin=sc.nextLine();
	
		PreparedStatement preparedStatement = null;
		ResultSet resultSet=null;
		try 
		{
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement("select * from accounts where security_pin =?");
			preparedStatement.setString(1,security_pin);
			resultSet=preparedStatement.executeQuery();
			if(resultSet.next()) 
			{
				double current_balance =resultSet.getDouble("balance");
				if(amount<=current_balance) 
				{
					String credit_query="Update accounts set balance=balance+ ? where account_number=?";
					PreparedStatement prepareStatement1=connection.prepareStatement(credit_query);
					prepareStatement1.setDouble(1,amount);
					prepareStatement1.setLong(2,account_number);
					prepareStatement1.executeUpdate();
					
						System.out.println("Rs."+amount+" credited Successfully");
						connection.commit();
                        connection.setAutoCommit(true);
						
				}
				else {
				System.out.println("Amount is not sufficient in your account");
				connection.setAutoCommit(true);
				}
			}
			else 
			{
					System.out.println("Invalid Security Pin!");
					
			}
		}
			
			
		 catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		finally {
			try {
				preparedStatement.close();
				resultSet.close();
				connection.setAutoCommit(true);
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	
	}

public void debit_money(long account_number) {
	System.out.println("Enter Amount: ");
	double amount=sc.nextDouble();
	sc.nextLine();
	System.out.println("Enter Security Pin: ");
	String security_pin =sc.nextLine();
	PreparedStatement preparedStatement=null;
	ResultSet resultSet=null;
    	try {
    		connection.setAutoCommit(false);
			preparedStatement=connection.prepareStatement("select * from accounts where account_number=? and security_pin=?");
			preparedStatement.setLong(1,account_number);
			preparedStatement.setString(2,security_pin);
			resultSet=preparedStatement.executeQuery();
			if(resultSet.next()) 
			{
				double current_balance=resultSet.getDouble("balance");
				if(amount<=current_balance) {
					String credit_query="update accounts set balance= balance- ? where account_number=?";
					PreparedStatement preparedStatement1= connection.prepareStatement(credit_query);
					preparedStatement1.setDouble(1,amount);
					preparedStatement1.setLong(2,account_number);
					preparedStatement1.executeUpdate();
					
						System.out.println("Rs."+amount+" debited Succcessfully");
						connection.commit();
                        connection.setAutoCommit(true);
					}
					else {
						System.out.println("Amount is not sufficient for debit");
						connection.setAutoCommit(true);
					}
					
				}
			else {
				System.out.println("Invalid Pin!");
			}
    	}
    	catch (SQLException e) 
    	{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	finally {
    		try {
				preparedStatement.close();
				resultSet.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    
    
    }
public void transfer_money(long sender_account_number) 
{
	System.out.println("Enter Receiver Account Number: ");
	long receiver_account_number=sc.nextLong();
	System.out.println("Enter Amount: ");
	double amount=sc.nextDouble();
	sc.nextLine();
	System.out.println("Enter Security Pin: ");
	String security_pin =sc.nextLine();
	
	PreparedStatement preparedStatement=null;
	ResultSet resultSet=null;
		try {
				connection.setAutoCommit(false);
				if(receiver_account_number!=0) 
				{
					preparedStatement=connection.prepareStatement("select * from accounts where account_number=? and security_pin =?");
					preparedStatement.setLong(1, sender_account_number);
					preparedStatement.setString(2, security_pin);
					resultSet= preparedStatement.executeQuery();
					
				
					if(resultSet.next()) 
					{
						double current_balance=resultSet.getDouble("balance");
								if(amount<=current_balance && amount>0) 
								{
									// Write debit and credit queries
									String debit_query="update accounts set balance=balance-? where account_number=?";
									String credit_query="update accounts set balance =balance + ? where account_number=?";
									
									// Debit and Credit prepared Statements
			                        PreparedStatement creditPreparedStatement = connection.prepareStatement(credit_query);
			                        PreparedStatement debitPreparedStatement = connection.prepareStatement(debit_query);
	
			                        // Set Values for debit and credit prepared statements
									creditPreparedStatement = connection.prepareStatement(credit_query);
									debitPreparedStatement = connection.prepareStatement(debit_query);
									creditPreparedStatement.setDouble(1,amount);
									creditPreparedStatement.setLong(2,receiver_account_number);
									debitPreparedStatement.setDouble(1,amount);
									debitPreparedStatement.setLong(2,sender_account_number);
									 int rowsAffected1 = debitPreparedStatement.executeUpdate();
				                        int rowsAffected2 = creditPreparedStatement.executeUpdate();
				                        if (rowsAffected1 > 0 && rowsAffected2 > 0) 
				                        {
				                            System.out.println("Transaction Successful!");
				                            System.out.println("Rs."+amount+" Transferred Successfully");
				                            connection.commit();
				                            connection.setAutoCommit(true);
				                            creditPreparedStatement.close();
				                            debitPreparedStatement.close();
				                            return;
				                        } 
				                        else 
				                        {
				                            System.out.println("Transaction Failed");
				                            connection.rollback();
				                            connection.setAutoCommit(true);
				                            creditPreparedStatement.close();
				                            debitPreparedStatement.close();
				                        }
								}
								else
								{
		                            System.out.println("Insufficient Balance!");
								}
					}
					else
					{
		                    System.out.println("Invalid Security Pin!");
		            }
			     }
				else
				{
	                System.out.println("Invalid account number");
				}
							
		}
					
				
		 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally 
		{
			try 
			{
				preparedStatement.close();  // Free Up Database Connections
				resultSet.close(); 
			} 
			catch (SQLException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


public void getBalance(long account_number) {
	sc.nextLine();
	System.out.println("Enter Security Pin: ");
	String security_pin=sc.nextLine();
	PreparedStatement preparedStatement= null;
	ResultSet resultSet = null;
	try {
		preparedStatement=connection.prepareStatement("select balance from Accounts where account_number = ? and security_pin=?");
		preparedStatement.setLong(1, account_number);
		preparedStatement.setString(2, security_pin);
		resultSet=preparedStatement.executeQuery();
		if(resultSet.next()) {
			double balance=resultSet.getDouble("balance");
			System.out.println("Balance: "+balance);
		}else {
			System.out.println("Invalid Pin!");
		}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	finally {
		try {
			preparedStatement.close();
			resultSet.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
}

