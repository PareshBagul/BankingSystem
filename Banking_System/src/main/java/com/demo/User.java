package com.demo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;


// every SQL statement is automatically committed to the database after it is executed

 

public class User {
private Connection connection;
private Scanner sc;
public User(Connection connection, Scanner sc) {
	this.connection=connection;
	this.sc=sc;
}
public void register() {
	
	System.out.println("Full Name:");
	sc.nextLine(); 
	String full_name=sc.nextLine();
	System.out.println("Email: ");
	String email=sc.nextLine();
	System.out.println("Password:");
	String password=sc.nextLine();
	if(user_exist(email)) {
		System.out.println("User Already Exists for this Address!!");
		
	}
	else 
	{
		String register_query="insert into user(full_name,email,password) values(?,?,?)";
	
		PreparedStatement preparedStatement=null;
		try 
		{
			preparedStatement = connection.prepareStatement(register_query);
			preparedStatement.setString(1,full_name);
			preparedStatement.setString(2, email);
			preparedStatement.setString(3,password);
			int affectedRows=preparedStatement.executeUpdate();
			System.out.println("Registration Successfully");
			
			
			
		} catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				preparedStatement.close();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
/* when we use nextInt function and nextLine function then there may be occur issue that when we 
enter then "\n" is also stored in buffer so for nextLine "\n"are assigned thats why we should use sc.nextLine()
to remove "\n" */
//and when we used nextInt() multiple then nextInt() ignore "\n"
public String login() {
	sc.nextLine();
	System.out.println("Email: ");
	String email= sc.nextLine();
	System.out.println("Password: ");
	String password=sc.nextLine();
	String login_query ="select * from user where email =? and password=?";
	try {
		PreparedStatement preparedStatement=connection.prepareStatement(login_query);
		preparedStatement.setString(1,email);
		preparedStatement.setString(2, password);
		ResultSet resultSet=preparedStatement.executeQuery();
	    if(resultSet.next()) {
	    	return email;
	    }else {
	    	return null;
	    }
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return null;
	
}
public boolean user_exist(String email) {
	String query="Select * from user Where email =?";
	PreparedStatement preparedStatement;
	try {
		preparedStatement = connection.prepareStatement(query);
		preparedStatement.setString(1,email);
		ResultSet resultSet=preparedStatement.executeQuery();
		if(resultSet.next()) {
			return true;
		}
		else {
			return false;
		}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	return false;
}

}
