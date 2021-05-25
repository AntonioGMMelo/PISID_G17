<?php
	session_start();
	include_once 'DatabaseConn.php';
		
	
	$nome = $_POST['nome'];
	$email = $_POST['email'];

	
	$dbConn = unserialize($_SESSION['dbConn']);
	$conn = mysqli_connect($dbConn->getDBServerName(), $dbConn->getDBUserName(), $dbConn->getDBPassWord(), $dbConn->getDBName() );

	if (!$conn) {
  		die("Connection failed: " . mysqli_connect_error());
	}

	$sql = "SELECT * FROM utilizador WHERE EmailUtilizador = '$email' ;";
	$result = mysqli_query($conn, $sql);
	$resultCheck = mysqli_num_rows($result);

	$investigador = array();
	

	if($resultCheck > 0){
		while($row = mysqli_fetch_assoc($result)){
			$investigador = $row;
		
			
		}
	}
	$id_investigador = $investigador['Utilizador_ID'];


	$sql = "UPDATE cultura SET cultura.Utilizador_ID = $id_investigador where cultura.NomeCultura = '$nome' ;";

	$result = mysqli_query($conn, $sql);

	if ($result){

		header("Location: ../Administrador/culturaAssocInvestigador.php?assoc=sucess");
	}else {
		header("Location: ../Administrador/culturaAssocInvestigador.php?assoc=insucess");
	}


		
?>