<?php
	session_start();
	include_once '../Includes/DatabaseConn.php';
?>

<!DOCTYPE html>
<html>
	<head>
		<title>Apagar Cultura</title>
		<meta charset="utf-8">

		<link rel="stylesheet" type="text/css" href="../estilo.css">
		<link rel = "stylesheet" type="text/css" href="../Investigador/css/alterarParametrosCultura.css">
		<link rel="stylesheet" type="text/css" href="./css/criarUtilizador.css">	</head>
		</head>
	<body >

		<h2>Apagar Cultura</h2>
		
		
		<div id="container">

			

			<form action="../Includes/apagarCultura_inc.php" method="POST">

				<fieldset>

					<legend>Apagar Cultura</legend>

				
			

					<div class="parametros">
						<div>
						<label for="nome">Nome da Cultura </label>
						<select name="nome" id="nome">

							<?php
								$dbConn = unserialize($_SESSION['dbConn']);
								$conn = mysqli_connect($dbConn->getDBServerName(), $dbConn->getDBUserName(), $dbConn->getDBPassWord(), $dbConn->getDBName() );
								if (!$conn) {
							  		die("Connection failed: " . mysqli_connect_error());
								}

								$sql = "SELECT * FROM cultura ;";
								$result = mysqli_query($conn, $sql);
								$numCulturas = mysqli_num_rows($result);

								$culturas = array(array());
								

								if($numCulturas > 0){
									while($row = mysqli_fetch_assoc($result)){
										$culturas[]=$row;
									
										
									}
								}

								for($i = 1; $i <= $numCulturas; $i++){
									$nomeCultura = $culturas[$i]['NomeCultura'];
									echo "<option> $nomeCultura </option>";

								}


							?>

						    
					    
						</select>
						</div>
					
					</div>

					
					
 

				</fieldset>

				<?php

				if(isset($_GET['apagarCultura']) && $_GET['apagarCultura'] == 'insucess'){ ?>

				
					<div id="error" class="text-danger">
						Cultura não existe
					</div>
				

				<?php } ?>



				<div>
					<button  type="submit" name="submit" >Apagar</button>
				</div>
			</form>

		</div>


				<?php

				if(isset($_GET['apagarCultura']) && $_GET['apagarCultura'] == 'sucess'){ ?>

				
					<div id= "succes" class="text-danger">
						Cultura eliminada com exito!
					</div>
				

				<?php } ?>


	</body>
</html>