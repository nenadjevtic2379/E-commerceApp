/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ecommerce;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Korsn
 */
@Entity
@Table(name = "prodavnica")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Prodavnica.findAll", query = "SELECT p FROM Prodavnica p")
    , @NamedQuery(name = "Prodavnica.findByIdProdavnica", query = "SELECT p FROM Prodavnica p WHERE p.idProdavnica = :idProdavnica")})
public class Prodavnica implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
   // @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PRODAVNICA", nullable = false)
    private Integer idProdavnica;
    @Lob
    //@Size(max = 16777215)
    @Column(name = "NAZIV_PRODAVNICA", length = 16777215)
    private String nazivProdavnica;
   // @OneToMany(mappedBy = "idProdavnica")
   // private Collection<Proizvod> proizvodCollection;
   /* @JoinColumn(name = "ID_USER", referencedColumnName = "ID_USER")
    @ManyToOne */
    @Column(name = "ID_USER")
    private Integer idUser;
    
    @Column(name = "ID_ZADUZENOG", nullable = true)
    private Integer idZaduzenog;

    public Prodavnica() {
    }

    public Prodavnica(Integer idProdavnica) {
        this.idProdavnica = idProdavnica;
    }

    public Integer getIdProdavnica() {
        return idProdavnica;
    }

    public void setIdProdavnica(Integer idProdavnica) {
        this.idProdavnica = idProdavnica;
    }

    public Integer getIdZaduzenog() {
        return idZaduzenog;
    }

    public void setIdZaduzenog(Integer idZaduzenog) {
        this.idZaduzenog = idZaduzenog;
    }
    
    

    public String getNazivProdavnica() {
        return nazivProdavnica;
    }

    public void setNazivProdavnica(String nazivProdavnica) {
        this.nazivProdavnica = nazivProdavnica;
    }

  /*  @XmlTransient
    public Collection<Proizvod> getProizvodCollection() {
        return proizvodCollection;
    }

    public void setProizvodCollection(Collection<Proizvod> proizvodCollection) {
        this.proizvodCollection = proizvodCollection;
    }*/

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idProdavnica != null ? idProdavnica.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Prodavnica)) {
            return false;
        }
        Prodavnica other = (Prodavnica) object;
        if ((this.idProdavnica == null && other.idProdavnica != null) || (this.idProdavnica != null && !this.idProdavnica.equals(other.idProdavnica))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ecommerce.Prodavnica[ idProdavnica=" + idProdavnica + " ]";
    }
    
}
