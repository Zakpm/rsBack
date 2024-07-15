package com.rootsshivasou.moduleCommun.model.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.rootsshivasou.moduleCommun.enums.Genre;
import com.rootsshivasou.moduleCommun.enums.Role;
import com.rootsshivasou.moduleCommun.model.Post;
import com.rootsshivasou.moduleCommun.model.User;

import lombok.Data;

@Data
public class UserDTO {

    private Integer id;
    private String first_name;
    private String last_name;
    private String nickname;
    private String password;
    private String email;
    private LocalDate date_naissance;
    private Genre genre;
    private Set<String> roles;
    private List<Integer> postIds;
    
    public UserDTO() {

    }
    
    public UserDTO(User u) {
        this.id = u.getId();
        this.first_name = u.getFirst_name();
        this.last_name = u.getLast_name();
        this.nickname = u.getNickname();
        this.password = u.getPassword();
        this.email = u.getEmail();
        this.date_naissance = u.getDate_naissance();
        this.genre = u.getGenre();
        this.roles = new HashSet<String>();
        if (u.getRoles() != null) {
            for (Role r : u.getRoles()) {
                this.roles.add(r + "");
            }
        }

        this.postIds = new ArrayList<>();
        if (u.getPosts() != null) {
            for (Post p : u.getPosts()) {
                this.postIds.add(p.getId());
            }
        }

        
    }
}