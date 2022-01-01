<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Página Inicial</title>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js">	
</script>

<script>
$(document).ready(function() {
	$('#submit').click(function(event){
		var nomeCliente = $('#nomeCliente').val();
		var cpfCliente = $('#cpfCliente').val();
		var id = $('#idCliente').val();
        $.ajax({
            type:'POST',
            data: {
            	idCliente : id,
            	nome : nomeCliente,
    			cpf : cpfCliente,
    			acao : 'manipular_cliente'
            },
            dataType: 'json',
            url:'ClienteController',
            success: function(cliente){
            	var $table = $('#tableResposta')
            	
            	var $ancoraApagar =  $("<a/>").addClass('del').attr("href","#").text('Apagar');
            	var $ancoraAlterar =  $("<a/>").addClass('alt').attr("href","#").text('Alterar');
            	var novaLinha = $("<tr>").append($("<td>").text(cliente.idCliente).addClass('idCell')) 
    			.append($("<td>").text(cliente.nome).addClass('nomeClienteCell'))     
    			.append($("<td>").text(cliente.cpf).addClass('cpfCell'))
    			.append($("<td>").append($ancoraApagar))
    			.append($("<td>").append($ancoraAlterar));
				
            	if (id == ""){
            		novaLinha.appendTo($table);
            	} else {
            		$('.editMode').replaceWith(novaLinha);
            	}
            	$("#idCliente").val("");
                $("#nomeCliente").val("");
                $("#cpfCliente").val("");    			
            },
            error: function(jqXHR, textStatus, message) {
            	alert(jqXHR.responseText);
            }
        });
        
    });
	
	$('#tableResposta').on('click','.del',function(event){
      	var $linhaCorrente = $(this).closest("tr");
        var id = $linhaCorrente.find(".idCell").html();        
        var confirmacao = confirm("Deseja excluir o item: " + id + " ?");
        if (confirmacao == true) {
        	$.ajax({
        	    type:'GET',
        	    data: {
        	    	idCliente : id,
    				acao : 'remover_cliente'
        	    },
        	    dataType: 'text',
        	    url:'ClienteController',
        	    success: function(response){
					$linhaCorrente.remove();
        	    },
        	    error: function(jqXHR, textStatus, message) {
        	    	alert(jqXHR.responseText);
        	    }
        	});
        }
    });
	
	$('#tableResposta').on('click','.alt',function(event){
		
		$('#tableResposta').find('.editMode').each(function(){
			  $(this).removeClass("editMode");
		});
		
      	var $linhaCorrente = $(this).closest("tr");
        var id = $linhaCorrente.find(".idCell").html();  
        var nome = $linhaCorrente.find(".nomeClienteCell").html();
        var cpf = $linhaCorrente.find(".cpfCell").html();
        $("#idCliente").val(id);
        $("#nomeCliente").val(nome);
        $("#cpfCliente").val(cpf);
        
        $linhaCorrente.addClass('editMode');
    });
	
});


</script>
</head>

<body>
	<form id="formcurso">
		<h1>Cadastrar Cliente</h1>
		<input type="hidden" id="idCliente" /> 
		Nome: <input type="text" id="nomeCliente" /> <br /> 
		CPF: <input type="text" id="cpfCliente" /> <br /> 
		<input type="button" id="submit" value="Cadastrar" /> <br />
		<div id="textResposta"></div>
	</form>
	<div id='divtable'>
	<table border="1px" id="tableResposta">
		<tr>
			<th>Id</th>
			<th>Nome</th>
			<th>CPF</th>
		</tr>
		<c:forEach var="cliente" items="${clientes}">
			<tr>
				<td class='idCell'><c:out value="${cliente.idCliente}"></c:out></td>
				<td class='nomeClienteCell'><c:out value="${cliente.nome}"></c:out></td>
				<td class='cpfCell'><c:out value="${cliente.cpf}"></c:out></td>
				<td><a class='del' href='#'>Apagar</a></td>
				<td><a class='alt' href='#'>Alterar</a></td>
			</tr>
		</c:forEach>
	</table>
	</div>
</body>
</html>