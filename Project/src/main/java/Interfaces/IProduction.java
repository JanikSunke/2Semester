package Interfaces;

import java.util.List;
import java.util.Map;

public interface IProduction {
    String getProductionID();
    String getName();
    Map<IRightsholder, List<String>> getRightsholders();
    List<String> getRightsholderRole(IRightsholder rightsholder);
}