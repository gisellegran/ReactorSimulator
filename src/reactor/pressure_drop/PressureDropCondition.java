package reactor.pressure_drop;

public enum PressureDropCondition {
    ISOBARIC, ERGUN_CORRELATION;

    public static PressureDropCondition findByName(String id) {
        PressureDropCondition result = null; //TODO: error handling?
        for (PressureDropCondition elem : values()) {
            if (elem.name().equalsIgnoreCase(id)) {
                result = elem;
                break;
            }
        }
        return result;
    }
}
