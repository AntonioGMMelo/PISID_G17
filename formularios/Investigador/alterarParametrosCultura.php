<?php
	session_start();
?>
<!DOCTYPE html>
<html>
	<head>
		<title>Alterar Parametros</title>
		<meta charset="utf-8">

		<link rel="stylesheet" type="text/css" href="../estilo.css">
		<link rel="stylesheet" type="text/css" href="./css/alterarParametrosCultura.css">

	</head>
	<body >

		<h2>Introduza os novos parametros para a cultura</h2>

		<div id = "container"	>	
		<?php

		

			include_once '../Includes/DatabaseConn.php';
	
			$dbConn = unserialize($_SESSION['dbConn']);

		
			$conn = mysqli_connect($dbConn->getDBServerName(), $dbConn->getDBUserName(), $dbConn->getDBPassWord(), $dbConn->getDBName() );

			
				
			$paramCult_ID = $_GET['paramCult_ID'];

			$cultura_ID = $_GET['Cultura_ID'];
			//echo $paramCult_ID;

			$sql = "CALL SelecionarParametroCultura($paramCult_ID);";

			$result = mysqli_query($conn, $sql);
			

			$resultCheck = mysqli_num_rows($result);

			$paramCult = array();
			

			if($resultCheck > 0){
				while($row = mysqli_fetch_assoc($result)){
					$paramCult=$row;
			
				
			}
		}

	
		$prevMinTemp = $paramCult['MinTemp'];
		$prevMaxTemp = $paramCult['MaxTemp'];
		$prevMinHumid = $paramCult['MinHumid'];
		$prevMaxHumid = $paramCult['MaxHumid'];
		$prevMinLuz = $paramCult['MinLuz'];
		$prevMaxLuz = $paramCult['MaxLuz'];


		
		?>
			
		

		
		?>

		<div >

			

			<form action="../Includes/updateParametrosCultura_inc.php?paramCultura_ID=" method="POST">

				<fieldset>

					<legend>Alterar Dados:</legend>
					<div class ="parametros">			
					<div>
						<label for="minTemp">Min Temperatura:</label>
						<input  type="number",  step="0.1"  min="0", max="100.00" name="minTemp" id="minTemp" placeholder=" <?php echo $prevMinTemp; ?> ">
					</div>
					<div>
						<label for="maxTemp">MAX Temperatura:</label>
						<input  type="number",  step="0.1"  min="0", max="100.00" name="maxTemp" id="maxTemp" placeholder=" <?php echo $prevMaxTemp; ?>">
					</div>

					<div>
						<label for="minHumid">Min Humidade:</label>
						<input  type="number",  step="0.1"  min="0", max="200.00" name="minHumid" id="minHumid" placeholder=" <?php echo $prevMinHumid; ?>" >
					</div>
					<div>
						<label for="maxHumid">MAX Humidade:</label>
						<input  type="number",  step="0.1"  min="0", max="200.00" name="maxHumid" id="maxHumid" placeholder=" <?php echo $prevMaxHumid; ?>" >
					</div>
					
					<div>
						<label for="minLuz">Min Luz:</label>
						<input  type="number",  step="0.1"  min="0", max="100.00" name="minLuz" id="minLuz" placeholder=" <?php echo $prevMinLuz; ?>">
					</div>
					<div>
						<label for="maxLuz">MAX Luz:</label>
						<input  type="number", step="0.1" min="0", max="100.00" name="maxLuz" id="maxLuz" placeholder=" <?php echo $prevMaxLuz; ?>" >
					</div>

					</div>

					<input type="hidden" name="paramCult_ID" value="<?php echo $paramCult_ID ?>">
					<input type="hidden" name="cultura_ID" value="<?php echo $cultura_ID ?>">

					




					
				</fieldset>

				<div id = "but">
					<button  type="submit" name="submit" >Registar</button>
				</div>
			</form>

		</div>
		

		</div>

	</body>
</html>