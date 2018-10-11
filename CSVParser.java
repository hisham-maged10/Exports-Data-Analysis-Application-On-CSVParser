/**
 * @author ${hisham_maged10}
 *https://github.com/hisham-maged10
 * ${DesktopApps}
 */
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import javax.swing.JFileChooser;
public class CSVParser
{
	private ArrayList<ArrayList<String>> csvInfo=null;
	private boolean header;
	private File csvFile;
	public String getCsvFileName()
	{
		return this.csvFile.getPath().substring(this.csvFile.getPath().lastIndexOf("\\")+1);	
	}
	public CSVParser()
	{
		this.header=true;
		getCSVInfo();
	}
	public CSVParser(String filePath)
	{
		this.header=true;
		getCSVInfo(filePath);
	}
	public CSVParser(File csvFile)
	{
		this.header=true;
		this.csvFile=csvFile;
		getCSVInfo(this.csvFile);
	}
	public CSVParser(boolean header)
	{
		this.header=header;
		getCSVInfo();
	}
	public CSVParser(String filePath,boolean header)
	{
		this.header=header;
		getCSVInfo(filePath);
	}
	public CSVParser(File csvFile,boolean header)
	{
		this.header=header;
		this.csvFile=csvFile;
		getCSVInfo(csvFile);
	}
	private int getColumn(String type)
	{
		if(!isCSVInfoExists()) return -1;
		int i=-1,n;
		csvInfo.trimToSize();
		for(i=0,n=csvInfo.size();i<n;i++)
		{
			if(csvInfo.get(i).get(0).equalsIgnoreCase(type))
			return i;
			else continue;
		}
		return i;			
	}
	private ArrayList<String> getColumnInfo(int columnNo)
	{	
		return (isCSVInfoExists())?csvInfo.get(columnNo):null;
	}
	public String[] get(String type)
	{
		if(!header)throw new CSVHeaderFormatException("No header was specified!");
		if(!isCSVInfoExists()){ throw new CSVFileNotLoadedException("csvFile not loaded successfully"); }
		int columnNo;
		type=validateSearch(type);
		if((columnNo=getColumn(type))==-1 || columnNo== csvInfo.size()) return null;
		ArrayList<String> columnInfo=new ArrayList<String>(getColumnInfo(getColumn(type)));
		if(header)
		columnInfo.remove(0);
		return columnInfo.toArray(new String[columnInfo.size()]);
	}
	public String[] getRow(int row)
	{
		return csvInfo.get(row).toArray(new String[csvInfo.get(row).size()]);	
	}
	public String[][] getCsvInfoArray()
	{
		String[][] returnArr=new String[csvInfo.size()][csvInfo.get(0).size()];
		for(int i=0;i<csvInfo.size();i++)
		{
			ArrayList<String> row=csvInfo.get(i);
			returnArr[i]=row.toArray(new String[row.size()]);
		}
		return returnArr;	
	}
	public boolean isCSVInfoExists()
	{
		return (csvInfo!=null)?true:false;
	}
	public void getCSVInfo()
	{	
		csvFile=getFile();
		String item="";
		int noOfItems=getNumberOfColumns(csvFile);
		try(Scanner input=new Scanner(csvFile))
		{
			if(header)
			csvInfo=make2DArrayListHeader(getNumberOfRows(csvFile));
			else
			csvInfo=make2DArrayListNoHeader();
			while(input.hasNextLine() && !(item=input.nextLine()).isEmpty())
			{
				fillRow(item,noOfItems);
				item="";
			}
	
		}
		catch(IOException ex)
		{
			System.err.println("Corrupted or Unreadable file !");
			System.exit(0);
		}
		csvInfo.trimToSize();
		filterArrayList(csvInfo);
	}
	//overload
	public void getCSVInfo(String path)
	{	
		csvFile=new File(path);
		if(!csvFile.isFile() || !csvFile.getPath().endsWith(".csv"))
		throw new CSVFileNotLoadedException("csvFile not loaded successfully");
		String item="";
		int noOfItems=getNumberOfColumns(csvFile);
		try(Scanner input=new Scanner(csvFile))
		{
			if(header)
			csvInfo=make2DArrayListHeader(getNumberOfRows(csvFile));
			else
			csvInfo=make2DArrayListNoHeader();
			while(input.hasNextLine() && !(item=input.nextLine()).isEmpty())
			{
				fillRow(item,noOfItems);
				item="";
			}
	
		}
		catch(IOException ex)
		{
			System.err.println("Corrupted or Unreadable file !");
			System.exit(0);
		}
		csvInfo.trimToSize();
		filterArrayList(csvInfo);
	}
	//overload
	public void getCSVInfo(File csvFile)
	{	
		if(!csvFile.isFile() || !csvFile.getPath().endsWith(".csv"))
		throw new CSVFileNotLoadedException("csvFile not loaded successfully");
		String item="";
		int noOfItems=getNumberOfColumns(csvFile);
		try(Scanner input=new Scanner(csvFile))
		{
			if(header)
			csvInfo=make2DArrayListHeader(getNumberOfRows(csvFile));
			else
			csvInfo=make2DArrayListNoHeader();
			while(input.hasNextLine() && !(item=input.nextLine()).isEmpty())
			{
				fillRow(item,noOfItems);
				item="";
			}
	
		}
		catch(IOException ex)
		{
			System.err.println("Corrupted or Unreadable file !");
			System.exit(0);
		}
		csvInfo.trimToSize();
		filterArrayList(csvInfo);
	}
	private void fillRow(String item,int n)
	{
		Scanner input=new Scanner(item);
		if(!header)csvInfo.add(new ArrayList<String>());
		input.useDelimiter(",|"+System.getProperty("line.separator"));
		String element="";
		for(int i=0;i<n;i++)
		{
			if(input.hasNext() && (element=input.next()).contains("\""))
			{
				input.useDelimiter("\"");
				element+=input.next();
				input.useDelimiter(",|"+System.getProperty("line.separator"));
				element+=input.next();
			}
			if(header)
			csvInfo.get(i).add(element);
			else
			csvInfo.get(csvInfo.size()-1).add(element);
		}
		
	}
	private ArrayList<ArrayList<String>> filterArrayList(ArrayList<ArrayList<String>> info)
	{
		for(int i=0,n=info.size();i<n;i++)
		{
			if(info.get(i).isEmpty()) 
			{
			info.remove(i--);
			n=info.size();
			}
		}
		return info;
	}
	private ArrayList<ArrayList<String>> make2DArrayListHeader(int rowsCount)
	{
		ArrayList<ArrayList<String>> ArrayList2D=new ArrayList<>();
		for(int i=0;i<rowsCount;i++)
		{
			ArrayList2D.add(new ArrayList<String>());
		}	
		return ArrayList2D;
	}
	private ArrayList<ArrayList<String>> make2DArrayListNoHeader()
	{
		return new ArrayList<ArrayList<String>>();
	}
	private int getNumberOfColumns(File csvFile)
	{
		int itemCount=0;
		try(Scanner input=new Scanner(csvFile))
		{
			if(input.hasNextLine())
			{
				itemCount++;
				char[] charArr=input.nextLine().toCharArray();
				for(int i=0;i<charArr.length;i++)
				{
					if(charArr[i]==',') itemCount++;
					else continue;
				}
			}		
		}catch(FileNotFoundException ex){System.err.println("File not Found, Exiting"); System.exit(0);}
		 catch(Throwable th){System.err.println("An Error in Memory Occured, Exiting"); System.exit(0);}
		return itemCount;
	}
	private int getNumberOfRows(File csvFile)
	{	
		int rowCount=0;
		try(Scanner input=new Scanner(csvFile))
		{
			while(input.hasNextLine()){if(input.nextLine().isEmpty())continue; rowCount++;}
			return rowCount;
		}catch(FileNotFoundException ex){System.err.println("File not Found, Exiting"); System.exit(0);}
		 catch(Throwable th){System.err.println("An Error in Memory Occured, Exiting"); System.exit(0);}
		
		return rowCount;
	}	
	
