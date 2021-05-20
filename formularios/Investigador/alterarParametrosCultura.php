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

		

		
			
		$paramCult_ID = $_GET['paramCult_ID'];

		$cultura_ID = $_GET['Cultura_ID'];
		echo $paramCult_ID;


		
		?>

		<div >

			

			<form action="../Includes/updateParametrosCultura_inc.php?paramCultura_ID=" method="POST">

				<fieldset>

					<legend>Alterar Dados:</legend>
					<div class ="parametros">			
					<div>
						<label for="minTemp">Min Temperatura:</label>
						<input  type="number",  step="0.1"  min="0", max="100.00" name="minTemp" id="minTemp">
					</div>
					<div>
						<label for="maxTemp">MAX Temperatura:</label>
						<input  type="number",  step="0.1"  min="0", max="100.00" name="maxTemp" id="maxTemp" >
					</div>

					<div>
						<label for="minHumid">Min Humidade:</label>
						<input  type="number",  step="0.1"  min="0", max="200.00" name="minHumid" id="minHumid" >
					</div>
					<div>
						<label for="maxHumid">MAX Humidade:</label>
						<input  type="number",  step="0.1"  min="0", max="200.00" name="maxHumid" id="maxHumid" >
					</div>
					
					<div>
						<label for="minLuz">Min Luz:</label>
						<input  type="number",  step="0.1"  min="0", max="100.00" name="minLuz" id="minLuz" >
					</div>
					<div>
						<label for="maxLuz">MAX Luz:</label>
						<input  type="number", step="0.1" min="0", max="100.00" name="maxLuz" id="maxLuz" >
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