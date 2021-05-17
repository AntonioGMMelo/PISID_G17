<?php
	include_once 'DatabaseConn.php';
		
	$email = $_POST['email'];
	$tipo = $_POST['tipo'];
	$nome = $_POST['nome'];
	$senha = $_POST['senha'];
	
	
	$sql = "CALL CriarUtilizador('$nome', '$email', '$tipo', '$senha');";

	$result = mysqli_query($conn, $sql);

	if ($result){

		header("Location: ../Administrador/criarUtilizador.php?signup=sucess");
	}else {
		header("Location: ../Administrador/criarUtilizador.php?signup=insucess");
	}


		
?>