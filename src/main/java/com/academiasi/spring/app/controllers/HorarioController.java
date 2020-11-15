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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.academiasi.spring.app.models.entities.Horario;
import com.academiasi.spring.app.models.services.IHorarioService;
import com.academiasi.spring.app.util.paginator.PageRender;

@Controller
@SessionAttributes("horario")
public class HorarioController {
	
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	private IHorarioService horarioService;
	
	/* ---------------------------------- */
	/* LISTAR HORARIOS                    
	 * @Secured({"ROLE_ADMIN","ROLE_USER"}) agregar mas de un rol a una vista*/
	/* ---------------------------------- */
	@RequestMapping(value="/horarios", method = RequestMethod.GET)
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
		
		Page<Horario> horarios = horarioService.findAll(pageRequest);
		
		PageRender<Horario> pageRender = new PageRender<Horario>("/horarios", horarios);
		model.addAttribute("titulo","Horarios");
		model.addAttribute("titulo1","Listado de Horarios Disponibles");
		model.addAttribute("horarios", horarios);
		model.addAttribute("page", pageRender);
		return "listarhorarios";
	}
	
	/* ---------------------------------- */
	/* CREAR NUEVO HORARIO                */
	/* ---------------------------------- */
	@Secured("ROLE_ADMIN")
	@RequestMapping(value="/form-horarios")
	public String crear(Map<String, Object> model) {
		
		Horario horario = new Horario();
		model.put("horario",horario);
		model.put("titulo", "Formulario de Horario");
		return "form-horarios";
	}
	
	/* ---------------------------------- */
	/* EDITAR HORARIO                     */
	/* ---------------------------------- */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value="/form-horarios/{id}")
	public String editar(@PathVariable(value="id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		
		Horario horario = null;
		
		if(id>0) {
			horario = horarioService.findOne(id);
			if(horario == null) {
				flash.addFlashAttribute("error", "El ID del Horario no existe en la Base de Datos!");
				return "redirect:/horarios";
			}
		} else {
			flash.addFlashAttribute("error", "El ID del Horario no puede ser cero!");
			return "redirect:/horarios";
		}
		model.put("horario", horario);
		model.put("titulo","Editar Horario");
		model.put("titulo1","Formulario para los Horarios");
		return "form-horarios";
	}
	
	/* ---------------------------------- */
	/* GUARDAR HORARIO                    */
	/* ---------------------------------- */
	@Secured("ROLE_ADMIN")
	@RequestMapping(value="/form-horarios", method=RequestMethod.POST)
	public String guardar(@Valid Horario horario, BindingResult result, Model model, RedirectAttributes flash,SessionStatus status) {
		
		if(result.hasErrors()) {
			model.addAttribute("titulo","Formulario de Horario");
			model.addAttribute("titulo1","Formulario para los Horarios");
			return"form-horarios";
		}
		
		String mensajeFlash = (horario.getId() != null)? "Horario Editado con Éxito" : "Horario Creado con Éxito";
		
		horarioService.save(horario);
		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);
		return"redirect:horarios";
	}
	
	/* ---------------------------------- */
	/* ELIMINAR HORARIO                   */
	/* ---------------------------------- */
	@Secured("ROLE_ADMIN")
	@RequestMapping(value="/eliminar/{id}")
	public String eliminar(@PathVariable(value="id") Long id, RedirectAttributes flash) {
		
		if(id>0) {
			horarioService.delete(id);
			flash.addFlashAttribute("success", "Horario Eliminado con Éxito");
		}
		
		return "redirect:/horarios";
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