	private File getFile()	
	{
		JFileChooser chooser=new JFileChooser();
		try{
		do
		{
		System.out.println("Please select a CSV File");
		try{Thread.sleep(1000);}catch(InterruptedException ex){ ex.printStackTrace(); }
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setCurrentDirectory(new File("."));
		chooser.showOpenDialog(null);
		}while(chooser.getSelectedFile() == null && !chooser.getSelectedFile().getPath().endsWith(".csv"));
		}catch(NullPointerException ex){return getFile();}
		return chooser.getSelectedFile();
	}	
	public String[] searchCSV(String searchField,String returnField,String... searchTarget)
	{
		if(!isCSVInfoExists()) throw new CSVFileNotLoadedException("csvFile not loaded successfully");
		ArrayList<String> searchOutput=new ArrayList<>();
		String[] searchFieldArr=get(searchField);
		String[] returnFieldArr=get(returnField);
		if(searchFieldArr==null || returnFieldArr==null)
		{
		if(searchFieldArr==null) throw new SearchStringNotFoundException("the search field: "+searchField+" doesn't exist!");
		if(returnFieldArr==null) throw new SearchStringNotFoundException("the return field: "+returnField+" doesn't exist!");
		}
		for(int i=0;i<searchFieldArr.length;i++)
		{
			if(searchFieldArr[i].contains("\""))
			{
				String[] items=getItemsInQuotes(searchFieldArr[i]);
				if(isSearchTargetExists(items,searchTarget))
					searchOutput.add(returnFieldArr[i]);
			}
			else if(isSearchTargetExists(searchFieldArr[i],searchTarget))
			 searchOutput.add(returnFieldArr[i]);
		}
		return (!searchOutput.isEmpty())?searchOutput.toArray(new String[searchOutput.size()]):null;
		
	}
	public int searchCSVCount(String searchField,String...searchTarget)
	{
		if(!isCSVInfoExists())throw new CSVFileNotLoadedException("csvFile not loaded successfully");
		int count=0;
		String[] searchFieldArr=get(searchField);
		if(searchFieldArr==null)throw new SearchStringNotFoundException("the search field: "+searchField+" doesn't exist!");
		for(int i=0;i<searchFieldArr.length;i++)
		{
			if(searchFieldArr[i].contains("\""))
			{
				String[] items=getItemsInQuotes(searchFieldArr[i]);
				if(isSearchTargetExists(items,searchTarget))
				count++;
			}
			else if(isSearchTargetExists(searchFieldArr[i],searchTarget))
			count++;
		}
		return count;
	}
	private boolean isSearchTargetExists(String item,String[] searchTarget)
	{
		int foundCount=0;
		for(int j=0;j<searchTarget.length;j++)
		if(item.toUpperCase().contains(searchTarget[j].toUpperCase()))
		{
			System.out.println("found in array "+j);
			foundCount++;		
		}
		
		return (foundCount==searchTarget.length)?true:false;
		
	}
	//overload
	private boolean isSearchTargetExists(String[] items,String[] searchTarget)
	{
		int foundCount=0;
		for(int i=0;i<items.length;i++)
		{
			for(int j=0;j<searchTarget.length;j++)
			if(items[i].toUpperCase().contains(searchTarget[j].toUpperCase()))
			{
				foundCount++;		
			}
		}
		return (foundCount==searchTarget.length)?true:false;
	}
	private String removeQuotes(String str)
	{
		int quoteIndex=str.indexOf("\"");
		int unquoteIndex=str.indexOf("\"",quoteIndex+1);
		str=str.substring(quoteIndex+1,unquoteIndex);
		return str;
	}
	private String validateSearch(String search)
	{
		for(int i=0,n=csvInfo.size();i<n;i++)
		{
			if(csvInfo.get(i).get(0).toUpperCase().startsWith(search.toUpperCase()))
			search=csvInfo.get(i).get(0);
		}
		return search;
	}
	private String[] getItemsInQuotes(String str)
	{
		
		String[] items=removeQuotes(str).split(",");
		return removeWhiteSpaces(items);
	}
	private String[] removeWhiteSpaces(String[] items)
	{
		for(int i=0;i<items.length;i++)
		items[i]=items[i].trim();	
		return items;
	}
	public void print(String[]... lists)
	{
		for(int i=0,n=lists[i].length;i<n;i++)  
		{
			for(int j=0;j<lists.length;j++)
			{
				System.out.print(lists[j][i] + " " );
			}
		System.out.println();
		}
	}
	public static ArrayList<CSVParser> makeArrayList(boolean headerExists)
	{
		ArrayList<CSVParser> csvArr=new ArrayList<>();
		File[] files=getFiles();
		for(int i=0;i<files.length;i++)
		{
			csvArr.add(new CSVParser(files[i],headerExists));
		}
		return csvArr;
	}
	private static File[] getFiles()
	{
		JFileChooser chooser=new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setMultiSelectionEnabled(true);
		chooser.setCurrentDirectory(new File("."));
		File[] files=null;
		try{
		do{
		System.out.println("Please select one ore multiple csv files"); 
		try{ Thread.sleep(1000); } catch(InterruptedException ex) {ex.printStackTrace();}
		chooser.showOpenDialog(null);
		files=(chooser.getSelectedFiles().length>0&&validateFiles((files=chooser.getSelectedFiles())))?files:null;
		}while(files==null);
		}catch(NullPointerException ex){return getFiles();}
		return files;
	}
	private static boolean validateFiles(File[] files)
	{
		for(int i=0;i<files.length;i++)
		{
			if(!files[i].isFile() || !files[i].getPath().endsWith(".csv"))
			return false;
		}
		return true;
	}
}