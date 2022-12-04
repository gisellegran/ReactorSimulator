public class InputException extends Exception {

    public InputException() {
        super("Invalid input entered.");
    }

    public InputException clone() {
        return this;
    }

    public boolean equals(Object comparator) {
        if (comparator == null) return false;
        else if (this.getClass() != comparator.getClass()) return false;
        return true;
    }
}
