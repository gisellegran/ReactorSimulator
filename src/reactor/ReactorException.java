package reactor;

public class ReactorException extends Exception{

    public ReactorException(String message)
    {
        super(message);
    }

    public ReactorException()
    {
        super("Failure to solve reactor");
    }
}
