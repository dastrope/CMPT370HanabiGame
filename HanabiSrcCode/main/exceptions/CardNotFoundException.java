package exceptions;

/**	This exception is used whenever a card position is invalid within a hand. */
public class CardNotFoundException extends Exception
{
	/**	Create an exception with the specified message. <br>
		Analysis: Time = O(1) */
	public CardNotFoundException(String message)
	{
		super(message);
	}

	/**	Create an exception with the default message. <br>
		Analysis: Time = O(1) */
	public CardNotFoundException()
	{
		super("CardNotFoundException thrown!");
	}

} 
