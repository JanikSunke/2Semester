package Interfaces;

import java.util.List;
import java.util.Map;

public interface IReporting {

    int getTotalCreditCount();

    Map<String, Integer> generateProductionCreditsCount(IProduction production);

    Map<String, Integer> generateCreditTypeCount();

    Map<String, Integer> generate10MostCredited();

    List<String> generateCreditsReport();
}
