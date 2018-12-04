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
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Korsn
 */
@Entity
@Table(name = "user")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u")
    , @NamedQuery(name = "User.findByIdUser", query = "SELECT u FROM User u WHERE u.idUser = :idUser")})
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_USER", nullable = false)
    private Integer idUser;
    @Lob
    //@Size(max = 16777215)
    @Column(name = "IME", length = 16777215)
    private String ime;
    @Lob
    // @Size(max = 16777215)
    @Column(name = "PREZIME", length = 16777215)
    private String prezime;
    @Lob
    //@Size(max = 16777215)
    @Column(name = "JMBG", length = 16777215)
    private String jmbg;
    @Lob
    @Column(name = "ADDED_BY", length = 16777215)
    private String addedBy;
    @Lob
    //@Size(max = 16777215)
    @Column(name = "USERNAME", length = 16777215)
    private String username;
    @Lob
    //@Size(max = 16777215)
    @Column(name = "PASSWORD", length = 16777215)
    private String password;
    @Lob
    //@Size(max = 16777215)
    @Column(name = "EMAIL", length = 16777215)
    private String email;
  //  @OneToMany(mappedBy = "idUser")
  //  private Collection<Proizvod> proizvodCollection;
   
    /*@JoinColumn(name = "ID_ROLE", referencedColumnName = "ID_ROLE")
    @ManyToOne
    private Userrole idRole;*/
    
    @Column(name = "ID_ROLE")
    private int idRole;
  

    public User() {
    }

    public User(Integer idUser) {
        this.idUser = idUser;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getJmbg() {
        return jmbg;
    }

    public void setJmbg(String jmbg) {
        this.jmbg = jmbg;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }
    
    
/*
    @XmlTransient
    public Collection<Proizvod> getProizvodCollection() {
        return proizvodCollection;
    }

    public void setProizvodCollection(Collection<Proizvod> proizvodCollection) {
        this.proizvodCollection = proizvodCollection;
    }*/

  /*  public Userrole getIdRole() {
        return idRole;
    }

    public void setIdRole(Userrole idRole) {
        this.idRole = idRole;
    }*/

    public int getIdRole() {
        return idRole;
    }

    public void setIdRole(int idRole) {
        this.idRole = idRole;
    }
    
    

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

  /*  public Collection<User> getProdavnicaCollection() {
        return prodavnicaCollection;
    }

    public void setProdavnicaCollection(Collection<User> prodavnicaCollection) {
        this.prodavnicaCollection = prodavnicaCollection;
    }*/

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUser != null ? idUser.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.idUser == null && other.idUser != null) || (this.idUser != null && !this.idUser.equals(other.idUser))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ecommerce.User[ idUser=" + idUser + " ]";
    }

}
