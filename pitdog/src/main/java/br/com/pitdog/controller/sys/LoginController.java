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

import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

import br.com.pitdog.model.conf.PerfilAcesso;
import br.com.pitdog.model.conf.Usuario;
import br.com.pitdog.model.sys.PanelMenu;
import br.com.pitdog.model.type.MenuSistema;
import br.com.pitdog.model.type.Situacao;
import br.com.pitdog.service.conf.UsuarioService;
import br.com.pitdog.service.sys.PanelMenuService;
import br.com.pitdog.util.DateUtil;
import br.com.pitdog.util.FacesUtil;

@Named
@SessionScoped
public class LoginController implements Serializable {

	private static final long serialVersionUID = -6030501658030781045L;

	private Usuario usuario;
	private MenuModel menuModel;
	private LocalDateTime dataUltimoAcesso = null;

	private DefaultSubMenu menuCompras;
	private DefaultSubMenu menuEstoque;
	private DefaultSubMenu menuGestaoServico;
	private DefaultSubMenu menuRh;
	private DefaultSubMenu menuFinanceiro;
	private DefaultSubMenu menuGlobal;
	private DefaultSubMenu menuConfiguracao;
	
	private DefaultSubMenu subMenuCaixa;
	private DefaultSubMenu subMenuCadastroGl;
	private DefaultSubMenu subMenuCadastroSG;
	private DefaultSubMenu subMenuCadastroRH;
	private DefaultSubMenu subMenuCadastroEstoque;

	private DefaultMenuItem menuItemLancamentoCaixa;
	private DefaultMenuItem menuItemUsuario;
	private DefaultMenuItem menuItemPerfilAcesso;
	private DefaultMenuItem menuItemFuncionario;
	private DefaultMenuItem menuItemServico;
	private DefaultMenuItem menuItemOrdemServico;
	private DefaultMenuItem menuItemCliente;
	private DefaultMenuItem menuItemFornecedor;
	private DefaultMenuItem menuItemBackup;
	private DefaultMenuItem menuItemFormaPagamento;
	private DefaultMenuItem menuItemProduto;

	@Inject
	private PanelMenuService panelMenuService;

	@Inject
	private UsuarioService usuarioService;

	// Faces redirect
	private static final String FACES_REDIRECT = "?faces-redirect=true";

	// Login
	private static final String PAGE_LOGIN = "/page_seguranca/p_login.xhtml";

	// Sistema
	private static final String PAGE_DASHBOARD = "/pages/sys/p_dashboard.xhtml" + FACES_REDIRECT;

	// Financeiro
	private static final String PAGE_LANCAMENTO_CAIXA = "/pages/financ/p_lancamento_caixa.xhtml" + FACES_REDIRECT;
	private static final String PAGE_CONDICAO_PAGAMENTO = "/pages/financ/p_condicao_pagamento.xhtml" + FACES_REDIRECT;

	// Configuração
	private static final String PAGE_USUARIO = "/pages/conf/p_usuario.xhtml" + FACES_REDIRECT;
	private static final String PAGE_PERFIL_ACESSO = "/pages/conf/p_perfil_acesso.xhtml" + FACES_REDIRECT;
	private static final String PAGE_BACKUP = "/pages/conf/p_backup.xhtml" + FACES_REDIRECT;
	
	private static final String PAGE_PRODUTO = "/pages/estoque/p_produto.xhtml" + FACES_REDIRECT;

	// RH
	private static final String PAGE_FUNCIONARIO = "/pages/rh/p_funcionario.xhtml" + FACES_REDIRECT;

	// Serviços
	private static final String PAGE_SERVICO = "/pages/gestserv/p_servicos.xhtml" + FACES_REDIRECT;
	private static final String PAGE_ORDEM_DE_SERVICO = "/pages/gestserv/p_ordem_servico.xhtml" + FACES_REDIRECT;

	// Global
	private static final String PAGE_CLIENTE = "/pages/global/p_cliente.xhtml" + FACES_REDIRECT;
	private static final String PAGE_FORNECEDOR = "/pages/global/p_fornecedor.xhtml" + FACES_REDIRECT;

	// Icone
	private static final String ICON_MENU = "ui-icon-newwin";

