/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ecommerce;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Korsn
 */
@Entity
@Table(name = "proizvod")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Proizvod.findAll", query = "SELECT p FROM Proizvod p")
    , @NamedQuery(name = "Proizvod.findByIdProizvod", query = "SELECT p FROM Proizvod p WHERE p.idProizvod = :idProizvod")
    , @NamedQuery(name = "Proizvod.findByRokupotrebe", query = "SELECT p FROM Proizvod p WHERE p.rokupotrebe = :rokupotrebe")
    , @NamedQuery(name = "Proizvod.findByStanje", query = "SELECT p FROM Proizvod p WHERE p.stanje = :stanje")
    , @NamedQuery(name = "Proizvod.findByMinimum", query = "SELECT p FROM Proizvod p WHERE p.minimum = :minimum")})
public class Proizvod implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PROIZVOD", nullable = false)
    private Integer idProizvod;
    @Lob
    //@Size(max = 16777215)
    @Column(name = "NAZIV_PROIZVOD", length = 16777215)
    private String nazivProizvod;
    @Column(name = "ROKUPOTREBE", length = 16777215)
   // @Temporal(TemporalType.TIMESTAMP)
    private String rokupotrebe;
    @Column(name = "STANJE")
    private Integer stanje;
    @Column(name = "MINIMUM")
    private Integer minimum;
  //  @JoinColumn(name = "ID_PRODAVNICA", referencedColumnName = "ID_PRODAVNICA")
  //  @ManyToOne
    @Column(name = "ID_PRODAVNICA")
    private Integer idProdavnica;
 //   @JoinColumn(name = "ID_USER", referencedColumnName = "ID_USER")
  //  @ManyToOne
    @Column(name = "ID_USER")
    private Integer idUser;

    public Proizvod() {
    }

    public Proizvod(Integer idProizvod) {
        this.idProizvod = idProizvod;
    }

    public Integer getIdProizvod() {
        return idProizvod;
    }

    public void setIdProizvod(Integer idProizvod) {
        this.idProizvod = idProizvod;
    }

    public String getNazivProizvod() {
        return nazivProizvod;
    }

    public void setNazivProizvod(String nazivProizvod) {
        this.nazivProizvod = nazivProizvod;
    }

    public String getRokupotrebe() {
        return rokupotrebe;
    }

    public void setRokupotrebe(String rokupotrebe) {
        this.rokupotrebe = rokupotrebe;
    }

    public Integer getStanje() {
        return stanje;
    }

    public void setStanje(Integer stanje) {
        this.stanje = stanje;
    }

    public Integer getMinimum() {
        return minimum;
    }

    public void setMinimum(Integer minimum) {
        this.minimum = minimum;
    }

    public Integer getIdProdavnica() {
        return idProdavnica;
    }

    public void setIdProdavnica(Integer idProdavnica) {
        this.idProdavnica = idProdavnica;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idProizvod != null ? idProizvod.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Proizvod)) {
            return false;
        }
        Proizvod other = (Proizvod) object;
        if ((this.idProizvod == null && other.idProizvod != null) || (this.idProizvod != null && !this.idProizvod.equals(other.idProizvod))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ecommerce.Proizvod[ idProizvod=" + idProizvod + " ]";
    }
    
}
