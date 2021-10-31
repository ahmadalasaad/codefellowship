package com.example.codefellowship.Repositories;


import com.example.codefellowship.Model.AppUser;
import org.springframework.data.repository.CrudRepository;

public interface AppUserRepository extends CrudRepository<AppUser,Integer> {
    AppUser findByUsername(String username);
}