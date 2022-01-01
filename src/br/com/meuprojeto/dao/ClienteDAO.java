package br.com.meuprojeto.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.meuprojeto.jdbc.ConnectionFactory;
import br.com.meuprojeto.modelo.Cliente;

public class ClienteDAO {

	// Conexão com o banco de dados
	private Connection connection;

	public  ClienteDAO() throws SQLException {
		this.connection = ConnectionFactory.getConnection();
	}
	
	public void apagar(Cliente cliente) throws SQLException {
		String sql = "delete from clientes where id_cliente=?";
		PreparedStatement stmt = this.connection.prepareStatement(sql);
		stmt.setLong(1, cliente.getIdCliente());
		stmt.execute();
		stmt.close();
	}
	
	public void alterar(Cliente cliente) throws SQLException {
		String sql = "update clientes set nome=?,  cpf =? where id_cliente=?";
		PreparedStatement stmt = this.connection.prepareStatement(sql);
		stmt.setString(1, cliente.getNome());
		stmt.setString(2, cliente.getcpf());
		stmt.setLong(3, cliente.getIdCliente());
		stmt.execute();
		stmt.close();
	}
	
	public void adicionar(Cliente cliente) throws SQLException {				
		String sql = "insert into clientes (nome,cpf) values (?,?)";
		PreparedStatement stmt = this.connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		stmt.setString(1, cliente.getNome());		
		stmt.setString(2, cliente.getcpf());
		stmt.execute();
		ResultSet rs = stmt.getGeneratedKeys();
		if (rs.next()) {
            Long idCliente = rs.getLong(1);
            cliente.setIdCliente(idCliente);
        }
		stmt.close();
	}
	
	public List<Cliente> listarCliente() throws SQLException {
		List<Cliente> clientes = new ArrayList<Cliente>();
		String sql = "select * from clientes";
		Statement stmt = this.connection.createStatement();
		ResultSet resultSet = stmt.executeQuery(sql);
		while (resultSet.next()) {			
			Long idCliente = resultSet.getLong("id_cliente");
			String nome = resultSet.getString("nome");
			String  cpf = resultSet.getString("cpf");
			Cliente cliente = new Cliente();
			cliente.setIdCliente(idCliente);
			cliente.setNome(nome);
			cliente.setCpf(cpf);
			clientes.add(cliente);
		}
		stmt.close();
		return clientes;
	}
}