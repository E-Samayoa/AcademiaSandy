package com.academiasi.spring.app.controllers;

import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import com.academiasi.spring.app.models.entities.Alumno;
import com.academiasi.spring.app.models.services.IAlumnoService;
import com.academiasi.spring.app.util.paginator.PageRender;

@Controller
@SessionAttributes("alumno")
public class AlumnoController {
	
	protected final Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private IAlumnoService alumnoService;

	@RequestMapping(value = "/alumnos", method = RequestMethod.GET)
	public String listar(@RequestParam(name="page", defaultValue = "0") int page, Model model,
			Authentication authentication,
			HttpServletRequest request) {
		
		if(authentication != null ) {
			logger.info("Hola usuario: ".concat(authentication.getName()));
		}
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if(auth != null ) {
			logger.info("Forma Estatica, Hola usuario: ".concat(auth.getName()));
		}
		
		if(hasRole("ROLE_ADMIN")) {
			logger.info("Hola ".concat(auth.getName()).concat(" tienes acceso!"));
		} else {
			logger.info("Hola ".concat(auth.getName()).concat(" NO tienes acceso!"));
		}
		
		SecurityContextHolderAwareRequestWrapper securityContext = new SecurityContextHolderAwareRequestWrapper(request, "");
		
		if(securityContext.isUserInRole("ROLE_ADMIN")) {
			logger.info("forma usando SecurityContextHolderAwareRequestWrapper: Hola ".concat(auth.getName()).concat(" tienes acceso!"));
		} else {
			logger.info("forma usando SecurityContextHolderAwareRequestWrapper: Hola ".concat(auth.getName()).concat(" NO tienes acceso!"));
		}
		
		if(request.isUserInRole("ROLE_ADMIN")) {
			logger.info("forma usando HttpServletRequest: Hola ".concat(auth.getName()).concat(" tienes acceso!"));
		} else {
			logger.info("forma usando HttpServletRequest: Hola ".concat(auth.getName()).concat(" NO tienes acceso!"));
		}
		
		
		// 10 el a cantidad de items a mostrar en una pagina
		Pageable pageRequest = PageRequest.of(page, 10);
		
		Page<Alumno> alumnos = alumnoService.findAll(pageRequest);
		
		PageRender<Alumno> pageRender = new PageRender<Alumno>("/alumnos", alumnos);
		model.addAttribute("titulo", "Alumnos");
		model.addAttribute("titulo1", "Listado de Alumnos");
		model.addAttribute("alumnos", alumnos);
		model.addAttribute("page", pageRender);
		return "listaralumnos";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/form-alumnos")
	public String crear(Map<String, Object> model) {

		Alumno alumno = new Alumno();
		model.put("alumno", alumno);
		model.put("titulo", "Formulario de Alumno");
		return "form-alumnos";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/form-alumnos/{id}")
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model) {

		Alumno alumno = null;

		if (id > 0) {
			alumno = alumnoService.findOne(id);

		} else {

			return "redirect:/alumnos";
		}
		model.put("alumno", alumno);
		model.put("titulo", "Editar Alumno");

		return "form-alumnos";
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/form-alumnos", method = RequestMethod.POST)
	public String guardar(@Valid Alumno alumno, BindingResult result, Model model, SessionStatus status) {
		if (result.hasErrors()) {
			model.addAttribute("titulo", "Formulario de alumnos");
			return "form-alumnos";
		}

		alumnoService.save(alumno);
		status.setComplete();
		return "redirect:alumnos";
	}

	@Secured("ROLE_ADMIN")
	@GetMapping(value = "/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id) {

		if (id > 0) {

			alumnoService.delete(id);

		}

		return "redirect:/alumnos";

	}
private boolean hasRole(String role) {
		
		SecurityContext context = SecurityContextHolder.getContext();
		
		if(context == null) {
			return false;
		}
		
		Authentication auth = context.getAuthentication();
		
		if(auth == null) {
			return false;
		}
		
		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		
		return authorities.contains(new SimpleGrantedAuthority(role));
		
		/*
		 * for(GrantedAuthority authority: authorities) {
			if(role.equals(authority.getAuthority())) {
				logger.info("Hola Usuario: ".concat(auth.getName()).concat(" tu rol es: ".concat(authority.getAuthority())));
				return true;
			}
		}
		
		return false;
		*/
		
		
	}

}
