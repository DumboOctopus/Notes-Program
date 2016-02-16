
//scanner stuff
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.File;

//buffered reader stuff
import java.io.IOException;
import java.nio.charset.Charset;
import java.io.BufferedWriter;
import java.nio.file.*;

//date
import java.util.Date;

//
import java.lang.SecurityException;
import java.lang.NullPointerException;


public class FileReaderWriter
{
	
	private File file;

	//========Constructors
	public FileReaderWriter(File file)
	{
		this.file = file;
	}
	public FileReaderWriter(String fileName)
	{
		try
		{
			this.file = new File(fileName);
		} 
		catch(NullPointerException e)
		{
			System.out.println("Lol file didn't work: " + fileName);
			e.printStackTrace();
		}
	}

	//====Reading and writing 
	public String getContent()
	{
		StringBuilder builder = new StringBuilder();
		if(file.isDirectory()) 
		{
			System.out.println("ERROR: tried to read to directory; 45 FileReaderWriter.java");
			return "";
		}
		try
		{
			Scanner scan = new Scanner(file);
			scan.useDelimiter("\n");
			while(scan.hasNext())
			{
				builder.append( scan.next());
				builder.append("\n");
			}

			scan.close();
			return builder.toString();
		} catch(FileNotFoundException e)
		{
			e.printStackTrace();
			return builder.toString();
		}	
	}
	
	

	public void setContent(String newData)
	{
		Path path = this.file.toPath();
		Charset charset = Charset.forName("US-ASCII");
		try (BufferedWriter writer = Files.newBufferedWriter(path, charset)) 
		{
		    writer.write(newData, 0, newData.length());
		} catch (IOException x) 
		{
		    System.err.format("IOException: %s%n", x);
		}
	}


	//get
	public Date getDateLastModified ()
	{
		try 
		{
			return new Date(file.lastModified());
		} catch (SecurityException e)
		{
			e.printStackTrace();
			return new Date(0);
		}
	}
	public String getName()
	{
		return file.getName();
	}

	public boolean setName (String name)
	{
		if(!file.exists()) return false;

		String parent = file.getParent();
		File newFile = new File(parent, name);
		if(!file.renameTo(newFile))
		{
			System.out.println("ERROR: file not able to rename 116 FileReaderWriter");
			return false;
		}
		this.file = newFile;
		return true;
	}

}