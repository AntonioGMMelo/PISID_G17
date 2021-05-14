<!DOCTYPE html>
<html>
	<head>
		<title>Cultura Dados</title>
		<meta charset="utf-8">
		<link rel="stylesheet" type="text/css" href="../estilo.css">
		<link rel="stylesheet" type="text/css" href="./css/dadosCultura.css">
	</head>
	<body >

		<h2>Dados da Cultura</h2>

		<div id="container">
		<?php

		

		include_once '../Includes/DatabaseConn.php';
			
		$cultura_ID = $_GET['Cultura_ID'];
		
		
		$sql = " SELECT * FROM cultura where Cultura_ID = $cultura_ID ;";
		//$sql = "CALL SelecionarCultura('$cultura_ID');";

		$result = mysqli_query($conn, $sql);


		$resultCheck = mysqli_num_rows($result);

		$cultura = array();
		

		if($resultCheck > 0){
			while($row = mysqli_fetch_assoc($result)){
				$cultura=$row;
			
				
			}
		}


		echo "<div class='cultura'>";
		echo "<p>ID da cultura</p>";
		echo "<p>" . $cultura['Cultura_ID'] . "</p>";
		echo "<p>" . "Nome da cultura"  . "</p>";
		echo "<p>" . $cultura['NomeCultura'] . "</p>";
		echo "<p>" . "Estado da cultura" . "</p>";
		echo "<p>" . $cultura['Estado'] . "</p>";
		echo "<p>" . "ID do utilizador" . "</p>";
		echo "<p>" . $cultura['Utilizador_ID'] . "</p>";
		echo "<p>" . "ID da zona da cultura" . "</p>";
		echo "<p>" . $cultura['Zona_ID'] . "</p>";
		echo "<p>" . "ID dos parametros da cultura" . "</p>";
		echo "<p>" . $cultura['ParametroCultura_ID'] . "</p>";
		echo "</div>";

		echo "<hr/>";
		$paramCult_ID = $cultura['ParametroCultura_ID'];

		$sql = " SELECT * FROM parametroCultura where ParametroCultura_ID = $paramCult_ID ;";

		$result = mysqli_query($conn, $sql);


		$resultCheck = mysqli_num_rows($result);

		$paramCult = array();
		

		if($resultCheck > 0){
			while($row = mysqli_fetch_assoc($result)){
				$paramCult=$row;
			
				
			}
		}
		echo "<div class='cultura'>";
		echo "<p>" . "ID do Parametro da cultura" . "</p>";
		echo "<p>" . $paramCult['ParametroCultura_ID'] . "</p>";
		echo "<p>" . "Temperatura mínima"  . "</p>";
		echo "<p>" . $paramCult['MinTemp'] . "</p>";
		echo "<p>" .  "Temperatura Máxima" . "</p>";
		echo "<p>" .  $paramCult['MaxTemp'] . "</p>";
		echo "<p>" . "Húmidade Mínima" . "</p>";
		echo "<p>" .  $paramCult['MinHumid'] . "</p>";
		echo "<p>" .  "Húmidade Máxima" . "</p>";
		echo "<p>" .  $paramCult['MaxHumid'] . "</p>";
		echo "<p>" . "Luz Mínima" . "</p>";
		echo "<p>" . $paramCult['MinLuz'] . "</p>";
		echo "<p>" . "Luz Máxima";
		echo "<p>" .  $paramCult['MaxLuz'] . "</p>";
		echo "</div>";

		echo "<div class ='buttons'>";
		$link = "alterarParametrosCultura.php?paramCult_ID=" . $paramCult_ID . "&Cultura_ID=" . $cultura_ID ."";
		echo "<a  id ='help' href = '$link'> alterar parametros</a>  <br/>";


		$link = "verAlertasCultura.php?Cultura_ID=" . $cultura_ID ."";
		echo "<a id='help2' href = '$link'> alertas</a>";

		echo "<div>";
		
		?>		
		</div>
	</body>
</html>