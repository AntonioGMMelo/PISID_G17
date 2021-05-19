<!DOCTYPE html>
<html>
	<head>
		<title>Registar Cultura</title>
		<meta charset="utf-8">

		<link rel="stylesheet" type="text/css" href="../estilo.css">
		<link rel = "stylesheet" type="text/css" href="../Investigador/css/alterarParametrosCultura.css">
		<link rel="stylesheet" type="text/css" href="./css/criarUtilizador.css">	</head>
	</head>
	<body >

		<h2>Registar Nova Cultura</h2>
		
		<div >

			

			<form action="../Includes/registarNovaCultura_inc.php" method="POST">

				<fieldset>

					<legend>Registar Cultura</legend>

					<div>
						<label for="nome">nome da Cultura:</label>
						<input  type="text" name="nome" id="nome" required / >
					</div>

			

					<div>
						<label for="tipo">ID Zona </label>
						<select name="IDZona" id="IDZona">

						    <option> Z1 </option>
						    <option> Z2 </option>
						    
					    
						</select>
					
					</div>

					
					<div>
						<label for="estado">Estado </label>
						<select name="estado" id="estado">

						    <option> 1 </option>
						    <option> 0 </option>
						    
					    
						</select>
					
					</div>


					<div>
						<label for="minTemp">minTemp:</label>
						<input  type="number", min="-100.00", max="100.00" name="minTemp" id="minTemp" >
					</div>
					<div>
						<label for="maxTemp">maxTemp:</label>
						<input  type="number", min="-100.00", max="100.00" name="maxTemp" id="maxTemp" >
					</div>

					<div>
						<label for="minHumid">minHumid:</label>
						<input  type="number", min="-100.00", max="100.00" name="minHumid" id="minHumid" >
					</div>
					<div>
						<label for="maxHumid">maxHumid:</label>
						<input  type="number", min="-100.00", max="100.00" name="maxHumid" id="maxHumid" >
					</div>
					
					<div>
						<label for="minLuz">minLuz:</label>
						<input  type="number", min="-100.00", max="100.00" name="minLuz" id="minLuz" >
					</div>
					<div>
						<label for="maxLuz">maxLuz:</label>
						<input  type="number", min="-100.00", max="100.00" name="maxLuz" id="maxLuz" >
					</div>
					
 

				</fieldset>

				<?php

				if(isset($_GET['registarCultura']) && $_GET['registarCultura'] == 'insucess'){ ?>

				
					<div class="text-danger">
						Cultura já existe
					</div>
				

				<?php } ?>



				<div>
					<button  type="submit" name="submit" >Registar</button>
				</div>
			</form>

		</div>


				<?php

				if(isset($_GET['registarCultura']) && $_GET['registarCultura'] == 'sucess'){ ?>

				
					<div class="text-danger">
						Cultura registada com exito!
					</div>
				

				<?php } ?>



	</body>
</html>