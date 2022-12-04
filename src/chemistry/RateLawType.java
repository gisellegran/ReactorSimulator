package chemistry;

public enum RateLawType {
    POWER, ELEMENTARY, POWER_REVERSIBLE, ELEMENTARY_REVERSIBLE;

    public static RateLawType findByName(String id) {
        RateLawType result = null; //TODO: error handling?
        //values() return an array of all the value for this enum
        for (RateLawType elem : values()) {
            if (elem.name().equalsIgnoreCase(id)) {
                result = elem;
                break;
            }
        }
        return result;
    }

    public static RateLawType getTypeOfRate(boolean isElementary, boolean isReversible) {

        if (isElementary && isReversible) return RateLawType.ELEMENTARY_REVERSIBLE;
        if (!isElementary && isReversible) return RateLawType.POWER_REVERSIBLE;
        if (isElementary && !isReversible) return RateLawType.ELEMENTARY;

        //power is default
        return RateLawType.POWER;

    }


}
