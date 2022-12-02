package reactor.heat_transfer;

public class CounterCurrentSolver extends HeatTransferEquation{

    public CounterCurrentSolver(CounterCurrentSolver source){
        super(source);

    }

    public CounterCurrentSolver clone(){
        return new CounterCurrentSolver(this);
    }
}