	@PostConstruct
	public void init() {
		this.usuario = new Usuario();
	}

	private void setarNullMenus() {
		menuCompras = null;
		menuEstoque = null;
		menuGestaoServico = null;
		menuRh = null;
		menuFinanceiro = null;
		menuGlobal = null;
		menuConfiguracao = null;
		
		subMenuCadastroSG = null;
		subMenuCadastroRH = null;
		subMenuCaixa = null;
		subMenuCadastroGl = null;
		subMenuCadastroEstoque = null;

		menuItemLancamentoCaixa = null;
		menuItemUsuario = null;
		menuItemPerfilAcesso = null;
		menuItemBackup = null;
		menuItemFuncionario = null;
		menuItemServico = null;
		menuItemCliente = null;
		menuItemFornecedor = null;
		menuItemOrdemServico = null;
		menuItemFormaPagamento = null;
		menuItemProduto = null;
	}

	public String autenticarLogin() {
		try {
			List<Usuario> usuarios = usuarioService.findBySituation(Situacao.ATIVO);
			if (usuarios.isEmpty()) {
				FacesUtil.mensagemWarn("Nenhum usuário 'ativo' encontrado!");
				return PAGE_LOGIN;
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

	private String procurarUsuarioESenha(List<Usuario> usuarios) {
		for (Usuario u : usuarios) {
			if (u.getNomeUsuario().equalsIgnoreCase(usuario.getNomeUsuario())
					&& u.getSenhaUsuario().equalsIgnoreCase(usuario.getSenhaUsuario())) {
				if (u.getUltimoAcesso() == null) {
					ZoneId fusoHorarioSaoPaulo = ZoneId.of("America/Sao_Paulo");
					dataUltimoAcesso = LocalDateTime.now(fusoHorarioSaoPaulo);
					u.setUltimoAcesso(DateUtil.asDate(dataUltimoAcesso));
					usuarioService.salvar(u);
				}
				usuario = u;
				setarNullMenus();
				//createPanelMenu(usuario.getPerfilAcesso()); Esse método não será utilizado!
				iniciarSessaoUsuario(usuario);
				return PAGE_DASHBOARD + FACES_REDIRECT;
			}
		}
		FacesUtil.mensagemWarn("Nenhum usuário " + usuario.getNomeUsuario() + " encontrado,verifique e tente novamente! ");
		return PAGE_LOGIN;
	}

	private void iniciarSessaoUsuario(Usuario usuario) {
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

	@SuppressWarnings("unused")
	private void createPanelMenu(PerfilAcesso perfilAcesso) {
		menuModel = new DefaultMenuModel();
		consistirMenu(perfilAcesso);
		adicionarElementos();
	}

	private void consistirMenu(PerfilAcesso perfilAcesso) {
		for (PanelMenu menu : panelMenuService.getListMenuTarget(perfilAcesso)) {
			
			if (menu.getMenu().equals(MenuSistema.COMPRAS.getMenu())) {
				menuCompras = new DefaultSubMenu(MenuSistema.COMPRAS.getMenu());
			}
			/*if (menu.getMenu().equals(MenuSistema.ESTOQUE.getMenu())) {
				menuEstoque = new DefaultSubMenu(MenuSistema.ESTOQUE.getMenu());
			}*/
			/*
			 * if(menu.getMenu().equals(MenuSistema.GESTAO_SERVICO.getMenu())){
			 * menuGestaoServico = new
			 * DefaultSubMenu(MenuSistema.GESTAO_SERVICO.getMenu()); }
			 */
			/*
			 * if(menu.getMenu().equals(MenuSistema.CADASTROS_GS.getMenu())){
			 * subMenuCadastroSG = new
			 * DefaultSubMenu(MenuSistema.CADASTROS_GS.getMenu()); }
			 */
			if (menu.getMenu().equals(MenuSistema.SERVICO.getMenu())) {
				menuItemServico = new DefaultMenuItem(MenuSistema.SERVICO.getMenu());
				menuItemServico.setOutcome(PAGE_SERVICO);
				menuItemServico.setIcon(ICON_MENU);
			}
			if (menu.getMenu().equals(MenuSistema.PRODUTO.getMenu())) {
				menuItemProduto = new DefaultMenuItem(MenuSistema.PRODUTO.getMenu());
				menuItemProduto.setOutcome(PAGE_PRODUTO);
				menuItemProduto.setIcon(ICON_MENU);
			}
			if (menu.getMenu().equals(MenuSistema.ORDEM_DE_SERVICO.getMenu())) {
				menuItemOrdemServico = new DefaultMenuItem(MenuSistema.ORDEM_DE_SERVICO.getMenu());
				menuItemOrdemServico.setOutcome(PAGE_ORDEM_DE_SERVICO);
				menuItemOrdemServico.setIcon(ICON_MENU);
			}
			/*
			 * if(menu.getMenu().equals(MenuSistema.RH.getMenu())){ menuRh = new
			 * DefaultSubMenu(MenuSistema.RH.getMenu()); }
			 */
			/*if (menu.getMenu().equals(MenuSistema.FINANCEIRO.getMenu())) {
				menuFinanceiro = new DefaultSubMenu(MenuSistema.FINANCEIRO.getMenu());
			}*/
			/*if (menu.getMenu().equals(MenuSistema.CAIXA.getMenu())) {
				subMenuCaixa = new DefaultSubMenu(MenuSistema.CAIXA.getMenu());
			}*/
			/*if (menu.getMenu().equals(MenuSistema.GLOBAL.getMenu())) {
				menuGlobal = new DefaultSubMenu(MenuSistema.GLOBAL.getMenu());
			}*/
			/*if (menu.getMenu().equals(MenuSistema.CADASTROS_GL.getMenu())) {
				subMenuCadastroGl = new DefaultSubMenu(MenuSistema.CADASTROS_GL.getMenu());
			}*/
			if (menu.getMenu().equals(MenuSistema.CLIENTE.getMenu())) {
				menuItemCliente = new DefaultMenuItem(MenuSistema.CLIENTE.getMenu());
				menuItemCliente.setOutcome(PAGE_CLIENTE);
				menuItemCliente.setIcon(ICON_MENU);
			}
			if (menu.getMenu().equals(MenuSistema.FORNECEDOR.getMenu())) {
				menuItemFornecedor = new DefaultMenuItem(MenuSistema.FORNECEDOR.getMenu());
				menuItemFornecedor.setOutcome(PAGE_FORNECEDOR);
				menuItemFornecedor.setIcon(ICON_MENU);
			}
			/*if (menu.getMenu().equals(MenuSistema.CONFIGURACAO.getMenu())) {
				menuConfiguracao = new DefaultSubMenu(MenuSistema.CONFIGURACAO.getMenu());
			}*/
			if (menu.getMenu().equals(MenuSistema.LANCAMENTO_CAIXA.getMenu())) {
				menuItemLancamentoCaixa = new DefaultMenuItem(MenuSistema.LANCAMENTO_CAIXA.getMenu());
				menuItemLancamentoCaixa.setOutcome(PAGE_LANCAMENTO_CAIXA);
				menuItemLancamentoCaixa.setIcon(ICON_MENU);
			}
			if(menu.getMenu().equals(MenuSistema.CONDICAO_PAGAMENTO.getMenu())){
				menuItemFormaPagamento = new DefaultMenuItem(MenuSistema.CONDICAO_PAGAMENTO.getMenu());
				menuItemFormaPagamento.setOutcome(PAGE_CONDICAO_PAGAMENTO);
				menuItemFormaPagamento.setIcon(ICON_MENU);
			}
			if (menu.getMenu().equals(MenuSistema.USUARIO.getMenu())) {
				menuItemUsuario = new DefaultMenuItem(MenuSistema.USUARIO.getMenu());
				menuItemUsuario.setOutcome(PAGE_USUARIO);
				menuItemUsuario.setIcon(ICON_MENU);
			}
			if (menu.getMenu().equals(MenuSistema.BACKUP.getMenu())) {
				menuItemBackup = new DefaultMenuItem(MenuSistema.BACKUP.getMenu());
				menuItemBackup.setOutcome(PAGE_BACKUP);
				menuItemBackup.setIcon(ICON_MENU);
			}
			if (menu.getMenu().equals(MenuSistema.PERFIL_ACESSO.getMenu())) {
				menuItemPerfilAcesso = new DefaultMenuItem(MenuSistema.PERFIL_ACESSO.getMenu());
				menuItemPerfilAcesso.setOutcome(PAGE_PERFIL_ACESSO);
				menuItemPerfilAcesso.setIcon(ICON_MENU);
			}
			if (menu.getMenu().equals(MenuSistema.FUNCIONARIO.getMenu())) {
				menuItemFuncionario = new DefaultMenuItem(MenuSistema.FUNCIONARIO.getMenu());
				menuItemFuncionario.setOutcome(PAGE_FUNCIONARIO);
				menuItemFuncionario.setIcon(ICON_MENU);
			}
		}
	}

	private void adicionarElementos() {
		if (menuCompras != null) {
			menuModel.addElement(menuCompras);
		}
		if (menuItemProduto != null) {
			menuEstoque = new DefaultSubMenu(MenuSistema.ESTOQUE.getMenu());
			menuModel.addElement(menuEstoque);
			subMenuCadastroEstoque =  new DefaultSubMenu(MenuSistema.CADASTRO_ESTQ.getMenu());
			menuEstoque.addElement(subMenuCadastroEstoque);
			subMenuCadastroEstoque.addElement(menuItemProduto);
		}

		if (menuItemServico != null || menuItemOrdemServico != null) {
			menuGestaoServico = new DefaultSubMenu(MenuSistema.GESTAO_SERVICO.getMenu());
			menuModel.addElement(menuGestaoServico);
			if (menuItemServico != null) {
				subMenuCadastroSG = new DefaultSubMenu(MenuSistema.CADASTROS_GS.getMenu());
				menuGestaoServico.addElement(subMenuCadastroSG);
				subMenuCadastroSG.addElement(menuItemServico);
			}
			if (menuItemOrdemServico != null) {
				menuGestaoServico.addElement(menuItemOrdemServico);
			}
		}

		if (menuItemFuncionario != null) {
			menuRh = new DefaultSubMenu(MenuSistema.RH.getMenu());
			menuModel.addElement(menuRh);
			subMenuCadastroRH = new DefaultSubMenu(MenuSistema.CADASTRO_RH.getMenu());
			menuRh.addElement(subMenuCadastroRH);
			subMenuCadastroRH.addElement(menuItemFuncionario);
		}

		if (menuItemLancamentoCaixa != null || menuItemFormaPagamento != null) {
			menuFinanceiro = new DefaultSubMenu(MenuSistema.FINANCEIRO.getMenu());
			menuModel.addElement(menuFinanceiro);
			if(menuItemLancamentoCaixa != null){
				subMenuCaixa = new DefaultSubMenu(MenuSistema.CAIXA.getMenu());
				menuFinanceiro.addElement(subMenuCaixa);
				subMenuCaixa.addElement(menuItemLancamentoCaixa);
			}
			if(menuItemFormaPagamento != null){
				menuFinanceiro.addElement(menuItemFormaPagamento);
			}
		}
			
		
		if (menuItemCliente != null || menuItemFornecedor != null) {
			menuGlobal = new DefaultSubMenu(MenuSistema.GLOBAL.getMenu());
			menuModel.addElement(menuGlobal);
			subMenuCadastroGl = new DefaultSubMenu(MenuSistema.CADASTROS_GL.getMenu());
			menuGlobal.addElement(subMenuCadastroGl);
			if(menuItemCliente != null){
				subMenuCadastroGl.addElement(menuItemCliente);
			}
			if(menuItemFornecedor != null){
				subMenuCadastroGl.addElement(menuItemFornecedor);
			}
		}
		
		if (menuItemUsuario != null || menuItemPerfilAcesso != null || menuItemBackup != null) {
			menuConfiguracao = new DefaultSubMenu(MenuSistema.CONFIGURACAO.getMenu());
			menuModel.addElement(menuConfiguracao);
			if (menuItemUsuario != null) {
				menuConfiguracao.addElement(menuItemUsuario);
			}
			if (menuItemPerfilAcesso != null) {
				menuConfiguracao.addElement(menuItemPerfilAcesso);
			}
			if (menuItemBackup != null) {
				menuConfiguracao.addElement(menuItemBackup);
			}
		}
		
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

}
