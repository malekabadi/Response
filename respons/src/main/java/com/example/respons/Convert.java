package com.example.respons;

import java.util.ArrayList;

public class Convert 
{

	public Convert() 
	{
	}

	public ArrayList<String> StrToList(String str)
	{
		ArrayList<String> listItems = new ArrayList<String>();
		String[] Rows = str.split(":");
		for (int i = 0; i < Rows.length; i++) 
			listItems.add("");
	    return listItems;
	}

}