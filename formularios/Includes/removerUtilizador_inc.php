<?php
	include_once 'DatabaseConn.php';
		
	$email = $_POST['email'];
	
	
	//$sql = " DELETE FROM `utilizador` WHERE `utilizador`.`EmailUtilizador` = '$email'";
	$sql = "CALL RemoverUtilizador('$email');";
	$result = mysqli_query($conn, $sql);


	if ($result){

		header("Location: ../Administrador/removerUtilizador.php?removed=sucess");
	}else {
		header("Location: ../Administrador/removerUtilizador.php?removed=insucess");
	}

	



		
?>