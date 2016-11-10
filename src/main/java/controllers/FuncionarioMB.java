package controllers;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;

//import java.util.ArrayList;

//import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

//import dao.ContatoDAO;
import dao.FuncionarioDAO;
import dominio.Contato;
import dominio.Funcionario;
import dominio.Usuario;
import ejb.categoriaService;

@ManagedBean
@SessionScoped
public class FuncionarioMB {
	private Funcionario funcionario;
	
	@Inject
	private FuncionarioDAO FuncionarioDao;
	@ManagedProperty(value="#{dados}")
	private DadosSistema ctrluser;
	
	
	public DadosSistema getCtrluser() {
		return ctrluser;
	}

	public void setCtrluser(DadosSistema ctrluser) {
		this.ctrluser = ctrluser;
	}








	private List<Funcionario> listaFunc;
	
	private double salarioParse;
	
	
	public double getSalarioParse() {
		return salarioParse;
	}

	public void setSalarioParse(double salarioParse) {
		this.salarioParse = salarioParse;
	}

	public FuncionarioMB() {
		funcionario = new Funcionario();
		//ctrluser=new ControleMB();
		
	}
	
	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcio) {
		this.funcionario = funcio;
	}
	

	public List<Funcionario> getListaFuncionario() {
		setListaFuncionario(FuncionarioDao.listar());
		return listaFunc;
	}

	
	public void setListaFuncionario(List<Funcionario> listaFunc) {
		this.listaFunc = listaFunc;
	}
	
	
	public String cadastrar() {
		Funcionario c = FuncionarioDao.buscarFuncionarioID(funcionario.getID());
		if(salarioParse<=0){
			FacesMessage msg = new FacesMessage("Digite uma quantia maior que 0 para o salario");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage("", msg);
			return null;
			
		}
		if (c == null) {
			funcionario.setSalario(salarioParse);
			funcionario.setAtivo(true);
			FuncionarioDao.salvar(funcionario);
		} else {
					
			FacesMessage msg = new FacesMessage("Login já existe");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage("", msg);
			return null;
		}
		funcionario = new Funcionario();
		salarioParse=0.0;
		FacesMessage msg = new FacesMessage("Funcionario Cadastrado");
		msg.setSeverity(FacesMessage.SEVERITY_ERROR);
		FacesContext.getCurrentInstance().addMessage("", msg);
		return null;
	}
	
	
	
	
	
	
	
	
	public String login() {
		
		Funcionario temp=FuncionarioDao.cadastrarPrimeiro();
		
		if(temp==null){
			Funcionario primeiroGerente=new Funcionario("a","a","Gerente Original","Natal",10000,"93543344","gerente");
			FuncionarioDao.salvar(primeiroGerente);
		}
		
		
		
		
		Funcionario u = FuncionarioDao.buscarFuncionarioID(funcionario.getID());
		funcionario.setSalario(salarioParse);
		if (u != null) {
			if (u.getSenha().equals(funcionario.getSenha())) {
				if(!u.isAtivo()){
					FacesMessage msg = new FacesMessage("Funcionario bloqueado");
					msg.setSeverity(FacesMessage.SEVERITY_ERROR);
					FacesContext.getCurrentInstance().addMessage("", msg);
					return null;
				}
				ctrluser.mudarUsuario(funcionario.getID());
				Funcionario funkoso=new Funcionario();
				funkoso=ctrluser.recuperarUsuario();
				funcionario=new Funcionario();
				
				if(funkoso.getTipoFuncionario().equals("gerente")){
					ctrluser.setTelaAnterior("MenuGerente");
					return "/interna/MenuGerente.jsf";
				}else{
					ctrluser.setTelaAnterior("MenuVendedor");

					return "/interna/MenuVendedor.jsf";
				}
				
			} else {
				FacesMessage msg = new FacesMessage("Funcionario e/ou senha incorretos");
				msg.setSeverity(FacesMessage.SEVERITY_ERROR);
				FacesContext.getCurrentInstance().addMessage("", msg);
				return null;
			}
		} else {
			FacesMessage msg = new FacesMessage("Funcionario nao encontrado");
			msg.setSeverity(FacesMessage.SEVERITY_ERROR);
			FacesContext.getCurrentInstance().addMessage("", msg);
			return null;
		}
		
	}
}
