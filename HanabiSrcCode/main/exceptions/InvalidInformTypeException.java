package exceptions;

/**	This exception is used whenever a Invalid Inform is attempted. */
public class InvalidInformTypeException extends Exception
{
	/**	Create an exception with the specified message. <br> 
		Analysis: Time = O(1) */
	public InvalidInformTypeException(String message)
	{
		super(message);
	}

	/**	Create an exception with the default message. <br> 
		Analysis: Time = O(1) */
	public InvalidInformTypeException()
	{
		super("InvalidInformTypeException thrown!");
	}

} 
