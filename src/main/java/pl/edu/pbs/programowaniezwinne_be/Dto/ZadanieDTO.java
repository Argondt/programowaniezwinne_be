package pl.edu.pbs.programowaniezwinne_be.Dto;

public class ZadanieDTO {
    private String nazwa;

    public void setKolejnosc(Long kolejnosc) {
        this.kolejnosc = kolejnosc;
    }

    private Long kolejnosc;
    private String opis;

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public Long getKolejnosc() {
        return kolejnosc;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public Long getProjektId() {
        return projektId;
    }

    public void setProjektId(Long projektId) {
        this.projektId = projektId;
    }

    private Long projektId;
}