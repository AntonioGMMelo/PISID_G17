<?php
	// include_once 'DatabaseConn.php';
		
	// $email = $_POST['email'];
	// $senha = $_POST['senha'];
	
	
	// $sql = " SELECT NomeUtilizador FROM `utilizador` WHERE EmailUtilizador='$email' ";
	// $result = mysqli_query($conn, $sql);
	// $resultCheck = mysqli_num_rows($result);

	

	// if($resultCheck > 0){
		// $getTipoUtilizador= " SELECT * FROM `utilizador` WHERE EmailUtilizador='$email' ";

		// $result2=mysqli_query($conn, $getTipoUtilizador);
		// if($result2->num_rows > 0){
			// $row = $result2->fetch_assoc();
			// $tipoUtilizador = $row["TipoUtilizador"];
			// $Utilizador_ID = $row["Utilizador_ID"];

			// if($tipoUtilizador == "Investigador"){
				// header("Location: ../Investigador/menuInvestigador.php?ID=" . "$Utilizador_ID" );
			// }else if($tipoUtilizador == "administradorAplicacao"){
				// header("Location: ../Administrador/menuAdministrador.php");
			// }else{
				// header("Location: ../index.php?login=insucess ");
			// }
		// }
	// }else {
		// header("Location: ../index.php?login=insucess" );
	// }
	
	
	session_start();
	include_once 'DatabaseConn.php';
		
	$email = $_POST['email'];
	$senha = $_POST['senha'];

	// $dbServerName = "localhost";
	// $dbUserName = "$email";
	// $dbPassWord = "$senha";
	// $dbName = "estufadb";

	// $conn = mysqli_connect($dbServerName, $dbUserName, $dbPassWord, $dbName );

	$dbConn = new DatabaseConn();
	
	$dbConn->setDBUserName($email);
	$dbConn->setDBPassWord($senha);
	$conn = mysqli_connect($dbConn->getDBServerName(), $dbConn->getDBUserName(), $dbConn->getDBPassWord(), $dbConn->getDBName() );

	
	if (!$conn) {
  		header("Location: ../index.php?login=insucess" );
	}

	$_SESSION['dbConn'] = serialize($dbConn);
	echo "Connected successfully";

	
		
	
	//$sql = " SELECT * FROM `utilizador` WHERE EmailUtilizador='$email' ";
	$sql = "CALL SelecionarUtilizador('$email');";
	$result = mysqli_query($conn, $sql);
	$resultCheck = mysqli_num_rows($result);

	$utilizador = array();
	if($resultCheck > 0){
		while($row = mysqli_fetch_assoc($result)){
			$utilizador = $row;
		
		}
		$tipoUtilizador = $utilizador["TipoUtilizador"];
		$Utilizador_ID = $utilizador["Utilizador_ID"];

		if($tipoUtilizador == "Investigador"){
			header("Location: ../Investigador/menuInvestigador.php?ID=" . "$Utilizador_ID" );
		}else if($tipoUtilizador == "administradorAplicacao"){
			header("Location: ../Administrador/menuAdministrador.php");
		}else{
			header("Location: ../index.php?login=insucess ");
		}
	
	}else {
		header("Location: ../index.php?login=insucess" );
	}

 



		
?>