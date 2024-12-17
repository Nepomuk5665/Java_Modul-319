import java.time.LocalDate;

enum ProduktStatus {
    VERFUEGBAR,
    VERKAUFT,
    RESERVIERT
}

interface Verkaufbar {
    void verarbeiteVerkauf() throws LagerException;
}

abstract class Produkt {
    protected String name;
    protected double preis;
    protected ProduktStatus status;

    public Produkt(String name, double preis) {
        this.name = name;
        this.preis = preis;
        this.status = ProduktStatus.VERFUEGBAR;
    }
    public abstract String getProduktInfo();


    public double getPreis() {
        return preis;
    }

    public String getName() {
        return name;
    }

    public ProduktStatus getStatus() {
        return status;
    }

    public void setStatus(ProduktStatus status) {
        this.status = status;
    }
}

class ElektronikProdukt extends Produkt implements Verkaufbar {
    private String hersteller;
    private int garantieZeit;

    public ElektronikProdukt(String name, double preis, String hersteller, int garantieZeit) {
        super(name, preis);
        this.hersteller = hersteller;
        this.garantieZeit = garantieZeit;
    }

    @Override
    public String getProduktInfo() {
        return String.format("Elektronik: %s von %s (Garantie: %d Monate)",
                name, hersteller, garantieZeit);
    }

    @Override
    public void verarbeiteVerkauf() throws LagerException {
        if (status == ProduktStatus.VERKAUFT) {
            throw new LagerException("Produkt bereits verkauft");
        }
        if (garantieZeit <= 0) {
            throw new LagerException("Produkt ohne gültige Garantie");
        }
        status = ProduktStatus.VERKAUFT;
    }
}

class LebensmittelProdukt extends Produkt implements Verkaufbar {
    private String haltbarkeitsDatum;

    public LebensmittelProdukt(String name, double preis, String haltbarkeitsDatum) {
        super(name, preis);
        this.haltbarkeitsDatum = haltbarkeitsDatum;
    }

    @Override
    public String getProduktInfo() {
        return String.format("Lebensmittel: %s (Haltbar bis: %s)",
                name, haltbarkeitsDatum);
    }

    @Override
    public void verarbeiteVerkauf() throws LagerException {
        if (status == ProduktStatus.VERKAUFT) {
            throw new LagerException("Produkt bereits verkauft");
        }
        LocalDate haltbarkeit = LocalDate.parse(haltbarkeitsDatum);
        if (LocalDate.now().isAfter(haltbarkeit)) {
            throw new LagerException("Produkt abgelaufen");
        }
        status = ProduktStatus.VERKAUFT;
    }
}

class LagerException extends Exception {
    public LagerException(String message) {
        super(message);
    }
}