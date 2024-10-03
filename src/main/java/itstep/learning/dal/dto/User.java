package itstep.learning.dal.dto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

public class User {
    private UUID id;
    private String name;
    private String email;
    private String avatar;
    private Date birthday;
    private Date signupDt;
    private Date deleteDt;
    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public User() {
    }

    public User( ResultSet res ) throws SQLException {
        String id;
        try { id = res.getString("user_id"); }
        catch( Exception ignore ) { id = res.getString("id"); }
        setId( UUID.fromString(id) );
        setName( res.getString("name") );
        setEmail( res.getString("email") );
        setAvatar( res.getString("avatar") );
        setBirthdate( res.getDate("birthdate") );
        setSignupDt( new Date( res.getTimestamp("signup_dt").getTime() ) );
        Timestamp timestamp = res.getTimestamp( "delete_dt" );
        if( timestamp != null ) {
            setDeleteDt( new Date( timestamp.getTime() ) );
        }
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthdate(Date birthday) {
        this.birthday = birthday;
    }

    public Date getSignupDt() {
        return signupDt;
    }

    public void setSignupDt(Date signupDt) {
        this.signupDt = signupDt;
    }

    public Date getDeleteDt() {
        return deleteDt;
    }

    public void setDeleteDt(Date deleteDt) {
        this.deleteDt = deleteDt;
    }
}
