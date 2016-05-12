package ray.hubu.edu.entity;

import java.io.Serializable;

/**
 * Created by Ray on 2016/3/3.
 */
public class User implements Serializable{

    private Long id;
    private String name;
    private String nickname;
    private String status;
    private String password;
    private String ip;

    public User()
    {
    }
    public User(Long id,String name,String nickname,String status,String password,String ip)
    {
        this.id=id;
        this.name=name;
        this.nickname=nickname;
        this.status=status;
        this.password=password;
        this.ip=ip;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {

        this.id = id;
    }
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
