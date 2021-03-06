package app.repositories;

import app.entities.User;
import app.entities.mappers.UserRowMapper;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserRepository {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<User> getAllUsers() {
        return jdbcTemplate.query("select * from users", new UserRowMapper());
    }

    public List<User> getNewUsers() {
        return jdbcTemplate.query("select * from users where new_user=true", new UserRowMapper());
    }

    public User getUserByLogin(String login) throws NotFoundException {
        String sql = "select * from users where login=?";
        Object [] args = {login};
        List<User> lu = jdbcTemplate.query(sql,args,new UserRowMapper());
        if (!lu.isEmpty()){ return lu.get(0);}
        //test comment
        else throw new NotFoundException("user with login=" + login+ " not found");
    }

    public void addUser(User user){

        String sql = "insert into users (login, password, fio, role,activity) values(?,?,?,?, false) ";
        Object [] args ={user.getLogin(),user.getPassword(),user.getFIO(),user.getRole()};
        jdbcTemplate.update(sql,args);
    }

    public void changeStatus(User user){
        String sql = "update users set activity=true, new_user=false where login=?";
        Object [] args = {user.getLogin()};
        jdbcTemplate.update(sql,args);
    }

}
