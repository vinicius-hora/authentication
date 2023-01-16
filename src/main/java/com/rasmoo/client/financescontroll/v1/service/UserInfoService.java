package com.rasmoo.client.financescontroll.v1.service;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rasmoo.client.financescontroll.core.impl.ResourceOwner;
import com.rasmoo.client.financescontroll.entity.User;
import com.rasmoo.client.financescontroll.repository.IUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserInfoService implements UserDetailsService, IUserInfoService {

    private final IUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> usuario = userRepository.findByEmail(email);

        if(usuario.isPresent()){
            UserDetails userDetails = new ResourceOwner(usuario.get());
            return userDetails;
        }else{
            throw new UsernameNotFoundException("Usuario não encontrado");
        }
    }
    
    public User findAuth() throws Exception{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

			Optional<User> usuario = this.userRepository.findByEmail(auth.getName());

			if(!usuario.isPresent()){
				throw new Exception();
			}
		
			return usuario.get();


	}
}
