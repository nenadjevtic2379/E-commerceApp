/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ecommerce.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author Korsn
 */
//@Stateless
@Path("auth")
public class AuthenticationServiceREST {

    final static String url = "jdbc:mysql://localhost:3306/ecommerce";
    final static String user = "root";
    final static String pass = "Nenad1995";

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public String login(@FormParam("username") String username, @FormParam("password") String password) {
        String result;
        JSONObject json = new JSONObject();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pass);

            json.put("status", "false");

            PreparedStatement ps = con.prepareStatement("select * from user where username=? and password=?");
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                /*    int id = rs.getInt("id_role");
                    if (id == 1) {
                    result = "admin";
                }
                else {
                    result = "user";
                }*/
                json.put("status", "true");
                json.put("idRole", rs.getInt("id_role"));
                json.put("idUser", rs.getString("id_user"));
                json.put("ime", rs.getString("ime"));
                json.put("prezime", rs.getString("prezime"));
                json.put("username", rs.getString("username"));
                json.put("email", rs.getString("email"));
                //json.put("addedBy", rs.getString("addedBy"));
            }

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        result = json.toString();
        return result;
    }

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public String register(@FormParam("username") String username, @FormParam("password") String password,
            @FormParam("id_role") int idRole, @FormParam("ime") String ime, @FormParam("prezime") String prezime,
            @FormParam("jmbg") String jmbg, @FormParam("email") String email, @FormParam("addedBy") String addedBy) {
        String result = "false";
        int x = 0;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pass);

            PreparedStatement st = con.prepareStatement("select username from user where username=?");
            st.setString(1, username);

            ResultSet rs = st.executeQuery();

            if (!rs.next()) {
                PreparedStatement ps = con.prepareStatement("insert into user(username, password, id_role, ime, prezime, jmbg, email, addedBy) values(?,?,?,?,?,?,?,?)");
                ps.setString(1, username);
                ps.setString(2, password);
                ps.setInt(3, 1);
                ps.setString(4, ime);
                ps.setString(5, prezime);
                ps.setString(6, jmbg);
                ps.setString(7, email);
                ps.setString(8, "null");

                x = ps.executeUpdate();
            }

            if (x == 1) {

                result = "true";
            }

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
    
    @POST
    @Path("/registerRadnik")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public String registerRadnik(@FormParam("username") String username, @FormParam("password") String password,
            @FormParam("id_role") int idRole, @FormParam("ime") String ime, @FormParam("prezime") String prezime,
            @FormParam("jmbg") String jmbg, @FormParam("email") String email, @FormParam("added_by") String addedBy) {
        String result = "false";
        int x = 0;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pass);

            PreparedStatement st = con.prepareStatement("select username from user where username=?");
            st.setString(1, username);

            ResultSet rs = st.executeQuery();

            if (!rs.next()) {
                PreparedStatement ps = con.prepareStatement("insert into user(username, password, id_role, ime, prezime, jmbg, email, added_by) values(?,?,?,?,?,?,?,?)");
                ps.setString(1, username);
                ps.setString(2, password);
                ps.setInt(3, 2);
                ps.setString(4, ime);
                ps.setString(5, prezime);
                ps.setString(6, jmbg);
                ps.setString(7, email);
                ps.setString(8, addedBy);

                x = ps.executeUpdate();
            }

            if (x == 1) {

                result = "true";
            }

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
