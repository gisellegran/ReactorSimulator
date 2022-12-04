public class InputException extends RuntimeException {

    public InputException(String message) {
        super(message);
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
