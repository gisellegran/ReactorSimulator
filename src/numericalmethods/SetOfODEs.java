package numericalmethods;
//the interface is implemented by the class that defines the set of coupled O.D.E.s
//that class must give concrete definition to calculateValue, which represents the rhs functions of the set of O.D.E.s
public interface SetOfODEs
{
   public double[] calculateValue(double x, double[] y);
}
