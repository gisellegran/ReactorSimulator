package reactor.heat_transfer;

public enum HeatTransferCondition {
    ISOTHERMAL, ADIABATIC, FREE_CONVECTIVE, COCURRENT, COUNTERCURRENT;

    public static HeatTransferCondition findByName(String id) {
        HeatTransferCondition result = null; //TODO: error handling?
        for (HeatTransferCondition elem : values()) {
            if (elem.name().equalsIgnoreCase(id)) {
                result = elem;
                break;
            }
        }
        return result;
    }


}
