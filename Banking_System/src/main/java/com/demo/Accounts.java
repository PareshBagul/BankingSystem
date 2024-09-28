package com.demo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Accounts {
	private Connection connection;
	private Scanner sc;
	public Accounts(Connection connection , Scanner sc) {
		this.connection=connection;
		this.sc=sc;
	}
	public long open_account(String email) {
		if(!account_exist(email)) {
			sc.nextLine();
			String  open_account_query="insert into Accounts(account_number,full_name,email,balance,security_pin) values(?,?,?,?,?)";
			System.out.println("Enter Full Name: ");
			String full_name=sc.nextLine();
			System.out.println("Enter Initial Ammount: ");
			double balance=sc.nextDouble();
			sc.nextLine();
			System.out.println("Enter Security Pin: ");
			String security_pin= sc.nextLine();
			
			
			try {
				long account_number= generateAccountNumber();
				PreparedStatement preparedStatement=connection.prepareStatement(open_account_query);
				preparedStatement.setLong(1, account_number);
				preparedStatement.setString(2,full_name);
				preparedStatement.setString(3,email);
				preparedStatement.setDouble(4,balance);
				preparedStatement.setString(5,security_pin);
				int rowAffected=preparedStatement.executeUpdate();
				if(rowAffected >0) {
					return account_number;
				}
				else {
					throw new RuntimeException("Account creation failed!!");
				}
			
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		throw new RuntimeException("Account Already Exist");
	}
	
	
	public long getAccount_number(String email) {
		String query="select account_number from Accounts where email=?";
		try {
			PreparedStatement preparedStatement= connection.prepareStatement(query);
			preparedStatement.setString(1,email);
			ResultSet resultSet=preparedStatement.executeQuery();
			if(resultSet.next()) {
				return resultSet.getLong("account_number");
			}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 throw new RuntimeException("Account Number Doesn't Exist!");
		
	}
	
	private long generateAccountNumber() {
		String sql="select account_number from accounts order by account_number desc limit 1";
		try {
			PreparedStatement preparedStatement=connection.prepareStatement(sql);
			ResultSet resultSet=preparedStatement.executeQuery();
			if(resultSet.next()) {
				long last_account_number=resultSet.getLong("account_number");
				return last_account_number+1;
			}else {
				return 10000100;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 10000100;
	}
	
	public boolean account_exist(String email) {
		String query ="Select account_number from Accounts where email=?";
		PreparedStatement preparedStatement;
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, email);
			ResultSet resultSet=preparedStatement.executeQuery();
			if(resultSet.next()) {
				return true;
			}else {
				return false;
			}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
		
	}
}
