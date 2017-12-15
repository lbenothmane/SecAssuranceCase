package test;

import javax.print.attribute.standard.MediaSize.Engineering;

public class Function
{
	private String name;
	private int startingLineNumber;
	private int endingLineNumber;
	private boolean modified;
	
	public Function(String name, int startingLineNumber, int endingLineNumber, boolean modified) 
	{
		this.name = name;
		this.startingLineNumber = startingLineNumber;
		this.endingLineNumber = endingLineNumber;
		this.modified = modified;
	}
	
	public String getName() { return name; }
	public int getStartingLineNumber() { return startingLineNumber; }
	public int getEndingLineNumber() { return endingLineNumber; }
	public boolean isModified() { return modified; }
}
