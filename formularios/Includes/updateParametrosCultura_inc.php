<?php
	// include_once 'DatabaseConn.php';
		
	// $minTemp = $_POST['minTemp'];
	// $maxTemp = $_POST['maxTemp'];
	// $minHumid = $_POST['minHumid'];
	// $maxHumid = $_POST['maxHumid'];
	// $minLuz = $_POST['minLuz'];
	// $maxLuz = $_POST['maxLuz'];
	// $paramCult_ID = $_POST['paramCult_ID'];
	// $cultura_ID = $_POST['cultura_ID'];
	
	// //$sql = " UPDATE parametroCultura SET MinTemp = $minTemp, MaxTemp = $maxTemp, MinHumid = $minHumid, MaxHumid = $maxHumid, MinLuz = $minLuz, MaxLuz = $maxLuz WHERE parametroCultura_ID = $paramCult_ID; ";

	// $sql = "CALL AlterarParametros('$minTemp', '$maxTemp', '$minHumid', '$maxHumid', '$minLuz', '$maxLuz', '$paramCult_ID');";

	 // mysqli_query($conn, $sql);

	 // header("Location: ../Investigador/dadosCultura.php?Cultura_ID=" . $cultura_ID . "" );
	 
	 
	 
	 session_start();
	include_once 'DatabaseConn.php';
		
	$minTemp = $_POST['minTemp'];
	$maxTemp = $_POST['maxTemp'];
	$minHumid = $_POST['minHumid'];
	$maxHumid = $_POST['maxHumid'];
	$minLuz = $_POST['minLuz'];
	$maxLuz = $_POST['maxLuz'];
	$paramCult_ID = $_POST['paramCult_ID'];
	$cultura_ID = $_POST['cultura_ID'];
	
	$dbConn = unserialize($_SESSION['dbConn']);
	$conn = mysqli_connect($dbConn->getDBServerName(), $dbConn->getDBUserName(), $dbConn->getDBPassWord(), $dbConn->getDBName() );

	$sql = "CALL AlterarParametros('$minTemp', '$maxTemp', '$minHumid', '$maxHumid', '$minLuz', '$maxLuz', '$paramCult_ID');";

	 mysqli_query($conn, $sql);

	 header("Location: ../Investigador/dadosCultura.php?Cultura_ID=" . $cultura_ID . "" );





		
?>