package jmetal.metrics.concernDrivenMetrics.concernCohesion;

import java.util.ArrayList;
import java.util.Collection;

import arquitetura.representation.Architecture;
import arquitetura.representation.Class;

public class LCCClass {

    private final Architecture architecture;
    private final Collection<LCCClassComponentResult> results = new ArrayList<LCCClassComponentResult>();

    public LCCClass(Architecture architecture) {
        this.architecture = architecture;
        for (Class cls : architecture.getAllClasses()) {
            //Thaina - 01/14 - se classe for um aspecto ela não é contada nas métricas
            //if (!cls.isAspect()) {
                getResults().add(new LCCClassComponentResult(cls));
            //}
        }
    }

    public Architecture getArchitecture() {
        return architecture;
    }

    public Collection<LCCClassComponentResult> getResults() {
        return results;
    }
}
