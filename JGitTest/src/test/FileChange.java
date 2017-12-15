package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.diff.EditList;

public class FileChange
{
	private final Pattern javaFunctionPattern = Pattern.compile("^.*public \\S* (.*)\\(.*\\).*|.*public (.*)\\(.*\\).*");
	private final Pattern jsFunctionPattern = Pattern.compile("^.*function \\S* (.*)\\(.*\\).*|.*function (.*)\\(.*\\).*");
	
	private String fileName;
	private String filePath;
	private String fullPath;
	private String pathToGitDir;
	private EditList editList;
	private ArrayList<Function> functions = new ArrayList<Function>();
	
	public FileChange(String fileName, EditList editList, String pathToGitDir) 
	{
		this.pathToGitDir = pathToGitDir;
		this.fullPath = fileName;
		
		fileName = fileName.replace('/', '\\');
		
		//parse out the file path with the file name
		Pattern pattern = Pattern.compile("^(.+\\\\)*(.+)$");
		Matcher matcher = pattern.matcher(fileName);
		if(matcher.matches()) 
		{
			this.filePath = matcher.group(1);
			if(this.filePath == null) 
			{
				this.filePath = "";
			}
			this.fileName = matcher.group(2);
		}
		else 
		{
			System.out.println("ERROR in FileChange class! Invalid filepath! PATH: " + fileName);
		}
		
		this.editList = editList;
		findFunctions();
		
	}
	
	private void findFunctions() 
	{
		File file = new File(pathToGitDir + "/" +  filePath + fileName);
		try
		{
			Scanner scanner = new Scanner(file);
						
			int lineNumber = 1;
			Stack<FunctionData> functionDatas = new Stack<FunctionData>();
			functionDatas.add(new FunctionData());
			
			while(scanner.hasNextLine()) 
			{
				FunctionData data = functionDatas.peek();
				String line = scanner.nextLine();
				
				//check for function
				Matcher matcher = null;
				if(fileName.contains(".java")) 
				{
					matcher = javaFunctionPattern.matcher(line);
				}
				else 
				{
					matcher = jsFunctionPattern.matcher(line);
				}
				
				if(matcher.matches())
				{
					
					if(data.functionName != null) //create new function (we are function inside a function)
					{
						data = new FunctionData();
						functionDatas.add(data);
					}
					
					data.functionName = matcher.group(1);
					if(data.functionName == null) 
					{
						data.functionName = matcher.group(2);
					}
					
					data.startingLineNumber = lineNumber;
					data.endingLineNumber = lineNumber;
					data.foundStartingBracket = false;
					data.currentOpenBrackets = 0;
				}
				
				if(data.functionName != null) 
				{
					int countOpening = line.length() - line.replace("{", "").length();
					int countClosing = line.length() - line.replace("}", "").length();
					
					if(!data.foundStartingBracket && countOpening > 0)
					{
						data.foundStartingBracket = true;
					}
					data.currentOpenBrackets += countOpening - countClosing;
					
					if(data.foundStartingBracket && data.currentOpenBrackets <= 0) 
					{
						data.endingLineNumber = lineNumber;
						functions.add(new Function(data.functionName, data.startingLineNumber, data.endingLineNumber, checkModified(data.startingLineNumber, data.endingLineNumber)));
						functionDatas.pop(); //remove the function data on top of the stack
						
						//now we need to understand a few things, do we have a another function encapsulating the inner function?
						//if so, we need to handle a current open bracket deficit
						//otherwise, we add a new functiondata for handling a new function
						if(!functionDatas.isEmpty())
						{
							int leftOverBrackets = data.currentOpenBrackets;
							while(!functionDatas.isEmpty())
							{
								if(leftOverBrackets < 0) 
								{
									data = functionDatas.peek();
									data.currentOpenBrackets += leftOverBrackets;
									if(data.currentOpenBrackets <= 0)
									{
										data.endingLineNumber = lineNumber;
										functions.add(new Function(data.functionName, data.startingLineNumber, data.endingLineNumber, checkModified(data.startingLineNumber, data.endingLineNumber)));
										functionDatas.pop();
										leftOverBrackets = data.currentOpenBrackets;
									}
								}
								else
								{
									break;
								}
							}

						}
						
						if(functionDatas.isEmpty())
						{
							functionDatas.add(new FunctionData());
						}
					}
				}

				lineNumber += 1;
			}
			
			scanner.close();
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public boolean checkModified(int startingLineNumber, int endingLineNumber) 
	{
		for(Edit edit : editList) 
		{
			//System.out.println(edit.getBeginA() + " " + edit.getEndA() + " " + edit.getBeginB() + " " + edit.getEndB());
			if((edit.getBeginB() + 1 <= startingLineNumber && edit.getEndB() >= endingLineNumber) || (edit.getBeginB() + 1 <= endingLineNumber && edit.getEndB() >= endingLineNumber) || (edit.getBeginB() + 1 >= startingLineNumber && edit.getEndB() <= endingLineNumber))
			{
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<Function> getModifiedFunctions()
	{
		ArrayList<Function> modifiedFunctions = new ArrayList<Function>();
		for(Function function : functions) 
		{
			if(function.isModified())
			{
				modifiedFunctions.add(function);
			}
		}
		return modifiedFunctions;
	}
	
	public String getFileName() { return fileName; }
	public String getFilePath() { return filePath; }
	public ArrayList<Function> getFunctions() { return functions; }
	public String getFullPath() { return filePath; }
	
	private class FunctionData 
	{
		public String functionName = null;
		public int startingLineNumber = 0;
		public int endingLineNumber = 0;
		public boolean foundStartingBracket = false;
		public int currentOpenBrackets = 0;
		public FunctionData() 
		{
			
		}
	}
}
