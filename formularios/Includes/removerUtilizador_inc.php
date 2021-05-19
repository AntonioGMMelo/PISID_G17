// 
	// include_once 'DatabaseConn.php';
		
	// $email = $_POST['email'];
	
	
	// //$sql = " DELETE FROM `utilizador` WHERE `utilizador`.`EmailUtilizador` = '$email'";
	// $sql = "CALL RemoverUtilizador('$email');";
	// $result = mysqli_query($conn, $sql);


	// if ($result){

		// header("Location: ../Administrador/removerUtilizador.php?removed=sucess");
	// }else {
		// header("Location: ../Administrador/removerUtilizador.php?removed=insucess");
	// }

	



		
// 


<?php
	session_start();
	include_once 'DatabaseConn.php';
		
	$email = $_POST['email'];
	
	
	//$sql = " DELETE FROM `utilizador` WHERE `utilizador`.`EmailUtilizador` = '$email'";

	$dbConn = unserialize($_SESSION['dbConn']);
	$conn = mysqli_connect($dbConn->getDBServerName(), $dbConn->getDBUserName(), $dbConn->getDBPassWord(), $dbConn->getDBName() );
	$sql = "CALL RemoverUtilizador('$email');";
	$result = mysqli_query($conn, $sql);


	if ($result){

		header("Location: ../Administrador/removerUtilizador.php?removed=sucess");
	}else {
		header("Location: ../Administrador/removerUtilizador.php?removed=insucess");
	}

	



		
?>