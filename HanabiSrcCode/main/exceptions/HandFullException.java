package exceptions;

/**	This exception is used whenever a card is added to a full hand. */
public class HandFullException extends Exception
{
	/**	Create an exception with the specified message. <br>
		Analysis: Time = O(1) */
	public HandFullException(String message)
	{
		super(message);
	}

	/**	Create an exception with the default message. <br>
		Analysis: Time = O(1) */
	public HandFullException()
	{
		super("HandFullException thrown!");
	}

} 
