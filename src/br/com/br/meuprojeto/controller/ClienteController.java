package br.com.br.meuprojeto.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import br.com.meuprojeto.dao.ClienteDAO;
import br.com.meuprojeto.modelo.Cliente;

@WebServlet("/ClienteController")
public class ClienteController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public ClienteController() {
		super();
	}
	
	private void processarRequisicao(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String action = request.getParameter("acao");

		if (action == null) {
			throw new ServletException("Sem ação especificada.");
		} else if (action.equals("manipular_cliente")) {
			String idCliente = request.getParameter("idCliente");
			if ((idCliente == null ) || (idCliente.equals(""))){
				adicionarCliente(request, response);
			}else {
				alterarCliente(request, response);
			}			
		} else if (action.equals("gerenciar")) {
			listarClientes(request, response);
		} else if (action.equals("remover_cliente")) {
			apagarCliente(request, response);
		}
	}

	private void alterarCliente(HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		long idCliente = Long.parseLong(request.getParameter("idCliente"));
		String nome = request.getParameter("nome");
		String cpf = request.getParameter("cpf");
		
		Cliente cliente = new Cliente();
		cliente.setIdCliente(idCliente);
		cliente.setNome(nome);
		cliente.setCpf(cpf);;
				
		ClienteDAO clienteDAO;
		try {
			clienteDAO = new ClienteDAO();
			clienteDAO.alterar(cliente);
			String json = new Gson().toJson(cliente);
		    response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
		    response.setStatus(200);		    
		    response.getWriter().write(json);
			response.getWriter().flush();
		} catch (SQLException e) {
			e.printStackTrace();
			response.setStatus(500);
			response.setCharacterEncoding("UTF-8");
			response.getWriter().print("Problemas ao alterar o cliente.");
			response.getWriter().flush();
		} 
		
	}

	private void listarClientes(HttpServletRequest request, HttpServletResponse response) {
		
		String destino = "gerenciar.jsp";
		RequestDispatcher requestDispatcher = request.getRequestDispatcher(destino);
		ClienteDAO clienteDAO;
		try {
			clienteDAO = new ClienteDAO();
			List<Cliente> cliente = clienteDAO.listarCliente();
			request.setAttribute("cliente", cliente);
			requestDispatcher.forward(request, response);
		} catch (SQLException | ServletException | IOException e) {			
			e.printStackTrace();
		}
	}
	
	private void apagarCliente(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		long idCliente = Long.parseLong(request.getParameter("idCliente"));
		
		Cliente cliente = new Cliente();
		cliente.setIdCliente(idCliente);
				
		ClienteDAO clienteDAO;
		try {
			clienteDAO = new ClienteDAO();
			clienteDAO.apagar(cliente);
			response.setStatus(200);
			response.setCharacterEncoding("UTF-8");
			response.getWriter().flush();
		} catch (SQLException e) {
			e.printStackTrace();
			response.setStatus(500);
			response.setCharacterEncoding("UTF-8");
			response.getWriter().print("Problemas ao remover o cliente.");
			response.getWriter().flush();
		} 
		
	    
	}

	private void adicionarCliente(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String nome = request.getParameter("nome");
		String cpf = request.getParameter("cpf");
		
		Cliente cliente = new Cliente();
		cliente.setNome(nome);
		cliente.setCpf(cpf);
				
		ClienteDAO clienteDAO;
		try {
			clienteDAO = new ClienteDAO();
			clienteDAO.adicionar(cliente);
			String json = new Gson().toJson(cliente);
		    response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
		    response.setStatus(200);		    
		    response.getWriter().write(json);
			response.getWriter().flush();
		} catch (SQLException e) {
			e.printStackTrace();
			response.setStatus(500);
			response.setCharacterEncoding("UTF-8");
			response.getWriter().print("Problemas ao salvar o cliente.");
			response.getWriter().flush();
		} 
		
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processarRequisicao(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processarRequisicao(request, response);
	}
	
}