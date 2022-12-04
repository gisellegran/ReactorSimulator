package chemistry;

public enum Phase {
  LIQUID,IDEALGAS, L_G;

  public static Phase findByName(String id) {
    if (id == null) {
      //TODO: error handling
    }
    Phase result = null; //TODO: error handling?

    switch (id.toLowerCase()){
      case "l":
        id = "liquid";
        break;
      case "g":
        id = "idealgas";
        break;
    }

    for (Phase elem : values()) {
      if (elem.name().equalsIgnoreCase(id)) {
        result = elem;
        break;
      }
    }
    return result;
  }
}
