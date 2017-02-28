package br.com.pitdog.controller.sys;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import org.primefaces.model.menu.MenuModel;

import br.com.pitdog.model.conf.Usuario;
import br.com.pitdog.model.global.Funcao;
import br.com.pitdog.model.rh.Funcionario;
import br.com.pitdog.model.type.Situacao;
import br.com.pitdog.service.conf.UsuarioService;
import br.com.pitdog.service.global.FuncaoService;
import br.com.pitdog.service.rh.FuncionarioService;
import br.com.pitdog.util.DateUtil;
import br.com.pitdog.util.FacesUtil;
import br.com.pitdog.util.RequestContextUtil;

@Named
@SessionScoped
public class LoginController implements Serializable {

	private static final long serialVersionUID = -6030501658030781045L;

	private Funcionario funcionario;
	
	private Funcao funcao;
	
	private Usuario usuario;
	
	private MenuModel menuModel;
	
	private LocalDateTime dataUltimoAcesso = null;

	@Inject
	private UsuarioService usuarioService;
	
	@Inject
	private FuncionarioService funcionarioService;
	
	@Inject
	private FuncaoService funcaoService;

	// Faces redirect
	private static final String FACES_REDIRECT = "?faces-redirect=true";

	// Login
	private static final String PAGE_LOGIN = "/page_seguranca/p_login.xhtml";
	private static final String PAGE_CADASTRO_INICIAL = "/page_seguranca/p_cadastro_inicial.xhtml";

	// Sistema
	private static final String PAGE_DASHBOARD = "/pages/sys/p_dashboard.xhtml" + FACES_REDIRECT;

	@PostConstruct
	public void init() {
		this.usuario = new Usuario();
		this.funcao = new Funcao();
		this.funcionario = new Funcionario();
	}

	public String autenticarLogin() {
		try {
			List<Usuario> usuarios = usuarioService.findBySituation(Situacao.ATIVO);
			if (usuarios.isEmpty()) {
				FacesUtil.mensagemWarn("Nenhum usuário 'ativo' encontrado!");
			} else {
				if (usuario.getNomeUsuario().trim().isEmpty() || usuario.getSenhaUsuario().trim().isEmpty()) {
					FacesUtil.mensagemWarn("Usuário e senha obrigatórios!");
					return PAGE_LOGIN;
				}
				return procurarUsuarioESenha(usuarios);
			}
		} catch (RuntimeException e) {
			FacesUtil.mensagemErro(e.getMessage());
		}
		return PAGE_LOGIN;
	}
	
	public void verificarSeExisteUsuarioCadastrado(){
		List<Usuario> usuarios = usuarioService.findAll();
		if (usuarios.isEmpty()) {
			RequestContextUtil.execute("PF('dialog_info').show();");
		}
	}
	
	public String redirecionarParaTelaCadastroInicialUsuario(){
		return PAGE_CADASTRO_INICIAL + FACES_REDIRECT;
	}
	
	public String procurarUsuarioESenha(List<Usuario> usuarios) {
		for (Usuario u : usuarios) {
			if (u.getNomeUsuario().equalsIgnoreCase(usuario.getNomeUsuario())
					&& u.getSenhaUsuario().equalsIgnoreCase(usuario.getSenhaUsuario())) {
				setarDataUltimoAcessoInicialUsuario(u);
				usuario = u;
				iniciarSessaoUsuario(usuario);
				return PAGE_DASHBOARD + FACES_REDIRECT;
			}
		}
		FacesUtil.mensagemWarn("Nenhum usuário " + usuario.getNomeUsuario() + " encontrado,verifique e tente novamente! ");
		return PAGE_LOGIN;
	}
	
	private void setarDataUltimoAcessoInicialUsuario(Usuario u){
		if (u.getUltimoAcesso() == null) {
			ZoneId fusoHorarioSaoPaulo = ZoneId.of("America/Sao_Paulo");
			dataUltimoAcesso = LocalDateTime.now(fusoHorarioSaoPaulo);
			u.setUltimoAcesso(DateUtil.asDate(dataUltimoAcesso));
			usuarioService.salvar(u);
		}
	}
	
	public String salvarDadosIniciais(){
		try {
			
			funcaoService.verificarSeExisteFuncaoCadastradoComMesmaDescricao(funcao);
			funcionarioService.verificarSeExisteFuncionarioCadastradoComMesmaDescricao(funcionario);
			
			funcao = funcaoService.salvar(funcao);
			
			funcionario.setFuncao(funcao);
			funcionario = funcionarioService.salvar(funcionario);
			
			usuario.setFuncionario(funcionario);
			usuario = usuarioService.salvar(usuario);
			
			return logarSistema(usuario);
		} catch (RuntimeException e) {
			FacesUtil.mensagemErro(e.getMessage());
		}
		return null;
	}
	
	public String logarSistema(Usuario usuario){
		setarDataUltimoAcessoInicialUsuario(usuario);
		iniciarSessaoUsuario(usuario);
		return PAGE_DASHBOARD + FACES_REDIRECT;
	}

	public void iniciarSessaoUsuario(Usuario usuario) {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		usuario.setDataInicial(Calendar.getInstance().getTime());
		usuarioService.salvar(usuario);
		session.setAttribute("usuario", usuario);
	}

	private void finalizarSessaoUsuario() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
	}

	public void salvarDataAtual(Usuario u) {
		u.setUltimoAcesso(Calendar.getInstance().getTime());
		usuarioService.salvar(u);
	}

	public String logoutSistema() {
		usuario = usuarioService.findById(usuario.getId());
		usuario.setDataFinal(Calendar.getInstance().getTime());
		usuario.setUltimoAcesso(usuario.getDataInicial());
		usuarioService.salvar(usuario);
		usuario = null;
		finalizarSessaoUsuario();
		return PAGE_LOGIN + FACES_REDIRECT;
	}

	public Usuario getUsuario() {
		if (usuario == null) {
			usuario = new Usuario();
		}
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public MenuModel getMenuModel() {
		return menuModel;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public Funcao getFuncao() {
		return funcao;
	}

	public void setFuncao(Funcao funcao) {
		this.funcao = funcao;
	}

}
