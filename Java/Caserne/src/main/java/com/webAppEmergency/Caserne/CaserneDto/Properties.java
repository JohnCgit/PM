package com.webAppEmergency.Caserne.CaserneDto;

public class Properties {
    private String nom;
    private Theme theme;
    private Soustheme soustheme;
    private String identifiant;
    private String idexterne;
    private String siret;
    private String datecreation;
    private long gid;

    public String getNom() { return nom; }
    public void setNom(String value) { this.nom = value; }

    public Theme getTheme() { return theme; }
    public void setTheme(Theme value) { this.theme = value; }

    public Soustheme getSoustheme() { return soustheme; }
    public void setSoustheme(Soustheme value) { this.soustheme = value; }

    public String getIdentifiant() { return identifiant; }
    public void setIdentifiant(String value) { this.identifiant = value; }

    public String getIdexterne() { return idexterne; }
    public void setIdexterne(String value) { this.idexterne = value; }

    public String getSiret() { return siret; }
    public void setSiret(String value) { this.siret = value; }

    public String getDatecreation() { return datecreation; }
    public void setDatecreation(String value) { this.datecreation = value; }

    public long getGid() { return gid; }
    public void setGid(long value) { this.gid = value; }
}