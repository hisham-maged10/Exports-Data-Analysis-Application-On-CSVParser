/**
 * @author ${hisham_maged10}
 *https://github.com/hisham-maged10
 * ${DesktopApps}
 */
import java.util.ArrayList;
import java.util.Scanner;
import java.util.NoSuchElementException;
import static java.lang.System.out;
import static java.lang.System.err;
//then upload to github, add license and authority
public class TestExportsCSV
{
	public static void main(String[] args)
	{
		testing();
	}	
	
	/*------------------------------------------------------------Export Analysis Section--------------------------------------------------------*/
	/* A method made to list all the information about a certain country's name given by user */
	public static void listCountryCSVInfo(CSVParser csvParser,String searchTarget)
	{
		//gets all countries from the CSVParser
		String[] countries=csvParser.get("country");
		//gets all exports from the CSVParser
		String[] exports=csvParser.get("export");
		//gets all Value of exports from the CSVParser
		String[] dollars=csvParser.get("value");
		//gets where is that country in the countries String array
		int neededCounter=getNeededCounterList(countries,searchTarget);
		//in case it was found print the country and it's exports and it's values
		if(neededCounter!=-1)
		System.out.println(countries[neededCounter]+":"+" "+exports[neededCounter]+": "+dollars[neededCounter]);	
		//if not found print "NOT FOUND"
		else
		System.out.println("NOT FOUND");
	}
	/* searches in the string[] of column for a specific search target and returns it's index if found, if not returns -1 */
	public static int getNeededCounterList(String[] searchField,String searchTarget)
	{
		for(int i=0;i<searchField.length;i++)
		{
			if(searchField[i].equalsIgnoreCase(searchTarget))
			return i;	
		}
		return -1;
	}
	/* searches in the string[] of value of exports column for the indexes that has a value higher than given export's value and
	returns their indexes then prints those countries that have the found higher values */
	public static void listCountryExportValueBigger(CSVParser csvParser,String value)
	{
		//parses the given String format to long in order not to overflow if bigger than int's range
		long parsedValue=getValueFromFormatString(value);
		//gets all value of exports from the CSVParser
		String[] valuesArr=csvParser.get("value");
		//if values array is equal to a null reference then it was not loaded successfully so throw a user-defined exception called CSVFileNotLoadedException that belongs to the csvParser
		if(valuesArr==null)throw new CSVFileNotLoadedException("csvFile not loaded successfully");
		//gets all countries from the csvParser
		String[] countryArr=csvParser.get("country");
		//if countries array is equal to a null reference then it was not loaded successfully so throw a user-defined exception called CSVFileNotLoadedException that belongs to the csvParser
		if(countryArr==null)throw new CSVFileNotLoadedException("csvFile not loaded successfully");
		//make a new ArrayList of Integer that will auto wrap the given int values using boxing holding the indexes of the needed countries
		ArrayList<Integer> indexes=new ArrayList<>();
		//gets the wanted indexes
		for(int i=0;i<valuesArr.length;i++)
		{
			if(compareDollarValues(parsedValue,getValueFromFormatString(valuesArr[i])))
			indexes.add(i);
		}
		//if there is an output print them
		if(indexes.size()>0)
		for(Integer e:indexes)
			System.out.println(countryArr[e] +" "+valuesArr[e]);
		else
			System.out.println("No country has exports' value higher than: "+value);
	}
	/* compares givens */
	private static boolean compareDollarValues(long given,long needed)
	{
		return (needed>given)?true:false;	
	}
	/* gets and validates the value of the string to be of the correct format in the columns */
	private static long getValueFromFormatString(String value)
	{
		//if the value had commas in it then there will be quotes so they must be removed to now throw NumberFormatException
		if(value.contains("\"")) value=removeQuotes(value);
		//make an arraylist having the value of value	
		StringBuilder valueStrB=new StringBuilder(value);
		//if the first char is a dollar sign, then remove it
		if(value.startsWith("$"))
		valueStrB.deleteCharAt(0);
		//initialize a string called dollarPart
		String dollarPart="";
		int j=0;
		//loop each char in the stringbuilder till the first comma and remove them
		while(valueStrB.charAt(j)!=',')
		{
			dollarPart+=valueStrB.charAt(j++);
			
		}
		// delete the part of the dollar sign
		valueStrB.delete(0,dollarPart.length());
		//loop and remove the commas from the remaining stringBuilder and shift the places backward
		for(int i=0;i<valueStrB.length();i++)
		{
			if(valueStrB.charAt(i)==',') valueStrB.deleteCharAt(i--);
			else continue;
		}
		//trim the stringbuilder to the size of the actual characters
		valueStrB.trimToSize();
		//validate the stringBuilder, if not valid then throw a user defined  exception called WrongValueFormatException
		if(!validateValueFormat(valueStrB.toString()))throw new WrongValueFormatException(" a String in the format of a dollar sign, followed by an integer number with a comma separator every three digits from the right.");
		//insert the dollar part from at the beginning of the stringbuilder
		valueStrB.insert(0,dollarPart);
		//return the stringbuilder to a string and rereference the String value to be of it's value
		value=valueStrB.toString();
		//return the parsed long of the string value
		return Long.parseLong(value);
	}
	/* validates the string value of the string bulder, if it was a multiple of three after the dollar sign and it's numbers part then it's valid, if not then unvalid */
	private static boolean validateValueFormat(String value)
	{
		//if it's a multiple of three then it's remainder if divided by three will equal zero, if not then not a multiple of three
		return (value.length()%3==0)?true:false;
	}
	/* A method that returns a String without the quote and unquote characters */
	private static String removeQuotes(String str)
	{
		//gets the index of the first quote character
		int quoteIndex=str.indexOf("\"");
		//gets the index of the first unquote character after the first one
		int unquoteIndex=str.indexOf("\"",quoteIndex+1);
		//substrings from a string the part in between , quoteIndex+1 to not include the first quote and unquote index as it's exclusive not inclusive
		//equate it to str as substring returns a string, but doesn't change the value of the str itself as String class is immutable
		str=str.substring(quoteIndex+1,unquoteIndex);
		return str;
	}	
	public static void printExportsHigher(CSVParser csvParser)
	{
		System.out.print("Please Enter the Value in format $...,XXX,XXX,XXX: ");
		try 
		{
		listCountryExportValueBigger(csvParser,new Scanner(System.in).nextLine());
		}catch(StringIndexOutOfBoundsException ex){System.out.println("Incorrect Format!"); guiController(csvParser);}
		catch(NoSuchElementException ex){System.out.println("Termination by outer source, exiting"); System.exit(0);}
	}
	public static void printData(CSVParser csvParser)
	{
		System.out.println("Country Exports Value");
		csvParser.print(csvParser.get("country"),csvParser.get("exports"),csvParser.get("val"));	
	}
	public static void printSearchData(CSVParser csvParser)
	{
		Scanner input=new Scanner(System.in);
		System.out.print("Enter number of exports: ");
		String[] searchFields=null;
		try{
		int length=Integer.parseInt(input.nextLine());
		if(length==0){System.out.println("Incorrect response!"); guiController(csvParser);}
		searchFields=new String[length];
		for(int i=0;i<length;i++){out.print("Enter export#"+(i+1)+":");searchFields[i]=input.nextLine();}
		}catch(NumberFormatException ex){System.out.println("Incorrect response!"); guiController(csvParser);}
		catch(NoSuchElementException ex){System.out.println("Termination by outer source, exiting"); System.exit(0);}
		String[] searchOutput=csvParser.searchCSV("exports","country",searchFields);
		if(searchOutput==null){System.out.println("No Country found"); guiController(csvParser);}
		for(String e:searchOutput)
		System.out.println(e);
	}
	public static void printCountryInfo(CSVParser csvParser)
	{
		System.out.print("Enter the country you want: ");
		try{listCountryCSVInfo(csvParser,new Scanner(System.in).nextLine());}
		catch(NoSuchElementException ex){System.out.println("Termination by outer source, exiting"); System.exit(0);}
	}
	public static void printCSVCount(CSVParser csvParser)
	{
		System.out.print("Enter the export you want to count for: ");
		try{System.out.println(csvParser.searchCSVCount("exports",new Scanner(System.in).nextLine()));}
		catch(NoSuchElementException ex){System.out.println("Termination by outer source, exiting"); System.exit(0);}
	}
	public static void testing()
	{
		CSVParser csvParser=new CSVParser();
		guiController(csvParser);
	}
	/*------------------------------------------------------------GUI SECTION--------------------------------------------------------*/
	public static void guiController(CSVParser csvParser)
	{	
		boolean execute=true;
		do
		{
			gui();
			execute=doOperation(csvParser);
			for(int i=0;i<15;i++) out.print("-"); out.println();
		}while(execute);
	}
	public static boolean doOperation(CSVParser csvParser)
	{
		Scanner input=new Scanner(System.in);
		int choice=0;
		try{choice=Integer.parseInt(input.nextLine());}catch(NumberFormatException ex){out.println("Incorrect Response");guiController(csvParser);}	
		catch(NoSuchElementException ex){System.out.println("Termination by outer source, exiting"); System.exit(0);}
		switch(choice)
		{
			case 1:printData(csvParser);break;
			case 2:printSearchData(csvParser);break;
			case 3:printExportsHigher(csvParser);break;
			case 4:printCountryInfo(csvParser);break;
			case 5:printCSVCount(csvParser);break;
			case 6:return false;
			default:out.println("Incorrect input!");
		}
		return continueOperation();
	}
	private static boolean continueOperation()
	{
		out.print("Do you want to Reuse the program? (y/n)");
		switch(new Scanner(System.in).nextLine().toLowerCase().charAt(0))
		{
			case 'y':
			case '1':return true;
			case 'n':
			case '0':return false;
			default:out.println("Incorrect response"); return continueOperation();
		}
		
	}
	public static void gui()
	{
		out.print("\nExports' Data Analysis (Application on CSVParser) Version 1.0\n----------------------------------------------------------------\n"+
		"1.Print all data\n"+
		"2.List countries that have Specific Exports (will specify Exports) \n"+
		"3.Print countries having export's value higher than what you specify (will specify export value in format $x,xxx,xxx,xxx)\n"+
		"4.List country's Info (will specify Country's name)\n"+
		"5.Print how many country exports a certain export (will specify export)\n"+
		"6.Exit Program\n"+
		"Enter your Choice: ");
	}	
	
}