package net.aesircraft.VisCraft.Machines;

public class Machine {

    private int id;
    private Object Machine;
    private boolean isExtractor = false;
    private boolean isInfuser = false;
    private boolean isCondenser = false;
    private boolean isCollector = false;

    public void Machine(Object machine) {
        if (machine instanceof Extractor) {
            Extractor extractor = (Extractor) machine;
            id = extractor.getID();
            Machine = machine;
            isExtractor = true;
        }
        if (machine instanceof Infuser) {
            Infuser infuser = (Infuser) machine;
            id = infuser.getID();
            Machine = machine;
            isInfuser = true;
        }
        if (machine instanceof Condenser) {
            Condenser condenser = (Condenser) machine;
            id = condenser.getID();
            Machine = machine;
            isCondenser = true;
        }
        if (machine instanceof Collector) {
            Collector collector = (Collector) machine;
            id = collector.getID();
            Machine = machine;
            isCollector = true;
        }
    }

    public Object getMachine() {
        return Machine;
    }

    public int getID() {
        return id;
    }

    public boolean isExtractor() {
        return isExtractor;
    }

    public boolean isInfuser() {
        return isInfuser;
    }

    public boolean isCondenser() {
        return isCondenser;
    }

    public boolean isCollector() {
        return isCollector;
    }
}
