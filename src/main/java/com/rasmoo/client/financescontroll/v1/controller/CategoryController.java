package com.rasmoo.client.financescontroll.v1.controller;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.rasmoo.client.financescontroll.entity.Category;
import com.rasmoo.client.financescontroll.entity.User;
import com.rasmoo.client.financescontroll.repository.ICategoryRepository;
import com.rasmoo.client.financescontroll.repository.IUserRepository;
import com.rasmoo.client.financescontroll.v1.dto.CategoryDTO;
import com.rasmoo.client.financescontroll.v1.vo.Response;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/categoria")
@RequiredArgsConstructor
@CrossOrigin
@PreAuthorize(value = "#oauth2.hasScope('cw_logado') and hasRole('ROLE_CUSTOMER')")
public class CategoryController {

	@Autowired
	private ICategoryRepository categoryRepository;
	
	private ModelMapper mapper = new ModelMapper();

	private final IUserRepository userRepository;

	@PostMapping
	public ResponseEntity<Response<Category>> cadastrarCategoria(@RequestBody CategoryDTO categoria) {
		Response<Category> response = new Response<>();
		try {

			User usuario = this.findAuthUser();

			if (categoria != null && categoria.getId() == null) {
				Category categoriaEntity = mapper.map(categoria, Category.class);
				categoriaEntity.setUser(usuario);
				categoryRepository.save(categoriaEntity);
				response.setData(categoriaEntity);
				response.setStatusCode(HttpStatus.CREATED.value());
				return ResponseEntity.status(HttpStatus.CREATED).body(response);
			}
			throw new Exception();
		} catch (Exception e) {
			response.setData(null);
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

	}

	@PutMapping
	public ResponseEntity<Response<Category>> atualizarCategoria(@RequestBody CategoryDTO categoria) {
		Response<Category> response = new Response<>();
		try {
			if (categoria != null && categoria.getId() != null) {
				User usuario = this.findAuthUser();
				Optional<Category> category = this.categoryRepository.findById(categoria.getId());
				if (category.isPresent() && category.get().getId() == usuario.getId()) {
					Category categoriaEntity = mapper.map(categoria, Category.class);
					categoriaEntity.setUser(usuario);
					categoryRepository.save(categoriaEntity);
					response.setData(categoriaEntity);
					response.setStatusCode(HttpStatus.OK.value());
					return ResponseEntity.status(HttpStatus.OK).body(response);
				}
			}
			throw new Exception();
		} catch (Exception e) {
			response.setData(null);
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

	}

	@GetMapping
	@PreAuthorize(value = "#oauth2.hasScope('cw_logado') and hasRole('ROLE_CUSTOMER')")
	public ResponseEntity<Response<List<Category>>> listarCategorias() {
		Response<List<Category>> response = new Response<>();
		try {
			User usuario = this.findAuthUser();
			response.setData(this.categoryRepository.findAllByUserId(usuario.getId()));
			response.setStatusCode(HttpStatus.OK.value());
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (Exception e) {
			response.setData(null);
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

	}

	@GetMapping("/{id}")
	public ResponseEntity<Response<Category>> consultarCategoria(@PathVariable Long id) {
		Response<Category> response = new Response<>();
		try {
			Optional<Category> category = this.categoryRepository.findById(id);
			if (category.isPresent()) {
				response.setData(category.get());
			}
			response.setStatusCode(HttpStatus.OK.value());
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (Exception e) {
			response.setData(null);
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Response<Boolean>> excluirCategoria(@PathVariable Long id) {
		Response<Boolean> response = new Response<>();
		try {
			if (this.categoryRepository.findById(id).isPresent()) {
				this.categoryRepository.deleteById(id);
				response.setData(Boolean.TRUE);
			}
			response.setStatusCode(HttpStatus.OK.value());
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} catch (Exception e) {
			response.setData(null);
			response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}

	}

	private User findAuthUser() throws Exception{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

			Optional<User> usuario = this.userRepository.findByEmail(auth.getName());

			if(!usuario.isPresent()){
				throw new Exception();
			}
		
			return usuario.get();


	}

}
