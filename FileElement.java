import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.io.FileNotFoundException;
import java.io.File;


public class FileElement extends File
{
	//==================ATTRIBUTES
	public final static byte NOTE = 0, FOLDER = 1, NONE = -1;

	private byte typeId;

	private FileReaderWriter readWrite;

	//==================CONSTRUCTORS==================
	public FileElement (File file) throws FileNotFoundException
	{
		super(file.getAbsolutePath());

		readWrite = new FileReaderWriter(this);
		if(this.exists())
		{

			this.typeId = getTypeId();
		} else
		{
			throw new FileNotFoundException("file does not exist; FileElements(File file): ");
		}
	}

	public FileElement (String file) throws FileNotFoundException
	{
		super(file);

		if(this.exists())
		{
			readWrite = new FileReaderWriter(file);
			this.typeId = getTypeId();
		} else
		{
			throw new FileNotFoundException("file does not exist (FileElements: FileElements(String file)");
		}
	}

	public FileElement (String file, byte newTypeId, byte in_importance) throws Exception
	{	
		super(file);
		readWrite = new FileReaderWriter(this);
		this.typeId = newTypeId;
		switch(newTypeId)
		{
			case NOTE:
				readWrite.setContent("");
				if(!setImportance(in_importance)) setImportance((byte)0);
				break;
			case FOLDER:
				this.mkdir();
				break;
			default:
				throw new Exception("Invalid Type id: FileElement(String file, byte newTypeId, byte in_importance)");
		}
	}
	public FileElement (File parent, String file, byte newTypeId, byte in_importance) throws Exception
	{	
		super(parent, file);
		readWrite = new FileReaderWriter(this);
		this.typeId = newTypeId;
		switch(newTypeId)
		{
			case NOTE:
				readWrite.setContent("");
				if(!setImportance(in_importance)) setImportance((byte)0);
				break;
			case FOLDER:
				this.mkdir();
				break;
			default:
				throw new Exception("Invalid Type id: FileElement(File parent, String file, byte newTypeId, byte in_importance)");
		}
	}
	//---Constructors helpers	
	private byte getTypeId ()
	{
		if(this.isFile()) return NOTE;
		if(this.isDirectory()) return FOLDER;
		return NONE;
	}

	//=========GETTERS AND SETTERS
	public int getImportance ()
	{
		if(typeId != NONE)
		{
			char num = super.getName().charAt(0);
			return Character.getNumericValue(num);
		}
		return -1;
	}
	public boolean setImportance(byte n)
	{
		if(n >= 0 && n <= 5)
		{
			readWrite.setName(n+getName());
			return true;
		}
		return false;
	}
	public String getBody()
	{
		if(typeId ==NOTE)
		{
			return readWrite.getContent();
		}
		return "";
	}  
	public String getName()
	{
		if(typeId != NONE)
		{
			try{
				String name = super.getName();
				return name.substring(1, name.length());
			} catch(Exception e)
			{
				System.out.println("ERROR: could not find importance 124 FileElement");
				return "";
			}
		}
		return "";
	}
	public Date getDate()
	{
		if(typeId != NONE)
			return readWrite.getDateLastModified();
		return new Date(0);
	}

	public void setBody (String body)
	{
		if(typeId == NOTE)
		{
			readWrite.setContent(body);
		}
	}

	public boolean setName (String name)
	{
		if(typeId == NONE)
			return false;
		String prcsName = name;
		String newImportance = "";
		if(name.length() >= 3)
		{
			if(name.charAt(0) == '[' && name.charAt(2) == ')')
			{
				newImportance = ""+ name.charAt(1);
				if(name.length() >= 4 && name.charAt(3) == ' ')
					prcsName = name.substring(4, name.length());
				else
					prcsName = name.substring(3,name.length());
			}else
				newImportance = ""+ getImportance();
		}
		return readWrite.setName(newImportance+prcsName);
	}

	public boolean deleteThisFile()
	{
		if(typeId == NOTE) {
			try {
				Files.delete(Paths.get(this.getPath()));
				typeId = NONE;
				return true;
			} catch (Exception e) {
				return false;
			}
		} else if(typeId == FOLDER)
		{
			return deleteDirectory(this);
		}
		return false;
	}

	private boolean deleteDirectory(File directory) {
		if(directory.exists()){
			File[] files = directory.listFiles();
			if(null!=files){
				for(int i=0; i<files.length; i++) {
					if(files[i].isDirectory()) {
						deleteDirectory(files[i]);
					}
					else {
						files[i].delete();
					}
				}
			}
		}
		return(directory.delete());
	}

	//=========
	public String toString()
	{
		return "["+getImportance()+") "+this.getName();
	}
}