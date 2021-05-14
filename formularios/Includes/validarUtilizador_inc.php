<?php
	include_once 'DatabaseConn.php';
		
	$email = $_POST['email'];
	$senha = $_POST['senha'];
	
	
	$sql = " SELECT NomeUtilizador FROM `utilizador` WHERE EmailUtilizador='$email' ";
	$result = mysqli_query($conn, $sql);
	$resultCheck = mysqli_num_rows($result);

	

	if($resultCheck > 0){
		$getTipoUtilizador= " SELECT * FROM `utilizador` WHERE EmailUtilizador='$email' ";

		$result2=mysqli_query($conn, $getTipoUtilizador);
		if($result2->num_rows > 0){
			$row = $result2->fetch_assoc();
			$tipoUtilizador = $row["TipoUtilizador"];
			$Utilizador_ID = $row["Utilizador_ID"];

			if($tipoUtilizador == "Investigador"){
				header("Location: ../Investigador/menuInvestigador.php?ID=" . "$Utilizador_ID" );
			}else if($tipoUtilizador == "administradorAplicacao"){
				header("Location: ../Administrador/menuAdministrador.php");
			}else{
				header("Location: ../index.php?login=insucess ");
			}
		}
	}else {
		header("Location: ../index.php?login=insucess" );
	}

 



		
?>