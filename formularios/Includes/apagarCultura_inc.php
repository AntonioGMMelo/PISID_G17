<?php
	session_start();
	include_once 'DatabaseConn.php';
		
	
	$nome = $_POST['nome'];


	
	$dbConn = unserialize($_SESSION['dbConn']);
	$conn = mysqli_connect($dbConn->getDBServerName(), $dbConn->getDBUserName(), $dbConn->getDBPassWord(), $dbConn->getDBName() );
	
	$sql = "DELETE  FROM cultura where cultura.NomeCultura = '$nome'";

	$result = mysqli_query($conn, $sql);

	if ($result){

		header("Location: ../Administrador/apagarCultura.php?apagarCultura=sucess");
	}else {
		header("Location: ../Administrador/apagarCultura.php?apagarCultura=insucess");
	}


		
?>