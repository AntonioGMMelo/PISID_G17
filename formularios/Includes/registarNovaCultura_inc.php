<?php
	session_start();
	include_once 'DatabaseConn.php';
		
	$nome = $_POST['nome'];
	$IDZona = $_POST['IDZona'];
	$estado = $_POST['estado'];
	$minTemp = $_POST['minTemp'];
	$maxTemp = $_POST['maxTemp'];
	$minHumid = $_POST['minHumid'];
	$maxHumid = $_POST['maxHumid'];
	$minLuz = $_POST['minLuz'];
	$maxLuz = $_POST['maxLuz'];


	
	$dbConn = unserialize($_SESSION['dbConn']);
	$conn = mysqli_connect($dbConn->getDBServerName(), $dbConn->getDBUserName(), $dbConn->getDBPassWord(), $dbConn->getDBName() );
	if (!$conn) {
  		die("Connection failed: " . mysqli_connect_error());
	}
	
	$sql = "INSERT INTO parametrocultura(MinTemp, MaxTemp, MinHumid, MaxHumid, MinLuz, MaxLuz) VALUES($minTemp, $maxTemp, $minHumid, $maxHumid, $minLuz, $maxLuz) ;";

	$result = mysqli_query($conn, $sql);

	if ($result){

		$last_id = mysqli_insert_id($conn);

		$sql2 = "INSERT INTO cultura(NomeCultura, Estado, Utilizador_ID, Zona_ID, parametroCultura_ID) VALUES('$nome', '$estado', null, '$IDZona', $last_id) ;";

		$result2 = mysqli_query($conn, $sql2);
		if ($result2){

			header("Location: ../Administrador/registarNovaCultura.php?registarCultura=sucess");
		}else{
			$sql3 = "DELETE from parametrocultura order by parametroCultura_ID desc limit 1 ;";
			$result3 = mysqli_query($conn, $sql3);

			header("Location: ../Administrador/registarNovaCultura.php?registarCultura=insucess");
		}

	}else {
		header("Location: ../Administrador/registarNovaCultura.php?registarCultura=insucess");
	}


		
?>