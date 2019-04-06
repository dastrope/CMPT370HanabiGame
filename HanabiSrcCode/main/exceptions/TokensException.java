package exceptions;

/**	This exception is used whenever a token operation fails. */
public class TokensException extends Exception
{
	/**	Create an exception with the specified message. <br> 
		Analysis: Time = O(1) */
	public TokensException(String message)
	{
		super(message);
	}

	/**	Create an exception with the default message. <br> 
		Analysis: Time = O(1) */
	public TokensException()
	{
		super("TokensException thrown!");
	}

} 
